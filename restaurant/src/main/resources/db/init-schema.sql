
-- invoice table

CREATE TABLE invoices (
    invoice_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id INT UNSIGNED NOT NULL,
    invoice_amount DECIMAL(10, 2) NOT NULL,
    invoice_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- restaurant_order table

CREATE TABLE restaurant_order (
    restaurant_order_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT UNSIGNED NOT NULL,
    order_id INT UNSIGNED NOT NULL,
    billing_amount DECIMAL(10, 2) NOT NULL,
    restaurant_status VARCHAR(20) NOT NULL, -- we can have enum here -- TODO
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- restaurant_order_items table

CREATE TABLE restaurant_order_items (
    restaurant_order_item_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    restaurant_order_id INT UNSIGNED NOT NULL,
    item_id INT UNSIGNED NOT NULL,
    quantity INT NOT NULL,
    notes TEXT,
    FOREIGN KEY (restaurant_order_id) REFERENCES restaurant_order(restaurant_order_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
