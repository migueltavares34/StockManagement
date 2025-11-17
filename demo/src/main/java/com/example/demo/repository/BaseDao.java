package com.example.demo.repository;

import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BaseEntity;

import jakarta.persistence.EntityManager;

@Repository
public class BaseDao {

	@Autowired
	private SessionFactory sessionFactory;

	public BaseEntity create(BaseEntity entity) {
		return presistEntity(entity);
	}

	public BaseEntity find(BaseEntity entity) {
		return findEntity(entity);
	}

	public BaseEntity delete(BaseEntity entity) {
		return deleteEntity(findEntity(entity));
	}

	public BaseEntity change(BaseEntity entity) {
		return changeEntity(entity);
	}

	public BaseEntity presistEntity(BaseEntity entity) {

		try {
			Consumer<EntityManager> consumer = em -> em.persist(entity);
			sessionFactory.inTransactionConsumer(consumer);

		} catch (Exception e) {
			entity.setErrorMessage(e.getMessage());
		}
		return entity;
	}

	public BaseEntity findEntity(BaseEntity entity) {
		BaseEntity returnedEntity = BaseEntity.builder().build();

		try {
			Function<EntityManager, BaseEntity> findEntity = em -> em.find(entity.getClass(), entity.getId());
			returnedEntity = sessionFactory.inTransactionFunction(findEntity);

		} catch (Exception e) {
			entity.setErrorMessage(e.getMessage());
		}
		return returnedEntity;
	}

	public BaseEntity changeEntity(BaseEntity entity) {
		try {
			Function<EntityManager, BaseEntity> changeEntity = em -> em.merge(entity);
			return sessionFactory.inTransactionFunction(changeEntity);

		} catch (Exception e) {
			return BaseEntity.builder().errorMessage(e.getMessage()).build();
		}
	}

	public BaseEntity deleteEntity(BaseEntity entity) {
		try {
			Consumer<EntityManager> findEntity = em -> em.remove(entity);
			sessionFactory.inTransactionConsumer(findEntity);

		} catch (Exception e) {
			entity.setErrorMessage(e.getMessage());
		}
		return entity;
	}
}
