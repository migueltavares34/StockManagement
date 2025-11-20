package com.example.demo.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.example.demo.model.BaseEntity;
import com.example.demo.model.Item;
import com.example.demo.model.StockMovement;
import com.example.demo.repository.StockMovementDao;
import com.example.demo.repository.StockMovementRepositoryInterface;

@ExtendWith(MockitoExtension.class)
public class StockMovementBusinessTest {

	@Mock
	private StockMovementDao stockMovementDao;

	@Mock
	private StockMovementRepositoryInterface stockMovementRepositoryInterface;

	@Mock
	private OrderBusiness orderBusiness;

	@InjectMocks
	private StockMovementBusiness business;

	@Test
	void testCreateStockMovement() {
		Item item = Item.builder().id(5L).build();
		StockMovement stockMovementDb = StockMovement.builder().item(item).quantity(1L).build();
		when(stockMovementDao.create(any(BaseEntity.class))).thenReturn(stockMovementDb);

		StockMovement stockMovement = business.create(1L, 1L);

		assertEquals(5L, stockMovement.getItem().getId());
	}

	@Test
	void testAddItemToStockMovementDoesntExistAndNoOrder() {
		when(stockMovementRepositoryInterface.getStockMovementByItem(anyLong())).thenReturn(null);

		Item item = Item.builder().id(5L).build();
		StockMovement stockMovement = StockMovement.builder().id(6L).item(item).quantity(2L).build();

//BaseEntity mockeBaseEntity = mock(BaseEntity.class);

		when(business.create(any(StockMovement.class))).thenReturn(stockMovement);

		when(orderBusiness.checkAndCloseOrders(any(StockMovement.class))).thenReturn(stockMovement);

		assertEquals(2L, business.add(5L, 1L).getQuantity());
		verify(stockMovementDao,times(1)).change(any());
		verify(stockMovementDao,times(0)).delete(any());		
	}

	@Test
	void testAddItemToStockMovementDoesntExistAndWithOrder() {
		when(stockMovementRepositoryInterface.getStockMovementByItem(anyLong())).thenReturn(null);

		Item item = Item.builder().id(5L).build();
		StockMovement stockMovement = StockMovement.builder().id(6L).item(item).quantity(2L).build();

		when(business.create(any(StockMovement.class))).thenReturn(stockMovement);

		Item item2 = Item.builder().id(5L).build();
		StockMovement stockMovement2 = StockMovement.builder().id(6L).item(item2).quantity(1L).build();

		when(orderBusiness.checkAndCloseOrders(any(StockMovement.class))).thenReturn(stockMovement2);
		
		when(stockMovementDao.change(any(StockMovement.class))).thenReturn(stockMovement2);

		assertEquals(1L, business.add(5L, 1L).getQuantity());
		verify(stockMovementDao,times(1)).change(any());
		verify(stockMovementDao,times(0)).delete(any());		
	}

	@Test
	void testAddItemToStockMovementDoesntExistAndWithOrderAndClose() {
		when(stockMovementRepositoryInterface.getStockMovementByItem(anyLong())).thenReturn(null);

		Item item = Item.builder().id(5L).build();
		StockMovement stockMovement = StockMovement.builder().id(6L).item(item).quantity(2L).build();

		when(business.create(any(StockMovement.class))).thenReturn(stockMovement);
		stockMovement.setQuantity(0L);
		when(orderBusiness.checkAndCloseOrders(any(StockMovement.class))).thenReturn(stockMovement);

		assertEquals(0L, business.add(5L, 1L).getQuantity());
	}

	@Test
	void testAddItemToStockMovementExistAndNoOrder() {
		Item item = Item.builder().id(5L).build();
		StockMovement stockMovement = StockMovement.builder().id(6L).item(item).quantity(2L).build();

		when(stockMovementRepositoryInterface.getStockMovementByItem(anyLong())).thenReturn(stockMovement);

		when(orderBusiness.checkAndCloseOrders(any(StockMovement.class))).thenReturn(stockMovement);

		assertEquals(3L, business.add(5L, 1L).getQuantity());
	}
}
