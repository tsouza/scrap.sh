'use strict';

import { DynamoDB, APIGateway } from "aws-sdk";
import { promisifyAll, resolve } from 'bluebird';
import Dynamizer from 'dynamizer';
import _ from 'lodash';

var encode = (obj) => Dynamizer().encode(obj).M;

var dynamodb = promisifyAll(new DynamoDB()),
    apiGateway = promisifyAll(new APIGateway());

class CreateScrapletService {

  handle(event, context) {
    makeEntry(event)
      .then((scraplet) => dynamodb.putItemAsync({
        TableName: "scraplet",
        Item: encode(scraplet),
        ConditionExpression:
          "attribute_not_exists (ApiKey) AND " +
          "attribute_not_exists (#Name)",
        ExpressionAttributeNames: { "#Name": "Name" }
      }).then(() => context.succeed(_(scraplet).omit("apiKey"))))
      .catch((err) => {
        if ("ConditionalCheckFailedException" === err.code)
          return context.fail("CONFLICT");
        context.fail(err);
      });

  }
}

module.exports = new CreateScrapletService();

function makeEntry(event) {
  return resolve({
    ApiKey: event.apiKey,
    Name: event.scraplet.name,
    Script: event.scraplet.script,
    Status: "NEW",
    CreatedAt: (new Date()).toISOString(),
    Version: 1
  });
}
