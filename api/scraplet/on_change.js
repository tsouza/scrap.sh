'use strict';

import request form 'request-promise';
import Dynamizer from 'dynamizer';

var decode = (obj) => Dynamizer().decode({ M: obj });

class OnChangeScrapletService {

  handle(event, context) {
    var oldImage = event.Records[0].dynamodb.OldImage,
        newImage = event.Records[0].dynamodb.NewImage;

    oldImage = oldImage ? decode(oldImage) : null;
    newImage = newImage ? decode(newImage) : null;

    request.post({
      url: "http://backend.scrapsh.net/scraplet/on-change",
      json: { OldImage: oldImage, NewImage: newImage }
    })
    .then((result) => context.succeed(result))
    .fail((err) => context.fail(err));
  }

}

module.exports = new OnChangeScrapletService();
