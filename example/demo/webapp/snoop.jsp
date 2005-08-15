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
