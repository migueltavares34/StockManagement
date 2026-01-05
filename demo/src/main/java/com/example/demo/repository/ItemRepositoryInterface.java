package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Item;

public interface ItemRepositoryInterface extends JpaRepository<Item, Long> {
	Optional<Item> findByName(String itemName);
}
