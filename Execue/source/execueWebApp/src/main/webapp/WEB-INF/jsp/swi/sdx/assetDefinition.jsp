<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="70%" style="padding-left:10px;">
    
<form id="assetDefinitionForm" name=assetDefinitionForm>
<div id="errorMessage"><s:fielderror /> <s:actionmessage /></div>
<table width="375" border="0" align="left" cellpadding="3"
	cellspacing="0">
	<tr>
		<td colspan="4" height="10"></td>
	</tr>
	<tr>		
		<td width="23%" class="fieldNames"><s:text
			name="execue.asset.ws.asset.label.data-source" /></td>
		<td  class="fieldNames"><s:if
			test="asset.id == null">
			<s:select name="asset.dataSource.id" cssStyle="width:123px;" id="dataSourceId"
				list="dataSources" listKey="id" listValue="displayName" />
		</s:if> <s:else>
			<strong> <s:property value="asset.dataSource.displayName" /> </strong>
			<s:hidden name="asset.dataSource.id" />
		</s:else></td><td></td><td width="38%" rowspan="6" align="right" valign="top" >
         
           </td>
	</tr>
	
	<tr>
		<td width="23%" class="fieldNames"><s:text
			name="execue.asset.ws.asset.label.name" /><s:text name="execue.global.mandatory"></s:text></td>
		<td width="30%"><s:textfield name="asset.displayName"
			id="assetNameFromAssetDefinition" maxlength="255" /></td>
		 <td width="9%">&nbsp;</td>
	</tr>
	<!-- <tr>
		<td class="fieldNames"><s:text
			name="asset.ws.asset.label.display-name" /></td>
		<td><s:textfield name="asset.displayName" id="asset.displayName" /></td>
		 <td>&nbsp;</td>
	</tr>-->
	<tr>
		<td class="fieldNames"><s:text
			name="execue.asset.ws.asset.label.description" /></td>
		<td><s:textfield name="asset.description" id="asset.description"  maxlength="255" /></td>
     <td>&nbsp;</td>
	</tr>    
   
    <s:if test="asset.id != null">
     <tr>
          <td width="23%" class="fieldNames"><s:text
			name="execue.asset.ws.asset.label.type" /></td>
		<td width="30%"><s:property value="asset.type"/></td>            
		 <td>&nbsp;</td>    
	</tr>
	<tr>
        <td class="fieldNames"><s:text
            name="execue.asset.ws.asset.label.status" /></td>
        <td><s:select name="asset.status" cssStyle="width:123px;" id="assetStatusId"
                list="statuses" listValue="description" /></td>
     <td>&nbsp;</td>
    </tr>
    </s:if>
    <s:else>
    <tr>
        <td class="fieldNames"><s:text
            name="execue.asset.ws.asset.label.status" /></td>
        <td><s:property value="asset.status"/></td>
     <td>&nbsp;</td>
    </tr>    
    </s:else>

    
    <!-- <tr>
		<td class="fieldNames"><s:text
			name="asset.ws.asset.label.subType" /></td>
		<td><s:select name="asset.subType" id="subType"
			list="assetSubTypes" listValue="description"/></td>
     <td>&nbsp;</td>
	</tr>-->
    
	
	<tr>
		<s:hidden id="assetIdFromAssetDefinition" name="asset.id"
			value="%{asset.id}" />
			
			<s:hidden id="assetOriginFromAssetDefinition" name="asset.originType"
			value="%{asset.originType}" />
            
		<td height="15" colspan="4" align="left"><s:if
			test="asset.id != null">
			<span id="enableUpdateAsset"> <input type="button"	class="buttonSize80"
				onclick="javascript:createUpdateAsset('update');"
				value="<s:text name='execue.assetDefinition.updateDatasetButton.name' />" /></span>
			<span id="updateProcess" style="display: none"><input type="button"	class="buttonLoaderSize80"
			disabled="disabled" value="<s:text name='execue.assetDefinition.updateDatasetButton.name' />" /></span>
			<span class="rightButton"><input type="button"	class="buttonSize90"  id="imageField2" 
				value="<s:text name='execue.global.resetButton.name' />"
				onclick="document.assetDefinitionForm.reset();" /> </span>
		</s:if> <s:else>
			<span id="enableCreateAsset"> <input type="button"	class="buttonSize80" id="imageField"
				onclick="javascript:createUpdateAsset('create');"
				value="<s:text name='execue.assetDefinition.createDatasetButton.name' />" /></span>
			<span id="updateProcess" style="display: none"><input type="button"	class="buttonLoaderSize80"
			disabled="disabled"	value="<s:text name='execue.assetDefinition.createDatasetButton.name' />" /></span>
			<span class="rightButton"> <input type="button"	class="buttonSize108" id="imageField2"
				value="<s:text name='execue.global.clearAllFieldsButton.name' />"
				onclick="document.assetDefinitionForm.reset();" /> </span>
		</s:else> <!--img id="imageField2" src="../images/clearAllFieldsButton.gif" /-->
		<!--
      <s:if test="asset.id != null">
      <img id="imageField3"  src="../images/analyzeAssetButton.gif" onclick="javascript:analyzeActivateAsset();"/>
      </s:if>
      --><!-- Analyze Funationality on Asset is disabled till constraints are resolved properly TODO: -RG- -->
		</td>
	</tr>
</table>
<s:hidden id="sourceDataSourceName" name="asset.dataSource.name" /> <s:hidden
	id="sourceDataSourceDescription" name="asset.dataSource.description" />
<s:if test="asset.id != null">
</s:if>
<s:else>
<s:hidden name="asset.status" />
</s:else>
<s:hidden name="asset.name" />
<s:hidden name="asset.type" />
<s:hidden name="asset.originType" />
</form>
    
    </td>
    <td width="30%" align="right" valign="top">
    
    <div id="advancedOptionsDiv" style="border:1px solid #D3D3D3;padding:3px;padding-top:5px;width:110px;height:120px;margin:5px;display:none;">
         <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0" >
		   <tr>
		     <td height="25" align="left" style="padding-left:5px;"><strong><s:text name='execue.assetDefinition.advancedOptions' /></strong></td>
	       </tr>
		   <tr>
		     <td><table width="100%" border="0" cellspacing="0" cellpadding="5">
		       <tr>
		         <td align="left"><a href="../swi/showConstraints.action"><s:text name='execue.assetDefinition.constraints' /></a></td>
	            </tr>
		       <tr>
		         <td align="left"><a id="assetGrainLink" href="javascript:showAssetsGrain();"><s:text name='execue.assetDefinition.granularity' /></a><span id="assetGrainLinkLoader" style="display:none;"><img src="../images/loaderTrans.gif" /></span></td>
	            </tr>
		       <tr>
		         <td align="left"><a id="assetDetailsLink" href="javascript:showAssetDetail();"><s:text name='execue.assetDefinition.datasetDetails' /></a><span id="assetDetailsLinkLoader" style="display:none;"><img src="../images/loaderTrans.gif" /></td>
	            </tr>
	            <tr>
		         <td align="left"><a href="../swi/showDefaultMetrics.action"><s:text name='execue.assetDefinition.metrics'/></a></td>
	            </tr>
	            
	          
	         </table></td>
	       </tr>
		   </table>
           
           </div></td>
  </tr>
</table>


<script>
$(document).ready(function() {
$("#assetNameFromAssetDefinition").select().focus();
  if (assetId != '') {
	$("#advancedOptionsDiv").show(); 
  }
});
</script>
