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

import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.example.demo.actionlistener.SimpleTabChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class TabController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(TabController.class);

  private String open = "/image/feather-open.png";
  private String close = "/image/feather.png";
  private int index;
  private SimpleTabChangeListener tabChangeListener;

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getTabOneImage() {
    if (index != 0) {
      return close;
    } else {
      return open;
    }
  }

  public String getTabTwoImage() {
    if (index != 1) {
      return close;
    } else {
      return open;
    }
  }

  public String getTabThreeImage() {
    if (index != 2) {
      return close;
    } else {
      return open;
    }
  }

  public TabChangeListener getTabChangeListener() {
    return tabChangeListener;
  }

  public void setTabChangeListener(final SimpleTabChangeListener tabChangeListener) {
    this.tabChangeListener = tabChangeListener;
  }

  public int getCount() {
    return tabChangeListener.getCount();
  }

  public int getNewTabIndex() {
    return tabChangeListener.getNewTabIndex();
  }

  public int getOldTabIndex() {
    return tabChangeListener.getOldTabIndex();
  }

  public String getClientId() {
    return tabChangeListener.getClientId();
  }
}
