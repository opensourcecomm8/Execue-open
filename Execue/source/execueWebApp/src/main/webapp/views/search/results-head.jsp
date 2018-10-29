<%@ taglib prefix="s" uri="/struts-tags"%>
<jsp:include page="../../views/search/qi-head-simpleSearch.jsp" flush="true" />
<link href="css/stylesSearchResult.css" rel="stylesheet" type="text/css">
<link href="css/vertical/news-tab.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="js/showReport.js"> </script>
<script src="js/jquery.execue.dynamicTable.js"></script>
<s:hidden name="request" id="requestedStringId" />
<s:hidden name="applicationId" id="applicationId" />
<s:hidden name="verticalId" id="verticalId" />
<s:hidden name="appNameForURL" id="appNameForURL" />

<script type="text/javascript">


var flag=false;
function showExtendedNote(x1,exeAssetDetailId){
var linkText=$("#short_"+x1+"_note").text();
if(linkText=="more"){
   $.getJSON("getExtendedNote.action",{"uiAssetDetail.assetDetailId":exeAssetDetailId},function(data){   
	   if(data.extendedNote){
	     $("#extended_"+x1+"_note").html(data.extendedNote);
	     $("#short_"+x1+"_note").text("close");
	     $("#extended_"+x1+"_note").show();	 
	    // $("#extended_"+x1+"_note").animate({height:"auto"});
	        
	    }  
    });

	$("#"+x1).show();
	
	}else{
	 $("#short_"+x1+"_note").text("more");
	 $("#extended_"+x1+"_note").hide();
	  //$("#extended_"+x1+"_note").animate({height:"1px"});
	    // $("#extended_"+x1+"_note").fadeOut("fast");	    
	}
}
var left=(screen.width)-(screen.width/2)-250;
var top=(screen.height)-(screen.height/2)-350;
function showUserInfo(uId){
$.get("./qi/getUser.action?userId="+uId,{},function(data){
$("#userInfoDynamicDivOuter").show();
$("#userInfoDynamicDiv").empty().show().html(data);

$("#userInfoDynamicDivOuter").css("left",left+"px");
$("#userInfoDynamicDivOuter").css("top",top+"px");
$("#lightBoxHeading").text("User Profile");
});
}

function showAssetDisclaimerInfo(assetId){
$.get("./qi/getAssetDisclaimer.action?assetId="+assetId,{},function(data){
																	
$("#userInfoDynamicDivOuter").show();
$("#userInfoDynamicDiv").empty().show().html(data);
$("#userInfoDynamicDivOuter").css("left",left+"px");
$("#userInfoDynamicDivOuter").css("top",top+"px");
$("#lightBoxHeading").text("Disclaimer");
});
}
function showReviewQry(x1)
{
	$("#"+x1).slideDown();
}
function hideReviewQry(x1){
	if(!flag){
		$("#"+x1).slideUp();
	}	  
}
function stayReviewQry(x1){
	if(flag){
		$("#"+x1).slideUp();
		flag=false;
	}else{
		$("#"+x1).slideDown();
		flag=true;
	}
}
function  backToHomePage(){	  	
	 var xmlValue = $("#requestedStringId").val();			 	 
	 var type = $("#typeId").val();	 
	 var applicationId = $("#applicationId").val();   
     var verticalId = $("#verticalId").val();	
     var appName = $("#appNameForURL").val(); 	  
	 var url="";
	 
		 if(xmlValue.indexOf('{__spring_security_filterSecurityInterceptor_filterApplied=true')>-1){xmlValue="";}
		  if(type=="QI"){
		   url="execueHome.action?type=QI";
		  }else{
		   url="execueHome.action?type=SI";
		  }
 	 $('#requestString').val(xmlValue);
 	 $('#typeId').val(type); 
 	 /*if(appName){ 	 
 	  document.backToHome.method="get";	
 	  $("form#backToHome #typeId").remove();
 	  $("form#backToHome #requestString").remove();
 	 }else{*/
 	  document.backToHome.method="post";	
 	 //}
	 document.backToHome.action=url;
	 document.backToHome.submit();
	 //window.location.href =back.action?xmlRequestString="+value	 
}
</script>
<script type="text/javascript">

	$ht=screen.height;
		$wt=screen.width;
function setLeftTop(d,m){
var margin=10;
	margin=m;
		var	$left=$(d).offset().left;
		var	$top=$(d).offset().top;
		
		var $divWidth=$(d).width();
		var $divHeight=$(d).height();
		

		
		var right=$wt-$divWidth;
		var bottom=$ht-$divHeight-100;

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
		
		var right=$wt-$divWidth;
		var bottom=$ht-$divHeight-100;
		
											 
		if(t <=m  ){
		 $(d).css("top",m+5+"px");
		// $(d).draggable('destroy');
         startDragging();}
		 
		 
		 if(t>=(bottom)){
		 $(d).css("top",bottom-10+"px");
		// $(d).draggable('destroy');
         startDragging();}
		 
		 
		if(l <=m ){
		 $(d).css("left",m+5+"px");
		// $(d).draggable('destroy'); 
         startDragging();
		}
		
		if( l>=right){
		 $(d).css("left",right-20+"px");
		 //$(d).draggable('destroy'); 
         startDragging();
		}
}
function startDragging(){
	divName='div#hiddenPane';
	margin=10;
		setLeftTop(divName,margin);
		var	$left=$(divName).offset().left;
		var	$top=$(divName).offset().top;

	  $(divName).draggable( {      drag:function(){
											 
												$left=$(divName).offset().left;
												$top=$(divName).offset().top;
											//	$('#left').html($left);
												//$('#top').html($top);
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
   
   
   
 $(document).ready(function () {
	
	
	$("#hiddenPane a#closeButtonId").click(function(){
		$("#hiddenPane").fadeOut("slow");
	});
	
   var requestedQuery = $("#requestedStringId").val();
  
   var srcType = $("#typeId").val(); 
   if(srcType=="SI"){
    $("#metrics").val(requestedQuery);
   } 

	$("#3TabReport .tab_content").hide(); //Hide all content
	$("#3TabReport ul.tabs li:first").addClass("active").show(); //Activate first tab
	$("#3TabReport .tab_content:first").show(); //Show first tab content
	$("#3TabReport ul.tabs li").click(function() { 
		$("#3TabReport ul.tabs li").removeClass("active"); //Remove any "active" class
		$(this).addClass("active"); //Add "active" class to selected tab
		$("#3TabReport .tab_content").hide(); //Hide all tab content
		var activeTab = $(this).find("a").attr("href"); //Find the rel attribute value to identify the active tab + content
		$(activeTab).show(); //Fade in the active content
		//$link = $(""+activeTab+" td.column0 a").attr("id");
		//showMarketDataChart($link);
		return false;
	});
	
	//var columnHeadings=["",""];
	//var columnVals=[{val:"name",colorCode:false},{val:"closePrice",colorCode:false}];	
	//$("#company").dynamicTable({inputURL:"finance/getThreeTabReport.action?tabName=company&tkrName=msft",numberOfColums:columnHeadings.length,columnHeadings:columnHeadings,columnVals:columnVals}); 
	//$("#profitability").dynamicTable({inputURL:"finance/getThreeTabReport.action?tabName=profitability&tkrName=msft",numberOfColums:columnHeadings.length,columnHeadings:columnHeadings,columnVals:columnVals}); 
	//$("#ratio").dynamicTable({inputURL:"finance/getThreeTabReport.action?tabName=ratio&tkrName=msft",numberOfColums:columnHeadings.length,columnHeadings:columnHeadings,columnVals:columnVals}); 
	
});

function setResultsPerPage(resultsPerPage){
 var srcType = $("#typeId").val(); 
 var xmlValue = $("#requestedStringId").val();
 //alert(xmlValue);
   //alert(srcType); 
   if(srcType=="QI"){
  	 window.location="./querySearch.action?request="+xmlValue+"&resultsPerPage="+resultsPerPage+"&type=QI";
   }else{
	window.location="./semanticSearch.action?request="+'<s:property	value="result.queryName" escape="html" />'+"&resultsPerPage="+resultsPerPage+"&type=SI";
	}
}

function relatedSearch(query) {
	$("#metrics").val(query);
	$("#searchFormId").submit();
}


</script>
