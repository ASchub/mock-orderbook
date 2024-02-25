package me.schubert.orderbook.api.dto;

public record SummaryDTO(
        String tickerName,
        double averagePrice,
        double minPrice,
        double maxPrice,
        int totalVolume,
        int totalOrders
) {
}
