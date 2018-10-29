<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<%
String host = request.getHeader("host");
if (host != null &&  host.contains("fb.semantifi.com")) {
%>
   <link href="<c:out value="${basePath}"/>/css/iframe.css" rel="stylesheet" type="text/css" />
<%} else { %>
   <link href="<c:out value="${basePath}"/>/css/noiframe.css" rel="stylesheet" type="text/css" />
<% } %>