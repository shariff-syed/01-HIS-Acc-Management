package com.ashokit.restcontroller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
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
import com.ashokit.exception.EmployeeManagementException;
import com.ashokit.model.HISEmployeeAccount;
import com.ashokit.model.HISEmployeeBasicDetails;
import com.ashokit.service.HISEmployeeAccountService;

@RestController
public class EmployeeManagementController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeManagementController.class);

	@Autowired
	private HISEmployeeAccountService hisEmployeeAccountService;

	@GetMapping(value = "/getEmployeeRoles")
	public ResponseEntity<List<String>> employeeRoles() {
		logger.debug(AppConstants.METHOD_STARTED);
		List<String> roles = null;
		try {
			roles = hisEmployeeAccountService.getAccountRoles();
			logger.info(AppConstants.ROLES_SENT_SUCC);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmployeeManagementException(AppConstants.EMPLOYEE_ROLES_EXCE);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}

	@PostMapping(value = "/createEmployee")
	public ResponseEntity<String> crateAccount(@RequestBody HISEmployeeBasicDetails employeeDetails) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<String> response = null;
		try {
			boolean isSaved = hisEmployeeAccountService.saveAccount(employeeDetails);
			if (isSaved == true) {
				response = new ResponseEntity<>(AppConstants.ACC_CREATED_SUCC, HttpStatus.CREATED);
				logger.info(AppConstants.ACC_CREATED_SUCC);
			} else {
				response = new ResponseEntity<>(AppConstants.INVALID_DETAILS, HttpStatus.BAD_REQUEST);
				logger.info(AppConstants.INVALID_DETAILS);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmployeeManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}

	@GetMapping(value = "/getEmployees/{role}")
	public ResponseEntity<List<HISEmployeeAccount>> retrieveEmployees(@PathVariable("role") String role,
			@RequestParam(name = "pgNo", required = false) String pageNum) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<List<HISEmployeeAccount>> response = null;
		int pageNo = 1;
		if (pageNum != null && pageNum != "") {
			pageNo = Integer.parseInt(pageNum);
		}
		try {
			List<HISEmployeeAccount> employeesList = hisEmployeeAccountService.getEmployeesByRole(role, pageNo - 1,
					AppConstants.PAGE_SIZE);
			response = new ResponseEntity<>(employeesList, HttpStatus.OK);
			logger.info(AppConstants.ALL_EMPLOYEES_RETRIVE_SUCC);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmployeeManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}

	@PostMapping("/updateEmployeeDetails")
	public ResponseEntity<String> updateEmployeeDetails(@RequestBody HISEmployeeBasicDetails employeeDetails) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<String> response = null;
		try {
			HISEmployeeAccount employeeAccount = hisEmployeeAccountService
					.getAccountDetailsByEmail(employeeDetails.getEmail());
			if (employeeAccount.getEmployeeId() != null) {
				BeanUtils.copyProperties(employeeDetails, employeeAccount);
				boolean updatedAccount = hisEmployeeAccountService.updatedAccount(employeeAccount);
				if (updatedAccount) {
					response = new ResponseEntity<>(AppConstants.UPDATE_SUCC, HttpStatus.OK);
					logger.info(AppConstants.UPDATE_SUCC);
				} else {
					response = new ResponseEntity<>(AppConstants.INVALID_DETAILS, HttpStatus.INTERNAL_SERVER_ERROR);
					logger.info(AppConstants.INVALID_DETAILS);
				}
			} else {
				response = new ResponseEntity<>(AppConstants.INVALID_DETAILS, HttpStatus.BAD_REQUEST);
				logger.info(AppConstants.INVALID_DETAILS);
			}
		} catch (BeansException e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmployeeManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}

	@GetMapping(value = "/deleteStatus")
	public ResponseEntity<String> deleteEmployee(@RequestParam("employeeId") Long employeeId,
			@RequestParam("deleteStatus") String deleteStatus) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<String> response = null;
		try {
			HISEmployeeAccount employeeAccount = hisEmployeeAccountService.getAccountDetailsByEmployeeId(employeeId);
			if (employeeAccount.getEmployeeId() != null) {
				employeeAccount.setDeleteStatus(deleteStatus);
				boolean updatedAccount = hisEmployeeAccountService.updatedAccount(employeeAccount);
				if (updatedAccount) {
					response = new ResponseEntity<>(AppConstants.REQ_SUCC, HttpStatus.OK);
					logger.info(AppConstants.REQ_SUCC);
				}
			} else {
				response = new ResponseEntity<String>(AppConstants.REQ_UNSUCC, HttpStatus.BAD_REQUEST);
				logger.info(AppConstants.REQ_UNSUCC);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmployeeManagementException(AppConstants.REQ_UNSUCC);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}

}
