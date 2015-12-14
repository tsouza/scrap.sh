var Firebase = require("firebase"),
    Fireproof = require("Fireproof");

var firebase = module.exports = new Fireproof(
  new Firebase("https://scrapsh.firebaseio.com");
);

firebase.authWithCustomToken("8hiWBIwZlzdujOZ4ZYMW6e5QUEau9Ij7EVfpIvtO");
