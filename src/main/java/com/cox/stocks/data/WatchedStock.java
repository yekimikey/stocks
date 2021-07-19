package com.cox.stocks.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@AllArgsConstructor
public class WatchedStock
{
    String symbol;
    String fullName;
    double currentPrice;
    double dayLow;
    double dayHigh;
}
