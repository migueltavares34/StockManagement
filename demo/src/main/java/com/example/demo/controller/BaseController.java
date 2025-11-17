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

	abstract public ResponseEntity<String> find(long id);

	abstract public ResponseEntity<String> delete(long id);

	public ResponseEntity<String> create(BaseEntity entity) {
		try {
			entity = business.create(entity);
		} catch (Exception e) {
			entity.setErrorMessage(e.getMessage());
		}

		return handleResult(entity, "Entity created");
	}

	public ResponseEntity<String> find(BaseEntity entity) {
		try {
			entity = business.find(entity);
		} catch (Exception e) {
			entity.setErrorMessage(e.getMessage());
		}

		return handleResult(entity, "Entity found");
	}

	public ResponseEntity<String> change(BaseEntity entity) {
		try {
			entity = business.change(entity);
		} catch (Exception e) {
			entity.setErrorMessage(e.getMessage());
		}

		return handleResult(entity, "Entity name changed");
	}

	public ResponseEntity<String> delete(BaseEntity entity) {
		try {
			entity = business.delete(entity);
		} catch (Exception e) {
			entity.setErrorMessage(e.getMessage());
		}
		return handleResult(entity, "Entity deleted");
	}

	protected ResponseEntity<String> handleResult(BaseEntity entity, String successMessage) {

		if (entity == null) {
			log.error("Entity is null");
			return new ResponseEntity<String>("Entity not found", HttpStatus.BAD_REQUEST);

		} else if (StringUtils.isNotBlank(entity.getErrorMessage())) {
			log.error(entity.getErrorMessage());
			return new ResponseEntity<String>(entity.getErrorMessage(), HttpStatus.BAD_REQUEST);
		}
		successMessage = successMessage + ": " + entity.toString();
		log.info(successMessage);
		return new ResponseEntity<String>(successMessage, HttpStatus.CREATED);
	}
}
