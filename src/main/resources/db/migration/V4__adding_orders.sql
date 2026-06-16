CREATE TABLE orders
(
        id      BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    status      VARCHAR(20) DEFAULT 'PENDING' NOT NULL,
    customer_id BIGINT                          NOT NULL,
    total_price DECIMAL(10, 2)        DEFAULT 0         NOT NULL,
    created_at  DATETIME    DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    CONSTRAINT orders__users_pk
        FOREIGN KEY (customer_id) REFERENCES users (id)
);

CREATE TABLE order_items
(
    order_id    BIGINT                   NOT NULL,
    product_id  BIGINT                   NOT NULL,
    quantity    INT            DEFAULT 1 NOT NULL,
    total_price DECIMAL(10, 2) DEFAULT 0 NOT NULL,
    unit_price  DECIMAL(10, 2) DEFAULT 0 NOT NULL,
    CONSTRAINT order_items_pk
        PRIMARY KEY (order_id, product_id),
    CONSTRAINT ORDER_FK
        FOREIGN KEY (order_id) REFERENCES `orders` (id),
    CONSTRAINT PRODUCT_FK
        FOREIGN KEY (product_id) REFERENCES products (id)
);

