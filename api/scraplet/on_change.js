'use strict';

import request from 'request';
import { promisifyAll } from 'bluebird';
import Dynamizer from 'dynamizer';

var decode = (obj) => Dynamizer().decode({ M: obj });

var http = promisifyAll(request.defaults({
  url: "http://backend.scrapsh.net/scraplet/on-change",
  json: true
}));

class OnChangeScrapletService {

  handle(event, context) {
    var oldImage = event.Records[0].dynamodb.OldImage,
        newImage = event.Records[0].dynamodb.NewImage;

    oldImage = oldImage ? decode(oldImage) : null;
    newImage = newImage ? decode(newImage) : null;

    http.postAsync({
      body: { OldImage: oldImage, NewImage: newImage },
      timestamp: context.getRemainingTimeInMillis() * 0.75
    })
    .then((response) => response.toJSON())
    .tap((response) => console.log(response))
    .spread((response) => response.statusCode == 200 ?
      context.succeed() : context.fail(response.body.error + ": " +
          response.body.exception + " - " +
          response.body.message))
    .catch((err) => context.fail(err));
  }

}

module.exports = new OnChangeScrapletService();
