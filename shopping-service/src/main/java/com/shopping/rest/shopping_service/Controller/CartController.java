package com.shopping.rest.shopping_service.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.rest.shopping_service.Dao.CartRepository;
import com.shopping.rest.shopping_service.Model.Cart;
import com.shopping.rest.shopping_service.Model.Coupon;
import com.shopping.rest.shopping_service.Service.CartService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/cart") // Mapping the endpoint to /cart for all cart-related operations.
public class CartController {

    // Injecting the CartService which contains business logic for cart operations.
    @Autowired
    private CartService cartService;

    // Injecting the CartRepository to interact with the database for cart data.
    @Autowired
    private CartRepository cartRepository;

    /**
     * Endpoint to apply a coupon to the cart.
     * 
     * @param cartId The ID of the cart to apply the coupon on.
     * @param id The ID of the coupon to be applied.
     * @return ResponseEntity<Cart> The updated cart after applying the coupon.
     * @throws Exception If there is an error while calculating the discount.
     */
    @PostMapping("/{cartId}/apply-coupons/{id}")
    public ResponseEntity<Cart> applyCoupons(@PathVariable String cartId, @PathVariable String id) throws Exception {
        // Fetch the current cart from the database using its ID.
        Cart newCart = cartRepository.findById(cartId).get();

        // Call the CartService to calculate the total discount after applying the coupon.
        Cart response = cartService.calculateTotalDiscount(newCart, id);

        // Return the updated cart as the response.
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to add a product to an existing cart.
     * 
     * @param cartId The ID of the cart to add the product to.
     * @param productId The ID of the product to add.
     * @param quant The quantity of the product to add to the cart.
     * @return Cart The updated cart after adding the product.
     */
    @PostMapping("/add/cartId/{cartId}/productId/{productId}/quantity/{quant}")
    public Cart addToCart(@PathVariable String cartId, @PathVariable String productId, @PathVariable int quant) {

        // Call the CartService to add the product to the cart and return the updated cart.
        Cart newCart = cartService.addProductToCart(cartId, productId, quant);

        return newCart; // Return the updated cart with the added product.
    }

    /**
     * Endpoint to get a cart by its ID.
     * 
     * @param id The ID of the cart to retrieve.
     * @return Cart The cart with the specified ID.
     */
    @GetMapping("/{id}")
    public Cart getCart(@PathVariable String id) {
        // Fetch the cart from the CartService by its ID.
        Cart newCart = cartService.getCartById(id);
        return newCart; // Return the cart.
    }

    /**
     * Endpoint to create a new cart.
     * 
     * @return Cart The newly created cart.
     */
    @PostMapping("/add")
    public Cart createCart() {
        // Call the CartService to create a new cart.
        Cart cart = cartService.createCart();
        return cart; // Return the newly created cart.
    }

}
