package com.mieker.AxonDemo.model.command;

import com.mieker.AxonDemo.application.commands.CreateOrderCommand;
import com.mieker.AxonDemo.application.commands.ShipOrderCommand;
import com.mieker.AxonDemo.application.events.OrderConfirmedEvent;
import com.mieker.AxonDemo.application.events.OrderCreatedEvent;
import com.mieker.AxonDemo.application.events.OrderShippedEvent;
import com.mieker.AxonDemo.domain.model.command.OrderAggregate;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class OrderAggregateTest {

    private FixtureConfiguration<OrderAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(OrderAggregate.class);
    }

    @Test
    void shouldProduceOrderCreatedEventAfterCreateOrderCommand() {
        String orderId = UUID.randomUUID().toString();
        String productId = "Deluxe Chair";
        fixture.givenNoPriorActivity()
                .when(new CreateOrderCommand(orderId, productId))
                .expectEvents(new OrderCreatedEvent(orderId, productId));
    }

    @Test
    void shouldLogErrorAfterShipOrderCommandWhileOrderIsNotConfirmed() {
        String orderId = UUID.randomUUID().toString();
        String productId = "Deluxe Chair";
        fixture.given(new OrderCreatedEvent(orderId, productId))
                .when(new ShipOrderCommand(orderId))
                .expectException(IllegalCallerException.class);
    }

    @Test
    void shouldProduceOrderShippedEventAfterOrderConfirmedEvent() {
        String orderId = UUID.randomUUID().toString();
        String productId = "Deluxe Chair";
        fixture.given(new OrderCreatedEvent(orderId, productId), new OrderConfirmedEvent(orderId))
                .when(new ShipOrderCommand(orderId))
                .expectEvents(new OrderShippedEvent(orderId));
    }
}