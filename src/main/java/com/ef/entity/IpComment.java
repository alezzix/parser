package com.ef.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ef.dto.IpDto;

@Entity
public class IpComment {
	
	public IpComment(){}
	
	
	
	public IpComment(IpDto ipDto) {
		this.ip = ipDto.getIp();
		this.comment = "Blocked because excedeed the threshold: "+ipDto.getCount() +" or more requests";
	}
	
	public IpComment(String ip) {
		this.ip = ip;	
	}
	
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_ip_comment")
    private Integer id;
	
	@Column(name="ip")
    private String ip;
	
	@Column(name="ip_comment")
    private String comment;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
