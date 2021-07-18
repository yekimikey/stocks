package com.cox.stocks.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.cox.stocks.model.Stocks;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@RestController
public class StocksController
{
    @RequestMapping(value = {"/retrieve/stock/{stockValue}"}, method = RequestMethod.GET)
    public Stock retrieveStock(@PathVariable("stockValue") String stockValue)
    {
        List<Stocks> stockList = new ArrayList<Stocks>();
        stockList.add(new Stocks(1,"inputval1","inputval2","inputval3"));
        Stock stock;
        try
        {
            stock = YahooFinance.get(stockValue);
        }
        catch(IOException ex)
        {
            return null;//todo?
        }

        return stock;
    }
}