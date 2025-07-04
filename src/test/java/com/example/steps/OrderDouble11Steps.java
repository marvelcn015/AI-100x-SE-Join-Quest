package com.example.steps;

import com.example.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDouble11Steps {
    
    private final SharedTestContext sharedTestContext;
    
    public OrderDouble11Steps(SharedTestContext sharedTestContext) {
        this.sharedTestContext = sharedTestContext;
    }
    
    @Given("the Double 11 promotion is active")
    public void theDouble11PromotionIsActive() {
        OrderService orderService = new OrderService();
        orderService.setDouble11PromotionActive(true);
        sharedTestContext.setOrderService(orderService);
    }
}