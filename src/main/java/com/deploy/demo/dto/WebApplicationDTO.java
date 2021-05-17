package com.deploy.demo.dto;


import com.deploy.demo.domain.WebApplication;
import com.deploy.demo.enums.WebAppStatus;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

//@JsonFilter("wepAppFilter")
public class WebApplicationDTO {

    private long id;
    private String name;
    private String url;
    private String message;
    private String state;
    private Date started;


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
