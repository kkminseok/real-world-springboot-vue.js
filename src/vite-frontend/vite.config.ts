import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import basicSsl from '@vitejs/plugin-basic-ssl'

// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    alias: [
      {find: '@', replacement: '/src'} ,
    ]
  },
  build:{
    outDir: "../main/resources/static",
  },
  //local
  server: {
    https: true,
    port: 4000,
    proxy:{
      '/': {
        target : "http://3.35.44.58:8080",
        rewrite: (path) => path.replace(/^\//,''),
        changeOrigin: true,
        secure: false
      }
    }
  },

  plugins: [vue(),basicSsl()]

})
