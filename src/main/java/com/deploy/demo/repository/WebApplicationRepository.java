package com.deploy.demo.repository;

import com.deploy.demo.domain.WebApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebApplicationRepository extends PagingAndSortingRepository<WebApplication, Long> {

    WebApplication findByName(String name);
    Page<WebApplication> findByNameContaining(String name, Pageable pageable);
}
