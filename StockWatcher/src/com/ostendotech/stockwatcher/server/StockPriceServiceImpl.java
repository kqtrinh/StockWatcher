package com.ostendotech.stockwatcher.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ostendotech.stockwatcher.client.DelistedException;
import com.ostendotech.stockwatcher.client.StockPrice;
import com.ostendotech.stockwatcher.client.StockRequest;
import com.ostendotech.stockwatcher.client.service.StockPriceService;

public class StockPriceServiceImpl extends RemoteServiceServlet implements StockPriceService {

	private static final long serialVersionUID = 1L;		
	private YqlFinanceQuoteFetcher yql;
	public StockPriceServiceImpl() {
		yql = new YqlFinanceQuoteFetcher();		
	}
	@Override
	public StockPrice[] getPrices(ArrayList<StockRequest> request) throws DelistedException {		    		
		// get the quote from Yahoo Finance server
		String queryStr = yql.createQuotesRequestString(request);			
		return yql.parseJsonSymbolPrices(request, queryStr);
	}
}
