@order_pricing @double11
Feature: E-commerce Order Pricing - Double 11 Promotion
  As a shopper during Double 11
  I want the system to apply Double 11 discounts on bulk purchases
  So that I can enjoy 20% discount for every 10 items of the same product

  Background:
    Given the Double 11 promotion is active

  Scenario: Single product with exactly 10 items
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 10       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 1000           | 200      | 800         |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 10       |

  Scenario: Single product with 12 items - partial discount
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 12       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 1200           | 200      | 1000        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 12       |

  Scenario: Single product with 27 items - multiple discount groups
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 襪子          | 27       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 2700           | 400      | 2300        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 27       |

  Scenario: Multiple different products with 1 each - no discount
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 商品A         | 1        | 100       |
      | 商品B         | 1        | 100       |
      | 商品C         | 1        | 100       |
      | 商品D         | 1        | 100       |
      | 商品E         | 1        | 100       |
      | 商品F         | 1        | 100       |
      | 商品G         | 1        | 100       |
      | 商品H         | 1        | 100       |
      | 商品I         | 1        | 100       |
      | 商品J         | 1        | 100       |
    Then the order summary should be:
      | totalAmount |
      | 1000        |
    And the customer should receive:
      | productName | quantity |
      | 商品A         | 1        |
      | 商品B         | 1        |
      | 商品C         | 1        |
      | 商品D         | 1        |
      | 商品E         | 1        |
      | 商品F         | 1        |
      | 商品G         | 1        |
      | 商品H         | 1        |
      | 商品I         | 1        |
      | 商品J         | 1        |

  Scenario: Multiple products with different quantities
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | T-shirt     | 15       | 200       |
      | 褲子          | 8        | 300       |
      | 襪子          | 20       | 50        |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 6400           | 600      | 5800        |
    And the customer should receive:
      | productName | quantity |
      | T-shirt     | 15       |
      | 褲子          | 8        |
      | 襪子          | 20       |

  Scenario: Double 11 with threshold discount promotion
    Given the threshold discount promotion is configured:
      | threshold | discount |
      | 3000      | 300      |
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | T-shirt     | 25       | 200       |
    Then the order summary should be:
      | originalAmount | thresholdDiscount | double11Discount | totalAmount |
      | 5000           | 300               | 800              | 3900        |
    And the customer should receive:
      | productName | quantity |
      | T-shirt     | 25       |

  Scenario: Double 11 with buy-one-get-one promotion
    Given the buy one get one promotion for cosmetics is active
    When a customer places an order with:
      | productName | category  | quantity | unitPrice |
      | 口紅          | cosmetics | 10       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 1000           | 200      | 800         |
    And the customer should receive:
      | productName | quantity |
      | 口紅          | 20       |

  Scenario: Double 11 discount calculation for expensive items
    When a customer places an order with:
      | productName | quantity | unitPrice |
      | 手機          | 11       | 10000     |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 110000         | 20000    | 90000       |
    And the customer should receive:
      | productName | quantity |
      | 手機          | 11       |