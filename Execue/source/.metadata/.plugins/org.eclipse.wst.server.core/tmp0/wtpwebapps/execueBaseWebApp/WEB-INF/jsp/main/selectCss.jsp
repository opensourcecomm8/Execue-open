<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<%
String host = request.getHeader("host");
if (host != null &&  host.contains("mobiledemos.semantifi.com")) {
%>
   <link href="<c:out value="${basePath}"/>/css/main/iframe.css" rel="stylesheet" type="text/css" />
   <div id="showLoaderPopup" style="display:none;" ><div style="width:100%;">Processing  your Request </div><div style="width:100%;"><img src="images/main/Loader-main-page-3.gif" /></div>
</div>
   
<%} else { %>
   <link href="<c:out value="${basePath}"/>/css/main/noiframe.css" rel="stylesheet" type="text/css" />
   
<% } %>
