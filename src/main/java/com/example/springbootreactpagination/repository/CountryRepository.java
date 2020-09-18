package com.example.springbootreactpagination.repository;

import com.example.springbootreactpagination.model.Country;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends PagingAndSortingRepository<Country, Long>, JpaSpecificationExecutor<Country> {
}
