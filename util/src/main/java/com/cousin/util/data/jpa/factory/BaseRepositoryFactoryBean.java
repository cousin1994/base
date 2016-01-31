package com.cousin.util.data.jpa.factory;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * 生成工厂实例，从而生产BaseRepository的实例对象
* @author 戴嘉诚 E-mail:a773807943@gmail.com
* @version 创建时间： 2016年1月31日 上午4:27:57
*/
public class BaseRepositoryFactoryBean<T extends JpaRepository<Object, Serializable>> extends JpaRepositoryFactoryBean<T, Object, Serializable> {

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new BaseJpaRepositoryFactory(entityManager);
	}
		
}
