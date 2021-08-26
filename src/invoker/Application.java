package invoker;

import lambda.rest.RestEngine;
import lambda.util.JsonUtil;
import lambda.util.Utility;

import java.util.List;
import java.util.LinkedList;

public class Application {
  public static void main(String[] args) {
    System.out.println("App runs");
    double price = Double.parseDouble("133883") / 28.5;
    price = Utility.round(price,2);
    System.out.println(price);
  }
}
