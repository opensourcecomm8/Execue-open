<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<div class="tableList"
	style="height: 265px; width: 190px; margin-left: 0px; margin-bottom: 5px; white-space: nowrap;padding-left:0px;padding-top:3px;overflow-y:hidden;">
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	id="searchList">
	<s:iterator value="relations" status="inst" id="relation">
		<tr id="relationRow1">
			<td width="1%" class="dotBullet">&nbsp;</td>
			<td width="99%" class="fieldNames">
			<div id="showRelation<s:property value="%{#relation.id}"/>Link"><a
				href="javascript:getRelation(<s:property value="%{#relation.id}"/>);"
				class="links" id=<s:property value="%{#relation.id}"/>><s:property
				value="%{#relation.displayName}" /></a></div>
			<div id="loadingShowRelation<s:property value="%{#relation.id}"/>Link"
				style="display: none;"><img src="../images/loadingBlue.gif"
				width="25" height="25" /></div>
			</td>
		</tr>
	</s:iterator>
</table>
</div>
<div id="paginationDiv2" style="margin-top: 10px; margin-bottom: 5px;"><pg:page targetURL="getRelations.action" targetPane="dynamicPaneRelations"/></div>
<s:hidden id="pageSizeRelations" name="pageDetail.pageSize" />
<s:hidden id="recordCountRelations" name="pageDetail.recordCount" />
<s:hidden id="pageCountRelations" name="pageDetail.pageCount" />
<s:hidden id="noOfLinksRelations" name="pageDetail.numberOfLinks" />
<s:hidden id="paginationTypeRelations" name="paginationType" />

<script>
highLightText(1);
</script>