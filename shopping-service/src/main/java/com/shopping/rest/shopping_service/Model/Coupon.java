package com.shopping.rest.shopping_service.Model;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Coupon class represents a discount coupon that can be applied to a shopping cart or products.
 * It contains details about the type of coupon, discount value, applicable products, and special offers like Buy X, Get Y.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    /**
     * Unique identifier for the coupon.
     * This is generated by the database when the coupon is saved.
     */
    @Id
    private String id;

    /**
     * Name of the coupon.
     * It is a descriptive name used to identify the coupon.
     */
    private String name;

    /**
     * Type of the coupon.
     * It can be one of the following types:
     * - Cart-wise: Applicable to the entire cart.
     * - Product-wise: Applicable to specific products.
     * - BxGy: Buy X items, Get Y items for free (Buy more to get a free product).
     */
    @NotNull
    private String type;

    /**
     * Discount value associated with the coupon.
     * This can either be a fixed discount (e.g., $10) or a percentage (e.g., 10%).
     */
    @NotNull
    private BigDecimal discountValue;

    /**
     * Minimum cart value required for applying the coupon (for Cart-wise coupons).
     * For example, a coupon may require the cart to exceed $50 to apply the discount.
     */
    private BigDecimal threshold;

    /**
     * List of product IDs for Product-wise coupons.
     * This is a list of products to which the coupon can be applied.
     */
    private List<String> applicableProducts;

    /**
     * List of BxGy (Buy X, Get Y) deals.
     * This list contains the details of Buy X, Get Y offers where customers get a free item (Y) when they buy X items.
     */
    private List<BxGyDeal> bxGyDeals;

}
