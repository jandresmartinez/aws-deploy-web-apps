package com.deploy.demo.dto;


import com.deploy.demo.domain.WebApplication;

import java.util.ArrayList;
import java.util.List;

public class RestWebAppsDTOResponseBuilder {

	private RestWebAppsDTOResponseBuilder() {}

	public static List<WebApplicationDTO> createWebApplicationDTOResponseList(List<WebApplication> entities) {
		List<WebApplicationDTO> dtoList = new ArrayList<>();
		for(WebApplication instance : entities) {
			dtoList.add(RestWebAppsDTOResponseBuilder.createWebApplicationDTOResponse(instance));
		}
		return dtoList;
	}

	public static WebApplicationDTO createWebApplicationDTOResponse(WebApplication entity) {

		return WebApplicationDTO
				.builder()
				.withWebApplication(entity)
				.build();
	}


	
}