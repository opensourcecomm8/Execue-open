<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" cellspacing="0" cellpadding="2">
	<s:iterator value="sflterms" status="inst" id="sflTerms">
		<tr class="joinRowEven" id="tableRow3">
			<td width="90%" colspan="2" class="fieldNames">
			<div id="showSFLTermToken<s:property value="businessTerm" />Link">
			<a href="#"
				onclick="showSFLTermTokens('<s:property value="businessTerm" />');"
				class="links"><s:property value="businessTerm" /></a></div>
			<div id="loadingShowSFLTermTokens<s:property value="businessTerm" />Link"
				style="display: none;"><img src="../images/admin/loadingBlue.gif"
				width="20" height="20" /></div>
			</td>
		</tr>
	</s:iterator>
</table>
