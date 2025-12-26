package com.example.demo.security;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.example.demo.model.Role;
import com.example.demo.model.RoleEnum;
import com.example.demo.repository.RoleRepositoryInterface;

@Component
public class Seeder implements ApplicationListener<ContextRefreshedEvent> {
	private final RoleRepositoryInterface RoleRepositoryInterface;

	public Seeder(RoleRepositoryInterface roleRepositoryInterface) {
		this.RoleRepositoryInterface = roleRepositoryInterface;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		this.loadRoles();
	}

	private void loadRoles() {
		RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.USER, RoleEnum.ADMIN, RoleEnum.SUPER_ADMIN };
		Map<RoleEnum, String> roleDescriptionMap = Map.of(RoleEnum.USER, "Default user role", RoleEnum.ADMIN,
				"Administrator role", RoleEnum.SUPER_ADMIN, "Super Administrator role");

		Arrays.stream(roleNames).forEach((roleName) -> {
			Optional<Role> optionalRole = RoleRepositoryInterface.findByName(roleName);

			optionalRole.ifPresentOrElse(System.out::println, () -> {
				Role roleToCreate = Role.builder().name(roleName).description(roleDescriptionMap.get(roleName)).build();

				RoleRepositoryInterface.save(roleToCreate);
			});
		});
	}
}