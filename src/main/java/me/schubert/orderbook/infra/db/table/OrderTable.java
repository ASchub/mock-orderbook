package me.schubert.orderbook.infra.db.table;

import me.schubert.orderbook.core.domain.Order;
import me.schubert.orderbook.core.domain.OrderSide;
import me.schubert.orderbook.core.domain.Persisted;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.time.Instant;

public class OrderTable extends TableBase {
    public static final OrderTable ORDER = new OrderTable();
    public final Field<Long> ID = DSL.field(makeFieldName("id"), SQLDataType.BIGINT.identity(true));
    public final Field<String> ACCOUNT_ID = makeField("account_id", String.class);
    public final Field<String> TICKER = makeField("ticker", String.class);
    public final Field<OrderSide> ORDER_SIDE = makeEnumField("order_side", OrderSide.class);
    public final Field<Integer> VOLUME = makeField("volume", Integer.class);
    public final Field<Double> PRICE = makeField("price", Double.class);
    public final Field<String> CURRENCY = makeField("currency", String.class);
    public final Field<Instant> CREATED_AT = makeField("created_at", Instant.class);


    @Override
    public String getTableName() {
        return "order";
    }

    public Persisted<Order> toDomain(Record record) {
        var order = new Order(
                record.get(ACCOUNT_ID),
                record.get(ORDER_SIDE),
                record.get(TICKER),
                record.get(CURRENCY),
                record.get(PRICE),
                record.get(VOLUME)
        );
        return Persisted.of(record.get(ID), order, record.get(CREATED_AT));
    }
}