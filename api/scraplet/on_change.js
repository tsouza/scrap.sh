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

    dispatchOnChange(oldImage, newImage, context)
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

function dispatchOnChange(oldImage, newImage, context) {
  return new Promise((resolve, reject) => {
    if (!newImage && oldImage.DeploymentID !== "$NOT_DEPLOYED")
        request({ method: "DELETE", url: "http://backend.scrapsh.net/scraplet/" + oldImage.DeploymentID },
            (err, response, body) => {
                if (err) return reject(err);
                return resolve([ response, body ]);
            });
    else if (newImage)
        request({ method: "POST", url: "http://backend.scrapsh.net/scraplet/" +
            [ newImage.ApiKey, newImage.Name, newImage.Status, newImage.Version ].
                join("/"), body: newImage.Script },
            (err, response, body) => {
               if (err) return reject(err);
               return resolve([ response, body ]);
            });

    else resolve({ statusCode: 200 });
  });
}
