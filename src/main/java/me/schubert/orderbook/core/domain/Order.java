package me.schubert.orderbook.core.domain;

import lombok.NonNull;

public record Order(
        @NonNull String userId,
        @NonNull OrderSide orderSide,
        @NonNull String ticker,
        @NonNull String currency,
        double price,
        int volume) {
}
