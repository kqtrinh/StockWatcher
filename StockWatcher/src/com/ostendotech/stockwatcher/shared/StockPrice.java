package com.ostendotech.stockwatcher.client;

import com.google.gwt.user.client.rpc.IsSerializable;

// this is the stock price class that is sent from the server thru serialization
public class StockPrice implements IsSerializable {
	  
	private StockRequest symbolData;
	private String symbol;
	private String price;
	private String change;
	private String time;
	private boolean validSymbol = true;
	
	  public StockPrice() {
	  }
	  
	  public StockPrice(String symbol, String price, String change, String time) {
	    this.symbol = symbol;
	    this.price = price;
	    this.change = change;
	    this.time = time;
	  }	      	  
	  public String getSymbol() {
	    return this.symbol;
	  }
	  
	  public double getPrice() {
	    return Double.parseDouble(this.price);
	  }
	  
	  public double getChange() {
	    return Double.parseDouble(this.change);
	  }
	  
	  public double getChangePercent() {
	    return 100.0 * Double.parseDouble(this.change) / Double.parseDouble(this.price);
	  }
	  
	  public void setSymbol(String symbol) {
	    this.symbol = symbol;
	  }
	  
	  public void setPrice(String price) {
	    this.price = price;
	  }
	  
	  public void setChange(String change) {
	    this.change = change;
	  }

	public StockRequest getRequestData() {
		return symbolData;
	}

	public void setRequestData(StockRequest symbolData) {
		this.symbolData = symbolData;
	}
	public void setSymbolValid(boolean b) {
		this.validSymbol = b;
	}
	public boolean isValidSymbol() {
		return this.validSymbol;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}