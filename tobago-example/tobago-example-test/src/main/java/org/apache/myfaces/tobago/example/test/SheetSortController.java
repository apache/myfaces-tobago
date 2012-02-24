package org.apache.myfaces.tobago.example.test;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SheetSortController {

  private static final Logger LOG = LoggerFactory.getLogger(SheetSortController.class);

  private List<Entry> list;

  public SheetSortController() {
    init();
  }

  private void init() {
    list = new ArrayList<Entry>();
    for (int i = 0; i < 26; i++) {
      list.add(new Entry(i));
    }
  }

  public List<Entry> getList() {
    return list;
  }

  public static final class Entry {
    
    private String first;
    private String second;
    private String third;
    private String fourth;
    private Date fifth;
    private String sixth;

    private Entry(int i) {
      this.first = "" + upper(i);
      i++;
      this.second = "" + upper(i) + lower(i);
      i++;
      this.third = "" + upper(i) + lower(i) + lower(i);
      i++;
      this.fourth = "" + upper(i) + lower(i) + lower(i) + lower(i);
      i++;
      final String dateString = "2012-02-" + i % 26;
      try {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.fifth = simpleDateFormat.parse(dateString);
      } catch (ParseException e) {
        LOG.error("string=" + dateString, e);
      }
      i++;
      this.sixth = "" + upper(i) + lower(i) + lower(i) + lower(i) + lower(i);
    }
    
    private char upper(int i) {
      return (char) (i % 26 + 'A');
    }

    private char lower(int i) {
      return (char) (i % 26 + 'a');
    }

    public String getFirst() {
      return first;
    }

    public void setFirst(String first) {
      this.first = first;
    }

    public String getSecond() {
      return second;
    }

    public void setSecond(String second) {
      this.second = second;
    }

    public String getThird() {
      return third;
    }

    public void setThird(String third) {
      this.third = third;
    }

    public String getFourth() {
      return fourth;
    }

    public void setFourth(String fourth) {
      this.fourth = fourth;
    }

    public Date getFifth() {
      return fifth;
    }

    public void setFifth(Date fifth) {
      this.fifth = fifth;
    }

    public String getSixth() {
      return sixth;
    }

    public void setSixth(String sixth) {
      this.sixth = sixth;
    }
  }
}
