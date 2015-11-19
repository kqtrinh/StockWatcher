package com.ostendotech.stockwatcher.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ostendotech.stockwatcher.shared.StockPrice;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.ParsingException;

public class Rest {
	
	private final String USER_AGENT = "Mozilla/5.0";
	public static void main(String[] args) throws Exception {
		//url = "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20yahoo.finance.quote%20WHERE%20symbol%3D%22msft%2Cyhoo%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
		String url = "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20yahoo.finance.quote%20WHERE%20symbol%3D%22msft%22&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
		String uuu = "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20yahoo.finance.quote%20WHERE%20symbol%3D%22msft%2C%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
//		parseSymbolPrice("MSFT", url);
//		Rest http = new Rest();
//		System.out.println("Testing 1 - Send Http GET request");
//		http.sendGet(url);
//		System.out.println("\nTesting 2 - Send Http POST request");
//		url = "https://selfsolve.apple.com/wcResults.do";
//		http.sendPost(url);
		
		parseJson();
	}

	public static StockPrice parseSymbolPrice(String symbol, String queryStr) {        
        
        try {
        	Builder parser = new Builder();
        	Document doc = (Document) parser.build(queryStr);
        	Element root = doc.getRootElement();
        	//System.out.println("Root Node : " + root.getLocalName());
            Elements query = root.getChildElements();
            Element results = query.get(0);
            Element quote = results.getFirstChildElement("quote");
            String quoteSymbol = quote.getAttributeValue("symbol");
            int count = quote.getChildCount();
            String price = "", change = "";
            if(quoteSymbol.toUpperCase().equals(symbol.toUpperCase())) {	            
            	for(int i=0; i<count;i++) {
            		Node quoteVar = quote.getChild(i);
            		String str = ((Element) quoteVar).getLocalName();
            		switch (str) {
            			case "LastTradePriceOnly": {
            				price = quoteVar.getValue();
            				break;
            			}
            			case "Change": {
            				change = quoteVar.getValue();
            				break;
            			}
            			default:
            				break;
            		}                    
                }	            	            	
            }
            if(price != null && change != null)
            	return new StockPrice(symbol, price, change, null);
        }
        catch (ParsingException ex) {
          System.err.println("XML malformed. How embarrassing!");
        }
        catch (IOException ex) {
          System.err.println("The site may be down today.");
        }
        return null;
	}

	// HTTP GET request
	public String sendGet(String url) throws Exception {	
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

//		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'GET' request to URL : " + url);
//		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		//System.out.println(response.toString());
		return response.toString();
	}
	
	// HTTP POST request
	public String sendPost(String url) throws Exception {		
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		//System.out.println(response.toString());
		return response.toString();
	}
	public static void parseJson() throws Exception {
		
//        String urlStr = "http://freemusicarchive.org/api/get/genres.json?api_key=60BLHNQCAOUFPIBZ&limit=2";
//		URL url = new URL(urlStr);
//        String genreJson = IOUtils.toString(url);
//        JSONObject json = new JSONObject(genreJson);
//        // get the title
//        String title = (String) json.get("title");
//        // get the data
//        JSONArray genreArray = (JSONArray) json.get("dataset");
//        // get the first genre
//        JSONObject firstGenre = (JSONObject) genreArray.get(0);
//        String genre = (String) firstGenre.get("genre_title");
		String urlStr = "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20yahoo.finance.quote%20WHERE%20symbol%3D%22msft%2Cintl%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
		URL url = new URL(urlStr);
        String root = IOUtils.toString(url);
        JSONObject jsonObj = new JSONObject(root);
        JSONObject query = (JSONObject) jsonObj.get("query");
        int count = query.getInt("count");
        String created = query.getString("created");
        JSONObject results = (JSONObject)query.get("results");
        
        JSONArray quotes = null;
        JSONObject quote = null;
        if(count <= 0) {
        	
        }
        else if(count == 1) {
        	quote = (JSONObject)results.get("quote");
        }
        else if(count > 1) {
        	quotes = (JSONArray)results.get("quote");
        	quote = (JSONObject)quotes.get(0);
        }
        //String date = (String) jsonObj.get("created");
        String symbol = quote.getString("symbol");
        if(quote.length() == 1) {
        	;
        }
	}
}
