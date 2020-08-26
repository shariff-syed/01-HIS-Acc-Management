package com.ashokit.repository;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ashokit.entity.HISEmployeeAccountsEntity;

public interface HISEmployeeAccountsRepository extends JpaRepository<HISEmployeeAccountsEntity, Serializable> {

	HISEmployeeAccountsEntity findByEmployeeId(Long employeeId);

	HISEmployeeAccountsEntity findByEmailAndPazzword(String email, String pazzword);

	HISEmployeeAccountsEntity findByEmail(String email);

	Page<HISEmployeeAccountsEntity> findByRoleContaining(String role, Pageable pageable);
}
