package com.deploy.demo.service;

import com.deploy.demo.domain.WebApplication;
import com.deploy.demo.exceptions.ObjectNotFoundException;
import com.deploy.demo.repository.WebApplicationRepository;
import com.deploy.demo.service.impl.AbstractService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class WebApplicationService extends AbstractService<WebApplication> {

    private WebApplicationRepository webApplicationRepository;

    public WebApplication findByName(String name){
        return webApplicationRepository.findByName(name);
    }

    @Override
    public WebApplication saveAndReturn(WebApplication webApp){
        return super.saveAndReturn(webApp);
    }

    public WebApplication updateAndReturn(WebApplication entity){
        WebApplication webApplication=this.findById(entity.getId());

        if(entity.getInstanceId()==null)
            entity.setInstanceId(webApplication.getInstanceId());
        if(entity.getUserData()==null)
            entity.setUserData(webApplication.getUserData());
        if(entity.getMessage()==null)
            entity.setMessage(webApplication.getMessage());
        if(entity.getState()==null)
            entity.setState(webApplication.getState());
        if(entity.getUrl()==null)
            entity.setUrl(webApplication.getUrl());

        return super.saveAndReturn(entity);
    }



    @Override
    public void delete(WebApplication webApp){
        super.delete(webApp);
    }


    @Autowired
    public WebApplicationService(WebApplicationRepository webApplicationRepository) {
        this.webApplicationRepository = webApplicationRepository;
    }


    public WebApplication findById(long id) {
        Optional<WebApplication> opt = webApplicationRepository.findById(id);
        if (opt.isPresent())
            return opt.get();
        else
            throw new ObjectNotFoundException("WebApplication with id:"+id+ " not found.");
    }

    public Page<WebApplication> findByNameContaining(String name, final int page, final int size, String direction, String orderBy) {
        return webApplicationRepository.findByNameContaining(name, PageRequest.of(page, size, Sort.Direction.fromString(direction),orderBy));
    }


    @Override
    protected PagingAndSortingRepository<WebApplication, Long> getRepository() {
        return webApplicationRepository;
    }
}
