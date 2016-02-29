package com.cousin.util.struts2;

import org.springframework.data.domain.Page;

/**
 *@author 戴嘉诚 E-mail:a773807943@gmail.com
 *@version V0.1 创建时间：2016年2月23日 上午9:49:57
 */
public abstract class BasicSuperAction<T> extends SuperAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2880854052078364537L;
	
	public int pageNumber = 1;
	public int pageSize = 20;
	protected Page<T> page;
	
protected Long sid ;
	
	protected Long[] id;

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public Long[] getId() {
		return id;
	}

	public void setId(Long[] id) {
		this.id = id;
	}



	//表示默认的action入口就是list函数
	@Override
	public String execute() throws Exception {
		return list();
	}
	
	public abstract String list() throws Exception;

	public int getPageNumber() {
		return pageNumber;
	}
	
	
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	
	public int getPageSize() {
		return pageSize;
	}
	
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
	public Page<T> getPage() {
		return page;
	}
	
	
	public void setPage(Page<T> page) {
		this.page = page;
	}
	
}
