package me.schubert.orderbook.core.service;

import me.schubert.orderbook.core.domain.Order;
import me.schubert.orderbook.core.domain.Persisted;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static me.schubert.orderbook.common.IntegrationTestBase.randomId;
import static me.schubert.orderbook.core.domain.OrderSide.BUY;
import static me.schubert.orderbook.core.domain.OrderSide.SELL;
import static org.assertj.core.api.Assertions.assertThat;

class OrderSummationServiceTest {

    private static final String TICKER_NAME = "ticker";
    private static Persisted<Order> order1;
    private static Persisted<Order> order2;
    private static Persisted<Order> order3;
    private static Persisted<Order> orderWrongTicker;
    private static List<Persisted<Order>> allOrders;

    @BeforeAll
    static void setup() {
        var now = Instant.now();
        order1 = Persisted.of(0L, new Order(
                randomId(),
                BUY,
                TICKER_NAME,
                randomId(),
                50.5,
                2
        ), now);

        order2 = Persisted.of(0L, new Order(
                randomId(),
                SELL,
                TICKER_NAME,
                randomId(),
                10.2,
                3
        ), now);

        order3 = Persisted.of(0L, new Order(
                randomId(),
                BUY,
                TICKER_NAME,
                randomId(),
                20.3,
                2
        ), now);

        orderWrongTicker = Persisted.of(0L, new Order(
                randomId(),
                SELL,
                randomId(),
                randomId(),
                122.5,
                3
        ), now);

        allOrders = List.of(order1, order2, order3, orderWrongTicker);
    }

    @Test
    void shouldCalculateOnlyCorrectTicker() {
        double o1p = order1.get().price();
        double o2p = order2.get().price();
        double o3p = order3.get().price();
        double total = o1p + o1p + o2p + o2p + o2p + o3p + o3p;
        int totalVolume = 7;

        double expectedAvg = total / totalVolume;
        double expectedMin = 10.2;
        double expectedMax = 50.5;

        var summary = OrderSummationService.toOrderSummary(allOrders, TICKER_NAME);

        assertThat(summary.averagePrice()).isEqualTo(expectedAvg);
        assertThat(summary.tickerName()).isEqualTo(TICKER_NAME);
        assertThat(summary.minPrice()).isEqualTo(expectedMin);
        assertThat(summary.maxPrice()).isEqualTo(expectedMax);
        assertThat(summary.totalOrders()).isEqualTo(3);
        assertThat(summary.totalVolume()).isEqualTo(totalVolume);
    }

}