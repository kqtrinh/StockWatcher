package com.ostendotech.stockwatcher.client.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ostendotech.stockwatcher.client.DelistedException;
import com.ostendotech.stockwatcher.shared.StockPrice;
import com.ostendotech.stockwatcher.shared.StockRequest;

@RemoteServiceRelativePath("stockprices")
public interface StockPriceService extends RemoteService {
	StockPrice[] getPrices(ArrayList<StockRequest> symbols) throws DelistedException;
}
