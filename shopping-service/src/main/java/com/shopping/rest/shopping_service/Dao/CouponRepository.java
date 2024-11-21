package com.shopping.rest.shopping_service.Dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shopping.rest.shopping_service.Model.Coupon;

@Repository // Marks this interface as a repository for managing Coupon entities in MongoDB.
public interface CouponRepository extends MongoRepository<Coupon, String> {

    // Method to find a Coupon by its type (e.g., Cart-wise, Product-wise, etc.)
    public Coupon findByType(String type); // Returns a Coupon based on the type of coupon (Cart-wise, etc.)
}
