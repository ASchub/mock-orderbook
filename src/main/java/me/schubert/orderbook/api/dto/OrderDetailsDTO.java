package me.schubert.orderbook.api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class OrderDetailsDTO {
        private String ticker;
        private String currency;
        private double price;
        private int volume;

        @Getter
        @EqualsAndHashCode(callSuper = true)
        @NoArgsConstructor
        public static class WithId extends OrderDetailsDTO {
                private long id;

                public WithId(@NonNull String ticker, @NonNull String currency, double price, int volume, long id) {
                        super(ticker, currency, price, volume);
                        this.id = id;
                }

                public static WithId of(long id, OrderDetailsDTO dto) {
                        return new WithId(dto.getTicker(), dto.getCurrency(), dto.getPrice(), dto.getVolume(), id);
                }
        }
}
