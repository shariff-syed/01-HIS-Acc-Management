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
import com.ashokit.entity.HISEmployeeAccountsEntity;
import com.ashokit.exception.EmployeeManagementException;
import com.ashokit.model.HISEmployeeAccount;
import com.ashokit.model.HISEmployeeBasicDetails;
import com.ashokit.repository.AccountsMasterRepository;
import com.ashokit.repository.HISEmployeeAccountsRepository;
import com.ashokit.utils.EmailUtils;
import com.ashokit.utils.PwdUtils;

@Service
public class HISEmployeeAccountServiceImpl implements HISEmployeeAccountService {

	private static final Logger logger = LoggerFactory.getLogger(HISEmployeeAccountServiceImpl.class);

	@Autowired
	private HISEmployeeAccountsRepository hisEmployeesAccountsRepository;

	@Autowired
	private AccountsMasterRepository accountsMasterRepository;

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
	public boolean saveAccount(HISEmployeeBasicDetails employee) {
		logger.debug(AppConstants.METHOD_STARTED);
		HISEmployeeAccountsEntity entity = new HISEmployeeAccountsEntity();
		try {
			BeanUtils.copyProperties(employee, entity);
			entity.setAccStatus(AppConstants.LOCKED);
			entity.setDeleteStatus(AppConstants.DELETED_N);
			entity.setPazzword(pwdUtils.generateRandomPassword(8));
			HISEmployeeAccountsEntity createdEntity = hisEmployeesAccountsRepository.save(entity);
			logger.info(AppConstants.ACC_CREATED_SUCC);
			if (createdEntity.getEmployeeId() != null) {
				boolean isEmailSent = emailUtils.sendUserAccUnlockEmail(createdEntity);
				if (isEmailSent) {
					return true;
				}
				logger.warn(AppConstants.EMAIL_SENT_FAILED);
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmployeeManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return false;
	}

	@Override
	public List<HISEmployeeAccount> getEmployeesByRole(String role, int pageNum, int pageSize) {
		logger.debug(AppConstants.METHOD_STARTED);
		List<HISEmployeeAccount> employees = new ArrayList<>();
		try {
			PageRequest page = PageRequest.of(pageNum, pageSize);
			Page<HISEmployeeAccountsEntity> roleContaining = hisEmployeesAccountsRepository.findByRoleContaining(role,
					page);
			List<HISEmployeeAccountsEntity> entities = roleContaining.getContent();
			employees = entities.stream().map(entity -> {
				HISEmployeeAccount employee = new HISEmployeeAccount();
				BeanUtils.copyProperties(entity, employee);
				return employee;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return employees;
	}

	@Override
	public boolean updatedAccount(HISEmployeeAccount employee) {
		logger.debug(AppConstants.METHOD_STARTED);
		HISEmployeeAccountsEntity entity = new HISEmployeeAccountsEntity();
		try {
			BeanUtils.copyProperties(employee, entity);
			HISEmployeeAccountsEntity createdEntity = hisEmployeesAccountsRepository.save(entity);
			if (createdEntity.getEmployeeId() != null) {
				return true;
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new EmployeeManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return false;
	}

	@Override
	public HISEmployeeAccount getAccountDetailsByEmailAndPwd(String email, String pwd) {
		logger.debug(AppConstants.METHOD_STARTED);
		HISEmployeeAccount employee = new HISEmployeeAccount();
		try {
			HISEmployeeAccountsEntity entity = hisEmployeesAccountsRepository.findByEmailAndPazzword(email, pwd);
			BeanUtils.copyProperties(entity, employee);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return employee;
	}

	@Override
	public HISEmployeeAccount getAccountDetailsByEmail(String email) {
		logger.debug(AppConstants.METHOD_STARTED);
		HISEmployeeAccount employee = new HISEmployeeAccount();
		try {
			HISEmployeeAccountsEntity entity = hisEmployeesAccountsRepository.findByEmail(email);
			BeanUtils.copyProperties(entity, employee);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return employee;
	}

	@Override
	public HISEmployeeAccount getAccountDetailsByEmployeeId(Long employeeId) {
		logger.debug(AppConstants.METHOD_STARTED);
		HISEmployeeAccount employee = new HISEmployeeAccount();
		try {
			HISEmployeeAccountsEntity entity = hisEmployeesAccountsRepository.findByEmployeeId(employeeId);
			BeanUtils.copyProperties(entity, employee);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return employee;
	}
}
