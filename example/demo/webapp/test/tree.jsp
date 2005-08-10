<%@ page import="javax.swing.tree.DefaultMutableTreeNode,
                 org.apache.myfaces.tobago.model.TreeState"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%!
  public class TreeTest {
    private TreeState state;
    private DefaultMutableTreeNode root;
    public TreeTest(TreeState state, DefaultMutableTreeNode root) {
      this.state = state;
      this.root = root;
    }
    public void setState(TreeState state) {
      this.state = state;
    }
    public TreeState getState() {
      return state;
    }
    public void setRoot(DefaultMutableTreeNode root) {
      this.root = root;
    }
    public DefaultMutableTreeNode getRoot() {
      return root;
    }
  }
%>
<%
  TreeTest treeTest
      = (TreeTest) session.getAttribute("treeTest");
  if (treeTest == null) {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Planeten");
    root.add(new DefaultMutableTreeNode("Merkur"));
    root.add(new DefaultMutableTreeNode("Venus"));
    root.add(new DefaultMutableTreeNode("Erde"));
    root.add(new DefaultMutableTreeNode("Mars"));
    root.add(new DefaultMutableTreeNode("Jupiter"));
    root.add(new DefaultMutableTreeNode("Saturn"));
    root.add(new DefaultMutableTreeNode("Uranus"));
    root.add(new DefaultMutableTreeNode("Neptun"));
    root.add(new DefaultMutableTreeNode("Pluto"));
    root.add(new DefaultMutableTreeNode("Rupert"));
    TreeState state = new TreeState();
    treeTest = new TreeTest(state, root);
    session.setAttribute("treeTest", treeTest);
  }
%>
<f:view>
  <t:page label="testing: validate length" id="page">

    <t:tree value="#{treeTest.root}" state="#{treeTest.state}" id="tree">
      <f:facet name="treeNodeCommand">
        <t:link action="HALLO" />
      </f:facet>
    </t:tree>


  </t:page>
</f:view>
