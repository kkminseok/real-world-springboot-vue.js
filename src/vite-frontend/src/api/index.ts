import axios, {AxiosResponse} from "axios";
import { useStore } from "vuex";

//TODO
//임시방편 watch로 바라보는게 더 좋다고함. watch로 감시하자. Token
const getToken = () => {
    let test = useStore().getters.getToken;
    console.log(test)
    return test;
}

const axiosService = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
})

const signUp = async (user: object): Promise<AxiosResponse> => {
    return axiosService.post('/api/users',{user});
}

const signIn = async (user: object): Promise<AxiosResponse> => {
    return axiosService.post('/api/users/login',{user})
}

const getCurrentUser = async (): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return axiosService.get('/api/user', {
        headers: {
            Authorization: "TOKEN " + currentToken
        }
    })
}

const updateUser = async (user: object): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    console.log(user);
    console.log(currentToken);
    return axiosService.put('/api/user', {user}, {
        headers: {
            Authorization: "TOKEN " + currentToken,
            "Content-Type": `application/json`,
        }
    })
}

export { signUp, signIn, getCurrentUser, updateUser }