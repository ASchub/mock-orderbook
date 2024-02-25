package me.schubert.orderbook.it;

import me.schubert.orderbook.api.dto.OrderDetailsDTO;
import me.schubert.orderbook.common.IntegrationTestBase;
import me.schubert.orderbook.common.RestAssuredClient;
import me.schubert.orderbook.common.TestIds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserOrderControllerIntegrationTest extends IntegrationTestBase {

    private static final TestIds IDS = generateRandomIds();
    private static RestAssuredClient client;
    private static OrderDetailsDTO buyOrder;
    private static OrderDetailsDTO sellOrder;
    private static List<OrderDetailsDTO.WithId> persistedOrders;

    // only writing one integration test for the purpose of showing how i generally test. Could argue for unit tets
    @BeforeEach
    public void setup() {
        client = client(IDS);
    }

    @Test
    @Order(10)
    void shouldCreatesOrders() {
        buyOrder = TestFactory.randomOrderDetailsDto(IDS);
        client.requests()
                .createBuyOrder(buyOrder)
                .then()
                .statusCode(200);

        sellOrder = TestFactory.randomOrderDetailsDto(IDS);
        client.requests()
                .createSellOrder(sellOrder)
                .then()
                .statusCode(200);
    }

    @Test
    @Order(20)
    void shouldGetAllOrders() {
        persistedOrders = client.requests()
                .getOrders()
                .then()
                .statusCode(200)
                .extract()
                .body().jsonPath()
                .getList(".", OrderDetailsDTO.WithId.class);

        assertThat(persistedOrders).hasSize(2);

        assertThat(persistedOrders).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").containsExactlyInAnyOrder(
                OrderDetailsDTO.WithId.of(0, buyOrder),
                OrderDetailsDTO.WithId.of(0, sellOrder));
    }

    @Test
    @Order(30)
    void shouldGetSpecificOrder() {
        var o1 = client.requests().getOrderById(persistedOrders.getFirst().getId())
                .then()
                .statusCode(200)
                .extract()
                .as(OrderDetailsDTO.WithId.class);
        var o2 = client.requests().getOrderById(persistedOrders.get(1).getId())
                .then()
                .statusCode(200)
                .extract()
                .as(OrderDetailsDTO.WithId.class);

        assertThat(o1).isEqualTo(persistedOrders.getFirst());
        assertThat(o2).isEqualTo(persistedOrders.get(1));
    }
}
