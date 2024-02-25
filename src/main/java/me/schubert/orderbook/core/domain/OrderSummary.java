package me.schubert.orderbook.core.domain;

import lombok.NonNull;

public record OrderSummary(
        @NonNull
        String tickerName,
        double averagePrice,
        double minPrice,
        double maxPrice,
        int totalVolume,
        int totalOrders
) {
}
