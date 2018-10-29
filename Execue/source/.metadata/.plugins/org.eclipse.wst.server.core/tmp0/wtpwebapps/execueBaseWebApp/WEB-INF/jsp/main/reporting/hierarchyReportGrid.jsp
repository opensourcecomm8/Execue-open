<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html; charset=ISO-8859-1;"
	pageEncoding="ISO-8859-1"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<html>
<head>
<style>
td.top-bg a,a#adminId{
    color:#3C71A1;  
    font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif;
    font-size:14px;
}
</style>
<script type="text/javascript" src="<c:out value="${basePath}"/>/js/common/jquery-1.7.2/jquery-1.7.2.min.js"> </script>
<script type="text/javascript" src="<c:out value="${basePath}"/>/js/common/jquery-1.7.2/jquery.jqGrid.min.js"> </script>
<script type="text/javascript" src="<c:out value="${basePath}"/>/js/common/jquery-1.7.2/i18n/grid.locale-en.js" ></script>
<LINK href="<c:out value="${basePath}"/>/drilldown-jqgrid/css/ui.jqgrid.css" rel="stylesheet" type="text/css" />
<LINK href="<c:out value="${basePath}"/>/drilldown-jqgrid/css/ui_all.css" rel="stylesheet" type="text/css" />



</head>
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	align="left">
	<tr>
		<td align="left" valign="top">
		<div style="font-weight: bold;"></div>
		<TABLE id='drillDownGrid<%=request.getParameter("hierarchySummaryId")%>' style="font-weight: normal;"></TABLE>
		<DIV id=pager></DIV>
		</td>
	</tr>
</table>

<script >

$(document).ready(function() {
   retriveData();
 });

function retriveData() {
$('#drillDownGrid<%=request.getParameter("hierarchySummaryId")%>').empty().html('<div id="pleaseWaitDiv" style="margin: 100px 200px; width: 110px;"><img id="waiting_img" src="images/main/Loader-main-page-3.gif" width="32" height="32" /></div>');
$.getJSON("showHierarchicalReport.action?agQueryIdList=<%=request.getParameter("agQueryIdList")%>&hierarchySummaryId=<%=request.getParameter("hierarchySummaryId")%>",function(data){ 
 
   var expanded=data.hierarchyColumnName;
   var title=data.title;
   var header= [];
   
   $.each(data.hierarchicalReportColumnNames,function(i,val){
     header[i]=val;
   })
   
  var colModelArray = [];
  $.each(data.hierarchicalReportColumnMetaInfo, function(k,v) {	
	colModelArray[k]= {name:v.name, index:v.index, width:v.width,  hidden:v.hidden, key:v.key, align:v.align};
}); 
 
   
   var obj= data.hierarchicalReportData
   var data= JSON.stringify(obj);   
   loadGrid(data,expanded,title,header,colModelArray); 

})
}

function loadGrid(data,expanded,title,header,colModelArray){
   grid = $('#drillDownGrid<%=request.getParameter("hierarchySummaryId")%>');
   grid.jqGrid({
	   datatype: "jsonstring",
	   datastr: data,
	   colNames:header,
	   colModel:colModelArray,
	   height: 'auto',
	   gridview: true,
	   rowNum: 10000,
	   sortname: 'id',
	   treeGrid: true,
	   treeGridModel: 'adjacency',
	   treedatatype: "local",
	   ExpandColumn: expanded,
	   //pager : "#pager",
	   caption: title,
	   jsonReader: {
		   repeatitems: false,
		   root: function (obj) { return obj; },
		   page: function (obj) { return 1; },
		   total: function (obj) { return 1; },
		   records: function (obj) { return obj.length; }
		   }
   });
   //grid.jqGrid('navGrid',"#pager");

}
</script>


</html>