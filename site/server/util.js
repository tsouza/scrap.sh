import crypto from 'crypto';

function hash(str) {
  var sha1sum = crypto.createHash("sha1");
  sha1.update(str);
  return crypto.digest("hex");
}

export default {
  hash: hash
}
