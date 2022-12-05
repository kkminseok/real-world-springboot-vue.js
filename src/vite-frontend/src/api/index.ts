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

const updateArticle = async (article: object | undefined, slug: string): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return await axiosService.put('/api/articles/' + slug, { article },{
        headers :{
            Authorization : "TOKEN " + currentToken,
            "Content-Type": `application/json`,
        }
    })
}

const listArticles = async (): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    if(currentToken == null){
        return await axiosService.get('/api/articles?limit=1000&offset=0');
    }else{
        return await axiosService.get('/api/articles?limit=1000&offset=0',{
            headers:{
                Authorization: "TOKEN " + currentToken,
            }
        })
    }
}

const listArticlesByUsername = async (author: string): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    if(currentToken == null){
        return await axiosService.get('/api/articles?author=' + author);
    }else{
        return await axiosService.get('/api/articles?author=' + author,{
            headers:{
                Authorization: "TOKEN " + currentToken,
            }
        })
    }
}

const listArticlesByFavorite = async (author: string): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    if(currentToken == null){
        return await axiosService.get('/api/articles?favorited=' + author);
    }else{
        return await axiosService.get('/api/articles?favorited=' + author,{
            headers:{
                Authorization: "TOKEN " + currentToken,
            }
        })
    }
}

const feedArticle = async (): Promise<AxiosResponse> => {
    let currentToken = localStorage.getItem("token");
    return await axiosService.get('/api/articles/feed?limit=1000&offset=0',{
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
         listArticles, listArticlesByUsername,
         unfollowUser, getArticle,
         addCommentToArticle, getCommentsFromArticle,
         favoriteArticle, unFavoriteArticle,
         listArticlesByFavorite, updateArticle,
         getTags
        }