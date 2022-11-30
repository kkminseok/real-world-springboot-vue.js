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
  },

  plugins: [vue(),basicSsl()]

})
