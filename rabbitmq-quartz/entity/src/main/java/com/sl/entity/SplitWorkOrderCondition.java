package com.sl.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SplitWorkOrderCondition {
	
	private Integer originOrderType;
	private Integer originOrderNo;
	private Integer originOrderQty;
	private Integer newOrderNo;
	private Integer newOrderQty;
  
	public SplitWorkOrderCondition(){
		
	}
	
	public SplitWorkOrderCondition(Integer originOrderType,
			Integer originOrderNo, Integer originOrderQty, Integer newOrderNo,
			Integer newOrderQty) {
		this.originOrderType = originOrderType;
		this.originOrderNo = originOrderNo;
		this.originOrderQty = originOrderQty;
		this.newOrderNo = newOrderNo;
		this.newOrderQty = newOrderQty;
	}

	public Integer getOriginOrderType() {
		return originOrderType;
	}

	public void setOriginOrderType(Integer originOrderType) {
		this.originOrderType = originOrderType;
	}

	public Integer getOriginOrderNo() {
		return originOrderNo;
	}

	public void setOriginOrderNo(Integer originOrderNo) {
		this.originOrderNo = originOrderNo;
	}

	public Integer getOriginOrderQty() {
		return originOrderQty;
	}

	public void setOriginOrderQty(Integer originOrderQty) {
		this.originOrderQty = originOrderQty;
	}

	public Integer getNewOrderNo() {
		return newOrderNo;
	}

	public void setNewOrderNo(Integer newOrderNo) {
		this.newOrderNo = newOrderNo;
	}

	public Integer getNewOrderQty() {
		return newOrderQty;
	}

	public void setNewOrderQty(Integer newOrderQty) {
		this.newOrderQty = newOrderQty;
	}

	@Override
	public String toString() {
		return "SplitWorkOrderCondition [originOrderType=" + originOrderType
				+ ", originOrderNo=" + originOrderNo + ", originOrderQty="
				+ originOrderQty + ", newOrderNo=" + newOrderNo
				+ ", newOrderQty=" + newOrderQty + "]";
	}
	
	public String toJson(){
		String result = null;
		ObjectMapper mapper=new ObjectMapper();
	     try {
			result = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			   System.out.println(e.getMessage());
		}
		return result;
	}
	
	public  static SplitWorkOrderCondition toObject(String json){
		ObjectMapper mapper=new ObjectMapper();
		try {
		 return	mapper.readValue(json, SplitWorkOrderCondition.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new SplitWorkOrderCondition();
		}
	}
}
