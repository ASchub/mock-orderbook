CREATE TABLE orderbook.order
(
    id                  BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    account_id          VARCHAR(36) NOT NULL, -- abstracting some form of account/wallet/user that made the order, likely handled in different MS, allows uuid. preffered: BINARY(16) NOT NULL DEFAULT (uuid_to_bin(uuid(), 1))
    ticker              VARCHAR(36) NOT NULL,
    order_side          ENUM('BUY', 'SELL') NOT NULL,
    volume              BIGINT NOT NULL,
    price               DECIMAL(13, 4) NOT NULL, -- higher precision/values? GAAP
    currency            VARCHAR(36) NOT NULL,
    created_at          TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
);

CREATE INDEX index_order_ticker ON orderbook.order (ticker);
CREATE INDEX index_order_order_side ON orderbook.order (order_side);
CREATE INDEX index_order_created_at ON orderbook.order (created_at);
