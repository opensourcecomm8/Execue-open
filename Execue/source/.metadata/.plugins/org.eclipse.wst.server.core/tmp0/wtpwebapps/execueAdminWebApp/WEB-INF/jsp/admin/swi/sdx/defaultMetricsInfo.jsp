<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@page import="com.execue.core.common.type.CheckType"%>
<div class="innerPane2" style="width: 430px; height: auto">
<div id="errorMessage" style="color: red"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage" style="color: green"><s:actionmessage /></div>
<table width="97%" border="0" align="left" style="margin-left: 20px;"
	cellpadding="0" cellspacing="3">
	<tr>
		<td width="80%" height="30" class="fieldNames"><s:text
			name="execue.defaultMatric.main.table.name" /> : <strong><s:label
			name="currentTable.displayName" id="name" /></strong></td>
	</tr>
	<tr>
		<td colspan="2"><s:form namespace="/swi" method="POST"
			id="defaultMetricsForm">
			<TABLE BORDER=0>
				<td class="fieldNames" valign="top"><s:text
					name="execue.defaultMatric.main.metrics" /></td>
				<TR>
					<TD><select id="leftMetrics" multiple="multiple"
						style='height: 108px; width: 250px; font-size: 12px; color: #525252; margin-top: 2px;'>
						<s:iterator id="eligibleDefaultMetric"
							value="eligibleDefaultMetrics" status="list">
							<option value="<s:property value='id'/>"
								tableId="<s:property value='tableId'/>"
								mappingId="<s:property value='mappingId'/>"
								popularity="<s:property value='popularity'/>"
								aedId="<s:property value='aedId'/>"
								valid="<s:property value='valid'/>"
								columnName="<s:property value='columnName'/>"
								conceptName="<s:property value='conceptName'/>"><s:property
								value="conceptName" />(<s:property value="columnName" />)</option>
						</s:iterator>

					</select></TD>
					<TD VALIGN=MIDDLE ALIGN=CENTER>
					<table width="100%" border="0" cellspacing="0" cellpadding="3"
						id="metricsTable">
						<tr>
							<td><a href="#" id="rightOptMetrics"><img
								src="../images/admin/rightAllArrowButton.gif" width="29" height="22"
								alt="Move All to right" border="0" title="Move All to right" /></a></td>
						</tr>
						<tr>
							<td><a href="#" id="rightOptMetric"><img
								src="../images/admin/rightArrowButton.gif" width="29" height="22"
								alt="Move to right" border="0" title="Move to right" /></a></td>
						</tr>
						<tr>
							<td><a href="#" id="leftOptMetric"><img
								src="../images/admin/leftArrowButton.gif" width="29" height="22"
								alt="Move to left" border="0" title="Move to left" /></a></td>
						</tr>
						<tr>
							<td><a href="#" id="leftOptMetrics"><img
								src="../images/leftAllArrowButton.gif" width="29" height="22"
								alt="Move All to left" border="0" title="Move All to left" /></a></td>
						</tr>
					</table>
					</TD>
					<TD><select id="rightMetrics" multiple="multiple"
						style='height: 108px; width: 250px; font-size: 12px; color: #525252; margin-top: 2px;'>
						<s:iterator id="existingDefaultMetric"
							value="existingDefaultMetrics" status="list">
							<option selected="selected" value="<s:property value='id'/>"
								tableId="<s:property value='tableId'/>"
								mappingId="<s:property value='mappingId'/>"
								popularity="<s:property value='popularity'/>"
								aedId="<s:property value='aedId'/>"
								valid="<s:property value='valid'/>"
								columnName="<s:property value='columnName'/>"
								conceptName="<s:property value='conceptName'/>"><s:property
								value="conceptName" />(<s:property value="columnName" />)</option>
						</s:iterator>
					</select></TD>
				</TR>
			</TABLE>
			<table width="100%" border="0" align="center" cellpadding="2"
				cellspacing="0">
				<tr>

				</tr>
				<tr>
					<td class="fieldNames"><s:text
						name="execue.defaultMatric.main.system.metrics" /> : <s:if
						test="currentTable.eligibleSystemDefaultMetric.value=='Y'">
						<input type="checkbox"
							name="currentTable.eligibleSystemDefaultMetric" checked="checked"
							value="<%=CheckType.YES %>" />
					</s:if> <s:else>
						<input type="checkbox"
							name="currentTable.eligibleSystemDefaultMetric"
							value="<%=CheckType.YES %>" />
					</s:else> <s:hidden name="currentTable.id" /> <s:hidden name="asset.id" />
					<s:hidden name="currentTable.displayName" /></td>

				</tr>
				<tr>
					<td class="fieldNames"><s:if
						test="invalidDefaultMetricsMessage!=null">
						 <s:property value="invalidDefaultMetricsMessage"/>.  <a href="../swi/showMappings.action"><s:text name="execue.defaultMatric.main.mapping.link"/> </a>
					</s:if> </td>

				</tr>
			</table>
			<table>
				<tr>
				<tr>
					<td height="40" colspan="2" align="left"><span
						id="enableUpdateConcept"> <input type="button"
						class="buttonSize108" id="submitDefaultMetrics" alt="update"
						value="update" /> <span id="updateGrainLoader"
						style="display: none;"><input type="button"
						class="buttonLoaderSize108" value="update" disabled="disabled" /></span>
					</span></td>
				</tr>
			</table>

		</s:form></td>
	</tr>
</table>
</div>
<script>




$("table#metricsTable a").click(function(){
var inputControl=this.id;//$("id").val();
moveMetric(inputControl);

});
  
function moveMetric(inputControl){ 

  var from, to;
  var bAll = false;
  var left = document.getElementById("leftMetrics");
  var right = document.getElementById("rightMetrics");    
  switch (inputControl)
  {
  case 'leftOptMetrics':
    bAll = true;
    // Fall through
  case 'leftOptMetric':
    from = right; to = left;
    break;
  case 'rightOptMetrics':
    bAll = true;
    $("select[name='existingDefaultMetric'] option").attr("selected" ,"true"); 	
    // Fall through
  case 'rightOptMetric':
    from = left; to = right;
    $("select[name='existingDefaultMetric'] option").attr("selected" ,"true"); 	
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
      try
      {
        to.add(o, null);  
        
      }
      catch (e)
      {
        to.add(o); 
      }
    }
  }
  }

$("#submitDefaultMetrics").click(function(){
  str="";
    $("#updateGrainLoader").show();
    $("#submitDefaultMetrics").hide();
    $("select[name='existingDefaultMetric'] option").attr("selected" ,"true");
    
   $.each($("#rightMetrics option"),function(i,v){
	   str=str+"&existingDefaultMetrics["+i+"].aedId="+$(this).attr('aedid');
	   str=str+"&existingDefaultMetrics["+i+"].id="+$(this).attr('id');
	   str=str+"&existingDefaultMetrics["+i+"].tableId="+$(this).attr('tableId');
	   str=str+"&existingDefaultMetrics["+i+"].mappingId="+$(this).attr('mappingId');
	   str=str+"&existingDefaultMetrics["+i+"].popularity="+$(this).attr('popularity');
	   str=str+"&existingDefaultMetrics["+i+"].valid="+$(this).attr('valid');
	   str=str+"&existingDefaultMetrics["+i+"].columnName="+$(this).attr('columnName');
	   str=str+"&existingDefaultMetrics["+i+"].conceptName="+$(this).attr('conceptName');
   });
   
  var defaultMetricsFormAction="updateDefaultMatric.action"; 
  var defaultMetricsForm=$('#defaultMetricsForm').serialize()+str; 
 $.post(defaultMetricsFormAction,defaultMetricsForm , function(data) {
           $("#dynamicPaneDefaultMetrics").empty().append(data);
		   $("#submitDefaultMetrics").show();
		   $("#updateGrainLoader").hide();
		   showAssetsTables(jsAssetId);
	}); 	
});

</script>
