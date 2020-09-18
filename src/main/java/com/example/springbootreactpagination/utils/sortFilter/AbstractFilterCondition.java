package com.example.springbootreactpagination.utils.sortFilter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Condition.class, name = "conditionEnum"),
        @JsonSubTypes.Type(value = Filter.class, name = "filter"),
        @JsonSubTypes.Type(value = CompositeFilter.class, name = "compositeFilter"),
        @JsonSubTypes.Type(value = SubqueryFilter.class, name = "subqueryFilter")
})
public abstract class AbstractFilterCondition implements Serializable {
}
