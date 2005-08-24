<%@ page import="java.util.List"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="employees" class="java.util.ArrayList" scope="request" ></jsp:useBean>
<%
  List list = (List)request.getAttribute("employees");
  list.add("Suppe");
  list.add("Huhn");
  list.add("Alfons");
%>

<f:view>
   <t:page>
       <c:forEach items="${employees}" var="emp" varStatus="status" >
           <t:out value="#{employees[${status.index}]}" id="out${status.index}"/>
        </c:forEach>
   </t:page>
</f:view>