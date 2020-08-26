package com.ashokit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class HISEmployeeAccount {

	private Long employeeId;
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	private String role;
	
	@JsonIgnore
	private String pazzword;
	private String accStatus;
	private String deleteStatus;
}
