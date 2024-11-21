package com.shopping.rest.shopping_service.Model;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cart class represents the shopping cart of a customer.
 * It holds all the items added to the cart, total price, discounts,
 * the applied coupon, and the total quantity of products in the cart.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    /**
     * Unique identifier for the cart.
     * This ID is generated when the cart is created.
     */
    @Id
    private String cartId;

    /**
     * List of CartItems in the cart.
     * Each CartItem corresponds to a product, with its quantity, price, discount, etc.
     */
    private List<CartItem> cartItem;

    /**
     * The total price of all the items in the cart, before any discount is applied.
     * This represents the cumulative price of all products in the cart.
     */
    private BigDecimal totalPrice;

    /**
     * The total discount applied to the cart.
     * This includes any discounts or special offers applied to the products in the cart.
     */
    private BigDecimal discount;

    /**
     * The ID of the coupon that has been applied to the cart.
     * This is used to track which coupon has been used for a particular cart.
     */
    private String appliedCouponId;

    /**
     * The total quantity of products in the cart.
     * This field calculates the sum of quantities for all CartItems in the cart.
     */
    private int totalQuantity;

}
