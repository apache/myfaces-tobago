/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.example.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

@SpringBootApplication
@ComponentScan(basePackages = "org.apache.myfaces.tobago")
public class TobagoApplication {

  public static final String FACES_SERVLET_NAME = "FacesServlet";

  public static void main(String[] args) {
    SpringApplication.run(TobagoApplication.class, args);
  }

  /**
   * This is to put &lt;multipart-config&gt; into the FacesServlet.
   * This "normally" happens in the web.xml file.
   */
  @Bean
  public ServletContextInitializer multipartServletContextInitializer(MultipartConfigElement multipartConfigElement) {
    return servletContext -> {
      ServletRegistration servletRegistration = servletContext.getServletRegistration(FACES_SERVLET_NAME);
      if (servletRegistration instanceof ServletRegistration.Dynamic) {
        ((ServletRegistration.Dynamic) servletRegistration).setMultipartConfig(multipartConfigElement);
      }
    };
  }
}
