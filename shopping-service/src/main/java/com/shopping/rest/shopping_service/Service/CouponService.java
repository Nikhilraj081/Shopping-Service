package com.shopping.rest.shopping_service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shopping.rest.shopping_service.Configuration.Constant;
import com.shopping.rest.shopping_service.Dao.CartRepository;
import com.shopping.rest.shopping_service.Dao.CouponRepository;
import com.shopping.rest.shopping_service.Dao.ProductRepository;
import com.shopping.rest.shopping_service.Exception.ApiException;
import com.shopping.rest.shopping_service.Model.BxGyDeal;
import com.shopping.rest.shopping_service.Model.Cart;
import com.shopping.rest.shopping_service.Model.CartItem;
import com.shopping.rest.shopping_service.Model.Coupon;
import com.shopping.rest.shopping_service.Model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service // Marks this class as a service for business logic.
public class CouponService {

    // Injecting the necessary dependencies using Spring's @Autowired annotation
    @Autowired
    private CouponRepository couponRepository; // Repository for interacting with coupon data.

    @Autowired
    private ProductRepository productRepository; // Repository for interacting with product data.

    @Autowired
    private CartRepository cartRepository; // Repository for interacting with cart data.

    /**
     * Creates a new coupon and associates it with products based on the coupon type (applicable products or BxGy deals).
     * 
     * @param coupon The coupon to be created.
     * @return Coupon The saved coupon after being created and applied to products.
     */
    public Coupon createCoupon(Coupon coupon) {
        // Check if the coupon contains BxGy deals and apply them to relevant products
        if (!coupon.getBxGyDeals().isEmpty()) {
            List<String> productList = coupon.getBxGyDeals().iterator().next().getProductId();

            for (String productId : productList) {
                Product newProduct = productRepository.findById(productId).get();
                newProduct.getCoupon().add(coupon.getId()); // Add coupon ID to product's list of coupons.

                // Save updated product
                productRepository.save(newProduct);

                // Save and return the coupon
                return couponRepository.save(coupon);
            }
        }

        // If the coupon applies to specific products, update those products accordingly
        if (!coupon.getApplicableProducts().isEmpty()) {
            List<String> productList = coupon.getApplicableProducts();
            for (String productId : productList) {
                Product newProduct = productRepository.findById(productId).get();
                newProduct.getCoupon().add(coupon.getId()); // Add coupon to the product.

                // Apply the coupon discount to the product
                newProduct.setDiscount(coupon.getDiscountValue());
                newProduct.setSpecialPrice(newProduct.getPrice().subtract(coupon.getDiscountValue())); // Update special price.

                // Save the product with updated values
                productRepository.save(newProduct);

                // Save and return the coupon
                return couponRepository.save(coupon);
            }
        }

        // If no applicable products or BxGy deals, just save the coupon without any product updates
        return couponRepository.save(coupon);
    }

    /**
     * Retrieves a coupon by its ID.
     * 
     * @param id The ID of the coupon to retrieve.
     * @return Coupon The coupon associated with the provided ID.
     */
    public Coupon getCouponById(String id) {
        return couponRepository.findById(id).orElse(null); // Return null if coupon not found.
    }

    /**
     * Retrieves all coupons available in the repository.
     * 
     * @return List<Coupon> A list of all coupons.
     */
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll(); // Fetch all coupons.
    }

    /**
     * Updates an existing coupon based on the provided coupon details.
     * 
     * @param id The ID of the coupon to update.
     * @param couponDetails The new details to update the coupon with.
     * @return Coupon The updated coupon.
     */
    public Coupon updateCoupon(String id, Coupon couponDetails) {
        Coupon coupon = getCouponById(id); // Fetch the coupon by ID.
        if (coupon != null) {
            // Update the coupon's fields with the new details
            coupon.setDiscountValue(couponDetails.getDiscountValue());
            coupon.setThreshold(couponDetails.getThreshold());
            coupon.setApplicableProducts(couponDetails.getApplicableProducts());
            coupon.setBxGyDeals(couponDetails.getBxGyDeals());

            // Save and return the updated coupon
            return couponRepository.save(coupon);
        }
        return null; // Return null if the coupon wasn't found.
    }

    /**
     * Deletes a coupon based on its ID.
     * 
     * @param id The ID of the coupon to delete.
     */
    public void deleteCoupon(String id) {
        couponRepository.deleteById(id); // Delete coupon by ID.
    }

    /**
     * Applies a cart-wise coupon to the total cart amount.
     * 
     * @param totalAmount The total price of the cart.
     * @param coupon The coupon to apply.
     * @return BigDecimal The discount value from the coupon if applicable, else zero.
     */
    public BigDecimal applyCartWiseCoupon(BigDecimal totalAmount, Coupon coupon) {
        // Check if the total amount is greater than or equal to the coupon's threshold.
        if (totalAmount.compareTo(coupon.getThreshold()) >= 0) {
            return coupon.getDiscountValue(); // Return the discount value.
        }
        return BigDecimal.ZERO; // No discount if the threshold is not met.
    }

    /**
     * Applies a Buy X Get Y coupon to the cart and calculates the total discount.
     * 
     * @param totalAmount The total amount of the cart.
     * @param cart The cart containing the products.
     * @param coupon The Buy X Get Y coupon to apply.
     * @return BigDecimal The total discount from the BxGy coupon.
     */
    public BigDecimal applyBxGyCoupon(BigDecimal totalAmount, Cart cart, Coupon coupon) {
        BigDecimal totalDiscount = BigDecimal.ZERO;

        // Check if the total amount is above the coupon's threshold
        if (coupon.getThreshold() != null && totalAmount.compareTo(coupon.getThreshold()) >= 0) {

            // Loop through each BxGy deal and calculate applicable discounts.
            for (BxGyDeal deal : coupon.getBxGyDeals()) {
                for (CartItem cartItem : cart.getCartItem()) {
                    // Check if the product is part of the BxGy deal
                    if (deal.getProductId().contains(cartItem.getProduct().getProductId())) {
                        // Calculate the eligible quantity and the discount for the product
                        int eligibleQuantity = cartItem.getQuantity() / deal.getBuyQuantity();

                        if (cart.getTotalQuantity() <= deal.getBuyQuantity()) {
                            throw new ApiException(HttpStatus.BAD_REQUEST, "Please add one more product to avail this coupon");
                        }

                        if (eligibleQuantity < 1) {
                            throw new ApiException(HttpStatus.BAD_REQUEST, "Coupon not eligible");
                        }

                        BigDecimal productPrice = cartItem.getProduct().getSpecialPrice();

                        // Calculate discount: eligibleQuantity * freeQuantity * productPrice
                        BigDecimal discountForItem = BigDecimal.valueOf(eligibleQuantity)
                                .multiply(BigDecimal.valueOf(deal.getFreeQuantity()))
                                .multiply(productPrice);

                        // Add the item discount to the total discount
                        totalDiscount = totalDiscount.add(discountForItem);
                    } else {
                        throw new ApiException(HttpStatus.BAD_REQUEST, "Coupon not eligible");
                    }
                }
            }
        }
        return totalDiscount; // Return the calculated discount.
    }

    /**
     * Retrieves a list of all applicable coupons for the provided cart.
     * 
     * @param cartId The ID of the cart.
     * @return List<Coupon> A list of applicable coupons for the cart.
     */
    public List<Coupon> getApplicableCoupons(String cartId) {

        List<String> couponsId = new ArrayList<>();
        List<Coupon> coupons = new ArrayList<>();
        Cart cart = cartRepository.findById(cartId).get(); // Get the cart by ID.

        List<CartItem> cartItems = cartRepository.findById(cartId).orElseThrow().getCartItem();
        // For each cart item, check for coupons applicable to the product.
        for (CartItem cartItem : cartItems) {
            productRepository.findById(cartItem.getProduct().getProductId())
                    .ifPresent(product -> {
                        if (product.getCoupon() != null) {
                            couponsId.addAll(product.getCoupon()); // Add applicable coupon IDs to list.
                        }
                    });
        }

        // Retrieve and add the coupons by their IDs.
        for (String cId : couponsId) {
            coupons.add(couponRepository.findById(cId).get());
        }

        // Check if a cart-wise coupon is applicable.
        Coupon cartWiseCoupon = couponRepository.findByType(Constant.CART_WISE);

        if (cartWiseCoupon != null && cart.getTotalPrice().compareTo(cartWiseCoupon.getThreshold()) >= 0) {
            coupons.add(cartWiseCoupon); // Add the cart-wise coupon if applicable.
        }

        return coupons; // Return the list of applicable coupons.
    }

    /**
     * Retrieves the list of product IDs in the cart.
     * 
     * @param cart The cart object.
     * @return List<String> A list of product IDs in the cart.
     */
    public List<String> getCartProductIds(Cart cart) {
        return cart.getCartItem().stream()
                .map(cartItem -> cartItem.getProduct().getProductId()) // Map each cart item to its product ID.
                .collect(Collectors.toList());
    }
}
