/**
 * Интерфейс для сортировки
 */
export interface ISortField {

    /**
     * Сортируемое поле
     */
    field: string;

    /**
     * Направление сортировки
     */
    sortDirection: "ASC" | "DESC";
}
