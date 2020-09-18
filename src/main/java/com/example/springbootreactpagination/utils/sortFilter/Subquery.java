package com.example.springbootreactpagination.utils.sortFilter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Dto для хранения информации о subquery фильтрах
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subquery implements Serializable {
    /**
     * Класс subquery
     */
    private Class clazz;

    /**
     * Выводимое поле
     */
    private String field;

    /**
     * Запрос
     */
    private SortFilterPageableRequest sortFilterPageableRequest;
}
