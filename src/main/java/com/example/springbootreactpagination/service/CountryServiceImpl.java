package com.example.springbootreactpagination.service;

import com.example.springbootreactpagination.model.Country;
import com.example.springbootreactpagination.repository.CountryRepository;
import com.example.springbootreactpagination.utils.sortFilter.SortFilterPageableRequest;
import com.example.springbootreactpagination.utils.sortFilter.SortFilterPageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(final CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    /**
     * Возвращает страницу стран
     * @param sortFilterPageableRequest {@link SortFilterPageableRequest}
     * @return {@link PageImpl} of {@link Country}
     */
    @Override
    public PageImpl<Country> getPage(final SortFilterPageableRequest sortFilterPageableRequest) {
        Pageable pageable = SortFilterPageableUtils.createPageable(sortFilterPageableRequest);
        Specification specification = SortFilterPageableUtils.createSpecification(sortFilterPageableRequest);
        Page page = countryRepository.findAll(specification, pageable);
        return new PageImpl<Country>(page.getContent(), pageable, page.getTotalElements());
    }
}
