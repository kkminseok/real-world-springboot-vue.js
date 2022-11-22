import { createApp } from 'vue'
import App from './App.vue'
import router from "./router";
import store from "./store";
import '@/assets/css/font.css'
import '@/assets/css/main.css'

const app = createApp(App);
app.use(router).use(store).mount('#app');