package com.example.demo.repository;

import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.BaseEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class SessionFactory {

	@PersistenceContext
	private EntityManager entityManager;


	@Transactional
	public void inTransactionConsumer(Consumer<EntityManager> work) {
		work.accept(entityManager);
	}
	
	@Transactional
	public BaseEntity inTransactionFunction(Function<EntityManager,BaseEntity> work) {
		return work.apply(entityManager);
	}
	
}
