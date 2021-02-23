package com.codecool.security.model.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplaceOffer {
    private long offerId;
    private String symbol;
    private String offerType;
    private int quantity;
    private float price;
    private String userName;
}
