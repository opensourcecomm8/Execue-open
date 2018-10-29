<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:form id="dimensionDefinitionForm" >
<table id="rangeDefinitionDetail" width="100%">

  <thead>
    <tr>
      <td class="fieldNames"><strong><s:text name="execue.cube.dimension.instance.name"/></strong></td>
      <td class="fieldNames"><strong><s:text name="execue.cube.dimension.member.value"/></strong></td>
      <td class="fieldNames"><strong><s:text name="execue.cube.dimension.member.description"/></strong></td>
    </tr>
  </thead>

  <tbody>
    <s:iterator value="instanceMembrs" status="rangeStatus" id="dimension">
     <tr>
      <td class="fieldNames"><s:property value="instanceDisplayName"/></td>
      <td class="fieldNames"><s:property value="memberValue"/></td>
      <td class="fieldNames"><s:property value="memberDescription"/></td>
    </tr>
    </s:iterator>
  </tbody>
</table>
<table id="rangeDefinitionDetailSubmits" width="100%">
    <tr><td colspan="2" height="1">&nbsp;</td></tr>
    <tr align = "center">
      <td align="center" colspan="2">
      <s:if test="dimensionType != null">
      &nbsp;
      </s:if>
      <s:else>
      <input type="button" value="Add to Selection" onclick="javascript: addDimension();">
      </s:else>
      </td>
    </tr>
</table>
<s:hidden name="concept.id"/>
</s:form>
