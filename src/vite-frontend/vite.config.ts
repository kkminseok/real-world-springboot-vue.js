import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

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
  server: {
    port: 4000,
    proxy:{
      '/': {
        target : "http://3.35.44.58:8080",
        //{'^/':''},
        rewrite: (path) => path.replace(/^\//,''),
        changeOrigin: true,
        secure: false
      }
    }
  },

  plugins: [vue()]

})
