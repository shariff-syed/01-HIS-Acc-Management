package com.ashokit.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ashokit.constants.AppConstants;
import com.ashokit.exception.UnlockEmployeeException;
import com.ashokit.model.AccountCredentialsUpdate;
import com.ashokit.model.EmployeeAccount;
import com.ashokit.service.AccountManagementService;

@RestController
public class UnlockEmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(UnlockEmployeeController.class);

	@Autowired
	private AccountManagementService hisEmployeeAccountService;

	/**
	 * Unlock the admin or case worker account by temporary password.
	 * 
	 * @param credentials
	 * @return status of account unlocked or invalid credentials
	 */
	@PostMapping(value = "/unlockAcc")
	public ResponseEntity<String> unclockAccount(@RequestBody AccountCredentialsUpdate credentials) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<String> response = null;
		try {
			EmployeeAccount employeeAccount = hisEmployeeAccountService
					.getAccountDetailsByEmailAndPwd(credentials.email, credentials.oldPwd);
			if (employeeAccount != null && employeeAccount.getAccStatus().equals(AppConstants.LOCKED)) {
				employeeAccount.setAccStatus(AppConstants.UN_LOCKED);
				employeeAccount.setPazzword(credentials.newPwd);
				hisEmployeeAccountService.updatedAccount(employeeAccount);
				response = new ResponseEntity<>(AppConstants.UNLOCK_SUCC, HttpStatus.OK);
				logger.info(AppConstants.UNLOCK_SUCC);
			} else {
				response = new ResponseEntity<>(AppConstants.INVALID_CREDENTIALS, HttpStatus.BAD_REQUEST);
				logger.info(AppConstants.INVALID_CREDENTIALS);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new UnlockEmployeeException(AppConstants.INVALID_CREDENTIALS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}
}
