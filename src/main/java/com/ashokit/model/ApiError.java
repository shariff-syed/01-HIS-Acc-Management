package com.ashokit.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
	
	private int errCode;
	private String errMsg;
	private Date date;

}
