'use strict';

function Scrapper($http, AppSettings) {
  'ngInject';

  const service = {};
  const endpoint = (path) => AppSettings.scrapperApiUrl + path;

  service.authorize = (authorization) => {
    return $http.post(endpoint("authorize"), authorization);
  };

  return service;

}

export default {
  name: "Scrapper",
  fn: Scrapper
};
