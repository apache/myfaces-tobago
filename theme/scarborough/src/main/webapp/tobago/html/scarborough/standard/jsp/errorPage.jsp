<%--
 * Copyright 2002-2005 atanion GmbH.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
--%>
<%@ page import="java.io.IOException,
                 java.io.ByteArrayOutputStream,
                 java.io.PrintStream"%>
<%@ page isErrorPage="true" %>


<%!
  public static void printStackTrace(Throwable exception, JspWriter out)
      throws IOException {
    ByteArrayOutputStream ostr = new ByteArrayOutputStream();
    if (exception != null) {
      exception.printStackTrace(new PrintStream(ostr));
      out.print(ostr);
    } else {
      out.print("&lt;null&gt;");
    }
  }

  public static void handleException(Throwable exception, JspWriter out)
      throws IOException {
    if (exception == null) {
      return;
    }
    out.println("<p> Exception: " + exception
        + "<p> Stack trace:");
    out.println("<pre>");
    printStackTrace(exception, out);
    out.println("</pre>");
    Throwable nested = null;
    if (exception instanceof javax.servlet.jsp.JspException) {
      javax.servlet.jsp.JspException jspException
          = (javax.servlet.jsp.JspException) exception;
      nested = jspException.getRootCause();
    } else if (exception instanceof ServletException) {
      ServletException servletException = (ServletException) exception;
      nested = servletException.getRootCause();
    }
    if (nested != null) {
      out.println("Nested exception:");
      handleException(nested, out);
    }
  }
%>
<html>
<head><title>Error Page</title></head>

<body>
<p><font face="Helvetica">

<h2><font color="#FF0000">Error Page</font></h2>
<hr />

<%
  if (exception == null) {
     exception = new Exception(
         "<b>Exception unavailable! Tracing Stack...</b>");
  }

  handleException(exception, out);
%>

<p>
<hr />

</body>
</html>
