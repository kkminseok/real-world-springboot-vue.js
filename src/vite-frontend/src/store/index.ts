import axios from "axios";
import { createStore } from 'vuex';


const url = import.meta.env.VITE_BASE_URL;

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
    },
    actions: {
        REGISTER({commit},{user}){
            return axios.post(url+'/api/users',{
                user
            }).then(response =>{
                console.log(response)
            })
        }
    },
})
