/**
 * Пагинация
 */
export interface IPagination {

    /**
     * Общее число элементов
     */
    total: number;

    /**
     * Число элементов на странице
     */
    pageSize: number;

    /**
     * Текущий номер страницы
     */
    current: number;

    /**
     * Количество страниц
     */
    totalPages: number;

    /**
     * Признак последней страницы
     */
    last: boolean;

    /**
     * Признак указывающий на то, что нужно отображать выбор числа записей на странице
     */
    showSizeChanger: boolean;

    /**
     * Набор возможных значений числа записей на странице
     */
    pageSizeOptions: Array<string>;
}
