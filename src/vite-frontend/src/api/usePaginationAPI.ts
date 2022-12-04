import { ref, Ref } from "@vue/reactivity";
import { listArticles } from "@/api/index";

import { usePagination } from "@/ts/usePagination";


export interface Todo {
    id: number;
    title: string;
}

export function usePaginationApi(
    currentPage: Ref<number>,
    rowsPerPage?: Ref<number>
) {
    const articleLists: Ref<Todo[]> = ref([]);

    const listsAreLoading = ref(false);

    const { paginatedArray, numberOfPages } = usePagination<Todo>({
        rowsPerPage,
        arrayToPaginate: articleLists,
        currentPage
    });

    const loadLists = async () => {
        listsAreLoading.value = true;
        try {
            const { data } = await listArticles();
            articleLists.value = data.articles;
            console.log(articleLists.value);
        } catch (err) {
            console.log(err);
        } finally {
            listsAreLoading.value = false;
        }
    };

    return {
        articleLists: paginatedArray,
        loadLists,
        listsAreLoading,
        numberOfPages
    };
}