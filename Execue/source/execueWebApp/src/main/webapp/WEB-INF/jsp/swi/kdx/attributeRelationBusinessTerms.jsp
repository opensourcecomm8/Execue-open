<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="s" uri="/struts-tags"%>
 <%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<s:iterator value="timeFrameTypeconcepts" id="concept" status="count">
				<tr id="conceptRow1">
					<td width="1%" class="dotBullet">&nbsp;</td>
					<td width="99%" class="fieldNames">
					<div id="showConcept1Link"><a
						href="javascript:getConcept('<s:property value="%{#concept.bedId}"/>');"
						class="links" id='<s:property value="%{#concept.bedId}"/>'
						attributeName='<s:property value="%{#concept.name}" />'><s:property
						value="%{#concept.displayName}" /></a></div>
					<div id="loadingShowConcept1Link" style="display: none;"><img
						src="../images/loadingBlue.gif" width="25" height="25" /></div>
					</td>
				</tr>
			</s:iterator>
		</table>

		</td>
	</tr>
</table>
<!-- 
 <div id="paginationDiv2" style="margin-top: 10px; margin-bottom: 5px;"><pg:page targetURL="getAttributeRelationsBusinessTerms.action" targetPane="dynamicPaneAttributeRelationBTerms"/></div>
		-->

</body>
</html>