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

import org.apache.commons.lang3.StringUtils;
import org.apache.myfaces.tobago.example.data.LocaleList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIInput;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Named
public class Countries {

  private static final Logger LOG = LoggerFactory.getLogger(Countries.class);

  public List<String> prefixed(final UIInput input) {
    final String prefix = (String) input.getSubmittedValue();
    LOG.info("Creating items for prefix: '" + prefix + "'");
    final List<String> result = new ArrayList<String>();
    for (final String name : LocaleList.COUNTRY) {
      if (StringUtils.startsWithIgnoreCase(name, prefix)) {
        result.add(name);
      }
      if (result.size() > 100) { // this value should be greater than the value of the input control
        break;
      }
    }
    return result;
  }

  public List<String> contains(final UIInput input) {
    final String contain = (String) input.getSubmittedValue();
    LOG.info("Creating items which contain: '" + contain + "'");
    final List<String> result = new ArrayList<String>();
    for (final String name : LocaleList.COUNTRY) {
      if (StringUtils.containsIgnoreCase(name, contain)) {
        result.add(name);
      }
      if (result.size() > 100) { // this value should be greater than the value of the input control
        break;
      }
    }
    return result;
  }

  public List<String> holiday(final UIInput input) {
    final List<String> result = new ArrayList<String>();
    for (final String name : LocaleList.HOLIDAY) {
      result.add(name);
      if (result.size() > 100) { // this value should be greater than the value of the input control
        break;
      }
    }
    return result;
  }

}
