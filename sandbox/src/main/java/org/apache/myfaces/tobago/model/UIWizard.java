package org.apache.myfaces.tobago.model;

import org.apache.myfaces.tobago.component.UIPanel;

public interface UIWizard {

    /**
     * 
     * @return the current UIPanel with the (dynamic) UIComponents as chlidren
     */
    UIPanel getCurrentComponent();

}
