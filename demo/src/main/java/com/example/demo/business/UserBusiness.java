package com.example.demo.business;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserDao;

@Service
@Qualifier("userBusiness")
public class UserBusiness extends BaseBusiness {

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
