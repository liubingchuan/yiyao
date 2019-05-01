package com.yiyao.app.model;

import com.yiyao.app.annotation.IdFlag;
import com.yiyao.app.annotation.TableName;

@TableName("paper")
public class PaperMysql {

	public PaperMysql() {
		
	}
	
	@IdFlag
	private Integer id;
	private String identifier;
	private String creator;
	private String datestamp;
	private String setSpec;
	private String title;
	private String subjects; 
	private String description; 
	private String dates; 
	private String types; 
	private String gooaidentifier;
	private String language;
	private String publisher;
	private String keywords;
	private String relation;
	private String source;
	private String doiidentifier;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getDatestamp() {
		return datestamp;
	}
	public void setDatestamp(String datestamp) {
		this.datestamp = datestamp;
	}
	public String getSetSpec() {
		return setSpec;
	}
	public void setSetSpec(String setSpec) {
		this.setSpec = setSpec;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubjects() {
		return subjects;
	}
	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getGooaidentifier() {
		return gooaidentifier;
	}
	public void setGooaidentifier(String gooaidentifier) {
		this.gooaidentifier = gooaidentifier;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDoiidentifier() {
		return doiidentifier;
	}
	public void setDoiidentifier(String doiidentifier) {
		this.doiidentifier = doiidentifier;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	

}
