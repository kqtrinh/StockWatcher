package com.ostendotech.stockwatcher.client.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ostendotech.stockwatcher.shared.StockPrice;
import com.ostendotech.stockwatcher.shared.StockRequest;

public interface StockPriceServiceAsync {
	void getPrices(ArrayList<StockRequest> symbols, AsyncCallback<StockPrice[]> callback);
}
