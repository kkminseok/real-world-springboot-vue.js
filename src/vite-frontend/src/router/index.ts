// @ts-ignore
import { createWebHashHistory, createRouter } from "vue-router";

const routes = [
    {
        path: "/",
        name: "Home",
        component: () => import(/* webpackChunkName "home" */ '@/views/TheHome.vue')
    },
    {
        path: "/settings",
        name: "Settings",
        component: () => import(/* webpackChunkName "inputTag" */ '@/views/TheSetting.vue')
    },
    {
        path: "/login",
        name: "Login",
        component: () => import(/* webpackChunkName "inputTag" */ '@/views/TheLogin.vue')
    },
    {
        path: "/register",
        name: "Register",
        component: () => import(/* webpackChunkName "inputTag" */ '@/views/TheRegister.vue')
    },
    {
      path: "/article/:slug",
      name: "ArticleDetail",
      component: () => import('@/views/TheArticleDetail.vue'),
      props: true
    },
    {
        path: "/editor/",
        name: "ArticleEditor",
        component: () => import(/* webpackChunkName "inputTag" */ '@/views/TheArticle.vue')
    },
    {
        path: "/editor/:slug",
        name: "ArticleUpdateEditor",
        component: () => import(/* webpackChunkName "inputTag" */ '@/views/ArticleUpdate.vue'),
        props: true
    },
    {
        path: "/@:username",
        name: "Profile",
        component: () => import(/* webpackChunkName "inputTag" */ '@/views/TheProfile.vue'),
        props: true
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

export default router;