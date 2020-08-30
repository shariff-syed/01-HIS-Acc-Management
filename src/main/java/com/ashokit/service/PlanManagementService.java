package com.ashokit.service;

import com.ashokit.model.PageResponse;
import com.ashokit.model.PlanManagement;
import com.ashokit.model.PlanManagementBasicDetails;

public interface PlanManagementService {

	PageResponse getAllPalns(int currPage, int pageSize);

	PlanManagement getPlan(String planId);

	boolean savePlan(PlanManagementBasicDetails plan);

	boolean updatePlan(PlanManagement plan);
}
