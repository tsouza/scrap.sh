var _ = require("lodash"),
    base = require("../lambda-config.json");

module.exports = [
  _.merge({}, base, {
    functionName: "scrap-sh-scraplet_create",
    handler: "create.handle"
  }),
  _.merge({}, base, {
    functionName: "scrap-sh-scraplet_update",
    handler: "update.handle"
  }),
  _.merge({}, base, {
    functionName: "scrap-sh-scraplet_invoke",
    handler: "invoke.handle"
  }),
  _.merge({}, base, {
    functionName: "scrap-sh-scraplet_on-change",
    handler: "on_change.handle"
  })
]
