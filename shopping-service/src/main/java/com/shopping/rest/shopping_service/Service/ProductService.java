package com.shopping.rest.shopping_service.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopping.rest.shopping_service.Configuration.Constant;
import com.shopping.rest.shopping_service.Dao.ProductRepository;
import com.shopping.rest.shopping_service.Model.Product;

@Service // Marks this class as a Spring service bean.
public class ProductService {

    @Autowired
    private ProductRepository productRepository; // Injecting the repository for accessing product data.

    /**
     * Retrieves all products from the database.
     * 
     * @return List<Product> A list of all products stored in the database.
     */
    public List<Product> getProduct() {
        List<Product> product = productRepository.findAll(); // Fetch all products using the repository.
        return product;
    }

    /**
     * Adds a new product to the database. This method also calculates the special price for the product
     * based on the discount applied to it.
     * 
     * @param product The product object to be added.
     * @return Product The product that was added to the database.
     */
    public Product addProduct(Product product) {
        // Calculate the special price after applying the discount
        product.setSpecialPrice(product.getPrice().subtract(product.getDiscount()));

        // Initialize the product's coupon list if it is null
        if (product.getCoupon() == null) {
            product.setCoupon(new ArrayList<>());
        }

        // Save the new product to the repository
        Product newProduct = productRepository.save(product);
        return newProduct; // Return the saved product
    }
}
