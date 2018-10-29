<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="errorMessage"><s:fielderror /> <s:actionmessage /></div>
<div class="assetTableList" style="height: 200px; width: 190px;">
<s:form id="assetTablesForm">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="searchList">
		
		<s:iterator value="tables" status="even_odd" id="assetTable">
			<tr id="tableRow<s:property value="name"/>" class="rowNotSelected">
				<td width="90%" class="fieldNames">
				<div id="tables<s:property value="name"/>link"><a title="<s:property value="name"/>"
					href="javascript:getDefaultMetric(<s:property value="id"/>,'<s:property value="name"/>','<s:property value="eligibleSystemDefaultMetric"/>','<s:property value="displayName"/>');"
					class="links" id="<s:property value="name"/>"><s:property
					value="displayName" /></a></div>
				<div id="loadingShowAssetTable<s:property value="name"/>Link"
					style="display: none;"><img src="../images/loading.gif"
					width="20" height="20"></div>
				</td>
			</tr>
		</s:iterator>
	</table>
	
</s:form>
</div>