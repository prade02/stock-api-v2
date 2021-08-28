package invoker;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.lang.UnsupportedOperationException;

public class LambdaLoggerImpl implements LambdaLogger {

  public void log(String message) {
    System.out.println(message);
  }

  public void log(byte[] message) {
    throw new UnsupportedOperationException();
  }

}
