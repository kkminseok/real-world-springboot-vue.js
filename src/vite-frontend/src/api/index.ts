import axios, {AxiosResponse} from "axios";

const axiosService = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
})


const signUp = async (user: object): Promise<AxiosResponse> => {
    return await axiosService.post('/api/users',{user});
}

const signIn = async (user: object): Promise<AxiosResponse> => {
    return await axiosService.post('/api/users/login',{user})
}

const getCurrentUser = async (): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return await axiosService.get('/api/user', {
        headers: {
            Authorization: "TOKEN " + currentToken
        }
    })
}

const updateUser = async (user: object): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return await axiosService.put('/api/user', {user}, {
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
    return await axiosService.post('/api/profiles/' + username + "/follow",{},{
        headers:{
            Authorization : "TOKEN " + currentToken,
            "Content-Type": `application/json`,
        }
    })
}

const unfollowUser = async (username: string | undefined): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return await axiosService.delete('/api/profiles/' + username + "/follow",{
        headers:{
            Authorization : "TOKEN " + currentToken,
        }
    })
}

const createArticle = async (article: object | undefined): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return await axiosService.post('/api/articles', { article },{
        headers :{
            Authorization : "TOKEN " + currentToken,
            "Content-Type": `application/json`,
        }
    })
}

const listArticles = async (): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    if(currentToken == null){
        return await axiosService.get('/api/articles');
    }else{
        return await axiosService.get('/api/articles',{
            headers:{
                Authorization: "TOKEN " + currentToken,
            }
        })
    }
}

const feedArticle = async (): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return await axiosService.get('/api/articles/feed',{
        headers:{
            Authorization: "TOKEN " + currentToken,
        }
    });
}

const getArticle = async (slug: string | undefined): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    if(currentToken == null){
        return await axiosService.get('/api/articles/' + slug);
    }else{
        return await axiosService.get('/api/articles/' + slug,{
            headers:{
                Authorization: "TOKEN " + currentToken,
            }
        });
    }

}

const addCommentToArticle = async (slug: string | undefined, comment: object): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return await axiosService.post('/api/articles/' + slug + '/comments',{
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
        return await axiosService.get('/api/articles/' + slug + "/comments");
    }else {
        return await axiosService.get('/api/articles/' + slug + "/comments",{
            headers:{
                Authorization: "TOKEN " + currentToken,
            }
        })
    }
}

const favoriteArticle = async (slug: string | undefined): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return await axiosService.post('/api/articles/' + slug + '/favorite',{},
        {
            headers:{
                Authorization : "TOKEN " + currentToken,
                "Content-Type": `application/json`,
            }
        });
}

const unFavoriteArticle = async (slug: string | undefined): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return await axiosService.delete('/api/articles/' + slug + '/favorite',{
        headers:{
            Authorization : "TOKEN " + currentToken,
            "Content-Type": `application/json`,
        }
    });
}

const getTags = async (): Promise<AxiosResponse> => {
    return await axiosService.get('/api/tags');
}


export { signUp, signIn,
         getCurrentUser, updateUser,
         getProfile, followUser,
         createArticle, feedArticle,
         listArticles,
         unfollowUser, getArticle,
         addCommentToArticle, getCommentsFromArticle,
         favoriteArticle, unFavoriteArticle,
         getTags
        }