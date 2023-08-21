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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderRestEndpoint {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping("/create")
    public CompletableFuture<Void> createOrder() {
        String orderId = UUID.randomUUID().toString();
        return commandGateway.send(new CreateOrderCommand(orderId, "Soccer Ball"));
    }

    @PostMapping("/confirm/{orderId}")
    public CompletableFuture<Void> confirmOrder(@PathVariable String orderId) {
        return commandGateway.send(new ConfirmOrderCommand(orderId));
    }

    @PostMapping("/ship/{orderId}")
    public CompletableFuture<Void> shipOrder(@PathVariable String orderId) {
        return commandGateway.send(new ShipOrderCommand(orderId));
    }

    @GetMapping
    public CompletableFuture<List<Order>> findAllOrders() {
        return queryGateway.query(new FindAllOrderedProductsQuery(), ResponseTypes.multipleInstancesOf(Order.class));
    }
}