<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>   
<%@ taglib prefix="s" uri="/struts-tags" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- saved from url=(0014)about:internet -->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%--
response.setHeader("Pragma", "No-cache");
response.setDateHeader("Expires", 0);
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Cache-Control","no-store"); 
response.addHeader("Cache-Control", "post-check=0, pre-check=0");
response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
--%>
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<meta name="robots" content="index,follow" />
<TITLE><tiles:getAsString name="title"/></TITLE>
<tiles:insertAttribute name="head" />
</head>
<body bgcolor="#CCCCCC" >
<div id="container" style="background-color:#FFF;">
<tiles:insertAttribute name="headerLinks" />
            
 <tiles:insertAttribute name="body" />

 <tiles:insertAttribute name="footer" />
</div>
</body>
</html>
