package com.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderService {
    
    private BigDecimal thresholdAmount;
    private BigDecimal discountAmount;
    private boolean buyOneGetOneCosmeticsActive;
    private boolean double11PromotionActive;
    
    public void setThresholdDiscount(BigDecimal thresholdAmount, BigDecimal discountAmount) {
        this.thresholdAmount = thresholdAmount;
        this.discountAmount = discountAmount;
    }
    
    public void setBuyOneGetOneCosmeticsPromotion(boolean active) {
        this.buyOneGetOneCosmeticsActive = active;
    }
    
    public void setDouble11PromotionActive(boolean active) {
        this.double11PromotionActive = active;
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
                if (double11PromotionActive) {
                    // During Double 11, buy-one-get-one means double the quantity
                    deliveryQuantity = item.getQuantity() * 2;
                } else {
                    // Regular buy-one-get-one means +1 free item per product type
                    deliveryQuantity = item.getQuantity() + 1;
                }
            }
            
            OrderItem deliveryItem = new OrderItem(item.getProduct(), deliveryQuantity);
            deliveryItems.add(deliveryItem);
        }
        
        BigDecimal double11Discount = BigDecimal.ZERO;
        BigDecimal thresholdDiscountAmount = BigDecimal.ZERO;
        
        // Apply Double 11 promotion - 20% discount for every 10 items of the same product
        if (double11PromotionActive) {
            for (OrderItem item : items) {
                int quantity = item.getQuantity();
                int discountGroups = quantity / 10; // Number of complete groups of 10
                if (discountGroups > 0) {
                    BigDecimal itemUnitPrice = item.getProduct().getUnitPrice();
                    BigDecimal discountPerGroup = itemUnitPrice.multiply(BigDecimal.valueOf(10)).multiply(BigDecimal.valueOf(0.20));
                    BigDecimal itemDiscount = discountPerGroup.multiply(BigDecimal.valueOf(discountGroups));
                    double11Discount = double11Discount.add(itemDiscount);
                }
            }
        }
        
        if (thresholdAmount != null && discountAmount != null) {
            if (originalAmount.compareTo(thresholdAmount) >= 0) {
                thresholdDiscountAmount = discountAmount;
            }
        }
        
        BigDecimal totalDiscount = double11Discount.add(thresholdDiscountAmount);
        
        BigDecimal totalAmount = originalAmount.subtract(totalDiscount);
        
        Order order = new Order();
        order.setTotalAmount(totalAmount);
        order.setOriginalAmount(originalAmount);
        order.setDiscount(totalDiscount);
        order.setThresholdDiscount(thresholdDiscountAmount);
        order.setDouble11Discount(double11Discount);
        order.setItems(deliveryItems);
        
        return order;
    }
}