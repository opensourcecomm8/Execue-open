<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table width="100%" border="0" cellspacing="0" cellpadding="1">
  <s:iterator value="tables" status="inst" id="table">  
  <s:if	test="%{#table.virtual != 'Y'}">    
    <tr id="tableRow">
      <td width="1%" class="dotBullet">&nbsp;</td>
      <td width="99%" class="fieldNames">
      <s:set name="tableName" value="%{#table.name}"/>
       <s:set name="orphanTableStatus" value="false"/>
      <s:iterator value="orphanTables" status="pkeyCount" id="orphanTable">
           <s:set name="orphanTableName" value='name'/>
          <s:if test="%{#tableName==#orphanTableName}">         
           <s:set name="orphanTableStatus" value="true"/>
          </s:if>
         </s:iterator>
      <s:if test="%{#orphanTableStatus}">
      <div id="showTableLink"><a href="javascript:getConstraintsForTable(<s:property value="%{#table.id}"/>);"
        class="linksColor" id=<s:property value="%{#table.id}"/>)><s:property value="%{#table.displayName}" /></a></div>
        </s:if>
       <s:else>
       <div id="showTableLink"><a href="javascript:getConstraintsForTable(<s:property value="%{#table.id}"/>);"
        class="links" id=<s:property value="%{#table.id}"/>)><s:property value="%{#table.displayName}" /></a></div>
      
       </s:else>
      <div id="loadingShowTableLink" style="display: none;"><img src="../images/admin/loadingBlue.gif" width="25"
        height="25" /></div>
      </td>
    </tr>
    </s:if>
  </s:iterator>
</table>
