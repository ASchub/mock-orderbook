package me.schubert.orderbook.common;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.schubert.orderbook.api.dto.OrderDetailsDTO;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

import java.time.LocalDate;

@RequiredArgsConstructor
public class RestAssuredClient {
    @NonNull
    private final String userId;
    @NonNull
    private final ServletWebServerApplicationContext servletWebServerApplicationContext;

    private RequestSpecification given() {
        return RestAssured.given()
                .log().ifValidationFails()
                .contentType(ContentType.JSON)
                .port(servletWebServerApplicationContext.getWebServer().getPort());
    }

    public Apis requests() {
        return new Apis();
    }

    public class Apis {
        public Response createBuyOrder(OrderDetailsDTO dto) {
            return given()
                    .body(dto)
                    .post("orderbook/v1/user/{userId}/order/buy", userId);
        }

        public Response createSellOrder(OrderDetailsDTO dto) {
            return given()
                    .body(dto)
                    .post("orderbook/v1/user/{userId}/order/sell", userId);
        }

        public Response getOrderById(Long orderId) {
            return given()
                    .get("orderbook/v1/user/{userId}/order/{orderId}", userId, orderId);
        }

        public Response getOrders() {
            return given()
                    .get("orderbook/v1/user/{userId}/order", userId);
        }

        public Response getSummaryForSell(String tickerName, LocalDate from, LocalDate to) {
            return given()
                    .queryParam("from", from.toString())
                    .queryParam("to", to.toString())
                    .get("orderbook/v1/summary/ticker/{tickerName}/sell", tickerName);

        }

        public Response getSummaryForBuy(String tickerName, LocalDate from, LocalDate to) {
            return given()
                    .queryParam("from", from.toString())
                    .queryParam("to", to.toString())
                    .get("orderbook/v1/summary/ticker/{tickerName}/buy", tickerName);
        }
    }
}
