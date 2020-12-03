package org.apache.myfaces.tobago.example.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.apache.myfaces.tobago")
public class TobagoApplication {

  public static void main(String[] args) {
    SpringApplication.run(TobagoApplication.class, args);
  }

}
