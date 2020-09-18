package com.example.springbootreactpagination.utils.sortFilter;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Dto объединяющий в себя параметры пагинации, сортировки и фильтрации
 */
@Data
public class SortFilterPageableRequest implements Serializable {

    /**
     * Номер страницы
     */
    private Integer pageNumber;

    /**
     * Колличество элементов на странице
     */
    private Integer pageSize;

    /**
     * Список сортировок
     */
    private List<SortField> sortFieldList = new ArrayList<>();

    /**
     * Список фильтраций
     */
    private List<AbstractFilterCondition> filterConditionList = new ArrayList<>();
}
