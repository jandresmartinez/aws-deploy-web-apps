package com.deploy.demo.service;

import com.deploy.demo.domain.WebApplication;
import com.deploy.demo.enums.WebAppStatus;
import com.deploy.demo.exceptions.ObjectNotFoundException;
import com.deploy.demo.repository.WebApplicationRepository;

import com.deploy.demo.utils.Consts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
class WebApplicationServiceTest {

    @Test
    @DisplayName("Return page(list) of web applications")
    void getWebApps_null_shouldReturnList() {
        // Given
        final WebApplicationRepository mockedWebApplicationRepository = Mockito.mock(WebApplicationRepository.class);
        final List<WebApplication>  mockedWebApps=  new ArrayList() {
            {
                add(new WebApplication(1L, "Bitnami1","www.isawsesome.com","Awesome Test",new Date(), WebAppStatus.IN_PROGRESS.getStatus(),"",""));
                add(new WebApplication(99L, "Bitnami2","www.isawsesome2.com","Awesome 2 Test",new Date(), WebAppStatus.IN_PROGRESS.getStatus(),"",""));
                add(new WebApplication(55L, "Bitnami3","www.isawsesome3.com","Awesome 3 Test",new Date(), WebAppStatus.IN_PROGRESS.getStatus(),"",""));
            }};

        PageRequest pageRequest = PageRequest.of(
                Integer.parseInt(Consts.CONTROLLERPAGEDEFAULTVALUE),
                Integer.parseInt(Consts.CONTROLLERSIZEDEFAULTVALUE),
                Sort.Direction.fromString(Consts.CONTROLLERDIRECTIONDEFAULTVALUE),
                Consts.CONTROLLERORDERBYDEFAULTVALUE);
        Page<WebApplication> page = new PageImpl<>(mockedWebApps);
        Mockito.when(
                mockedWebApplicationRepository.findAll(pageRequest)
                       ).thenReturn(page);



        final WebApplicationService webApplicationService = new WebApplicationService(mockedWebApplicationRepository);

        Page<WebApplication> results = webApplicationService.findPaginated(Integer.parseInt(Consts.CONTROLLERPAGEDEFAULTVALUE),
                Integer.parseInt(Consts.CONTROLLERSIZEDEFAULTVALUE),
                Consts.CONTROLLERDIRECTIONDEFAULTVALUE,
                Consts.CONTROLLERORDERBYDEFAULTVALUE);


        assertTrue(results.getContent().stream().anyMatch(e->e instanceof WebApplication));
    }


    @Test
    @DisplayName("Throw object not found exceptions when non existing id")
    void getWebApps_nonExistingId_shouldThrowException() {

        final WebApplicationRepository mockedWebAppsRepository = Mockito.mock(WebApplicationRepository.class);
        Mockito.when(
                mockedWebAppsRepository.findById(86L)
        ).thenReturn(null);
        final WebApplicationService webAppsService = new WebApplicationService(mockedWebAppsRepository);
        assertThrows(ObjectNotFoundException.class,() ->{ webAppsService.findById(88L);});
    }



}