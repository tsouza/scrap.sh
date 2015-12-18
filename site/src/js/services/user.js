'use strict';

import _ from "lodash";

function User($http, auth0) {
  'ngInject';

  var auth = !_.isEmpty(window.location.hash) && auth0.parseHash(window.location.hash),
      authenticated = auth && !_.isEmpty(auth);

  if (!authenticated)
    { return { authenticated: false }; }

  return _.extend(auth, {
    authenticated: true,
    getProfile: () => new Promise((resolve, reject) =>
      auth0.getProfile(auth.id_token, (err, profile) =>
        err ? reject(err) : resolve(profile)
      )
    )
  });
}

export default {
  name: "User",
  factory: true,
  fn: User
};
