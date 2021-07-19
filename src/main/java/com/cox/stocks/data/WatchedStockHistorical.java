package com.cox.stocks.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@AllArgsConstructor
public class WatchedStockHistorical
{
    String symbol;
    String fullName;
    double currentPrice;
    double yearAverage;
    double monthAverage;
    double yearLow;
    double yearHigh;
    double monthLow;
    double monthHigh;
}
