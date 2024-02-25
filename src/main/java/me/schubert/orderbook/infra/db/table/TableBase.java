package me.schubert.orderbook.infra.db.table;

import me.schubert.orderbook.infra.db.schema.OrderbookSchema;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.EnumConverter;
import org.jooq.impl.SQLDataType;

public abstract class TableBase {
    public final Table<Record> TABLE = DSL.table(DSL.name(getSchema().getName(), getTableName()));

    protected <T> Field<T> makeField(String columnName, Class<T> clazz) {
        return DSL.field(makeFieldName(columnName), clazz);
    }

    protected Name makeFieldName(String columnName) {
        return DSL.name(getSchema().getName(), getTableName(), columnName);
    }

    protected <T extends Enum<T>> Field<T> makeEnumField(String columnName, Class<T> clazz) {
        Converter<String, T> converter = new EnumConverter<>(String.class, clazz);
        var dataType = SQLDataType.VARCHAR.asConvertedDataType(converter);
        return DSL.field(makeFieldName(columnName), dataType);
    }

    public Schema getSchema() {
        return OrderbookSchema.ORDERBOOK.SCHEMA;
    }

    public abstract String getTableName();
}
