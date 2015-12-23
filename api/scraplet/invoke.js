'use script';

import { DynamoDB, Lambda } from "aws-sdk";
import { promisifyAll, resolve } from 'bluebird';
import _ from 'lodash';

var encode = (obj) => Dynamizer().encode(obj).M;

var dynamodb = promisifyAll(new DynamoDB()),
    lambda = promisifyAll(new Lambda(), { suffix: "Promise" });


class InvokeScrapletService {

  handle(event, context) {
    var now = new Date();
    dynamodb.getItemAsync({
      TableName: "scraplet",
      Key: {
        ApiKey: { S: event.ApiKey },
        Name: { S: event.Invoke.Name }
      },
      ConsistentRead: true,
      ProjectionExpression: "#Status, Script",
      ExpressionAttributeNames: {
        "#Status": "Status"
      }
    })
    .tap((response) => {
      if (!response || !response.Item ||
          !"VALID" === response.Item.Status.S)
          throw "CONFLICT";
    })
    .then((response) => response.Item)
    .then((scraplet) =>
      lambda.invokePromise({
        FunctionName: "scrap-sh-scraplet_execute",
        InvocationType: "RequestResponse",
        LogType: "Tail",
        Payload: JSON.stringify({
          Key: event.ApiKey + "-" + event.Invoke.Name,
          Script: scraplet.Script.S,
          Data: event.Invoke.Data,
          Metadata: event.Invoke.Metadata
        })
      })
    )
    .tap((result) =>
      lambda.invokePromise({
        FunctionName: "scrap-sh-scraplet_history",
        InvocationType: "Event",
        LogType: "Tail",
        Payload: JSON.stringify({
          ApiKey: event.ApiKey,
          Name: event.Invoke.Name,
          Timestamp: now.toISOString(),
          Result: _.pick(result,
            "StatusCode", "LogResult")
        })
      })
    )
    .then((result) => JSON.parse(result.Payload))
    .then((result) => context.succeed(result))
    .catch((err) => context.fail(err));
  }

  history(event, context) {
    var logResult = new Buffer(event.Result.LogResult, "base64").toString();
    var stats = parseStats(logResult);
    dynamodb.putItemAsync({
      TableName: "scraplet-invoke-history",
      Item: _.merge(stats, {
        Key: { S: event.ApiKey + "-" + event.Name },
        Timestamp: { S: event.Timestamp },
        ResultCode: { N: event.Result.StatusCode + "" },
        Log: { S: logResult }
      })
    })
    .then((result) => context.succeed(result))
    .catch((err) => context.fail(err));
  }

}

module.exports = new InvokeScrapletService();

function parseStats(logResult) {
  var match = /\s+Duration:\s+([0-9\.]+?)\s+ms\s+Billed Duration:\s+([0-9\.]+?)\s+ms\s+Memory Size:\s+([0-9\.]+?)\s+MB\s+Max Memory Used:\s+([0-9\.]+?)\s+MB\s+/
    .exec(logResult);
  return {
    Duration: { N: match[1] },
    BilledDuration: { N: match[2] },
    MemorySize: { N: match[3] },
    MaxMemoryUsed: { N: match[4] }
  }
}

/*

{
  "StatusCode": 200,
  "LogResult": "START RequestId: f563319d-a9a3-11e5-a2ee-7d98e0f7e61a Version: $LATEST\nEND RequestId: f563319d-a9a3-11e5-a2ee-7d98e0f7e61a\nREPORT RequestId: f563319d-a9a3-11e5-a2ee-7d98e0f7e61a\tDuration: 132.67 ms\tBilled Duration: 200 ms \tMemory Size: 512 MB\tMax Memory Used: 129 MB\t\n",
  "Payload": {
    "test2": "testing"
  }
}*/
