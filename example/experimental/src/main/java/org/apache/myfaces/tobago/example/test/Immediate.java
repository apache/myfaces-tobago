package org.apache.myfaces.tobago.example.test;

import javax.faces.model.SelectItem;

public class Immediate {

  private String in;
  private int selectOne;
  private Integer[] selectMany;

  private SelectItem[] selectItems = new SelectItem[] {
        new SelectItem(0, "Zero"),
        new SelectItem(1, "One"),
        new SelectItem(2, "Two"),
        new SelectItem(3, "Tree"),
        new SelectItem(4, "Four"),
    };

  public String getIn() {
    return in;
  }

  public void setIn(String in) {
    this.in = in;
  }

  public int getSelectOne() {
    return selectOne;
  }

  public void setSelectOne(int selectOne) {
    this.selectOne = selectOne;
  }

  public SelectItem[] getSelectItems() {
    return selectItems;
  }

  public void setSelectMany(Integer[] selectMany) {
    this.selectMany = selectMany;
  }

  public Integer[] getSelectMany() {
    return selectMany;
  }
}
