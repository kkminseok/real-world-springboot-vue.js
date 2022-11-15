import { createApp } from 'vue'
import App from './App.vue'
import router from "./router";
import axios from 'axios';

axios.defaults.baseURL = 'http://3.35.44.58:8080/'
const app = createApp(App);
app.config.globalProperties.axios = axios;
app.use(router,axios);
app.mount('#app');

