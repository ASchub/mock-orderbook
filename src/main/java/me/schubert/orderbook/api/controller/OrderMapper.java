package me.schubert.orderbook.api.controller;

import lombok.experimental.UtilityClass;
import me.schubert.orderbook.api.dto.OrderDetailsDTO;
import me.schubert.orderbook.api.dto.SummaryDTO;
import me.schubert.orderbook.core.domain.Order;
import me.schubert.orderbook.core.domain.OrderSummary;
import me.schubert.orderbook.core.domain.Persisted;

import java.util.List;

@UtilityClass
public class OrderMapper {
    public static OrderDetailsDTO.WithId toDto(Persisted<Order> order) {
        return OrderDetailsDTO.WithId.of(order.getId(), toDto(order.get()));
    }

    public static OrderDetailsDTO toDto(Order order) {
        return new OrderDetailsDTO(
                order.ticker(),
                order.currency(),
                order.price(),
                order.volume()
        );
    }

    public static SummaryDTO toDto(OrderSummary summary) {
        return new SummaryDTO(
                summary.tickerName(),
                summary.averagePrice(),
                summary.minPrice(),
                summary.maxPrice(),
                summary.totalVolume(),
                summary.totalOrders()
        );
    }

    public static List<OrderDetailsDTO.WithId> toDtos(List<Persisted<Order>> orders) {
        return orders.stream()
                .map(OrderMapper::toDto)
                .toList();
    }
}
