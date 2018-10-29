<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<form name="sflTermForm" id="sflTermForm">
<table width="100%" border="0" cellspacing="0" cellpadding="2">
	<div id="errorMessage"><s:actionmessage /></div>	
	<tr>
		<td><b>SFL Term</b></td><td width="68%"><s:set name="sflTermVal" value="sflTerm"/><s:textfield name="sflTerm"
			cssClass="textBoxDisabled" id="sflTerm" readonly="true"  title="%{#sflTermVal}"/></td>
	</tr>
	<tr>
		<td><b>SFL Term Tokens</b></td>
		<td><b>Weight</b></td>
	</tr>
	<s:iterator value="sflTermsTokens" status="ct" id="sflTermsToken">
		<tr>							
			<td><input type="text" name="sflTermsTokens[<s:property value="#ct.index"/>].businessTermToken" class="textBoxDisabled"  id='sflTermsTokens<s:property value="#ct.index"/>businessTermToken' title="<s:property value="businessTermToken"/>" value="<s:property value="businessTermToken"/>" readonly="true" /></td>
			<td><input type="text" name="sflTermsTokens[<s:property value="#ct.index"/>].weight"  id="sflTermsTokens<s:property value="#ct.index"/>weight" value="<s:property value="weight"/>"/></td>
			<input type="hidden" name="sflTermsTokens[<s:property value="#ct.index"/>].id" name="sflTermsTokens[<s:property value="#ct.index"/>].id"	value="<s:property value="id"/>" />
			<input type="hidden" name="sflTermsTokens[<s:property value="#ct.index"/>].hits" name="sflTermsTokens[<s:property value="#ct.index"/>].hits"	value="<s:property value="hits"/>" />
			<input type="hidden" name="sflTermsTokens[<s:property value="#ct.index"/>].sflTerm.id" name="sflTermsTokens[<s:property value="#ct.index"/>].sflTerm.id"	value="<s:property value="sflTerm.id"/>" />
		</tr>
	</s:iterator>	
	<tr><td height="20"><span id="enableUpdateSFLTerm"> <label>
			<img name="imageField3" onclick="javascript:updateSFLTerms();"
				id="imageField3" src="../images/admin/updateSFLTermButton.jpg" hspace="3"
				vspace="5"> </label> </span> <span id="updateSFLTermProcess"
				style="display: none"><img
				src="../images/admin/updateTableButton_disabled.gif" hspace="3"
				vspace="5" /></span>
				</td></tr>
</table>
</form>