package com.cousin.util.struts2;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 *@author 戴嘉诚 E-mail:a773807943@gmail.com
 *@version V0.1 创建时间：2016年2月22日 下午10:06:42
 */
public abstract class SuperAction extends ActionSupport implements Preparable, ServletRequestAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8475300431394468777L;

	protected static Log log = LogFactory.getLog(SuperAction.class);
	
	//定义静态变量
	public static final String RELOAD = "reload";//重定向变量
	
	/** HttpServletRequest对象,可以利用该对象获取Http请求的相应数据 */
	protected HttpServletRequest reqeust;
	
	/**
	 * 预定义操作。可以在这里直接写预定义操作
	 */
	@Override
	public void prepare() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.reqeust = request;
	}
	
	
	public abstract String execute() throws Exception;

}
