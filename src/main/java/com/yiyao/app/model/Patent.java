package com.yiyao.app.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "patent", type = "pt")
public class Patent implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String ticket;  //公开号
	private String number; // 申请号
	private String subject; //摘要
	private String title; // 标题
	@Field(type=FieldType.Keyword)
	private String ipc; // ipc分类
	@Field(type=FieldType.Keyword)
	private String cpc; // cpc分类
	@Field(type=FieldType.Keyword)
	private String year; // 优先权年
	@Field(type=FieldType.Keyword)
	private String country; // 国家
	private String description; // 专利描述
	@Field(type=FieldType.Keyword)
	private String person; // 专利权人
	@Field(type=FieldType.Keyword)
	private String creator; // 发明人
	private String pdf; //pdf全文
	private String law; //法律状态
	private Long now;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIpc() {
		return ipc;
	}
	public void setIpc(String ipc) {
		this.ipc = ipc;
	}
	public String getCpc() {
		return cpc;
	}
	public void setCpc(String cpc) {
		this.cpc = cpc;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getPdf() {
		return pdf;
	}
	public void setPdf(String pdf) {
		this.pdf = pdf;
	}
	public String getLaw() {
		return law;
	}
	public void setLaw(String law) {
		this.law = law;
	}
	public Long getNow() {
		return now;
	}
	public void setNow(Long now) {
		this.now = now;
	}
	
	
	
}
