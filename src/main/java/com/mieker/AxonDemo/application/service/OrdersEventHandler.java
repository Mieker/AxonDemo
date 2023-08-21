package com.mieker.AxonDemo.application.service;

import com.mieker.AxonDemo.application.events.OrderConfirmedEvent;
import com.mieker.AxonDemo.application.events.OrderCreatedEvent;
import com.mieker.AxonDemo.application.events.OrderShippedEvent;
import com.mieker.AxonDemo.application.queries.FindAllOrderedProductsQuery;
import com.mieker.AxonDemo.domain.model.Order;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrdersEventHandler {

    private final Map<String, Order> orders = new HashMap<>();

    @EventHandler
    public void on(OrderCreatedEvent event) {
        String orderId = event.getOrderId();
        orders.put(orderId, new Order(orderId, event.getProductId()));
    }

    @EventHandler
    public void on(OrderConfirmedEvent event) {
        String orderId = event.getOrderId();
        Order order = orders.get(orderId);
        order.setOrderConfirmed();
        orders.put(orderId, order);
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        String orderId = event.getOrderId();
        Order order = orders.get(orderId);
        order.setOrderShipped();
        orders.put(orderId, order);
    }

    @QueryHandler
    public List<Order> handle(FindAllOrderedProductsQuery query) {
        return new ArrayList<>(orders.values());
    }

}