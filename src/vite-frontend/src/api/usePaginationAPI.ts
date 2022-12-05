import { ref, Ref } from "@vue/reactivity";
import {listArticles, feedArticle, listArticlesByUsername, listArticlesByFavorite} from "@/api/index";

import { usePagination } from "@/ts/usePagination";


export interface Article {
    slug: string,
    title: string,
    description: string,
    favorited: boolean,
    favoritesCount: number,
    createdAt: string,
    author: {
        username: string,
        image: string
    }
}

export function usePaginationApi(
    currentPage: Ref<number>,
    rowsPerPage?: Ref<number>
) {
    const articleLists: Ref<Article[]> = ref([]);
    const listsAreLoading = ref(false);
    const isEmpty = ref(false);

    const { paginatedArray, numberOfPages } = usePagination<Article>({
        rowsPerPage,
        arrayToPaginate: articleLists,
        currentPage
    });

    const feedLists = async () => {
        listsAreLoading.value = true;
        isEmpty.value = false;
        try{
            const { data } = await feedArticle();
            articleLists.value = data.articles;
            if(data.articlesCount == 0){
                isEmpty.value = true;
            }
        } catch (err) {
            console.log(err);
        } finally {
            listsAreLoading.value = false;
        }
    }

    const loadLists = async () => {
        listsAreLoading.value = true;
        isEmpty.value = false;
        try {
            const { data } = await listArticles();
            articleLists.value = data.articles;
            if(data.articlesCount == 0){
                isEmpty.value = true;
            }
        } catch (err) {
            console.log(err);
        } finally {
            listsAreLoading.value = false;
        }
    };

    const loadMyArticles = async (author: string) => {
        listsAreLoading.value = true;
        isEmpty.value = false;
        try{
            const { data } = await listArticlesByUsername(author);
            articleLists.value = data.articles;
            if(data.articlesCount == 0){
                isEmpty.value = true;
            }
        }catch (err){
            console.log(err);
        }finally {
            listsAreLoading.value = false;
        }
    }

    const loadFavoriteArticles = async (author: string) => {
        listsAreLoading.value = true;
        isEmpty.value = false;
        try{
            const { data } = await listArticlesByFavorite(author);
            articleLists.value = data.articles;
            if(data.articlesCount == 0){
                isEmpty.value = true;
            }
        }catch (err){
            console.log(err);
        }finally {
            listsAreLoading.value = false;
        }
    }


    return {
        articleLists: paginatedArray,
        loadLists,
        feedLists,
        loadMyArticles,
        loadFavoriteArticles,
        listsAreLoading,
        isEmpty,
        numberOfPages
    };
}