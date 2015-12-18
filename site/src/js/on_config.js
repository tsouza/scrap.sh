'use strict';

function OnConfig($stateProvider, $locationProvider,
          $urlRouterProvider, AppSettings) {
  'ngInject';

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
