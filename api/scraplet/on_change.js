'use strict';

import { DynamoDB, APIGateway, S3, Lambda } from "aws-sdk";
import Promise from 'bluebird';
import Dynamizer from 'dynamizer';
import _ from 'lodash';
import Hub from "./hub";

var decode = (obj) => Dynamizer().decode({ M: obj });

var lambda = Promise.promisifyAll(new Lambda(), { suffix: "Promise" }),
    s3 = Promise.promisifyAll(new S3()),
    dynamodb = Promise.promisifyAll(new DynamoDB()),
    apiGateway = Promise.promisifyAll(new APIGateway());

var hub = Hub(lambda, dynamodb, apiGateway, s3);

class OnChangeScrapletService {

  handle(event, context) {
    var oldVal = event.Records[0].dynamodb.OldImage,
        newVal = event.Records[0].dynamodb.NewImage;

    oldVal = oldVal && decode(oldVal);
    newVal = newVal && decode(newVal);

    var stateChange =
      (oldVal ? oldVal.Status : "NULL") + "." +
      (newVal ? newVal.Status : "NULL");

    hub.emit(stateChange, oldVal, newVal,
      (err) => {
        if (err) return context.fail(err);
        context.succeed();
      });
  }

}

module.exports = new OnChangeScrapletService();
