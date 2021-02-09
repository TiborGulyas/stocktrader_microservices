package com.codecool.stocktrader.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReactCandleContainer {
    private ReactCandle reactCandle1 = new ReactCandle();
    private ReactCandle reactCandle5 = new ReactCandle();
    private ReactCandle reactCandleD = new ReactCandle();



}

