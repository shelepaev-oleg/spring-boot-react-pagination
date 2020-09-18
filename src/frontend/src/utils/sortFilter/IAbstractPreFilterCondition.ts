/**
 * Интерфейс для фильтрации
 */
export interface IAbstractPreFilterCondition {

    /**
     * Имя поля
     */
    refFilterFieldCode: string;

    /**
     * Тип фильтрации
     */
    conditionCode: string;

    /**
     * Значение фильтрации
     */
    value: any | null;

    /**
     * Вес
     */
    weight: string;
}
