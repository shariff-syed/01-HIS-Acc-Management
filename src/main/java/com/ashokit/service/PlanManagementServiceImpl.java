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
import com.ashokit.entity.PlanManagementEntity;
import com.ashokit.exception.PlanManagementException;
import com.ashokit.model.PageResponse;
import com.ashokit.model.PlanManagement;
import com.ashokit.model.PlanManagementBasicDetails;
import com.ashokit.repository.PlanManagementRepository;

@Service
public class PlanManagementServiceImpl implements PlanManagementService {
	
	private static final Logger logger = LoggerFactory.getLogger(PlanManagementServiceImpl.class);
	
	@Autowired
	private PlanManagementRepository planManagementRepository;

	@Override
	public PageResponse getAllPalns(int currPage, int pageSize) {
		logger.debug(AppConstants.METHOD_STARTED);
		List<PlanManagement> plans = new ArrayList<>();
		PageResponse pageResponse = null;
		try {
			PageRequest page = PageRequest.of(currPage, pageSize);
			Page<PlanManagementEntity> pageContent = planManagementRepository.findAll(page);
			List<PlanManagementEntity> entities = pageContent.getContent();
			plans = entities.stream().map(entity -> {
				PlanManagement plan = new PlanManagement();
				BeanUtils.copyProperties(entity, plan);
				return plan;
			}).collect(Collectors.toList());
			pageResponse = new PageResponse(pageContent.getTotalPages(), currPage+1, plans);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new PlanManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return pageResponse;
	}

	@Override
	public PlanManagement getPlan(String planId) {
		logger.debug(AppConstants.METHOD_STARTED);
		PlanManagement plan = new PlanManagement();
		try {
			PlanManagementEntity entity = planManagementRepository.findByPlanId(planId);
			BeanUtils.copyProperties(entity, plan);
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new PlanManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return plan;
	}

	@Override
	public boolean savePlan(PlanManagementBasicDetails plan) {
		logger.debug(AppConstants.METHOD_STARTED);
		boolean isSaved = false;
		PlanManagementEntity entity = new PlanManagementEntity();
		BeanUtils.copyProperties(plan, entity);
		try {
			entity.setActiveSW(AppConstants.ACTIVE_Y);
			PlanManagementEntity savedEntity = planManagementRepository.save(entity);
			if(savedEntity.getPlanId() != null) {
				isSaved = true;
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new PlanManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return isSaved;
	}
	
	@Override
	public boolean updatePlan(PlanManagement plan) {
		logger.debug(AppConstants.METHOD_STARTED);
		boolean isSaved = false;
		PlanManagementEntity entity = new PlanManagementEntity();
		BeanUtils.copyProperties(plan, entity);
		try {
			PlanManagementEntity savedEntity = planManagementRepository.save(entity);
			if(savedEntity.getPlanId() != null) {
				isSaved = true;
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCE_OCCUR, e.getMessage());
			throw new PlanManagementException(AppConstants.INVALID_DETAILS);
		}
		logger.debug(AppConstants.METHOD_ENDED);
		return isSaved;
	}
}
