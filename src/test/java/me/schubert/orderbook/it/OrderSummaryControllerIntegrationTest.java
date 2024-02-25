package me.schubert.orderbook.it;

import me.schubert.orderbook.api.dto.OrderDetailsDTO;
import me.schubert.orderbook.api.dto.SummaryDTO;
import me.schubert.orderbook.common.IntegrationTestBase;
import me.schubert.orderbook.common.RestAssuredClient;
import me.schubert.orderbook.common.TestIds;
import me.schubert.orderbook.core.domain.OrderSide;
import me.schubert.orderbook.infra.db.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderSummaryControllerIntegrationTest extends IntegrationTestBase {

    @Autowired
    OrderRepository orderRepository;

    private static final TestIds IDS = generateRandomIds();

    private static RestAssuredClient client;
    private static OrderDetailsDTO dto1;
    private static OrderDetailsDTO dto2;
    private static OrderDetailsDTO dto3;
    private static OrderDetailsDTO dto4;
    private static final Instant now = Instant.now();
    private static final LocalDate date = LocalDate.ofInstant(now, ZoneId.systemDefault());
    private static final LocalDate future = date.plusDays(1);
    private static final LocalDate farPast = date.minusDays(10);

    @BeforeEach
    public void setup() {
        client = client(IDS);

    }

    @Order(10)
    @Test
    void populateDb() {
        dto1 = TestFactory.randomOrderDetailsDto(IDS);
        dto2 = TestFactory.randomOrderDetailsDto(IDS);
        dto3 = TestFactory.randomOrderDetailsDto(IDS);
        dto4 = TestFactory.randomOrderDetailsDto(IDS);

        orderRepository.insert(
                new me.schubert.orderbook.core.domain.Order(IDS.userId(), OrderSide.BUY, dto1.getTicker(), dto1.getCurrency(), dto1.getPrice(), dto1.getVolume()),
                now
        );
        orderRepository.insert(
                new me.schubert.orderbook.core.domain.Order(IDS.userId(), OrderSide.BUY, dto2.getTicker(), dto2.getCurrency(), dto2.getPrice(), dto2.getVolume()),
                now.minus(Duration.ofDays(2))
        );
        orderRepository.insert(
                new me.schubert.orderbook.core.domain.Order(IDS.userId(), OrderSide.SELL, dto3.getTicker(), dto3.getCurrency(), dto3.getPrice(), dto3.getVolume()),
                now.minus(Duration.ofDays(4))
        );
        orderRepository.insert(
                new me.schubert.orderbook.core.domain.Order(IDS.userId(), OrderSide.SELL, dto4.getTicker(), dto4.getCurrency(), dto4.getPrice(), dto4.getVolume()),
                now.minus(Duration.ofDays(6))
        );
    }

    @Test
    @Order(20)
    void shouldGetBuySummaryForToday() {
        var buySummary = client.requests().getSummaryForBuy(IDS.ticker(), date, future)
                .then()
                .statusCode(200)
                .extract()
                .as(SummaryDTO.class);

        assertThat(buySummary.averagePrice()).isEqualTo(dto1.getPrice());
        assertThat(buySummary.maxPrice()).isEqualTo(dto1.getPrice());
        assertThat(buySummary.minPrice()).isEqualTo(dto1.getPrice());
    }

    @Test
    @Order(20)
    void shouldGetAllBuySummary() {
        var buySummary = client.requests().getSummaryForBuy(IDS.ticker(), farPast, future)
                .then()
                .statusCode(200)
                .extract()
                .as(SummaryDTO.class);

        assertThat(buySummary.averagePrice()).isEqualTo((dto1.getPrice() + dto2.getPrice()) / 2);
        assertThat(buySummary.maxPrice()).isEqualTo(Math.max(dto1.getPrice(), dto2.getPrice()));
        assertThat(buySummary.minPrice()).isEqualTo(Math.min(dto1.getPrice(), dto2.getPrice()));
    }

    @Test
    @Order(20)
    void shouldGetSellSummaryFor4daysAgo() {
        var sellSummary = client.requests().getSummaryForSell(IDS.ticker(), date.minusDays(4), future)
                .then()
                .statusCode(200)
                .extract()
                .as(SummaryDTO.class);

        assertThat(sellSummary.averagePrice()).isEqualTo(dto3.getPrice());
        assertThat(sellSummary.maxPrice()).isEqualTo(dto3.getPrice());
        assertThat(sellSummary.minPrice()).isEqualTo(dto3.getPrice());
    }

    @Test
    @Order(20)
    void shouldGetAllSellSummary() {
        var sellSummary = client.requests().getSummaryForSell(IDS.ticker(), farPast, future)
                .then()
                .statusCode(200)
                .extract()
                .as(SummaryDTO.class);

        assertThat(sellSummary.averagePrice()).isEqualTo((dto3.getPrice() + dto4.getPrice()) / 2);
        assertThat(sellSummary.maxPrice()).isEqualTo(Math.max(dto3.getPrice(), dto4.getPrice()));
        assertThat(sellSummary.minPrice()).isEqualTo(Math.min(dto3.getPrice(), dto4.getPrice()));
    }

}
