package com.example.demo.business;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.function.Consumers;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.dto.EmailDTO;
import com.example.demo.kafka.Producers;
import com.example.demo.model.BaseEntity;
import com.example.demo.model.Item;
import com.example.demo.model.Order;
import com.example.demo.model.StockMovement;
import com.example.demo.model.User;
import com.example.demo.repository.OrderDao;
import com.example.demo.repository.OrderRepositoryInterface;
import com.example.demo.utils.EmailService;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Qualifier("orderBusiness")
@Slf4j
public class OrderBusiness extends BaseBusiness {

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

	@Autowired
	Consumers consumers;

	public BaseEntity create(Long userId, Long itemId, Long quantity) {

		User user = (User) userBusiness.find(User.builder().id(userId).build());
		Item item = (Item) itemBusiness.find(Item.builder().id(itemId).build());
		Order order = Order.builder().user(user).item(item).quantity(quantity).creationDate(new Date()).build();

		if (stockMovementBusiness.orderCanBefulfilled(order)) {

			log.info(" order is fullfilled: " + order);

			sendEmail(order);
			order.setQuantity(0L);
			return order;
		}

		order = (Order) dao.create(order);

		if (StringUtils.isNotEmpty(order.getErrorMessage())) {
			log.info(" order was created: " + order);
		}

		return order;
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
		log.info(" order is fullfilled: " + order);
	}

	public void sendEmail(Order order) {
		ProducerRecord<String, EmailDTO> record = new ProducerRecord<>("my-first-topic", "key",
				EmailDTO.builder().notificationType("email").to(order.getUser().getEmail())
						.subject("Your order is fulfilled").messageBody("Your Order of " + order.getQuantity() + " "
								+ order.getItem().getName() + " has been fullfilled.")
						.build());
		Producers.sendMessage(record);
	}
}
