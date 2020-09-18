package com.example.springbootreactpagination.utils.sortFilter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Набор статических методов для работы с {@link Pageable}
 */
@SuppressWarnings("squid:S4449")
public class SortFilterPageableUtils {
    private SortFilterPageableUtils() {
        throw new IllegalStateException("Utility class!");
    }

    /**
     * Метод для создания {@link Pageable}
     *
     * @param pageNumber    номер страницы
     * @param pageSize      размер страницы
     * @param sort          сортировка по полю
     * @param sortDirection направление сортировки
     * @return {@link Pageable}
     */
    public static Pageable createPageable(final Integer pageNumber,
                                          final Integer pageSize,
                                          final String sort,
                                          final SortDirectionEnum sortDirection) {
        Pageable pageable;
        switch (sortDirection) {
            case ASC:
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sort).ascending());
                break;
            case DESC:
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sort).descending());
                break;
            default:
                pageable = PageRequest.of(pageNumber, pageSize, Sort.unsorted());
                break;
        }
        return pageable;
    }

    public static Pageable createPageable(final SortFilterPageableRequest sortFilterPageableRequest) {
        Pageable pageable;
        Sort sort = null;
        for (SortField current : sortFilterPageableRequest.getSortFieldList()) {
            switch (current.getSortDirection()) {
                case ASC:
                    if (sort == null) {
                        sort = Sort.by(current.getField()).ascending();
                    } else {
                        sort.and(Sort.by(current.getField()).ascending());
                    }
                    break;
                case DESC:
                    if (sort == null) {
                        sort = Sort.by(current.getField()).descending();
                    } else {
                        sort.and(Sort.by(current.getField()).descending());
                    }
                    break;
                default:
                    if (sort == null) {
                        sort = Sort.unsorted();
                    } else {
                        sort.and(Sort.unsorted());
                    }
                    break;
            }
        }
        if (sort == null) {
            sort = Sort.unsorted();
        }
        pageable = PageRequest.of(sortFilterPageableRequest.getPageNumber(),
                sortFilterPageableRequest.getPageSize(), sort);
        return pageable;
    }

    public static <T> Specification createSpecification(final SortFilterPageableRequest sortFilterPageableRequest) {
        return new CustomSpecification<T>(sortFilterPageableRequest);
    }

    public static SortFilterPageableRequest createMaxSortFilterPageableRequest() {
        SortFilterPageableRequest sortFilterPageableRequest = new SortFilterPageableRequest();
        sortFilterPageableRequest.setPageNumber(0);
        sortFilterPageableRequest.setPageSize(Integer.MAX_VALUE);
        List<SortField> sortFieldList = new ArrayList<>();
        sortFilterPageableRequest.setSortFieldList(sortFieldList);
        return sortFilterPageableRequest;
    }

    public static Pageable createPageableBySortFilterPageableRequest(
            final SortFilterPageableRequest sortFilterPageableRequest) {
        if (!sortFilterPageableRequest.getFilterConditionList().isEmpty()) {
            if (!(sortFilterPageableRequest.getFilterConditionList().get(sortFilterPageableRequest
                    .getFilterConditionList().size() - 1) instanceof Condition)) {
                Condition condition = new Condition();
                condition.setConditionEnum(ConditionEnum.AND);
                sortFilterPageableRequest.getFilterConditionList().add(condition);
            }
        }
        return SortFilterPageableUtils.createPageable(sortFilterPageableRequest);
    }
}
