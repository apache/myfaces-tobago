<%@ page import="com.atanion.tobago.demo.jsp.JspFormatter,
                 java.io.InputStreamReader,
                 java.io.PrintWriter"
%><%@ page errorPage="/errorPage.jsp"
%><%
  String jsp = request.getParameter("jsp");

  if (jsp == null || jsp.length() == 0) {
    throw new RuntimeException("There is no 'jsp' parameter in the request!");
  }

  JspFormatter.writeJsp(
      new InputStreamReader(
          pageContext.getServletContext().getResourceAsStream(jsp)),
      new PrintWriter(out));
%>
