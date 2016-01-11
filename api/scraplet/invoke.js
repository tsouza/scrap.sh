'use strict';

import { DynamoDB, Lambda } from "aws-sdk";
import { promisifyAll, resolve, map } from 'bluebird';
import Promise from 'bluebird';
import _ from 'lodash';
import request from 'request';
import Dynamizer from 'dynamizer';

var encode = (obj) => Dynamizer().encode(obj).M;

var dynamodb = promisifyAll(new DynamoDB()),
    lambda = promisifyAll(new Lambda(), { suffix: "Promise" });


class InvokeScrapletService {

  handle(event, context) {
    var now = new Date();
    invoke(event, context)
      .spread((response, body) =>
        lambda.invokePromise({
          FunctionName: "scrap-sh-scraplet_history",
          InvocationType: "Event",
          Payload: JSON.stringify({
            ApiKey: event.ApiKey,
            Name: event.Name,
            RequestID: context.awsRequestId,
            Timestamp: now.toISOString(),
            StatusCode: response.statusCode,
            Result: response.statusCode == 200 ?
              _(body).omit("Output") : (body || {})
            })
          }).return([response, body])
        ).spread((response, body) =>
          200 == response.statusCode ?
            context.succeed(body.Output) :
            context.fail(body ? body.error + ": " +
              body.exception + " - " +
              body.message : response.statusCode + "")
        ).catch((err) => context.fail(err));
  }

  history(event, context) {
    dynamodb.putItemAsync({
      TableName: "scraplet-invoke-history",
      Item: encode({
        RequestID: event.RequestID,
        ApiKey: event.ApiKey,
        Name: event.Name,
        Timestamp: event.Timestamp,
        StatusCode: event.StatusCode,
        BytesProcessed: event.Result.ProcessedBytes || 0,
        BytesReceived: event.Result.ReceivedBytes || 0,
        Error: event.StatusCode === 200 ? {} : event.Result
      })
    })
    .then((result) => context.succeed(result))
    .catch((err) => context.fail(err));
  }

}

module.exports = new InvokeScrapletService();


function invoke(event, context) {
  return new Promise((resolve, reject) => {
    request.post({ url: "http://backend.scrapsh.net/scraplet/invoke",
      json: true, body: event,
      timeout: context.getRemainingTimeInMillis() * 0.75
    }, function(err, response, body) {
      if (err) return reject(err);
      resolve([ response, body ]);
    })
  });
}
