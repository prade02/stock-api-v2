package lambda.rest;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;
import java.net.http.HttpResponse;

import java.util.List;

public class RestEngine {

  private static final String HTTP_REQUEST_COMPLETE = "HTTP REQUEST COMPLETE";
  private static final String EXCEPTION = "EXCEPTION";

  /*
  generic method to make GET/POST calls to the URI.
  */
  public static String makeHttpRequest(String method, String sURI, List<String> headerList, String body) {
    String response = null;
    try {
      // create new HttpClient using its static method
      HttpClient httpClient = HttpClient.newHttpClient();

      // create an uri
      URI uri = new URI(sURI);

      // create a GET HttpRequest
      HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder(uri);
      if(headerList != null)
        httpRequestBuilder = httpRequestBuilder.headers(headerList.toArray(new String[0]));
      HttpRequest httpRequest = null;
      if(method.equalsIgnoreCase("POST"))
        httpRequest = httpRequestBuilder.POST(HttpRequest.BodyPublishers.ofString(body)).build();
      else
        httpRequest = httpRequestBuilder.build();

      // make GET request using HttpClient
      HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
      if(httpResponse != null && httpResponse.statusCode() == 200) {
        response = httpResponse.body();
        return response;
      }
    } catch(Exception e) {
      System.out.println(EXCEPTION + ": " + e.getMessage());
    }
    return response;
  }

}
