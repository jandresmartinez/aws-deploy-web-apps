package com.deploy.demo.controller;

import com.deploy.demo.JsonResponseCreator;
import com.deploy.demo.domain.WebApplication;
import com.deploy.demo.dto.WebApplicationDTO;
import com.deploy.demo.enums.WebAppStatus;
import com.deploy.demo.executor.InstanceService;
import com.deploy.demo.service.WebApplicationService;

import com.deploy.demo.utils.Consts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class WebApplicationControllerUnitTest {

    private WebApplicationController webApplicationController;

    @Mock
    private WebApplicationService webApplicationService;

    @Mock
    private InstanceService instanceService;

    private List<WebApplication> mockedWebApps;
    @BeforeAll
    void init(){
        webApplicationService = Mockito.mock(WebApplicationService.class);
        webApplicationController= new WebApplicationController(webApplicationService,instanceService);
        mockedWebApps=  new ArrayList() {
            {
                add(new WebApplication(233L, "Bitnami1","www.isawsesome.com","Awesome Test",new Date(), WebAppStatus.IN_PROGRESS.getStatus(),"",""));
                add(new WebApplication(99L, "Bitnami2","www.isawsesome2.com","Awesome 2 Test",new Date(), WebAppStatus.IN_PROGRESS.getStatus(),"",""));
                add(new WebApplication(55L, "Bitnami3","www.isawsesome3.com","Awesome 3 Test",new Date(), WebAppStatus.IN_PROGRESS.getStatus(),"",""));
            }};

    }
    @Test
    @DisplayName("List of web apps")
    void getWebAppsShouldReturnListOfWebApps()  {



        Page<WebApplication> page = new PageImpl<>(mockedWebApps);

        Mockito.when(
                webApplicationService.findPaginated(
                        Integer.parseInt(Consts.CONTROLLERPAGEDEFAULTVALUE),
                        Integer.parseInt(Consts.CONTROLLERSIZEDEFAULTVALUE),
                        Consts.CONTROLLERDIRECTIONDEFAULTVALUE,
                        Consts.CONTROLLERORDERBYDEFAULTVALUE)).thenReturn(page);

        HttpEntity<JsonResponseCreator<WebApplicationDTO>> response = webApplicationController.getAllWebApps(null,Integer.parseInt(Consts.CONTROLLERPAGEDEFAULTVALUE), Integer.parseInt(Consts.CONTROLLERSIZEDEFAULTVALUE));
        assertEquals(response.getBody().getContent().size(),mockedWebApps.size(),"Size of the web apps do not match");
        assertEquals(response.getBody().getContent().get(0),WebApplicationDTO.builder().withWebApplication(mockedWebApps.get(0)).build(),"Bitnami1 app must be the same");

        Mockito.when(
                webApplicationService.findPaginated(
                        Integer.parseInt(Consts.CONTROLLERPAGEDEFAULTVALUE),
                        Integer.parseInt(Consts.CONTROLLERSIZEDEFAULTVALUE),
                        Consts.CONTROLLERDIRECTIONDEFAULTVALUE,
                        Consts.CONTROLLERORDERBYDEFAULTVALUE)).thenReturn(page);

    }


    @Test
    @DisplayName("List of single web app")
    void getSingleWebApp()  {
        Mockito.when(
                webApplicationService.findOne(mockedWebApps.get(0).getId())).thenReturn(mockedWebApps.get(0));

        HttpEntity<MappingJacksonValue> response = webApplicationController.getSingleWebbApp(mockedWebApps.get(0).getId());

        assertEquals(response.getBody().getValue(),WebApplicationDTO.builder().withWebApplication(mockedWebApps.get(0)).build(),"Bitnami1 app must be the same");


    }



}