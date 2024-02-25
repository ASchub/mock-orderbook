package me.schubert.orderbook.api.controller;

import lombok.AllArgsConstructor;
import me.schubert.orderbook.api.dto.SummaryDTO;
import me.schubert.orderbook.core.domain.OrderSide;
import me.schubert.orderbook.core.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("orderbook/v1")
@AllArgsConstructor
//@Secured("ROLE_TICKER_VIEWER") some other role than the user apis
public class OrderSummaryController {
    private final OrderService orderService;

    @GetMapping(value = "/summary/ticker/{tickerName}/sell")
    public SummaryDTO getSummaryForSell(
            @PathVariable String tickerName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return OrderMapper.toDto(orderService.getSummaryByTicker(tickerName, OrderSide.SELL, from, to));
    }

    @GetMapping(value = "/summary/ticker/{tickerName}/buy")
    public SummaryDTO getSummaryForBuy(
            @PathVariable String tickerName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return OrderMapper.toDto(orderService.getSummaryByTicker(tickerName, OrderSide.BUY, from, to));
    }
}
