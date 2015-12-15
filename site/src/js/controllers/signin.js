'use strict';

function SigninCtrl($scope, scrapper) {
  'ngInject';

  $scope.login = (username, password, remember) => {
    scrapper.authorize().then(function() {
      console.log("teste");
    });
  };

  $scope.github = (remember) => {
  };

  $scope.signup = (username, password, remember) => {
    scrapper.authorize().then(function() {
      console.log("teste");
    });
  };

}

export default {
  name: 'SigninCtrl',
  fn: SigninCtrl
};
