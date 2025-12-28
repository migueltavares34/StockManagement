package com.example.demo.model.request;

public record CreateOrderRequest(Long userId, Long itemId, Long quantity) {
}
