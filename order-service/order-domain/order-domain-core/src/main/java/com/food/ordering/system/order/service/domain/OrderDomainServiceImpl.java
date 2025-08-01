package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {
    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("order with id: {} is initialized", order.getId().getValue());
        return new OrderCreatedEvent(ZonedDateTime.now(ZoneId.of("UTC")), order);
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getItems().forEach(orderItem -> restaurant.getProducts().forEach(product -> {
            Product currentProduct = orderItem.getProduct();
            if (currentProduct.equals(product))
                product.updateConfirmedNameAndPrice(product.getName(), product.getPrice());
        }));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("restaurant with id: " + restaurant.getId().getValue() + " is not active.");
        }
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("order with id: {} is paid", order.getId().getValue());
        return new OrderPaidEvent(ZonedDateTime.now(ZoneId.of("UTC")), order);
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessage) {
        order.initCancel(failureMessage);
        log.info("order with id: {} is cancelling", order.getId().getValue());
        return new OrderCancelledEvent(ZonedDateTime.now(ZoneId.of("UTC")), order);
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessage) {
        order.cancel(failureMessage);
        log.info("order with id: {} is cancelled", order.getId().getValue());
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("order with id: {} is approved", order.getId().getValue());
    }
}
