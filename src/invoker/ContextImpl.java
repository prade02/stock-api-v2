package invoker;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.ClientContext;
import java.lang.UnsupportedOperationException;
import java.util.UUID;

public class ContextImpl implements Context {

  public String getAwsRequestId() {
    return UUID.randomUUID().toString();
  }

  public String getLogGroupName() {
    throw new UnsupportedOperationException();
  }

  public String getLogStreamName() {
    throw new UnsupportedOperationException();
  }

  public String getFunctionName() {
    throw new UnsupportedOperationException();
  }

  public String getFunctionVersion() {
    throw new UnsupportedOperationException();
  }

  public String getInvokedFunctionArn() {
    throw new UnsupportedOperationException();
  }

  public CognitoIdentity getIdentity() {
    throw new UnsupportedOperationException();
  }

  public ClientContext getClientContext() {
    throw new UnsupportedOperationException();
  }

  public int getRemainingTimeInMillis() {
    throw new UnsupportedOperationException();
  }

  public int getMemoryLimitInMB() {
    throw new UnsupportedOperationException();
  }

  public LambdaLogger getLogger() {
    return new LambdaLoggerImpl();
  }
}
