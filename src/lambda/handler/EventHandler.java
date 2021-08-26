package lambda.handler;

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.List;
import java.util.LinkedList;

import lambda.rest.RestEngine;
import lambda.dynamodb.DynamoDbEngine;
import lambda.util.JsonUtil;
import lambda.util.Utility;

public class EventHandler implements RequestHandler<ScheduledEvent, String> {
  private static final String EVENT_PROCESSED = "EVENT PROCESSED";
  private static final String SAVED_TO_THINGSPEAK = "SAVED TO THINGSPEAK";
  private static final String SAVED_TO_DDB = "SAVED TO DDB";

  private String stockApiUri;
  private String thingSpeakUri;
  private String stockApiApiKey;
  private String thingSpeakApiApiKey;

  private final String X_ACCESS_TOKEN = "x-access-token";
  private final String STOCK_API_URI = "STOCK_API_URI";
  private final String STOCK_API_API_KEY = "STOCK_API_API_KEY";
  private final String THINGSPEAK_API_URI = "THINGSPEAK_API_URI";
  private final String THINGSPEAK_API_API_KEY = "THINGSPEAK_API_API_KEY";
  private final String GET_ENV_VAR_LOCALLY = "GET_ENV_VAR_LOCALLY";

  LambdaLogger logger;
  String awsRequestID;

  @Override
  public String handleRequest(ScheduledEvent event, Context context) {
    logger = context.getLogger();
    awsRequestID = context.getAwsRequestId();
    setEnvVariables();
    double price = this.getPrice();
    saveToThingSpeak(price);
    saveToDDB(price);
    return EVENT_PROCESSED;
  }

  private void setEnvVariables() {
    boolean getEnvLocally = Boolean.parseBoolean(System.getenv(GET_ENV_VAR_LOCALLY));
    logger.log("GET_ENV_VAR_LOCALLY: " + getEnvLocally);
    if((stockApiUri = System.getenv(STOCK_API_URI)) == null && getEnvLocally)
      stockApiUri = "https://www.goldapi.io/api/XAU/INR";
    if((thingSpeakUri = System.getenv(THINGSPEAK_API_URI)) == null && getEnvLocally)
      thingSpeakUri = "https://api.thingspeak.com/update";
    if((stockApiApiKey = System.getenv(STOCK_API_API_KEY)) == null && getEnvLocally)
      stockApiApiKey = "goldapi-j37uxukjtotcud-io";
    if((thingSpeakApiApiKey = System.getenv(THINGSPEAK_API_API_KEY)) == null && getEnvLocally)
      thingSpeakApiApiKey = "HSQ1I2JEHW9EHG4V";
  }

  private double getPrice() {
    List<String> headers = new LinkedList<>();
    headers.add(X_ACCESS_TOKEN);
    headers.add(stockApiApiKey);
    String response = RestEngine.makeHttpRequest("GET", stockApiUri, headers, null);
    String price = JsonUtil.getStringFromJson(response, "price");
    double dPrice = Double.parseDouble(price) / 28.5;
    dPrice = Utility.round(dPrice, 2);
    logger.log("GOT PRICE: " + dPrice);
    return dPrice;
  }

  private void saveToThingSpeak(double price) {
    String uri = thingSpeakUri + "?api_key=" + thingSpeakApiApiKey + "&field1=" + Double.toString(price);
    RestEngine.makeHttpRequest("GET", uri, null, null);
    logger.log(SAVED_TO_THINGSPEAK);
  }

  private void saveToDDB(double price){
    DynamoDbEngine.putItem(price, awsRequestID, Utility.getTimestamp());
    logger.log(SAVED_TO_DDB);
  }

}
