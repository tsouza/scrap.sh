'use strict';

import { Lambda, DynamoDB, APIGateway } from "aws-sdk";
import { promisifyAll } from 'bluebird';
import Dynamizer from 'dynamizer';
import _ from 'lodash';

var encode = (obj) => Dynamizer().encode(obj).M;

var dynamodb = promisifyAll(new DynamoDB()),
    apiGateway = promisifyAll(new APIGateway());

class ScrapletService {

  create(event, context) {
    apiGateway.getApiKeyAsync({ apiKey: event.apiKey })
      .tap((apiKey) => { if (!apiKey) { throw "FORBIDDEN"; } })
      .then((apiKey) => ({
        accountId: apiKey.name,
        name: event.scraplet.name,
        script: event.scraplet.script,
        status: "NEW",
        createdAt: (new Date()).toISOString(),
        updatedAt: (new Date()).toISOString()
      }))
      .then((scraplet) => dynamodb.putItemAsync({
        TableName: "scraplet",
        Item: encode(scraplet),
        Expected: {
          accountId: { Exists: false },
          name: { Exists: false }
        }
      }).then(() => context.succeed(_(scraplet).omit("accountId"))))
      .catch((err) => {
        if ("ConditionalCheckFailedException" === err.code)
          return context.fail("CONFLICT");
        context.fail(err);
      });
  }

  deploy(event, context) {

  }
}

module.exports = new ScrapletService();

/*
{
  "Records": [
    {
      "eventID": "1",
      "eventVersion": "1.0",
      "dynamodb": {
        "Keys": {
          "Id": {
            "N": "101"
          }
        },
        "NewImage": {
          "Message": {
            "S": "New item!"
          },
          "Id": {
            "N": "101"
          }
        },
        "StreamViewType": "NEW_AND_OLD_IMAGES",
        "SequenceNumber": "111",
        "SizeBytes": 26
      },
      "awsRegion": "us-west-2",
      "eventName": "INSERT",
      "eventSourceARN": "arn:aws:dynamodb:us-west-2:account-id:table/ExampleTableWithStream/stream/2015-06-27T00:48:05.899",
      "eventSource": "aws:dynamodb"
    },
    {
      "eventID": "2",
      "eventVersion": "1.0",
      "dynamodb": {
        "OldImage": {
          "Message": {
            "S": "New item!"
          },
          "Id": {
            "N": "101"
          }
        },
        "SequenceNumber": "222",
        "Keys": {
          "Id": {
            "N": "101"
          }
        },
        "SizeBytes": 59,
        "NewImage": {
          "Message": {
            "S": "This item has changed"
          },
          "Id": {
            "N": "101"
          }
        },
        "StreamViewType": "NEW_AND_OLD_IMAGES"
      },
      "awsRegion": "us-west-2",
      "eventName": "MODIFY",
      "eventSourceARN": "arn:aws:dynamodb:us-west-2:account-id:table/ExampleTableWithStream/stream/2015-06-27T00:48:05.899",
      "eventSource": "aws:dynamodb"
    },
    {
      "eventID": "3",
      "eventVersion": "1.0",
      "dynamodb": {
        "Keys": {
          "Id": {
            "N": "101"
          }
        },
        "SizeBytes": 38,
        "SequenceNumber": "333",
        "OldImage": {
          "Message": {
            "S": "This item has changed"
          },
          "Id": {
            "N": "101"
          }
        },
        "StreamViewType": "NEW_AND_OLD_IMAGES"
      },
      "awsRegion": "us-west-2",
      "eventName": "REMOVE",
      "eventSourceARN": "arn:aws:dynamodb:us-west-2:account-id:table/ExampleTableWithStream/stream/2015-06-27T00:48:05.899",
      "eventSource": "aws:dynamodb"
    }
  ]
}
*/
