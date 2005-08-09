<%@ page import="java.util.Map,
                 java.util.Iterator"%><html>

<body bgcolor="white">
<h1> Request Information </h1>
<font size="4">
JSP Request Method: "<%= request.getMethod() %>"
<br>
Request URI: "<%= request.getRequestURI() %>"
<br>
Request Protocol: "<%= request.getProtocol() %>"
<br>
Servlet path: "<%= request.getServletPath() %>"
<br>
Path info: "<% out.print(request.getPathInfo()); %>"
<br>
Query string: "<% out.print(request.getQueryString()); %>"
<br>
Content length: "<%= request.getContentLength() %>"
<br>
Content type: "<%= request.getContentType() %>"
<br>
Server name: "<%= request.getServerName() %>"
<br>
Server port: "<%= request.getServerPort() %>"
<br>
Remote user: "<%= request.getRemoteUser() %>"
<br>
Remote address: "<%= request.getRemoteAddr() %>"
<br>
Remote host: "<%= request.getRemoteHost() %>"
<br>
Authorization scheme: "<%= request.getAuthType() %>"
<br>
Locale: "<%= request.getLocale() %>"
<br>
RequestParameterMap:
<% Map parameters = request.getParameterMap();
for (Iterator i = parameters.keySet().iterator(); i.hasNext(); ) {
  String key = (String)i.next();
  String[] values = (String[]) parameters.get(key);
%>
<br />&nbsp;&nbsp;&nbsp; key = "<%= key %>", values =
<%
  for (int j = 0; j < values.length; j++) {
%>"<%= values[j] %>";
<%

    for (int k=0; k < values[j].length(); k++) {
      char c = values[j].charAt(k);
      %><%= Integer.toHexString((int)c) %> <%
    }

  }
}
%>
<hr>
The browser you are using is "<% out.print(request.getHeader("User-Agent")); %>"
<hr>
</font>
</body>
</html>
