package com.example.demo.security;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.repository.UserRepositoryInterface;

import io.jsonwebtoken.SignatureAlgorithm;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration {
	
    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    
	@Autowired
	UserRepositoryInterface userRepository;

	@Value("${security.jwt.secret-key}")
	private String secretKey;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/actuator/**", "/auth/**", "/swagger-ui/**", "/v3/**", "/graphiql", "/graphql", "/favicon")
						.permitAll().anyRequest().authenticated())
				.oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))// validates JWTs
	            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); 
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
		return NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
	}

	@Bean
	UserDetailsService userDetailsService() {
		return name -> userRepository.findByName(name);
	}

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());

        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    
	@Bean
	public RoleHierarchy roleHierarchy() {		
	    RoleHierarchyImpl roleHierarchy = RoleHierarchyImpl.fromHierarchy("SUPER_ADMIN > ADMIN \n ADMIN > USER");
	    return roleHierarchy;
	}
}
