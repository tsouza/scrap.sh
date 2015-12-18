'use strict';

import _ from "lodash";

function User($http, auth0, AppSettings) {
  'ngInject';

  var auth = !_.isEmpty(window.location.hash) && auth0.parseHash(window.location.hash),
      authenticated = auth && !_.isEmpty(auth);

  if (!authenticated)
    return { authenticated: false };

  return {
    authenticated: true,
    getProfile: () => {
      return new Promise((resolve, reject) => {
        auth0.getProfile(auth.id_token, (err, profile) => {
          if (err) return reject(err);
          auth.authenticated = true;
          auth.profile = profile;
          resolve(auth);
        });
      });
    }
  }
}

export default {
  name: "User",
  factory: true,
  fn: User
};
