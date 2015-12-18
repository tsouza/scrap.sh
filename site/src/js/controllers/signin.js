'use strict';

function SigninCtrl($scope, auth0, scrapper) {
  'ngInject';

  $scope.login = (username, password, remember) => {
    scrapper.authorize().then(function() {
      console.log("teste");
    });
  };

  $scope.github = (remember) => {
    auth0.login({
      connection: "github"
    });
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
