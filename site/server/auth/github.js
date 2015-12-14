'use strict';

import Account from '../model';
import { Strategy as GithubStrategy } from "passport-github";

function setup(passport, router) {
    passport.use(new GithubStrategy({
      clientID: "d4c7947b24789348e700",
      clientSecret: "9f12ad24900e1969211f89e36e907cfb4d8b5b7a",
      callbackURL: (/production/i.test(process.env.NODE_ENV) ?
          "https://scrap.sh" : "http://localhost:3000"
        ) + "/api/v1/auth/github/callback"
    }), (accessToken, refreshToken, profile, done) => {
      Account.findOneAndUpdate(
          { email: profile.email },
          { name: profile.displayName,
            authorization: {
              provider: "github",
              token: accessToken
            }
          }, { new: true, upsert: true }
        ).then((account) => {
          done(null, account);
        }).catch(done);
    });

    router.get("/github", passport.authenticate("github"));
    router.get("/github/callback", passport.authenticate("github", {
      failureRedirect: "/"
    }))


}
