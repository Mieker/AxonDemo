package com.mieker.AxonDemo.application.events;

import lombok.Data;

@Data
public class OrderShippedEvent {

    private final String orderId; 

}