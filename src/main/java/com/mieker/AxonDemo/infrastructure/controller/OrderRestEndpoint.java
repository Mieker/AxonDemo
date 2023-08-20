package com.mieker.AxonDemo.infrastructure.controller;

import com.mieker.AxonDemo.application.commands.ConfirmOrderCommand;
import com.mieker.AxonDemo.application.commands.CreateOrderCommand;
import com.mieker.AxonDemo.application.commands.ShipOrderCommand;
import com.mieker.AxonDemo.application.queries.FindAllOrderedProductsQuery;
import com.mieker.AxonDemo.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class OrderRestEndpoint {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping("/ship-order")
    public CompletableFuture<Void> shipOrder() {
        String orderId = UUID.randomUUID().toString();
        return commandGateway.send(new CreateOrderCommand(orderId, "Deluxe Chair"))
                .thenCompose(result -> commandGateway.send(new ConfirmOrderCommand(orderId)))
                .thenCompose(result -> commandGateway.send(new ShipOrderCommand(orderId)));
    }

    @GetMapping("/all-orders")
    public CompletableFuture<List<Order>> findAllOrders() {
        return queryGateway.query(new FindAllOrderedProductsQuery(), ResponseTypes.multipleInstancesOf(Order.class));
    }
}