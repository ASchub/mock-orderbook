package me.schubert.orderbook.core.service;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import me.schubert.orderbook.core.domain.Order;
import me.schubert.orderbook.core.domain.OrderSummary;
import me.schubert.orderbook.core.domain.Persisted;

import java.util.List;

@UtilityClass
public class OrderSummationService {
    private static final String STANDARD_CROSS_RATE = "CURRENCY"; //requirements did not specify what a "summary" is, only that its by ticker, so to simplify ill convert everything to some made up currency

    public static OrderSummary toOrderSummary(List<Persisted<Order>> orders, String tickerName) {
        double totalPrice = 0.0;
        double minPrice = Double.MAX_VALUE;
        double maxPrice = Double.MIN_VALUE;
        int totalVolume = 0;
        int totalOrders = 0;

        for (Persisted<Order> o : orders) {
            if (!o.get().ticker().equals(tickerName)) {
                continue;
            }
            totalOrders++;
            var volume = o.get().volume();
            var price = new Price(o.get().currency(), o.get().price()).convertTo(STANDARD_CROSS_RATE).price(); // mocking a conversion of price
            minPrice = Math.min(minPrice, price);
            maxPrice = Math.max(maxPrice, price);

            for (int i = 0; i < volume; i++) {
                totalPrice += price;
                totalVolume++;
            }
        }

        double averagePrice = totalPrice / totalVolume;
        return new OrderSummary(tickerName, averagePrice, minPrice, maxPrice, totalVolume, totalOrders);
    }


    private record Price(
            @NonNull
            String currency,
            double price
    ) {
        public Price convertTo(String currency) {
            //fake conversion rate, everything is 1:1
            return new Price(currency, price);
        }
    }
}
