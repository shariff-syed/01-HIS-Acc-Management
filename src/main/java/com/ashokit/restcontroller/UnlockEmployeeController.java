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
import com.ashokit.model.EmployeeCredentials;
import com.ashokit.model.HISEmployeeAccount;
import com.ashokit.service.HISEmployeeAccountService;

@RestController
public class UnlockEmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(UnlockEmployeeController.class);

	@Autowired
	private HISEmployeeAccountService hisEmployeeAccountService;

	@PostMapping(value = "/unlockAcc")
	public ResponseEntity<String> unclockAccount(@RequestBody EmployeeCredentials credentials) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<String> response = null;
		try {
			HISEmployeeAccount employeeAccount = hisEmployeeAccountService
					.getAccountDetailsByEmailAndPwd(credentials.email, credentials.oldPwd);
			if (employeeAccount != null && employeeAccount.getAccStatus().equals(AppConstants.LOCKED)) {
				employeeAccount.setAccStatus(AppConstants.UN_LOCKED);
				employeeAccount.setPazzword(credentials.newPwd);
				hisEmployeeAccountService.updatedAccount(employeeAccount);
				response = new ResponseEntity<>(AppConstants.UNLOCK_SUCC, HttpStatus.OK);
				logger.info(AppConstants.UNLOCK_SUCC);
			} else {
				response = new ResponseEntity<>(AppConstants.UNLOCK_UNSUCC, HttpStatus.BAD_REQUEST);
				logger.info(AppConstants.UNLOCK_UNSUCC);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new UnlockEmployeeException(AppConstants.UNLOCK_UNSUCC);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}
}
