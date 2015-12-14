import mongoose from "mongoose";

mongoose.connect("mongodb://localhost/test");

import Account from './Account';

export default {
  Account: Account
}
