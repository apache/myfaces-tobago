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

package org.apache.myfaces.tobago.example.demo;

import java.io.Serializable;

public enum DockerServer implements Serializable {

  tomcat(
      "Tomcat",
      "/usr/local/tomcat/webapps/demo.war",
      8080,
      0,
      "tomcat",
      new String[]{
          "7-jre8",
          "8-jre8",
          "8-jre11",
          "9-jre8",
          "9-jre11"},
      false,
//    XXX JSTL is only needed, if jsf=mojarra... and Server = Tomcat
      ""),
  tomee(
      "TomEE",
      "/usr/local/tomee/webapps/demo.war",
      8080,
      0,
      "tomee",
      new String[]{
          "8.0-jre8-plus",
          "9.0-jre11-plus"},
      false,
      " -Djsf=provided"),
  liberty(
      "Liberty",
      "/config/dropins/demo.war",
      9080,
      9443,
      "websphere-liberty",
      new String[]{
          "webProfile7",
          "webProfile8"},
      true,
      " -Djsf=provided"),
  wildfly(
      "Wildfly",
      "/opt/jboss/wildfly/standalone/deployments/demo.war",
      8080,
      8443,
      "jboss/wildfly",
      new String[]{
          "8.2.1.Final",
          "9.0.2.Final",
          "10.1.0.Final",
          "11.0.0.Final",
          "12.0.0.Final",
          "13.0.0.Final",
          "14.0.1.Final"
      },
      true,
      " -Djsf=provided");

  private String displayName;
  private String volume;
  private int port;
  private int sslPort;
  private String image;
  private String[] tags;
  private boolean ssl;
  private String mavenOptions;

  DockerServer(
      final String displayName, final String volume, final int port, final int sslPort,
      final String image, final String[] tags, final boolean ssl, final String mavenOptions) {
    this.displayName = displayName;
    this.volume = volume;
    this.port = port;
    this.sslPort = sslPort;
    this.image = image;
    this.tags = tags;
    this.ssl = ssl;
    this.mavenOptions = mavenOptions;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getVolume() {
    return volume;
  }

  public int getPort() {
    return port;
  }

  public int getSslPort() {
    return sslPort;
  }

  public String getImage() {
    return image;
  }

  public String[] getTags() {
    return tags;
  }

  public boolean isSsl() {
    return ssl;
  }

  public String getMavenOptions() {
    return mavenOptions;
  }
}
