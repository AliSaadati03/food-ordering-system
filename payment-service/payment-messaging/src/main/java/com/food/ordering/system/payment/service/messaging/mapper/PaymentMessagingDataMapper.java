package com.food.ordering.system.payment.service.messaging.mapper;


import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.order.service.domain.valueobject.PaymentOrderStatus;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class PaymentMessagingDataMapper {

    public PaymentRequest paymentRequestAvroModelToPaymentRequest(PaymentRequestAvroModel paymentRequestAvroModel) {
        return PaymentRequest.builder()
                .id(paymentRequestAvroModel.getId().toString())
                .sagaId(paymentRequestAvroModel.getSagaId().toString())
                .customerId(paymentRequestAvroModel.getCustomerId().toString())
                .orderId(paymentRequestAvroModel.getOrderId().toString())
                .price(paymentRequestAvroModel.getPrice())
                .createdAt(paymentRequestAvroModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(paymentRequestAvroModel.getPaymentOrderStatus().name()))
                .build();
    }

    public PaymentResponseAvroModel paymentCompletedEventToPaymentResponseAvroModel(PaymentFailedEvent domainEvent) {
        Payment payment = domainEvent.getPayment();
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setPaymentId(payment.getId().getValue())
                .setCustomerId(payment.getCustomerId().getValue())
                .setOrderId(payment.getOrderId().getValue())
                .setPrice(payment.getPrice().getAmount())
                .setCreatedAt(domainEvent.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.COMPLETED)
                .setFailureMessages(
                        domainEvent.getFailureMessages() == null
                                ? List.of()
                                : domainEvent.getFailureMessages()
                )
                .build();
    }

    public PaymentResponseAvroModel paymentCancelledEventToPaymentResponseAvroModel(PaymentFailedEvent domainEvent) {
        Payment payment = domainEvent.getPayment();
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setPaymentId(payment.getId().getValue())
                .setCustomerId(payment.getCustomerId().getValue())
                .setOrderId(payment.getOrderId().getValue())
                .setPrice(payment.getPrice().getAmount())
                .setCreatedAt(domainEvent.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.CANCELLED)
                .setFailureMessages(
                        domainEvent.getFailureMessages() == null
                                ? List.of()
                                : domainEvent.getFailureMessages()
                )
                .build();
    }

    public PaymentResponseAvroModel paymentFailedEventToPaymentResponseAvroModel(PaymentFailedEvent domainEvent) {
        Payment payment = domainEvent.getPayment();
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setPaymentId(payment.getId().getValue())
                .setCustomerId(payment.getCustomerId().getValue())
                .setOrderId(payment.getOrderId().getValue())
                .setPrice(payment.getPrice().getAmount())
                .setCreatedAt(domainEvent.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.FAILED)
                .setFailureMessages(
                        domainEvent.getFailureMessages() == null
                                ? List.of()
                                : domainEvent.getFailureMessages()
                )
                .build();
    }
}
