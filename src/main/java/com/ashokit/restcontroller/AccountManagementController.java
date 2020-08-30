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
import com.ashokit.model.EmployeeAccount;
import com.ashokit.model.EmployeeBasicDetails;
import com.ashokit.model.PageResponse;
import com.ashokit.service.AccountManagementService;

@RestController
public class AccountManagementController {

	private static final Logger logger = LoggerFactory.getLogger(AccountManagementController.class);

	@Autowired
	private AccountManagementService accountService;

	/**
	 * It return role types to create or update account by admin.
	 * 
	 * @return List of employee role types
	 */
	@GetMapping(value = "/getEmployeeRoles")
	public ResponseEntity<List<String>> employeeRoles() {
		logger.debug(AppConstants.METHOD_STARTED);
		List<String> roles = null;
		try {
			roles = accountService.getAccountRoles();
			logger.info(AppConstants.ROLES_SENT_SUCC);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmployeeManagementException(AppConstants.EMPLOYEE_ROLES_EXCE);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}

	/**
	 * Create employee account by admin with basic details of employee.
	 * 
	 * @param employeeDetails
	 * @return account created message or reason failed to create account message.
	 */
	@PostMapping(value = "/createEmployee")
	public ResponseEntity<String> crateAccount(@RequestBody EmployeeBasicDetails employeeDetails) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<String> response = null;
		try {
			boolean isSaved = accountService.saveAccount(employeeDetails);
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

	/**
	 * It return the particular employee details retrieved by employee id.
	 * 
	 * @param employeeId
	 * @return employee details
	 */
	@GetMapping("/retreiveEmployee/{employeeId}")
	public ResponseEntity<EmployeeAccount> retrieveEmployee(@PathVariable("employeeId") Long employeeId) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<EmployeeAccount> response = null;
		try {
			EmployeeAccount account = accountService.getAccountDetailsByEmployeeId(employeeId);
			if (account.getEmployeeId() != null) {
				response = new ResponseEntity<>(account, HttpStatus.OK);
				logger.info(AppConstants.EMPLOYEE_RETRIVE_SUCC);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				logger.info(AppConstants.INVALID_DETAILS);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmployeeManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}

	/**
	 * It return list of employees filtered by inputs of role and current page
	 * number.
	 * 
	 * @param role
	 * @param pageNum
	 * @return List of employees filtered by role type and page number.
	 */
	@GetMapping(value = "/getEmployees/{role}")
	public ResponseEntity<PageResponse> retrieveEmployees(@PathVariable("role") String role,
			@RequestParam(name = "pgNo", required = false) String pageNum) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<PageResponse> response = null;
		int pageNo = 1;
		if (pageNum != null && pageNum != "") {
			pageNo = Integer.parseInt(pageNum);
		}
		try {
			PageResponse pageResponse = accountService.getEmployeesByRole(role, pageNo - 1,
					AppConstants.ACCS_PAGE_SIZE);
			response = new ResponseEntity<>(pageResponse, HttpStatus.OK);
			logger.info(AppConstants.EMPLOYEES_RETRIVE_SUCC);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmployeeManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}

	/**
	 * It returns string response employee details are updated or if not updated
	 * failed reason. It take input as object of employee basic details.
	 * 
	 * @param employeeDetails
	 * @return string value of employee details are updated or failed reasons.
	 */
	@PostMapping("/updateEmployeeDetails")
	public ResponseEntity<String> updateEmployeeDetails(@RequestBody EmployeeBasicDetails employeeDetails) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<String> response = null;
		try {
			EmployeeAccount employeeAccount = accountService.getAccountDetailsByEmail(employeeDetails.getEmail());
			if (employeeAccount.getEmployeeId() != null) {
				BeanUtils.copyProperties(employeeDetails, employeeAccount);
				boolean updatedAccount = accountService.updatedAccount(employeeAccount);
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

	/**
	 * It take input as employee id and switch to active or inactive by present
	 * status.
	 * 
	 * @param employeeId
	 * @return Request success or fail status.
	 */
	@GetMapping(value = "/deleteStatus/{employeeId}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("employeeId") Long employeeId) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<String> response = null;
		try {
			EmployeeAccount employeeAccount = accountService.getAccountDetailsByEmployeeId(employeeId);
			if (employeeAccount.getEmployeeId() != null) {
				String deleteStatus = employeeAccount.getDeleteStatus().equals(AppConstants.DELETED_N)
						? AppConstants.DELETED_Y
						: AppConstants.DELETED_N;
				employeeAccount.setDeleteStatus(deleteStatus);
				boolean updatedAccount = accountService.updatedAccount(employeeAccount);
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
