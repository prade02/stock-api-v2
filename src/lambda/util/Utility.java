package lambda.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

public class Utility {

  public static double round(double value, int places) {
    BigDecimal bd = new BigDecimal(Double.toString(value));
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

  public static double round(String value, int places) {
    return round(Double.parseDouble(value), places);
  }

  public static String getTimestamp() {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
    return formatter.format(new Date());
  }
}
