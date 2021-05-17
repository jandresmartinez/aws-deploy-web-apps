package com.deploy.demo.service;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public interface IOperations<T extends Serializable> {

    T findOne(final long id);

    List<T> findAll();

    Page<T> findPaginated(int page, int size,String direction,String orderBy);


}
