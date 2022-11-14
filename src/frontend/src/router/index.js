import { createWebHistory, createRouter } from "vue-router";

const routes = [
    {
        path: "/",
        name: "Home",
        component: () => import(/* webpackChunkName "home" */ '@/views/TheHome.vue')
    },
    {
        path: "/setting",
        name: "Setting",
        component: () => import(/* webpackChunkName "inputTag" */ '@/views/TheSetting.vue')
    },
    {
        path: "/login",
        name: "Login",
        component: () => import(/* webpackChunkName "inputTag" */ '@/views/TheLogin.vue')
    },
    {
        path: "/signup",
        name: "SignUp",
        component: () => import(/* webpackChunkName "inputTag" */ '@/views/TheSignUp.vue')
    },
    {
        path: "/article",
        name: "Article",
        component: () => import(/* webpackChunkName "inputTag" */ '@/views/TheArticle.vue')
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;