package com.shopping.rest.shopping_service.Dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shopping.rest.shopping_service.Model.Product;

@Repository // Marks this interface as a repository for managing Product entities in MongoDB.
public interface ProductRepository extends MongoRepository<Product, String> {

    // Inherits all CRUD operations from MongoRepository for the Product entity.
    // Additional query methods can be defined here if needed (e.g., findByName, findByCategory, etc.)
}
