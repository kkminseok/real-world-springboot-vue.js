import axios, {AxiosResponse} from "axios";

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
    return axiosService.put('/api/user', {user}, {
        headers: {
            Authorization: "TOKEN " + currentToken,
            "Content-Type": `application/json`,
        }
    })
}

export { signUp, signIn, getCurrentUser, updateUser }