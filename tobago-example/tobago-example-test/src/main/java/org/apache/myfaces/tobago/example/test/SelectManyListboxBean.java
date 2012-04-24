package org.apache.myfaces.tobago.example.test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SelectManyListboxBean {

  private List<String> list;

  private Set<String> set;

  private Collection<String> collection;

  private String[] array;

  public List<String> getList() {
    return list;
  }

  public void setList(List<String> list) {
    this.list = list;
  }

  public String[] getArray() {
    return array;
  }

  public void setArray(String[] array) {
    this.array = array;
  }

  public Set<String> getSet() {
    return set;
  }

  public void setSet(Set<String> set) {
    this.set = set;
  }

  public Collection<String> getCollection() {
    return collection;
  }

  public void setCollection(Collection<String> collection) {
    this.collection = collection;
  }
}
