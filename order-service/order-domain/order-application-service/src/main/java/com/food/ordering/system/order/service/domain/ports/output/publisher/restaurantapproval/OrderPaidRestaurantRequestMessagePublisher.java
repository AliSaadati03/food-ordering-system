package com.food.ordering.system.order.service.domain.ports.output.publisher.restaurantapproval;

import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.event.publisher.DomainEventPublisher;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
    void publish(OrderPaidEvent event);
}
