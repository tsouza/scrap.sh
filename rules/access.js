function (user, context, callback) {
  user.user_metadata = user.user_metadata || {};
  var apiKey = user.user_metadata.apiKey;
  if (!apiKey || "$REPLACE$" === apiKey)
    requestAccessKey(!apiKey ? "POST" : "DELETE", function(err, apiKey) {
      if (err) return callback(err, user, context);
      user.user_metadata.apiKey = apiKey;
      auth0.users.updateUserMetadata(user.user_id, user.user_metadata)
        .then(function() {
          callback(null, user, context);
        })
        .catch(function(err) {
          callback(err, user, context);
        });
    });
  else callback(null, user, context);

  function requestAccessKey(method, callback) {
    request({ method: method,
      url: "https://api.scrap.sh/v1/access",
      headers: {
        "x-api-key": "EKBQstsyLq7Vuz1IUR1UNavCrmthEmHf5ISEamUE",
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ accessToken: user.user_id })
    }, function(err, resp, body) {
        if (err || resp.statusCode !== 200)
          return callback(err || new Error(resp.statusCode));
        callback(null, JSON.parse(body).apiKey);
    });
  }
}
