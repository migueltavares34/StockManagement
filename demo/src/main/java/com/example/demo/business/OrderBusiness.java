package com.example.demo.business;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.Item;
import com.example.demo.model.Order;
import com.example.demo.model.StockMovement;
import com.example.demo.model.User;
import com.example.demo.repository.OrderDao;
import com.example.demo.repository.OrderRepositoryInterface;
import com.example.demo.utils.EmailService;
import com.example.demo.utils.ManageLogs;

@Component
public class OrderBusiness {

	private static final Logger logger = ManageLogs.getLogger();

	@Autowired
	OrderDao dao;

	@Autowired
	OrderRepositoryInterface orderRepositoryInterface;

	@Autowired
	StockMovementBusiness stockMovementBusiness;

	@Autowired
	EmailService emailService;

	public Order create(Long userId, Long itemId, Long quantity) {

		User user = User.builder().id(userId).build();
		Item item = Item.builder().id(itemId).build();
		Order order = Order.builder().user(user).item(item).quantity(quantity).creationDate(new Date()).build();

		if (stockMovementBusiness.orderCanBefulfilled(order)) {

			logger.info(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
					+ " order is fullfilled: " + order);

			sendEmail(order);
			order.setQuantity(0L);
			return order;
		}

		logger.info(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ " order was created: " + order);
		return dao.create(order);
	}

	public Order find(long Id) {
		Order order = Order.builder().id(Id).build();
		return dao.find(order);
	}

	public Order change(long Id, long quantity) {
		Order order;

		order = Order.builder().id(Id).quantity(quantity).build();
		order = dao.change(order);

		logger.info(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ " order was changed: " + order);
		return order;
	}

	public Order delete(long id) {
		return delete(Order.builder().id(id).build());
	}

	public Order delete(Order order) {
		return dao.delete(order);
	}

	public StockMovement checkAndCloseOrders(StockMovement stockMovement) {
		List<Order> ordersList = orderRepositoryInterface.getOrdersByItem(stockMovement.getItem().getId());

		for (Order order : ordersList) {

			if (order.getQuantity() <= stockMovement.getQuantity()) {
				closeOrder(order);

				stockMovement.subtractItems(order.getQuantity());

			} else {
				return stockMovement;
			}
		}

		return stockMovement;
	}

	private void closeOrder(Order order) {
		sendEmail(order);
		delete(order);
		logger.info(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ " order is fullfilled: " + order);
	}

	private void sendEmail(Order order) {
		emailService.sendEmail(order.getUser().getEmail(), "Order completed",
				"Your order for " + order.getQuantity() + " " + order.getItem().getName() + " is completed.");
	}

}
