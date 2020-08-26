package com.ashokit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ACCOUNTS_MASTER")
public class AccountsMasterEntity {

	@Id
	@GeneratedValue
	@Column(name = "ACCOUNT_ID")
	private int accId;

	@Column(name = "ROLE")
	private String role;

}
