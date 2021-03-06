"use strict";
const merge = require("webpack-merge");
const prodEnv = require("./prod.env");

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  //本地服务
  BASE_URL: '"http://localhost:8081/multi-user-excel-system"',
  WEBSOCKET_URL: '"ws://localhost:8081/multi-user-excel-system/ws/asset/"',
  SERVER_IP_PORT: '"127.0.0.1:8082"',
  AES_IV:'"ZS20200207ADDAES"'
  //服务器
  // BASE_URL: '"http://118.190.156.144:8081/multi-user-excel-system/"',
  // WEBSOCKET_URL: '"ws://118.190.156.144:8081/multi-user-excel-system/ws/asset/"'
});
