package com.deploy.demo.repository;

import com.deploy.demo.domain.WebApplication;

import com.deploy.demo.utils.Consts;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebApplicationRepositoryTest {

    @Autowired
    WebApplicationRepository citiesRepository;

    private static final String NAME_WEB_APP="Joomla";

    @Test
    @DisplayName("Save a web app")
    @Order(0)
    void saveWebApp() {

        assertNotNull(createDummyWebApp().getId());

    }

    @Test
    @DisplayName("Find a web app by name")
    @Order(1)
    void findWebAppByName() {

        createDummyWebApp();

        PageRequest pageRequest = PageRequest.of(
                Integer.parseInt(Consts.CONTROLLERPAGEDEFAULTVALUE),
                Integer.parseInt(Consts.CONTROLLERSIZEDEFAULTVALUE),
                Sort.Direction.fromString(Consts.CONTROLLERDIRECTIONDEFAULTVALUE),
                Consts.CONTROLLERORDERBYDEFAULTVALUE);
        Page<WebApplication> response = citiesRepository.findByNameContaining(NAME_WEB_APP, pageRequest);
        assertEquals(1,response.getContent().size());
        assertEquals( 0,citiesRepository.findByNameContaining("Drupal", pageRequest).getTotalElements(),"Result should be empty");

    }

    private WebApplication createDummyWebApp(){
        WebApplication dummyWebApp= new WebApplication();
        dummyWebApp.setName(NAME_WEB_APP);
        citiesRepository.save(dummyWebApp);

        return dummyWebApp;

    }





}