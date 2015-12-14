'use strict';

function SigninCtrl($scope, scrapper, firebase) {
  'ngInject';

  $scope.login = (username, password, remember) => {
    scrapper.authorize().then(function() {
      console.log("teste");
    });
  };

  $scope.github = (remember) => {
    firebase.authWithOAuthPopup('github')
        .then((auth) => {
          return firebase.child("users").child(auth.uid)
            .transaction((user) => user || auth)
            .return(auth); })
        .then((auth) => {
            return scrapper.authorize({ uid: auth.uid })
              .then((scrapperAuth) => {
                  return firebase.child("users").child(auth.uid)
                    .update({ apiKey: scrapperAuth.apiKey });
              });
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
