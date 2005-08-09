package com.atanion.tobago.demo.download;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 15.12.2004
 * Time: 14:21:57
 */
public class DownloadInfo {

  private String email;
  private Date endTime;

  public DownloadInfo(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
}
