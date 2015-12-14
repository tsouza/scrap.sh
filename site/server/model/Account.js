'use scrict';

import { Schema } from 'mongoose';
import mongoose from "mongoose";

import { hash } from '../util';

var AccountSchema = new Schema({
  email: { type: String, lowercase: true,
    trim: true, required: true,
    index: true },
  displayName: { type: String, trim: true, required: true },
  createdAt: { type: Date, default: () => new Date() },
  authentication: {
    provider: String,
    token: String
  }
})

var Account = mongoose.model("Account", AccountSchema);

AccountSchema.pre("save", (next) => {
  var account = this;
  if ("local" === account.authentication.provider)
    account.authentication.token = hash(account.authentication.token);
  next();
});

AccountSchema.statics.findLocalAccount = (email, password) => {
  return Account.findOne({
    email: email,
    authentication: {
      provider: "local",
      token: hash(password)
    }
  });
}

export default Account;
