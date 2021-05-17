package com.deploy.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonResponse<T> {

	protected long totalElements;
	protected int totalPages;
	protected int pageNumber;
	protected int pageSize;
	protected List<T> content;

}
