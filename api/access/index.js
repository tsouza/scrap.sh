'use strict';

import { APIGateway } from "aws-sdk";

var apiGateway = new APIGateway();

class AccessService {

  create(auth, context) {
    apiGateway.createApiKey({
      name: auth.accessToken, description: auth.accessToken,
      enabled: true, stageKeys: [
        { restApiId: "3fp2gra6ia", stageName: "v1" }
      ]
    }, (err, response) => {
      if (err) return context.fail(err);
      context.succeed({
        AccountId: auth.AccountId,
        ApiKey: response.id
      });
    })
  }

  replace(auth, context) {
    var self = this;
    apiGateway.getApiKey({ apiKey: auth.ApiKey },
      (err, response) => {
        if (err) return context.fail(err);
        if (!response || response.name != auth.AccountId)
          return context.fail("FORBIDDEN");
        apiGateway.deleteApiKey({ apiKey: auth.ApiKey },
          (err) => err ? context.fail(err) : self.create(auth, context));
      });
  }
}

module.exports = new AccessService();
