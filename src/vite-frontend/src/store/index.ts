import axios from "axios";
import { createStore } from 'vuex';

export default createStore({
    state: {
        token: localStorage.getItem("token") || '',
        username: localStorage.getItem("username") || '',
    },
    mutations: {
        setUsername(state, username){
            state.username = username;
        },
        setToken(state, token){
            state.token = token;
        },
        getToken(state){
            return state.token
        }
    },
    actions: {
        LOGIN({commit}, user){
                commit("setUsername", user.username);
                commit("setToken", user.token);
                localStorage.setItem("username", user.username);
                localStorage.setItem("token", user.token);
            },
        },
    }
)
