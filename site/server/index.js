'use strict';

import express from 'express';
import restify from 'express-restify-mongoose';

import model from "./model";

var router = express.Router();

for (var modelName in model)
  restify.serve(router, model[modelName], {
    prefix: "/model", version: "",
    lowercase: true, plural: false
  });


//import auth from "./api/auth";



//router.use("/auth", auth);

/*router.get("/", (req, res, next) => {
  res.sendStatus(200);
});*/


var v1 = express.Router();
v1.use("/v1", router);

export default v1;
