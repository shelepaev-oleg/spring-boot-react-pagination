package com.example.springbootreactpagination.utils.sortFilter;

import lombok.*;

import java.io.Serializable;

/**
 * Dto для хранения информации о subquery фильтрах
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubqueryFilter extends AbstractFilterCondition implements Serializable {
    /**
     * Фильтруемое поле
     */
    private String field;

    /**
     * Тип фильтрации
     */
    private FilterTypeEnum filterType;

    /**
     * Подзапрос
     */
    private Subquery subquery;
}
