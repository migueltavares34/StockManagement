package com.example.demo.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.request.CreateUserRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Table(name = "appUser")
public class User extends BaseEntity implements UserDetails {

	private static final long serialVersionUID = 1L;

	public User(CreateUserRequest createUserRequest) {
		this.name = createUserRequest.getUserName();
		this.email = createUserRequest.getEmail();
		this.password = createUserRequest.getPassword();
	}

	@Column(unique = true, nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
	private Role role;

//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return List.of(new SimpleGrantedAuthority(role.getName().name()));
//	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().name());
	        
	    return List.of(authority);
	}

	@Override
	public String getUsername() {
		return this.name;
	}
}
