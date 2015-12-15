import express from 'express';
import qs from "querystring";
import _ from "lodash";
import Parse from "parse/node";
import { v4 as uuid } from "uuid";

var auth = express.Router();

var githubClientId = 'd4c7947b24789348e700';
var githubClientSecret = '9f12ad24900e1969211f89e36e907cfb4d8b5b7a';

var githubRedirectEndpoint = 'https://github.com/login/oauth/authorize?';
var githubValidateEndpoint = 'https://github.com/login/oauth/access_token';
var githubUserEndpoint = 'https://api.github.com/user';

var TokenRequest = Parse.Object.extend("TokenRequest");
var TokenStorage = Parse.Object.extend("TokenStorage");

var restrictedAcl = new Parse.ACL();
restrictedAcl.setPublicReadAccess(false);
restrictedAcl.setPublicWriteAccess(false);

auth.get("/", (req, res) => {
  var tokenRequest = new TokenRequest();
  tokenRequest.setACL(restrictedAcl);

  tokenRequest.save(null, { useMasterKey: true })
    .then((obj) => res.status(200).json({
          redirect: githubRedirectEndpoint + qs.stringify({
            client_id: githubClientId,
            state: obj.id
          }
      }),
      (err) => res.status(500).json({ message: "AUTH_REQUEST_FAILED" }));
});

auth.get("/callback", (req, res) => {
  var data = req.query, token;

  if (!(data && data.code && data.state))
      return res.sendStatus(500);

  var query = new Parse.Query(TokenRequest);

  return query.get(data.code, { useMasterKey: true })
    .then((token) => token.destroy())
    .then(() => lookupGitHubAccessCode(data.code))
    .then((githubData) => {
      if (githubData && githubData.access_token && githubData.token_type) {
        token = githubData.access_token;
        return lookupGitHubUserDetails(token);
      } else
        return Parse.Promise.error("INVALID_ACCESS_REQUEST");
    })
    .then((userData) => {
      if (userData && userData.login && userData.id) {
        return upsertGitHubUser(token, userData);
      } else
        return Parse.Promise.error("INVALID_USERDATA");
    })
    .then(reply, reply);

    function reply(object) {
      res.set("Content-Type", "text/html");
      res.send([
          '<script type="text/javascript">',
            'window.opener.authCallback({ success: ', object.code === null,
              ', result: "', object.code || object, '"});',
              'window.close();',
          '</script>'
      ].join(""))
    }
});

function lookupGitHubAccessCode(code) {
 return Parse.Cloud.httpRequest({
   method: 'POST',
   url: githubValidateEndpoint,
   headers: {
     'Accept': 'application/json',
     'User-Agent': 'Parse.com Cloud Code'
   },
   body: qs.stringify({
      client_id: githubClientId,
      client_secret: githubClientSecret,
      code: code
   })
 }).then((response) => response.data);
}

function lookupGitHubUserDetails(accessToken) {
  return Parse.Cloud.httpRequest({
    method: 'GET',
    url: githubUserEndpoint,
    params: { access_token: accessToken },
    headers: {
      'User-Agent': 'Parse.com Cloud Code'
    }
  }).then((response) => response.data);
}

function upsertGitHubUser(accessToken, githubData) {
  var query = new Parse.Query(TokenStorage);
  query.equalTo('githubId', githubData.id);
  query.ascending('createdAt');
  var password;
  return query.first({ useMasterKey: true }).then((tokenData) => {
    if (!tokenData)
      return newGitHubUser(accessToken, githubData);

    var user = tokenData.get('user');
    return user.fetch({ useMasterKey: true }).then((user) => {
      if (accessToken !== tokenData.get('accessToken')) {
        tokenData.set('accessToken', accessToken);
      }
      return tokenData.save(null, { useMasterKey: true });
    }).then((obj) => {
  		password = uuid();
  		user.setPassword(password);
  		return user.save();
    }).then((user) => Parse.User.logIn(user.get('username'), password))
      .then((user) => Parse.Promise.as(user));
  });
}

function newGitHubUser(accessToken, githubData) {
  var user = new Parse.User();
  user.set("username", uuid());
  user.set("password", uuid());
  return user.signUp().then((user) => {
    var ts = new TokenStorage();
    ts.set('githubId', githubData.id);
    ts.set('githubLogin', githubData.login);
    ts.set('accessToken', accessToken);
    ts.set('user', user);
    ts.setACL(restrictedAcl);
    return ts.save(null, { useMasterKey: true });
  }).then((tokenStorage) => upsertGitHubUser(accessToken, githubData));
}
