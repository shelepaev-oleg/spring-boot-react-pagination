package com.example.springbootreactpagination.service;

import com.example.springbootreactpagination.model.Country;
import com.example.springbootreactpagination.utils.sortFilter.SortFilterPageableRequest;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface CountryService {

    /**
     * Возвращает страницу стран
     * @param sortFilterPageableRequest {@link SortFilterPageableRequest}
     * @return {@link PageImpl} of {@link Country}
     */
    PageImpl<Country> getPage(SortFilterPageableRequest sortFilterPageableRequest);
}
