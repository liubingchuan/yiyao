package com.yiyao.app.common.request;

import com.yiyao.app.common.PagedRequest;
import com.yiyao.app.model.User;

public class UserRequest extends PagedRequest{

	private User example;

	public User getExample() {
		return example;
	}

	public void setExample(User example) {
		this.example = example;
	}
	
}
