package com.ashokit.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ashokit.entity.PlanManagementEntity;

public interface PlanManagementRepository extends JpaRepository<PlanManagementEntity, Serializable> {

	PlanManagementEntity findByPlanId(String planId);
	
}
