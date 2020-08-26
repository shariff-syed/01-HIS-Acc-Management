package com.ashokit.constants;

public class AppConstants {

	public static final Integer PAGE_SIZE = 3;
	public static final String LOCKED = "locked";
	public static final String UN_LOCKED = "unlocked";
	public static final String DELETED_N = "N";
	public static final String CONTROLLER_PACKAGE = "com.ashokit.restcontroller";

	// rest controller
	public static final String ACC_CREATED_SUCC = "Account created successfully";
	public static final String INVALID_DETAILS = "Invalid details";
	public static final String ROLES_SENT_SUCC = "Account roles sent successfully";
	public static final String REQ_SUCC = "Request Completed successfully";
	public static final String REQ_UNSUCC = "Failed to perform request";
	public static final String UNLOCK_SUCC = "Account activated Successfully";
	public static final String UNLOCK_UNSUCC = "Invalid credentials";
	public static final String UPDATE_SUCC = "Account updated successfully";
	public static final String ALL_EMPLOYEES_RETRIVE_SUCC = "Employees retrived successfully";

	public static final String METHOD_STARTED = "Method started";
	public static final String METHOD_ENDED = "Method ended";

	// service
	public static final String EMAIL_SENT_FAILED = "Failed to send email";
	
	//exceptions
	public static final String EXCE_OCCUR = "Exception occued :: {}";
	public static final String EMPLOYEE_ROLES_EXCE = "Failed to fetch employee roles";
	
	// status code
	public static final Integer BAD_REQUEST = 400;
	
	// utils
	public static final String UNLOCCK_MAIL_SUB = "Unlock account";
	public static final String UNLOCCK_MAIL_TXT = "USER-ACC-UNLOCK-EMAIL-BODY.txt";
	public static final String MAIL_BIND_FNAME = "{fName}";
	public static final String MAIL_BIND_LNAME = "{lName}";
	public static final String MAIL_BIND_EMAIL = "{email}";
	public static final String MAIL_BIND_PWD = "{pwd}";

	public static final String PWD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
}
