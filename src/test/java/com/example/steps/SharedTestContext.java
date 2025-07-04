package com.example.steps;

import com.example.Order;
import com.example.OrderService;

public class SharedTestContext {
    private OrderService orderService;
    private Order resultOrder;
    
    public OrderService getOrderService() {
        return orderService;
    }
    
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
    
    public Order getResultOrder() {
        return resultOrder;
    }
    
    public void setResultOrder(Order resultOrder) {
        this.resultOrder = resultOrder;
    }
}