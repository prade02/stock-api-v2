package invoker;

import lambda.handler.EventHandler;
import lambda.rest.RestEngine;
import lambda.util.JsonUtil;
import lambda.util.Utility;

import java.util.List;
import java.util.LinkedList;

public class Application {
  public static void main(String[] args) {
    System.out.println("App runs");
    EventHandler handler = new EventHandler();
    handler.handleRequest(null, new ContextImpl());
  }
}
