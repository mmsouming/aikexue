package com.dy.aikexue.ssolibrary.view;


import com.dy.aikexue.ssolibrary.bean.SSOHTTP;

/**
 * 
 * @author zengdl
 * @create 2015-01-29
 *
 */
public abstract interface SSOListener {
	public abstract void onComplete(SSOHTTP data);

	public abstract void onCancel();
}
