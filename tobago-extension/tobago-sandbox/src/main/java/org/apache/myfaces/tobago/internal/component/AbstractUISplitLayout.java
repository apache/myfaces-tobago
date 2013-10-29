package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

public abstract class AbstractUISplitLayout extends AbstractUIGridLayout {

  public static final String VERTICAL = Orientation.VERTICAL.name();
  public static final String HORIZONTAL = Orientation.HORIZONTAL.name();

  public String submittedLayout;

//  @Override
//  public LayoutContainer getLayoutContainer() {
//    return new SplitLayoutContainer(this);
//  }        

  public void updateLayout(int position) {
    LayoutContainer container = (LayoutContainer) getParent();
    int oldPosition;

    int currentMeasure1;
    int currentMeasure2;
    if (HORIZONTAL.equals(getOrientation())) {
      oldPosition = container.getComponents().get(1).getLeft().getPixel() - 5;
      currentMeasure1 = container.getComponents().get(0).getCurrentWidth().getPixel();
      currentMeasure2 = container.getComponents().get(1).getCurrentWidth().getPixel();
    } else {
      oldPosition = container.getComponents().get(1).getTop().getPixel() - 5;
      currentMeasure1 = container.getComponents().get(0).getCurrentHeight().getPixel();
      currentMeasure2 = container.getComponents().get(1).getCurrentHeight().getPixel();
    }

    int offset = position - oldPosition;
    int newMeasure1 = currentMeasure1 + offset;
    int newMeasure2 = currentMeasure2 - offset;

    int ggt = gcd(newMeasure1, newMeasure2);
    submittedLayout = new StringBuilder()
        .append(Integer.toString(newMeasure1 / ggt)).append("*;")
        .append(Integer.toString(newMeasure2 / ggt)).append("*")
        .toString();
  }

  // TODO: MathUtils
  public static int gcd(int a, int b) {
    if (a < 0) {
      a = -a;
    }
    if (b < 0) {
      b = -b;
    }
    int t;
    while (b != 0) {
      t = a % b;
      a = b;
      b = t;
    }
    return a;
  }

  @Override
  public void processUpdates(FacesContext facesContext) {
    updateModel(facesContext);
    super.processUpdates(facesContext);
  }

  private void updateModel(FacesContext facesContext) {
    if (submittedLayout != null) {
      final ValueExpression expression = getValueExpression("layout");
      if (expression != null) {
        final ELContext elContext = facesContext.getELContext();
        expression.setValue(elContext, submittedLayout);
        submittedLayout = null;
      }
    }
  }

  public Measure getSpacing(Orientation orientation) {
    return orientation == Orientation.HORIZONTAL ? getColumnSpacing() : getRowSpacing();
  }

@Override
  public void setColumns(String columns) {
  }

  @Override
  public String getColumns() {
    return VERTICAL.equals(getOrientation()) ? "1*" : getLayout2();
  }

//  private String getLayout2() {
//    return getLayout().replace(";", ";5px;");
//  }

  @Override
  public void setRows(String rows) {
  }

  @Override
  public String getRows() {
    return HORIZONTAL.equals(getOrientation()) ? "1*" : getLayout2();
  }

  private String getLayout2() {
    return submittedLayout != null ? submittedLayout : getLayout();
  }

  @Override
  public boolean isRowOverflow() {
    return false;
  }

  @Override
  public boolean isColumnOverflow() {
    return false;
  }

  public abstract String getLayout();

  public abstract String getOrientation();

  @Deprecated
  public abstract Measure getCellspacing();

  public abstract Measure getRowSpacing();

  public abstract Measure getColumnSpacing();

  public abstract Measure getMarginLeft();

  public abstract Measure getMarginTop();

  public abstract Measure getMarginRight();

  public abstract Measure getMarginBottom();

  public abstract boolean isRigid();
}
