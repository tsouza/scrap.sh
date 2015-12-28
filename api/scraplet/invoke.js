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
    request.post({
      url: "http://backend.scrapsh.net/scraplet/invoke",
      body: event, json: true, simple: false,
      resolveWithFullResponse: true,
      timeout: Math.floor(context.getRemainingTimeInMillis() * 0.75)
    })
    .then((response) => {
      var result = response.body;
      return lambda.invokePromise({
        FunctionName: "scrap-sh-scraplet_history",
        InvocationType: "Event",
        Payload: JSON.stringify({
          ApiKey: event.ApiKey,
          Name: event.Invoke.Name,
          RequestID: context.awsRequestId,
          Timestamp: now.toISOString(),
          StatusCode: response.statusCode,
          Result: response.statusCode == 200 ?
            _(result).omit("Output") : result
        })
      }).then(() =>
        200 == response.statusCode ?
          context.succeed(result.Output) :
          context.fail(result)
      );
    }).catch((err) => context.fail(err));
  }

  history(event, context) {
    dynamodb.putItemAsync({
      TableName: "scraplet-invoke-history",
      Item: {
        Key: { S: event.ApiKey + "-" + event.Name },
        ApiKey: { S: event.ApiKey },
        Name: { S: event.Name },
        RequestID: { S: event.RequestID },
        Timestamp: { S: event.Timestamp },
        StatusCode: { N: event.StatusCode + "" },
        BytesProcessed: { N: (event.Result.BytesProcessed || 0) + "" },
        BytesReceived: { N: (event.Result.BytesReceived || 0) + "" },
        Error: { M: event.StatusCode === 200 ? {} : event.Result  }
      }
    })
    .then((result) => context.succeed(result))
    .catch((err) => context.fail(err));
  }

}

module.exports = new InvokeScrapletService();
