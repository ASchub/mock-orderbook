package me.schubert.orderbook.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;

@AllArgsConstructor
public class Persisted<T> {
    @Getter
    private final long id;
    @NonNull
    private final T persistedObject;
    @Getter
    @NonNull
    private final Instant createdAt;

    public static <T> Persisted<T> of(Long id, T obj, Instant createdAt) {
        return new Persisted<>(id, obj, createdAt);
    }

    @NonNull
    public T get() {
        return persistedObject;
    }
}
