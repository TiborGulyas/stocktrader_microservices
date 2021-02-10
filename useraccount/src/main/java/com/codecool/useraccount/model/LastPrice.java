package com.codecool.useraccount.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LastPrice {
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    Stock stock;
    float currentPrice;
    float openPrice;
    float highPrice;
    float lowPrice;
    float previousClosePrice;
    Date timeOfRetrieval;
}
