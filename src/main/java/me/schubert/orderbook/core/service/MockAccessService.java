package me.schubert.orderbook.core.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

//mock
//should check if user exists, enough money, ticker exists... any other access/verification required to make an order
@Service
public class MockAccessService {

    @SneakyThrows
    private static void throwIfEmpty(String str) {
        if (str == null || str.isBlank()) {
            throw new IllegalAccessException();
        }
    }

    @SneakyThrows
    public void verifyUser(String userId) {
        throwIfEmpty(userId);
    }

    public void verifyTicker(String tickerName) {
        throwIfEmpty(tickerName);
    }

    public void verifyCurrency(String currency) {
        throwIfEmpty(currency);
    }
}
