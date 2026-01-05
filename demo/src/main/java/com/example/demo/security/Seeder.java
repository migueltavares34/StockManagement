package com.example.demo.security;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.example.demo.model.Item;
import com.example.demo.model.Order;
import com.example.demo.model.Role;
import com.example.demo.model.RoleEnum;
import com.example.demo.model.StockMovement;
import com.example.demo.model.User;
import com.example.demo.repository.ItemRepositoryInterface;
import com.example.demo.repository.OrderRepositoryInterface;
import com.example.demo.repository.RoleRepositoryInterface;
import com.example.demo.repository.StockMovementRepositoryInterface;
import com.example.demo.repository.UserRepositoryInterface;

@Component
public class Seeder implements ApplicationListener<ContextRefreshedEvent> {
	private final RoleRepositoryInterface roleRepositoryInterface;
	private final UserRepositoryInterface userRepositoryInterface;
	private final ItemRepositoryInterface itemRepositoryInterface;
	private final StockMovementRepositoryInterface stockMovementRepositoryInterface;
	private final OrderRepositoryInterface orderRepositoryInterface;
	private final PasswordEncoder passwordEncoder;
	private String generalEmail;

	public Seeder(RoleRepositoryInterface roleRepositoryInterface, UserRepositoryInterface userRepositoryInterface,
			ItemRepositoryInterface itemRepositoryInterface,
			StockMovementRepositoryInterface stockMovementRepositoryInterface,
			OrderRepositoryInterface orderRepositoryInterface, PasswordEncoder passwordEncoder) {
		this.roleRepositoryInterface = roleRepositoryInterface;
		this.userRepositoryInterface = userRepositoryInterface;
		this.itemRepositoryInterface = itemRepositoryInterface;
		this.stockMovementRepositoryInterface = stockMovementRepositoryInterface;
		this.orderRepositoryInterface = orderRepositoryInterface;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		Properties prop = new Properties();

		try (InputStream input = Seeder.class.getClassLoader().getResourceAsStream("application.properties")) {

			if (input == null) {
				System.out.println("Sorry, unable to find config.properties");
				return;
			}

			prop.load(input);

			generalEmail = prop.getProperty("spring.mail.username");

			System.out.println("general email: " + generalEmail);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		loadRoles();
		loadUsers();
		loadItems();
		loadStockMovements();
		loadOrders();
	}

	private void loadRoles() {
		RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.USER, RoleEnum.ADMIN, RoleEnum.SUPER_ADMIN };
		Map<RoleEnum, String> roleDescriptionMap = Map.of(RoleEnum.USER, "Default user role", RoleEnum.ADMIN,
				"Administrator role", RoleEnum.SUPER_ADMIN, "Super Administrator role");

		Arrays.stream(roleNames).forEach((roleName) -> {
			Optional<Role> optionalRole = roleRepositoryInterface.findByName(roleName);

			optionalRole.ifPresentOrElse(System.out::println, () -> {
				Role roleToCreate = Role.builder().name(roleName).description(roleDescriptionMap.get(roleName)).build();

				roleRepositoryInterface.save(roleToCreate);
			});
		});
	}

	private void loadUsers() {
		List<User> userList = new ArrayList<>();
		Optional<Role> optionalRole = roleRepositoryInterface.findByName(RoleEnum.SUPER_ADMIN);

		optionalRole.ifPresent(role -> {
			User user = User.builder().email(generalEmail).name("Super Admin")
					.password(passwordEncoder.encode("superadminpassword")).role(role).build();
			userList.add(user);
		});

		optionalRole = roleRepositoryInterface.findByName(RoleEnum.ADMIN);

		optionalRole.ifPresent(role -> {
			User user = User.builder().email(generalEmail).name("Admin")
					.password(passwordEncoder.encode("adminpassword")).role(role).build();
			userList.add(user);
		});

		optionalRole = roleRepositoryInterface.findByName(RoleEnum.USER);

		optionalRole.ifPresent(role -> {
			User user = User.builder().email(generalEmail).name("User")
					.password(passwordEncoder.encode("userpassword")).role(role).build();
			userList.add(user);
		});

		userList.stream().forEach(userToCreate -> {
			User dbUser = userRepositoryInterface.findByName(userToCreate.getName());

			if (ObjectUtils.isEmpty(dbUser)) {
				userRepositoryInterface.save(userToCreate);
			}
		});
	}

	private void loadItems() {
		List<Item> itemsList = new ArrayList<>();

		checkAndAddItem("stappler", itemsList);
		checkAndAddItem("tape", itemsList);
		checkAndAddItem("pensil", itemsList);

		itemsList.stream().forEach(itemToCreate -> {
			Optional<Item> dbItem = itemRepositoryInterface.findByName(itemToCreate.getName());

			if (dbItem.isEmpty()) {
				itemRepositoryInterface.save(itemToCreate);
			}
		});
	}

	private void checkAndAddItem(String itemName, List<Item> itemsList) {
		Optional<Item> optionalItem = itemRepositoryInterface.findByName(itemName);

		if (optionalItem.isEmpty()) {
			itemsList.add(Item.builder().name(itemName).build());
		}
	}

	private void loadStockMovements() {
		List<Item> itemsList = itemRepositoryInterface.findAll();

		itemsList.stream().forEach(item -> {
			StockMovement stockMovement = stockMovementRepositoryInterface.getStockMovementByItem(item.getId());

			if (ObjectUtils.isEmpty(stockMovement)) {
				stockMovementRepositoryInterface
						.save(StockMovement.builder().item(item).quantity(100L).creationDate(new Date()).build());
			}
		});
	}

	private void loadOrders() {
		List<User> usersList = userRepositoryInterface.findAll();

		if (CollectionUtils.isEmpty(usersList)) {
			return;
		}

		User user = usersList.getFirst();

		List<Item> itemsList = itemRepositoryInterface.findAll();

		itemsList.stream().forEach(item -> {
			List<Order> orderList = orderRepositoryInterface.getOrdersByItem(item.getId());

			if (CollectionUtils.isEmpty(orderList)) {
				orderRepositoryInterface
						.save(Order.builder().user(user).item(item).quantity(3L).creationDate(new Date()).build());
			}
		});
	}
}