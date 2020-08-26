package com.ashokit.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ashokit.entity.AccountsMasterEntity;

public interface AccountsMasterRepository extends JpaRepository<AccountsMasterEntity, Serializable> {
	
	@Query("select role from AccountsMasterEntity")
	List<String> findAllAccountsRoles();
}
