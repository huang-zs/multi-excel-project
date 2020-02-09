"use strict";
const merge = require("webpack-merge");
const prodEnv = require("./prod.env");

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  //本地服务
  BASE_URL: '"http://localhost:8081/multi-user-excel-system"',
  SERVER_IP_PORT: '"192.168.149.133:8083"',
  FILE_IP_PORT: '"192.168.149.133:8084"'
});
