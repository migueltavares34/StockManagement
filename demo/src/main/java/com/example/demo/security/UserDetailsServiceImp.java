package com.example.demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepositoryInterface;

@Service
public class  UserDetailsServiceImp implements UserDetailsService {

	private final UserRepositoryInterface userRepositoryInterface;

	public UserDetailsServiceImp(UserRepositoryInterface userRepositoryInterface) {
		this.userRepositoryInterface = userRepositoryInterface;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepositoryInterface.findByName(username);
	}
}
