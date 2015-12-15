'use strict';

import Parse from "parse";

function OnConfig($stateProvider, $locationProvider,
          $urlRouterProvider, AppSettings) {
  'ngInject';

  Parse.initialize(AppSettings.parse.appId, AppSettings.parse.jsKey);

  $locationProvider.html5Mode(true);

  $stateProvider
  .state('signin', {
    url: '/',
    title: 'Sign in',
    templateUrl: 'signin.html'
  });

  $urlRouterProvider.otherwise('/');

}

export default OnConfig;
