var _ = require("lodash"),
    base = require("../lambda-config.json");

module.exports = [
  _.merge({}, base, {
    functionName: "scrap-sh-scraplet_create",
    handler: "index.create"
  })
]
