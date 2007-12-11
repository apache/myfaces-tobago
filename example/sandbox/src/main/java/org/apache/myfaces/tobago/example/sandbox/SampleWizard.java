package org.apache.myfaces.tobago.example.sandbox;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UIOutput;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.model.AbstractWizardController;
//import org.apache.myfaces.tobago.model.BeanItem;

public class SampleWizard extends AbstractWizardController {

  private static final Log LOG = LogFactory.getLog(SampleWizard.class);

//  private List<BeanItem> items = new ArrayList<BeanItem>();

  public SampleWizard() {
    setDefaultOutcome("wizard");
  }

  public UIPanel getCurrentComponent() {

    UIOutput out = new UIOutput();
    out.setValue(getIndex());

    UIPanel panel = new UIPanel();
    panel.getChildren().add(out);

    return panel;
  }

  @Override
  public boolean doNext() {

/*    LOG.info("items: " + items);
    items.clear();

    switch (getIndex()) {
      case 1:
        items.add(new BeanItem("out", null, "Bitte geben Sie ihren Namen ein."));
        items.add(new BeanItem("in", "Vorname", null));
        items.add(new BeanItem("in", "Nachname", null));
        break;
      case 2:
        items.add(new BeanItem("out", null, "Bitte geben Sie ihre Adresse ein."));
        items.add(new BeanItem("in", "Straße", null));
        items.add(new BeanItem("in", "Ort", null));
        items.add(new BeanItem("in", "Land", null));
        break;
      default:
    }
*/    return true;
  }

  public boolean doPrevious() {
    return true;
  }

  public boolean doFinish() {
    return true;
  }

  public boolean doCancel() {
    return true;
  }

  public boolean doGotoStep(int indexToShow) {
    return true;
  }

/*
  public void makeContentDecision(int indexToShow) {
  }
*/
}
