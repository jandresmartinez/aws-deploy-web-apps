package com.deploy.demo.controller;

import com.deploy.demo.domain.WebApplication;
import com.deploy.demo.dto.WebApplicationDTO;
import com.deploy.demo.enums.WebAppStatus;
import com.deploy.demo.executor.InstanceService;
import com.deploy.demo.service.WebApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(
        value = WebApplicationController.class
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class WebApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebApplicationService webApplicationService;

    @MockBean
    private InstanceService instanceService;

    private WebApplication dummyWebApplication;

    private List<WebApplication> mockedWebApps ;

    @BeforeAll
    public void initMocks()  {

        mockedWebApps=  new ArrayList() {
            {
                add(new WebApplication(1L, "Bitnami1","www.isawsesome.com","Awesome Test",new Date(), WebAppStatus.IN_PROGRESS.getStatus(),"",""));
                add(new WebApplication(99L, "Bitnami2","www.isawsesome2.com","Awesome 2 Test",new Date(), WebAppStatus.IN_PROGRESS.getStatus(),"",""));
                add(new WebApplication(55L, "Bitnami3","www.isawsesome3.com","Awesome 3 Test",new Date(), WebAppStatus.IN_PROGRESS.getStatus(),"",""));
            }};
        dummyWebApplication=mockedWebApps.get(0);
    }

    @Test
    @DisplayName("Test Single Web App")
    @Order(0)
    void getSingleWebApp_shouldReturnOk() throws Exception {

        Mockito.when(webApplicationService.findOne(dummyWebApplication.getId())).thenReturn(dummyWebApplication);
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/web-apps/launch-state/"+dummyWebApplication.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(dummyWebApplication.getId()))
              .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(dummyWebApplication.getName()))
              .andDo(MockMvcResultHandlers.print());

        Mockito.verify(webApplicationService).findOne(dummyWebApplication.getId());
    }

    @Test
    @DisplayName("Test Create/Start new web app")
    @Order(1)
    void createWebApp() throws Exception {

        Mockito.when(webApplicationService.saveAndReturn(dummyWebApplication)).thenReturn(dummyWebApplication);

        /**
         * Test post folder address without config
         */
        ObjectMapper mapper = new ObjectMapper();
        String postRequest = mapper.writeValueAsString(WebApplicationDTO.builder().withWebApplication(dummyWebApplication).build());

        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post("/web-apps/new-launch")
                        .content(postRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(dummyWebApplication.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(dummyWebApplication.getName()))
                .andDo(MockMvcResultHandlers.print());

    }




}