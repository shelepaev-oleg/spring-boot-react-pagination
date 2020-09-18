/**
 * Интерфейс для фильтрации
 */
export interface IAbstractFilterCondition {

    /**
     * Тип операции
     */
    type: string;

    /**
     * Тип фильтрации
     */
    filterType: string;

    /**
     * Поле, по которому будет работать фильтрация
     */
    field: string | null;

    /**
     * Значение фильтрации
     */
    value: any | null;

    /**
     * Служебное поле для связи по нескольким условиям
     */
    conditionEnum?: string;
}
