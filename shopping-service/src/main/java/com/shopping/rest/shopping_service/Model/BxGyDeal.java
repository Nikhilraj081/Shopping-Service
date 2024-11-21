package com.shopping.rest.shopping_service.Model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BxGyDeal represents a Buy X, Get Y deal associated with a coupon.
 * This deal defines which products are part of the promotion and the conditions 
 * required to get the "free" products based on a purchase quantity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BxGyDeal {

    /**
     * Unique identifier for the Buy X, Get Y deal.
     * This ID is used to reference the specific deal within a coupon.
     */
    private String Id;

    /**
     * List of product IDs that are part of the Buy X, Get Y deal.
     * These are the products eligible for the promotion.
     */
    private List<String> productId;

    /**
     * The quantity of the product that needs to be purchased to avail the deal.
     * For example, in a "Buy 2, Get 1 Free" deal, this value would be 2.
     */
    private int buyQuantity;

    /**
     * The quantity of the product that is given for free when the buy quantity is met.
     * For example, in a "Buy 2, Get 1 Free" deal, this value would be 1.
     */
    private int freeQuantity;
}
