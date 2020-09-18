package com.example.springbootreactpagination.controller;

import com.example.springbootreactpagination.model.Country;
import com.example.springbootreactpagination.service.CountryService;
import com.example.springbootreactpagination.utils.sortFilter.SortFilterPageableRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(final CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * Возвращает страницу стран
     * @param sortFilterPageableRequest {@link SortFilterPageableRequest}
     * @return {@link PageImpl} of {@link Country}
     */
    @PostMapping(path = {"/api/rest/country/page"})
    public PageImpl<Country> getPage(
            @RequestBody final SortFilterPageableRequest sortFilterPageableRequest) {
        return countryService.getPage(sortFilterPageableRequest);
    }
}
