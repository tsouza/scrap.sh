'use strict';

import { DynamoDB, APIGateway } from "aws-sdk";
import { promisifyAll, resolve } from 'bluebird';
import Dynamizer from 'dynamizer';
import _ from 'lodash';

var encode = (obj) => Dynamizer().encode(obj).M;
var decode = (obj) => Dynamizer().decode({ M: obj });

var dynamodb = promisifyAll(new DynamoDB());

class UpdateScrapletService {

  handle(event, context) {
    dynamodb.getItemAsync({
      TableName: "scraplet",
      Key: {
        ApiKey: { S: event.ApiKey },
        Name: { S: event.Scraplet.Name }
      },
      ProjectionExpression: "#Status, Version",
      ExpressionAttributeNames: { "#Status": "Status" }
    })
    .tap((response) => {
      if (!response || !response.Item) throw "NOT_FOUND";
    })
    .then((response) => response.Item)
    .tap((scraplet) => {
      if (/PENDING_/.test(scraplet.Status.S)) throw "CONFLICT";
    })
    .then((scraplet) =>
      dynamodb.updateItemAsync({
        TableName: "scraplet",
        Key: {
          ApiKey: { S: event.ApiKey },
          Name: { S: event.Scraplet.Name }
        },
        UpdateExpression:
         "SET #Status = :pending_validation, " +
             "Script = :script " +
         "ADD Version :increment",
        ConditionExpression:
          "Version = :version AND " +
          "Script <> :script AND " +
          "#Status = :status",
        ExpressionAttributeNames: { "#Status": "Status" },
        ExpressionAttributeValues: {
          ":version": scraplet.Version,
          ":status": scraplet.Status,
          ":script": { S: event.Scraplet.Script },
          ":pending_validation": { S: "PENDING_VALIDATION" },
          ":increment": { N: '1' }
        }
      })
    )
    .then(() =>
      dynamodb.getItemAsync({
        TableName: "scraplet",
        Key: {
          ApiKey: { S: event.ApiKey },
          Name: { S: event.Scraplet.Name }
        }
      })
    )
    .tap((response) => {
      if (!response || !response.Item) throw "NOT_FOUND";
    })
    .then((response) => decode(response.Item))
    .then((scraplet) => _.omit(scraplet, "ApiKey"))
    .then((result) => context.succeed(result))
    .catch((err) => {
      if ("ConditionalCheckFailedException" === err.code)
        throw "CONFLICT";
      throw err;
    })
    .catch((err) => context.fail(err));
  }
}

module.exports = new UpdateScrapletService();
