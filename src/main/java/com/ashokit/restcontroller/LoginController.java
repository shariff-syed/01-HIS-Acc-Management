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
import org.springframework.web.bind.annotation.RestController;

import com.ashokit.constants.AppConstants;
import com.ashokit.entity.AccountManagementEntity;
import com.ashokit.exception.LoginException;
import com.ashokit.model.AccountCredentials;
import com.ashokit.model.EmployeeAccount;
import com.ashokit.service.AccountManagementService;
import com.ashokit.utils.EmailUtils;

@RestController
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private AccountManagementService accountService;

	@Autowired
	private EmailUtils emailUtil;

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
	public ResponseEntity<String> login(@RequestBody AccountCredentials credentials) {
		logger.debug(AppConstants.METHOD_STARTED);
		ResponseEntity<String> response = null;
		try {
			EmployeeAccount account = accountService.getAccountDetailsByEmailAndPwd(credentials.getEmail(),
					credentials.getPazzword());
			if (account.getEmployeeId() != null) {
				if (account.getDeleteStatus().equals(AppConstants.DELETED_N)) {
					if (account.getAccStatus().equals(AppConstants.UN_LOCKED)) {
						response = new ResponseEntity<>(account.getRole(), HttpStatus.OK);
						logger.info(AppConstants.ACC_LOGIN_SUCC);
					} else {
						response = new ResponseEntity<>(AppConstants.ACC_LOCKED, HttpStatus.BAD_REQUEST);
						logger.info(AppConstants.INVALID_CREDENTIALS);
					}
				} else {
					response = new ResponseEntity<>(AppConstants.ACC_DELETED, HttpStatus.BAD_REQUEST);
					logger.info(AppConstants.INVALID_CREDENTIALS);
				}
			} else {
				response = new ResponseEntity<>(AppConstants.INVALID_CREDENTIALS, HttpStatus.BAD_REQUEST);
				logger.info(AppConstants.INVALID_CREDENTIALS);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new LoginException(AppConstants.INVALID_CREDENTIALS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}

	/**
	 * This is help to retrieve the login password through email by entering valid
	 * email id which is already registered.
	 * 
	 * @param email
	 * @return String status of email sent or failed reason
	 */
	@GetMapping("/fogotPassword/{email}")
	public ResponseEntity<String> forgotPazzword(@PathVariable("email") String email) {
		ResponseEntity<String> response = null;
		try {
			EmployeeAccount account = accountService.getAccountDetailsByEmail(email);
			if (account.getEmployeeId() != null) {
				if (account.getDeleteStatus().equals(AppConstants.DELETED_N)) {
					if (account.getAccStatus().equals(AppConstants.UN_LOCKED)) {
						AccountManagementEntity entity = new AccountManagementEntity();
						BeanUtils.copyProperties(account, entity);
						boolean isSent = emailUtil.sendUserAccUnlockEmail(entity, AppConstants.FORGOT_PAZZWORD_MAIL_SUB,
								AppConstants.FORGOT_PAZZWORD_MAIL_TXT);
						if (isSent) {
							response = new ResponseEntity<>(AppConstants.FORGOT_PAZZWORD_MAIL_SUCC,
									HttpStatus.OK);
							logger.info(AppConstants.FORGOT_PAZZWORD_MAIL_SUCC);
						} else {
							response = new ResponseEntity<>(AppConstants.EMAIL_SENT_FAILED,
									HttpStatus.INTERNAL_SERVER_ERROR);
							logger.info(AppConstants.EMAIL_SENT_FAILED);
						}
					} else {
						response = new ResponseEntity<>(AppConstants.ACC_LOCKED, HttpStatus.BAD_REQUEST);
						logger.info(AppConstants.INVALID_CREDENTIALS);
					}
				} else {
					response = new ResponseEntity<>(AppConstants.ACC_DELETED, HttpStatus.BAD_REQUEST);
					logger.info(AppConstants.INVALID_CREDENTIALS);
				}
			} else {
				response = new ResponseEntity<>(AppConstants.INVALID_CREDENTIALS, HttpStatus.BAD_REQUEST);
				logger.info(AppConstants.INVALID_CREDENTIALS);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new LoginException(AppConstants.INVALID_CREDENTIALS);
		}
		return response;
	}
}
