let baseUrl = ''
switch (process.env.NODE_ENV) {
  case 'development':
    baseUrl = 'http://localhost:8081/multi-user-excel-system/' // 开发环境
    // baseUrl = 'http://118.190.156.144:8081/multi-user-excel-system/' // 开发环境
    break
  // case 'uat':
  //   baseUrl = 'http://localhost:8081/' // 测试环境
  //   break
  case 'production':
    baseUrl = 'http://localhost:8081/' // 生产环境
    break
}
export default baseUrl
