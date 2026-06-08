CREATE TABLE carts
(
    id         BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL,
    created_at DATE DEFAULT (CURDATE()) NOT NULL,
    CONSTRAINT pk_carts PRIMARY KEY (id)
);

CREATE TABLE cart_item
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    cart_id    BINARY(16) NOT NULL,
    product_id BIGINT     NOT NULL,
    quantity   INT DEFAULT 1 NOT NULL,
    CONSTRAINT pk_cart_item PRIMARY KEY (id),
    CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES products (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_cart_item_carts FOREIGN KEY (cart_id) REFERENCES carts (id)
        ON DELETE CASCADE
);
