package lambda.util;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

public class JsonUtil {

  public static JsonObject stringToJson(String sJson) {
    return JsonParser.parseString(sJson).getAsJsonObject();
  }

  public static String getStringFromJson(String sJson, String key) {
    return stringToJson(sJson).getAsJsonPrimitive(key).getAsString();
  }

}
