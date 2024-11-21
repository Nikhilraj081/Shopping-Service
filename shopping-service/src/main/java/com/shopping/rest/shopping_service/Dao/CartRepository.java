package com.shopping.rest.shopping_service.Dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shopping.rest.shopping_service.Model.Cart;

@Repository // Marks this interface as a repository for Spring's Data Access Layer.
public interface CartRepository extends MongoRepository<Cart, String> {

    // This interface extends MongoRepository, which provides basic CRUD operations.
    // The Cart entity is stored with a String as the identifier (cartId).
}
