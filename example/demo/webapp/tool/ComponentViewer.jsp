<%@ page import="com.atanion.tobago.config.ThemeConfig,
                 javax.swing.BoundedRangeModel,
                 javax.swing.DefaultBoundedRangeModel,
                 javax.faces.context.FacesContext,
                 javax.faces.component.UIComponentBase,
                 javax.faces.component.UIComponent,
                 javax.faces.component.UIOutput"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%!
  public class ImageName {
    String[] image = new String[30];
    public String[] getImage() {return image;}
  }
%>

<f:view>
  <t:page label="ComponentTest" focusId="">




      <%
      String divOverflow = "";
      divOverflow = "overflow: auto; ";
      int borderHeight = 50;
       BoundedRangeModel progress = new DefaultBoundedRangeModel( 66, 0, 0, 100);
      pageContext.setAttribute("progress", progress, PageContext.REQUEST_SCOPE);
       UIComponent component = new UIOutput();
      ImageName imageName = new ImageName();
      pageContext.setAttribute("imageName", imageName, PageContext.REQUEST_SCOPE);
      int index = -1;
     %>


     <table>
       <tr> <td cellspan="2">Actual Theme : <t:out value="#{clientConfigController.localizedTheme}" /></td>
       </tr>
       <tr>
         <td><t:selectOneChoice value="#{clientConfigController.theme}"
                                  label="Theme">
               <f:selectItems value="#{clientConfigController.themeItems}" />
             </t:selectOneChoice>
         </td>
         <td><t:button action="#{clientConfigController.submit}">Wechseln</t:button>
         </td>
       </tr>
     </table>
     <table><tr><td>
        <c:forEach var="tag" items="Verbatim,Label,TextBox,TextBox:Label,Text,Checkbox,Checkbox:Label,SingleSelect,SingleSelect:Label,MultiSelect,MultiSelect:Label,Link,Button,Date,Date:Label,File,Progress" >
<%--        <c:forEach var="tag" items="" >--%>
         <%
           index++;
           String tag = (String)pageContext.getAttribute("tag");
           int idx = tag.indexOf(":L");
           String rendererType;
           if (idx == -1) {
             rendererType = tag;
           }
           else {
             rendererType = tag.substring(0, idx);
           }
           component.setRendererType(rendererType);
           int fixedHeight = ThemeConfig.getValue(FacesContext.getCurrentInstance(), component, "fixedHeight");
           int usedHeight = fixedHeight + borderHeight;
           imageName.image[index] = "image/5x" + fixedHeight + "-green.gif";
           String vb = "#{imageName.image[" + index +"]}";
         %>
        <div style="<%= "background: red;" + divOverflow + " border: 0px; margin:0px; padding:0px; width: 310px; height: " + usedHeight + "px;" %>">
       <table border="0" cellpadding="0" cellspacing="0" summary=""
              style="<%= "background: white; border: 0px; margin:0px; padding:0px; width: 310px; height: " + usedHeight + "px;" %>">
         <tr><td style="background: yellow; height: 20px;" colspan="3"><c:out value="${tag}"/> <%= "(fixedHeight=" + fixedHeight + "px)" %></td
        ></tr>
         <tr><td style="background: red; width: 1px; height: 1px;"
              ><t:image value="image/5x5-yellow.gif" /></td
          ><td style="background: red; width: 1px; height: 1px;" >
               <t:image value="image/300x5-yellow.gif" /></td
          ><td style="background: red; width: 1px; height: 1px;" >
               <t:image value="image/5x5-yellow.gif" /></td
        ></tr>
         <tr><td  style="background: red;" >
             <t:image value="<%= vb %>" /></td
          ><td
            ><t:panel width="300px"
              ><f:facet name="layout"><t:gridLayout /></f:facet
            ><c:choose
            ><c:when test="${tag == 'Verbatim'}">verbatim text</c:when
            ><c:when test="${tag == 'Label'}"
              ><t:label value="label"
            /></c:when
            ><c:when test="${tag == 'TextBox'}"
              ><t:in value="Content"
            /></c:when
            ><c:when test="${tag == 'TextBox:Label'}"
              ><t:in value="Content" label="Label"
            /></c:when
            ><c:when test="${tag == 'Text'}"
              ><t:out value="just text"
            /></c:when
            ><c:when test="${tag == 'Checkbox'}"
              ><t:selectBooleanCheckbox value="#{demo.bool[0]}"
            /></c:when
            ><c:when test="${tag == 'Checkbox:Label'}"
              ><t:selectBooleanCheckbox value="#{demo.bool[1]}"
                  label="Label"
            /></c:when
            ><c:when test="${tag == 'SingleSelect'}"
              ><t:selectOneChoice value="#{demo.salutation[0]}" >
                 <f:selectItems value="#{demo.salutationItems}" />
               </t:selectOneChoice
            ></c:when
            ><c:when test="${tag == 'MultiSelect'}"
              ><t:selectManyListbox value="#{demo.phoneProtocols[0]}">
                 <f:selectItems value="#{demo.phoneProtocolItems}" />
               </t:selectManyListbox
            ></c:when
            ><c:when test="${tag == 'MultiSelect:Label'}"
              ><t:selectManyListbox value="#{demo.phoneProtocols[0]}" label="Label">
                 <f:selectItems value="#{demo.phoneProtocolItems}" />
               </t:selectManyListbox
            ></c:when
            ><c:when test="${tag == 'SingleSelect:Label'}"
              ><t:selectOneChoice value="#{demo.salutation[1]}" label="Label">
                 <f:selectItems value="#{demo.salutationItems}" />
               </t:selectOneChoice
            ></c:when
            ><c:when test="${tag == 'Link'}"
              ><t:link action="http://www.atanion.com" type="navigate"
                            label="Dies ist ein link" />
            ></c:when
            ><c:when test="${tag == 'Button'}"
              ><t:button type="reset" label="Press me" />
            ></c:when
            ><c:when test="${tag == 'Date'}"
              ><t:date value="#{demo.date}">
               <f:convertDateTime pattern="dd.MM.yyyy" />
             </t:date
            ></c:when
            ><c:when test="${tag == 'Date:Label'}"
              ><t:date value="#{demo.date}" label="Label" >
               <f:convertDateTime pattern="dd.MM.yyyy" />
             </t:date
            ></c:when
            ><c:when test="${tag == 'File'}"
              ><t:file value="#{demo.fileItem}" id="file2"
           /></c:when
            ><c:when test="${tag == 'Progress'}"
              ><t:progress value="#{progress}"
           /></c:when
            ><c:otherwise
              ><t:in value="Unknown"
            /></c:otherwise></c:choose></t:panel
          ></td
          ><td style="background: red;" ><t:image value="<%= vb %>" /></td
        ></tr>
         <tr><td style="background: red; width: 1px; height: 1px;"
               ><t:image value="image/5x5-yellow.gif" /></td
          ><td style="background: red; width: 1px; height: 1px;"
            ><t:image value="image/300x5-yellow.gif" /></td
          ><td style="background: red; width: 1px; height: 1px;"
            ><t:image value="image/5x5-yellow.gif" /></td>
         </tr>
         <tr><td style="background: yellow; height: 20px;" colspan="3"> </td>
         </tr>
       </table>
       </div>
       <br />
<!-- #################################################################################################### -->
       </c:forEach >
     </td
><!-- #################################################################################################### -->
<!-- #################################################################################################### --><td>
          <c:forEach var="tag" items="textarea,textarea_label,multiselect,multiselect_label,editor,box,box_textarea,box_textbox,box_file_textarea,box_toolbar,box_toolbar_facet" >
<%--          <c:forEach var="tag" items="box,box_textarea,box_textbox" >--%>
        <div style="<%= "background: red;" + divOverflow + " border: 0px; margin:0px; padding:0px; width: 310px; height: 150px;" %>">
       <table border="0" cellpadding="0" cellspacing="0" summary=""
              style="<%= "background: white; border: 0px; margin:0px; padding:0px; width: 310px; height: 150px;" %>">
         <tr><td style="background: yellow; height: 20px;" colspan="3"><c:out value="${tag}"/></td
        ></tr>
         <tr><td style="background: red; width: 1px; height: 1px;"
              ><t:image value="image/5x5-yellow.gif" /></td
          ><td style="background: red; width: 1px; height: 1px;" >
               <t:image value="image/300x5-yellow.gif" /></td
          ><td style="background: red; width: 1px; height: 1px;" >
               <t:image value="image/5x5-yellow.gif" /></td
        ></tr>
         <tr><td  style="background: red;" >
             <t:image value="image/5x100-green.gif" /></td
          ><td
            ><t:panel width="300px" height="100px"
              ><f:facet name="layout"><t:gridLayout /></f:facet
            ><c:choose
            ><c:when test="${tag == 'textarea'}"
              ><t:textarea value="Content"
            /></c:when
            ><c:when test="${tag == 'textarea_label'}"
              ><t:textarea value="Content" label="Label"
            /></c:when
            ><c:when test="${tag == 'multiselect'}"
              ><t:selectManyListbox value="#{demo.phoneProtocols[0]}" >
                 <f:selectItems value="#{demo.phoneProtocolItems}" />
               </t:selectManyListbox
            ></c:when
            ><c:when test="${tag == 'multiselect_label'}"
              ><t:selectManyListbox value="#{demo.phoneProtocols[0]}" label="Label">
                 <f:selectItems value="#{demo.phoneProtocolItems}" />
               </t:selectManyListbox
            ></c:when
            ><c:when test="${tag == 'editor'}"
              ><t:richTextEditor value="#{demo.text[10]}"
            /></c:when
            ><c:when test="${tag == 'box'}"
              ><t:box label="empty box">
               </t:box
            ></c:when
            ><c:when test="${tag == 'box_textarea'}"
              ><t:box label="box with textarea">
                 <f:facet name="layout"><t:gridLayout /></f:facet>
                 <t:textarea value="Content" />
               </t:box
            ></c:when
            ><c:when test="${tag == 'box_textbox'}"
              ><t:box label="box with textboxes">
                 <f:facet name="layout"><t:gridLayout /></f:facet>
                 <t:in value="Content 1" />
                 <t:in value="Content 2" />
               </t:box
            ></c:when
            ><c:when test="${tag == 'box_file_textarea'}"
              ><t:box label="box with fileinput and textarea">
                 <f:facet name="layout">
                   <t:gridLayout rows="fixed;*" />
                 </f:facet>
                 <t:file value="#{demo.fileItem}" id="file1" />
                 <t:textarea value="Content" />
               </t:box
            ></c:when
            ><c:when test="${tag == 'box_toolbar'}"
              ><t:box label="box with toolbar and textarea" >
                 <f:facet name="layout">
                   <t:gridLayout rows="fixed;*" />
                 </f:facet>
                 <t:toolBar>
                   <t:toolBarCommand type="script" action="alert('action 0')"
                        image="image/tobago-richtext-preview.gif" />
                   <t:toolBarCommand type="script" action="alert('action 1')"
                        label="alert 1" image="image/tobago-richtext-edit.gif" />
                 </t:toolBar>
                 <t:textarea value="Content" />
               </t:box
            ></c:when
            ><c:when test="${tag == 'box_toolbar_facet'}"
              ><t:box label="box with toolbar" >
                 <f:facet name="layout">
                   <t:gridLayout rows="fixed;*" />
                 </f:facet>
                 <f:facet name="toolBar">
                   <t:toolBar>
                     <t:toolBarCommand type="script" action="alert('action 0')" image="image/tobago-richtext-preview.gif" />
                     <t:toolBarCommand type="script" action="alert('action 1')" image="image/tobago-richtext-edit.gif" label="doAlert" />
                   </t:toolBar>
                 </f:facet>
                 <t:textarea value="Content" />
               </t:box
            ></c:when
            ><c:otherwise
              ><t:in value="Unknown"
            /></c:otherwise></c:choose></t:panel
          ></td
          ><td style="background: red;" ><t:image value="image/5x100-green.gif" /></td
        ></tr>
         <tr><td style="background: red; width: 1px; height: 1px;"
               ><t:image value="image/5x5-yellow.gif" /></td
          ><td style="background: red; width: 1px; height: 1px;"
            ><t:image value="image/300x5-yellow.gif" /></td
          ><td style="background: red; width: 1px; height: 1px;"
            ><t:image value="image/5x5-yellow.gif" /></td>
         </tr>
         <tr><td style="background: yellow; height: 20px;" colspan="3"> </td>
         </tr>
       </table>
       </div>
       <br />
     <!-- #################################################################################################### -->
     </c:forEach >
      </td></tr></table>







  </t:page>
</f:view>
