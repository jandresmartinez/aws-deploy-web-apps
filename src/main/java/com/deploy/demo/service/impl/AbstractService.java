package com.deploy.demo.service.impl;


import com.deploy.demo.service.IOperations;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional
public abstract class AbstractService<T extends Serializable> implements IOperations<T> {

    @Override
    @Transactional(readOnly = true)
    public T findOne(final long id) {
        return getRepository().findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return Lists.newArrayList(getRepository().findAll());
    }

    @Override
    public Page<T> findPaginated(final int page, final int size,String direction,String orderBy) {
        return getRepository().findAll(PageRequest.of(page, size, Sort.Direction.fromString(direction),orderBy));
    }

    public T saveAndReturn(T entity) {
        return getRepository().save(entity);
    }

    protected abstract PagingAndSortingRepository<T, Long> getRepository();

}
