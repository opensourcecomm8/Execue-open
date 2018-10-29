<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
   response.setHeader("Pragma", "No-cache");
   response.setDateHeader("Expires", 0);
   response.setHeader("Cache-Control", "no-cache");
   response.setHeader("Cache-Control", "no-store");
   response.addHeader("Cache-Control", "post-check=0, pre-check=0");
   response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- saved from url=(0055)http://www.xs4all.nl/~peterned/examples/csslayout1.html -->
<HTML xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title>Execue homepage</title>
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<META content="text/html; charset=iso-8859-1" http-equiv=Content-Type>

<META name=GENERATOR content="MSHTML 8.00.6001.18702">
<meta http-equiv=“X-UA-Compatible” content=“IE=7; IE=8” />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/qiStyle_new.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="css/roundedSearch.css" type="text/css" />
<link rel="stylesheet" href="css/jquery.autocomplete.css" type="text/css" />
<link rel="stylesheet" href="css/ui.theme.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/jquery-ui-daterangepicker.css" type="text/css" media="screen" />
</HEAD>

<script type="text/javascript">
window.mm_menu_0105220345_0;
x="Show&nbsp;All";
y="javascript:showAll();";
<!--
function mmLoadMenus() {
	
  if (window.mm_menu_0105220345_0) return;
            mm_menu_0105220345_0 = new Menu("root",130,24,"Lucida Grande, Arial, Helvetica, sans-serif",11,"#000000","#FFFFFF","#FFFFFF","#5A8EC7","left","middle",5,0,1000,-5,7,true,true,true,0,false,false);
  mm_menu_0105220345_0.addMenuItem("Business&nbsp;Metrics","javascript:showOptions('business_metrics');");			
  mm_menu_0105220345_0.addMenuItem("Add&nbsp;Condition","javascript:showOptions('condition');");
  mm_menu_0105220345_0.addMenuItem("Group&nbsp;/&nbsp;Present&nbsp;by","javascript:showOptions('groups');");
  mm_menu_0105220345_0.addMenuItem("Best&nbsp;/&nbsp;Worst","javascript:showOptions('tb');");
  mm_menu_0105220345_0.addMenuItem("Select&nbsp;Group&nbsp;of","javascript:showOptions('population');");
  mm_menu_0105220345_0.addMenuItem("Cohort&nbsp;/&nbsp;Sub&nbsp;Query","javascript:showOptions('cohort');");
 
 // mm_menu_0105220345_0.addMenuItem(x,y);
 // mm_menu_0105220345_0.addMenuItem("Hide&nbsp;All","javascript:hideAll();");
   mm_menu_0105220345_0.hideOnMouseOut=true;
   mm_menu_0105220345_0.bgColor='#555555';
   mm_menu_0105220345_0.menuBorder=1;
   mm_menu_0105220345_0.menuLiteBgColor='#FFFFFF';
   mm_menu_0105220345_0.menuBorderBgColor='#D5E0F7';

mm_menu_0105220345_0.writeMenus();
} // mmLoadMenus()

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
<script type="text/javascript" language="JavaScript" src="js/jquery.js"></script>
<script type="text/javascript" language="JavaScript" src="js/qinew/XMLWriter.js"></script>
<script  type="text/javascript" language="JavaScript" src="js/qinew/qiSuggestTerm.js"></script>
<script type="text/javascript" language="JavaScript" src="js/qinew/qiSuggestCondition.js"></script>
<script type="text/javascript" language="JavaScript" src="js/qinew/qiSuggestTB.js"></script>
<script  type="text/javascript" language="JavaScript" src="js/mm_menu.js"></script>
<script  type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/daterangepicker.jQuery.js"></script>
<script type='text/javascript' src='js/qinew/jquery.autocomplete.js'></script>
<script type='text/javascript' src='js/localdata.js'></script>
<script>
var tabIn=0;
var xmlQueryData;
var toggle=true;
var selectTextProcessed;
var datePicker;
var datePickerB;
$(document).ready(function() {
/*var $select=$("<select name='select' id='selectApp' style='height:25px;font-size:20px;color:#525252;margin-top:2px;' ></select>");
	$("#appselectionDiv").append($select);
	  $.get("querySuggest/getApplicationModelInfo.action",function(data) {
	     var data=eval(data);  
	     $.each(data, function(i, appInfo) {
	    if(i==0){	  
	        $('#modelId').val(appInfo.modelId);
	        $('#applicationId').val(appInfo.applicationId);        
	     }
	     $option=$("<option></option>");
	     $data=[];
	     $data[0]=appInfo.modelId;	   
	     $option.html(appInfo.applicationName);
	     $option.val(appInfo.applicationId);
	     $option.data("data",$data);
	     $select.append($option);     
	    });
	  });	
	*/   
	datePicker= $('input#rangeA').daterangepicker({arrows: true, dateFormat: 'M-d-yy',condType:'div#advOpt_condition'}); 
	//datePickerB= $('input#rangeA').daterangepicker({arrows: true, dateFormat: 'M-d-yy',condType:'div#advOpt_cohort_condition'}); 
	$("#selectApp").change(function(){	 
		 var $selected = $(this).find("option:selected");	  
		   //$data=$selected.data("data");
		   $('#modelId').val($selected.attr("modelId"));
	       $('#applicationId').val($(this).val());
	       $('#hiddenPane').hide();	
		  $('#clearPage').trigger('click');
	  });	  
	   $('#modelId').val($("#selectApp option:selected").attr("modelId"));
	   $('#applicationId').val($("#selectApp").val());
 });
 
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
	///$("#menuItem6").empty();
//$("#menuItem6").append("<span class='showhide'>Show&nbsp;All</span>");
//$("#clearAndConditionButtons").css("top",$("#searchBtn").position().top+6);
//$("#clearAndConditionButtons").css("left",$("#searchBtn").position().left-100);
//$("#clearAndConditionButtonsCohort").css("top",$("#searchBtn").position().top+6);
//$("#clearAndConditionButtonsCohort").css("left",$("#searchBtn").position().left-100);
$("#Image3").attr("src","images/c1_qi_advanced_options.gif");
$("#showAllContainer").hide();
$("#toggleSnapshotCohort").attr("src","images/toggle_show1.jpg");
$("#toggleMore").attr("src","images/toggle_show.jpg");
}
function showAll(){

	$("#business_metrics").show();
	$("#showAllContainer").show();
	$("#showAll").empty();
	$("<div></div").attr("id","showOnlyTop").appendTo($("#showAll"));
	
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
	//$("#clearAndConditionButtons").css("top",$("#condition").position().top+26);
	//$("#clearAndConditionButtons").css("left",$("#condition").position().left+440);
   // $("#clearAndConditionButtonsCohort").css("top",$("#cohort").position().top+40);
	//$("#clearAndConditionButtonsCohort").css("left",$("#cohort").position().left+466);
	$("#cohortToggleDiv").hide();
	$("#toggleSnapshotCohort").attr("src","images/toggle_show.jpg");
	
//$("#menuItem6").empty();
//$("#menuItem6").append("<span class='showhide'>Hide&nbsp;All</span>");
}


function submitData() {
    var xmlData=getXMLQueryData();
    var type=$('#type').val();

	if(xmlData.indexOf("<selectText/><selectTextProcessed/><selects/><conditions/><populations/><summarizations/><cohort><summarizations/><conditions/></cohort>")>-1){
		alert("Please Enter Data"); return false}
	$('#request').val(xmlData);
	$('#type').val(type);
	//$('#query').hide();
	//hideAll();
	//$("#showAllContainer").hide();
	$("#toggleSnapshot").attr("src","images/toggle_show1.jpg");
	$("#Image3").attr("src","images/c1_qi_advanced_options.gif");
	
	showQuerySnapShot();
	$("#pleaseWaitDiv").show();
		
	setTimeout('document.images["waiting_img"].src = "images/wait.gif"', 200); 
	return true;
}
function submitDataFreeForm() {
	tempImg = $("#waiting_img").attr("src");
    var data=$("#metrics").val();    
    var type=$("#type").val();
	if(data==""){
		alert("Please Enter Data"); $("#metrics").focus(); $("#searchBtnFreeForm").removeClass("imgBorder1");
						$("#searchBtnFreeForm").addClass("imgNoBorder1"); return false}
	$('#request').val(data);
	$('#type').val(type);
	//$('#query').hide();
	//hideAll();
	//$("#showAllContainer").hide();
	
	$("#pleaseWaitDiv").show();
	setTimeout('document.images["waiting_img"].src = tempImg', 1); 
	return true;
}

function getXMLQueryData() {
	var xmlData = '<qf>';
	var data = SelectMatric.qiSTGenerateSelectTextXML(); 
	xmlData = xmlData + data;
	var data = SelectMatric.qiSTGenerateWithStatXML({baseNode: 'selects', BTNode: 'select'}); 
	xmlData = xmlData + data;
	var data = ConditionMetric.qiConditionGenerateXML(); 
	xmlData = xmlData + data;
	var data = PopulationMetric.qiSTGenerateXML({baseNode: 'populations'});
	xmlData = xmlData + data;
	var data = GroupByMetric.qiSTGenerateXML({baseNode: 'summarizations'});
	xmlData = xmlData + data;
	var data = TopBottomMetric.qiTopBottomGenerateXML();
	xmlData = xmlData + data +"<cohort>" ;
	var data = CohortGroupByMetric.qiSTGenerateXML({baseNode: 'summarizations'});
	xmlData = xmlData + data ;   
	var data =CohortConditionMetric.qiConditionGenerateXML();
	xmlData = xmlData + data + '</cohort> </qf>';
	//alert('xmlData: '+xmlData);
	return xmlData;
	
}


function showHideQuerySnapShot() {	
showQuerySnapShot();
	//	$('#query').hide();	
/*$imgName=	$("#toggleSnapshot").attr("src");	

		if($imgName=="images/toggle_close.jpg"){							
			showQuerySnapShot();
			
		}
		if($imgName=="images/toggle_show.jpg"){							
			$('#query').hide();		
			
		}*/
	
}
function showQuerySnapShot() {	
$("#showQS").hide();


	if($('#ss_metrics').text()== '' && $('#ss_population').text() == '' && $('#ss_TB').text() == ''
		&& $('#ss_condition').text() == '' && $('#ss_summarize').text() == '' && $('#ss_cohort_condition').text()== '' && $('#ss_cohort_summarize').text() == '') {
		$('#ss').hide();
		$('#query').hide();		
	} else {
		$('#ss').show();
		$('#query').show();
		$("#qs").show();
		$("#toggleSnapshot").attr("src","images/toggle_close1.jpg");
	}
	if($('#ss_cohort_condition').text()== '' && $('#ss_cohort_summarize').text() == '') {
		$('#ss_cohort').hide();
	} else {
		$('#ss_cohort').show();
	}
	
}


</script>
<script>

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
function stopStartDragging(l,t,d,m){
	
	var $divWidth=$(d).width();
		var $divHeight=$(d).height();
		
		$ht=screen.height;
		$wt=screen.width;
		
		var right=$wt-$divWidth;
		var bottom=$ht-$divHeight-200;
		
											 
		if(t <=m  ){
		 $(d).css("top",m+5+"px");
		 //$(d).draggable('destroy');
         startDragging();
		 }
		 
		 
		 if(t>=(bottom)){
		 $(d).css("top",bottom-10+"px");
		// $(d).draggable('destroy');
         startDragging();
		 }
		 
		 
		if(l <=m ){
		 $(d).css("left",m+5+"px");
		 //$(d).draggable('destroy'); 
         startDragging();
		}
		
		if( l>=right){
		 $(d).css("left",right-20+"px");
		// $(d).draggable('destroy'); 
         startDragging();
		}
}
function startDragging(){
	divName='div#hiddenPane';
	margin=10;
		//setLeftTop(divName,margin);
		var	$left=$(divName).offset().left;
		var	$top=$(divName).offset().top;
		
		

	  $(divName).draggable( {      drag:function(){
											 
												$left=$(divName).offset().left;
												$top=$(divName).offset().top;
												$('#left').html($left);
												$('#top').html($top);
												stopStartDragging($left,$top,divName,margin);
												}
												     , stop:function(){
													stopStartDragging($left,$top,divName,margin);
													 }
													/*  , start:function(){
													  stopStartDragging($left,$top,divName,margin);
													 }*/
									 }							 
					);
 }
function doDrag(){

 $("#boxTemplate_r1_c2,#boxTemplate_r1_c1,#boxTemplate_r1_c3").mousedown(startDragging);
 
    $("#boxTemplate_r1_c2,#boxTemplate_r1_c1").mouseup(function(){
			setLeftTop('div#hiddenPane');
   });
  }
 
 
 var example;

	function showHide(){
	$('#queries').hide();
	$("#metrics").focus();
	}
 $(document).ready(function () {
	showAll();
	$ht=$("#showAll").height()+55;
	$("#showAllContainer .divBorderWhite").css("height",$ht+"px");						 
		
		
		//$('#queries').hide();
		$('#examplesId').mouseover(function(){
			$('#queries').css("left",$(this).position().left+"px");
			$('#queries').css("top",$(this).position().top+99+"px");
			clearTimeout(example);
			$('#queries').show();
		});   
		$('#examplesId-freeform').mouseover(function(){
			$('#queries').css("left",$(this).position().left+"px");
			$('#queries').css("top",$(this).position().top+15+"px");
			$('#queries a').css({color:"#333",fontSize:"12px"});
			clearTimeout(example);
			$('#queries').show();
		});  
		$('#examplesId-sem2').mouseover(function(){
			$('#queries').css("left",$(this).position().left+0+"px");
			$('#queries').css("top",$(this).position().top+16+"px");
			
			clearTimeout(example);
			$('#queries').show();
			$('#queries').find("a").css("color","#333");
		});  
		$('#examplesId').mouseout(function(){
			example=setTimeout(showHide,200);
		});	
		$('#examplesId-freeform').mouseout(function(){
			example=setTimeout(showHide,200);
		});	
		$('#examplesId-sem2').mouseout(function(){
			example=setTimeout(showHide,200);
		});
		$('#queries').mouseover(function(){
			clearTimeout(example);
		});		
		$('#queries').mouseout(function(){
			example=setTimeout(showHide,200);
		});	
		
	$("#queries td a").bind("click",function(){
			
			$("#metrics").val($(this).text()); 
			example=setTimeout(showHide,200);
			
			//$("#searchBtn").click();
			 });
	$("#metrics").autocomplete('querySuggest/suggestSelect!suggestSelect.action', {
		multiple: true,
		mustMatch: false,
		formatMatch: false,
		selectFirst: false,
		cacheLength: 0,
		autoFill: false,
		multipleSeparator: " ",
		width:534
	});
$("input[type='radio']").attr("name","rad1").click(function(){$("#selectMetrics_textbox").val($(this).val())});
 showLoginInfo('<security:authentication property="principal.fullName"/>');
  showPublisherInfo('<security:authentication property="principal.admin"/>','<security:authentication property="principal.publisher"/>');
Pane_Left=0;
Pane_Top=0;
$('#query').hide();	
//$("#clearAndConditionButtons").css("left",$("#searchBtn").position().left-90);
//$("#clearAndConditionButtons").css("top",$("#searchBtn").position().top+5);

//$("#clearAndConditionButtonsCohort").css("left",$("#searchBtn").position().left-90);
//$("#clearAndConditionButtonsCohort").css("top",$("#searchBtn").position().top+5);

/*$("#Image3").click(function(){ 
$imgName=	$(this).attr("src");
if($imgName=="images/c1_qi_advanced_options.gif"){	
	showAll();
	$ht=$("#showAll").height()+25;
	$("#showAllContainer .divBorderWhite").css("height",$ht+"px");
	$(this).attr("src","images/c2_qi_advanced_options.gif");
}
if($imgName=="images/c2_qi_advanced_options.gif"){	
	showOptions('business_metrics');
	$(this).attr("src","images/c1_qi_advanced_options.gif");
	$("#showAllContainer").hide();
}
	});
*/
$("#toggleSnapshot").click(function(){ 
$imgName=	$(this).attr("src");								
		if($imgName=="images/toggle_show1.jpg"){							
			showQuerySnapShot();
			$("#qs").show();
			$(this).attr("src","images/toggle_close1.jpg");
		}
		if($imgName=="images/toggle_close1.jpg"){							
			$("#qs").hide();
			$(this).attr("src","images/toggle_show1.jpg");
		}
});

$("#closeQS").click(function(){ 
	
			$("#qs").hide();
			$("#showQS").show();
			$('#query').hide();
		
});
$("#showQS").click(function(){ 
	
			showQuerySnapShot();
			$("#qs").show();
			$("#showQS").hide();
		
});

		
			
$("#toggleSnapshotCohort").click(function(){ 
$imgName=	$(this).attr("src");								
		if($imgName=="images/toggle_show.jpg"){							
			$("#cohortToggleDiv").show();
			$("#showAllContainer .divBorderWhite").css("height","360px")
			$(this).attr("src","images/toggle_close.jpg");
		}
		if($imgName=="images/toggle_close.jpg"){							
			$("#cohortToggleDiv").hide();
			$("#showAllContainer .divBorderWhite").css("height","260px")
			$(this).attr("src","images/toggle_show.jpg");
		}
		$("#showAllContainer .divBorderWhite").height($("#showAll").height()+10);
}); 


$("#toggleMore").click(function(){ 
$imgName=	$(this).attr("src");								
		if($imgName=="images/toggle_show.jpg"){							
			$("#tb").show();
			$("#tbText").show();
			//$("#tbText").css("left",$("#tb").position().left+535+"px");
			//$("#tbText").css("top",$("#tb").position().top+40+"px");
			$("#population").show();
			$("#cohort_title").show();
	
			
			$(this).attr("src","images/toggle_close.jpg");
			$("#toggleSnapshotCohort").attr("src","images/toggle_show.jpg");
		}
		if($imgName=="images/toggle_close.jpg"){							
			$("#tb").hide();
			$("#population").hide();
			$("#cohort_title").hide();
			$("#cohortToggleDiv").hide();		
			$("#tbText").hide();
			$(this).attr("src","images/toggle_show.jpg");
		} 
		$("#showAllContainer .divBorderWhite").height($("#showAll").height());
}); 

$ht=screen.height;
//$htMargin=($ht-($ht-160))/4;
$htSearchDiv=($ht-160)-($ht/6);
$("#tdHeight").css("height",$htSearchDiv+"px");
$("#tdHeightMargin").css("height","17px");

$("#menuItem6").click(function(){ 
			  
	if(toggle){						   
	showAll();
toggle=false;
	}else{
		
		$("#population").hide();
	hideAll();
		toggle=true;
		}
									
									});
							 
 $("a#bookmarksId").click(function(){
   var xmlQueryData=getXMLQueryData();
    $("#requestId").val(xmlQueryData);
   // alert(xmlQueryData);
	Pane_Left=$("#divcontainer").position().left;
	Pane_Top=$("#divcontainer").position().top;
	maxRecords=2;
	$("#showBookmarksLink").hide(); 	
	$("#loadingShowBookmarksLink").show();
	var qStr="<qf><selectText/><selectTextProcessed/><selects/><conditions/><populations/><summarizations/><cohort><summarizations/><conditions/></cohort> </qf>"; 
	if(xmlQueryData==qStr){
		alert("please enter query");
		$("#showBookmarksLink").show();
		$("#loadingShowBookmarksLink").hide();
		return false; 
	}else{
	$.get("bookmark/showUserFolders.action", {ajax: 'true'}, function(data) { 
		$("#loadingShowBookmarksLink").hide(); 
		$("#hiddenPaneContent").empty(); 
		$("#hiddenPaneContent").append(data);
		$("#hiddenPane").css("left",Pane_Left+450); 
		$("#hiddenPane").css("top",Pane_Top+80); 
		$("#hiddenPane").fadeIn("slow"); 
		$("#userName").select().focus();
		$("#hiddenPaneContent").css("height",160+"px");
		//$("#hiddenPaneContent").css("height","auto");
		$("#hiddenPaneContent").css("width",290+"px");
		$("#showBookmarksLink").show();
		$("#loadingShowBookmarksLink").hide();
		//$("#bookmarkName").focus();
		$("#txtId").select().focus();
		$("#bookmarkVal").val(xmlQueryData);
		//doDrag();
		//alert($("#bookmarkVal").val(xmlQueryData));
		if(data.indexOf("SIGN IN")>-1){
			$("form#s_form input#userName").select().focus();
		}else{
			$("ul#browser li ul").hide();
			$("form#form1 input#bookmarkName").focus();
			$("#hiddenPaneContent").css("height",100+"px");
		}
		
		

	});
	}
	
});
 
  $("a#bookmarksSearchIdBi").click(function(){
	Pane_Left=$("#divcontainer").position().left;
	Pane_Top=$("#divcontainer").position().top;
	maxRecords=2;
	//$("#clearPage").trigger("click");
	$("#hiddenPane").hide();
	$("#showBookmarksSearchLinkBi").hide(); 	
	$("#loadingShowBookmarksSearchLinkBi").show();
	$.get("bookmark/showBookmarkSearch.action", {ajax: 'true'}, function(data) { 
		$("#loadingShowBookmarksSearchLink").hide(); 
		$("#hiddenPaneContent").empty(); 
		$("#hiddenPaneContent").append(data);
		$("#hiddenPane").css("left",Pane_Left+450); 
		$("#hiddenPane").css("top",Pane_Top+80); 
		$("#hiddenPane").fadeIn("slow"); 
		$("#userName").select().focus();
		$("#hiddenPaneContent").css("height",100+"px");
	$("#hiddenPaneContent").css("height","160px");
		$("#hiddenPaneContent").css("width",290+"px");
		$("#showBookmarksSearchLinkBi").show();
		$("#loadingShowBookmarksSearchLinkBi").hide();
		$("#searchText").focus();
		//doDrag();

		if(data.indexOf("SIGN IN")==-1){
			if(data.indexOf("###searchBookMarksBox####")>-1){
			$("ul#browser li ul").hide();
			$("#hiddenPaneContent").css("width",320+"px");
			$("#hiddenPaneContent").css("height","auto");
			}
		}
		$.each($("li span.file"),function(){				
			if($(this).find('.dType').text()!='Q' && $(this).attr('appId')!=$("#selectApp").val()){
			$(this).find('.dVal').attr("title","Not applicable for this page");
			$(this).find('.dVal').css("color","#DFDFDF");
			$(this).find('.dVal').unbind("click");
			//$(this).parent('li').hide();
		 }
		});
		
	});
});
 
 $("a#loginId").click(function(){
Pane_Left=$("#showLoginLink").position().left;
Pane_Top=$("#showLoginLink").position().top;
$("#showLoginLink").hide(); 	
$("#loadingShowLoginLink").show(); 							   
$.get("ajaxlogin.jsp", {ajax: 'true', noRedirect : 'true'}, function(data) { 
		$("#loadingShowLoginLink").hide(); 

		$("#hiddenPaneContent").empty(); 
		$("#hiddenPaneContent").append(data);
		$("#hiddenPane").css("left",Pane_Left-300); 
		$("#hiddenPane").css("top",Pane_Top+23); 
		$("#hiddenPane").fadeIn("slow");
		$("#hiddenPaneContent").css("height","auto");
		$("#hiddenPaneContent").css("width",290+"px");
		$("#hiddenPaneContent").css("height",160+"px");
		$("#showLoginLink").show();
		$("#userName").select().focus();
	});
});
							 
$("#hiddenPane a#closeButtonId").click(function(){
	
		$("#hiddenPane").fadeOut("slow");
	});
	
// component 

showHideQuerySnapShot();

SelectMatric = $('#selectMetrics').qiSuggestTerm({
		suggestURL: 'querySuggest/suggestSelect!suggestSelect.action',
		snapShotId: 'ss_metrics',
		snapShotText: '<b>Search .. </b>'
	}).get(0);		
PopulationMetric = $('#advOpt_population').qiSuggestTerm({
		suggestURL: 'querySuggest/suggestPopulation!suggestBTsForPopulation.action',
		snapShotId: 'ss_population',
		snapShotText: '<b>Population of... </b>'
	}).get(0);
GroupByMetric = $('#advOpt_summarize').qiSuggestTerm({
		suggestURL: 'querySuggest/suggestSummarize!suggestBTsForSummarize.action',
		snapShotId: 'ss_summarize',
		snapShotText: '<b>Summarize by... </b>'
		}).get(0);
ConditionMetric = $('#advOpt_condition').qiSuggestCondition({
		snapShotId: 'ss_condition',
		sampleElementId: 'sampleValueId',
		snapShotText: '<b>Meeting criteria ..</b>',
		datePicker:datePicker
}).get(0);
TopBottomMetric = $('#advOpt_tb').qiSuggestTB({
		snapShotId: 'ss_TB',
		snapShotText: '<b>Which Meets these Top and Bottom ... </b>'
}).get(0);
CohortGroupByMetric = $('#advOpt_cohort_summarize').qiSuggestTerm({
		suggestURL: 'querySuggest/suggestSummarize!suggestBTsForSummarize.action',
		snapShotRootId : 'ss_cohort',
		snapShotId: 'ss_cohort_summarize',
		snapShotText: '<b>Summarize by.. </b>'
		}).get(0);
CohortConditionMetric = $('#advOpt_cohort_condition').qiSuggestCondition({
		snapShotRootId : 'ss_cohort',
		snapShotId: 'ss_cohort_condition',
		sampleElementId: 'cohortSampleValueId',
		snapShotText: '<b>Limiting ... </b>',
		datePicker:datePicker
}).get(0);
//$("#groups .qiSuggestTerm-holder input.maininput").val("");
//$("#business_metrics ul.qiSuggestTerm-holder").hide();
$("#selectMetrics_tb").hide();
$('#advOpt').hide();
$("#tb").append($("#tbText"));
$("#tbText").css("display","inline");
$("#tbText").css("float","right");
ConditionMetric.hideSelect(); 
CohortConditionMetric.hideSelect(); 
/*$("#addCondition").click(function(){
	ConditionMetric.addCondition(); 
});*/
$("#clearCondition").click(function(){
	ConditionMetric.clearCondition(); 
});
$("#addCohortCondition").click(function(){
	CohortConditionMetric.addCondition(); 
});
$("#clearCohortCondition").click(function(){
	CohortConditionMetric.clearCondition(); 
});
$("#clearTB").click(function(){
	TopBottomMetric.clearTB(); 
});
$("#clearPage").click(function(){
	hideAll();	
	TopBottomMetric.clearTB();
	ConditionMetric.clearCondition();
	CohortConditionMetric.clearCondition(); 
	SelectMatric.clearAll();
	PopulationMetric.clearAll();
	GroupByMetric.clearAll();
	CohortGroupByMetric.clearAll();
	$("#ss_cond").empty();
	$("ss_term").empty();
	$("#conditionSS").empty();	
	CohortConditionMetric.deleteCondition();
	$("#business_metrics").show();	
	$("#Image3").attr("src","images/c1_qi_advanced_options.gif");	
	$("#toggleSnapshot").attr("src","images/toggle_show1.jpg");
	showAll();
	$ht=$("#showAll").height()+10;
	$("#showAllContainer .divBorderWhite").css("height",$ht+"px");				
});
//sample data defination
/*
SelectMatric.clearAll();
SelectMatric.add({name: 'sum', displayName : 'Summation', type: 'STAT'});
SelectMatric.add({name: 'sales', displayName : 'Merch sales', type: 'CONCEPT'});
SelectMatric.add({name: 'ficoScore', displayName : 'FICO Score', type: 'CONCEPT'});

ConditionMetric.clearAll();
ConditionMetric.add({name: 'sum', displayName : 'sum', type: 'STAT'});									
ConditionMetric.add({name: 'sales', displayName : 'Sales', type: 'CONCEPT'});
ConditionMetric.add({name: 'EQUALS', displayName : 'equals to', type: 'OPERATOR'});
ConditionMetric.add({name: '1', displayName : '1', type: 'VALUE'});
ConditionMetric.addCondition();
*/
});// close of ready
</script>
<script>
/*
var bookmarkId;
	var folderID;
  	var bookMarkName;
  	var strName;
  	var strID;
     var folderInfo;
	var urlUserFolderJSON = "bookmark/getUserFolders!getUserFolders.action";   
	var dispFolders;
	var selFoldersList;
	
	
function keyPressFun(e){ 
 // var KeyID = e.keyCode;  
  if(!keyvalue){   
   if(e.keyCode==13 || e.which==13){    
    createFolder();
    keyvalue=true;
   }  
  }
}

function setColorForFolder(folderId)
		{ 
			if(strName=="bookmark"){
 				$("#"+bookmarkId).removeClass("selectedColor");
				$("#"+bookmarkId).addClass("defaultColor");	
 			}
			strName="folder";						
			var fldrName;
			var fldrId;	  	 
	  	 	$.each(folderInfo, function(i, folderInfo) {			    		     	
			          for(prop in folderInfo){
			            if(prop=='id')
		              	 	 fldrId=folderInfo[prop];		              	 	 	              	 	       	             
		            	if(prop=='name')
		            	  	 fldrName=folderInfo[prop];              		           		
			           }			           				              	 	  
			           	if(fldrId==folderId){
			           		strID=	folderId;	
			           		folderID=folderId;		           							
							$("#"+folderId).removeClass("defaultColor");
							$("#"+folderId).addClass("selectedColor");							
						}else{
							strID=	folderId;
							folderID=folderId;							
							$("#"+fldrId).removeClass("selectedColor");
							$("#"+fldrId).addClass("defaultColor");							
						}			    		        
	  	 	});			
	}		
	function setColorForBookmark(bmkId)
	{ 		
		if(strName=="folder"){
 				$("#"+folderID).removeClass("selectedColor");
				$("#"+folderID).addClass("defaultColor");	
 			}
		strName="bookmark";		
		var bmName;
		var bmId;			
		$.each(folderInfo, function(i, folderInfo) {						
			     var bookmarks=folderInfo.uiBookmarks;			    			     
			     $.each(bookmarks, function(i,bookmarks) {			     	
			          for(prop in bookmarks){
			            if(prop=='id')
		              	 	 bmId=bookmarks[prop];		              	 	 	              	 	       	             
		            	if(prop=='name')
		            	  	 bmName=bookmarks[prop];              		           		
			           }			           				              	 	  
			           	if(bmId==bmkId){
			           		strID=	bmkId;	
			           		bookmarkId=bmkId;		           							
							$("#"+bmkId).removeClass("defaultColor");
							$("#"+bmkId).addClass("selectedColor");							
						}else{
							strID=	bmkId;	
							bookmarkId=bmkId;
							bookMarkName=bmName;
							$("#"+bmId).removeClass("selectedColor");
							$("#"+bmId).addClass("defaultColor");							
						}          		         
			     });			        
	  	 });			
	}*/
	function showLoginInfo(name) {
		name = name.replace(/^\s+|\s+$/g,"");
		if(name && name.length > 0) {
			$("#showWelcome").empty().append('<s:text name="execue.login.welcome.message"/> ' + name);
			$("#loginId").hide();
			$("#logoutId").show();
			$("#moredata").hide();
		}	
	}
	
	function showPublisherInfo(admin,publisher) {
			if(admin=="true" ||publisher=="true"){
			$("#publisherTdSeperator").show();
			$("#publisherTd").show();
			$("#adminId").attr("href","swi/showSearchAppsDashboard.action");
			}
		}
	
$(document).ready(function () {
			var JsonString=$("#qiJSONString").val();
		   var jxml=eval( "(" + JsonString + ")" );
		   appid=jxml.applicationId;
		   $("#selectApp option[value='"+appid+"']").attr("selected",true);		
		   $("#selectApp option[value='"+appid+"']").change();
							
var request=$("#request").val(); 
if(request){    
  $.getJSON("querySuggest/qiQueryXML.action",{requestXML:request},function(data){
       if(data){       
        var obj = eval("(" + data + ")");       
	   displayJSONData(obj);
    }   
  });
}

$("#companyFinancialId").click(function() {  
  var data="{\"cohort\":{\"conditions\":[],\"summarizations\":[]},\"conditions\":[{\"lhsBusinessTerm\":{\"datatype\":\"Currency\",\"hasInstances\":false,\"stat\":null,\"statDisplayName\":null,\"term\":{\"displayName\":\"Market Capital\",\"name\":\"MarketCapital\",\"type\":\"CONCEPT\"}},\"operator\":\"GREATER_THAN\",\"qiConversion\":{\"conversionId\":21,\"displayName\":\"dollar\",\"name\":\"DOLLAR\"},\"rhsValue\":{\"query\":null,\"terms\":null,\"values\":[{\"qiConversion\":{\"conversionId\":62,\"displayName\":\"millions\",\"name\":\"MILLIONS\"},\"value\":\"1000\"}]}}],\"orderBys\":null,\"populations\":[],\"selectText\":\"\",\"selectTextProcessed\":false,\"selects\":[{\"stat\":null,\"statDisplayName\":null,\"term\":{\"displayName\":\"Net Sales\",\"name\":\"NetSales\",\"type\":\"CONCEPT\"}}],\"summarizations\":[],\"topBottom\":null}"
	 if(data){       
	    var obj = eval("(" + data + ")");       
		 displayJSONData(obj);
		 }
});

$("#companyFinancialIdQ1").click(function() {  
  var data="{\"cohort\":{\"conditions\":[],\"summarizations\":[]},\"conditions\":[{\"lhsBusinessTerm\":{\"datatype\":\"String\",\"hasInstances\":true,\"stat\":null,\"statDisplayName\":null,\"term\":{\"displayName\":\"Company\",\"name\":\"Company\",\"type\":\"CONCEPT\"}},\"operator\":\"EQUALS\",\"qiConversion\":null,\"rhsValue\":{\"query\":null,\"terms\":[{\"displayName\":\"AMAZON.COM INC (AMZN)\",\"name\":\"Company421\",\"type\":\"INSTANCE\"}],\"values\":null}}],\"orderBys\":null,\"populations\":[],\"selectText\":\"\",\"selectTextProcessed\":false,\"selects\":[{\"stat\":null,\"statDisplayName\":null,\"term\":{\"displayName\":\"Net Income\",\"name\":\"NetIncome\",\"type\":\"CONCEPT\"}}],\"summarizations\":[],\"topBottom\":null}"

	 if(data){       
	    var obj = eval("(" + data + ")");       
		 displayJSONData(obj);
		 }
});

$("#companyFinancialIdQ2").click(function() {  
  var  data='{\"cohort\":{\"conditions\":[],\"summarizations\":[]},\"conditions\":[{\"lhsBusinessTerm\":{\"datatype\":\"String\",\"hasInstances\":true,\"stat\":null,\"statDisplayName\":null,\"term\":{\"displayName\":\"Company\",\"name\":\"Company\",\"type\":\"CONCEPT\"}},\"operator\":\"IN\",\"qiConversion\":null,\"rhsValue\":{\"query\":null,\"terms\":[{\"displayName\":\"INTL BUS MACH (IBM)\",\"name\":\"Company3684\",\"type\":\"INSTANCE\"},{\"displayName\":\"MICROSOFT CORP (MSFT)\",\"name\":\"Company4913\",\"type\":\"INSTANCE\"}],\"values\":null}}],\"orderBys\":null,\"populations\":[],\"selectText\":\"\",\"selectTextProcessed\":false,\"selects\":[{\"stat\":null,\"statDisplayName\":null,\"term\":{\"displayName\":\"Net Sales\",\"name\":\"NetSales\",\"type\":\"CONCEPT\"}}],\"summarizations\":[],\"topBottom\":null}'; 
	 if(data){       
	    var obj = eval("(" + data + ")");       
		 displayJSONData(obj);
		 }
});

$("#companyFinancialIdQ3").click(function() {  
  var data="{\"cohort\":{\"conditions\":[],\"summarizations\":[]},\"conditions\":[{\"lhsBusinessTerm\":{\"datatype\":\"Currency\",\"hasInstances\":false,\"stat\":null,\"statDisplayName\":null,\"term\":{\"displayName\":\"Market Capital\",\"name\":\"MarketCapital\",\"type\":\"CONCEPT\"}},\"operator\":\"GREATER_THAN\",\"qiConversion\":{\"conversionId\":21,\"displayName\":\"dollar\",\"name\":\"DOLLAR\"},\"rhsValue\":{\"query\":null,\"terms\":null,\"values\":[{\"qiConversion\":{\"conversionId\":62,\"displayName\":\"millions\",\"name\":\"MILLIONS\"},\"value\":\"100\"}]}}],\"orderBys\":null,\"populations\":[],\"selectText\":\"\",\"selectTextProcessed\":false,\"selects\":[{\"stat\":null,\"statDisplayName\":null,\"term\":{\"displayName\":\"Income Tax Payables\",\"name\":\"IncomeTaxPayables\",\"type\":\"CONCEPT\"}}],\"summarizations\":[],\"topBottom\":null}"
	 if(data){       
	    var obj = eval("(" + data + ")");       
		 displayJSONData(obj);
		 }
});

$("#companyFinancialIdQ4").click(function() {  
  var  data='{\"cohort\":{\"conditions\":[],\"summarizations\":[]},\"conditions\":[],\"orderBys\":null,\"populations\":[],\"selectText\":\"\",\"selectTextProcessed\":false,\"selects\":[{\"stat\":null,\"statDisplayName\":null,\"term\":{\"displayName\":\"Total Assets\",\"name\":\"TotalAssets\",\"type\":\"CONCEPT\"}}],\"summarizations\":[{\"displayName\":\"Sector\",\"name\":\"Sector\",\"type\":\"CONCEPT\"}],\"topBottom\":null}'; 
	 if(data){       
	    var obj = eval("(" + data + ")");       
		 displayJSONData(obj);
		 }
});

var request=$("#request").val(); 
	if(request){    
	  $.getJSON("querySuggest/qiQueryXML.action",{requestXML:request},function(data){
	       if(data){       
	        var obj = eval("(" + data + ")");       
		   displayJSONData(obj);
	    }   
	});
	}
  var data;
	$("div#advOpt_condition .conditionSpan").hide();
	$("div#advOpt_cohort_condition  .conditionSpan").hide();
	$("div#advOpt_condition .clrConditionSpan").hide();
	$("div#advOpt_cohort_condition  .clrConditionSpan").hide();
  $("#errorMessage").hide();
  $("#messagesDiv").hide();
     data='<c:out value="${qiJSONString}"/>'; 
     if(data){
	   data=data.replace(/&#034;/g, "'");
	   var obj = eval("(" + data + ")");
	   displayJSONData(obj);
    }
$("#adv_metrics").click(function(){
	 if( $("#adv_metrics").html()=="Show Advanced Metrics"){
		 $("#selectMetrics_tb").show();
		 $("#business_metrics ul.qiSuggestTerm-holder").hide();
		 $("#adv_metrics").html("Close Advanced Metrics");
		 // $("#messagesDiv").show();
		 return false;
	 }
	 if( $("#adv_metrics").html()=="Close Advanced Metrics"){
		 $("#selectMetrics_tb").hide();
		 $("#business_metrics ul.qiSuggestTerm-holder").show();
		 $("#adv_metrics").html("Show Advanced Metrics");
		 $("#messagesDiv").hide();		 
		 return false;
	 }		 
	});		 
		
	$("#selectMetrics_textbox").blur(function(){
	   var selectText="";
	   if ($("#selectMetrics_textbox").val() != "Enter Metrics") {
			selectText =$("#selectMetrics_textbox").val();						
		}			  
	    $("#errorId").empty();
	    $("#errorMessage").hide();
	    var textValue="";
	    $radio=$('<input type="radio"></input>');
		$("#selectMetrics_textbox").removeClass("ac_input");
		$("#selectMetrics_textbox").addClass("ac_input1");
		
	 if(selectText != ""){	 	  
		$.getJSON("querySuggest/qiValidation.action",{request : selectText,context : "Select"},function(data){	
		$("#selectMetrics_textbox").removeClass("ac_input1");
		$("#selectMetrics_textbox").addClass("ac_input");
			if(data.success){
			    if(data.result != null){
			       selectTextProcessed=true;			       
			       SelectMatric.clearAll();		    
				   $.each(data.result, function(i,result){
				       if(result.stat){
				         textValue=textValue +result.statDisplayName+", ";
				       }
				       if(result.term.name){
				         textValue=textValue +result.term.displayName+", ";				         
				       }
				       $("#selectMetrics_textbox").val(textValue)
				       getSeletMetric(result);
				     });
				    }				 
				}else{
				  if(data.suggestion != null){
					if(data.suggestion.errMsg){	
					  $("#errorId").html(data.suggestion.errMsg);	
					  $("#suggestionMessage").hide();
					  $("#messagesDiv").show();
					  $("#errorMessage").show();			  
					   
					} else{
					    $("#errorMessage").hide();
					    $("#suggestionMessage").show();
					     $("#messagesDiv").show();
					     $("#suggestionsId").empty();
					    		
					     $.each(data.suggestion.possibilites, function(i,possibilites){					    
					     $.each(possibilites.component, function(i,component){
					   
					     $x=$("<div style='display:inline;float:left;width:300px;'>");
					     if(component.stat != null){
					       $rad=$("<input type='radio'></input").attr('name','suggestionRadio').val(component.statDisplayName +', '+ component.term.displayName).data('data',component);
					     }else{
					       $rad=$("<input type='radio'></input").attr('name','suggestionRadio').val(component.term.displayName).data('data',component);
					     }	
					     				       
					       $rSpan=$("<span></span>");
					       $x.append($rSpan);
					       $rSpan.append($rad);
					       if(component.stat != null){
					         $x.append('<span>'+component.statDisplayName +','+ component.term.displayName+'</span>');
					       }else{
					         $x.append('<span>'+ component.term.displayName+'</span>');
					       }
					       $("#suggestionsId").append($x);					       
					     });
					 });					 
					 $("#suggestionsId").prepend('<div style="display: inline; float: left; width: 300px;"><span><input type="radio" name="suggestionRadio" checked><span></span></span><span>None</span></div>');					
					 $("input[type='radio']").click(function(){
					       SelectMatric.clearAll();	 						  
						  $("#selectMetrics_textbox").val($(this).val());
						  getSeletMetric($(this).data('data'));
						  selectTextProcessed=true;
					});		
			     }
			    }
			}
			//[JSON]{"result":[{"stat":null,"statDisplayName":null,"term":{"displayName":"Account Status","name":"AccountStatus","type":"CONCEPT"}},{"stat":null,"statDisplayName":null,"term":{"displayName":"Fico Score","name":"FicoScore","type":"CONCEPT"}}],"success":true,"suggestion":null}		
		});	
	   }
	}); 
	$("div#cohort ul.qiSuggestTerm-holder li input.maininput").val("Enter Metrics");
	$("div#business_metrics ul.qiSuggestTerm-holder li input.maininput").select().focus();
	/*$("div#groups ul.qiSuggestTerm-holder li input.maininput").attr("tabindex","-1");
	$("div#population ul.qiSuggestTerm-holder li input.maininput").attr("tabindex","-1");
	$("div#advOpt_cohort_condition ul.qiSuggestCondition-holder li input.maininput").attr("tabindex","-1");
	$("div#cohort ul.qiSuggestTerm-holder li input.maininput").attr("tabindex","-1");*/
	$("#showAllContainer .divBorderWhite").height($("#showAll").height());
 });
/*$(document).bind("keypress",function(e){
									 
			switch (e.keyCode) {
							
							case 13 : // enter
							$("input").each(function(){
													x= $(this).focus();
													alert(x);
													if(!x){$("#searchBtn").trigger("click");}
													 })
								
								break;
							
						}						 
		 });*/
 function getSeletMetric(data){
  if(data.stat){
         SelectMatric.add({name: data.stat, displayName : data.statDisplayName, type: 'STAT'});
	}
	if(data.term.name){						  
	  SelectMatric.add({name: data.term.name, displayName : data.term.displayName, type: data.term.type});
	}
}
function getConditionMetric(data){ 
var format={};
var units={};
 if(data.lhsBusinessTerm.stat != null){
	   ConditionMetric.add({name: data.lhsBusinessTerm.stat, displayName : data.lhsBusinessTerm.statDisplayName, type: 'STAT'});
	}								
	if(data.lhsBusinessTerm.term.name != null){
	  ConditionMetric.add({name: data.lhsBusinessTerm.term.name, displayName : data.lhsBusinessTerm.term.displayName, type: data.lhsBusinessTerm.term.type, hasInstances: data.lhsBusinessTerm.hasInstances,datatype: data.lhsBusinessTerm.datatype});
	}
	if(data.operator != null){
	 ConditionMetric.add({name: data.operator, displayName : data.operator, type: 'OPERATOR'});
	}
	if(data.rhsValue.query != null){
	 ConditionMetric.add({name: data.rhsValue.query, displayName : data.rhsValue.query, type: 'VALUE'});
	}
	if(data.rhsValue.terms) {  
		$.each(data.rhsValue.terms,function(i,terms){
		  if(terms.name) {
		   ConditionMetric.add({name: terms.name, displayName : terms.displayName, type: terms.type});
		  }
	   });
	}
	if(data.rhsValue.values) {
		$.each(data.rhsValue.values,function(i,values){		
			//ConditionMetric.add({name: values, displayName : values, type: 'VALUE'});
			//ConditionMetric.add({name: values.value, displayName : values.value, type: 'VALUE', conversionId : values.conversionId});
			ConditionMetric.add({name: values.value, displayName : values.value, type: 'VALUE'});
		});
		$.each(data.rhsValue.values,function(i,values){		
		  if(i=='0'){
		  if(values.qiConversion!=null){
		   format={name: values.qiConversion.name, displayName : values.qiConversion.displayName, id: values.qiConversion.conversionId,type:'FORMAT'};
		  }
		  }
		});
	}
	if(data.qiConversion != null){    
	  units={name: data.qiConversion.name, displayName : data.qiConversion.displayName, id: data.qiConversion.conversionId,type:'UNIT'};
	}		
	ConditionMetric.addCondition("true",format,units);
}
function getPopulations(data){	
	if(data.name != null){
	  PopulationMetric.add({name: data.name, displayName : data.displayName, type: 'CONCEPT'});
	}
}
function getSummarizations(data){   
  if(data.name != null){
    	GroupByMetric.add({name: data.name, displayName : data.displayName, type: 'CONCEPT'});
    }
}
function getCohortConditionMetric(data){   
var format={};
var units={};
	if(data.lhsBusinessTerm.stat != null){
	  CohortConditionMetric.add({name: data.lhsBusinessTerm.stat, displayName : data.lhsBusinessTerm.statDisplayName, type: 'STAT'});
	}								
	if(data.lhsBusinessTerm.term.name != null){
	  CohortConditionMetric.add({name: data.lhsBusinessTerm.term.name, displayName : data.lhsBusinessTerm.term.displayName, type: data.lhsBusinessTerm.term.type, hasInstances: data.lhsBusinessTerm.hasInstances,datatype: data.lhsBusinessTerm.datatype});
	}
	if(data.operator != null){
	  CohortConditionMetric.add({name: data.operator, displayName : data.operator, type: 'OPERATOR'});
	}
	if(data.rhsValue.query != null){
	  CohortConditionMetric.add({name: data.rhsValue.query, displayName : data.rhsValue.query, type: 'VALUE'});
	}
	if(data.rhsValue.terms) {   
	  $.each(data.rhsValue.terms,function(i,terms){
		CohortConditionMetric.add({name: terms.name, displayName : terms.displayName, type: terms.type});
	  });
	}
	/*if(data.rhsValue.values != null){
	  CohortConditionMetric.add({name: data.rhsValue.values, displayName : data.rhsValue.values, type: 'VALUE'});
	}*/
	if(data.rhsValue.values) {
		$.each(data.rhsValue.values,function(i,values){		
			//ConditionMetric.add({name: values, displayName : values, type: 'VALUE'});
			//ConditionMetric.add({name: values.value, displayName : values.value, type: 'VALUE', conversionId : values.conversionId});
			CohortConditionMetric.add({name: values.value, displayName : values.value, type: 'VALUE'});
		});
		$.each(data.rhsValue.values,function(i,values){		
		  if(i=='0'){
			   if(values!=null){  if(values.qiConversion!=null){
		  format={name: values.qiConversion.name, displayName : values.qiConversion.displayName, id: values.qiConversion.conversionId,type:'FORMAT'};
			   }}
		  }
		});
	}
	if(data.qiConversion != null){    
	  units={name: data.qiConversion.name, displayName : data.qiConversion.displayName, id: data.qiConversion.conversionId,type:'UNIT'};
	}	
	 CohortConditionMetric.addCondition('true',format,units);

}
function getCohortGroupByMetric(data){   
   if(data.name != null){
      CohortGroupByMetric.add({name: data.name, displayName : data.displayName, type: 'CONCEPT'});
    } 
}		
function displayJSONData(data){
      // Select Matric	
      SelectMatric.clearAll();	 
	   if(data.selects){
		    $.each(data.selects, function(i,selects){
			  getSeletMetric(selects);
		   });
	    }
	   // Condition Matric
	   ConditionMetric.clearAll();		   
	   if(data.conditions){
		    $.each(data.conditions,function(i,conditions){
			  getConditionMetric(conditions);
		    });
	    }
	  //populations
	  PopulationMetric.clearAll();
	  if(data.populations){
	  	$.each(data.populations,function(i,populations){
		  getPopulations(populations);
		 });
	   }
	   //summarizations
	    GroupByMetric.clearAll();
	   if(data.summarizations){
	   	  $.each(data.summarizations,function(i,summarizations){
		    getSummarizations(summarizations);
		  });
	   }
	  //TopBottom
	  //TopBottomMetric.clearAll();			
		if(data.topBottom){
			//alert("in top"+data.topBottom.term.name+data.topBottom.type+data.topBottom.value);
			TopBottomMetric.add({name: data.topBottom.term.name, displayName : data.topBottom.term.name, type: data.topBottom.term.type});				
			TopBottomMetric.add({name: data.topBottom.type, displayName :data.topBottom.type,type: 'operator'});
			TopBottomMetric.add({name: data.topBottom.value, displayName :data.topBottom.value,type: 'VALUE'});
		    TopBottomMetric.addBitsToArray();
		}
	   //cohort conditions	
	  CohortConditionMetric.clearAll();	 
	  if(data.cohort.conditions){
		    $.each(data.cohort.conditions,function(i,conditions){
             getCohortConditionMetric(conditions);
	       });
	   }
	   //cohort summarizations
	    CohortGroupByMetric.clearAll();		   
	   if(data.cohort.summarizations){
		   $.each(data.cohort.summarizations,function(i,summarizations){
		     getCohortGroupByMetric(summarizations);
	      });
	   }
}
	
</script>