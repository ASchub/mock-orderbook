package me.schubert.orderbook.infra.db.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.schubert.orderbook.core.domain.Order;
import me.schubert.orderbook.core.domain.OrderSide;
import me.schubert.orderbook.core.domain.Persisted;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record8;
import org.jooq.SelectJoinStep;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static me.schubert.orderbook.infra.db.table.OrderTable.ORDER;

@Repository
@AllArgsConstructor
@Slf4j
public class OrderRepository {
    private final DSLContext jooq;

    public List<Persisted<Order>> getByUser(String userId) {
        return selectCommonRows()
                .where(ORDER.ACCOUNT_ID.eq(userId))
                .fetch(ORDER::toDomain);
    }

    public Persisted<Order> getByUserAndId(String userId, long orderId) {
        return selectCommonRows()
                .where(ORDER.ID.eq(orderId))
                .and(ORDER.ACCOUNT_ID.eq(userId))
                .fetchSingle(ORDER::toDomain);
    }

    private @NotNull SelectJoinStep<Record8<Long, String, OrderSide, String, Double, String, Integer, Instant>> selectCommonRows() {
        return jooq.select(
                        ORDER.ID,
                        ORDER.ACCOUNT_ID,
                        ORDER.ORDER_SIDE,
                        ORDER.TICKER,
                        ORDER.PRICE,
                        ORDER.CURRENCY,
                        ORDER.VOLUME,
                        ORDER.CREATED_AT
                )
                .from(ORDER.TABLE);
    }

    public void insert(Order order) {
        insert(order, Instant.now());
    }

    public void insert(Order order, Instant createdAt) {
        var success = jooq.insertInto(ORDER.TABLE)
                .columns(
                        ORDER.ACCOUNT_ID,
                        ORDER.ORDER_SIDE,
                        ORDER.TICKER,
                        ORDER.PRICE,
                        ORDER.CURRENCY,
                        ORDER.VOLUME,
                        ORDER.CREATED_AT)
                .values(
                        order.userId(),
                        order.orderSide(),
                        order.ticker(),
                        order.price(),
                        order.currency(),
                        order.volume(),
                        createdAt)
                .execute() == 1;
        if (!success) {
            log.error("Failed to persist order: %s".formatted(order));
        }
    }

    //from = inclusive
    //to = exclusive
    public List<Persisted<Order>> getByTickerAndOrderSideAndDate(String tickerName, OrderSide orderSide, LocalDate from, LocalDate to) {
        Instant start = from.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = to.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return selectCommonRows()
                .where(ORDER.TICKER.eq(tickerName))
                .and(ORDER.ORDER_SIDE.eq(orderSide))
                .and(ORDER.CREATED_AT.between(start, end))
                .fetch(ORDER::toDomain);
    }
}
