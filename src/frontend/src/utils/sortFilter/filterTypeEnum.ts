/**
 * Направление сортировки
 */
export enum FILTER_TYPE {
    /**
     * Равно
     */
    EQUAL = 'EQUAL',

    /**
     * Неравно
     */
    NOT_EQUAL = 'NOT_EQUAL',

    /**
     * Like
     */
    LIKE = 'LIKE',

    /**
     * In
     */
    IN = 'IN',

    /**
     * Между
     */
    BETWEEN = 'BETWEEN'
}
