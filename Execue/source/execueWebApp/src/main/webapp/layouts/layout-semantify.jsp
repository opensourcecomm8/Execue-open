<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

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
<title>
Semantifi Advanced Search
</title>
<tiles:insertAttribute name="head" />
</head>
<body bgcolor="#ffffff">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<!-- fwtable fwsrc="index.png" fwpage="Page 1" fwbase="index.jpg" fwstyle="Dreamweaver" fwdocid = "966166702" fwnested="1" -->
	<tr>
		<td width="980">
		<table width="98%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="16%" height="70"><img name="index_r1_c1"
					src="images/semantify.bmp" width="237" height="60" border="0"
					id="index_r1_c1" alt="" /></td>
				<td width="84%">
				<div class="qiHeaderDiv" style="margin-top:10px;"><tiles:insertAttribute
					name="headerLinks" /></div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<tiles:insertAttribute name="body" />
<div id=footer><tiles:insertAttribute name="footer" /></div>
</body>
</html>
