package com.example.springbootreactpagination.utils.sortFilter;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Condition extends AbstractFilterCondition implements Serializable {

    private ConditionEnum conditionEnum;
}
