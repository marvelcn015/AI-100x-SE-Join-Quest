package com.example;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private BigDecimal totalAmount;
    private BigDecimal originalAmount;
    private BigDecimal discount;
    private BigDecimal thresholdDiscount;
    private BigDecimal double11Discount;
    private List<OrderItem> items;
    
    public Order() {
    }
    
    public Order(BigDecimal totalAmount, BigDecimal originalAmount, BigDecimal discount, List<OrderItem> items) {
        this.totalAmount = totalAmount;
        this.originalAmount = originalAmount;
        this.discount = discount;
        this.items = items;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }
    
    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }
    
    public BigDecimal getDiscount() {
        return discount;
    }
    
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    
    public BigDecimal getThresholdDiscount() {
        return thresholdDiscount;
    }
    
    public void setThresholdDiscount(BigDecimal thresholdDiscount) {
        this.thresholdDiscount = thresholdDiscount;
    }
    
    public BigDecimal getDouble11Discount() {
        return double11Discount;
    }
    
    public void setDouble11Discount(BigDecimal double11Discount) {
        this.double11Discount = double11Discount;
    }
}