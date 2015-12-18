import express from "express";

var router = express.Router();

import auth from './auth';
router.use("/auth", auth);

var v1 = express.Router();
v1.use("/v1", router);

export default v1;
