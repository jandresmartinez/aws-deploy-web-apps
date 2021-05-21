package com.deploy.demo.dto;


import com.deploy.demo.domain.WebApplication;
import com.deploy.demo.enums.WebAppStatus;
import com.fasterxml.jackson.annotation.JsonFilter;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebApplicationDTO {

    private long id;
    private String name;
    private String url;
    private String message;
    private String state;
    private Date started;
    @ApiModelProperty(example = "#! /bin/bash\ncurl -sSL https://raw.githubusercontent.com/bitnami/bitnami-docker-joomla/master/docker-compose.yml > docker-compose.yml\ndocker-compose up -d")
    private String userData;
    private String instanceId;


    public WebApplicationDTO(WebApplicationDTOBuilder webApplicationDTOBuilder) {

        setId(webApplicationDTOBuilder.webApplications.getId());
        setName(webApplicationDTOBuilder.webApplications.getName());
        if(webApplicationDTOBuilder.webApplications.getUrl()!=null)
            setUrl(webApplicationDTOBuilder.webApplications.getUrl());
        if(webApplicationDTOBuilder.webApplications.getState()!=null)
            setState(WebAppStatus.get(webApplicationDTOBuilder.webApplications.getState()).name());
        if(webApplicationDTOBuilder.webApplications.getStartedDate()!=null)
            setStarted(webApplicationDTOBuilder.webApplications.getStartedDate());
        if(webApplicationDTOBuilder.webApplications.getMessage()!=null)
            setMessage(webApplicationDTOBuilder.webApplications.getMessage());
        if(webApplicationDTOBuilder.webApplications.getUserData()!=null)
            setUserData(webApplicationDTOBuilder.webApplications.getUserData());
        if(webApplicationDTOBuilder.webApplications.getInstanceId()!=null)
            setInstanceId(webApplicationDTOBuilder.webApplications.getInstanceId());
    }


    public static WebApplicationDTOBuilder builder() {
        return new WebApplicationDTOBuilder();
    }


    public static WebApplication getWebApplicationEntity(WebApplicationDTO dto) {
        WebApplication webApp = new WebApplication();
        webApp.setId(dto.getId());
        webApp.setMessage(dto.getMessage());
        webApp.setName(dto.getName());
        webApp.setStartedDate(dto.getStarted());
        webApp.setUserData(dto.getUserData());
        webApp.setInstanceId(dto.getInstanceId());
        webApp.setUrl(dto.getUrl());
        return  webApp;
    }


    public static final class WebApplicationDTOBuilder{

        private WebApplication webApplications;

        private WebApplicationDTOBuilder() {
        }

        public WebApplicationDTOBuilder withWebApplication(WebApplication webApplications) {
            this.webApplications = webApplications;
            return this;
        }

        public WebApplicationDTO build() {
            return new WebApplicationDTO(this);
        }

    }
}
