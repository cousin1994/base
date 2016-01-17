package com.cousin.util.data.jpa.repository;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 
 * @author 戴嘉诚
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean //标记这是我自己封装的，不是JPA的Reponsitory
public interface BaseJpaRepository<T , ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

	/**
	 * 封装的函数，通过自己添加各种条件，将查找出来的数据再通过JPA的分页函数进行分页
	 * @param searchParams		自己添加的条件
	 * @param pageNumber		当前的页数
	 * @param pageSize				每页有多少行
	 * @param orders					排序
	 * @return
	 */
	public Page<T> findAll(Map<String,Object> searchParams, int pageNumber, int pageSize, Order...orders);
	
}
