var _ = require("lodash"),
    base = require("../lambda-config.json");

module.exports = _(base).merge({
    functionName: "scrap-sh-authorize"
}).value();