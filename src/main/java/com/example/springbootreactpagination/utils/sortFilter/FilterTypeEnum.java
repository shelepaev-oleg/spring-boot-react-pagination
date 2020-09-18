package com.example.springbootreactpagination.utils.sortFilter;

/**
 * Тип фильтрации
 */
public enum FilterTypeEnum {
    /**
     * Равно
     */
    EQUAL,

    /**
     * Не равно
     */
    NOT_EQUAL,

    /**
     * In
     */
    IN,

    /**
     * Like
     */
    LIKE,

    /**
     * Между(для дат)
     */
    BETWEEN,

    /**
     * Невозможное условие
     */
    IMPOSIBLE,

    /**
     * Подзапрос с in
     */
    SUB_IN,

    /**
     * Подзапрос с exist
     */
    SUB_EXIST
}
