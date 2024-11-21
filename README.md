# Shopping-Service

## Overview

This application provides backend functionality for an e-commerce platform. It supports managing products, shopping carts, and applying coupons for discounts. The application is built using **Spring Boot** and **MongoDB** for persistent data storage.

## Features

### 1. Implemented Cases

The following features have been implemented:

#### 1.1 Product Management

- **Add Products**: Allows products to be added to the system, including their price, discount, and special price (after applying the discount).
- **Retrieve Products**: Fetches all products from the database.

**How it works**:
- Products are stored with details like name, category, price, discount, and any associated coupons. A special price is calculated when a discount is applied.

#### 1.2 Cart Management

- **Create Cart**: A new shopping cart can be created for a user. The cart is initialized with an empty list of items.
- **Add Products to Cart**: Products can be added to the shopping cart with a specified quantity. Each item stores details such as price, discount, and quantity.
- **Retrieve Cart**: The cart details (including products, total price, and applied discounts) can be retrieved by the cart ID.
- **Set Cart Price**: The total price and discount are recalculated whenever an item is added to the cart. The cart price includes any applicable discounts.

**How it works**:
- Each cart is represented by a unique `cartId` and contains a list of `CartItem`s. The price and discount of the cart are recalculated dynamically based on the items in the cart and any coupons applied.

#### 1.3 Coupon System

- **Create Coupons**: Coupons can be created with various types: `Cart-wise`, `Product-wise`, and `Buy X, Get Y (BxGy)` deals.
  - `Cart-wise`: Provides a discount to the entire cart if the total value meets a threshold.
  - `Product-wise`: Applies a discount to specific products.
  - `BxGy (Buy X, Get Y)`: Offers a free product based on the quantity of a specific product purchased.
  
- **Apply Coupons**: Coupons can be applied to the cart. Depending on the type of coupon, it will provide either a discount on the cart total or a discount on specific items.

**How it works**:
- Coupons are applied based on the cart's total price or specific products. The coupon details include the type, discount value, and applicable products or threshold. 

#### 1.4 Coupon Eligibility Check

- **Check Applicable Coupons for Cart**: Based on the items in the cart and the type of coupon, the system checks which coupons can be applied.
- **Apply Cart-wise Coupon**: If the total cart value exceeds the threshold, a cart-wise coupon is applied.
- **Apply BxGy Coupon**: A BxGy coupon is applied by verifying if the cart meets the eligibility criteria (e.g., buying a specific number of products).

**How it works**:
- The cart is checked against the coupons’ conditions. If a coupon is eligible for the cart, it will be applied to calculate the discount.

### 2. Unimplemented Cases

While the majority of key functionality is implemented, there are some cases that are not yet handled:

#### 2.1 User Authentication and Authorization

- **Reason**: User authentication and authorization (e.g., login, user roles, session management) is not yet implemented. This feature is crucial for securing the cart, user-specific discounts, and order history.
  
#### 2.2 Product Inventory Management

- **Reason**: There is no inventory management system for tracking stock levels of products. This could be added to ensure that products in the cart are available for purchase.

#### 2.3 Payment Integration

- **Reason**: Payment gateways like Stripe, PayPal, etc., are not integrated. Implementing payment methods would require handling secure transactions, which is beyond the current scope of the application.

#### 2.4 Order Management

- **Reason**: There is no feature for managing orders once the cart has been processed (e.g., order creation, order status tracking). Adding order management would be a next step after completing the checkout flow.

### 3. Limitations

The following limitations are present in the current implementation:

#### 3.1 Lack of Real-Time Product Availability Check

- **Limitation**: Currently, there is no real-time stock or inventory management system. The cart allows adding unlimited quantities of any product, regardless of the actual availability.

#### 3.2 Static Coupon Discount Calculation

- **Limitation**: The coupon discount is applied statically (e.g., percentage or fixed amount). There is no support for more advanced dynamic coupon features like "Buy One, Get One Free" or tiered discounts based on the total cart value.

#### 3.3 No User-Specific Data Handling

- **Limitation**: The current cart does not store user-specific data, such as previous orders, preferences, or saved carts. Adding this functionality would require implementing user authentication and a more complex data model.

#### 3.4 Limited Coupon Types

- **Limitation**: Only three types of coupons are implemented: Cart-wise, Product-wise, and BxGy. More advanced coupon types (e.g., seasonal or time-limited coupons) are not supported.

#### 3.5 No Expiry Date for Coupons

- **Limitation**: Coupons in the current implementation do not have an expiration date. Adding a feature for coupon expiry would help in controlling time-sensitive promotions.

### 4. Assumptions

To ensure the correct working of the system, the following assumptions were made:

- **Database Consistency**: It is assumed that the MongoDB database is running and accessible. The application relies on the availability of MongoDB for storing and retrieving products, carts, and coupons.
  
- **Product Price Integrity**: The system assumes that the price of products is always updated correctly, and discounts are consistently applied to the price when creating a product or applying a coupon.

- **Valid Coupon Application**: It is assumed that coupons are only applied when they meet the necessary conditions (e.g., valid threshold, product applicability, etc.).

- **Cart State**: Cart data is maintained in real-time, meaning that any changes to the cart (adding/removing products or applying coupons) will reflect immediately in the cart's total price.

- **No User-Specific Handling**: Currently, the cart is not linked to any specific user, meaning that anyone can create a cart, add products, and apply coupons without user authentication.

- **Valid Product and Coupon IDs**: It is assumed that the product and coupon IDs provided when adding products to the cart or applying coupons are valid and exist in the database.

---
## Installation and Run

Clone this repository in your local system

```bash
  git clone https://github.com/Nikhilraj081/Shopping-Service.git
```
Note: The application uses MongoDB Atlas, a cloud-based database solution, eliminating the need for local database setup.

Then open terminal and go to project directory and run below command to start project:

```bash
  mvn clean spring-boot:run
```
Once server is started paste below swagger-ui link to your browser  
```bash
  http://localhost:8082/shopping-service/swagger-ui/index.html
```
It will open swagger ui interface where you can test the all endpoints.
