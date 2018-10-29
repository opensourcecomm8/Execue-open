<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div class="innerPane2" style="width: 430px; height: auto">
<div id="errorMessage" style="color: red"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage" style="color: green"><s:actionmessage /></div>
<table width="97%" border="0" align="center" cellpadding="0"
	cellspacing="3">
	<tr>
		<td width="80%" height="30" class="fieldNames"><s:text
			name="execue.asset.grain.asset.name" /> : <strong><s:label
			name="execue.asset.displayName" id="name" /></strong></td>
	</tr>
	<tr>
		<td colspan="2"><s:form namespace="/swi" method="POST"
			id="assetGrainForm">
			<TABLE BORDER=0>
				<td class="fieldNames" valign="top"><s:text name="Grain" /></td>
				<TR>
					<TD><select id="leftGrains" multiple="multiple"
						style='height: 108px; font-size: 12px; color: #525252; margin-top: 2px;'>
						<s:iterator id="existingAssetGrainId"
							value="assetGrainInfo.updatedEligibleAssetGrain" status="list">
							<option value="<s:property value='mappingId'/>"><s:property
								value="conceptDisplayName" /></option>
						</s:iterator>
					</select></TD>
					<TD VALIGN=MIDDLE ALIGN=CENTER>
					<table width="100%" border="0" cellspacing="0" cellpadding="3"
						id="grainsTable">
						<tr>
							<td><a href="#" id="rightOptGrains"><img
								src="../images/admin/rightAllArrowButton.gif" width="29" height="22"
								alt="Move All to right" border="0" title="Move All to right" /></a></td>
						</tr>
						<tr>
							<td><a href="#" id="rightOptGrain"><img
								src="../images/admin/rightArrowButton.gif" width="29" height="22"
								alt="Move to right" border="0" title="Move to right" /></a></td>
						</tr>
						<tr>
							<td><a href="#" id="leftOptGrain"><img
								src="../images/admin/leftArrowButton.gif" width="29" height="22"
								alt="Move to left" border="0" title="Move to left" /></a></td>
						</tr>
						<tr>
							<td><a href="#" id="leftOptGrains"><img
								src="../images/leftAllArrowButton.gif" width="29" height="22"
								alt="Move All to left" border="0" title="Move All to left" /></a></td>
						</tr>
					</table>
					</TD>
					<TD><select id="rightGrains" multiple="multiple"
						name="selectedAssetGrain"
						style='height: 108px; font-size: 12px; color: #525252; margin-top: 2px;'>
						<s:iterator id="existingAssetGrainId"
							value="assetGrainInfo.existingAssetGrain" status="list">
							<option selected="selected"
								value="<s:property value='mappingId'/>"><s:property
								value="conceptDisplayName" /></option>
						</s:iterator>
					</select></TD>
				</TR>
			</TABLE>
			<table width="100%" border="0" align="center" cellpadding="2"
				cellspacing="0">
				<tr>
					<td colspan="2">
					<div id="message" STYLE="color: red" align="left"></div>
					</td>
				</tr>

				<tr>
					<td class="fieldNames"><s:text name="Default Population" /></td>
					<td><s:select list="assetGrainInfo.existingAssetGrain"
						listKey="mappingId" listValue="conceptDisplayName"
						name="defaultPopulation" id="dPopulation"
						cssStyle="height: 22px; font-size: 11px; color: #525252; margin-top: 2px;">
					</s:select></td>
				</tr>
				<tr>
					<td class="fieldNames"><s:text name="Default Distribution" /></td>
					<td><s:select list="assetGrainInfo.existingAssetGrain"
						listKey="mappingId" listValue="conceptDisplayName"
						name="defaultDistribution" id="dDistribution"
						cssStyle="height: 22px; font-size: 11px; color: #525252; margin-top: 2px;">
					</s:select></td>
				</tr>
				<tr>
					<td class="fieldNames"><s:text
						name="execue.AssetGrainInfo.defaultDistributionValue" /></td>
					<td><s:textfield
						name="assetGrainInfo.defaultDistributionValue" cssClass="textBox" /></td>
				</tr>

			</table>
			<table>
				<tr>
				<tr>
					<td height="40" colspan="2" align="left"><span
						id="enableUpdateConcept"> <input type="button"
						class="buttonSize108" id="submitAssetGrains" alt="Update Grain"
						value="Update Grain" /> <span id="updateGrainLoader"
						style="display: none;"><input type="button"
						class="buttonLoaderSize108" value="Update Grain"
						disabled="disabled" /></span><span id="resetAssetGrains"><input
						type="button" class="buttonSize51"
						onclick="resetAssetGrainInfo();" value="Reset" /></span> <span
						id="resetAssetGrainsLoader" style="display: none;"><input
						type="button" class="buttonSize51" value="Reset" /></span> </span></td>
				</tr>
			</table>

			<s:hidden name="asset.id" id="assetId" />
			<s:hidden name="asset.displayName" id="displayName" />
			<s:iterator id="sourceGrain" value="assetGrainInfo.eligibleGrain"
				status="even_odd">
				<s:hidden name="mappingIds" value="%{#sourceGrain.mappingId}" />
				<s:hidden name="grainTypes" value="%{#sourceGrain.grainType.value}" />
				<s:hidden name="displayNames"
					value="%{#sourceGrain.conceptDisplayName}" />
			</s:iterator>

		</s:form></td>
	</tr>
</table>
</div>
<script>

$("table#grainsTable a").click(function(){
var inputControl=this.id;//$("id").val();
moveGrain(inputControl);

});
function moveGrain(inputControl){  
  var from, to;
  var bAll = false;
  var left = document.getElementById("leftGrains");
  var right = document.getElementById("rightGrains");
  var dPopulationSelect=$("#dPopulation");    
  var dDistributionSelect=$("#dDistribution");    
  switch (inputControl)
  {
  case 'leftOptGrains':
    bAll = true;
    // Fall through
  case 'leftOptGrain':
    from = right; to = left;
    break;
  case 'rightOptGrains':
    bAll = true;
    $("select[name='selectedAssetGrain'] option").attr("selected" ,"true"); 	
    // Fall through
  case 'rightOptGrain':
    from = left; to = right;
    $("select[name='selectedAssetGrain'] option").attr("selected" ,"true"); 	
    break;
  default:
   // alert("Check your HTML!");
   
  }
  for (var i = from.length - 1; i >= 0; i--)
  {
    var o = from.options[i];
    if (bAll || o.selected)
    {
      from.remove(i);
       optionValue=o.value;       
       if(from.id=="leftGrains"){
	       $opt=$("<option></option>");
	       dPopulationSelect.append($opt);
	       $opt.val(optionValue);
	       $opt.text(o.text);
	       $opt.attr("selected",true);
	       
	       $opt=$("<option></option>");
	       dDistributionSelect.append($opt);
	       $opt.val(optionValue);
	       $opt.text(o.text);
	       $opt.attr("selected",true);       
       }else{      			
	      $.each($("#dPopulation option"),function(i,val){			      
		      if($(this).val()==o.value){
		      $(this).remove();
		      }
	      });			   
	      $.each($("#dDistribution option"),function(i,val){
	      
		      if($(this).val()==o.value){
		      $(this).remove();
		      }
	      });       
       }
      try {
        to.add(o, null); 
      }
      catch (e){
        to.add(o); 
      }
    }
  }
  
  }
  
  
$("#submitAssetGrains").click(function(){
    $("#updateGrainLoader").show();
    $("#submitAssetGrains").hide();
    $("select[name='selectedDefaultMetrics'] option").attr("selected" ,"true");
    $("select[name='selectedAssetGrain'] option").attr("selected" ,"true");
    var updateAssetGrain="../swi/updateAssetGrain.action"; 
    //alert($('#assetGrainForm').serialize());    
    $.post(updateAssetGrain, $('#assetGrainForm').serialize(), function(data) {	
		$("#hiddenPaneContent").empty().append(data);
	}); 	
});

</script>