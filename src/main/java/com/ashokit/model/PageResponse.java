package com.ashokit.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponse {
	
	private int totalPages;
	private int currPage;
	private List<?> response;

}
