<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="layout_jsp" >

  <t:box label="SheetLayout">
    <f:facet name="layout" >
      <t:gridLayout border="5" columns="50%;25%;*" />
    </f:facet>
    <t:out value="1. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. " />
    <t:out value="1. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat." />
    <t:out value="1. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi." />

    <t:cell spanX="2" spanY="2">
      <t:out value="2. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat." />
    </t:cell>
    <t:out value="2. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat." />
    <t:out value="2. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi." />

  </t:box>

  <t:box label="Layout 2">
    <f:facet name="layout" >
      <t:gridLayout border="2" id="outer" columns="*;16%;22%;22%;22%" />
    </f:facet>

    <t:out value="Lorem ipsum dolor sit amet," />
    <t:out value="consectetuer adipiscing elit, " />
    <t:out value="sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat." />
    <t:cell spanX="2">
      <t:out value="Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat," />
    </t:cell>

    <t:cell spanX="2" spanY="2">
      <t:out value="Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat." />
    </t:cell>
    <t:out value="vel illum dolore eu feugiat nulla facilisis at vero et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi." />
    <t:out value="Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum." />
    <t:out value="Lorem ipsum dolor sit amet," />

    <t:cell spanX="3" spanY="2">
      <t:panel>
        <f:facet name="layout" >
          <t:gridLayout columns="1*;1*;1*;1*" border="2"
            id="inner" cellspacing="10" />
        </f:facet>
        <t:out value="Lorem ipsum dolor sit amet," />
        <t:out value="Lorem ipsum dolor sit amet," />
        <t:out value="Lorem ipsum dolor sit amet," />
        <t:out value="Lorem ipsum dolor sit amet," />

        <t:out value="Lorem ipsum dolor sit amet," />
        <t:cell spanX="2" spanY="2">
          <t:out value="Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat." />
        </t:cell>
        <t:out value="Lorem ipsum dolor sit amet," />

        <t:out value="Lorem ipsum dolor sit amet," />
        <t:out value="Lorem ipsum dolor sit amet," />

        <t:out value="Lorem ipsum dolor sit amet," />
        <t:out value="Lorem ipsum dolor sit amet," />
        <t:out value="Lorem ipsum dolor sit amet," />
        <t:out value="Lorem ipsum dolor sit amet," />

      </t:panel>
    </t:cell>

    <t:out value="Lorem ipsum dolor sit amet," />
    <t:out value="Lorem ipsum dolor sit amet," />

  </t:box>

</f:subview>
