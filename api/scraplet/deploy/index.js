/* jshint node: true */
'use strict';

var Firebase = require("firebase"), AWS = require("aws-sdk"),
    Fireproof = require("fireproof"), Promise = require("bluebird");

var lambda = Promise.promisify(new AWS.Lambda()),
    firebase;

exports.handler = function(deploy, context) {

    firebaseRef()
      .then(function(ref) { return ref.child("scraplets").child(deploy.apiKey); })
      .then(function(ref) {
        return ref.transaction(function(scraplet) {
          deployLambdaFunction(scraplet === null, deploy)
            .then(function(functioName) {
              return ref.update({
                    status: "INITIALIZING",
                    updatedAt: new Date(),
                    lastError: null
                }).then(function() {
                  return lambda.invoke({
                    FunctionName: functioName,
                    InvocationType: "Event",
                    Payload: JSON.stringify({ "$INIT$": true })
                  }).then(function() {
                    context.succeed();
                    return;
                  });
                });
            }).catch(function(err) {
              return ref.update({
                status: "NOT_DEPLOYED",
                updatedAt: new Date(),
                lastError: err
              }).finally(function() {
                return context.fail("INTERNAL_SERVER_ERROR");
              });
            });

          return {
            status: "DEPLOYING",
            lastError: null,
            updatedAt: new Date(),
            script: deploy.data.scraplet
          };
        });
    }).catch(function(err) {
      context.fail(err);
    });

    function firebaseRef() {
        return firebase || (firebase =
          (new Fireproof(new Firebase("https://scrapsh.firebaseio.com/")))
            .authWithCustomToken("8hiWBIwZlzdujOZ4ZYMW6e5QUEau9Ij7EVfpIvtO")
        );
    }

    function deployLambdaFunction(isNew, deploy) {
      var functionName = "scraplet." + deploy.apiKey + "." + deploy.data.name;
      if (isNew)
        return lambda.createFunction({
          FunctionName: functionName,
          Handler: "sh.scrapper.scraplet.ScrapperService::execute",
          Role: "arn:aws:iam::700122456994:role/lambda_basic_execution",
          Runtime: "java8",
          MemorySize: 512,
          Publish: true,
          Timeout: 3,
          Code: {
            S3Bucket: "scrap.sh",
            S3Key: "codebase/scraplet/latest.zip"
          }
        }).return(functionName);

      return lambda.updateFunctionCode({
        FunctionName: functionName,
        S3Bucket: "scrap.sh",
        S3Key: "codebase/scraplet/latest.zip",
        Publish: true
      }).return(functionName);
    }
};
