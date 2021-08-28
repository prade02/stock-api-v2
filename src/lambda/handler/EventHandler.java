package lambda.handler;

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.List;
import java.util.LinkedList;
import java.lang.Thread;
import java.lang.Runnable;

import lambda.rest.RestEngine;
import lambda.dynamodb.DynamoDbEngine;
import lambda.util.JsonUtil;
import lambda.util.Utility;

public class EventHandler implements RequestHandler<ScheduledEvent, String> {
  private static final String EVENT_PROCESSED = "EVENT PROCESSED";
  private static final String SAVED_TO_THINGSPEAK = "SAVED TO THINGSPEAK";
  private static final String SAVED_TO_DDB = "SAVED TO DDB";
  private static final String DDB_THREAD_START = "DDB THREAD START";
  private static final String THINGSPEAK_THREAD_START = "THINGSPEAK THREAD START";

  private String stockApiUri;
  private String thingSpeakUri;
  private String stockApiApiKey;
  private String thingSpeakApiApiKey;
  private boolean doMultiTask = true;

  private final String X_ACCESS_TOKEN = "x-access-token";
  private final String STOCK_API_URI = "STOCK_API_URI";
  private final String STOCK_API_API_KEY = "STOCK_API_API_KEY";
  private final String THINGSPEAK_API_URI = "THINGSPEAK_API_URI";
  private final String THINGSPEAK_API_API_KEY = "THINGSPEAK_API_API_KEY";
  private final String GET_ENV_VAR_LOCALLY = "GET_ENV_VAR_LOCALLY";
  private final String DO_MULTI_TASK = "DO_MULTI_TASK";
  private final String MULTI_TASKING = "MULTI_TASKING";
  private final String NON_MULTI_TASKING = "NON_MULTI_TASKING";

  LambdaLogger logger;
  String awsRequestID;

  @Override
  public String handleRequest(ScheduledEvent event, Context context) {
    try {
      logger = context.getLogger();
      awsRequestID = context.getAwsRequestId();
      logger.log(awsRequestID);
      setEnvVariables();
      double price = this.getPrice();

      if(doMultiTask) {
        logger.log(MULTI_TASKING);
        Runnable thingSpeakRunnable = () -> saveToThingSpeak(price);
        Runnable ddbRunnable = () -> saveToDDB(price);
        Thread thingSpeakThread = new Thread(thingSpeakRunnable);
        Thread ddbThread = new Thread(ddbRunnable);

        // start threads in parallel
        thingSpeakThread.start();
        ddbThread.start();

        // wait for threads to finish
        thingSpeakThread.join();
        ddbThread.join();
      } else {
        logger.log(NON_MULTI_TASKING);
        saveToThingSpeak(price);
        saveToDDB(price);
      }
    } catch (Exception e) {
        logger.log("Error in handler: " + e.getMessage());
      }
    return EVENT_PROCESSED;
  }

  private void setEnvVariables() {
    // set multi tasking behaviour
    String sDoMultiTask = System.getenv(DO_MULTI_TASK);
    if(sDoMultiTask != null)
      doMultiTask = Boolean.parseBoolean(sDoMultiTask);
    // get env variable locally?
    String sGetEnvLocally = System.getenv(GET_ENV_VAR_LOCALLY);
    boolean getEnvLocally = true;
    // set other variables
    if(sGetEnvLocally != null)
      getEnvLocally = Boolean.parseBoolean(sGetEnvLocally);
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
    logger.log(THINGSPEAK_THREAD_START);
    String uri = thingSpeakUri + "?api_key=" + thingSpeakApiApiKey + "&field1=" + Double.toString(price);
    RestEngine.makeHttpRequest("GET", uri, null, null);
    logger.log(SAVED_TO_THINGSPEAK);
  }

  private void saveToDDB(double price){
    logger.log(DDB_THREAD_START);
    DynamoDbEngine.putItem(price, awsRequestID, Utility.getTimestamp());
    logger.log(SAVED_TO_DDB);
  }

}
