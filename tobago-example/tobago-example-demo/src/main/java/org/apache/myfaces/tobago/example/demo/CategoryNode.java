package org.apache.myfaces.tobago.example.demo;

import java.util.List;

public class CategoryNode {

  private String id;
  private String name;
  private List<CategoryNode> children;

  public CategoryNode() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<CategoryNode> getChildren() {
    return children;
  }

  public void setChildren(List<CategoryNode> children) {
    this.children = children;
  }

  @Override
  public String toString() {
    return "CategoryNode{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", children=" + children +
        '}';
  }
}
