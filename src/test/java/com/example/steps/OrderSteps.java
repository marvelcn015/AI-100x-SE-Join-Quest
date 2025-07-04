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
    
    private OrderService orderService;
    private Order resultOrder;
    private BigDecimal thresholdAmount;
    private BigDecimal discountAmount;
    private boolean buyOneGetOneCosmeticsActive;
    
    @Given("no promotions are applied")
    public void noPromotionsAreApplied() {
        orderService = new OrderService();
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
        
        resultOrder = orderService.checkout(items.toArray(new OrderItem[0]));
    }
    
    @Then("the order summary should be:")
    public void theOrderSummaryShouldBe(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> expectedRow = rows.get(0);
        
        if (expectedRow.containsKey("totalAmount")) {
            BigDecimal expectedTotal = new BigDecimal(expectedRow.get("totalAmount"));
            Assertions.assertThat(resultOrder.getTotalAmount()).isEqualTo(expectedTotal);
        }
        
        if (expectedRow.containsKey("originalAmount")) {
            BigDecimal expectedOriginal = new BigDecimal(expectedRow.get("originalAmount"));
            Assertions.assertThat(resultOrder.getOriginalAmount()).isEqualTo(expectedOriginal);
        }
        
        if (expectedRow.containsKey("discount")) {
            BigDecimal expectedDiscount = new BigDecimal(expectedRow.get("discount"));
            Assertions.assertThat(resultOrder.getDiscount()).isEqualTo(expectedDiscount);
        }
    }
    
    @Then("the customer should receive:")
    public void theCustomerShouldReceive(DataTable dataTable) {
        List<Map<String, String>> expectedItems = dataTable.asMaps(String.class, String.class);
        
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
        
        thresholdAmount = new BigDecimal(row.get("threshold"));
        discountAmount = new BigDecimal(row.get("discount"));
        
        if (orderService == null) {
            orderService = new OrderService();
        }
        orderService.setThresholdDiscount(thresholdAmount, discountAmount);
    }
    
    @Given("the buy one get one promotion for cosmetics is active")
    public void theBuyOneGetOnePromotionForCosmeticsIsActive() {
        buyOneGetOneCosmeticsActive = true;
        if (orderService == null) {
            orderService = new OrderService();
        }
        orderService.setBuyOneGetOneCosmeticsPromotion(true);
    }
}