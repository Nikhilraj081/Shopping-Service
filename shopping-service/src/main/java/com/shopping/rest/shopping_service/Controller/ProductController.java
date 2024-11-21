package com.shopping.rest.shopping_service.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.shopping.rest.shopping_service.Model.Product;
import com.shopping.rest.shopping_service.Service.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/product") // Mapping the endpoint for product-related operations.
public class ProductController {

    // Injecting ProductService to handle business logic related to products.
    @Autowired
    private ProductService productService;

    /**
     * Endpoint to add a new product to the catalog.
     * 
     * @param product The product data to be added.
     * @return Product The newly added product.
     */
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        // Calling ProductService to add the product to the database.
        Product newProduct = productService.addProduct(product);

        // Returning the newly added product.
        return newProduct;
    }

    /**
     * Endpoint to fetch all products from the catalog.
     * 
     * @return List<Product> A list of all available products.
     */
    @GetMapping("/all")
    public List<Product> getProduct() {
        // Calling ProductService to fetch all products.
        List<Product> product = productService.getProduct();

        // Returning the list of products.
        return product;
    }

}
