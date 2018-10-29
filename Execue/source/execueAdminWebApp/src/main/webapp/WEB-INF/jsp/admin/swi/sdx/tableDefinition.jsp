<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.execue.core.common.type.CheckType"%>
<form id="tableDefinitionForm" name="tableDefinitionForm">
<div id="errorMessage">
  <s:fielderror />
  <s:actionmessage/>
   <s:actionerror/>
</div>
<div id="displayError" style="color:red"/>
<table width="98%" border="0" align="center" cellpadding="2" cellspacing="0" style="margin-top:10px;margin-left:10px;">
  
  <tr>
     <td width="17%" class="fieldNames">
      <s:text name="execue.asset.ws.table.label.name" /> <span class="redFont">*</span>
   </td>    
    <td width="21%">
        <s:set name="tabval" value="table.name"/>
        <s:textfield cssClass="textBox1" id="tableNameFromTableDefinition"
         name="table.displayName" readonly="true" title="%{#tabval}" maxlength="255" />         
          <div style="display:none;white-space: nowrap; position: absolute; z-index: auto; padding: 4px; border: 1px solid rgb(204, 204, 204); background-color: #fff; color: #f00; top: 14.75px; left: 330.85px;" id="errorMessageDiv"></div>
   </td>    
    
    <!--td width="11%" class="fieldNames">&nbsp;<span class="hotspot"
      onmouseover="tooltip.show('<strong>Tooltip will be here.</strong><br /> This is dummy text. More description be added. More lines can be added but restrict to max 3 to 4 rows..');"
      onmouseout="tooltip.hide();">tip</span></td-->
    <td colspan="2" rowspan="6"  valign="top" style="padding-left:30px;">
    
    <!---------------dynamic rows for  type ------->
    
    
    <table width="100%" border="0" cellspacing="0" cellpadding="3">
 
 
  <tr id="lookupValueColRow">
    <td width="44%" height="20"><div id="lookupValueColText"> <s:text name="execue.asset.ws.table.label.value-column" /><span class="redFont">*</span></div> </td>
    <td width="56%"> <div id="lookupValueColValue">
    <select id="tableLookupValueColumn" name="table.lookupValueColumn" >
    <s:iterator value="columnsForSelection" >
    	<s:if test='%{table.lookupValueColumn == name}'>
        <!-- s:select cssClass="textBox1" id="tableLookupValueColumn" name="table.lookupValueColumn" list="columnsForSelection" listKey="name" listValue="name"/-->
        <option value="<s:property value="name"/>" alt="<s:property value="name"/>" title="<s:property value="name"/>" selected="selected"><s:property value="name"/></option>
        </s:if>
        <s:else>
        	<option value="<s:property value="name"/>" alt="<s:property value="name"/>" title="<s:property value="name"/>"><s:property value="name"/></option>
        </s:else>
    </s:iterator>
    </select>
      </div></td>
  </tr>
  
  
  <tr id="lookupDescColRow">
    <td height="20">  <div id="lookupDescColText" style="white-space:nowrap;"><s:text name="execue.asset.ws.table.label.description-column" /><span class="redFont">*</span></div> </td>
    <td><div id="lookupDescColValue">
    <select name="table.lookupDescColumn" id="tableLookupDescColumn">
    <s:iterator value="columnsForSelection">
    	<!--s:select cssClass="textBox1" id="tableLookupDescColumn" name="table.lookupDescColumn" list="columnsForSelection" listKey="name" listValue="name"/></div></td-->    	
    	<s:if test='%{table.lookupDescColumn == name}'>
    	<option value="<s:property value="name"/>" alt="<s:property value="name"/>" title="<s:property value="name"/>" selected="selected"><s:property value="name"/></option>
    	</s:if>
    	<s:else>
    		<option value="<s:property value="name"/>" alt="<s:property value="name"/>" title="<s:property value="name"/>" ><s:property value="name"/></option>
    	</s:else>
    </s:iterator>
    </select>
    </div></td>
  </tr>
  
  
  
  <tr id="lookupLowerLimitColRow">
    <td height="20"><div id="lookupLowerLimitColText" style="white-space:nowrap;"><s:text name="execue.asset.ws.table.label.lower-limit-column" /> <span class="redFont">*</span></div>   </td>
    <td><div id="lookupLowerLimitColValue">
    <select name="table.lowerLimitColumn" id="tableLowerLimitColumn">
    <s:iterator value="columnsForSelection">
    	<!--s:select cssClass="textBox1" id="tableLookupDescColumn" name="table.lookupDescColumn" list="columnsForSelection" listKey="name" listValue="name"/></div></td-->    	
    	<s:if test='%{table.lowerLimitColumn == name}'>
    	<option value="<s:property value="name"/>" alt="<s:property value="name"/>" title="<s:property value="name"/>" selected="selected"><s:property value="name"/></option>
    	</s:if>
    	<s:else>
    		<option value="<s:property value="name"/>" alt="<s:property value="name"/>" title="<s:property value="name"/>" ><s:property value="name"/></option>
    	</s:else>
    </s:iterator>
    </select>
    <!--s:select cssClass="textBox1" id="table.lowerLimitColumn" name="table.lowerLimitColumn" list="columnsForSelection" listKey="name" listValue="name"/-->
    </div></td>
  </tr>
  
  
  <tr id="lookupUpperLimitColRow">
    <td height="20"><div id="lookupUpperLimitColText" style="white-space:nowrap;"><s:text name="execue.asset.ws.table.label.upper-limit-column" /> <span class="redFont">*</span></div></td>
    <td><div id="lookupUpperLimitColValue">
    <select name="table.upperLimitColumn" id="tableUpperLimitColumn">
    <s:iterator value="columnsForSelection">
    	<!--s:select cssClass="textBox1" id="tableLookupDescColumn" name="table.lookupDescColumn" list="columnsForSelection" listKey="name" listValue="name"/></div></td-->    	
    	<s:if test='%{table.upperLimitColumn == name}'>
    	<option value="<s:property value="name"/>" alt="<s:property value="name"/>" title="<s:property value="name"/>" selected="selected"><s:property value="name"/></option>
    	</s:if>
    	<s:else>
    		<option value="<s:property value="name"/>" alt="<s:property value="name"/>" title="<s:property value="name"/>" ><s:property value="name"/></option>
    	</s:else>
    </s:iterator>
    </select>
    <!--s:select cssClass="textBox1" id="table.upperLimitColumn" name="table.upperLimitColumn" list="columnsForSelection" listKey="name" listValue="name"/-->
    </div></td>
  </tr>
  
  
  <tr id="lookupParentTableRow">
    <td height="20"><div id="lookupParentTablesText" style="white-space:nowrap;">Parent Table<!--s:text name="asset.ws.table.label.upper-limit-column" /--></div></td>
    <td><div id="lookupParentTablesValue">
    <select name="table.parentTable" id="tableLookupParentDescColumn">
    <s:iterator value="parentTables">
    	<!--s:select cssClass="textBox1" id="tableLookupDescColumn" name="table.lookupDescColumn" list="columnsForSelection" listKey="name" listValue="name"/></div></td-->    	
    	<s:if test='%{table.parentTable == name}'>
    	<option value="<s:property value="name"/>" alt="<s:property value="name"/>" title="<s:property value="name"/>" selected="selected"><s:property value="name"/></option>
    	</s:if>
    	<s:else>
    		<option value="<s:property value="name"/>" alt="<s:property value="name"/>" title="<s:property value="name"/>" ><s:property value="name"/></option>
    	</s:else>
    </s:iterator>
    </select>
    <!--s:select cssClass="textBox1" id="table.parentTableId" name="table.parentTableId" list="parentTables" listKey="id" listValue="name"/-->
    </div></td>
  </tr>
  
  
  
</table>

     
      <!---------------dynamic rows for  type -------> 
        
        
         </td>
    </tr>
  <tr>
    <td class="fieldNames"><s:text name="execue.asset.ws.table.label.description" /></td>
    <td><s:textfield cssClass="textBox1" id="tableDescription" name="table.description" maxlength="255"/></td>
    </tr>
  <%--
  <tr>
    <td class="fieldNames"><s:text name="asset.ws.table.label.aggregated" /></td>
    <td>
      <s:select cssClass="textBox1" id="table.aggregated" name="table.aggregated" list="checkTypes" listValue="description"/>
    </td>
    </tr>
    --%>
  <tr>
    <td class="fieldNames"><s:text name="execue.asset.ws.table.label.type" /></td>
    <td>
      <s:select cssClass="textBox1" id="tableLookupType" name="table.lookupType" list="lookupTypesForTableTab" listValue="description" onchange="javascript:showHideLookupBlock();"/>
    </td>
    </tr> 
    <s:if test="sourceName!='source'">
	  <tr>
	    <td class="fieldNames"><s:text name="execue.asset.ws.table.label.virtual"/></td>         
	    <s:if test="table.virtual.value=='Y'"> 
	      <td>
	      <input style="margin:0px;padding:0px;" id="virtualCheckBoxId" type="checkbox" checked="checked" disabled="disabled"/>
	      <input name="table.virtual" style="margin:0px;padding:0px;" type="hidden" id="virtualCB" checked="checked" value="YES" onclick="javascript:virtualCBCheck();" /></td>
	    </s:if>
	    <s:else> 
	      <td><input name="table.virtual" style="margin:0px;padding:0px;" type="checkbox" id="virtualCB" onclick="javascript:virtualCBCheck();" /></td>
	    </s:else>     
	    </tr>
    </s:if>
    <tr id="indicatorRow">
      <td class="fieldNames"><s:text name="execue.asset.ws.table.label.indicator"/> </td>  
	     <s:if test="table.indicator.value=='Y'"> 
	       <s:if test="table.virtual.value=='Y'"> 
	           <td><input name="table.indicator" style="margin:0px;padding:0px;" type="checkbox" id="indicatorcb" checked="checked" value="YES"" disabled="disabled" onclick="javascript:checkIndicator();"/></td>
	       </s:if>
	       <s:else>
	         <td><input name="table.indicator" style="margin:0px;padding:0px;" type="checkbox" id="indicatorcb" checked="checked" value="YES"" onclick="javascript:checkIndicator();"/></td>
	       </s:else>
	    </s:if>
	    <s:else> 
	      <s:if test="table.virtual.value=='Y'"> 
	          <td><input name="table.indicator" style="margin:0px;padding:0px;" type="checkbox" id="indicatorcb" disabled="disabled" onclick="javascript:checkIndicator();" /></td>
	       </s:if>
	       <s:else>
	      <td><input name="table.indicator" style="margin:0px;padding:0px;" type="checkbox" id="indicatorcb" onclick="javascript:checkIndicator();" /></td>
	      </s:else>
	    </s:else>     
    </tr>
    
    
    <!--  eligible default metric  -->
    <tr>
      <td class="fieldNames" style="white-space:nowrap"><div id="defaultMetricText">Eligible Default Metric</div></td>    
    <s:if test="table.eligibleDefaultMetric.value=='Y'"> 
      <td><div id ="defaultMetricValue"><input name="table.eligibleDefaultMetric" style="margin:0px;padding:0px;" type="checkbox" id="eligibleDefaultMetricCB" checked="checked"  value="<%=CheckType.YES %>"  /></div></td>
    </s:if>
    <s:else> 
      <td><div id ="defaultMetricValue"><input name="table.eligibleDefaultMetric" style="margin:0px;padding:0px;" type="checkbox" id="eligibleDefaultMetricCB"  value="<%=CheckType.YES %>"/></div></td>
    </s:else> 
  	</tr>
    <!--  eligible default metric ends -->
    
    
  <tr id="virtualTableRowId">  
    <td class="fieldNames">Actual Name</td>     
     <td>
     <s:if test="table.actualTableDisplayName !=null">
      <s:textfield name="table.actualTableDisplayName"  id="virtualTabledisplayName"  cssClass="textBox1" readonly="true"/>
     </s:if>
     <s:else>
      <s:textfield name="table.displayName"  id="virtualTabledisplayName"  cssClass="textBox1" readonly="true"/>
     </s:else>
    
     <input name="table.actualName" type="hidden" id="virtualTableActualName" class="textBox1" /></td>    
    </tr>
  <tr>
   
    <!-- td height="40" align="left">&nbsp;</td-->
     <td height="40" colspan="4" align="left">
       <s:if test="table.id == null">
       	<s:hidden id="tblId" value="addTable"></s:hidden>       	  
       		<span id="enableCreateTable"><input type="button" class="buttonSize80" id="imageField" value="<s:text name='execue.asset.tableDefinition.AddTable' />" onclick="javascript:createUpdateTable('create');"/></span>
       		<span id="createUpdateTablProcess" style="display:none"><input type="button" disabled="disabled" class="buttonLoaderSize80"  value="<s:text name='execue.asset.tableDefinition.AddTable' />"  /></span>
        </s:if> 
       <s:else>  
       	<s:hidden id="tblId" value="updateTable"></s:hidden>
       	<s:if test="table.virtual.value=='Y'">        
         <span id="enableUpdateTable"><input type="button" disabled="disabled" title="Unsupported Operation" alt="Unsupported Operation"	class="buttonSize80" id="imageField" value="<s:text name='execue.asset.tableDefinition.updateTable' />" onclick="javascript:createUpdateTable('update');"/></span>
      	</s:if>
      	<s:else>
      	 <span id="enableUpdateTable"><input type="button"	class="buttonSize80" id="imageField" value="<s:text name='execue.asset.tableDefinition.updateTable' />" onclick="javascript:createUpdateTable('update');"/></span>
      	</s:else>
      	 <span id="createUpdateTablProcess" style="display:none"><input type="button" disabled="disabled"	class="buttonLoaderSize80"   value="<s:text name='execue.asset.tableDefinition.updateTable' />" /></span>
        </s:else>
      <span class="rightButton"><input type="button"	class="buttonSize90"  id="imageField7"  value="<s:text name='execue.global.resetButton.name' />" onclick="javascript:resetTablInfo();"/></span></td>
    </tr>
</table>
<s:hidden id="tableOwner" name="table.owner"/>
<s:hidden id="tableIdFromTableDefinition" name="table.id"/>
<s:hidden id="tableAlias" name="table.alias"/>
<s:hidden id="tableName" name="table.name"/>
<s:hidden id="tableAggregatedFlag" name="table.aggregated"/>
</form>

<script>
	if(virtualFlag){
	if(!virtualSaved){
	    var actualTabName=$("#tableName").val();		   
	    $("#virtualTableRowId").show();
		$("#virtualTableActualName").val(actualTabName);
		$("#tableNameFromTableDefinition").attr("readOnly","");
		$("#tableNameFromTableDefinition").attr("disabled","");
		$("#virtualTableActualName").attr("readOnly","readOnly");
		$("#virtualTableActualName").css("background-color","#BEBEBE");
		$("#tableNameFromTableDefinition").css("background-color","#FFFFFF");
		$("#tableNameFromTableDefinition").val("");
		$("#virtualCB").attr("checked","checked");
		$("#virtualCB").attr("value","YES");
		}else{
		  $("#virtualTableActualName").val('<s:property value="table.actualName"/>');	
		  $("#virtualTableActualName").attr("readOnly","readOnly");
		  $("#virtualTableActualName").css("background-color","#BEBEBE");
		}
	  }else{	 
	  var isVirtual='<s:property value="table.virtual.value"/>';
	  if(isVirtual=='Y'){	 
	  	 $("#virtualTableRowId").show();	
		 $("#virtualTableActualName").val('<s:property value="table.actualName"/>');	
		 $("#virtualTableActualName").attr("readOnly","readOnly");
		 $("#virtualTableActualName").css("background-color","#BEBEBE");	  
	  }else{
	  	$("#virtualTableRowId").hide();	
		$("#virtualTableActualName").val("");
		$("#virtualCB").removeAttr("checked");
		$("#virtualCB").attr("value","NO");	  
	  }
	     var tableName=$("#tableNameFromTableDefinition").val();			
		//$("#virtualTableRowId").hide();	
		//$("#virtualTableActualName").val("");
		//$("#tableNameFromTableDefinition").val(tableName);
		$("#tableNameFromTableDefinition").attr("readOnly","true");
		$("#tableNameFromTableDefinition").attr("readOnly","readOnly");
		$("#tableNameFromTableDefinition").css("background-color","#BEBEBE");	
	}
     $(function(){
		  $virtual=$("#virtualCheckBoxId").is(":disabled");
			  if($virtual){
				 $("#tableLookupType").attr("readonly","true").attr("disabled","disabled");
				 $("#tableLookupValueColumn").attr("disabled","disabled");
				 $("#tableLookupDescColumn").attr("disabled","disabled");
				 $("#tableDescription").attr("readonly","true").css("background-color","#BEBEBE");	
				 $("#tableLowerLimitColumn").attr("disabled","disabled");
				 $("#tableUpperLimitColumn").attr("disabled","disabled");
				 $("span.rightButton").hide();
			  }
		  });
		   
  function checkIndicator(){ 
	  if( $('#indicatorcb').is(':checked')) {
	      $("#indicatorcb").attr("value","YES");
	  }else{
	     $("#indicatorcb").attr("value","NO");
	  }  
  }

</script>
