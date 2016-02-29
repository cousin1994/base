package com.cousin.util.data.jpa.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class BaseJpaSupportRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseJpaRepository<T, ID> {

	private static final ConversionService CONVERSION_SERVICE = new DefaultConversionService();
	
	private final EntityManager em;
	
	private Class<?> repositoryInterface;
	
	/**
	 * 通过BaseJpaRepositoryFactory调用这个构造函数，
	 * @param domainClass
	 * @param em
	 */
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
		return findAll(bulidSpecification(searchParams, getDomainClass()),buildPageRequest(pageNumber>0?pageNumber-1:0 ,pageSize,orders));
	}
	
	/**
	 * 创建动态的查询条件组合
	 * @param searchParams
	 * @param clz
	 * @return
	 */
	private Specification<T> bulidSpecification(final Map<String,Object> searchParams, final Class<T> clz){
		return new Specification<T>() {

			/**
			 * Root代表的是要查询的根对象
			 * CriteriaQuery:相当于查询语句拼装器，用来组合查询的内容和查询的条件
			 * CriteriaBuilder:用于CriteriaQuery的工厂，用于创建CriteriaQuery对象
			 */
			@SuppressWarnings({"rawtypes", "unchecked"})
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(searchParams!=null&& searchParams.size()>1){
					List<Predicate> predicates = new ArrayList<Predicate>();
					Iterator<Map.Entry<String,Object>> entries = searchParams.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<String,Object> entry = entries.next();
						String operator = entry.getKey().substring(0, entry.getKey().indexOf("_"));
						String[] param = StringUtils.split(entry.getKey().substring(entry.getKey().indexOf("_")+1, entry.getKey().length()),".");
						Object value = entry.getValue();
						Path expression = root.get(param[0]);
						for(int i = 1; i<param.length; i++) {
							expression = expression.get(param[i]);
						}
						Class attributeClass = expression.getJavaType();
						if(!attributeClass.equals(String.class) && value instanceof String && CONVERSION_SERVICE.canConvert(String.class, attributeClass) && !"IS".equals(operator)){
							value = CONVERSION_SERVICE.convert(value, attributeClass);
						}
						
						switch (operator) {
						case "EQ" :
							predicates.add(cb.equal(expression ,value));
							break;
						case "LIKE" :
							predicates.add(cb.like(expression, "%"+value+"%"));
							break;
						case "RLIKE" :
							predicates.add(cb.like(expression,value+"%"));
							break;
						case "LLIKE" :
							predicates.add(cb.like(expression, "%"+value));
							break;
						case "GT" :  //GT    ">"
							predicates.add(cb.greaterThan(expression, (Comparable)value));
							break;
						case "LT" : //LT     "<"
							predicates.add(cb.lessThan(expression, (Comparable)value));
							break;
						case "GTE" :  //GTE	">="
							predicates.add(cb.greaterThanOrEqualTo(expression, (Comparable)value));
							break;
						case "LTE" :  //LTE		"<="
							predicates.add(cb.lessThanOrEqualTo(expression, (Comparable)value));
							break;
						case "IS" :  //IS	判断是否为空
							if("NULL".equals(value.toString().toUpperCase()))
								predicates.add(cb.isNull(expression));
							else
								predicates.add(cb.isNotNull(expression));
							break;
						case "NEQ" :  //不等于<>
							predicates.add(cb.notEqual(expression, (Comparable)value));
							break;
						case "IN" :
							if(value instanceof Object[]){
								List<Object> list = Arrays.asList((Object[])value); //将数组转化成list
								predicates.add(in(cb,expression,list));
							}else if(value instanceof Collection){
								predicates.add(in(cb, expression, (Collection<Object>)value));
							}else{
								List<Object> list = new ArrayList<Object>();
								list.add(value);
								predicates.add(in(cb, expression, list));
							}
							break;
						case "NIN" :
							if (value instanceof Object[]) {
								List<Object> list = Arrays.asList((Object[])value); //将数组转化成list
								predicates.add(cb.not(in(cb,expression,list)));
							}else if(value instanceof Collection){
								predicates.add(cb.not(in(cb, expression, (Collection<Object>)value)));
							}else{
								List<Object> list = new ArrayList<Object>();
								list.add(value);
								predicates.add(cb.not(in(cb, expression, list)));
							}
						}
					}
					if(predicates.size()>0){
						//return cb.and(predicates.toArray(new Predicate[predicates.size()]));
						return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
					}
				}
				return cb.conjunction();
			}
		};
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
	
	/**
	 * 对进行in条件选择时，进行封装。因为如果用in条件的时候，要将所有参数放到in集合中。
	 * @param cb
	 * @param expression
	 * @param value
	 * @return
	 */
	private static Predicate in(CriteriaBuilder cb, Path expression, Collection<Object>value){
		In in = cb.in(expression);
		for(Object object : value){
			in.value(object);
		}
		return in;
	}
}