package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.layout.Measure;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

public class UIEquationGridLayout
    extends AbstractUIEquationGridLayout  {
  
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.GridLayout";

  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.GridLayout";

  private String border;
  private Measure cellspacing;
  private Measure columnSpacing;
  private Measure rowSpacing;
  private String margin;
  private String marginBottom;
  private String marginLeft;
  private String marginRight;
  private String marginTop;
  private String columns;
  private String rows;
  private Markup markup;

  public String getFamily() {
    return COMPONENT_FAMILY;
  }


  public String getBorder() {
    if (border != null) {
      return border;
    }
    javax.el.ValueExpression ve = getValueExpression("border");
    if (ve != null) {
      try {
        return (String) ve.getValue(getFacesContext().getELContext());
      } catch (javax.el.ELException e) {
  		  throw new javax.faces.FacesException(e);
  	  }
    }
    return null;
  }

  public void setBorder(String border) {
    this.border = border;
  }

  public Measure getCellspacing() {
    if (cellspacing != null) {
      return cellspacing;
    }
    javax.el.ValueExpression ve = getValueExpression("cellspacing");
    if (ve != null) {
      try {
        Object object = ve.getValue(getFacesContext().getELContext());
        if (object instanceof Measure) {
          Measure cellspacing = (Measure) object;
          return cellspacing;
        } else if (object instanceof Number) {
          return Measure.valueOf(((Number)object).intValue());
        } else if (object instanceof String) {
          return Measure.parse((String)object);
        } else if (object != null) {
          return Measure.parse(object.toString());
        }
      } catch (javax.el.ELException e) {
  		  throw new javax.faces.FacesException(e);
  	  }
    }
    return null;
  }

  public void setCellspacing(Measure cellspacing) {
    this.cellspacing = cellspacing;
  }

  public Measure getColumnSpacing() {
    if (columnSpacing != null) {
      return columnSpacing;
    }
    javax.el.ValueExpression ve = getValueExpression("columnSpacing");
    if (ve != null) {
      try {
        Object object = ve.getValue(getFacesContext().getELContext());
        if (object instanceof Measure) {
          Measure columnSpacing = (Measure) object;
          return columnSpacing;
        } else if (object instanceof Number) {
          return Measure.valueOf(((Number)object).intValue());
        } else if (object instanceof String) {
          return Measure.parse((String)object);
        } else if (object != null) {
          return Measure.parse(object.toString());
        }
      } catch (javax.el.ELException e) {
  		  throw new javax.faces.FacesException(e);
  	  }
    }
    return getCellspacing() != null 
        ? getCellspacing() 
        : ResourceManagerUtil.getThemeMeasure(getFacesContext(), this, "columnSpacing");
  }

  public void setColumnSpacing(Measure columnSpacing) {
    this.columnSpacing = columnSpacing;
  }

  public Measure getRowSpacing() {
    if (rowSpacing != null) {
      return rowSpacing;
    }
    javax.el.ValueExpression ve = getValueExpression("rowSpacing");
    if (ve != null) {
      try {
        Object object = ve.getValue(getFacesContext().getELContext());
        if (object instanceof Measure) {
          Measure rowSpacing = (Measure) object;
          return rowSpacing;
        } else if (object instanceof Number) {
          return Measure.valueOf(((Number)object).intValue());
        } else if (object instanceof String) {
          return Measure.parse((String)object);
        } else if (object != null) {
          return Measure.parse(object.toString());
        }
      } catch (javax.el.ELException e) {
  		  throw new javax.faces.FacesException(e);
  	  }
    }
    return getCellspacing() != null 
        ? getCellspacing() 
        : ResourceManagerUtil.getThemeMeasure(getFacesContext(), this, "rowSpacing");
  }

  public void setRowSpacing(Measure rowSpacing) {
    this.rowSpacing = rowSpacing;
  }

  public String getMargin() {
    if (margin != null) {
      return margin;
    }
    javax.el.ValueExpression ve = getValueExpression("margin");
    if (ve != null) {
      try {
        return (String) ve.getValue(getFacesContext().getELContext());
      } catch (javax.el.ELException e) {
  		  throw new javax.faces.FacesException(e);
  	  }
    }
    return null;
  }

  public void setMargin(String margin) {
    this.margin = margin;
  }

  public String getMarginBottom() {
    if (marginBottom != null) {
      return marginBottom;
    }
    javax.el.ValueExpression ve = getValueExpression("marginBottom");
    if (ve != null) {
      try {
        String marginBottom = (String) ve.getValue(getFacesContext().getELContext());
        if (marginBottom != null) {
          return marginBottom;
        }
      } catch (javax.el.ELException e) {
  		  throw new javax.faces.FacesException(e);
  	  }
    }
    return getMargin();
  }

  public void setMarginBottom(String marginBottom) {
    this.marginBottom = marginBottom;
  }

  public String getMarginLeft() {
    if (marginLeft != null) {
      return marginLeft;
    }
    javax.el.ValueExpression ve = getValueExpression("marginLeft");
    if (ve != null) {
      try {
        String marginLeft = (String) ve.getValue(getFacesContext().getELContext());
        if (marginLeft != null) {
          return marginLeft;
        }
      } catch (javax.el.ELException e) {
  		  throw new javax.faces.FacesException(e);
  	  }
    }
    return getMargin();
  }

  public void setMarginLeft(String marginLeft) {
    this.marginLeft = marginLeft;
  }

  public String getMarginRight() {
    if (marginRight != null) {
      return marginRight;
    }
    javax.el.ValueExpression ve = getValueExpression("marginRight");
    if (ve != null) {
      try {
        String marginRight = (String) ve.getValue(getFacesContext().getELContext());
        if (marginRight != null) {
          return marginRight;
        }
      } catch (javax.el.ELException e) {
  		  throw new javax.faces.FacesException(e);
  	  }
    }
    return getMargin();
  }

  public void setMarginRight(String marginRight) {
    this.marginRight = marginRight;
  }

  public String getMarginTop() {
    if (marginTop != null) {
      return marginTop;
    }
    javax.el.ValueExpression ve = getValueExpression("marginTop");
    if (ve != null) {
      try {
        String marginTop = (String) ve.getValue(getFacesContext().getELContext());
        if (marginTop != null) {
          return marginTop;
        }
      } catch (javax.el.ELException e) {
  		  throw new javax.faces.FacesException(e);
  	  }
    }
    return getMargin();
  }

  public void setMarginTop(String marginTop) {
    this.marginTop = marginTop;
  }

  public String getColumns() {
    if (columns != null) {
      return columns;
    }
    javax.el.ValueExpression ve = getValueExpression("columns");
    if (ve != null) {
      try {
        String columns = (String) ve.getValue(getFacesContext().getELContext());
        if (columns != null) {
          return columns;
        }
      } catch (javax.el.ELException e) {
  		  throw new javax.faces.FacesException(e);
  	  }
    }
    return "1*";
  }

  public void setColumns(String columns) {
    this.columns = columns;
  }

  public String getRows() {
    if (rows != null) {
      return rows;
    }
    javax.el.ValueExpression ve = getValueExpression("rows");
    if (ve != null) {
      try {
        String rows = (String) ve.getValue(getFacesContext().getELContext());
        if (rows != null) {
          return rows;
        }
      } catch (javax.el.ELException e) {
  		  throw new javax.faces.FacesException(e);
  	  }
    }
    return "1*";
  }

  public void setRows(String rows) {
    this.rows = rows;
  }

  public Markup getMarkup() {
    if (markup != null) {
      return markup;
    }
    ValueExpression ve = getValueExpression("markup");
    if (ve != null) {
      try {
        Object strArray = ve.getValue(getFacesContext().getELContext());
        return Markup.valueOf(strArray);
     } catch (ELException e) {
  		  throw new FacesException(e);
  	  }
    }
    return Markup.NULL;
  }

  public void setMarkup(Markup markup) {
    this.markup = markup;
  }

  public void restoreState(FacesContext context, Object componentState) {
    Object[] values = (Object[]) componentState;
    super.restoreState(context, values[0]);
    border = (String) values[1];
    cellspacing = (Measure) values[2];
    columnSpacing = (Measure) values[3];
    rowSpacing = (Measure) values[4];
    margin = (String) values[5];
    marginBottom = (String) values[6];
    marginLeft = (String) values[7];
    marginRight = (String) values[8];
    marginTop = (String) values[9];
    columns = (String) values[10];
    rows = (String) values[11];
    markup = (Markup) values[12];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[13];
    values[0] = super.saveState(context);
    values[1] = border;
    values[2] = cellspacing;
    values[3] = columnSpacing;
    values[4] = rowSpacing;
    values[5] = margin;
    values[6] = marginBottom;
    values[7] = marginLeft;
    values[8] = marginRight;
    values[9] = marginTop;
    values[10] = columns;
    values[11] = rows;
    values[12] = markup;
    return values;
  }
}
