package com.ef.dto;

import java.text.ParseException;
import java.util.Date;

import com.ef.utils.DateUtil;

public class LogDto {

	private String accessDate;

	private String ip;

	private String request;

	private String accessStatus;

	private String userAgent;

	public String getAccessDate() {
		return accessDate;
	}

	public void setAccessDate(String accessDate) {
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

	public Date getDate() {		
		//DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date;
		try {
			date = DateUtil.LOG_DATE_FORMAT.parse(accessDate);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		
	}

}
