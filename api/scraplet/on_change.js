'use strict';

import request from 'request';
import Promise from 'bluebird';
import Dynamizer from 'dynamizer';

var decode = (obj) => Dynamizer().decode({ M: obj });

class OnChangeScrapletService {

  handle(event, context) {
    var oldImage = event.Records[0].dynamodb.OldImage,
        newImage = event.Records[0].dynamodb.NewImage;

    oldImage = oldImage ? decode(oldImage) : null;
    newImage = newImage ? decode(newImage) : null;

    dispatchOnChange({ OldImage: oldImage, NewImage: newImage }, context)
      .spread((response, body) =>
        response.statusCode == 200 ?
          context.succeed() :
          context.fail(body ? body.error + ": " +
            body.exception + " - " +
            body.message : response.statusCode + ""))
      .catch((err) => context.fail(err));
  }

}

module.exports = new OnChangeScrapletService();


function dispatchOnChange(event, context) {
  return new Promise((resolve, reject) => {
    request.post({ url: "http://backend.scrapsh.net/scraplet/" + urlSuffix(),
      json: true, body: event,
      timestamp: context.getRemainingTimeInMillis() * 0.75
    }, function(err, response, body) {
      if (err) return reject(err);
      resolve([ response, body ]);
    })
  });

  function urlSuffix() {
    if (!event.NewImage)
      return "on-delete";
    return "on-change/" + event.NewImage.Status;
  }
}
