package com.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderService {
    
    private BigDecimal thresholdAmount;
    private BigDecimal discountAmount;
    private boolean buyOneGetOneCosmeticsActive;
    
    public void setThresholdDiscount(BigDecimal thresholdAmount, BigDecimal discountAmount) {
        this.thresholdAmount = thresholdAmount;
        this.discountAmount = discountAmount;
    }
    
    public void setBuyOneGetOneCosmeticsPromotion(boolean active) {
        this.buyOneGetOneCosmeticsActive = active;
    }
    
    public Order checkout(OrderItem[] items) {
        // Calculate price based on original quantities
        BigDecimal originalAmount = BigDecimal.ZERO;
        for (OrderItem item : items) {
            BigDecimal itemTotal = item.getProduct().getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            originalAmount = originalAmount.add(itemTotal);
        }
        
        // Create delivery items with promotion applied
        List<OrderItem> deliveryItems = new ArrayList<>();
        for (OrderItem item : items) {
            int deliveryQuantity = item.getQuantity();
            
            // Apply buy-one-get-one promotion for cosmetics
            if (buyOneGetOneCosmeticsActive && "cosmetics".equals(item.getProduct().getCategory())) {
                deliveryQuantity = item.getQuantity() + 1;
            }
            
            OrderItem deliveryItem = new OrderItem(item.getProduct(), deliveryQuantity);
            deliveryItems.add(deliveryItem);
        }
        
        BigDecimal discount = BigDecimal.ZERO;
        if (thresholdAmount != null && discountAmount != null) {
            if (originalAmount.compareTo(thresholdAmount) >= 0) {
                discount = discountAmount;
            }
        }
        
        BigDecimal totalAmount = originalAmount.subtract(discount);
        
        Order order = new Order();
        order.setTotalAmount(totalAmount);
        order.setOriginalAmount(originalAmount);
        order.setDiscount(discount);
        order.setItems(deliveryItems);
        
        return order;
    }
}