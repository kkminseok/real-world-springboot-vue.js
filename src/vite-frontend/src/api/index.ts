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

const getProfile = async (username: string | undefined): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    if(currentToken == null){
        return axiosService.get('/api/profiles/' + username);
    }else {
        return axiosService.get('/api/profiles/' + username,{
            headers:{
                Authorization: "TOKEN " + currentToken,
            }
        })
    }
}

const followUser = async (username: string | undefined): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return axiosService.post('/api/profiles/' + username + "/follow",{},{
        headers:{
            Authorization : "TOKEN " + currentToken,
            "Content-Type": `application/json`,
        }
    })
}

const unfollowUser = async (username: string | undefined): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return axiosService.delete('/api/profiles/' + username + "/follow",{
        headers:{
            Authorization : "TOKEN " + currentToken,
        }
    })
}

const getArticle = async (slug: string | undefined): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    if(currentToken == null){
        return axiosService.get('/api/articles/' + slug);
    }else{
        return axiosService.get('/api/articles/' + slug,{
            headers:{
                Authorization: "TOKEN " + currentToken,
            }
        });
    }

}

const addCommentToArticle = async (slug: string | undefined, comment: object): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return axiosService.post('/api/articles' + slug + '/comments',{
        comment
    },{
        headers:{
            Authorization : "TOKEN " + currentToken,
            "Content-Type": `application/json`,
        }
    });
}

const getCommentsFromArticle = async (slug: string | undefined): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    if(currentToken == null){
        return axiosService.get('/api/articles/' + slug + "/comments");
    }else {
        return axiosService.get('/api/articles/' + slug + "/comments",{
            headers:{
                Authorization: "TOKEN " + currentToken,
            }
        })
    }
}

const favoriteArticle = async (slug: string | undefined): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return axiosService.post('/api/articles/' + slug + '/favorite',{},
        {
            headers:{
                Authorization : "TOKEN " + currentToken,
                "Content-Type": `application/json`,
            }
        });
}

const unFavoriteArticle = async (slug: string | undefined): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return axiosService.delete('/api/articles/' + slug + '/favorite',{
        headers:{
            Authorization : "TOKEN " + currentToken,
            "Content-Type": `application/json`,
        }
    });
}


export { signUp, signIn,
         getCurrentUser, updateUser,
         getProfile, followUser,
         unfollowUser, getArticle,
         addCommentToArticle, getCommentsFromArticle,
         favoriteArticle, unFavoriteArticle
        }