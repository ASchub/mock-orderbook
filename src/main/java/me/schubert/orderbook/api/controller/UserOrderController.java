package me.schubert.orderbook.api.controller;

import lombok.AllArgsConstructor;
import me.schubert.orderbook.api.dto.OrderDetailsDTO;
import me.schubert.orderbook.core.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orderbook/v1/user")
@AllArgsConstructor
//@Secured("ROLE_USER")
public class UserOrderController {
    private final OrderService orderService;

    @PostMapping(value = "/{userId}/order/buy") //normally i would have the userId in the jwt, but its a bit much to setup security/auth filters/jwt etc for a poc
    public void buyOrder(
            @PathVariable String userId,
            @RequestBody OrderDetailsDTO dto) {
        orderService.createBuyOrder(userId, dto.getTicker(), dto.getCurrency(), dto.getPrice(), dto.getVolume());
    }

    @PostMapping(value = "/{userId}/order/sell")
    public void sellOrder(
            @PathVariable String userId,
            @RequestBody OrderDetailsDTO dto) {
        orderService.createSellOrder(userId, dto.getTicker(), dto.getCurrency(), dto.getPrice(), dto.getVolume());
    }

    @GetMapping(value = "/{userId}/order/{orderId}")
    public OrderDetailsDTO.WithId getOrderById(
            @PathVariable String userId,
            @PathVariable long orderId
    ) {
        return OrderMapper.toDto(orderService.getById(userId, orderId));
    }

    @GetMapping(value = "/{userId}/order") //again, should be in JWT to verify, this currently allows anyone to view any known users order history
    public List<OrderDetailsDTO.WithId> getOrders(
            @PathVariable String userId
    ) {
        return OrderMapper.toDtos(orderService.getByUser(userId));
    }
}
