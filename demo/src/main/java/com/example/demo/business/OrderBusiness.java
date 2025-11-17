package com.example.demo.business;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.model.BaseEntity;
import com.example.demo.model.Item;
import com.example.demo.model.Order;
import com.example.demo.model.StockMovement;
import com.example.demo.model.User;
import com.example.demo.repository.OrderDao;
import com.example.demo.repository.OrderRepositoryInterface;
import com.example.demo.utils.EmailService;

@Service
@Qualifier("orderBusiness")
public class OrderBusiness extends BaseBusiness {

    private static final Logger logger = LoggerFactory.getLogger(OrderBusiness.class);

	@Autowired
	OrderDao dao;

	@Autowired
	OrderRepositoryInterface orderRepositoryInterface;

	@Autowired
	StockMovementBusiness stockMovementBusiness;

	@Autowired
	ItemBusiness itemBusiness;

	@Autowired
	UserBusiness userBusiness;

	@Autowired
	EmailService emailService;

	public BaseEntity create(Long userId, Long itemId, Long quantity) {

		User user = (User) userBusiness.find(Item.builder().id(userId).build());
		Item item = (Item) itemBusiness.find(Item.builder().id(itemId).build());
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
