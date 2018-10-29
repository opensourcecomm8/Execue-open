<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<style>
#tableGridMemberInfo td{
padding-top:0px;
padding-left:3px;
}
#tableGridMemberInfo input[type='text'],input[type='password'],textarea,.inputText{
border:none;	
font-size:11px;
padding-top:2px;
padding-bottom:2px;
}
</style>
<s:form id="memberListForm" name="memberListForm">
<div id="errorMessage">
  
  <s:actionmessage/>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
    <div style="height:212px;overflow:auto;overflow-x:hidden;" id="collist1">
    <table  border="0" cellpadding="1" cellspacing="1" id="tableGridMemberInfo">
      <tr class="tableTitles">
        <td width="100" class="columnHeading"><s:text name="execue.asset.ws.member.label.value" /></td>
        <td width="100" class="columnHeading"><s:text name="execue.asset.ws.member.label.description" /></td>
        <td width="100" class="columnHeading"><s:text name="execue.asset.ws.member.label.long-description" /></td>
        <s:if test='table.lookupType.value != "SL"'>
        <td width="100" class="columnHeading"><s:text name="execue.asset.ws.member.label.lower-limit" /></td>
        <td width="100" class="columnHeading"><s:text name="execue.asset.ws.member.label.upper-limit" /></td>
        </s:if>
        <s:if test="table.indicator.value == 'Y'">
         <td width="100" class="columnHeading"><s:text name="execue.asset.ws.member.label.indicator" /></td>         
        </s:if>
      </tr>
      <s:iterator value="subListForMemberInfo" status="even_odd" id="subListForMemberInfo">      
      <tr>
        <td><input style="width:100px;" type="text" id="subListForMemberInfo<s:property value="#even_odd.index"/>LookupValue" name="subListForMemberInfo[<s:property value="#even_odd.index"/>].lookupValue" value="<s:property value="lookupValue"/>" readonly="readonly"/></td>
        <td><input style="width:150px;" type="text" id="subListForMemberInfo<s:property value="#even_odd.index"/>LookupDescription" name="subListForMemberInfo[<s:property value="#even_odd.index"/>].lookupDescription" value="<s:property value="lookupDescription"/>" maxlength="255" title="<s:property value="lookupDescription"/>"/></td>
        <td><input style="width:100px;" type="text" id="subListForMemberInfo<s:property value="#even_odd.index"/>LongDescription" name="subListForMemberInfo[<s:property value="#even_odd.index"/>].longDescription" value="<s:property value="longDescription"/>" maxlength="255" title="<s:property value="longDescription"/>"  /></td>
        <s:if test='lookupColumn.ownerTable.lookupType.value != "SL"'> 
        <td><input style="width:100px;" type="text" id="subListForMemberInfo<s:property value="#even_odd.index"/>LowerLimit" name="subListForMemberInfo[<s:property value="#even_odd.index"/>].lowerLimit" value="<s:property value="lowerLimit"/>"/></td>
        <td><input style="width:100px;" type="text" id="subListForMemberInfo<s:property value="#even_odd.index"/>UpperLimit" name="subListForMemberInfo[<s:property value="#even_odd.index"/>].upperLimit" value="<s:property value="upperLimit"/>"/></td>
        </s:if> 
        <s:if test="table.indicator.value == 'Y'">
        <s:if test="indicatorBehavior.value == 'P'">
         <td><input style="width:100px;" type="radio" checked="checked" class="indicator" id="subListForMemberInfo<s:property value="#even_odd.index"/>indicatorBehavior" name="subListForMemberInfo[<s:property value="#even_odd.index"/>].indicatorBehavior" value="<s:property value="indicatorBehavior"/>" />
         </td>         
        </s:if>
        <s:else>
          <td><input style="width:100px;" type="radio" class="indicator" id="subListForMemberInfo<s:property value="#even_odd.index"/>indicatorBehavior" name="subListForMemberInfo[<s:property value="#even_odd.index"/>].indicatorBehavior" value="<s:property value="indicatorBehavior"/>"/>
        </s:else>   
        </s:if>     
      </tr>
      <input type="hidden" id="subListForMemberInfo<s:property value="#even_odd.index"/>Id" name="subListForMemberInfo[<s:property value="#even_odd.index"/>].id" value="<s:property value="id"/>"/>
      <input type="hidden" id="subListForMemberInfo<s:property value="#even_odd.index"/>originalDescription" name="subListForMemberInfo[<s:property value="#even_odd.index"/>].originalDescription" value="<s:property value="originalDescription"/>"/>      
      </s:iterator>

      

    </table>
    </div>
    </td>
  </tr>
  <tr><td> <div id="paginationDiv1" ><pg:paginateResults /></div></td></tr>
  <tr>
    <td height="40" valign="top">
    	<span id="enableUpdateMember"> <label><input type="button"	class="buttonSize80" style="width:80px;"  name="imageField4" id="imageField5" onclick="javascript:updateMembers();" value="<s:text name='execue.asset.memberList.updateMembers' />" /> </label> </span>
      		<span id="updateMemberProcess" style="display:none"><input type="button" disabled="disabled"	class="buttonLoaderSize80" style="width:80px;"  value="<s:text name='execue.asset.memberList.updateMembers' />" /></span>
      	<span class="rightButton" id="enableRestMember">  <input type="button"	class="buttonSize80" style="width:81px;margin-left:10px;"  name="imageField4" id="restMembers"   value="<s:text name='execue.global.resetButton.name' />" onclick="javascript:restMemberInfo();"></span>
      	<span class="rightButton" id="restMemberProcess" style="display:none" >  <input type="button" disabled="disabled"	class="buttonLoaderSize80" name="imageField3" id="resetColumn"  value="<s:text name='execue.global.resetButton.name' />" >  </span></td>     
  </tr>
</table>
</s:form>
<script>
	//initializing for ever pagelet.
	tableValueChange();
	sameData=true;
	$("#collist").width(collistWidth);
	function tableValueChange(){
		//console.log("init page");
		var tableData = $( "#tableGridMemberInfo :input" );//table id
		//console.log(jInput);
		// Bind the onchange event for dirty saves
		tableData.bind('change',function( objEvent ){
				// Add dirty flag
				//console.log("value changed");
				sameData=false;
			}
		);
	}
	function setIndicatorBehavior(id){
	  alert(id);
	}
	$(function(){
	$("input.indicator").click(function(){
	var val=$(this).val();
	var id=$(this).attr("id");
	var checked=$(this).attr("checked"); 
	if(id=="subListForMemberInfo0indicatorBehavior"){
	   $("#subListForMemberInfo0indicatorBehavior").attr("checked",true).attr("value","POSITIVE");
	   $("#subListForMemberInfo1indicatorBehavior").attr("checked",false).attr("value","NEGATIVE");
	}
	else if(id=="subListForMemberInfo1indicatorBehavior"){
	  $("#subListForMemberInfo1indicatorBehavior").attr("checked",true).attr("value","POSITIVE");
	  $("#subListForMemberInfo0indicatorBehavior").attr("checked",false).attr("value","NEGATIVE");
	}
	
	});
	});
</script>
