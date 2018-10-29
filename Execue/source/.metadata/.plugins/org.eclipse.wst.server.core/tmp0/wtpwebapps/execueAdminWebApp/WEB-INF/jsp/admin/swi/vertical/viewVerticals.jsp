<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<div class="tableList"
	style="height: 265px; width: 190px; margin-right: 10px; margin-bottom: 5px; white-space: nowrap; padding-left: 0px; padding-top: 3px; overflow-y: auto;">
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	id="searchList">
	<s:iterator value="verticals" status="inst">
		<tr id="verticalRow1">
			<td width="1%" class="dotBullet">&nbsp;</td>
			<td width="99%" class="fieldNames">
			<div id="showVertical<s:property value="id"/>Link"><a
				href='javascript:getVerticalDetails(<s:property value="id"/>);'
				class="links" id='<s:property value="id"/>' ><s:property
				value="name" /></a></div>
			<div id="loadingShowVertical<s:property value="id"/>Link"
				style="display: none;"><img src="../images/loadingBlue.gif"
				width="25" height="25" /></div>
			</td>
		</tr>
	</s:iterator>
</table>