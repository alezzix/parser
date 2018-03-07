package com.ef.dto;

import java.util.Date;

public class DatesDto {
	
	private Date startDate;
	
	private Date finishDate;
	
	
	public DatesDto(){}
	
	public DatesDto(Date startDate, Date finishDate){
		this.startDate = startDate;
		this.finishDate = finishDate;
		
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	

	
	
	


}
