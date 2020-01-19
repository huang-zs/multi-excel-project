package com.zs.entity;

/**
 * 实体
 * 
 * @author MagicBook
 *
 */
public class Excel {
	private String id;
	private String name;
	private String createDate;
	private String lastModifyDate;
	private String json;
	private String createrId;
	private char status;
	private String fileDescribe;
	
	
	
	@Override
	public String toString() {
		return "Excel [id=" + id + ", name=" + name + ", createDate=" + createDate + ", lastModifyDate="
				+ lastModifyDate + ", json=" + json + ", createrId=" + createrId + ", status=" + status + ", describe="
				+ fileDescribe + "]";
	}
	public String getDescribe() {
		return fileDescribe;
	}
	public void setDescribe(String describe) {
		this.fileDescribe = describe;
	}
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getLastModifyDate() {
		return lastModifyDate;
	}
	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getCreaterId() {
		return createrId;
	}
	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}

	

}
