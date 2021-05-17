package com.deploy.demo.controller;


import com.deploy.demo.JsonResponseCreator;
import com.deploy.demo.domain.WebApplication;
import com.deploy.demo.dto.RestWebAppsDTOResponseBuilder;
import com.deploy.demo.dto.WebApplicationDTO;
import com.deploy.demo.exceptions.DuplicatedObjectException;
import com.deploy.demo.exceptions.ObjectNotFoundException;
import com.deploy.demo.service.WebApplicationService;
import com.deploy.demo.utils.Consts;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/web-apps")
public class WebApplicationController {


    private WebApplicationService webApplicationService;

    @Autowired
    public WebApplicationController(WebApplicationService webApplicationService) {
        this.webApplicationService = webApplicationService;
    }

    @ApiOperation(value = "One web application", nickname ="oneWebApp", tags = { "Web applications" })
    @GetMapping(path = "/launch-state/{id}")
    public @ResponseBody
    HttpEntity<MappingJacksonValue> getSingleWebbApp(@PathVariable Long id ) {


        WebApplication webApplication = webApplicationService.findOne(id);
        if (webApplication==null)
            throw new ObjectNotFoundException("Web Application with id:"+id+ " not found.");
        WebApplicationDTO webApplicationDTO=WebApplicationDTO.builder().withWebApplication(webApplication).build();
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(webApplicationDTO);
        return new ResponseEntity<>(mappingJacksonValue,HttpStatus.OK);

    }

    @ApiOperation(value = "Start and create new application", nickname ="oneWebApp", tags = { "Web applications" })
    @PostMapping(path = "/new-launch")
    public @ResponseBody
    HttpEntity<MappingJacksonValue> startAndCreateNewWebApp(@RequestBody WebApplicationDTO webApplicationDTO ) {

        if (webApplicationService.findByName(webApplicationDTO.getName())!=null)
            throw new DuplicatedObjectException("There is already one web app with name "+webApplicationDTO.getName());

        WebApplication webApp = webApplicationService.saveAndReturn(WebApplicationDTO.getWebApplicationEntity(webApplicationDTO));
        WebApplicationDTO webApplicationDTOResponse=WebApplicationDTO.builder().withWebApplication(webApp).build();
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(webApplicationDTOResponse);
        return new ResponseEntity<>(mappingJacksonValue,HttpStatus.CREATED);


    }

    @ApiOperation(value = "List of web applications", nickname ="listWebApss", tags = { "Web applications" })
    @GetMapping(path = "/list-launch")
    public @ResponseBody
    HttpEntity<JsonResponseCreator<WebApplicationDTO>> getAllWebApps(
            @ApiParam(value = "Filter by the name of the app.", example = "Joomla")
            @RequestParam(required = false) String name,
            @ApiParam(value = "Number of page.", example = Consts.CONTROLLERPAGEDEFAULTVALUE)
            @RequestParam(value = "page", required = false, defaultValue = Consts.CONTROLLERPAGEDEFAULTVALUE) Integer pageNo,
            @ApiParam(value = "Size of page.", example = Consts.CONTROLLERSIZEDEFAULTVALUE)
            @RequestParam(value = "size", required = false, defaultValue = Consts.CONTROLLERSIZEDEFAULTVALUE) Integer pageSize) {

        Page<WebApplication> page;
        if (name==null|| name.isEmpty())
            page = webApplicationService.findPaginated(pageNo, pageSize,Consts.CONTROLLERDIRECTIONDEFAULTVALUE,Consts.CONTROLLERORDERBYDEFAULTVALUE);
        else
            page = webApplicationService.findByNameContaining(name,pageNo, pageSize,Consts.CONTROLLERDIRECTIONDEFAULTVALUE,Consts.CONTROLLERORDERBYDEFAULTVALUE);
        int totalPages=(int)Math.ceil(((Number)page.getTotalElements()).doubleValue()/pageSize);

        List<WebApplicationDTO> webAppsList = RestWebAppsDTOResponseBuilder.createWebApplicationDTOResponseList(page.getContent());
        JsonResponseCreator<WebApplicationDTO> jsonResponseCreator = new JsonResponseCreator<>(page.getTotalElements(), totalPages,pageNo, pageSize,webAppsList);
        return new ResponseEntity<>(jsonResponseCreator,  HttpStatus.OK);
    }



    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> onObjectNotFoundException(ObjectNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("%s - %s",
                HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(DuplicatedObjectException.class)
    public ResponseEntity<String> onDuplicatedObjectException(DuplicatedObjectException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format("%s - %s",
                HttpStatus.CONFLICT, ex.getMessage()));
    }


}
