package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Item;

public interface ItemRepositoryInterface extends JpaRepository<Item, Long> {
}
