import {IAbstractFilterCondition} from "./IAbstractFilterCondition";
import {ISortField} from "./ISortField";

/**
 * Интерфейс объединяющий в себя параметры пагинации, сортировки и фильтрации
 */
export interface ISortFilterPageableRequest {

    /**
     * Номер страницы
     */
    pageNumber: number;

    /**
     * Колличество элементов на странице
     */
    pageSize: number;

    /**
     * Список сортировок
     */
    sortFieldList: ISortField[];

    /**
     * Список фильтраций
     */
    filterConditionList: IAbstractFilterCondition[];
}
