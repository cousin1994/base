package com.cousin.util.data.jpa.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class BaseJpaSupportRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseJpaRepository<T, ID> {

	private final EntityManager em;
	
	private Class<?> repositoryInterface;
	
	public BaseJpaSupportRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.em=em;
	}

	/**
	 * 动态封装条件并且分页，排序
	 */
	@Override
	public Page<T> findAll(Map<String, Object> searchParams, int pageNumber, int pageSize, Order... orders) {
		// TODO Auto-generated method stub
		return findAll(bulidSpecification(searchParams, getDomainClass()),buildPageRequest(pageNumber,pageSize,orders));
	}
	
	/**
	 * 创建动态的查询条件组合
	 * @param searchParams
	 * @param clz
	 * @return
	 */
	private Specification<T> bulidSpecification(Map<String,Object> searchParams, Class<T> clz){
		Specification<T> spec = new Specification<T>() {

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(searchParams!=null&& searchParams.size()>1){
					List<Predicate> predicates = new ArrayList<Predicate>();
					
				}
				
				return null;
			}
			
		};
		
		return null;
	}
	
	/**
	 * 创建分页函数，使得可以调用
	 * @param pageNumber
	 * @param PageSize
	 * @param orders
	 * @return
	 */
	private PageRequest buildPageRequest(int pageNumber, int PageSize,Order...orders){
		return new PageRequest(pageNumber, PageSize, new Sort(orders));
	}
	
}
