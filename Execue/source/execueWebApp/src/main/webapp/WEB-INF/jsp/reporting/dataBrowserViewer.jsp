<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html; charset=ISO-8859-1;"
	pageEncoding="ISO-8859-1"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<link href="<c:out value="${basePath}"/>/css/ui-layout-styles.css" rel="stylesheet" type="text/css">
<link href="<c:out value="${basePath}"/>/css/reporting/jquery-ui-1.7.1.custom.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="<c:out value="${basePath}"/>/css/ui.jqgrid.css" />
<style type="text">
   html, body {
   margin: 0;			/* Remove body margin/padding */
   padding: 0;
   overflow: hidden;	/* Remove scroll bars on browser window */
   font-size: 75%;
   }
   .ui-jqgrid-title{float:none;}
   
</style>
<script src="<c:out value="${basePath}"/>/js/jquery.js" type="text/javascript"></script>
<script src="<c:out value="${basePath}"/>/js/i18n/grid.locale-en.js" type="text/javascript"></script>
<script src="<c:out value="${basePath}"/>/js/jquery.jqGrid.min.js" type="text/javascript"></script>

<table width="100%" border="0" cellspacing="0" cellpadding="0"
	align="left">
	<tr>
		<td align="left" valign="top">
		<div style="font-weight: bold;"></div>
		<TABLE id="dataBrowserGrid" style="font-weight: normal;"></TABLE>
		<DIV id=pager></DIV>
		</td>
	</tr>
</table>

<SCRIPT type=text/javascript>

var agQueryId = '<s:property value="dataBrowserGrid.gridId"/>';
var headerString = '<s:iterator value="dataBrowserGrid.columnGridHeader" id="head"><s:property value="#head.name"/>~</s:iterator>';
var headerTypeString = '<s:iterator value="dataBrowserGrid.columnGridHeader" id="head"><s:property value="#head.type"/>,</s:iterator>';
var colNamesArray = headerString.substring(0,(headerString.length - 1)).split("~");
var colHeaderTypeArray = headerTypeString.substring(0,(headerTypeString.length - 1)).split(",");
var colModelArray = [];
var colSize  = '<s:property value="dataBrowserGrid.numOfColumns"/>';
var gridWidth = 100;
if(colSize > 10)
	gridWidth = 800;
else if(colSize <= 3)
	gridWidth = 500;
else if(colSize <= 5)
	gridWidth = 650;
else
	gridWidth = 700;

var colWidth = gridWidth/colSize;

$.each( colNamesArray, function(k,v) {
	var formatType = "";
	var textAlign = "left";
	if(colHeaderTypeArray[k] == 'DOUBLE'){
		formatType = "number";
		textAlign = "right";
	} else { 
		formatType = colHeaderTypeArray[k];
		textAlign = "left";
	}
	colModelArray[k]= {name:colNamesArray[k], index:'name', width:colWidth, search:false, sortable:false, align:textAlign, formatter:formatType};
});

loadGrid();

function loadGrid() {
	$('#dataBrowserGrid').jqGrid({
	  url: 'getJsonDataBrowserCells.action?agQueryId='+agQueryId,
      datatype: 'json',
      mtype: 'GET',
      caption: '<s:property value="dataBrowserGrid.gridTitle"/>',
	  width: gridWidth,
	  height: 220,
	  rowNum: 50,
	  rowList: [50,75,100],
      pager: $('#pager'),
      sortname: 'name',
      viewrecords: true,
      rownumbers: true,
      rownumWidth: 50,
	  gridview: true,
	  colNames: colNamesArray,
	  colModel: colModelArray,
	  gridComplete: function() {
          var records = $('#dataBrowserGrid').getGridParam("records");
          if (records < 11){
          	$('#dataBrowserGrid').setGridHeight("auto");          	
          }
        },
	jsonReader: {
    	repeatitems: true,
    	cell: "gridCellValues"
	},
	prmNames:{page:"requestedPage",rows:"pageSize",sort:"sortField",order:"sortOrder",id:null,nd:null,search:null},
	pager:"#pager"});
	
	$('#dataBrowserGrid').jqGrid('navGrid','#pager',
					{"edit":false,"add":false,"del":false,"search":false,"refresh":true,"view":false,"excel":false}, 		   // options
					{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150,"mtype":"POST",reloadAfterSubmit:false},  // edit
					{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150,"mtype":"POST",reloadAfterSubmit:false},  // add
					{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150,"mtype":"POST",reloadAfterSubmit:false},  // del
					{"drag":true,"closeAfterSearch":true, sopt: ['eq','bw','ew','cn'],sOper:"searchType","closeOnEscape":true,"dataheight":150}); // search
}
 </SCRIPT>
 <style>

   td#pager_left{
   width:20px;
   }
   td#pager_center{
   width:270px;
   }
   td#pager_right{
   text-align: left;
   }
   td#pager_right div.ui-paging-info{
   text-align: left;
   }
   input.ui-pg-input{
   width:30px;
   }
   </style>