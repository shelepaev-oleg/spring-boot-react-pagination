package com.example.springbootreactpagination.utils.sortFilter;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Subquery;
import javax.persistence.criteria.*;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.springbootreactpagination.utils.sortFilter.FilterConstants.DATE_FORMAT_DDMMYYYY_HH24MMSSNANO;

/**
 * Спецификация для сортировки
 *
 * @param <T> Generic сущности
 */
@Slf4j
@Builder
public class CustomSpecification<T> implements Specification<T> {
    private static int maxInCount = 1000;
    private static ThreadLocal<DateTimeFormatter> dayMonthYearHourMinuteSecNanosecDateFormat =
            ThreadLocal.withInitial(() ->
                    DateTimeFormatter.ofPattern(DATE_FORMAT_DDMMYYYY_HH24MMSSNANO));

    private SortFilterPageableRequest sortFilterPageableRequest;

    /**
     * Формирует {@link Predicate}
     *
     * @param root            {@link Root}
     * @param criteriaQuery   {@link CriteriaQuery}
     * @param criteriaBuilder {@link CriteriaBuilder}
     * @return {@link Predicate}
     */
    @Override
    public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> criteriaQuery,
                                 final CriteriaBuilder criteriaBuilder) {
        return toPredicate(sortFilterPageableRequest.getFilterConditionList(), root, criteriaQuery, criteriaBuilder);
    }

    /**
     * Формирует {@link Predicate}
     *
     * @param filterConditionList List {@link AbstractFilterCondition}
     * @param root                {@link Root}
     * @param criteriaQuery       {@link CriteriaQuery}
     * @param criteriaBuilder     {@link CriteriaBuilder}
     * @return {@link Predicate}
     */
    private Predicate toPredicate(final List<AbstractFilterCondition> filterConditionList,
                                  final Root<T> root,
                                  final CriteriaQuery<?> criteriaQuery,
                                  final CriteriaBuilder criteriaBuilder) {
        if (filterConditionList.isEmpty()) {
            return criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
        }

        Predicate predicate = null;
        ConditionEnum conditionEnum = null;
        for (AbstractFilterCondition current : filterConditionList) {
            if (current instanceof Filter) {
                Predicate calculatePredicate = this.createPredicate((Filter) current, root, criteriaQuery,
                        criteriaBuilder);
                predicate = unionAsCondition(conditionEnum, predicate, calculatePredicate, criteriaBuilder);
                conditionEnum = null;
            } else {
                if (current instanceof Condition) {
                    conditionEnum = ((Condition) current).getConditionEnum();
                } else {
                    if (current instanceof CompositeFilter) {
                        List<AbstractFilterCondition> abstractFilterConditionList =
                                ((CompositeFilter) current).getFilterList();
                        Predicate compositePredicate = toPredicate(abstractFilterConditionList, root,
                                criteriaQuery,
                                criteriaBuilder);
                        predicate = unionAsCondition(conditionEnum, predicate, compositePredicate, criteriaBuilder);
                        conditionEnum = null;
                    } else {
                        if (current instanceof SubqueryFilter) {
                            SubqueryFilter subqueryFilter = (SubqueryFilter) current;
                            Subquery subquery = criteriaQuery.subquery(subqueryFilter.getSubquery().getClazz());
                            Root subqueryRoot = subquery.from(subqueryFilter.getSubquery().getClazz());
                            subquery.where(toPredicate(subqueryFilter.getSubquery().getSortFilterPageableRequest().
                                    getFilterConditionList(), subqueryRoot, criteriaQuery, criteriaBuilder));
                            Predicate subqueryPredicate = null;
                            switch (subqueryFilter.getFilterType()) {
                                case SUB_IN:
                                    subqueryPredicate =
                                            root.get(subqueryFilter.getField()).in(subquery.
                                                    select(subqueryRoot.get(subqueryFilter.getSubquery().getField())));
                                    break;
                                case SUB_EXIST:
                                    //Todo разобраться как в criteriaApi делать field exists (subquery)
                                    /*subqueryPredicate =
                                            root.get(subqueryFilter.getField()).in(subquery.
                                                    select(subqueryRoot.get(subqueryFilter.getSubquery().getField()))
                                                    );*/
                                    break;
                                default:
                                    throw new RuntimeException("Ошибка! Неверное условие объединения с "
                                            + "подзапросом.");
                            }

                            predicate = unionAsCondition(conditionEnum, predicate, subqueryPredicate, criteriaBuilder);
                            conditionEnum = null;
                        }
                    }
                }
            }
        }
        return predicate;
    }

    private Predicate unionAsCondition(ConditionEnum conditionEnum, Predicate predicate,
                                       Predicate calculatePredicate, CriteriaBuilder criteriaBuilder) {
        if (conditionEnum != null) {
            switch (conditionEnum) {
                case AND:
                    predicate = criteriaBuilder.and(predicate, calculatePredicate);
                    break;
                case OR:
                    predicate = criteriaBuilder.or(predicate, calculatePredicate);
                    break;
                default:
                    throw new RuntimeException("Ошибка! Таке условие не определено!");
            }
        } else {
            if (predicate == null) {
                predicate = calculatePredicate;
            } else {
                throw new RuntimeException("Ошибка! Неверное условие фильтрации.");
            }
        }
        return predicate;
    }

    private Predicate createPredicate(final Filter filter, final Root<T> root, final CriteriaQuery<?> criteriaQuery,
                                      final CriteriaBuilder criteriaBuilder) {
        switch (filter.getFilterType()) {
            case EQUAL:
                if (filter.getValue() == null) {
                    return criteriaBuilder.isNull(root.get(filter.getField()));
                } else {
                    Class classOfField = ((SingularAttributePath) root.get(filter.getField())).getJavaType();
                    if (isEnumField(classOfField)) {
                        if (isEnumObject(filter.getValue(), filter)) {
                            Object enumObject = getEnumFromMap(classOfField, filter.getValue());
                            return criteriaBuilder.equal(root.get(filter.getField()), enumObject);
                        } else {
                            if (filter.getValue().getClass().isEnum()) {
                                return criteriaBuilder.equal(root.get(filter.getField()), filter.getValue());
                            } else {
                                if (filter.getValue() instanceof Integer) {
                                    Object enumObject = getEnumFromInteger(classOfField, (Integer) filter.getValue());
                                    return criteriaBuilder.equal(root.get(filter.getField()), enumObject);
                                } else {
                                    throw new RuntimeException("Ошибка Equal! фильтрации!");
                                }
                            }
                        }
                    } else {
                        return criteriaBuilder.equal(root.get(filter.getField()), filter.getValue());
                    }
                }
            case NOT_EQUAL:
                if (filter.getValue() == null) {
                    return criteriaBuilder.isNull(root.get(filter.getField()));
                } else {
                    Class classOfField = ((SingularAttributePath) root.get(filter.getField())).getJavaType();
                    if (isEnumField(classOfField)) {
                        if (isEnumObject(filter.getValue(), filter)) {
                            Object enumObject = getEnumFromMap(classOfField, filter.getValue());
                            return criteriaBuilder.notEqual(root.get(filter.getField()), enumObject);
                        } else {
                            if (filter.getValue().getClass().isEnum()) {
                                return criteriaBuilder.notEqual(root.get(filter.getField()), filter.getValue());
                            } else {
                                if (filter.getValue() instanceof Integer) {
                                    Object enumObject = getEnumFromInteger(classOfField, (Integer) filter.getValue());
                                    return criteriaBuilder.notEqual(root.get(filter.getField()), enumObject);
                                } else {
                                    throw new RuntimeException("Ошибка NotEqual! фильтрации!");
                                }
                            }
                        }
                    } else {
                        return criteriaBuilder.notEqual(root.get(filter.getField()), filter.getValue());
                    }
                }
            case IN:
                if (filter.getValue() == null) {
                    return criteriaBuilder.isNull(root.get(filter.getField()));
                } else {
                    try {
                        List filterList = new ArrayList();
                        ((Iterable) filter.getValue()).forEach(filterList::add);
                        List<List> listFilterList = Lists.partition(filterList, maxInCount);
                        if (filterList.isEmpty()) {
                            return criteriaBuilder.notEqual(criteriaBuilder.literal(1), 1);
                        }
                        List<Predicate> predicateList = new ArrayList<>();
                        for (Object current : listFilterList) {
                            predicateList.add(root.get(filter.getField()).in(current));
                        }
                        return criteriaBuilder.or(predicateList.toArray(new Predicate[0]));
                    } catch (Exception e) {
                        throw new RuntimeException("Ошибка In фильтрации!");
                    }
                }
            case LIKE:
                if (filter.getValue() == null) {
                    return criteriaBuilder.isNull(root.get(filter.getField()));
                } else {
                    if (!(filter.getValue() instanceof String)) {
                        throw new RuntimeException("Ошибка Like фильтрации!");
                    }
                    return criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(filter.getField())),
                            ((String) filter.getValue()).toLowerCase()
                    );
                }
            case BETWEEN:
                if (filter.getValue() == null) {
                    return criteriaBuilder.isNull(root.get(filter.getField()));
                } else {
                    if (filter.getValue() instanceof Map) {
                        Map<String, String> map = (Map<String, String>) filter.getValue();
                        if (map.isEmpty() || map.size() != 1) {
                            throw new RuntimeException("Ошибка Between фильтрации!");
                        }

                        LocalDateTime leftDateTime = null;
                        LocalDateTime rightDateTime = null;
                        for (Map.Entry<String, String> current : map.entrySet()) {
                            try {
                                leftDateTime = LocalDateTime.parse(current.getKey(),
                                        dayMonthYearHourMinuteSecNanosecDateFormat.get());
                                rightDateTime = LocalDateTime.parse(current.getValue(),
                                        dayMonthYearHourMinuteSecNanosecDateFormat.get());
                            } catch (Exception e) {
                                throw new RuntimeException("Ошибка Between фильтрации!");
                            }
                        }
                        dayMonthYearHourMinuteSecNanosecDateFormat.remove();

                        return criteriaBuilder.between(root.get(filter.getField()), leftDateTime,
                                rightDateTime);
                    }
                    throw new RuntimeException("Ошибка Between фильтрации!");

                }
            case IMPOSIBLE:
                return criteriaBuilder.notEqual(criteriaBuilder.literal(1), 1);
            default:
                throw new RuntimeException("Ошибка! Данный тип фильтра не поддерживается.");
        }
    }

    /**
     * Является ли класс - Enum
     *
     * @param clazz Класс
     * @return логическое
     */
    private boolean isEnumField(final Class clazz) {
        return clazz.isEnum();
    }

    /**
     * Объект является Enum-м
     *
     * @param object объект
     * @param filter filter
     * @return логическое
     */
    private boolean isEnumObject(final Object object, final Filter filter) {
        return filter.getValue() instanceof Map &&
                ((Map) filter.getValue()).containsKey("name") && ((Map) filter.getValue()).containsKey("code");
    }

    /**
     * Получает enum объект из Map через статический метод fromCode
     *
     * @param clazz класс
     * @param in    объект
     * @return объект
     */
    @SneakyThrows
    private Object getEnumFromMap(final Class clazz, final Object in) {
        Method method = clazz.getMethod("fromCode", Integer.class);
        return method.invoke(null, ((Map<String, String>) in).get("code"));
    }

    @SneakyThrows
    private Object getEnumFromInteger(final Class clazz, final Integer in) {
        Method method = clazz.getMethod("fromCode", Integer.class);
        return method.invoke(null, in);
    }
}
