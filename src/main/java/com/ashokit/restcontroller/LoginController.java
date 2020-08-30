package com.ashokit.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ashokit.constants.AppConstants;
import com.ashokit.exception.LoginException;
import com.ashokit.model.AccountCredentials;
import com.ashokit.model.EmployeeAccount;
import com.ashokit.model.EmployeeBasicDetails;
import com.ashokit.service.AccountManagementService;
import com.ashokit.service.PlanManagementServiceImpl;

@RestController
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(PlanManagementServiceImpl.class);

	@Autowired
	private AccountManagementService accountService;

	/**
	 * By credentials of email and password it return the message. If login success
	 * return employee basic details. If login failed reasons will be invalid
	 * credentials, account locked & account soft deleted and return the appropriate
	 * message.
	 * 
	 * @param credentials
	 * @return Employee details when login success or return login failed reason
	 */
	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody AccountCredentials credentials) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<?> response = null;
		try {
			EmployeeAccount account = accountService.getAccountDetailsByEmailAndPwd(credentials.getEmail(),
					credentials.getPazzword());
			if (account.getEmployeeId() != null) {
				if (account.getDeleteStatus().equals(AppConstants.DELETED_N)) {
					if (account.getAccStatus().equals(AppConstants.UN_LOCKED)) {
						EmployeeBasicDetails employee = new EmployeeBasicDetails();
						BeanUtils.copyProperties(account, employee);
						response = new ResponseEntity<EmployeeBasicDetails>(employee, HttpStatus.OK);
						logger.info(AppConstants.ACC_LOGIN_SUCC);
					} else {
						response = new ResponseEntity<String>(AppConstants.ACC_LOCKED, HttpStatus.BAD_REQUEST);
						logger.info(AppConstants.INVALID_CREDENTIALS);
					}
				} else {
					response = new ResponseEntity<String>(AppConstants.ACC_DELETED, HttpStatus.BAD_REQUEST);
					logger.info(AppConstants.INVALID_CREDENTIALS);
				}
			} else {
				response = new ResponseEntity<String>(AppConstants.INVALID_CREDENTIALS, HttpStatus.BAD_REQUEST);
				logger.info(AppConstants.INVALID_CREDENTIALS);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new LoginException(AppConstants.INVALID_CREDENTIALS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}
}
