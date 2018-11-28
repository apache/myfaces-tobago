package org.apache.myfaces.tobago.example.demo;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class DockerController implements Serializable {

  private DockerServer server;
  private int port = 8081;
  private int sslPort = 8443;
  private String tag;

  public DockerController() {
    setServer(DockerServer.tomee);
  }

  public DockerServer[] getServers() {
    return DockerServer.values();
  }

  public String[] getTags() {
    return server.getTags();
  }

  public DockerServer getServer() {
    return server;
  }

  public void setServer(DockerServer server) {
    if (this.server != server) {
      tag = server.getTags()[0];
    }
    this.server = server;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public int getSslPort() {
    return sslPort;
  }

  public void setSslPort(int sslPort) {
    this.sslPort = sslPort;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getCommandLine() {
    final StringBuilder builder = new StringBuilder();
    builder.append("mvn clean install -Djsf=provided\ndocker run -it --rm");
    builder.append(" -p ");
    builder.append(port);
    builder.append(":");
    builder.append(getServer().getPort());
    if (server.isSsl()) {
      builder.append(" -p ");
      builder.append(sslPort);
      builder.append(":");
      builder.append(getServer().getSslPort());
    }
    builder.append(" -v `pwd`/target/tobago-example-demo.war:");
    builder.append(server.getVolume());
    builder.append(" ");
    builder.append(server.getImage());
    builder.append(":");
    builder.append(tag);
    return builder.toString();
  }

  public String getUrl() {
    final StringBuilder builder = new StringBuilder();
    builder.append("http://localhost:");
    builder.append(port);
    builder.append("/demo/");
    return builder.toString();
  }

  public String getSslUrl() {
    final StringBuilder builder = new StringBuilder();
    builder.append("https://localhost:");
    builder.append(sslPort);
    builder.append("/demo/");
    return builder.toString();
  }
}
