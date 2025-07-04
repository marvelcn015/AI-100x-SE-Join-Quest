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

public class OrderSteps {
    
    private final SharedTestContext sharedTestContext;
    
    public OrderSteps(SharedTestContext sharedTestContext) {
        this.sharedTestContext = sharedTestContext;
    }
    
    @Given("no promotions are applied")
    public void noPromotionsAreApplied() {
        sharedTestContext.setOrderService(new OrderService());
    }
    
    @When("a customer places an order with:")
    public void aCustomerPlacesAnOrderWith(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        List<OrderItem> items = new ArrayList<>();
        
        for (Map<String, String> row : rows) {
            String productName = row.get("productName");
            int quantity = Integer.parseInt(row.get("quantity"));
            BigDecimal unitPrice = new BigDecimal(row.get("unitPrice"));
            String category = row.get("category");
            
            Product product = new Product(productName, unitPrice, category);
            OrderItem item = new OrderItem(product, quantity);
            items.add(item);
        }
        
        Order order = sharedTestContext.getOrderService().checkout(items.toArray(new OrderItem[0]));
        sharedTestContext.setResultOrder(order);
    }
    
    @Then("the order summary should be:")
    public void theOrderSummaryShouldBe(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> expectedRow = rows.get(0);
        
        Order resultOrder = sharedTestContext.getResultOrder();
        
        if (expectedRow.containsKey("totalAmount")) {
            BigDecimal expectedTotal = new BigDecimal(expectedRow.get("totalAmount"));
            Assertions.assertThat(resultOrder.getTotalAmount()).isEqualByComparingTo(expectedTotal);
        }
        
        if (expectedRow.containsKey("originalAmount")) {
            BigDecimal expectedOriginal = new BigDecimal(expectedRow.get("originalAmount"));
            Assertions.assertThat(resultOrder.getOriginalAmount()).isEqualByComparingTo(expectedOriginal);
        }
        
        if (expectedRow.containsKey("discount")) {
            BigDecimal expectedDiscount = new BigDecimal(expectedRow.get("discount"));
            Assertions.assertThat(resultOrder.getDiscount()).isEqualByComparingTo(expectedDiscount);
        }
        
        if (expectedRow.containsKey("thresholdDiscount")) {
            BigDecimal expectedThresholdDiscount = new BigDecimal(expectedRow.get("thresholdDiscount"));
            Assertions.assertThat(resultOrder.getThresholdDiscount()).isEqualByComparingTo(expectedThresholdDiscount);
        }
        
        if (expectedRow.containsKey("double11Discount")) {
            BigDecimal expectedDouble11Discount = new BigDecimal(expectedRow.get("double11Discount"));
            Assertions.assertThat(resultOrder.getDouble11Discount()).isEqualByComparingTo(expectedDouble11Discount);
        }
    }
    
    @Then("the customer should receive:")
    public void theCustomerShouldReceive(DataTable dataTable) {
        List<Map<String, String>> expectedItems = dataTable.asMaps(String.class, String.class);
        Order resultOrder = sharedTestContext.getResultOrder();
        
        Assertions.assertThat(resultOrder.getItems()).hasSize(expectedItems.size());
        
        for (int i = 0; i < expectedItems.size(); i++) {
            Map<String, String> expectedItem = expectedItems.get(i);
            OrderItem actualItem = resultOrder.getItems().get(i);
            
            Assertions.assertThat(actualItem.getProduct().getName()).isEqualTo(expectedItem.get("productName"));
            Assertions.assertThat(actualItem.getQuantity()).isEqualTo(Integer.parseInt(expectedItem.get("quantity")));
        }
    }
    
    @Given("the threshold discount promotion is configured:")
    public void theThresholdDiscountPromotionIsConfigured(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = rows.get(0);
        
        BigDecimal thresholdAmount = new BigDecimal(row.get("threshold"));
        BigDecimal discountAmount = new BigDecimal(row.get("discount"));
        
        if (sharedTestContext.getOrderService() == null) {
            sharedTestContext.setOrderService(new OrderService());
        }
        sharedTestContext.getOrderService().setThresholdDiscount(thresholdAmount, discountAmount);
    }
    
    @Given("the buy one get one promotion for cosmetics is active")
    public void theBuyOneGetOnePromotionForCosmeticsIsActive() {
        if (sharedTestContext.getOrderService() == null) {
            sharedTestContext.setOrderService(new OrderService());
        }
        sharedTestContext.getOrderService().setBuyOneGetOneCosmeticsPromotion(true);
    }
}