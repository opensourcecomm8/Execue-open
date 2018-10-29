<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<div class="tableList" style="height: 200px; width: 190px; margin-left: 0px; margin-bottom: 3px;overflow:auto;overflow-y:hidden;">
<table width="100%" border="0" cellspacing="0" cellpadding="0" id="searchList2">

  <s:iterator value="sourceTables" status="even_odd" id="sourceTable">
  <tr id="tableRow<s:property value="name"/>" class="rowNotSelected">
    <td width="1%" class="dotBullet">&nbsp;</td>
    <td width="99%" class="fieldNames">
    <div id="showTable<s:property value="name"/>Link"><a  title="<s:property value="name"/>" href="javascript:getTableInfo('<s:property value="name"/>','<s:property value="description"/>','<s:property value="owner"/>','source','<s:property value="eligibleSystemDefaultMetric"/>');" class="links" id="<s:property value="name"/>"><s:property value="displayName"/></a></div>
    <div id="loadingShowTable<s:property value="name"/>Link" style="display: none;"><img src="../images/loading.gif" width="20" height="20"></div>
    </td>
  </tr>
  </s:iterator>

</table>
</div>
<s:hidden id="assetId" name="asset.id" value="%{asset.id}" />
<s:hidden id="assetName" name="asset.name" value="%{asset.name}" />
<s:set name="url" id="url" value="%{'../swi/showSubSourceTables.action?asset.id='+asset.id+'&asset.name='+asset.name}"/>
<div id="paginationDiv2" style="margin-top:5px;margin-bottom:5px;" ><pg:page targetURL="${url}" targetPane="sourceTableList"/></div>
<script>
highLightText(2);
</script>