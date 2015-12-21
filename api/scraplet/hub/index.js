import { AsyncEmitter } from 'async-glob-events';

export default (lambda, dynamodb, apiGateway, s3) => {
  var hub = new AsyncEmitter();

  ["new"]
  .forEach((state) => {
    var listener = require("./" + state).default;
    var handler = listener.handler(lambda, dynamodb, apiGateway, s3);
    hub.on(listener.on, (oldVal, newVal, callback) => {
      try {
        handler(oldVal, newVal)
          .then((result) => callback(null, result))
          .catch((err) => callback(err));
      } catch (e) {
        callback(e);
      }
    });
  })

  return hub;
}
