package me.schubert.orderbook.core.domain;

public record Pair<T, U>(
        T first,
        U second
) {
}
