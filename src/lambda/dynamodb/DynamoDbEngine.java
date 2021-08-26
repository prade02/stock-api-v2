package lambda.dynamodb;

import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.Map;
import java.util.HashMap;

public class DynamoDbEngine {

  public static PutItemRequest createPutItemRequest(String tableName, Map<String, AttributeValue> item) {
    return new PutItemRequest(tableName, item);
  }

  public static void putItem(double price, String id, String timestamp) {
    // create a map containing all the key/value pairs
    Map<String, AttributeValue> item = new HashMap<>();
    item.put("price_id", new AttributeValue(id));
    item.put("time", new AttributeValue(timestamp));
    item.put("price", new AttributeValue(Double.toString(price)));

    PutItemRequest putItemRequest = createPutItemRequest("gold_price", item);

    AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClient.builder().defaultClient();
    amazonDynamoDB.putItem(putItemRequest);
  }
}
