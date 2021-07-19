package com.cox.stocks.data;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class WatchedStocks
{
    Set<String> watchedStocksSet = new HashSet<String>();

}
