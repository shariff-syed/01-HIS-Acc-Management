package com.ashokit.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ashokit.constants.AppConstants;
import com.ashokit.exception.PlanManagementException;
import com.ashokit.model.PageResponse;
import com.ashokit.model.PlanManagement;
import com.ashokit.model.PlanManagementBasicDetails;
import com.ashokit.service.PlanManagementService;

@RestController
public class PlanManagementController {

	private static final Logger logger = LoggerFactory.getLogger(PlanManagementController.class);

	@Autowired
	private PlanManagementService planManagementService;

	/**
	 * To create plan with name, description, start date & end date.
	 * 
	 * @param planManagement
	 * @return Plan is created or not
	 */
	@PostMapping(value = "/createPlan")
	public ResponseEntity<String> createPlan(@RequestBody PlanManagementBasicDetails planManagement) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<String> response = null;
		try {
			boolean savePlan = planManagementService.savePlan(planManagement);
			if (savePlan) {
				response = new ResponseEntity<>(AppConstants.PLAN_CREATED_SUCC, HttpStatus.CREATED);
			} else {
				response = new ResponseEntity<>(AppConstants.PLAN_CREATED_UNSUCC, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new PlanManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}

	/**
	 * Retrieve plans by page request.
	 * 
	 * @param currPage
	 * @return Plans according to page request
	 */
	@GetMapping(value = "/getPlans")
	public ResponseEntity<PageResponse> retrievePlans(
			@RequestParam(value = "pageNum", required = false) String currPage) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<PageResponse> response = null;
		int pageNo = 1;
		if (currPage != null && currPage.equals("")) {
			pageNo = Integer.parseInt(currPage);
		}
		try {
			PageResponse pageResponse = planManagementService.getAllPalns(pageNo - 1, AppConstants.PLAN_PAGE_SIZE);
			response = new ResponseEntity<>(pageResponse, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new PlanManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}

	/**
	 * Return plan details by plan id.
	 * 
	 * @param planId
	 * @return Plan details.
	 */
	@GetMapping(value = "/getPlan/{planId}")
	public ResponseEntity<PlanManagement> retrievePlan(@PathVariable("planId") String planId) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<PlanManagement> response = null;
		try {
			PlanManagement plan = planManagementService.getPlan(planId);
			if (plan.getPlanId() != null) {
				response = new ResponseEntity<>(plan, HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new PlanManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}

	/**
	 * Plan is updated by Plan object details and return plan is updated or not
	 * 
	 * @param plan
	 * @return Plan is updated or not
	 */
	@PostMapping(value = "/updatePlan")
	public ResponseEntity<String> updatePlan(@RequestBody PlanManagement plan) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<String> response = null;
		try {
			PlanManagement plan1 = planManagementService.getPlan(plan.getPlanId());
			BeanUtils.copyProperties(plan, plan1);
			if (plan.getPlanId() != null) {
				boolean isUpdated = planManagementService.updatePlan(plan1);
				if (isUpdated) {
					response = new ResponseEntity<>(AppConstants.PLAN_UPDATED_SUCC, HttpStatus.OK);
				} else {
					response = new ResponseEntity<>(AppConstants.INVALID_DETAILS, HttpStatus.BAD_REQUEST);
				}
			} else {
				response = new ResponseEntity<>(AppConstants.INVALID_DETAILS, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new PlanManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}

	/**
	 * Plan is switched to active or inactive state by plan id.
	 * 
	 * @param planId
	 * @return Plan is switched to active or inactive by request.
	 */
	@GetMapping(value = "/activeSWPlan/{planId}")
	public ResponseEntity<String> activeSWPlan(@PathVariable("planId") String planId) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<String> response = null;
		try {
			PlanManagement plan = planManagementService.getPlan(planId);
			if (plan.getPlanId() != null) {
				plan.setActiveSW(plan.getActiveSW().equals(AppConstants.ACTIVE_N) ? AppConstants.ACTIVE_Y
						: AppConstants.ACTIVE_N);
				boolean isUpdated = planManagementService.updatePlan(plan);
				if (isUpdated) {
					response = new ResponseEntity<>(AppConstants.PLAN_UPDATED_SUCC, HttpStatus.OK);
				} else {
					response = new ResponseEntity<>(AppConstants.INVALID_DETAILS, HttpStatus.BAD_REQUEST);
				}
			} else {
				response = new ResponseEntity<>(AppConstants.INVALID_DETAILS, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new PlanManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}
}
