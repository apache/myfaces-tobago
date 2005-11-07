package org.apache.myfaces.tobago.apt;

import org.w3c.dom.Document;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 06.11.2005
 * Time: 23:43:31
 * To change this template use File | Settings | File Templates.
 */
public class DocumentAndFileName {
  private Document document;
  private String fileName;
  private String packageName;

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public Document getDocument() {
    return document;
  }

  public void setDocument(Document document) {
    this.document = document;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
