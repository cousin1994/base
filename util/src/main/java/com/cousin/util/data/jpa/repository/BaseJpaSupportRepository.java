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
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class BaseJpaSupportRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseJpaRepository<T, ID> {

	private final EntityManager em;
	
	private Class<?> repositoryInterface;
	
	public BaseJpaSupportRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.em=em;
	}

	
	/**
	 * 构造函数，注入不能改变的实体管理跟JPA的管理还有要查询的实体
	 * @param entityInformation
	 * @param em
	 * @param repositoryInterface
	 */
	public BaseJpaSupportRepository(final JpaEntityInformation<T, ?> entityInformation,final EntityManager em,final Class<?> repositoryInterface) {
		super(entityInformation, em);
		this.em = em;
		this.repositoryInterface = repositoryInterface;
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

			/**
			 * Root代表的是要查询的根对象
			 * CriteriaQuery:相当于查询语句拼装器，用来组合查询的内容和查询的条件
			 * CriteriaBuilder:用于CriteriaQuery的工厂，用于创建CriteriaQuery对象
			 */
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(searchParams!=null&& searchParams.size()>1){
					List<Predicate> predicates = new ArrayList<Predicate>();
					
				}
				
				return null;
			}
			
		};
		
		return spec;
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
