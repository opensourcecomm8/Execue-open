<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<style>
td[aria-describedby='grid_rowHeader'] {
	background-color: #F1F2F2;
	font-weight: 100;
}

td[aria-describedby='grid_subgrid'] {
	width: 0px;
}

div [class='ui-state-default ui-jqgrid-hdiv'] {
	display: none;
}

div[id='gview_grid'] div[class='ui-jqgrid-bdiv'] {
	overflow: hidden;
}

div[class='.ui-jqgrid .ui-paging-info'] {
	margin-left: 5px;
}

/*div[id='grid_toppager'] {
	float: right;
	top: -28px;
}*/ /* Css for custom pager looking div for submit buttons */
div[id='submitDiv'] {
	border-bottom: 0 none !important;
	border-left: 0 none !important;
	border-right: 0 none !important;
	height: 25px;
	margin: 0 !important;
	overflow: hidden;
	padding: 0 !important;
	position: relative;
	white-space: nowrap;
}

div[id='submitDiv'] .ui-pager-control {
	position: relative;
}

div[id='submitDiv'] .ui-jqgrid-pager .ui-pg-button {
	cursor: pointer;
}

div[id='submitDiv'] .ui-pg-table {
	margin: 0px 6px 0px 2px;
	padding-bottom: 20px;
	position: relative;
}

div[id='submitDiv'] .ui-pg-table td {
	font-weight: normal;
	padding: 1px;
	vertical-align: middle;
}

div[id='submitDiv'] .ui-pg-button {
	height: 19px !important;
	cursor: pointer;
}

div[id='submitDiv'] .ui-pg-div {
	float: left;
	list-style: none outside none;
	padding: 1px 0;
	position: relative;
}

div[id='submitDiv'] .ui-pg-div span.ui-icon {
	float: left;
	margin: 0 2px;
}

div[id='submitDiv'] .ui-pg-button span {
	display: block;
	float: left;
	margin: 1px;
}

div[id='submitDiv'] .ui-separator {
	border-left: 1px solid #CCCCCC;
	border-right: 1px solid #CCCCCC;
	height: 18px;
}

div[id='disableGrid'] {
	background-color: lightGrey;
	border: 1px solid #CCCCCC;
	color: #000000;
	height: 328px;
	opacity: 0.4;
	outline: medium none;
	position: absolute;
	top: 240px;
	width: 930px;
	z-index: 1000000;
}

div[id='disableGrid'] span {
	font-size: 20pt;
	font-weight: bold;
	left: 350px;
	position: absolute;
	text-align: center;
	top: 150px;
}

#gbox_grid,#submitDiv {
	top: -30px;
	width: 933px;
}

.ui-jqgrid td input[type='checkbox'] {
	margin: 0 0 0 80px;
}
</style>
<div class="innerPane" style="width: 99%; margin-top: 25px; height: 380px;" id="evalCollist">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<thead>
		<tr>
			<td colspan="2">
			<div id="actionMessage" align="center" style="color: green; top: -30px; position: relative;"></div>
			<div id="errorMessage" align="center" style="color: red; top: -30px; position: relative;"></div>
			</td>
		</tr>
	</thead>
	<tr>
		<td colspan="2" style="height: 340px; width: 880px;">
		<div id="disableGrid" style='display: none;'><span>Processing ...</span></div>
		<TABLE id="grid"></TABLE>
		<div id="pager"></div>
		<div id="submitDiv" stlye="width: 100%;" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
		<div class="ui-pager-control">
		<table cellspacing="0" cellpadding="0" border="0" style="width: 100%; table-layout: fixed;" class="ui-pg-table">
			<tbody>
				<tr>
					<td align="left" id="pager_left" style="width: 155px;">
					<table cellspacing="0" cellpadding="0" border="0" style="float: left; table-layout: auto;"
						class="ui-pg-table navtable">
						<tbody>
							<tr>
								<td class="ui-pg-button ui-corner-all ui-state-hover">
								<div class="ui-pg-div" style="font-weight: bold" onclick="javascript:confirmSubmit();"><span
									class="ui-icon ui-icon-disk"></span><s:text name="execue.review.metadata.confirm.metadata" /></div>
								</td>
								<td style="width: 4px;" class="ui-pg-button ui-state-disabled"><span class="ui-separator"></span></td>
							</tr>
						</tbody>
					</table>
					</td>
					<td align="justify" id="pager_center">
					<table style="margin-bottom: 15px;">
						<tr>
							<td class="ui-pg-button ui-state-disabled">
							<div id="absorbtionDiv" style="display: none;"></div>
							</td>
						</tr>
					</table>
					</td>
					<td align="right" id="pager_right" style="width: 130px;">
					<table cellspacing="0" cellpadding="0" border="0" style="float: right; table-layout: auto;"
						class="ui-pg-table navtable">
						<tbody>
							<tr>
								<td style="width: 4px;" class="ui-pg-button ui-state-disabled"><span class="ui-separator"></span></td>
								<td class="ui-pg-button ui-corner-all ui-state-hover" style="font-weight: bold" id="absorbButton"><s:if
									test='infoSource == "DC"'>
									<div class="ui-pg-div" onclick="javascript:window.location='../swi/showPublishDatasets.action';"><span
										class="ui-icon ui-icon-arrowreturnthick-1-e"></span><s:text name="execue.review.metadata.publish.app" /></div>
								</s:if><s:else>
									<div class="ui-pg-div" id="absorbDialogLink"><span class="ui-icon ui-icon-arrowreturnthick-1-e"></span><s:text
										name="execue.review.metadata.absorb.app" /></div>
								</s:else></td>
							</tr>
						</tbody>
					</table>
					</td>
				</tr>
			</tbody>
		</table>
		</div>
		</div>
		</td>
	</tr>
</table>
</div>
<div id="pagerDialogDiv" style="display: hidden;">It seems your leaving the page without saving your changes. Do
you want to save them now?</div>
<div id="absorbDialogDiv" style="display: hidden;">Do you want to absorb the Dataset?</div>
<div id="pagerErrorDialogDiv" style="display: hidden;"><span style="color: red; font-weight: bold;">Due to
system-compatibility errors, changes done by you are not saved yet. </span><br />
<br />
Note: If you choose "No" all your unsaved changes will be reverted and absorbtion process will start. <br />
<br />
Do you wish to correct and save them now?</div>
<script>
var colSize  = 6; // this is a customizable option to decide the number of columns to show on grid
var counter = 0;
var colCounter = 0; // this will be used to save each cell value maxvalue = 5
var colCountArray = []; // this will be used mostly for validation purpose
var isDataChanged = false;
var isNotSavedDueToErrors = false;
var types = []; // array for qiConversion types.
// declaration of custom pagination objects
var totalRecords = 0;
var currentPage = 0;
var recordCount = colSize-1;
var lastReqPage = 0;
// end of cutom pagination
// action variables
var fileTableId = '<c:out value="${fileTableId}"/>';
var fileId = '<c:out value="${publishedFileId}"/>';
var assetId = '<c:out value="${assetId}"/>';
var tableId = '<c:out value="${tableId}"/>';
var infoSource = '<c:out value="${infoSource}"/>';
var gridURL = "showEvalutedColumnDetails.action?fileTableId="+fileTableId+"&assetId="+assetId+"&tableId="+tableId+"&infoSource="+infoSource;

// dynamic header code
var headerString = "";
for(var i = 0; i < colSize; i++)
	headerString = headerString + " ~";

var colNamesArray = headerString.substring(0,(headerString.length - 1)).split("~");
var subColNamesArray = headerString.substring(0,(headerString.length - 1)).split("~");
var colModelArray = [];
var subColModelArray = [];

var gridWidth = 930;
if(colSize > 10)
	gridWidth = 930;
else if(colSize <= 3)
	gridWidth = 930;
else if(colSize <= 6)
	gridWidth = 930;
else
	gridWidth = 930;
var colWidth = (gridWidth/colSize)-6;
var modelColWidth = colWidth;
var firstColWidth = modelColWidth - 65;
$.each( colNamesArray, function(k,v) {
	if(k == 0){
		modelColWidth = firstColWidth;
		colModelArray[k]= {name:"rowHeader", index:'rowHeader', width:modelColWidth, search:false, sortable:false, title:false, fixed:true, resizable: true};
	} else {
		modelColWidth = colWidth + 15;		
		colModelArray[k]= {name:colNamesArray[k], index:'name', width:modelColWidth, search:false, sortable:false, title:false, formatter:'showUIObject', fixed:true, resizable: true};
		subColModelArray[k-1] = {name:colNamesArray[k], index:'name', width:modelColWidth, search:false, sortable:false, title:true, fixed:true, resizable: true};
	}
});
var inputSize = modelColWidth - 134; // size of the field name textbox
// resetting the subGrid colModel and colNames array with removing 0th element
subColNamesArray = subColNamesArray.splice(1,subColNamesArray.length);
// end of dynamic header code

// start of dialog binders
$("#pagerDialogDiv").dialog({
	autoOpen: false,
	resizable: false,
	closeText: 'hide',	
	modal: true,
	height: 140,
	width: 330,
	title: 'Warning'
});

$("#pagerErrorDialogDiv").dialog({
	autoOpen: false,
	resizable: false,
	closeText: 'hide',
	modal: true,
	height: 190,
	width: 450,
	title: 'Warning'
});

$("#absorbDialogDiv").dialog({
	autoOpen: false,
	closeText: 'hide',
	resizable: false,	
	modal: true,
	height: 120,
	width: 330,
	//dialogClass: 'ui-state-highlight',
	title: 'Alert',
	buttons: {
			"No": function() {
				$(this).dialog("close");					
			},
			"Yes": function() {
				$(this).dialog("close");
				if(isDataChanged) {
					// check for the changes done by user are supported or not
					saveBeforeAbsorb();
				} else {
					submitForAbsorption();
				}							
			}
	}
});
// end of dialogs

$(document).ready(function() {
	$("#absorbDialogLink").click (function() {
		$('#absorbDialogDiv').dialog('open');
		return false;
	});
	
	$('#grid').jqGrid({
	  url: gridURL,
      datatype: 'json',
      mtype: 'GET',
	  width: gridWidth,
	  height: 'auto',
	  pginput: false,
	  pagerpos: 'right',
	  recordpos: 'left',
	  hidegrid: false,
      hiddengrid: false,
      scrollOffset: 0,
	  caption: 'Metadata',
      pager: '#pager',
      pginput: false,
  	  pagerpos: 'right',
  	  recordpos: 'left',
      viewrecords: true,
      gridview: false,
      subGrid: true,
	  colNames: colNamesArray,
	  colModel: colModelArray,
	jsonReader: {
		repeatitems: false,
	    root: "rows",
	    id: "id"
	},
	prmNames : { page:"requestedPage", rows:"pageSize", sort:"sortField", order:"sortOrder", id:"id", nd:null, search:null},	
	// save before paginate
	onPaging : function(pgButton) {
		$("#actionMessage").html("");
		var reqPage = $('#grid').getGridParam("page");
		if(isDataChanged) {
			$("#pagerDialogDiv").bind("dialogopen", function(event, ui) {
				$('#pagerDialogDiv').dialog({
					buttons: {
							"No": function() {
								$(this).dialog("close");
								reqPage = customOnPaging(false);
								if(pgButton == "next") {
									reqPage++;
								} else if(pgButton == "prev") {
									reqPage--;
								} else if(pgButton == "last") {
									reqPage = $('#grid').getGridParam("lastpage");
								} else if(pgButton == "first") {
									reqPage = 1;
								}
								$('#grid').setGridParam({page: reqPage});
								$("#grid").trigger('reloadGrid');
							},
							"Yes": function() {
								$(this).dialog("close");								
							}// Yes denotes to stop the user navigation and stay on current page.
					}
				});
			});
			$('#pagerDialogDiv').dialog('open');
			customOnPaging(true);
		}	
	  	// code for custom pagination on the meta grid
		currentPage = reqPage;
		var viewText = getPagerText(currentPage);
		$('#grid').setGridParam({recordtext: viewText});
		// end of custom pagination 
	},
	// invoking the bind event after the grid is loaded.
	loadComplete : function() {
		$("#grid").bind ('change', function (event){
			isDataChanged = true;
		});
		lastReqPage = $('#grid').getGridParam("page");
		// code for custom pagination on the meta grid
		// this code will be executed for the 1st time the grid is loaded. 
		// Rest of the part will be taken care of in onPaging method.
		totalRecords = $('#grid').getGridParam("records");
		currentPage = $('#grid').getGridParam("page");
		if(currentPage == 1){
			var startCount = 1;
			var endCount = recordCount;
			if(endCount > totalRecords)
				endCount = totalRecords;
			var viewText = "View "+startCount+" - "+endCount+" of "+totalRecords+" Fields";			
			$("#pager td[id='pager_left'] div[class='ui-paging-info']").html(viewText);
		}
		// end of custom pagination
		
		clearMsgs();
		// setting pager display properties
		$("#gbox_grid div[id='pager']").attr('style','height:19px;position:absolute;top:-1px;left:684px;z-index:1000');
		$("#pager div[id='pg_pager']").attr('style','position:relative;width:245px;');
		$("#pager td[id='pager_left'] div[class='ui-paging-info']").attr('style','margin-left: 3px;');
		// end of pager props
		$("#grid").expandSubGridRow(6);
		$("#grid td[aria-describedby='grid_subgrid']").remove();
		$("#grid td[class='ui-widget-content subgrid-cell']").remove();
		$("#grid tr[id='5'] select").each(function(index,data){updateMetricAndGrain(data, index);});
	},
	// code for sub grid
	subGridRowExpanded: function(subgrid_id, row_id) {
       var subgrid_table_id, pager_id;
       subgrid_table_id = subgrid_id+"_t";
       pager_id = "p_"+subgrid_table_id;
       var currentPage = $("#grid").getGridParam('page');
       $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>"); 
       $("#"+subgrid_table_id).jqGrid({
          url:'showColumnData.action?fileTableId='+fileTableId+'&fileId='+fileId+"&currentPage="+currentPage+"&assetId="+assetId+"&tableId="+tableId+"&infoSource="+infoSource,
          datatype: 'json',
      	  mtype: 'GET',
	      width: gridWidth-4,
	      height: 150,          
          hidegrid: false,
          hiddengrid: false,
          caption: 'Sample Data',
          gridview: true,
          viewrecords: true,
      	  rownumbers: true,
      	  rowNum: 7,
          rownumWidth: firstColWidth-3,
          pager: pager_id,
          scrollOffset: 0,
          /*toppager: true,*/
          pginput: false,
	  	  pagerpos: 'right',
	  	  recordpos: 'left',
          colNames: subColNamesArray,
	  	  colModel: subColModelArray,
	  	jsonReader: {
		    repeatitems: true,
    		cell: "cells"
		},
		prmNames : { page:"requestedPage", rows:"pageSize", sort:null, order:null, id:"id", nd:null, search:null},
		loadComplete : function() {
			//var gridPagerHtml = $("#p_grid_6_t").html();
			$("#p_grid_6_t").attr('style','height:19px;position:absolute;top:-1px;left:680px;z-index:1000');
			$("#pg_p_grid_6_t").attr('style','position:relative;width:245px;');
			$("#p_grid_6_t_left div[class='ui-paging-info']").attr('style','margin-left: 3px;');
			$("#pg_p_grid_6_t span[class='ui-icon ui-icon-seek-first']").attr('class', 'ui-icon ui-icon-arrowthickstop-1-n');
			$("#pg_p_grid_6_t span[class='ui-icon ui-icon-seek-end']").attr('class', 'ui-icon ui-icon-arrowthickstop-1-s');
			$("#pg_p_grid_6_t span[class='ui-icon ui-icon-seek-prev']").attr('class', 'ui-icon ui-icon-arrowthick-1-n');
			$("#pg_p_grid_6_t span[class='ui-icon ui-icon-seek-next']").attr('class', 'ui-icon ui-icon-arrowthick-1-s');
			
			//$("#grid_toppager div[class='.ui-jqgrid .ui-paging-info']").attr('style','margin-left: 3px;');
		}
       });
       //$("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false,search:false,refresh:false})
	},
	subGridRowColapsed: function(subgrid_id, row_id) {
		// this function is called before removing the data 
		var subgrid_table_id; 
		subgrid_table_id = subgrid_id+"_t"; 
		$("#"+subgrid_table_id).remove(); 
	} // end of sub grid code
	});
	
	//$('#grid').jqGrid('navGrid','#pager', {edit:false,add:false,del:false,search:false,refresh:false})// options
					
			/*.navButtonAdd('#pager',{
					caption:"<s:text name='execue.global.confirmColumnDetails' />", 
   					buttonicon:"ui-icon-disk", 
   					onClickButton: function(){
   						var confirmed = confirm("Are you sure to save the data?");
   						if(confirmed){
   							if(validateGrid()){
	   							// the below transformation is achieved by including jquery.execue.UIPublishedFileTable.js file. See for more details.
	   							var jsonObjAsArray = $('#grid').gridToJSON();
	   							$.post("updateEvaluatedColumns.action",{fileTableId:fileTableId, evaluatedColumnList:$.toJSON(jsonObjAsArray), assetId:assetId, tableId:tableId, infoSource:infoSource},function(data,status) {
	   								if(data.length > 0){
	   									//$("#errorMessage").html("Error in updation.");
	   								} else {
	   									$("#actionMessage").html("Records updated successfully");	   									
	   								}
								});
								resetGridState();
							} else {
								$("#errorMessage").html("Error in Field Name for column(s) # "+colCountArray
								+"<br/> Please make sure that name:"
								+"<br/> -Has atleast 1 character"
								+"<br/> -Is not just space characters");
							}
						}
   					},
   					position:"last"})
   			.navSeparatorAdd("#pager",{})*/;

// extending the showUIObject as an internal function to jqGrid.
$.extend($.fn.fmatter , {
	showUIObject : function (cellvalue, options, rowObject) {
		// init the col counter before calling the function
		if(colCounter >= (colSize - 1)){
			colCounter = 0;
		}
		cellvalue = rowObject.cols[colCounter].columnInfo;
		if(rowObject.id == 1){
			if(cellvalue == null){
				colCounter++;
				return "";
			} else
				return columnName (cellvalue, options, rowObject);
		} else if(rowObject.id == 2){
			cellvalue = rowObject.cols[colCounter].conversionTypeColumnInfo;
			if(cellvalue == null){
				colCounter++;
				return "";
			} else
				return conversionTypeSelect (cellvalue, options, rowObject);
		} else if(rowObject.id == 3){
			if(cellvalue == ""){
				colCounter++;
				return "";
			} else
				return formatSelect (cellvalue, options, rowObject);
		} else if(rowObject.id == 4){
			if(cellvalue == ""){
				colCounter++;
				return "";
			} else
				return unitSelect (cellvalue, options, rowObject);
		} else if(rowObject.id == 5){
			if(cellvalue == null){
				colCounter++;
				return "";
			} else
				return kdxDataTypeSelect (cellvalue, options, rowObject);
		} else if(rowObject.id == 6){
			if(cellvalue == null){
				colCounter++;
				return "";
			} else
				return grainSelect (cellvalue, options, rowObject);
		}
	}
});

// uiobject creation functions
function columnName (cellvalue, options, rowObject) {
	$inputDiv = $("<div></div>");
	$input = $("<input id='"+rowObject.id+"_columnName_"+colCounter+"' type='text' value='"+cellvalue+"' size='"+inputSize+"'/>");
	$inputDiv.append($input);
	colCounter++;
	return $inputDiv.html();
}

function kdxDataTypeSelect (cellvalue, options, rowObject) {
	$selectDiv = $("<div></div>");
	$select = $("<select style='width:"+modelColWidth+"px;' id='"+rowObject.id+"_kdxDataType_"+colCounter+"' onchange='javascript:updateMetricAndGrain(this,"+colCounter+");'></select>");
	var optionsArray = '<s:iterator value="columnTypesForFactTableColumnsTab" id="data"><s:property value="%{#data}" />~<s:property value="%{#data.description}" />;</s:iterator>'.split(';');
	$select = iterateOverCustomEnumString(cellvalue, optionsArray);
	$selectDiv.append($select);
	return $selectDiv.html();
}

function conversionTypeSelect (cellvalue, options, rowObject) {
	types[colCounter] = eval(cellvalue);
	var qiType = types[colCounter];
	$selectDiv = $("<div></div>");
	$select = $("<select style='width:"+modelColWidth+"px;' id='"+rowObject.id+"_conversionType_"+colCounter+"' onchange='javascript:reloadConversionType(\""+rowObject.id+"_conversionType_"+colCounter+"\","+colCounter+");'></select>");
	var optionsArray = '<s:iterator value="conversionTypes" id="data"><s:property value="%{#data}" />~<s:property value="%{#data.description}" />;</s:iterator>'.split(';');
	$select = iterateOverCustomEnumString(cellvalue.conversionType, optionsArray);
	$selectDiv.append($select);
	createHiddenInputs(''+rowObject.id+'_conversionType_'+(colCounter-1)+'',qiType);	
	return $selectDiv.html();
}

function createHiddenInputs (id, types) {
	//var $dataType = $("<input id='"+id+"_dataType' type='hidden' value='"+types.dataType+"' />");
	//var $precision = $("<input id='"+id+"_precision' type='hidden' value='"+types.precision+"' />");
	//var $scale = $("<input id='"+id+"_scale' type='hidden' value='"+types.scale+"' />");
	var $conceptId = $("<input id='"+id+"_conceptId' type='hidden' value='"+types.conceptId+"' />");
	var $conceptExist = $("<input id='"+id+"_conceptExist' type='hidden' value='"+types.conceptExist+"' />");
	var $colId = $("<input id='"+id+"_publishedFileTableDetailId' type='hidden' value='"+types.publishedFileTableDetailId+"' />");
	$selectDiv.append($colId,$conceptExist,$conceptId);
}

function formatSelect (cellvalue, options, rowObject) {
	$selectDiv = $("<div></div>");
	if(types[colCounter]!= undefined){
		$select = $("<select style='width:"+modelColWidth+"px;' id='"+rowObject.id+"_format_"+colCounter+"'></select>");
		var optionsArray = '';
		var conversionString = '';
		$.each(types[colCounter].qiConversion.dataFormats, function (index, data) {
			conversionString += data.name + "~" + data.displayName + ";";
		});
		optionsArray = conversionString.split(";");
		$select = iterateOverCustomEnumString(cellvalue, optionsArray);
		$selectDiv.append($select);
	} else {
		colCounter++;
	}
	return $selectDiv.html();
}

function unitSelect (cellvalue, id, rowObject) {
	$selectDiv = $("<div></div>");
	if(types[colCounter]!= undefined){
		$select = $("<select style='width:"+modelColWidth+"px;' id='"+rowObject.id+"_unit_"+colCounter+"'></select>");
		var optionsArray = '';
		var conversionString = '';
		$.each(types[colCounter].qiConversion.units, function (index, data) {
			conversionString += data.name + "~" + data.displayName + ";";
		});
		optionsArray = conversionString.split(";");
		$select = iterateOverCustomEnumString(cellvalue, optionsArray);
		$selectDiv.append($select);
	} else {
		colCounter++;
	}
	return $selectDiv.html();
}

function grainSelect (cellvalue, options, rowObject) {
	$chkDiv = $("<div></div>");
	var $chk = $("<input type='checkbox' checked id='"+rowObject.id+"_granularity_"+colCounter+"'></input>");
	var optionsArray = '<s:iterator value="granularityTypes" id="data"><s:property value="%{#data}" />~<s:property value="%{#data.description}" />;</s:iterator>'.split(';'); 
	 $chk.attr("value","GRAIN"); 
	if(cellvalue=="NA"){
		$chk.removeAttr("checked");
		$chk.attr("checked",false);
	}else{ $chk.attr("checked",true); }
	//$select = iterateOverCustomEnumString(cellvalue, optionsArray);
	$chkDiv.append($chk);
	colCounter++;
	return $chkDiv.html();
}
// end of uiobject creation functions
function iterateOverCustomEnumString (cellvalue, optionsArray) {
	$.each(optionsArray, function(index, data){
		var option = data.split("~");
		var selectOption = "";
		if(cellvalue == option[0])
			selectOption = "<option value='"+option[0]+"' selected>"+option[1]+"</option>";
		else if (option[0].length > 0)
			selectOption = "<option value='"+option[0]+"'>"+option[1]+"</option>";
		$select.append(selectOption);	
	});
	colCounter++;
	return $select;
}

$("#absorbEvaluatedDataLink").show();
});

function reloadConversionType(id, columCounter){
	var jsonObj = $("#"+id).convertToJSON();
	$.getJSON("getConversionTypeJSON.action", {conversionTypeStr:$.toJSON(jsonObj)}, function (data, status){
		if(status == "success") {
			types[columCounter] = eval(data);
			var formatOptions = "";
			var unitOptions = "";
			$.each(data.qiConversion.dataFormats, function (index, data) {
				formatOptions += '<option value="' + data.name + '">' + data.displayName + '</option>';
			});
			$.each(data.qiConversion.units, function (index, data) {
				unitOptions += '<option value="' + data.name + '">' + data.displayName + '</option>';
			});
			$("#3_format_"+columCounter).children().remove();
			$("#4_unit_"+columCounter).children().remove();
			$("#3_format_"+columCounter).html(formatOptions);
			$("#4_unit_"+columCounter).html(unitOptions);
			
			//openMetaInfo(id, columCounter);
		}
	});
}
function updateMetricAndGrain(kdxType, index){
	kdxType = $(kdxType).val();
  if(kdxType == 'MEASURE') {
    //$("#7_defaultMetric_"+index).removeAttr("disabled");
    //$("#6_granularity_"+index+" option[value='NA']").attr('selected', 'selected');   
    $("#6_granularity_"+index).attr("disabled",true);  
	$("#6_granularity_"+index).attr("checked",false);  
  } else if(kdxType == 'DIMENSION' || kdxType == 'ID') {
     $("#6_granularity_"+index).removeAttr("disabled");
     //$("#7_defaultMetric_"+index).attr("disabled","disabled");
     //$("#7_defaultMetric_"+index).attr('checked', false);    
  } else {
     //$("#7_defaultMetric_"+index).attr("disabled","disabled"); 
    // $("#6_granularity_"+index).attr("disabled",true); 
     //$("#6_granularity_"+index+" option[value='NA']").attr('selected', 'selected');
	 $("#6_granularity_"+index).attr("checked",false);  
	  $("#6_granularity_"+index).removeAttr("disabled");
     //$("#7_defaultMetric_"+index).attr('checked', false);   
  }
}

function validateGrid() {
	var flag = true;
	counter = 0;
	$("#grid tr[id='1'] input").each(function(index,data) {
		var dataVal = $.trim($(this).val());
		if(dataVal.length < 1){
			colCountArray[counter] = index+1;
			flag = false;
			counter++;
		}
	});
	return flag;
}

function getPagerText (currentPage) {
	var viewText = "";
	if(currentPage > 1) {
		var startCount = ((recordCount * currentPage) - recordCount) + 1;
		var endCount = (recordCount * currentPage);
		if(endCount > totalRecords)
			endCount = totalRecords;
		viewText = "View "+startCount+" - "+endCount+" of "+totalRecords+" Fields";	
	}
	return viewText
}

function clearMsgs() {
	$("#actionMessage").html("");
	$("#errorMessage").html("");	
}

function resetGridState(){
	// setting the grid to it's defaul state if the user doesn't wishes to save data.
	isDataChanged = false;
	$('#grid').setGridParam({url: gridURL});
	clearMsgs();
}

function confirmSubmit(){
	//var confirmed = confirm("Are you sure to save the data?");
	//if(confirmed){
		if(validateGrid()){
			// disable the absorbtion button
			$("#submitDiv td[id='pager_right']").hide();
			// the below transformation is achieved by including jquery.execue.UIPublishedFileTable.js file. See for more details.
			var jsonObjAsArray = $('#grid').gridToJSON();
			$.post("updateEvaluatedColumns.action",{fileTableId:fileTableId, evaluatedColumnList:$.toJSON(jsonObjAsArray), assetId:assetId, tableId:tableId, infoSource:infoSource},function(data,status) {
				if(data.length > 0) {
					$("#errorMessage").html("");					
					$("#errorMessage").append("<ul>");
					$.each(data, function(index, value) {
						$("#errorMessage").append("<li>"+value+"</li>");
					});
					$("#errorMessage").append("</ul>");
					isNotSavedDueToErrors = true;
				} else {
					$("#actionMessage").html("Records updated successfully");
					isNotSavedDueToErrors = false;
					setTimeout('resetGridState();', 3000);
				}
				// enable the absorbtion button after the call
				$("#submitDiv td[id='pager_right']").show();
			});			
		} else {
			$("#errorMessage").html("Error in Field Name for column(s) # "+colCountArray
			+"<br/> Please make sure that name:"
			+"<br/> -Has atleast 1 character"
			+"<br/> -Is not just space characters");
		}
	//}
}

function absorbCallback (hasErrors) {
	if(hasErrors) {
		// some error occured and cancel the absorbtion process.
		$('#pagerErrorDialogDiv').dialog('open');
		$('#pagerErrorDialogDiv').dialog({
			buttons: {
				"No": function() {
					$(this).dialog("close");
					$("#grid").trigger('reloadGrid');
					submitForAbsorption();
				},
				"Yes": function() {
					$(this).dialog("close");								
				}// Yes denotes to stop the user navigation and stay on current page.
			}
		});
	} else {
		submitForAbsorption();
	}
}

function saveBeforeAbsorb() {

	if(validateGrid()){
		// disable the absorbtion button
		$("#submitDiv td[id='pager_right']").hide();
		// the below transformation is achieved by including jquery.execue.UIPublishedFileTable.js file. See for more details.
		var jsonObjAsArray = $('#grid').gridToJSON();
		$.post("updateEvaluatedColumns.action",
			{fileTableId:fileTableId, evaluatedColumnList:$.toJSON(jsonObjAsArray), assetId:assetId, tableId:tableId, infoSource:infoSource},
			function(data,status) {
				if(data.length > 0) {
					$("#errorMessage").html("");					
					$("#errorMessage").append("<ul>");
					$.each(data, function(index, value) {
						$("#errorMessage").append("<li>"+value+"</li>");
					});
					$("#errorMessage").append("</ul>");
					isNotSavedDueToErrors = true;
				} else {
					$("#actionMessage").html("Records updated successfully");
					isNotSavedDueToErrors = false;
					setTimeout('resetGridState();', 3000);
				}
				// enable the absorbtion button after the call
				$("#submitDiv td[id='pager_right']").show();
				// this call back will let the process halt after a save on the grid is triggered and the function will process the boolean
				absorbCallback(isNotSavedDueToErrors);
		});			
	} else {
		$("#errorMessage").html("Error in Field Name for column(s) # "+colCountArray
		+"<br/> Please make sure that name:"
		+"<br/> -Has atleast 1 character"
		+"<br/> -Is not just space characters");
	}
}
function customOnPaging (confirmed) {
	var reqsPage = $('#grid').getGridParam("page");
	
	if(confirmed){
		// resetting the url so that server returns nothing as this event is non-revokable.
		$('#grid').setGridParam({url: "#"});
		$('#grid').setGridParam({page:lastReqPage});
		reqsPage = lastReqPage;
	} else {
		resetGridState();
	}
	return reqsPage;
}

</script>