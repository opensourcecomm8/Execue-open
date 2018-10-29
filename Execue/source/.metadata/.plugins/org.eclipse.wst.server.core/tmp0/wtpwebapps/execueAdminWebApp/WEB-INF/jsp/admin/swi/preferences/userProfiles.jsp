<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:form id="profilesForm">
<table width="100%" border="0" cellspacing="0" cellpadding="0" id="searchList">
  <s:iterator value="conceptProfiles" status="even_odd" id="conceptProfile">
  <tr id="tableRow3">
    <td width="14%" class="dotBullet" width="30" style="padding-top:5px;"><span class="fieldNames"> <input type="checkbox" onclick="checkedProfile();" name="conceptProfiles[<s:property value="#even_odd.index"/>].id"
      id="conceptProfiles[<s:property value="#even_odd.index"/>].id" value="<s:property value="id"/>"/> </span></td>
    <td width="86%" class="fieldNames" align="justify">
    <div id="showUserProfile<s:property value="id"/>Link"><a href="javascript:getProfileDefinition('<s:property value="id"/>', '<s:property value="type"/>');" class="links" id="<s:property value="name"/>"><s:property value="displayName"/></a></div>
    <div id="loadingShowUserProfile<s:property value="id"/>Link" style="display: none;"><img src="../images/admin/loading.gif" width="20"
      height="20"></div>
    </td>
  </tr>
  </s:iterator>
  <s:iterator value="instanceProfiles" status="even_odd" id="instanceProfile">
  <tr id="tableRow4">
    <td class="dotBullet" width="30"><span class="fieldNames"> <input type="checkbox" onclick="checkedProfile();" name="instanceProfiles[<s:property value="#even_odd.index"/>].id" 
    id="instanceProfiles[<s:property value="#even_odd.index"/>].id" value="<s:property value="id"/>"/>
    </span></td>
    <td class="fieldNames" align="left">
    <div id="showUserProfile<s:property value="id"/>Link"><a href="javascript:getProfileDefinition('<s:property value="id"/>', '<s:property value="type"/>');" class="links" id="<s:property value="name"/>"><s:property value="displayName"/></a></div>
    <div id="loadingShowUserProfile<s:property value="id"/>Link" style="display: none;"><img src="../images/admin/loading.gif" width="20"
      height="20"></div>
    </td>
  </tr>
  </s:iterator>
</table>
</s:form>