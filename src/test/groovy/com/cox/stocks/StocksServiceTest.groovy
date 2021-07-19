package com.cox.stocks

import com.cox.stocks.service.StocksService;
import spock.lang.Specification
import yahoofinance.Stock
import yahoofinance.YahooFinance
import yahoofinance.histquotes.HistoricalQuote
import yahoofinance.quotes.stock.StockQuote;


class StocksServiceTest extends Specification
{

    def service = new StocksService()

    List<HistoricalQuote> historicalQuotes = new ArrayList<>()

    Set<String> watchedStocksSymbolSet = new HashSet<String>()

    def setup()
    {
        HistoricalQuote historicalQuote1 = new HistoricalQuote();
        historicalQuote1.setHigh(100)
        historicalQuote1.setLow(10)
        historicalQuote1.setClose(25)
        HistoricalQuote historicalQuote2 = new HistoricalQuote();
        historicalQuote2.setHigh(200)
        historicalQuote2.setLow(100)
        historicalQuote2.setClose(125)
        HistoricalQuote historicalQuote3 = new HistoricalQuote();
        historicalQuote3.setHigh(300)
        historicalQuote3.setLow(150)
        historicalQuote3.setClose(150)
        historicalQuotes.add(historicalQuote1)
        historicalQuotes.add(historicalQuote2)
        historicalQuotes.add(historicalQuote3)

    }

    def 'retrieveCurrentStockData test'()
    {
        given:
        watchedStocksSymbolSet.add("abc")
        watchedStocksSymbolSet.add("cox")


        when:
        def result = service.retrieveCurrentStockData(watchedStocksSymbolSet)

        then:
        assert result.get(0).getFullName().equals("AmerisourceBergen Corporation")
        assert result.get(1).getSymbol().equals("Error retrieving data for cox")

    }

    def 'calculatePriceAverageForSpan test'()
    {
        when:
        def result = service.calculatePriceAverageForSpan(historicalQuotes)

        then:
        assert result == 100
    }

    def 'calculatePriceLowForSpan test'()
    {
        when:
        def result = service.calculatePriceLowForSpan(historicalQuotes)

        then:
        assert result == 10
    }

    def 'calculatePriceHighForSpan test'()
    {
        when:
        def result = service.calculatePriceHighForSpan(historicalQuotes)

        then:
        assert result == 300
    }

}
