package com.example.springbootreactpagination.utils.sortFilter;

import lombok.*;

import java.io.Serializable;

/**
 * Dto для хранения информации о фильтрации
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Filter extends AbstractFilterCondition implements Serializable {
    /**
     * Фильтруемое поле
     */
    private String field;

    /**
     * Тип фильтрации
     */
    private FilterTypeEnum filterType;

    /**
     * Значение
     */
    private transient Object value;
}
