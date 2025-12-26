package com.example.demo.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.model.Item;
import com.example.demo.model.Order;
import com.example.demo.model.StockMovement;
import com.example.demo.repository.BaseDao;
import com.example.demo.repository.StockMovementDao;
import com.example.demo.repository.StockMovementRepositoryInterface;

//@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class StockMovementBusinessTest {

	@Mock
	private StockMovementDao stockMovementDao;

	@Mock
	private StockMovementRepositoryInterface stockMovementRepositoryInterface;

	@Mock
	private OrderBusiness orderBusiness;

	@Mock
	private BaseDao baseDao;

	@InjectMocks
	private StockMovementBusiness business;

	@InjectMocks
	private BaseBusiness baseBusiness;

	@Test
	void testOrderCanBefulfilledWithNoStock() {
		when(stockMovementRepositoryInterface.getStockMovementByItem(anyLong())).thenReturn(null);
		Item item = Item.builder().id(1L).build();
		Order order = Order.builder().item(item).build();
		assertThat(!business.orderCanBefulfilled(order));
	}

	@Test
	void testOrderCanBefulfilledWithStock() {

		StockMovement stockMovement = StockMovement.builder().quantity(2L).build();
		when(stockMovementRepositoryInterface.getStockMovementByItem(anyLong())).thenReturn(stockMovement);

		Item item = Item.builder().id(1L).build();
		Order order = Order.builder().item(item).quantity(1L).build();

		assertThat(business.orderCanBefulfilled(order));

		verify(baseDao, times(1)).update(any(StockMovement.class));
	}

	@Test
	void testOrderCanBefulfilledWithSameStock() {

		StockMovement stockMovement = StockMovement.builder().quantity(1L).build();
		when(stockMovementRepositoryInterface.getStockMovementByItem(anyLong())).thenReturn(stockMovement);

		Item item = Item.builder().id(1L).build();
		Order order = Order.builder().item(item).quantity(1L).build();

		assertThat(business.orderCanBefulfilled(order));

		verify(baseDao, times(1)).delete(any(StockMovement.class));
	}

	@Test
	void testOrderCanBefulfilledWithNotEnoughStock() {

		StockMovement stockMovement = StockMovement.builder().quantity(1L).build();
		when(stockMovementRepositoryInterface.getStockMovementByItem(anyLong())).thenReturn(stockMovement);

		Item item = Item.builder().id(1L).build();
		Order order = Order.builder().item(item).quantity(2L).build();

		assertThat(!business.orderCanBefulfilled(order));
	}
}
