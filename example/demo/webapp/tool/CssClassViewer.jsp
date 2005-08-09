<%@ page import="com.atanion.util.http.HttpClient,
                 java.net.URL,
                 com.atanion.util.http.Response,
                 java.util.regex.Pattern,
                 java.util.regex.Matcher,
                 java.util.*,
                 com.atanion.tobago.context.ResourceManagerUtil,
                 javax.faces.context.FacesContext"%>
 <%-- Copyright (c) 2003 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: CssClassViewer.jsp 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>



<%!
  class Entry {
    String name;
    List values;
    Entry(String n, Value v) {
      name = n;
      values = new ArrayList();
      add(v);
    }
    String getName() {
      return name;
    }
    void add(Value value) {
      values.add(0,value);
    }
    List getValues() {
      return values;
    }
  }

  class Value {
    String file;
    int fileNr;
    String value;
    Value(int i, String f, String v) {
      file = f;
      fileNr = i;
      value = v;
    }
    String getFile() {
      return file;
    }
    int getFileNr() {
      return fileNr;
    }
    String getValue() {
      return value;
    }
  }

%>

<%
  Map classes = new HashMap();

  List styles = ResourceManagerUtil.getStyles(
            FacesContext.getCurrentInstance(), "style/style.css");
  List sheetStyles = ResourceManagerUtil.getStyles(
            FacesContext.getCurrentInstance(), "style/tobago-sheet.css");

  styles.addAll(sheetStyles);
  List menuStyles = ResourceManagerUtil.getStyles(
            FacesContext.getCurrentInstance(), "style/tobago-menu.css");

  styles.addAll(menuStyles);

  String[] styleStrings = (String[]) styles.toArray(new String [0]);

  String url = "http://" + pageContext.getRequest().getServerName();
  url += ":" + pageContext.getRequest().getServerPort();



  Pattern classPattern = Pattern.compile("([\\S|^\\{]+)\\s*?\\{(.*?)\\}", Pattern.DOTALL);
  Pattern stylePattern = Pattern.compile("(\\S*)\\s*:\\s*?(.*?);", Pattern.DOTALL);

  for (int i = 0 ; i < styleStrings.length ; i++) {
    if (styleStrings[i].length() > 0 ) {

      HttpClient httpClient = new HttpClient();
      Response httpResponse = httpClient.get(new URL(url + styleStrings[i]), null, false);
      byte[] data = httpResponse.getData();
      String cssData = new String(data).replaceAll("(?s)/\\*.*?\\*/", "");
      Matcher classMatcher = classPattern.matcher(cssData);
      while (classMatcher.find()) {
        String key = classMatcher.group(1);
        Map cssClass = (Map) classes.get(key);
        if (cssClass == null) {
          cssClass = new HashMap();
          classes.put(key, cssClass);
        }
        Matcher styleMatcher = stylePattern.matcher(classMatcher.group(2));
        while (styleMatcher.find()) {
          String name = styleMatcher.group(1);
          Value value = new Value(i, styleStrings[i], styleMatcher.group(2));
          Entry entry = (Entry) cssClass.get(name);
          if (entry == null) {
            entry = new Entry(name, value);
            cssClass.put(name, entry);
          }
          else {
            entry.add(value);
          }
        }
      }
    }
  }


  String[] classKeys = (String[]) classes.keySet().toArray(new String[0]);
  Arrays.sort(classKeys);
  Arrays.sort(classKeys, new Comparator() {
    public int compare(Object o1, Object o2) {
      String s1 = o1.toString();
      int dot = s1.indexOf('.');
      if (dot > -1) {
        s1 = s1.substring(dot);
      }

      String s2 = o2.toString();
      dot = s2.indexOf('.');
      if (dot > -1) {
        s2 = s2.substring(dot);
      }
      return s1.compareTo(s2);
    }
  });
  %>


<f:view>
  <t:page label="CssClass Viewer" id="page">


   <table>
     <tr> <td cellspan="2">Actual Theme : <t:out value="#{clientConfigController.theme}" /></td>
     </tr>
     <tr>
       <td>
         <t:selectOneChoice value="#{clientConfigController.theme}"
                              label="Theme">
           <f:selectItems value="#{clientConfigController.themeItems}" />
         </t:selectOneChoice>
       </td>
       <td><t:button action="#{clientConfigController.submit}" label="Wechseln" />
       </td>
     </tr>
   </table>

   <hr />
<%

  for (int i = 0 ; i < styleStrings.length ; i++) {
    if (styleStrings[i].length() > 0 ) {
      String href = url + styleStrings[i];

      %> [<%= i %>] <a href="<%= href %>" > <%= href %> </a> <br /> <%

    }
  }
  %> <hr />

<div style="overflow: auto; height: 500px; border: 2px inset grey; " >
<table cellspacing="0">
  <tr>
    <td><b>Class</b></td>
    <td><b>FileNr</b></td>
    <td><b>Name</b></td>
    <td><b>Value</b></td>
    <td><b>Values</b></td>
  </tr><%
  for (int i = 0; i < classKeys.length; i++) {
    String key = classKeys[i];
    %>
  <tr style="background: #FFFFFF">
    <td><%= key %></td>
    <td colspan="4"></td>
  </tr><%
    Map classMap = (Map) classes.get(key);
    String[] styleKeys = (String[]) classMap.keySet().toArray(new String[0]);
    Arrays.sort(styleKeys);
    for (int j = 0; j < styleKeys.length; j++) {
      Entry entry =  (Entry) classMap.get(styleKeys[j]);
      List values = entry.getValues();
      Value value = (Value) values.get(0);
      %>
  <tr>
    <td>&nbsp;</td>
    <td title="<%= value.getFile() %>"><%= value.getFileNr() %></td>
    <td><%= entry.getName() %></td>
    <td><span title="<%= value.getFile() %>"<%

      if (values.size() > 2 && ((Value)values.get(1)).getValue().equals(value.getValue())) {
         %> style="color: red;"<%
      }

%>> <%= value.getValue() %></span></td>
    <td style="background: #CCCCCC; white-space: nowrap" ><%
      for (int k=1; k< values.size(); k++) {
        value = (Value) values.get(k);
        %><span title="<%= value.getFile() %>"> <%= value.getValue() %> </span> &nbsp;|&nbsp; <%
      }

      %></td>
  </tr><%
    }

  }

  %>
</table>

</div>








  </t:page>
</f:view>
