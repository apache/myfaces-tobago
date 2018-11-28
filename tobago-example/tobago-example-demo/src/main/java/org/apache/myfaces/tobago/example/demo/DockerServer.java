package org.apache.myfaces.tobago.example.demo;

import java.io.Serializable;

public enum DockerServer implements Serializable {

  tomee(
      "TomEE",
      "/usr/local/tomee/webapps/demo.war",
      8080,
      0,
      "tomee",
      new String[]{
          "8-jre-1.7.5-plus",
          "8-jre-7.0.5-plus",
          "8-jre-7.1.0-plus",
          "8-jre-8.0.0-M1-plus"},
      false),
  liberty(
      "Liberty",
      "/config/dropins/demo.war",
      9080,
      9443,
      "websphere-liberty",
      new String[]{
          "webProfile7",
          "webProfile8"},
      true);

  private String displayName;
  private String volume;
  private int port;
  private int sslPort;
  private String image;
  private String[] tags;
  private boolean ssl;

  DockerServer(
      final String displayName, final String volume, final int port,final int sslPort,
      final String image, final String[] tags, final boolean ssl) {
    this.displayName = displayName;
    this.volume = volume;
    this.port = port;
    this.sslPort = sslPort;
    this.image = image;
    this.tags = tags;
    this.ssl = ssl;
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
}
