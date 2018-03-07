package com.ef.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ef.dto.LogDto;

@Entity
public class Log {
	
	public Log() {
	
	}
	
	public Log(LogDto dto) {
		this.accessDate = dto.getDate();
		this.ip = dto.getIp();
		this.request = dto.getRequest();
		this.accessStatus = dto.getAccessStatus();
		this.userAgent = dto.getUserAgent();
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_log")
	private Integer id;

	@Column(name = "access_date")
	private Date accessDate;

	@Column(name = "ip")
	private String ip;

	@Column(name = "request")
	private String request;

	@Column(name = "access_status")
	private String accessStatus;

	@Column(name = "user_agent")
	private String userAgent;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getAccessDate() {
		return accessDate;
	}

	public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getAccessStatus() {
		return accessStatus;
	}

	public void setAccessStatus(String accessStatus) {
		this.accessStatus = accessStatus;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	
	

}
