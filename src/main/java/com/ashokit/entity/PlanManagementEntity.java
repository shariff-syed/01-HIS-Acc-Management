package com.ashokit.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "PLAN_DETAILS")
public class PlanManagementEntity {

	@Id
	@GenericGenerator(name = "plan_id", strategy = "com.ashokit.utils.PlanIdGenerator")
	@GeneratedValue(generator = "plan_id")
	@Column(name = "PLAN_ID")
	private String planId;
	
	@Column(name = "PLAN_NAME")
	private String planName;
	
	@Column(name = "PLAN_DESC")
	private String description;
	
	@Column(name = "START_DATE")
	private Date startDate;
	
	@Column(name = "END_DATE")
	private Date endDate;
	
	@Column(name = "ACTIVE_SW")
	private String activeSW;
	
	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;
	
	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", updatable = false)
	private Date createdDate;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name ="UPDATED_DATE", insertable = false)
	private Date updatedDate;
}
