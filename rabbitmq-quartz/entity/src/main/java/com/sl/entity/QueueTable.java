package com.sl.entity;

import java.io.Serializable;
import java.util.Date;

public class QueueTable implements Serializable{
  
	private static final long serialVersionUID = 95620218555427021L;

 	private int id;
 	
 	private String request;
 	
 	private String type;
 	
 	private String status;

 	private Date entryDate;
 	
 	private String comment;
 	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
    
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "QueueTable [id=" + id + ", request=" + request + ", type="
				+ type + ", status=" + status + ", entryDate=" + entryDate
				+ "]";
	}
}
