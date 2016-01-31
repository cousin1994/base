package com.cousin.util.data.jpa.factory;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

import com.cousin.util.data.jpa.repository.BaseJpaRepository;
import com.cousin.util.data.jpa.repository.BaseJpaSupportRepository;

/**
 * 拓展化的JPA的工厂，可以创建工厂化的实例
* @author 戴嘉诚 E-mail:a773807943@gmail.com
* @version 创建时间： 2016年1月31日 上午3:18:38
*/
public class BaseJpaRepositoryFactory extends JpaRepositoryFactory {

	private EntityManager entityManager;
	
	public BaseJpaRepositoryFactory(EntityManager entityManager) {
		super(entityManager);
		this.entityManager = entityManager;
	}

	@Override
	protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(
			RepositoryInformation information, EntityManager entityManager) {
		//return super.getTargetRepository(information, entityManager);
		return new BaseJpaSupportRepository<>(information.getDomainType(), entityManager);
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return BaseJpaRepository.class;
	}
	
	
	/*protected<T, ID extends Serializable> JpaRepository<?,?> getTargetRepository(RepositoryMetadata metadata,EntityManager entityManager) {
		
		return null;
	}*/
	
	
	
	
	

}
