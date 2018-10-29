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
<script src="../js/ranges-jq.js" type="text/javascript" ></script>
<s:form id="rangeDefinitionForm" method="post">
<table id="rangeDefinition" width="100%">
  <thead>
    <tr>
      <td colspan="2" align="left" class="innerPaneHeading"><strong><s:text name='execue.ranges.rangeDefinition.text' /></strong></td>
    </tr>
    <tr>
      <td colspan="2"><div id="errorMessage" align="left"></div></td>
    </tr>
    <tr>
    <td width="70%" height="40" ><strong><s:text name='execue.ranges.Range.heading' /></strong>
       <s:textfield size="40" name="range.name"   id="name" maxlength="255"/><a href="#" id="helpLink" title="Help" alt="Help">?</a></td>
    </tr>
      
      <tr>    
      <td class="fieldNames"><strong><s:text name='execue.ranges.conceptName.text' /></strong> <s:label name = "range.concept.name" id="conceptName"/></td>
    </tr>
    <tr>
     <s:if test="range.enabled == true">
      <td class="fieldNames" colspan="2">
      <strong>Status :</strong>
          <input type="checkbox" name="range.enabled" id="statusCheckboxId" checked value="<s:property value="range.enabled" />" />
          <s:text name="execue.ranges.status.enabled"/> </td>
      </s:if>
      <s:else>
      <td class="fieldNames" colspan="2">
      <strong>Status :</strong>
          <input type="checkbox" name="range.enabled" id="statusCheckboxId" value="false"/>
          <s:text name="execue.ranges.status.disabled"/> </td>
      </s:else>
    </tr>
  </thead>
</table>
<table  width="100%" width="100%"">
  <thead>
    <tr>
      <td class="fieldNames"><strong>Lower Limit</strong></td>
      <td class="fieldNames"><strong>Upper Limit</strong></td>
      <td class="fieldNames" colspan="3"><strong>Description</strong></td>
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
       <td><img  class="addRow"  title="Add range below" alt="Add range below" src="../images/plus.gif" ></td>
      <td><img style='display:none;'   title='Remove this range' alt='Remove this range' src='../images/remove.jpg' class='delRow' /></td>
 
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
      		<span id="enableUpdateRange"> <input type="button" class="buttonSize108" value="<s:text name='execue.ranges.updateRange.button' />" onclick="javascript: updateRange();"></span>
        			<span id="updateProcess" style="display:none"><img src="../images/loadingWhite.gif" hspace="3" vspace="5" />
    		</span>
      </s:if>
      <s:else>
      		<span id="enableUpdateRange">  <input type="button"	class="buttonSize108"  value="<s:text name='execue.ranges.createRange.button' />" onclick="javascript: updateRange();"></span>
        			<span id="updateProcess" style="display:none"><img src="../images/loadingWhite.gif" hspace="3" vspace="5" />
    		</span>
      
      </s:else>
      </td>
      <td colspan="2">&nbsp;</td>
    </tr>
</table>

<s:hidden name="range.id"/>
<s:hidden name="range.conceptBedId"/>
</s:form>
<script>

  $(document).ready(function() {
	reNameTrIds();
   $("#helpLink").helpMessage({messageKey:"execue.ranges.help.name"}); 
   });
</script>
