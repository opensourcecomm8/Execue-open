<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
div [id='memberMetaPane'] div [class='ui-state-default ui-jqgrid-hdiv']{
	display: block;
}
</style>
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	align="left">
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
	   <td colspan="2">
			<table align="center" width="60%" border="0" cellspacing="0" cellpadding="0">
			  <tr>			 
			  <s:if test="columnList.size !=0">
			   <td width="40%" align="right" valign="top">
				      <span><s:text name="execue.review.metadata.lookup.select"/>&nbsp;&nbsp;</span>
				  </td>
				  <td width="60%" align="left" valign="top">
				      <s:select name="lookupColumn" id="lookupColumn" list="columnList"
					listValue="lookupColumnName" listKey="id" onchange="javascript:reloadMembers();"/>
				  </td>
			  </s:if>
			  <s:else>
			     <td width="60%" align="left" valign="top">
				     <s:text name="execue.lookup.column.unavilable"></s:text>
				  </td>
			  </s:else>				 
			  </tr>
			</table>
	   </td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2"><div id="membererrorMessage" align="center" style="color: red"><s:property
				value="%{#updationValidationErrors}" /></div>
			<div id="memberactionMessage" align="center" style="color: green"></div></td>
	</tr>
	<tr>
		<td colspan="2" align="center" valign="top">
		<TABLE id="memberEvalGrid" style="font-weight: normal;"></TABLE>
		<DIV id="memberEvalGridPager"></DIV>
		</td>
	</tr>
</table>
<div id="memberpagerDialogDiv" style="display:hidden;">It seems your leaving the page without saving your changes. Do you want to save them now?</div>

<SCRIPT type=text/javascript>
var assetId = '<c:out value="${assetId}"/>';
var lookupTableId = $("#lookupColumn").val();
var isMemberDataChanged = false;
var gridMemberUrl = "showMemberEvaluationData.action?assetId="+assetId+"&tableId="+lookupTableId;
// declaration of custom pagination objects
var totalMemRecords = 0;
var currentMemPage = 0;
var recordMemCount = 10;
var lastMemReqPage = 0;
// end of cutom pagination
// start of dialog binders
$("#memberpagerDialogDiv").dialog({
	autoOpen: false,
	resizable: false,
	closeText: 'hide',	
	modal: true,
	height: 140,
	width: 330,
	title: 'Warning'
});


$(document).ready(function() {
if(lookupTableId){
	$('#memberEvalGrid').jqGrid({
	  url: gridMemberUrl,
      datatype: 'json',
      mtype: 'GET',
      width: 500,
	  height: "auto",
	  rowNum: recordMemCount,	  
      pager: '#memberEvalGridPager',      
      viewrecords: true,
	  gridview: true,
	  colNames: ['ID', 'Lookup Value', 'Lookup Description', 'instanceExist', 'parentConceptId','optionalMemberId'],
	  colModel: [{name:'id', index:'id', hidden:true, search:false, sortable:false},
	  			 {name:'name', index:'name', width:50, search:false, sortable:false},
	  			 {name:'description', index:'description', width:200, search:false, sortable:false, formatter:descTextArea},
	  			 {name:'instanceExist', index:'instanceExist', hidden:true, search:false, sortable:false},
	  			 {name:'optionalMemberId', index:'optionalMemberId', hidden:true, search:false, sortable:false,formatter:inputName},
	  			 {name:'parentConceptId', index:'parentConceptId', hidden:true, search:false, sortable:false, formatter:inputName}],
	jsonReader: {
    	repeatitems: false,
    	id: "id"
	},
	prmNames : { page:"requestedPage", rows:"pageSize", sort:"sortField", order:"sortOrder", id:"id", nd:null, search:null},
	// save before paginate
	onPaging : function(pgButton) {
		$("#memberactionMessage").html("");
		var reqPage = $('#memberEvalGrid').getGridParam("page");
		if(isMemberDataChanged) {
			$("#memberpagerDialogDiv").bind("dialogopen", function(event, ui) {
				$('#memberpagerDialogDiv').dialog({
					buttons: {
							"No": function() {
								$(this).dialog("close");
								reqPage = customOnPaging(false);
								if(pgButton == "next") {
									reqPage++;
								} else if(pgButton == "prev") {
									reqPage--;
								} else if(pgButton == "last") {
									reqPage = $('#memberEvalGrid').getGridParam("lastpage");
								} else if(pgButton == "first") {
									reqPage = 1;
								}
								$('#memberEvalGrid').setGridParam({page: reqPage});
								$("#memberEvalGrid").trigger('reloadGrid');
							},
							"Yes": function() {
								$(this).dialog("close");								
							}
					}
				});
			});
			$('#memberpagerDialogDiv').dialog('open');
			customOnPaging(true);
		}	
	  	// code for custom pagination on the meta grid
		currentMemPage = reqPage;
		var viewText = getPagerText(currentMemPage);
		$('#memberEvalGrid').setGridParam({recordtext: viewText});
		// end of custom pagination 
	},
	// invoking the bind event after the grid is loaded.
	loadComplete : function() {
		$("#memberEvalGrid").bind ('change', function (event){
			isMemberDataChanged = true;
		});
		
		lastMemReqPage = $('#memberEvalGrid').getGridParam("page");
		// code for custom pagination on the meta grid
		// this code will be executed for the 1st time the grid is loaded. 
		// Rest of the part will be taken care of in onPaging method.
		totalMemRecords = $('#memberEvalGrid').getGridParam("records");
		currentMemPage = $('#memberEvalGrid').getGridParam("page");
		if(currentMemPage == 1){
			var startCount = 1;
			var endCount = recordMemCount;
			if(endCount > totalMemRecords)
				endCount = totalMemRecords;
			var viewText = "View "+startCount+" - "+endCount+" of "+totalMemRecords;			
			$("#memberEvalGridPager td[id='memberEvalGridPager_right'] div[class='ui-paging-info']").html(viewText);
		}
		// end of custom pagination
		
	}
	});
	$("#memberEvalGrid").jqGrid('navGrid',"#memberEvalGridPager",{edit:false,add:false,del:false,search:false,refresh:false})
	.navButtonAdd('#memberEvalGridPager',{
					caption:"Confirm Changes", 
   					buttonicon:"ui-icon-disk", 
   					onClickButton: function(){
   						//var confirmed = confirm("Are you sure to save the data?");
   						//if(confirmed){
   							// the below transformation is achieved by including jquery.execue.UIPublishedFileTable.js file. See for more details.
   							var jsonObjAsArray = $('#memberEvalGrid').saveColumnToJSON();
   							$.post("updateEvaluatedMembers.action",{evaluatedMemberList:$.toJSON(jsonObjAsArray)},function(data,status) {
   								if(data == "true"){
   									$("#memberactionMessage").html("Records updated successfully");
   									setTimeout(function(){$("#memberactionMessage").empty();}, 3000);
   								} else {
   									$("#membererrorMessage").html("Error in updation. Please try again later.");
   									setTimeout(function(){$("#membererrorMessage").empty();}, 3000);
   								}
							});
						//}
   					},
   					position:"last"})
   	.navSeparatorAdd("#pager",{});	
   	}
});

function inputName (cellvalue, options, rowObject) {
	$inputDiv = $("<div></div>");
	$input = $("<input id='"+rowObject.id+"' value='"+cellvalue+"' disabled />");
	$inputDiv.append($input);
	return $inputDiv.html();
}

function descTextArea (cellvalue, options, rowObject) {
	$inputDiv = $("<div></div>");
	$input = $("<input id='"+rowObject.id+"_description' value='"+cellvalue+"' size='60' />");
	$inputDiv.append($input);
	return $inputDiv.html();
}

function reloadMembers () {
	lookupTableId = $("#lookupColumn :selected").val();
	var lastIndex = gridMemberUrl.indexOf('tableId') + 8;
	gridMemberUrl = gridMemberUrl.substring(0, lastIndex) + lookupTableId;
	$('#memberEvalGrid').setGridParam({url: gridMemberUrl});
	$("#memberEvalGrid").trigger("reloadGrid");
}

function clearMsgs() {
	$("#memberactionMessage").html("");
}

function resetGridState(){
	// setting the grid to it's defaul state if the user doesn't wishes to save data.
	isMemberDataChanged = false;
	$('#memberEvalGrid').setGridParam({url: gridMemberUrl});
	clearMsgs();
}

function getPagerText (currentMemPage) {
	var viewText = "";
	if(currentMemPage > 1) {
		var startCount = ((recordMemCount * currentMemPage) - recordMemCount) + 1;
		var endCount = (recordMemCount * currentMemPage);
		if(endCount > totalMemRecords)
			endCount = totalMemRecords;
		viewText = "View "+startCount+" - "+endCount+" of "+totalMemRecords;	
	}
	return viewText
}

function customOnPaging (confirmed) {
	var reqsPage = $('#memberEvalGrid').getGridParam("page");
	
	if(confirmed){
		// resetting the url so that server returns nothing as this event is non-revokable.
		$('#memberEvalGrid').setGridParam({url: "#"});
		$('#memberEvalGrid').setGridParam({page:lastMemReqPage});
		reqsPage = lastMemReqPage;
	} else {
		resetGridState();
	}
	return reqsPage;
}
</script>