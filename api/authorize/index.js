/* jshint node: true */
'use strict';

var Firebase = require("firebase"),
    AWS = require("aws-sdk"),
    Promise = require("bluebird");

var apigateway = new AWS.APIGateway();
var ref;

exports.handler = function (auth, context) {

    if (!auth.uid) {
      console.log("no uid");
      return context.fail("FORBIDDEN");
    }

    firebaseRef().then(function (ref) {
        ref.child("users").child(auth.uid)
            .transaction(function (user) {
               if (!user) {
                    console.log("user not found");
                    context.fail("FORBIDDEN");
                    return;
               }
               switch (user.apiKey) {
                    case "$AUTHORIZING":
                       context.fail("CONFLICT");
                       return;
                    case null: case undefined:
                        apigateway.createApiKey({
                            name: auth.uid, enabled: true,
                            stageKeys: [{
                              restApiId: "3fp2gra6ia",
                              stageName: "v1" }]
                        }, function (err, key) {
                            if (err) return context.fail(err);
                            key = { apiKey: key.id };
                            ref.child("users").child(auth.uid)
                               .update(key, function (err) {
                                    if (!err) return context.succeed(response(key));
                                    apigateway.deleteApiKey(key, function () {
                                        context.fail(err);
                                    });
                                });

                        });
                        user.apiKey = "$AUTHORIZING";
                        return user;
                    default:
                        context.succeed(response(user));
                        return;
                }
            }, function(err) {
                if (err) return context.fail(err);
            });
    }).catch(function(err) {
      context.fail(err);
    });

};

function firebaseRef() {
    if (ref) return ref;
    return (ref = new Promise(function(resolve, reject) {
        var firebase = new Firebase("https://scrapsh.firebaseio.com");
        firebase.authWithCustomToken("8hiWBIwZlzdujOZ4ZYMW6e5QUEau9Ij7EVfpIvtO",
            function(err) {
                if (err) return reject(err);
                resolve(firebase);
            });
    }));
}

function response(obj) {
    return { uid: obj.uid, apiKey: obj.apiKey };
}

firebaseRef().then(function(ref) {
  ref.child("users").child("github:122435")
    .transaction(function(user) {
      console.log("teste " + user);
      return;
    }, function() {});
});
