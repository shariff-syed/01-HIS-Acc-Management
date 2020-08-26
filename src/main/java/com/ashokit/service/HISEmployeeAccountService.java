package com.ashokit.service;

import java.util.List;

import com.ashokit.model.HISEmployeeAccount;
import com.ashokit.model.HISEmployeeBasicDetails;

public interface HISEmployeeAccountService {

	public List<String> getAccountRoles();

	public boolean saveAccount(HISEmployeeBasicDetails employee);

	public List<HISEmployeeAccount> getEmployeesByRole(String role, int pageNum, int pageSize);

	public boolean updatedAccount(HISEmployeeAccount employee);

	public HISEmployeeAccount getAccountDetailsByEmailAndPwd(String email, String pwd);

	public HISEmployeeAccount getAccountDetailsByEmail(String email);

	public HISEmployeeAccount getAccountDetailsByEmployeeId(Long employeeId);

}
