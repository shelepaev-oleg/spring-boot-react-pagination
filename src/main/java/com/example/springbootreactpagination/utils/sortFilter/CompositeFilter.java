package com.example.springbootreactpagination.utils.sortFilter;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Dto для хранения информации о составных фильтрах
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompositeFilter extends AbstractFilterCondition implements Serializable {

    private List<AbstractFilterCondition> filterList = new ArrayList<>();
}
