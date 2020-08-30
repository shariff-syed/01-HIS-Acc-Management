package com.ashokit.model;

import lombok.Data;

@Data
public class AccountCredentialsUpdate {
	
	public String email;
	public String oldPwd;
	public String newPwd;

}
