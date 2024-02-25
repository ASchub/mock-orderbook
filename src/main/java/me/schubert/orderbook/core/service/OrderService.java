package me.schubert.orderbook.core.service;

import lombok.AllArgsConstructor;
import me.schubert.orderbook.core.domain.Order;
import me.schubert.orderbook.core.domain.OrderSide;
import me.schubert.orderbook.core.domain.OrderSummary;
import me.schubert.orderbook.core.domain.Persisted;
import me.schubert.orderbook.infra.db.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MockAccessService mockAccessService;

    public void createBuyOrder(String userId, String ticker, String currency, double price, int volume) {
        var order = new Order(userId, OrderSide.BUY, ticker, currency, price, volume);
        verifyAndCreateOrder(order);
    }

    public void createSellOrder(String userId, String ticker, String currency, double price, int volume) {
        var order = new Order(userId, OrderSide.SELL, ticker, currency, price, volume);
        verifyAndCreateOrder(order);
    }

    private void verifyAndCreateOrder(Order order) {
        //dummy calls, should verify request
        mockAccessService.verifyUser(order.userId());
        mockAccessService.verifyTicker(order.ticker());
        mockAccessService.verifyCurrency(order.currency());

        orderRepository.insert(order);
        //probably other handling needed here, such as publishing an event
    }

    public Persisted<Order> getById(String userId, long orderId) {
        mockAccessService.verifyUser(userId);

        return orderRepository.getByUserAndId(userId, orderId);
    }

    public List<Persisted<Order>> getByUser(String userId) {
        mockAccessService.verifyUser(userId);

        return orderRepository.getByUser(userId);
    }

    public OrderSummary getSummaryByTicker(String tickerName, OrderSide orderSide, LocalDate from, LocalDate to) {
        mockAccessService.verifyTicker(tickerName);

        var orders = orderRepository.getByTickerAndOrderSideAndDate(tickerName, orderSide, from, to);
        return OrderSummationService.toOrderSummary(orders, tickerName);
    }
}
