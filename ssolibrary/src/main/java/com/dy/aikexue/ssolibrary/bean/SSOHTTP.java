package com.dy.aikexue.ssolibrary.bean;


import com.dy.aikexue.ssolibrary.db.UserInfo;


/**
 * 
 * @author zengdl
 * @create 2015-01-29
 * 
 */
public class SSOHTTP {
	private String token;
	private UserInfo  userInfo;


	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
