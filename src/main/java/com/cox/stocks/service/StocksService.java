package com.cox.stocks.service;

import com.cox.stocks.data.WatchedStock;
import com.cox.stocks.data.WatchedStockHistorical;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.util.ArrayList;
import java.util.Calendar;
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

    public List<WatchedStockHistorical> retrieveHistoricalStockData(Set<String> watchedStocksSymbolSet)
    {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, -1); // from 5 years ago

        List<WatchedStockHistorical> watchedStocksData = new ArrayList<>();
        for(String symbol: watchedStocksSymbolSet)
        {
            List<HistoricalQuote> stockHistorical;
            Stock stock;
            try
            {
                stock = YahooFinance.get(symbol);
                stockHistorical = YahooFinance.get(symbol, from, to, Interval.DAILY).getHistory();

                watchedStocksData.add(
                        new WatchedStockHistorical(
                                stock.getSymbol(),
                                stock.getName(),
                                stock.getQuote().getPrice().doubleValue(),
                                calculatePriceAverageForSpan(stockHistorical),
                                calculatePriceAverageForSpan(stockHistorical.subList(stockHistorical.size()-30, stockHistorical.size())),
                                stock.getQuote().getYearLow().doubleValue(),
                                stock.getQuote().getYearHigh().doubleValue(),
                                calculatePriceLowForSpan(stockHistorical.subList(stockHistorical.size()-30, stockHistorical.size())),
                                calculatePriceHighForSpan(stockHistorical.subList(stockHistorical.size()-30, stockHistorical.size()))
                        )
                );
            }
            catch(Exception ex)
            {
                watchedStocksData.add(new WatchedStockHistorical("Error retrieving data for " + symbol, "", -1, -1, -1, -1, -1, -1, -1));
            }
        }

        return watchedStocksData;
    }

    private double calculatePriceAverageForSpan(List<HistoricalQuote> stockHistorical)
    {
        double addedClosePrices = 0;
        for(HistoricalQuote quote : stockHistorical)
        {
            addedClosePrices = addedClosePrices + quote.getClose().doubleValue();
        }

        return addedClosePrices/stockHistorical.size();
    }

    private double calculatePriceLowForSpan(List<HistoricalQuote> stockHistorical)
    {
        double priceLow = stockHistorical.get(0).getLow().doubleValue();
        for(HistoricalQuote quote : stockHistorical)
        {
            if(quote.getLow().doubleValue() < priceLow)
            {
                priceLow = quote.getLow().doubleValue();
            }
        }
        return priceLow;
    }

    private double calculatePriceHighForSpan(List<HistoricalQuote> stockHistorical)
    {
        double priceHigh = stockHistorical.get(0).getHigh().doubleValue();
        for(HistoricalQuote quote : stockHistorical)
        {
            if(quote.getHigh().doubleValue() > priceHigh)
            {
                priceHigh = quote.getHigh().doubleValue();
            }
        }
        return priceHigh;
    }
}