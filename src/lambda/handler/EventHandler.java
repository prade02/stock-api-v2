package lambda.handler;

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;

public class EventHandler implements RequestHandler<ScheduledEvent, String> {
  private static final String EVENT_PROCESSED = "EVENT PROCESSED";

  @Override
  public String handleRequest(ScheduledEvent event, Context context) {
    //TODO: get gold price from external api
    //TODO: save the gold price to thingspeak
    //TODO: save the gold price to dynamodb
    return EVENT_PROCESSED;
  }
}
