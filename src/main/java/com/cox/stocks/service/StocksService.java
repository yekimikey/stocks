package com.cox.stocks.service;

import com.cox.stocks.data.WatchedStock;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class StocksService {

    public List<WatchedStock> retrieveCurrentStockData(Set<String> watchedStocksSymbolSet)
    {
        List<WatchedStock> watchedStocksData = new ArrayList<>();
        for(String symbol: watchedStocksSymbolSet)
        {
            Stock stock;
            try
            {
                stock = YahooFinance.get(symbol);
                watchedStocksData.add(new WatchedStock(stock.getSymbol(),
                        stock.getName(),
                        stock.getQuote().getPrice().doubleValue(),
                        stock.getQuote().getDayLow().doubleValue(),
                        stock.getQuote().getDayHigh().doubleValue()));
            }
            catch(Exception ex)
            {
                watchedStocksData.add(new WatchedStock("Error retrieving data for " + symbol, "", -1, -1, -1));
            }
        }

        return watchedStocksData;
    }
}