'use strict';

import Auth0Impl from 'auth0-js';

function Auth0(AppSettings) {
  'ngInject';

  return new Auth0Impl(AppSettings.auth0);
}

export default {
  name: "Auth0",
  fn: Auth0
};
