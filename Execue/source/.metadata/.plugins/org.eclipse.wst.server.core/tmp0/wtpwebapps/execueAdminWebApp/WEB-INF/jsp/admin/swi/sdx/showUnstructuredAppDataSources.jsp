<%@taglib uri="/struts-tags" prefix="s"%>
<table width="100%" border="0" cellspacing="0" cellpadding="2">
<tr>
		<td >
		<div id="errorMessage" style="color: red"><s:actionerror /><s:fielderror /></div>
		<div id="actionMessage" style="color: green"><s:actionmessage /></div>
		</td>
	</tr>
</table>

<table width="70%">
<tr>
	<td height="25"  ><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="right">Select Datasource :  </td>
    <td align="left"><select
				id="selectedUnstructuredDataSourceId" name="selectedUnstructuredDataSourceForApp" style="width: 200px;">				
				<s:iterator value="unstructuredDataSources">
					<option 
						value="<s:property value="id"/>"
						<s:if test="id == unstructuredDataSourceForApp.id">
							selected="selected"
						</s:if>><s:property
						value="displayName" /> </option>
				</s:iterator>

			</select></td>
  </tr>
  <tr><td>&nbsp;&nbsp;&nbsp; </td></tr>
  <tr>
    <td align="right">Select SOLR Datasource :  </td>
    <td align="left"><select
				id="selectedUnstructuredSOLRDataSourceId" name="selectedUnstructuredSOLRDataSourceForApp" style="width: 200px;">				
				<s:iterator value="unstructuredSOLRDataSources">
					<option 
						value="<s:property value="id"/>"
						<s:if test="id == unstructuredSOLRDataForApp.id">
							selected="selected"
						</s:if>><s:property
						value="displayName" /> </option>
				</s:iterator>

			</select></td>
  </tr>
</table>
</td>
</tr>
<tr><td>

<form id="unstructuredAppsForm">
<table width="30%" border="0" align="center" cellpadding="3"
	cellspacing="0">
    
    <tr>
		<td width="41%" align="right" class="fieldNames">&nbsp;</td>
		<td width="3%">&nbsp;</td>	
	</tr>
    
    
	<tr>
		<td width="45%" align="center"><strong><span class="cellTExtCenter">
		<s:text name="execue.manageUnstructuredApps.leftlist.heading" />
		</span></strong></td>
		<td width="8%" align="center">&nbsp;</td>
		<td width="47%" align="center"><strong><span class="cellTExtCenter">
		  <s:text
			name="execue.manageUnstructuredApps.rightlist.heading" />
		  </span></strong></td>
		<td width="11%" align="center" class="fieldNames">&nbsp;
		
		</td>
	</tr>

	
	<tr>
	<tr>
		<td align="center">
            <select multiple="multiple" id="unstructuredContentAggregatorId" style="width:200px;" size="7" name="">
            <s:iterator value="unstructuredContentAggregators">
               <option value="<s:property value='id'/>"><s:property value="displayName"/></option>
            </s:iterator>

</select>
            </td>
		<td align="center">
		<table width="100%" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td><a href="#" id="&gt;&gt"><img
					src="../images/admin/rightAllArrowButton.gif" width="29" height="22"
					alt="Move All to right" border="0" title="Move All to right" /></a></td>
			</tr>
			<tr>
				<td><a href="#" id="&gt"><img
					src="../images/admin/rightArrowButton.gif" width="29" height="22"
					alt="Move to right" border="0" title="Move to right" /></a></td>
			</tr>
			<tr>
				<td><a href="#" id="&lt"><img
					src="../images/admin/leftArrowButton.gif" width="29" height="22"
					alt="Move to left" border="0" title="Move to left" /></a></td>
			</tr>
			<tr>
				<td><a href="#" id="&lt;&lt"><img
					src="../images/leftAllArrowButton.gif" width="29" height="22"
					alt="Move All to left" border="0" title="Move All to left " /></a></td>
			</tr>
		</table>
		</td>
		<td align="center">
            
            <select style="width:200px;" multiple="multiple" id="unstructuredAppContentAggregatorId" size="7" name="selectedUnstructuredContentAggregatorsForApp">
             <s:iterator value="unstructuredContentAggregatorsForApp">
                 <option value="<s:property value='id'/>"><s:property value="displayName"/></option>
            </s:iterator>
           </select>
            </td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>

</table>
</form></td></tr>
<tr>
	<td height="35" colspan="3" align="center"><input type="button" class="buttonSize108"
		name="save" id="save"
		value="Save Changes" /> 
        
        <input type="button" class="buttonSize90" name="imageField2"
		id="resetId" value="Reset" /></td>
</tr>
</table>
<script>


$("#closeInnerPaneLink").show();
$("#addNewUserLink").hide();
$().ready(function(){
  	$("select[name='selectedUnstructuredContentAggregatorsForApp'] option").attr("selected" ,"true"); 	
  });

$("a").click(function(){
var inputControl=this.id;//$("id").val();
Move(inputControl);
});
function Move(inputControl)
{ 
  var from, to;
  var bAll = false;
  var left = document.getElementById("unstructuredContentAggregatorId");
  var right = document.getElementById("unstructuredAppContentAggregatorId"); 
 
  switch (inputControl)
  {
  case '<<':
    bAll = true; 
  case '<':
    from = right; to = left;
    break;
  case '>>':
    bAll = true;
    $("select[name='selectedUnstructuredContentAggregatorsForApp'] option").attr("selected" ,"true"); 
    // Fall through
  case '>':
    from = left; to = right;
    $("select[name='selectedUnstructuredContentAggregatorsForApp'] option").attr("selected" ,"true"); 
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
  
$("#save").click(function(){
 var dataSource=$("#selectedUnstructuredDataSourceId").val();
    $("select[name='selectedUnstructuredContentAggregatorsForApp'] option").attr("selected" ,"true");
  	 var right = document.getElementById("unstructuredAppContentAggregatorId");
  	 count=0;
  	 var groups = new Array(right.length);  
  	 if(right.length==0){	  
	  	 alert("App Content Aggregator can not be empty");
            return false;   
  	 }else{
		 $(this).removeClass("buttonSize108");	
 		$(this).addClass("buttonLoaderSize108");
  	 	var req="selectedUnstructuredDataSourceForApp="+$("#selectedUnstructuredDataSourceId").val();
  	 	req=req+"&selectedUnstructuredSOLRDataSourceForApp="+$("#selectedUnstructuredSOLRDataSourceId").val();  	 	
 		req=req+"&applicationId=<s:property value='applicationId'/>";
 		$.each(  $("select[name='selectedUnstructuredContentAggregatorsForApp'] option"),function(k,v){
				req=req+"&selectedUnstructuredContentAggregatorsForApp["+k+"]="+$(this).val();
			});
			
			//alert(req);
			$.post('saveUnstructuredDatasourceAndContentAggregators.action', req, function(data) {
				$("#dynamicPane").empty();
				$("#dynamicPane").fadeIn("fast");    
				$("#dynamicPane").append(data);
				$("#dynamicPane").show();  
			}); 
	
	 }
});
$("#resetId").click(function(){	
	showUnstructuredAppDatasource("<s:property value='applicationId'/>");
});

</script>
