import express from 'express';
import passport from 'passport';

import github from './github';

var auth = express.Router();

github(passport, auth);

export default auth;
