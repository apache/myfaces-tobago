package org.apache.myfaces.tobago.application;

import java.util.HashMap;
import java.util.Map;

public class TobagoFacesMessage extends LabelValueExpressionFacesMessage {

    private final Map<Object, Object> dataAttributes = new HashMap<>();

    public Map<Object, Object> getDataAttributes() {
        return dataAttributes;
    }

    public void setDataAttribute(final Object key, final Object value) {
        dataAttributes.put(key, value);
    }

}
