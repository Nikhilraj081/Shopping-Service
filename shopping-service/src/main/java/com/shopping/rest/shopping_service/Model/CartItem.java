package com.shopping.rest.shopping_service.Model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.shopping.rest.shopping_service.Model.Cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CartItem class represents a single item in a shopping cart.
 * Each CartItem is linked to a product, and it stores information like 
 * quantity, price, discount, and special price.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    /**
     * Unique identifier for the cart item.
     * This ID is generated when the cart item is added to the cart.
     */
    @Id
    private String cartItemId;

    /**
     * The product associated with this cart item.
     * Each CartItem corresponds to a specific product in the cart.
     */
    private Product product;

    /**
     * The quantity of the product in this cart item.
     * This defines how many units of the product are being purchased.
     */
    private int quantity;

    /**
     * The price of the product before any discount.
     * This is the regular price of the product at the time the item was added to the cart.
     */
    private BigDecimal price;

    /**
     * The discount applied to this product.
     * It is the discount amount or percentage applied to the product.
     */
    private BigDecimal discount;

    /**
     * The special price of the product after applying the discount.
     * This price is the actual amount the customer will pay after the discount is applied.
     */
    private BigDecimal specialPrice;
}
