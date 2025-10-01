import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import dto.create.CreateOrderCommand;
import dto.create.CreateOrderResponse;
import lombok.extern.slf4j.Slf4j;
import mapper.OrderDataMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ports.output.messgae.publisher.OrderCreatedPaymentRequestMessagePublisher;

@Component
@Slf4j
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper, OrderDataMapper orderDataMapper, OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        final OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(),"Order created successfully");
    }
}
