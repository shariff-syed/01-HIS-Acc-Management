package com.ashokit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ashokit.constants.AppConstants;
import com.ashokit.entity.AccountManagementEntity;
import com.ashokit.exception.EmployeeManagementException;
import com.ashokit.model.EmployeeAccount;
import com.ashokit.model.EmployeeBasicDetails;
import com.ashokit.model.PageResponse;
import com.ashokit.repository.AccountMasterRepository;
import com.ashokit.repository.AccountManagementRepository;
import com.ashokit.utils.EmailUtils;
import com.ashokit.utils.PwdUtils;

@Service
public class AccountManagementServiceImpl implements AccountManagementService {

	private static final Logger logger = LoggerFactory.getLogger(AccountManagementServiceImpl.class);

	@Autowired
	private AccountManagementRepository hisEmployeesAccountsRepository;

	@Autowired
	private AccountMasterRepository accountsMasterRepository;

	@Autowired
	private PwdUtils pwdUtils;

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public List<String> getAccountRoles() {
		logger.debug(AppConstants.METHOD_STARTED);
		List<String> roles = null;
		try {
			roles = accountsMasterRepository.findAllAccountsRoles();
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmployeeManagementException(AppConstants.EMPLOYEE_ROLES_EXCE);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return roles;
	}

	@Override
	public boolean saveAccount(EmployeeBasicDetails employee) {
		logger.debug(AppConstants.METHOD_STARTED);
		AccountManagementEntity entity = new AccountManagementEntity();
		boolean isSaved = false;
		try {
			BeanUtils.copyProperties(employee, entity);
			entity.setAccStatus(AppConstants.LOCKED);
			entity.setDeleteStatus(AppConstants.DELETED_N);
			entity.setPazzword(pwdUtils.generateRandomPassword(8));
			AccountManagementEntity createdEntity = hisEmployeesAccountsRepository.save(entity);
			logger.info(AppConstants.ACC_CREATED_SUCC);
			if (createdEntity.getEmployeeId() != null) {
				boolean isEmailSent = emailUtils.sendUserAccUnlockEmail(createdEntity);
				if (isEmailSent) {
					isSaved = true;
				}
				logger.warn(AppConstants.EMAIL_SENT_FAILED);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmployeeManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return isSaved;
	}

	@Override
	public PageResponse getEmployeesByRole(String role, int currPage, int pageSize) {
		logger.debug(AppConstants.METHOD_STARTED);
		PageResponse response = null;
		List<EmployeeAccount> employees = new ArrayList<>();
		try {
			PageRequest page = PageRequest.of(currPage, pageSize);
			Page<AccountManagementEntity> roleContaining = hisEmployeesAccountsRepository.findByRoleContaining(role,
					page);
			List<AccountManagementEntity> entities = roleContaining.getContent();
			employees = entities.stream().map(entity -> {
				EmployeeAccount employee = new EmployeeAccount();
				BeanUtils.copyProperties(entity, employee);
				return employee;
			}).collect(Collectors.toList());
			response = new PageResponse(roleContaining.getTotalPages(), currPage + 1, employees);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return response;
	}

	@Override
	public boolean updatedAccount(EmployeeAccount employee) {
		logger.debug(AppConstants.METHOD_STARTED);
		boolean isUpdated = false;
		AccountManagementEntity entity = new AccountManagementEntity();
		try {
			BeanUtils.copyProperties(employee, entity);
			AccountManagementEntity createdEntity = hisEmployeesAccountsRepository.save(entity);
			if (createdEntity.getEmployeeId() != null) {
				isUpdated = true;
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmployeeManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return isUpdated;
	}

	@Override
	public EmployeeAccount getAccountDetailsByEmailAndPwd(String email, String pwd) {
		logger.debug(AppConstants.METHOD_STARTED);
		EmployeeAccount employee = new EmployeeAccount();
		try {
			AccountManagementEntity entity = hisEmployeesAccountsRepository.findByEmailAndPazzword(email, pwd);
			BeanUtils.copyProperties(entity, employee);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return employee;
	}

	@Override
	public EmployeeAccount getAccountDetailsByEmail(String email) {
		logger.debug(AppConstants.METHOD_STARTED);
		EmployeeAccount employee = new EmployeeAccount();
		try {
			AccountManagementEntity entity = hisEmployeesAccountsRepository.findByEmail(email);
			BeanUtils.copyProperties(entity, employee);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return employee;
	}

	@Override
	public EmployeeAccount getAccountDetailsByEmployeeId(Long employeeId) {
		logger.debug(AppConstants.METHOD_STARTED);
		EmployeeAccount employee = new EmployeeAccount();
		try {
			AccountManagementEntity entity = hisEmployeesAccountsRepository.findByEmployeeId(employeeId);
			BeanUtils.copyProperties(entity, employee);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return employee;
	}
}
