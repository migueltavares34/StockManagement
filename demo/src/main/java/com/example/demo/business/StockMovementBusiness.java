package com.example.demo.business;

import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Item;
import com.example.demo.model.Order;
import com.example.demo.model.StockMovement;
import com.example.demo.repository.StockMovementDao;
import com.example.demo.repository.StockMovementRepositoryInterface;
import com.example.demo.utils.ManageLogs;

@Component
public class StockMovementBusiness {

	private static final Logger logger = ManageLogs.getLogger();

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
		stockMovement = dao.create(stockMovement);

		logger.info(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ " stockMovement created: " + stockMovement);
		return stockMovement;
	}

	public StockMovement findStockMovementByItem(long Id) {
		StockMovement stockMovement = StockMovement.builder().id(Id).build();
		return dao.find(stockMovement);
	}

	@Transactional
	public StockMovement add(Long itemId, Long quantity) {
		StockMovement stockMovement = stockMovementRepositoryInterface.getStockMovementByItem(itemId);

		if (stockMovement == null) {
			stockMovement = create(itemId, quantity);
		} else {
			stockMovement.addItems(quantity);

			logger.info(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
					+ " " + quantity + " items added to stockMovement: " + stockMovement);
		}

		stockMovement = orderBusiness.checkAndCloseOrders(stockMovement);
		return stockMovement.getQuantity() == 0 ? dao.delete(stockMovement) : dao.change(stockMovement);
	}

	public StockMovement find(long Id) {
		StockMovement stockMovement = StockMovement.builder().id(Id).build();
		return dao.find(stockMovement);
	}

	public StockMovement change(long Id, long quantity) {
		return change(StockMovement.builder().id(Id).quantity(quantity).build());
	}

	public StockMovement change(StockMovement stockMovement) {
		stockMovement = dao.change(stockMovement);

		logger.info(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ " stockMovement changed: " + stockMovement);
		return stockMovement;
	}

	public StockMovement delete(long id) {
		StockMovement stockMovement = StockMovement.builder().id(id).build();
		stockMovement = dao.delete(stockMovement);
		return stockMovement;
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
			delete(stockMovement.getId());

			logger.info(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
					+ " stockMovement is closed: " + order);

			return true;
		}
		return false;
	}
}
