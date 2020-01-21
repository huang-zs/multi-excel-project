// https://eslint.org/docs/user-guide/configuring

module.exports = {
  // 表面该文件问根文件
  root: true,
  parserOptions: {
    parser: "babel-eslint"
  },
  env: {
    browser: true
  },
  extends: [
    "plugin:vue/essential"
    // 'eslint:recommended',
    // 'standard'
  ],
  // required to lint *.vue files
  plugins: [
    "vue" //帮助检测.vue文件的<template>和<script>
  ],
  // add your custom rules here
  rules: {
    // allow async-await
    "generator-star-spacing": "off",
    // allow debugger during development
    "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off",
    //强制使用单引号
    quotes: ["error", "single"],
    //强制不使用分号结尾
    semi: ["error", "never"]
  }
};
