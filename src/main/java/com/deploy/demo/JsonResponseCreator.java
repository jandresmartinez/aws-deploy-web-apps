package com.deploy.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class JsonResponseCreator<T> extends JsonResponse<T> {
	
	@JsonCreator
	public JsonResponseCreator(@JsonProperty("totalElements") long totalElements,
			@JsonProperty("totalPages") int totalPages,
			@JsonProperty("pageNumber") int pageNumber,
			@JsonProperty("pageSize") int pageSize,
			@JsonProperty("content") List<T> content) {
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.content = content;
	}

}
