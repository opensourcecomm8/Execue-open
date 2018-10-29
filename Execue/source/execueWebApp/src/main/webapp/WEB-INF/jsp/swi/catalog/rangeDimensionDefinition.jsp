<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:form id="rangeDefinitionForm" >
<table id="rangeDefinitionDetail" width="100%">

  <thead>
    <tr>
      <td class="fieldNames"><strong><s:text name="execue.ranges.rangeDetail.lowerLimit"/></strong></td>
      <td class="fieldNames"><strong><s:text name="execue.ranges.rangeDetail.upperLimit"/></strong></td>
      <td class="fieldNames" colspan="3"><strong><s:text name="execue.ranges.rangeDetail.description"/></strong></td>
    </tr>
  </thead>

  <tbody>
   
    <s:iterator value="rangeDetailList" status="rangeStatus" id="rangeDetail">
     <tr>
       <td> <input type="text" class="grayed" readonly="readonly" name="rangeDetailList[<s:property value="#rangeStatus.index"/>].lowerLimit"  class="textBox" style="width:120px;" id="rangeDetailList[<s:property value="#rangeStatus.index"/>].lowerLimit" value="<s:property value="lowerLimit"/>"/></td>
       <s:if test="#rangeStatus.last ">	
       <td> <input type="text" class="grayed" readonly="readonly" onchange='javascript:setNextLowerLimit(<s:property value="#rangeStatus.index"/>)' name="rangeDetailList[<s:property value="#rangeStatus.index"/>].upperLimit"  class="textBox" id="rangeDetailList[<s:property value="#rangeStatus.index"/>].upperLimit" value="<s:property value="upperLimit"/>"/></td>
       </s:if>
       <s:else>
       <td> <input type="text" onchange='javascript:setNextLowerLimit(<s:property value="#rangeStatus.index"/>)' name="rangeDetailList[<s:property value="#rangeStatus.index"/>].upperLimit"  class="textBox" id="rangeDetailList[<s:property value="#rangeStatus.index"/>].upperLimit" value="<s:property value="upperLimit"/>"/></td> 
       </s:else>
       <td> <input type="text" name="rangeDetailList[<s:property value="#rangeStatus.index"/>].description" class="textBox" id="rangeDetailList[<s:property value="#rangeStatus.index"/>].description" value="<s:property value="description"/>"/></td>
       <td><img class="plusminus"  src="../images/plus.gif" alt="Add range below"
        title="Add range below" onclick='return addRowToTable(<s:property value="#rangeStatus.index"/>)' tabindex="-1" /></td>
      <td><img class="plusminus"  src="../images/remove.jpg" alt="Remove this range"
        title="Remove this range" onclick='return removeRowFromTable(<s:property value="#rangeStatus.index"/>)' tabindex="-1" /></td>
 
        <input type="hidden" name="rangeDetailList[<s:property value="#rangeStatus.index"/>].id" value="<s:property value="id"/>" id="rangeDetailList[<s:property value="#rangeStatus.index"/>].id">
        <input type="hidden" name="rangeDetailList[<s:property value="#rangeStatus.index"/>].order" value="<s:property value="order"/>" id="rangeDetailList[<s:property value="#rangeStatus.index"/>].order">
        <input type="hidden" name="rangeDetailList[<s:property value="#rangeStatus.index"/>].value" value="<s:property value="value"/>" id="rangeDetailList[<s:property value="#rangeStatus.index"/>].value">

     </tr>
    </s:iterator>
  </tbody>
</table>
<table id="rangeDefinitionDetailSubmits" width="100%">
    <tr><td colspan="5" height="2">&nbsp;</td></tr>
    <tr align = "center">
      <td align="center" colspan="2">
      <s:if test="range.id != null">
      <input type="button" class="buttonSize130" value="<s:text name='execue.rangeDimensionDefinition.updateDimension.button' />" style="cursor:pointer;" value="Update Dimension" onclick="javascript: updateRange();" />
      </s:if>
      <s:else>
      <input type="button" class="buttonSize108" value="<s:text name='execue.rangeDimensionDefinition.addDimension.button' />" style="cursor:pointer;" value="Add Dimension" onclick="javascript: updateRange();">
      </s:else>
      </td>
      <td colspan="2">&nbsp;</td>
    </tr>
</table>
<s:hidden id="rangeId" name="range.id"/>
<s:hidden name="range.concept.id"/>
<s:hidden name="concept.id"/>
<s:hidden name="concept.bedId"/>
</s:form>
