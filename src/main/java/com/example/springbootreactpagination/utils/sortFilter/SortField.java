package com.example.springbootreactpagination.utils.sortFilter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Dto для хранения информации о сортировке
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortField implements Serializable {

    /**
     * Сортируемое поле
     */
    private String field;

    /**
     * Направление сортировки
     */
    private SortDirectionEnum sortDirection;
}
