<!-- saved from url=(0014)about:internet -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="selectAssetDiv">

<table>
	<tr>
	  <td  width="100%" align="left" >	  
	  <s:if test="sourceURL != null"><div id="assetAnalysisReportLink"><a href='<s:property value="sourceURL"/>' class="arrowBg"><s:text name="execue.selectAsset.datasetCollectionAnalysisReport"></s:text> </a></div>
	  </s:if></td>
	</tr>
</table>

<s:form action="submitSelectedAsset" namespace="/swi" method="POST">
	<table width="300" border="0" cellspacing="0" cellpadding="0"
		align="left">

		<tr>
			<td height="35"><span style="white-space: nowrap"><s:text
				name='execue.selectAsset.selectDatasetCollection' /></span></td>
			<td align="center" style="padding-left: 3px;"><select
				id="selectAssetId" name="selectedAssetId" style="width: auto;">
				<option value="0"><s:text name="execue.selectAsset.select" /></option>
				<option value="-1"><s:text name="execue.selectAsset.newAsset" /></option>
				<s:iterator value="assets">
					<option desc="<s:property value='description'/>"
						value="<s:property value="id"/>"
						<s:if test="id == applicationContext.assetId">
							selected="selected"
						</s:if>><s:property
						value="name" /> (<s:property value="status.description" />) (<s:property
						value="publishMode.description" />)</option>
				</s:iterator>

			</select></td>
			<s:hidden name="sourceName" id="srcName" />
			<input type="hidden" name="assetName" id="assetName" value="" />
			<s:hidden name="sourceURL" />
			<td><!--label> <s:submit value="submit" /></label--></td>
		</tr>

	</table>
</s:form> <s:hidden name="selectedAssetId" /></div>
<!--div id="changeAssetDiv" style="display:none;float:left;padding-right:0px;height:35px;vertical-align:middle;" ><div style="padding-top:10px;"><a href="#" class="arrowBg" id="ChangeAssetLink" onClick="javascript:showSelectBox();" ><s:text name='execue.selectAsset.changeDataset' /></a></div></div-->
<script>
	var assetIdFromSession = '<s:property value="applicationContext.assetId"/>';
	if(assetIdFromSession){
		//$("#changeAssetDiv").show();
		$("#selectAssetDiv").show();
	}else{
	$("#changeAssetDiv").hide();
		$("#selectAssetDiv").show();	
	}
	
	$("#selectAssetId").change(function(){
	 $("#assetName").val($("#selectAssetId option:selected").text());
	$("#submitSelectedAsset").submit();
		//$("#changeAssetDiv").show();
		$("#selectAssetDiv").show();						 
	});
	
	function showSelectBox(){
		$("#changeAssetDiv").hide();
		$("#selectAssetDiv").show();	
		
	}

</script>