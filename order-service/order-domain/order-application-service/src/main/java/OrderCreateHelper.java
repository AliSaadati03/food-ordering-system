import com.food.ordering.system.order.service.domain.OrderDomainService;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import dto.create.CreateOrderCommand;
import lombok.extern.slf4j.Slf4j;
import mapper.OrderDataMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ports.output.repository.CustomerRepository;
import ports.output.repository.OrderRepository;
import ports.output.repository.RestaurantRepository;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class OrderCreateHelper {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateHelper(CustomerRepository customerRepository, OrderDomainService orderDomainService, OrderRepository orderRepository, RestaurantRepository restaurantRepository, OrderDataMapper orderDataMapper) {
        this.customerRepository = customerRepository;
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        final OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order,restaurant);
        saveOrder(order);
        log.info("Order is created with id : {}", orderCreatedEvent.getOrder().getId().getValue());
        return orderCreatedEvent;
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {

        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        final Optional<Restaurant> restaurantInformation = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantInformation.isEmpty()) {
            log.warn("Could not find restaurant information for restaurant id {}", createOrderCommand.getRestaurantId());
            throw new OrderDomainException("Restaurant not found restaurant with restaurant id " + createOrderCommand.getRestaurantId());
        }

        return restaurantInformation.get();

    }

    private void checkCustomer(UUID customerId) {
        final Optional<Customer> customer = customerRepository.findCustomer(customerId);

        if (customer.isEmpty()) {

            log.warn("Could not find customer with customer id {}", customerId);
            throw new OrderDomainException("Could not find customer with customer id " + customer);
        }
    }

    private Order saveOrder(Order order) {

        final Order orderResult = orderRepository.save(order);
        if (orderResult == null) {
            log.error("Could not save order");
            throw new OrderDomainException("Could not save order");
        }
        log.info("Order is saved with id {}", orderResult.getId().getValue());
        return orderResult;
    }

}
