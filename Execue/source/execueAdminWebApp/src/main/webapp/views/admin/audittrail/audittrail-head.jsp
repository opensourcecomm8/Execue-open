<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.execue.web.core.util.ExecueWebConstants"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<c:set var="adminPath"
	value="<%=application.getAttribute(ExecueWebConstants.ADMIN_CONTEXT)%>" />
<%
   response.setHeader("Pragma", "No-cache");
   response.setDateHeader("Expires", 0);
   response.setHeader("Cache-Control", "no-cache");
   response.setHeader("Cache-Control", "no-store");
   response.addHeader("Cache-Control", "post-check=0, pre-check=0");
   response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">

<HTML xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title>Execue homepage</title>
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<META content="text/html; charset=iso-8859-1" http-equiv=Content-Type>

<META name=GENERATOR content="MSHTML 8.00.6001.18702">



<link href="<c:out value='${basePath}'/>/css/admin/SpryTabbedPanels.css" rel="stylesheet" type="text/css">
<link href="<c:out value='${basePath}'/>/css/common/styles.css" rel="stylesheet" type="text/css">
<link href="<c:out value='${basePath}'/>/css/common/ui-layout-styles.css" rel="stylesheet" type="text/css">
<link href="<c:out value='${basePath}'/>/css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="<c:out value='${basePath}'/>/css/common/roundedSearch.css" rel="stylesheet">

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<c:out value='${basePath}'/>/css/common/qiStyle_new.css"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet"
	href="<c:out value='${basePath}'/>/css/common/roundedSearch.css"
	type="text/css" />
<link rel="stylesheet"
	href="<c:out value='${basePath}'/>/css/admin/audittrail/jquery.autocomplete.css"
	type="text/css" />
<link rel="stylesheet"
	href="<c:out value='${basePath}'/>/css/admin/audittrail/ui.theme.css"
	type="text/css" media="screen" />
<link rel="stylesheet"
	href="<c:out value='${basePath}'/>/css/admin/audittrail/jquery-ui-daterangepicker.css"
	type="text/css" media="screen" />
<LINK href="<c:out value='${basePath}'/>/css/admin/autoComplete.css"
	rel=stylesheet>
</HEAD>



<script language="JavaScript" src="../js/common/jquery.js"></script>
<script language="JavaScript" src="<c:out value='${basePath}'/>/js/admin/jquery.execue.js"></script>
<script type="text/javascript" src="<c:out value='${basePath}'/>/js/common/jquery.ui.all.js"></script>
<script type="text/javascript" src="<c:out value='${basePath}'/>/js/common/jquery.layout.js"></script>
<script type="text/javascript" src="<c:out value='${basePath}'/>/js/admin/execue.commons.js"></script>
<script type="text/javascript" src="<c:out value='${basePath}'/>/js/admin/thickbox.js"></script>

<script type="text/javascript" language="JavaScript"
	src="<c:out value='${basePath}'/>/js/common/jquery.js"></script>
<script type="text/javascript" language="JavaScript"
	src="<c:out value='${basePath}'/>/js/admin/audittrail/audittrailSuggestTerm.js"></script>
<script type="text/javascript" language="JavaScript"
	src="<c:out value='${basePath}'/>/js/admin/audittrail/audittrailSuggestTimeframe.js"></script>
<script type="text/javascript" language="JavaScript"
	src="<c:out value='${basePath}'/>/js/common/mm_menu.js"></script>
<script type="text/javascript"
	src="<c:out value='${basePath}'/>/js/admin/audittrail/jquery-ui.min.js"></script>
<script type="text/javascript"
	src="<c:out value='${basePath}'/>/js/admin/audittrail/daterangepicker.jQuery.js"></script>
<script type='text/javascript'
	src='<c:out value='${basePath}'/>/js/common/jquery.autocomplete.js'></script>

<script>
var tabIn=0;
var xmlQueryData;
var toggle=true;
var selectTextProcessed;
var datePicker;
var datePickerB;
$(document).ready(function() {  
	datePicker= $('input#rangeA').daterangepicker({arrows: true, dateFormat: 'M-d-yy',condType:'div#advOpt_condition'});
	
	showAll();
	$ht=$("#showAll").height()+100;
	$("#showAllContainer .divBorderWhite").css("height",$ht+"px");	
	Pane_Left=0;
	Pane_Top=0;
	$('#query').hide();	
	
	$ht=screen.height;
	//$htMargin=($ht-($ht-160))/4;
	$htSearchDiv=($ht-160)-($ht/6);
	$("#tdHeight").css("height",$htSearchDiv+"px");
	$("#tdHeightMargin").css("height","17px");
							 
   $("#hiddenPane a#closeButtonId").click(function(){	
		$("#hiddenPane").fadeOut("slow");
	});
	
	// component 
	SelectMatric = $('#selectMetrics').qiSuggestTerm({
			suggestURL: '../audittrail/showUsers!showUsers.action',
			snapShotId: 'ss_metrics',
			snapShotText: '<b>Search .. </b>'
	}).get(0);

	GroupByMetric = $('#advOpt_summarize').qiSuggestTerm({
			suggestURL: '../audittrail/showAuditLogType!showAuditLogType.action',
			snapShotId: 'ss_summarize',
			snapShotText: '<b>Summarize by... </b>'
	}).get(0);
	ConditionMetric = $('#advOpt_condition').audittrailSuggestTimeframe({
	        suggestLHSURL : '',
			snapShotId: 'ss_condition',
			sampleElementId: 'sampleValueId',
			snapShotText: '<b>Meeting criteria ..</b>',
			datePicker:datePicker
	}).get(0);
	
	$("#selectMetrics_tb").hide();
	$('#advOpt').hide();
	$("#tb").append($("#tbText"));
	$("#tbText").css("display","inline");
	$("#tbText").css("float","right");
	ConditionMetric.hideSelect();
		
	$("#clearCondition").click(function(){
		ConditionMetric.clearCondition(); 
	});
	
	$("#clearTB").click(function(){
		TopBottomMetric.clearTB(); 
	});
	$("#clearPage").click(function(){
		hideAll();				
		ConditionMetric.clearCondition();			 
		SelectMatric.clearAll();			
		GroupByMetric.clearAll();		
		$("#ss_cond").empty();
		$("ss_term").empty();
		$("#conditionSS").empty();	
		CohortConditionMetric.deleteCondition();
		$("#business_metrics").show();	
		$("#Image3").attr("src","images/main/c1_qi_advanced_options.gif");	
		$("#toggleSnapshot").attr("src","images/main/toggle_show1.jpg");
		showAll();
		$ht=$("#showAll").height()+10;
		$("#showAllContainer .divBorderWhite").css("height",$ht+"px");				
	});
		
 });
 


function setLeftTop(d,m){
   var margin=10;
	margin=m;
		var	$left=$(d).offset().left;
		var	$top=$(d).offset().top;
		
		var $divWidth=$(d).width();
		var $divHeight=$(d).height();
		
		$ht=screen.height;
		$wt=screen.width;
		
		var right=$wt-$divWidth;
		var bottom=$ht-$divHeight-200;

		if($top <=margin   ){
		$(d).css("top",margin+5+"px");
		}
		
		if( $top >=(bottom) ){
		$(d).css("top",bottom-10+"px");
		}
		
		if($left <=margin ){
		$(d).css("left",margin+5+"px");
		}
		
		if( $left >= right){
		$(d).css("left",right-20+"px");
		}
}

function showHide(){
	$('#queries').hide();
	$("#metrics").focus();
}

function showOptions(fName){
		hideAll();		
		$("#"+fName).show();
        toggle=true;
}
function hideAll(){	
	$("#population").appendTo($("#showAllComps"));
	$("#condition").appendTo($("#showAllComps"));
	$("#moreDiv").appendTo($("#showAllComps"));
	$("#groups").appendTo($("#showAllComps"));
	$("#tb").appendTo($("#showAllComps"));
	$("#cohort_title").appendTo($("#showAllComps"));
	$("#cohort").appendTo($("#showAllComps"));
	$(".search_header_text_qi").css("color","#fff");
	$(".search_header_text_qi").css("font-weight","bold");
	$("#showAllContainer .divBorderWhite").css("height","0px")
	
	$("#cohort_title").hide();
	$("#moreDiv").hide();
	$("#business_metrics").hide();
	$("#population").hide();
	$("#groups").hide();
	$("#condition").hide();
	$("#tb").hide();
	$("#cohort").hide();
	$("#cohortToggleDiv").css("display","block");
	
	$("#Image3").attr("src","images/main/c1_qi_advanced_options.gif");
	$("#showAllContainer").hide();
	$("#toggleSnapshotCohort").attr("src","images/main/toggle_show1.jpg");
	$("#toggleMore").attr("src","images/main/toggle_show.jpg");
}
function showAll(){
	//$("#business_metrics").show();
	$("#showAllContainer").show();
	$("#showAll").empty();
	$("<div></div").attr("id","showOnlyTop").appendTo($("#showAll"));
	$("#business_metrics").appendTo($("#showOnlyTop")).show();
	$("#condition").appendTo($("#showOnlyTop")).show();
	$("#groups").appendTo($("#showOnlyTop")).show();
	
	$("<div></div").attr("id","showMore").appendTo($("#showAll"));
	$("#moreDiv").appendTo($("#showMore")).show();
	//$("#topMenu").appendTo($("#more")).show();
	
	$("<div></div").attr("id","showOnlyMore").appendTo($("#showMore"));
	
	$("#tb").appendTo($("#showOnlyMore")).hide();
	$("#population").appendTo($("#showOnlyMore")).hide();
	$("#cohort_title").appendTo($("#showAll")).hide();
	
	$("<div></div").attr("id","showOnlyCo").appendTo($("#showAll"));
	$("#cohort").appendTo($("#showOnlyCo")).show();
	
	
	$(".search_header_text_qi").css("color","#00468C");
	$(".search_header_text_qi").css("font-weight","normal");
	$("#showAllContainer .divBorderWhite").css("height","360px");	
	$("#cohortToggleDiv").hide();
	$("#toggleSnapshotCohort").attr("src","images/main/toggle_show.jpg");
	
}


</script>