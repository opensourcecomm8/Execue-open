<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
#myAlert{
display: none; position: absolute; padding: 5px; color: rgb(0, 0, 0); background: none repeat scroll 0% 0% rgb(255, 255, 215); border: 1px solid rgb(0, 0, 0); left: 200px;z-index:10000;	
}
input[readonly='readonly']{
background-color:#EAEAEA;	
}
input[type='text']{
width:145px;	
}
</style>
<script src="../js/admin/ranges-jq.js" language="JavaScript"></script>
<s:form id="rangeDefinitionForm" >
<table id="rangeDefinitionDetail" width="100%">

  <thead>
    <tr>
      <td class="fieldNames"><strong><s:text name="execue.ranges.rangeDetail.lowerLimit"/></strong></td>
      <td class="fieldNames"><strong><s:text name="execue.ranges.rangeDetail.upperLimit"/></strong></td>
      <td class="fieldNames" colspan="3"><strong><s:text name="execue.ranges.rangeDetail.description"/></strong></td>
    </tr>
  </thead>

  </table>
   
    <table  width="80%" id="rangesTable" align="left"  >
    <s:iterator value="rangeDetailList" status="rangeStatus" id="rangeDetail">
     <tr>
       <td> 
       
       <input type="text" readonly="readonly" value="<s:property value="lowerLimit"/>" name="rangeDetailList[<s:property value="#rangeStatus.index"/>].lowerLimit"    />
 
       </td>
      
       <td> 
       <input type="text" value="<s:property value="upperLimit"/>" name="rangeDetailList[<s:property value="#rangeStatus.index"/>].upperLimit"   />
       
       
       </td> 
      
       <td> 
       <input type="text" rangeId="<s:property value="id"/>"  name="rangeDetailList[<s:property value="#rangeStatus.index"/>].description" value="<s:property value="description"/>"   />
             
       </td>
       <td><img  class="addRow"  title="Add range below" alt="Add range below" src="../images/admin/plus.gif" ></td>
      <td><img style='display:none;'   title='Remove this range' alt='Remove this range' src='../images/admin/remove.jpg' class='delRow' /></td>
 
        <input type="hidden" name="rangeDetailList[<s:property value="#rangeStatus.index"/>].id" value="<s:property value="id"/>" id="rangeDetailList[<s:property value="#rangeStatus.index"/>].id">
        <input type="hidden" name="rangeDetailList[<s:property value="#rangeStatus.index"/>].order" value="<s:property value="order"/>" id="rangeDetailList[<s:property value="#rangeStatus.index"/>].order">
        <input type="hidden" name="rangeDetailList[<s:property value="#rangeStatus.index"/>].value" value="<s:property value="value"/>" id="rangeDetailList[<s:property value="#rangeStatus.index"/>].value">

     </tr>
    </s:iterator>
  
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
<script>

  $(document).ready(function() {
	reNameTrIds();
   //$("#helpLink").helpMessage({messageKey:"execue.ranges.help.name"}); 
   });
</script>