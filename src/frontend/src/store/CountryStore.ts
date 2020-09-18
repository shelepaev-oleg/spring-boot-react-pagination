import {flow, observable} from "mobx";
import {IPagination} from "../utils/sortFilter/IPagination";
import {ICountry} from "../interface/ICountry";
import {ISortFilterPageableRequest} from "../utils/sortFilter/ISortFilterPageableRequest";
import {getCountryPage} from "../api/rest";
import {ISortField} from "../utils/sortFilter/ISortField";
import {SORT_DIRECTION} from "../utils/sortFilter/sortDirectionEnum";
import {ABSTRACT_FILTER_TYPE} from "../utils/sortFilter/abstractFilterTypeEnum";
import {FILTER_TYPE} from "../utils/sortFilter/filterTypeEnum";
import {IAbstractFilterCondition} from "../utils/sortFilter/IAbstractFilterCondition";

/**
 * Страница
 */
interface ICountryPage {

    /**
     * Список документов
     */
    list: ICountry[]

    /**
     * Пагинация
     */
    pagination: IPagination
}

/**
 * Стор для стран
 */
export class CountryStore {

    // Список стран
    @observable countryList: ICountry[] = [];

    // Страница стран
    @observable countryPage: ICountryPage = {
        list: [],
        pagination: {} as IPagination
    } as ICountryPage;

    // Параметры пагинации, сортировки, фильтрации
    @observable private sortFilterPageableRequest: ISortFilterPageableRequest = {
        pageNumber: 0,
        pageSize: 100,
        sortFieldList: [
            {
                field: 'name',
                sortDirection: SORT_DIRECTION.ASC,
            } as ISortField
        ],
        filterConditionList: []
    } as ISortFilterPageableRequest;

    // Сброс настроек параметров пагинации, сортировки и фильтрации
    private resetCondition = (): void => {
        this.countryPage = {
            list: [],
            pagination: {} as IPagination
        } as ICountryPage;
        this.sortFilterPageableRequest = {
            pageNumber: 0,
            pageSize: 100,
            sortFieldList: [
                {
                    field: 'name',
                    sortDirection: SORT_DIRECTION.ASC,
                } as ISortField
            ],
            filterConditionList: []
        } as ISortFilterPageableRequest;
    };

    // Преобразует response в IPagination
    private responseToPagination = (response: any): IPagination => {
        return {
            pageSize: this.sortFilterPageableRequest.pageSize,
            current: response.data.number + 1,
            total: response.data.totalElements,
            last: response.data.last,
        } as IPagination;
    };

    /**
     * Загружает следующую страницу
     */
    nextPage = flow(function* (this: any) {
        try {
            if (this.countryPage.pagination.last) {
                // Если страница последняя
                return;
            } else if (this.countryPage.pagination.total) {
                this.sortFilterPageableRequest.filterConditionList = [];
                this.sortFilterPageableRequest.pageNumber++;
            }
            const response = yield getCountryPage(this.sortFilterPageableRequest);
            this.countryPage.list = this.countryPage.list.concat(response.data.content);
            this.countryPage.pagination = this.responseToPagination(response);
        } catch (error) {
            console.log(error);
        }
    });

    /**
     * Загружает страницу
     * @param searchText - искомый параметр запрос
     */
    loadPage = flow(function* (this: any, searchText?: string) {
        try {
            if (searchText) {
                // Если передан новый набор искомых символов
                this.resetCondition();
                this.sortFilterPageableRequest.filterConditionList.push({
                        field: 'name',
                        type: ABSTRACT_FILTER_TYPE.filter,
                        filterType: FILTER_TYPE.LIKE,
                        value: `%${searchText}%`,
                    } as IAbstractFilterCondition
                );
            }
            const response = yield getCountryPage(this.sortFilterPageableRequest);
            this.countryPage.list = response.data.content;
            this.countryPage.pagination = this.responseToPagination(response);
        } catch (error) {
            console.log(error);
        }
    });
}
