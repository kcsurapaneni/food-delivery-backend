
-- restaurants table

CREATE TABLE restaurants (
     restaurant_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     address VARCHAR(255) NOT NULL,
     phone VARCHAR(15),
     cuisine_type VARCHAR(50)
);


-- items table

CREATE TABLE items (
   item_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
   restaurant_id INT UNSIGNED,
   name VARCHAR(255) NOT NULL,
   description TEXT,
   price DECIMAL(10, 2) NOT NULL,
   category VARCHAR(50),
   FOREIGN KEY (restaurant_id) REFERENCES restaurants(restaurant_id)
);


-- customers table

-- either we can have this table in a separate database which is looking into the user service, in that case we will loose the relationship
-- or we can only copy the required fields like the following and update this only if there is a change into this fields
-- [This also can happen either by API call or async like pub-sub model]

CREATE TABLE customers (
   customer_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   email VARCHAR(255) UNIQUE NOT NULL,
   phone VARCHAR(15) NOT NULL
);


-- orders table

CREATE TABLE orders (
    order_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    customer_id INT UNSIGNED,
    delivery_address VARCHAR(255) NOT NULL,
    billing_amount DECIMAL(10, 2),
    order_status VARCHAR(20) NOT NULL, -- we can have enum here -- TODO
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- order_items table

CREATE TABLE order_items (
    order_item_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id INT UNSIGNED,
    item_id INT UNSIGNED,
    quantity INT NOT NULL,
    notes TEXT,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (item_id) REFERENCES items(item_id)
);

-- billing table

CREATE TABLE billing (
    billing_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id INT UNSIGNED,
    total_amount DECIMAL(10, 2) NOT NULL,
    tax DECIMAL(6, 2) NOT NULL,
    discount DECIMAL(6, 2),
    delivery_charge DECIMAL(6, 2),
    payment_method VARCHAR(50) NOT NULL,
    payment_status VARCHAR(20) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

-- Sample records into the `restaurants` table

INSERT INTO restaurants
    (restaurant_id, name, address, phone, cuisine_type)
VALUES
    (1, 'Spice House', '456 Elm St, Townsville', '+1-987-654-3210', 'Indian'),
    (2, 'Tasty Bites', '123 Main St, Cityville', '+1-123-456-7890', 'Italian'),
    (3, 'Sushi Express', '789 Oak St, Villagetown', '+1-555-123-4567', 'Japanese');

-- Sample records into the `items` table
INSERT INTO items
    (restaurant_id, name, description, price, category)
VALUES
    (1, 'Chicken Tikka Masala', 'Tender chicken in rich tomato sauce', 14.99, 'Curry'),
    (1, 'Vegetable Biryani', 'Fragrant rice dish with mixed vegetables', 11.99, 'Rice'),
    (2, 'Margherita Pizza', 'Classic tomato and mozzarella cheese pizza', 12.99, 'Pizza'),
    (2, 'Penne Alfredo', 'Creamy Alfredo sauce with penne pasta', 9.99, 'Pasta'),
    (3, 'Sushi Platter', 'Assorted sushi rolls and sashimi', 18.99, 'Sushi');

-- Sample records into the `customers` table
INSERT INTO customers
    (name, email, phone)
VALUES
    ('Krishna Chaitanya', 'kc.surapaneni@gmail.com', '+1-777-888-9999'),
    ('Hello world', 'hello.world@yahoo.com', '+1-111-222-3333'),
    ('Karthik', 'karthik@outlook.com', '+1-444-555-6666');
