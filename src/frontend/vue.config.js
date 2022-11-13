const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: "../main/resources/static",

  devServer: {
    port: 4000,
    proxy:{
      '/': {
        target : "http://3.35.44.58:8080",
        pathRewrite: {'^/':''},
        changeOrigin: true,
        secure: false
      }
    }
  }

})
