/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.util.RangeParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectReferenceRenderer extends RendererBase{

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(SelectReferenceRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component)
      throws IOException {
    String referenceId = (String)
        component.getAttributes().get(TobagoConstants.ATTR_FOR);
    UIComponent reference = component.findComponent(referenceId);

    reference.getAttributes().put(TobagoConstants.ATTR_RENDER_RANGE_EXTERN,
        component.getAttributes().get(TobagoConstants.ATTR_RENDER_RANGE));

    HtmlRendererUtil.encodeHtml(facesContext, reference);

    reference.getAttributes().remove(TobagoConstants.ATTR_RENDER_RANGE_EXTERN);
  }



  public static List getItemsToRender(UISelectOne component) {
    return getItems(component);
  }
  public static List getItemsToRender(UISelectMany component) {
    return getItems(component);
  }
  
  private static List getItems(UIInput component) {

    List selectItems = ComponentUtil.getSelectItems(component);

    String renderRange = (String)
        component.getAttributes().get(TobagoConstants.ATTR_RENDER_RANGE_EXTERN);
    if (renderRange == null) {
      renderRange = (String)
          component.getAttributes().get(TobagoConstants.ATTR_RENDER_RANGE);
    }
    if (renderRange == null) {
      return selectItems;
    }

    int[] indices = RangeParser.getIndices(renderRange);
    List items = new ArrayList(indices.length);

    if (selectItems.size() != 0) {
      for (int i = 0; i < indices.length; i++) {
        items.add(selectItems.get(indices[i]));
      }
    } else {
      LOG.warn("No items found! rendering dummys instead!");
      for (int i = 0; i < indices.length; i++) {
        items.add(new SelectItem(Integer.toString(i), "Item " + i, ""));
      }
    }
    return items;
  }

// ///////////////////////////////////////////// bean getter + setter

}

