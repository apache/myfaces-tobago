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
import org.apache.myfaces.tobago.component.UIInput;

import java.util.ArrayList;
import java.util.List;


public class InputSuggestController {

  private static final Logger LOG = LoggerFactory.getLogger(InputSuggestController.class);

  public List<String> getInputSuggestItems(UIInput component) {
    String prefix = (String) component.getSubmittedValue();
    LOG.info("Creating items for prefix :\"" + prefix + "\"");
    List<String> li = new ArrayList<String>();
    li.add(prefix + 1);
    li.add(prefix + 2);
    li.add(prefix + 3);
    li.add(prefix + 4);
    li.add(prefix + 5);
    li.add(prefix + 6);
    return li;
  }
  
}
