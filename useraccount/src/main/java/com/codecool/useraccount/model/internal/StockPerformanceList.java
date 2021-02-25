package com.codecool.useraccount.model.internal;

import com.codecool.useraccount.model.StockPerformance;
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
public class StockPerformanceList {
    List<StockPerformance> stockPerformancesList = new ArrayList<>();
}
