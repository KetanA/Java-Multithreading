package ketana.coding.Apple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
* @author KetanA
* Created with Eclipse IDE. Date: 08/14/2016 Time: 11:50 AM
* RequestHandler class is responsible for making HTTP Get 
* requests to web resource specified by URL object.
* It parses response code and recursively fetches new 
* location if the response code is 'Redirect'. 
*/

public class RequestHandler {
	private final String USER_AGENT = "Mozilla/5.0";
	private final String REQUEST_METHOD = "GET";
	private final boolean FOLLOW_REDIRECTS = true;
	private final String REQUEST_URL = "http://en.wikipedia.org/wiki/Main_Page";
	private final Logger logger = Logger.getLogger(RequestHandler.class.getName());
	
	
	/**
	 * sendRequest() method is responsible for making HTTP GET 
	 * call to the resource specified by URL object.
	 * If responseCode is 'Redirect'(301/302/303), it fetches the content 
	 * from redirected location, specified by 'Location' field in the header.
	 * */
	public void sendRequest() {
		URL obj;
		try {
			// Instantiating HttpURLConnection object for making GET request.
			obj = new URL(REQUEST_URL);
		    HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
		    connection.setRequestMethod(REQUEST_METHOD);
		    connection.setRequestProperty("User-Agent", USER_AGENT);
		    connection.setInstanceFollowRedirects(FOLLOW_REDIRECTS);
			HttpURLConnection.setFollowRedirects(FOLLOW_REDIRECTS);
		
			// Checking response code for successful request.
			// If responseCode==200, read the response,
			// if responseCode==3xx, i.e., a redirect, then make the request to 
			// new redirected link, specified by 'Location' field. 
			// NOTE: Only one level of redirection is supported for now.  
			// Can be modified to support multiple levels of Redirections.
			int responseCode = connection.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
					responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
					responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
				logger.info("Redirect received in responseCode");
				String newUrl = connection.getHeaderField("Location");
				connection = (HttpURLConnection) new URL(newUrl).openConnection();
			}
			responseCode = connection.getResponseCode();
			
			// process response message if responseCode==200 i.e., success.
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				//Uncomment following line to log response data.
				//logger.info(response.toString());
			} else {
				logger.warning("Http GET request was unsuccessful!");
			}
		} catch (MalformedURLException e) {
			logger.severe("MalformedURLException: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.severe("IOException: " + e.getMessage());
			e.printStackTrace();
		}
   }
}
