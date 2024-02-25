package me.schubert.orderbook.infra.db.schema;

import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.impl.DSL;

public class OrderbookSchema {
    private static final Name SCHEMA_NAME = DSL.name("orderbook");
    public static final OrderbookSchema ORDERBOOK = new OrderbookSchema();
    public final Schema SCHEMA = DSL.schema(SCHEMA_NAME);
}
