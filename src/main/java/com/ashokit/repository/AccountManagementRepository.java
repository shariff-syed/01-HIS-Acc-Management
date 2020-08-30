package com.ashokit.repository;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ashokit.entity.AccountManagementEntity;

public interface AccountManagementRepository extends JpaRepository<AccountManagementEntity, Serializable> {

	AccountManagementEntity findByEmployeeId(Long employeeId);

	AccountManagementEntity findByEmailAndPazzword(String email, String pazzword);

	AccountManagementEntity findByEmail(String email);

	Page<AccountManagementEntity> findByRoleContaining(String role, Pageable pageable);
}
