package com.ashokit.service;

import java.util.List;

import com.ashokit.model.EmployeeAccount;
import com.ashokit.model.EmployeeBasicDetails;
import com.ashokit.model.PageResponse;

public interface AccountManagementService {

	public List<String> getAccountRoles();

	public boolean saveAccount(EmployeeBasicDetails employee);

	public PageResponse getEmployeesByRole(String role, int currPage, int pageSize);

	public boolean updatedAccount(EmployeeAccount employee);

	public EmployeeAccount getAccountDetailsByEmailAndPwd(String email, String pwd);

	public EmployeeAccount getAccountDetailsByEmail(String email);

	public EmployeeAccount getAccountDetailsByEmployeeId(Long employeeId);

}
