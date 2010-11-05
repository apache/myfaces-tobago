package org.apache.myfaces.tobago.internal.layout;

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

import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.Measure;

/**
 * BankHead represents the head information of a bank like the token for this bank, the computed
 * "current" and "minimum" measure and if it will be rendered. A bank is a generalization for columns and rows.
 */
public class BankHead {

  private LayoutToken token;

  private IntervalList intervalList;

  private Measure current;

  private boolean rendered;

  public BankHead(LayoutToken token) {
    this.token = token;
    this.rendered = true;
  }

  public LayoutToken getToken() {
    return token;
  }

  public void setToken(LayoutToken token) {
    this.token = token;
  }

  public IntervalList getIntervalList() {
    return intervalList;
  }

  public void setIntervalList(IntervalList intervalList) {
    this.intervalList = intervalList;
  }

  public Measure getCurrent() {
    return current;
  }

  public void setCurrent(Measure current) {
    this.current = current;
  }

  public boolean isRendered() {
    return rendered;
  }

  public void setRendered(boolean rendered) {
    this.rendered = rendered;
  }

  @Override
  public String toString() {
    return "BankHead{"
        + token
        + "," + intervalList
        + "," + current
        + "," + rendered
        + '}';
  }
}
