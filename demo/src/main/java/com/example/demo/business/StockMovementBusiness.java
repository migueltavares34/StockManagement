package com.example.demo.business;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Item;
import com.example.demo.model.Order;
import com.example.demo.model.StockMovement;
import com.example.demo.repository.StockMovementDao;
import com.example.demo.repository.StockMovementRepositoryInterface;

import lombok.extern.slf4j.Slf4j;

@Service
@Qualifier("stockMovementBusiness")
@Slf4j
public class StockMovementBusiness extends BaseBusiness {

	@Autowired
	StockMovementDao dao;

	@Autowired
	StockMovementRepositoryInterface stockMovementRepositoryInterface;

	@Autowired
	OrderBusiness orderBusiness;

	public StockMovement create(Long itemId, Long quantity) {
		Item item = Item.builder().id(itemId).build();
		StockMovement stockMovement = StockMovement.builder().item(item).quantity(quantity).creationDate(new Date())
				.build();
		stockMovement = (StockMovement) dao.create(stockMovement);

		log.info(" stockMovement created: " + stockMovement);
		return stockMovement;
	}

	@Transactional
	public StockMovement add(Long itemId, Long quantity) {
		Pair<StockMovement, Boolean> result = getStockMovementFromDbOrBuildNew(itemId, quantity);

		StockMovement stockMovement = result.getFirst();

		Long stockMovementItemsQuantity = stockMovement.getQuantity();
		result = Pair.of(orderBusiness.checkAndCloseOrders(stockMovement), result.getSecond());

		return updateDb(result, stockMovementItemsQuantity);
	}

	private StockMovement updateDb(Pair<StockMovement, Boolean> result, Long stockMovementItemsQuantity) {

		StockMovement stockMovement = result.getFirst();
		boolean stockMovementNotInDb = result.getSecond();

		if (stockMovementNotInDb && stockMovement.getQuantity() != 0) {

			stockMovement = (StockMovement) create(stockMovement);
			log.info("StockMovement was created in DB: " + stockMovement);

		} else if (stockMovementItemsQuantity.compareTo(stockMovement.getQuantity()) != 0
				&& stockMovement.getQuantity() != 0) {

			stockMovement = dao.change(stockMovement);
			log.info("StockMovement's quantity was updated in DB: " + stockMovement);

		} else if (stockMovement.getQuantity() == 0) {

			log.info("StockMovement is empty after closing orders and it will be removed form DB: " + stockMovement);
			stockMovement = dao.delete(stockMovement);
		}

		return stockMovement;
	}

	private Pair<StockMovement, Boolean> getStockMovementFromDbOrBuildNew(Long itemId, Long quantity) {
		StockMovement stockMovement = stockMovementRepositoryInterface.getStockMovementByItem(itemId);
		boolean stockMovementNotInDb = stockMovement == null;
		if (stockMovementNotInDb) {
			Item item = Item.builder().id(itemId).build();
			stockMovement = StockMovement.builder().item(item).quantity(quantity).build();

			log.info(" stockMovement not yet in DB: " + stockMovement);
		} else {
			stockMovement.addItems(quantity);

			log.info(" stockMovement already in DB and " + quantity + " items where added: " + stockMovement);
		}
		return Pair.of(stockMovement, stockMovementNotInDb);
	}

	public boolean orderCanBefulfilled(Order order) {

		StockMovement stockMovement = stockMovementRepositoryInterface.getStockMovementByItem(order.getItem().getId());

		if (stockMovement == null) {
			return false;
		} else if (stockMovement.getQuantity() > order.getQuantity()) {
			stockMovement.subtractItems(order.getQuantity());
			change(stockMovement);
			return true;
		} else if (stockMovement.getQuantity() == order.getQuantity()) {
			delete(StockMovement.builder().id(stockMovement.getId()).build());

			log.info("stockMovement is closed: " + order);

			return true;
		}
		return false;
	}
}
