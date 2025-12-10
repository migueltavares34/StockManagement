package com.example.demo.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.model.BaseEntity;
import com.example.demo.repository.BaseDao;

@Service
@Qualifier("baseBusiness")
public class BaseBusiness {

	@Autowired
	BaseDao baseDao;

	public BaseEntity create(BaseEntity entity) {
		return baseDao.create(entity);
	}

	public BaseEntity find(BaseEntity entity) {
		return baseDao.find(entity);
	}

	public BaseEntity update(BaseEntity entity) {
		return baseDao.update(entity);
	}

	public BaseEntity delete(BaseEntity entity) {
		return baseDao.delete(entity);
	}

}
