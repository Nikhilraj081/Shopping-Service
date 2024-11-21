package com.shopping.rest.shopping_service.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shopping.rest.shopping_service.Configuration.Constant;
import com.shopping.rest.shopping_service.Dao.CartRepository;
import com.shopping.rest.shopping_service.Dao.CouponRepository;
import com.shopping.rest.shopping_service.Dao.ProductRepository;
import com.shopping.rest.shopping_service.Exception.ApiException;
import com.shopping.rest.shopping_service.Model.Cart;
import com.shopping.rest.shopping_service.Model.CartItem;
import com.shopping.rest.shopping_service.Model.Coupon;
import com.shopping.rest.shopping_service.Model.Product;

@Service // Service annotation to indicate this class handles business logic.
public class CartService {

    // Injecting dependencies
    @Autowired
    private CouponService couponService; // Service for coupon-related operations.
    
    @Autowired
    private CouponRepository couponRepository; // Repository to interact with coupon data.
    
    @Autowired
    private CartRepository cartRepository; // Repository to interact with cart data.
    
    @Autowired
    private ProductRepository productRepository; // Repository to interact with product data.

    /**
     * This method calculates the total discount based on the applied coupon.
     * 
     * @param cart The current cart object.
     * @param id The ID of the coupon to apply.
     * @return Cart The updated cart with discount and total price.
     * @throws Exception If the coupon is already applied or invalid.
     */
    public Cart calculateTotalDiscount(Cart cart, String id) throws Exception {
        BigDecimal totalDiscount = BigDecimal.ZERO;

        // Check if the coupon is already applied.
        if (cart.getAppliedCouponId() != null && cart.getAppliedCouponId().equals(id)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Coupon already applied");
        }

        // Check if another coupon is already applied.
        if (cart.getAppliedCouponId() != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "You can apply maximum one coupon at a time");
        }

        // Retrieve the coupon object from the repository.
        Coupon coupon = couponRepository.findById(id).get();
        System.out.println(coupon.getType());

        // Apply cart-wise discount if the coupon type is CART_WISE.
        if (coupon.getType().equals(Constant.CART_WISE)) {
            totalDiscount = totalDiscount.add(couponService.applyCartWiseCoupon(cart.getTotalPrice(), coupon));
            cart.setDiscount(totalDiscount.add(cart.getDiscount())); // Update discount in cart.
            cart.setTotalPrice(cart.getTotalPrice().subtract(totalDiscount)); // Subtract discount from total price.
            cart.setAppliedCouponId(id); // Set applied coupon ID.
            System.out.println(totalDiscount);
        }
        // Apply BxGy (Buy X Get Y) discount if the coupon type is BX_GY.
        else if (coupon.getType().equals(Constant.BX_GY)) {
            totalDiscount = totalDiscount.add(couponService.applyBxGyCoupon(cart.getTotalPrice(), cart, coupon));
            cart.setDiscount(totalDiscount.add(cart.getDiscount())); // Update discount in cart.
            cart.setTotalPrice(cart.getTotalPrice().subtract(totalDiscount)); // Subtract discount from total price.
            cart.setAppliedCouponId(id); // Set applied coupon ID.
            System.out.println(totalDiscount);
        }

        System.out.println(cart);
        return cartRepository.save(cart); // Save the updated cart.
    }

    /**
     * Adds a product to the cart and calculates the updated cart price.
     * 
     * @param cartId The ID of the cart.
     * @param productId The ID of the product to add.
     * @param quantity The quantity of the product to add.
     * @return Cart The updated cart.
     */
    public Cart addProductToCart(String cartId, String productId, int quantity) {

        // Retrieve the cart from the repository.
        Cart newCart = cartRepository.findById(cartId).get();
        
        // Retrieve the product to add to the cart.
        Product newProduct = productRepository.findById(productId).get();
        
        // Create a new cart item.
        CartItem item = new CartItem();

        List<CartItem> cartItems = newCart.getCartItem();

        // Set unique ID and product details for the cart item.
        item.setCartItemId(UUID.randomUUID().toString());
        item.setProduct(newProduct);
        item.setQuantity(quantity);
        item.setPrice(newProduct.getPrice());
        item.setDiscount(newProduct.getDiscount());
        item.setSpecialPrice(newProduct.getSpecialPrice());

        // Add the item to the cart.
        cartItems.add(item);

        newCart.setCartItem(cartItems); // Update the cart with new cart items.

        return cartRepository.save(setCartPrice(newCart)); // Save and return the updated cart.
    }

    /**
     * Retrieves the cart by its ID.
     * 
     * @param id The ID of the cart to retrieve.
     * @return Cart The cart associated with the provided ID.
     */
    public Cart getCartById(String id) {
        // Retrieve the cart by its ID.
        Cart cart = cartRepository.findById(id).get();
        return cart;
    }

    /**
     * This method sets the total price and discount for the cart.
     * 
     * @param cart The cart object to calculate price and discount for.
     * @return Cart The updated cart with calculated total price and discount.
     */
    public Cart setCartPrice(Cart cart) {
        BigDecimal totalPrice = Constant.DEFAULT_DOUBLE_VALUE;
        BigDecimal totalDiscount = Constant.DEFAULT_DOUBLE_VALUE;
        int totalQuantity = 0;

        List<CartItem> cartItem = cart.getCartItem();

        // If no items in cart, set default price and discount.
        if (cartItem == null) {
            cart.setDiscount(Constant.DEFAULT_DOUBLE_VALUE);
            cart.setTotalPrice(Constant.DEFAULT_DOUBLE_VALUE);
            return cartRepository.save(cart);
        }

        // Iterate through each cart item and calculate total price, discount, and quantity.
        for (CartItem item : cartItem) {
            totalPrice = totalPrice.add(item.getSpecialPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            totalDiscount = totalDiscount.add(item.getDiscount().multiply(BigDecimal.valueOf(item.getQuantity())));
            totalQuantity = totalQuantity + item.getQuantity();
        }

        // If a coupon is applied, apply the coupon discount to total price.
        if (cart.getAppliedCouponId() != null) {
            Coupon coupon = couponRepository.findById(cart.getAppliedCouponId()).get();
            totalPrice = totalPrice.subtract(coupon.getDiscountValue()); // Apply the discount value.
            totalDiscount = totalDiscount.add(coupon.getDiscountValue()); // Add the coupon discount.
        }

        // Set final values for total price, discount, and quantity in the cart.
        cart.setTotalPrice(totalPrice);
        cart.setDiscount(totalDiscount);
        cart.setTotalQuantity(totalQuantity);

        return cartRepository.save(cart); // Save and return the updated cart.
    }

    /**
     * Creates a new cart with no items and returns the created cart.
     * 
     * @return Cart The newly created cart.
     */
    public Cart createCart() {
        Cart cart = new Cart();
        List<CartItem> cartItem = new ArrayList<>();
        cart.setCartItem(cartItem);

        // Save and return the created cart.
        return cartRepository.save(cart);
    }
}
