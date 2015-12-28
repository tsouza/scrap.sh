'use script';

import { DynamoDB, Lambda } from "aws-sdk";
import { promisifyAll, resolve, map } from 'bluebird';
import _ from 'lodash';
import request from 'request-promise';

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
      ProjectionExpression: "#Status",
      ExpressionAttributeNames: {
        "#Status": "Status"
      }
    })
    .tap((response) => {
      if (!response || !response.Item ||
          !"DEPLOYED" === response.Item.Status.S)
          throw "CONFLICT";
    })
    .then((response) => response.Item)
    .then((scraplet) =>
      request.post({
        url: "http://backend.scrapsh.net/scraplet/invoke",
        json: event
      })
    )
    .spread((response, body) => {
      var result = body && JSON.parse(body);
      if (response.statusCode != 200)
        return context.fail(result);
      /*return lambda.invokePromise({
        FunctionName: "scrap-sh-scraplet_history",
        InvocationType: "Event",
        Payload: JSON.stringify({
          ApiKey: event.ApiKey,
          Name: event.Invoke.Name,
          Timestamp: now.toISOString(),
          Result: _(result)
            .pick("BytesProcessed", "BytesReceived")
            .merge({ StatusCode: response.StatusCode })
        })
      }).return(payload.Output);*/
    })
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
        ApiKey: { S: event.ApiKey },
        Name: { S: event.Name },
        Timestamp: { S: event.Timestamp },
        StatusCode: { N: event.Result.StatusCode + "" },
        Log: { S: logResult },
        BytesProcessed: { N: event.Result.BytesProcessed + "" },
        BytesReceived: { N: event.Result.BytesReceived + "" }
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
