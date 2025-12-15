package com.example.demo.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.business.BaseBusiness;
import com.example.demo.model.BaseEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class BaseController {

	@Autowired
	@Qualifier("baseBusiness")
	BaseBusiness business;

	abstract public ResponseEntity<BaseEntity> read(long id);

	abstract public ResponseEntity<BaseEntity> delete(long id);

	public ResponseEntity<BaseEntity> create(BaseEntity entity) {
		try {
			entity = business.create(entity);
		} catch (Exception e) {
			entity.setErrorMessage(e.getMessage());
		}

		return handleResult(entity);
	}

	public ResponseEntity<BaseEntity> read(BaseEntity entity) {
		try {
			entity = business.find(entity);
		} catch (Exception e) {
			entity.setErrorMessage(e.getMessage());
		}

		return handleResult(entity);
	}

	public ResponseEntity<BaseEntity> update(BaseEntity entity) {
		try {
			entity = business.update(entity);
		} catch (Exception e) {
			entity.setErrorMessage(e.getMessage());
		}

		return handleResult(entity);
	}

	public ResponseEntity<BaseEntity> delete(BaseEntity entity) {
		try {
			entity = business.delete(entity);
		} catch (Exception e) {
			entity.setErrorMessage(e.getMessage());
		}
		return handleResult(entity);
	}

	protected ResponseEntity<BaseEntity> handleResult(BaseEntity entity) {

		if (entity == null) {
			log.error("Entity is null");
			entity = BaseEntity.builder().errorMessage("Entity not found").build();
			return new ResponseEntity<BaseEntity>(entity, HttpStatus.BAD_REQUEST);

		} else if (StringUtils.isNotBlank(entity.getErrorMessage())) {
			log.error(entity.getErrorMessage());
			return new ResponseEntity<BaseEntity>(entity, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<BaseEntity>(entity, HttpStatus.CREATED);
	}
}
