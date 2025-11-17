package com.example.demo.business;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;
import com.example.demo.repository.UserDao;


@Component
public class UserBusiness {

    private static final Logger logger = LoggerFactory.getLogger(UserBusiness.class);
    
	@Autowired
	UserDao dao;

	public User create(String name, String email) {
		User appUser;
		if (!isValidEmailAddress(email)) {
			appUser = User.builder().build();
			appUser.setErrorMessage("Invalid email address");
			return appUser;
		}

		appUser = User.builder().name(name).email(email).build();
		appUser = dao.create(appUser);
		return appUser;
	}

	public User find(long userId) {
		User appUser= User.builder().id(userId).build();
		return dao.find(appUser);
	}
	
	public User change(long userId, String email) {
		User appUser;
		if (!isValidEmailAddress(email)) {
			appUser = User.builder().build();
			appUser.setErrorMessage("Invalid email address");
			return appUser;
		}

		appUser = User.builder().id(userId).email(email).build();
		appUser = dao.change(appUser);
		return appUser;
	}

	public User delete(long userId) {
		User appUser;

		appUser = User.builder().id(userId).build();
		appUser = dao.delete(appUser);
		return appUser;
	}
	


	// Method to check if the email is valid
	public static boolean isValidEmailAddress(String email) {

		// Regular expression to match valid email formats
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

		// Compile the regex
		Pattern p = Pattern.compile(emailRegex);

		// Check if email matches the pattern
		return email != null && p.matcher(email).matches();
	}
}
