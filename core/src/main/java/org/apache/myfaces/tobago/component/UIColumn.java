package org.apache.myfaces.tobago.component;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ALIGN;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SORTABLE;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;


/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 18.04.2006
 * Time: 21:50:29
 * To change this template use File | Settings | File Templates.
 */
public class UIColumn extends javax.faces.component.UIColumn {
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Column";
  private Boolean sortable;
  private String align;


   public void restoreState(FacesContext context, Object state) {
     Object[] values = (Object[]) state;
     super.restoreState(context, values[0]);
     align = (String) values[1];
     sortable = (Boolean) values[2];
   }

   public Object saveState(FacesContext context) {
     Object[] values = new Object[3];
     values[0] = super.saveState(context);
     values[1] = align;
     values[2] = sortable;
     return values;
   }

   public boolean isSortable() {
     if (sortable != null) {
       return sortable;
     }
     ValueBinding vb = getValueBinding(ATTR_SORTABLE);
     if (vb != null) {
       return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
     } else {
       return false;
     }
   }

   public void setSortable(boolean sortable) {
     this.sortable = sortable;
   }

  public String getAlign() {
    if (align != null) {
      return align;
    }
    ValueBinding vb = getValueBinding(ATTR_ALIGN);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return align;
    }
  }

  public void setAlign(String align) {
    this.align = align;
  }



}
