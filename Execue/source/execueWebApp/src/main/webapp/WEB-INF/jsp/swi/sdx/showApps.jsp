<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<div class="tableList" style="height: 240px; width: 205px; margin: auto; margin-bottom: 5px; margin-left: 0px;padding-left:3px;padding-top:3px;">
<table width="100%" border="0" cellspacing="0" cellpadding="2"
	id="searchList2">
	<s:iterator value="applications">
		<tr>
			<td style="padding-left:5px;">
			<div id="showApplication<s:property value="applicationName" />Link">
			<a href="#"
				onclick="showApplication('<s:property value="applicationId" />');"
				class="links"><s:property value="applicationName" /></a></div>
			<div
				id="loadingShowApplication<s:property value="applicationName" />Link"
				style="display: none;"><img src="../images/loadingBlue.gif"
				width="25" height="25" /></div>
			</td>
		</tr>
	</s:iterator>
</table>
</div>

  <div id="paginationDiv2" style="margin-top:10px;margin-bottom:5px;" ><pg:page targetPane="dynamicPaneApplications" targetURL="getAllSubApplications.action"/></div>
