package com.ashokit.model;

import lombok.Data;

@Data
public class AccountCredentialsUpdate {
	
	private String email;
	private String oldPwd;
	private String newPwd;

}
