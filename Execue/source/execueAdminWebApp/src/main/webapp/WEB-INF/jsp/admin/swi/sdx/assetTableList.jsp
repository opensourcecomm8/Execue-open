<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="errorMessage"><s:fielderror /> <s:actionmessage /></div>
<div class="assetTableList" style="height: 200px; width: 190px;">
<s:form id="assetTablesForm">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="searchList">
		<%
		   int ct = 0;
		%>
		<s:iterator value="assetTables" status="even_odd" id="assetTable">
			<tr id="tableRow<s:property value="name"/>" class="rowNotSelected">
				<td width="10%" class="dotBullet"><input
					name="tableNamesForDeletion[<s:property value="#even_odd.index"/>]"
					type="checkbox" onclick="checkedAsset();"
					id="tableNamesForDeletion<s:property value="#even_odd.index"/>"
					value="<s:property value="name"/>" /></td>
				<%
				   ct++;
				%>
				<td width="90%" class="fieldNames">
				<div id="showAssetTable<s:property value="name"/>Link"><a title="<s:property value="name"/>"
					href="javascript:getTableInfo('<s:property value="name"/>','<s:property value="description"/>','<s:property value="owner"/>','asset','<s:property value="eligibleSystemDefaultMetric"/>');"
					class="links" id="<s:property value="name"/>"><s:property
					value="displayName" /></a></div>
				<div id="loadingShowAssetTable<s:property value="name"/>Link"
					style="display: none;"><img src="../images/admin/loading.gif"
					width="20" height="20"></div>
				</td>
			</tr>
		</s:iterator>
	</table>
	<input type=hidden name='ccolName' id='ccolName' value='<%=ct%>' />
</s:form>
</div>