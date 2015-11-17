package com.ostendotech.stockwatcher.client;

import com.google.gwt.user.client.rpc.IsSerializable;

// this is the stock price class that is sent from the client to the server for quote request thru serialization
public class StockRequest implements IsSerializable {	  
	private String symbol;
	private int flexTableRowNum;
	
	public StockRequest() {		
	}
	public StockRequest(int row, String symbol) {
		this.flexTableRowNum = row;
		this.symbol = symbol.toUpperCase();
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getFlexTableRowNum() {
		return flexTableRowNum;
	}
	public void setFlexTableRowNum(int flexTableRowNum) {
		this.flexTableRowNum = flexTableRowNum;
	}
}
