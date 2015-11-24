package com.ostendotech.stockwatcher.server;

import nu.xom.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.ostendotech.stockwatcher.shared.StockPrice;
import com.ostendotech.stockwatcher.shared.StockRequest;

// Yahoo Query Language Quote Fetcher class
public class YqlFinanceQuoteFetcher {
	// https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20yahoo.finance.quote%20WHERE%20symbol%3D%22MSFT%22&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys
	// https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20yahoo.finance.quote%20WHERE%20symbol%3D%22msft%2Cyhoo%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=
	// https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20yahoo.finance.quote%20WHERE%20symbol%3D%22msft%2C%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=
	private String yqlUrl = "https://query.yahooapis.com/v1/public/yql?";
	private String yqlQuery = "q=SELECT%20*%20FROM%20yahoo.finance.quote%20WHERE%20";
	private String yqlDB = "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
	private static final String YQL_SPACE = "%20";
	private static final String YQL_DQUOTES = "%22";
	private static final String YQL_EQUAL = "%3D";
	private static final String YQL_COMMA = "%2C";
	private static final String YQL_AND = "&";
	
	private Rest rest;
	private String query;
	
	public YqlFinanceQuoteFetcher() {
		this.rest = new Rest();
		this.query = "";		
	}

	public String createQuotesRequestString(ArrayList<StockRequest> request) {
		
		String reqStr = yqlUrl + yqlQuery + "symbol" + YQL_EQUAL + YQL_DQUOTES;
		for(StockRequest s:request) {
			reqStr += s.getSymbol() + YQL_COMMA;
		}
		return reqStr + YQL_DQUOTES + yqlDB;		
	}
	public String createQuoteRequestString(StockRequest request) {
		return yqlUrl + yqlQuery + "symbol" + YQL_EQUAL + YQL_DQUOTES + request.getSymbol() + YQL_DQUOTES + yqlDB;
	}
	public String getSymbolQuote(String symbol) {
		
		String json = null;
		this.query = yqlUrl + yqlQuery + "symbol" + YQL_EQUAL + YQL_DQUOTES + symbol + YQL_DQUOTES + yqlDB;
		try {
			json = rest.sendGet(this.query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return json;
	}
		
	public StockPrice parseXmlSymbolPrice(StockRequest request, String queryStr) {        
        
		// parsing the XML document for a stock quote from Yahoo Finance Server
		// that has the following structure:
		
//		<?xml version="1.0" encoding="UTF-8"?>
//		<query xmlns:yahoo="rest://www.yahooapis.com/v1/base.rng"
//			yahoo:count="3" yahoo:created="2015-11-06T23:24:10Z" yahoo:lang="en-US">
//			<results>
//				<quote symbol="yhoo">
//					<AverageDailyVolume>18146900</AverageDailyVolume>
//					<Change>-0.92</Change>
//					<DaysLow>33.46</DaysLow>
//					<DaysHigh>35.20</DaysHigh>
//					<YearLow>27.20</YearLow>
//					<YearHigh>52.62</YearHigh>
//					<MarketCapitalization>32.39B</MarketCapitalization>
//					<LastTradePriceOnly>34.20</LastTradePriceOnly>
//					<DaysRange>33.46 - 35.20</DaysRange>
//					<Name>Yahoo! Inc.</Name>
//					<Symbol>yhoo</Symbol>
//					<Volume>16602378</Volume>
//					<StockExchange>NMS</StockExchange>
//				</quote>
//			</results>
//		</query>

        try {
        	Builder parser = new Builder();
        	Document doc = (Document) parser.build(queryStr);
        	Element root = doc.getRootElement();
            Elements query = root.getChildElements();
            Element results = query.get(0);
            Element quote = results.getFirstChildElement("quote");
            String quoteSymbol = quote.getAttributeValue("symbol");
            int count = quote.getChildCount();
            
            String price = "", change = "";
            if(quoteSymbol.toUpperCase().equals(request.getSymbol().toUpperCase())) {	            
            	for(int i=0; i<count;i++) {
            		Node quoteElement = quote.getChild(i);
            		String val = quoteElement.getValue();
            		String elName = ((Element) quoteElement).getLocalName();            		
            		switch (elName) {
            			case "LastTradePriceOnly": {            				
            				if(val.length() != 0)
            					price = quoteElement.getValue();
            				break;
            			}
            			case "Change": {
            				if(val.length() != 0)
            					change = quoteElement.getValue();
            				break;
            			}
            			default:
            				break;
            		}                    
                }	            	            	
            }
            if(price != null && change != null) {
            	StockPrice sp = new StockPrice(request.getSymbol(), price, change, null);
            	sp.setRequestData(request);
            	return sp;
            }            	
        }
        catch (ParsingException ex) {
          System.err.println("XML malformed. How embarrassing!");
        }
        catch (IOException ex) {
          System.err.println("The site may be down today.");
        }
        return null;
	}
	public StockPrice[] parseXmlSymbolPrices(ArrayList<StockRequest> request, String queryStr) {        
        
		// parsing the XML document for a stock quote from Yahoo Finance Server
		// that has the following structure:
		
//		<?xml version="1.0" encoding="UTF-8"?>
//		<query xmlns:yahoo="rest://www.yahooapis.com/v1/base.rng"
//			yahoo:count="3" yahoo:created="2015-11-06T23:24:10Z" yahoo:lang="en-US">
//			<results>
//				<quote symbol="yhoo">
//					<AverageDailyVolume>18146900</AverageDailyVolume>
//					<Change>-0.92</Change>
//					<DaysLow>33.46</DaysLow>
//					<DaysHigh>35.20</DaysHigh>
//					<YearLow>27.20</YearLow>
//					<YearHigh>52.62</YearHigh>
//					<MarketCapitalization>32.39B</MarketCapitalization>
//					<LastTradePriceOnly>34.20</LastTradePriceOnly>
//					<DaysRange>33.46 - 35.20</DaysRange>
//					<Name>Yahoo! Inc.</Name>
//					<Symbol>yhoo</Symbol>
//					<Volume>16602378</Volume>
//					<StockExchange>NMS</StockExchange>
//				</quote>
//			</results>
//		</query>
		
		StockPrice[] prices = null;
        try {
        	Builder parser = new Builder();
        	Document doc = (Document) parser.build(queryStr);
        	Element root = doc.getRootElement();
            Elements query = root.getChildElements();
            Element results = query.get(0);
            int nQuotes = results.getChildCount();
            Elements quotes = results.getChildElements();
    		prices = new StockPrice[nQuotes];
            
            int n = 0;
            String symbol = null;
            for(StockRequest s: request) {
            	symbol = s.getSymbol();
                Element quote = quotes.get(n);
                String quoteSymbol = quote.getAttributeValue("symbol");
                
                String price = null, change = null;
                StockPrice sp = null;
                if(quoteSymbol.toUpperCase().equals(symbol.toUpperCase())) {	            
            		price = quote.getFirstChildElement("LastTradePriceOnly").getValue();
            		change = quote.getFirstChildElement("Change").getValue();
                }
                if(price.isEmpty()) {
                	sp = new StockPrice(symbol, price, change, null);
                	sp.setSymbolValid(false);
                }
                else {
                	sp = new StockPrice(symbol, price, change, null);
                }
            	sp.setRequestData(s);
            	prices[n] = sp;
            	n++;
            }
        }
        catch (ParsingException ex) {
          System.err.println("XML malformed. How embarrassing!");
        }
        catch (IOException ex) {
          System.err.println("The site may be down today.");
        }
        return prices;
	}
	public StockPrice[] parseJsonSymbolPrices(ArrayList<StockRequest> request, String queryUrl) {        
        
		// parsing the XML document for a stock quote from Yahoo Finance Server
		// that has the following structure:
		
//		{
//			 "query": {
//			  "count": 2,
//			  "created": "2015-11-16T20:32:51Z",
//			  "lang": "en-US",
//			  "results": {
//			   "quote": [
//			    {
//			     "symbol": "msft",
//			     "AverageDailyVolume": "37414200",
//			     "Change": "+0.965",
//			     "DaysLow": "52.850",
//			     "DaysHigh": "53.875",
//			     "YearLow": "39.720",
//			     "YearHigh": "54.980",
//			     "MarketCapitalization": "429.79B",
//			     "LastTradePriceOnly": "53.805",
//			     "DaysRange": "52.850 - 53.875",
//			     "Name": "Microsoft Corporation",
//			     "Symbol": "msft",
//			     "Volume": "21992194",
//			     "StockExchange": "NMS"
//			    },
//			    {
//			     "symbol": "yhoo",
//			     "AverageDailyVolume": "17669900",
//			     "Change": "+0.77",
//			     "DaysLow": "32.12",
//			     "DaysHigh": "32.99",
//			     "YearLow": "27.20",
//			     "YearHigh": "52.62",
//			     "MarketCapitalization": "31.13B",
//			     "LastTradePriceOnly": "32.96",
//			     "DaysRange": "32.12 - 32.99",
//			     "Name": "Yahoo! Inc.",
//			     "Symbol": "yhoo",
//			     "Volume": "7889363",
//			     "StockExchange": "NMS"
//			    }
//			   ]
//			  }
//			 }
//			}
		
		StockPrice[] prices = null;
        try {
        	String jsonStr = rest.sendGet(queryUrl);
            JSONObject root = new JSONObject(jsonStr);
                        
            // start traverse down the JSON object from root
            JSONObject query = (JSONObject) root.get("query");
            int count = query.getInt("count");
            prices = new StockPrice[count];
            String quoteTime = query.getString("created");
            JSONObject results = (JSONObject)query.get("results");
            
            int n = 0;
            JSONArray quotes = null;
            JSONObject quote = null;
            String symbol = null;
            for(StockRequest s: request) {
            	symbol = s.getSymbol();                
                String price = null, change = null;
                StockPrice sp = null;
                if(count == 1) {
                	quote = (JSONObject)results.get("quote");
                }
                else if(count > 1) {
                	quotes = (JSONArray)results.get("quote");
                	quote = (JSONObject)quotes.get(n);
                }
                String quoteSymbol = (String) quote.get("symbol"); 
                if(quoteSymbol.toUpperCase().equals(symbol.toUpperCase())) {	            
            		price = quote.isNull("LastTradePriceOnly") ? null : quote.getString("LastTradePriceOnly");
            		change = quote.isNull("Change") ? null : quote.getString("Change");
                }
                if(price == null || price.isEmpty()) {
                	sp = new StockPrice(symbol, price, change, quoteTime);
                	sp.setSymbolValid(false);
                }
                else {
                	sp = new StockPrice(symbol, price, change, quoteTime);
                }
            	sp.setRequestData(s);
            	prices[n] = sp;
            	n++;
            } 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return prices;
	}

}
