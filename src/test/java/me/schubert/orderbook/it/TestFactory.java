package me.schubert.orderbook.it;

import lombok.experimental.UtilityClass;
import me.schubert.orderbook.api.dto.OrderDetailsDTO;
import me.schubert.orderbook.common.TestIds;
import me.schubert.orderbook.core.domain.Order;
import me.schubert.orderbook.core.domain.OrderSide;
import me.schubert.orderbook.core.domain.Persisted;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@UtilityClass
public class TestFactory {
    private static final Random rn = new Random();
    public static OrderDetailsDTO randomOrderDetailsDto(TestIds ids) {
        return new OrderDetailsDTO(
                ids.ticker(),
                ids.currency(),
                randomDouble(),
                1
        );
    }

    private static double randomDouble() {
        return BigDecimal.valueOf(rn.nextDouble(100.00)).setScale(4, RoundingMode.FLOOR).doubleValue();
    }

}
