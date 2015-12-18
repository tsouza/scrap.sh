var _ = require("lodash"),
    base = require("../lambda-config.json");

module.exports = [
  _.merge({}, base, {
    functionName: "scrap-sh-access_create",
    handler: "index.create"
  }),
  _.merge({}, base, {
    functionName: "scrap-sh-access_replace",
    handler: "index.replace"
  })
]
