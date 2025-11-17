package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.model.BaseEntity;
import com.example.demo.model.User;

@Repository
public class UserDao extends BaseDao {

	public User create(User appUser) {
		return (User) presistEntity(appUser);
	}

	public User find(User user) {
		return (User) findEntity(user);
	}

	public User change(User user) {
		BaseEntity returnedEntity = findEntity(user);
		((User) returnedEntity).setEmail(user.getEmail());
		return (User) changeEntity(returnedEntity);
	}

	public User delete(User user) {
		user = (User) findEntity(user);
		return (User) deleteEntity(user);
	}
}
