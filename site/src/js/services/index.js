'use strict';

import angular from 'angular';
const bulk = require('bulk-require');

const servicesModule = angular.module('app.services', []);

const services = bulk(__dirname, ['./**/!(*index|*.spec).js']);

Object.keys(services).forEach((key) => {
  let item = services[key];
  var name = item.name.split("");
  name[0] = name[0].toLowerCase();
  servicesModule.service(name.join(""), item.fn);
});

export default servicesModule;
