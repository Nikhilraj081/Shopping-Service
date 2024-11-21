package com.shopping.rest.shopping_service.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.rest.shopping_service.Dao.CartRepository;
import com.shopping.rest.shopping_service.Dao.CouponRepository;
import com.shopping.rest.shopping_service.Model.Cart;
import com.shopping.rest.shopping_service.Model.CartItem;
import com.shopping.rest.shopping_service.Model.Coupon;
import com.shopping.rest.shopping_service.Service.CouponService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/coupon") // Mapping the endpoint for coupon-related operations.
public class CouponController {

    // Injecting CouponService to handle business logic related to coupons.
    @Autowired
    private CouponService couponService;

    /**
     * Endpoint to create a new coupon.
     * 
     * @param coupon The coupon data to be saved.
     * @return ResponseEntity<Coupon> The created coupon, wrapped in a ResponseEntity.
     */
    @PostMapping("/add")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        // Call the CouponService to create a new coupon.
        Coupon createdCoupon = couponService.createCoupon(coupon);

        // Return the created coupon with an HTTP status of 201 (Created).
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoupon);
    }

    /**
     * Endpoint to fetch all available coupons.
     * 
     * @return ResponseEntity<List<Coupon>> A list of all available coupons.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        // Fetch all coupons using the CouponService.
        List<Coupon> coupons = couponService.getAllCoupons();

        // Return the list of coupons with an HTTP status of 200 (OK).
        return ResponseEntity.ok(coupons);
    }

    /**
     * Endpoint to get all coupons applicable to a specific cart.
     * 
     * @param cartId The ID of the cart for which applicable coupons are being requested.
     * @return List<Coupon> A list of applicable coupons for the specified cart.
     */
    @GetMapping("/applicable-coupons/cartId/{cartId}")
    public List<Coupon> getApplicableCoupons(@PathVariable String cartId) {
        // Fetch applicable coupons for the given cart ID using the CouponService.
        List<Coupon> coupon = couponService.getApplicableCoupons(cartId);

        // Return the list of applicable coupons.
        return coupon;
    }

    /**
     * Endpoint to fetch a specific coupon by its ID.
     * 
     * @param id The ID of the coupon to fetch.
     * @return ResponseEntity<Coupon> The coupon data, or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable String id) {
        // Retrieve the coupon by its ID using the CouponService.
        Coupon coupon = couponService.getCouponById(id);

        // Return the coupon if found, otherwise return 404 (Not Found).
        if (coupon == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(coupon); // Return the coupon with a 200 (OK) status.
    }

    /**
     * Endpoint to update an existing coupon.
     * 
     * @param id The ID of the coupon to update.
     * @param coupon The updated coupon data.
     * @return ResponseEntity<Coupon> The updated coupon, or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable String id, @RequestBody Coupon coupon) {
        // Call the CouponService to update the coupon data.
        Coupon updatedCoupon = couponService.updateCoupon(id, coupon);

        // If the coupon doesn't exist, return 404 (Not Found).
        if (updatedCoupon == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Return the updated coupon with a 200 (OK) status.
        return ResponseEntity.ok(updatedCoupon);
    }

    /**
     * Endpoint to delete a coupon by its ID.
     * 
     * @param id The ID of the coupon to delete.
     * @return ResponseEntity<Void> No content response if deletion is successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable String id) {
        // Call the CouponService to delete the coupon by its ID.
        couponService.deleteCoupon(id);

        // Return a 204 (No Content) status indicating successful deletion.
        return ResponseEntity.noContent().build();
    }

}
