package com.ashokit.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ashokit.constants.AppConstants;
import com.ashokit.model.ApiError;

@RestControllerAdvice
public class ExceptionsHandler {
	
	@ExceptionHandler(value = EmployeeManagementException.class)
	public ResponseEntity<ApiError> handleCreateEmployeeException() {
		ApiError apiError = new ApiError(AppConstants.BAD_REQUEST, AppConstants.INVALID_DETAILS, new Date());
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST); 
	}
	
	@ExceptionHandler(value = UnlockEmployeeException.class)
	public ResponseEntity<ApiError> handleUnlockEmployeeException() {
		ApiError apiError = new ApiError(AppConstants.BAD_REQUEST, AppConstants.UNLOCK_UNSUCC, new Date());
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST); 
	}
	
	@ExceptionHandler(value = EmailException.class)
	public ResponseEntity<ApiError> handleEmailException() {
		ApiError apiError = new ApiError(AppConstants.BAD_REQUEST, AppConstants.EMAIL_SENT_FAILED, new Date());
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST); 
	}

}
