package com.ashokit.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PlanManagementBasicDetails {

	private String planName;
	private String description;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date startDate;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date endDate;

}
