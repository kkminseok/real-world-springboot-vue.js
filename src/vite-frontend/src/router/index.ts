import { createWebHashHistory, createRouter } from "vue-router";

const routes = [
    {
        path: "/",
        name: "Home",
        component: () => import(/* webpackChunkName "home" */ '../views/TheHome.vue')
    },
    {
        path: "/settings",
        name: "Settings",
        component: () => import(/* webpackChunkName "inputTag" */ '../views/TheSetting.vue')
    },
    {
        path: "/login",
        name: "Login",
        component: () => import(/* webpackChunkName "inputTag" */ '../views/TheLogin.vue')
    },
    {
        path: "/register",
        name: "Register",
        component: () => import(/* webpackChunkName "inputTag" */ '../views/TheRegister.vue')
    },
    {
        path: "/article",
        name: "Article",
        component: () => import(/* webpackChunkName "inputTag" */ '../views/TheArticle.vue')
    },
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

export default router;