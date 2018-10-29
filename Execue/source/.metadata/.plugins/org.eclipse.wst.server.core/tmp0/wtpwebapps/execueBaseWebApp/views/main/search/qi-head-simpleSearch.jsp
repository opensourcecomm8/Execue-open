<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.execue.core.common.bean.Pagination"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<%@page import="com.execue.web.core.util.ExecueWebConstants"%>
<c:set var="adminPath" value="<%=application.getAttribute(ExecueWebConstants.ADMIN_CONTEXT)%>" />
<%
   String baseURL = "";
   response.setHeader("Pragma", "No-cache");
   response.setDateHeader("Expires", 0);
   response.setHeader("Cache-Control", "no-cache");
   response.setHeader("Cache-Control", "no-store");
   response.addHeader("Cache-Control", "post-check=0, pre-check=0");
   response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
   
   Pagination pagination = (Pagination) request.getAttribute("PAGINATION");
   String requestedPage = "";
   String totalRecrods = "";
   if (pagination != null) {
      baseURL = pagination.getBaseURL();
      requestedPage = pagination.getRequestedPage();
      totalRecrods = pagination.getPageCount();
   }
      
%>

<c:set var="basePath" value="<%=request.getContextPath()%>" />
<link href="<c:out value="${basePath}"/>/css/common/qiStyle_new.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<c:out value="${basePath}"/>/css/common/roundedSearch.css" type="text/css" />
<link rel="stylesheet" href="<c:out value="${basePath}"/>/css/main/jquery.autocomplete.css" type="text/css" />
<script language="JavaScript" src="<c:out value="${basePath}"/>/js/common/goog_analytics.js"></script>
<script language="JavaScript" src="<c:out value="${basePath}"/>/js/common/jquery.js"></script>
<script type="text/javascript" src="<c:out value="${basePath}"/>/js/common/jquery.ui.all.js"></script>
<script type='text/javascript'
	src='<c:out value="${basePath}"/>/js/main/qi/jquery.autocomplete.freeform.js'></script>
    
<script><!--
var autoSuggestClicked=false;
var tabIn=0;
var xmlQueryData;
var toggle=true;
var selectTextProcessed;
var processing=false;
var appCount='<s:text name="execue.appCount" />'

var str='<s:text name="execue.qi.search.all.published.apps"/>';
var disableCache=false;
var reSemantificationProcess=false;
var oldQuery="";
$(document).ready(function(){
     var appId='<s:property value="applicationId"/>';  
     if(appId){
      str='<s:text name="execue.qi.search.selected.published.apps"/>';
     }            
	if($("#metrics").val()==""){
		$("#metrics").val(str);	
		$("#metrics").blur();
		//setTimeout(function(){  $("#metrics").blur();},100);
	}
$("#metrics").focus(function(){ 
							 if($("#metrics").val()==str){
							$("#metrics").val(""); 
							 }
					 });
$("#metrics").blur(function(){
							if($("#metrics").val()==""){
							$("#metrics").val(str); 	
							$("#noLink").focus();
							}
							 });



	
 });




function refreshClicked(){
disableCache=true; 
$("#searchBtnFreeForm").click();
}
function submitDataFreeForm(st) {
	$("div.ac_results").hide();
	 var $appId=$("#applicationId").val(); 	
	tempImg = $("#waiting_img").attr("src");
    var data=$.trim($("#metrics").val());    
    var type=$("#type").val(); 
	if((data=="")||(data.indexOf(str)>-1)){
		alert("Please Enter Data"); processing=false; $("#metrics").focus(); $("#searchBtnFreeForm").removeClass("imgBorder1");
						$("#searchBtnFreeForm").addClass("imgNoBorder1"); return false}
	if(disableCache){$("#disableCache").val('true'); }
	//TODO-JT- need to read appId from the configration
	if($appId == 1508){
	   $('#requestString').val(data);
	}else{	 
	   $('#request').val(data);
	}
	$('#type').val(type);	
	$("#pleaseWaitDiv").show();
	$("#showLoaderPopup").show();
	setTimeout('document.images["waiting_img"].src = tempImg', 1); 
	setTimeout(function(){$("div.ac_results").hide();},1000);
	$("#waitImage").css("left",$("#metrics").position().left+190+"px");
	$("#waitImage").css("top",$("#metrics").position().top+"px");
	if (navigator.appVersion.indexOf("MSIE 7.") != -1 && st){
	$("#waitImage").css("left",$("#metrics").position().left+230+"px")
	}
	if(navigator.appVersion.indexOf("MSIE 7.") != -1 && !st){
	$("#waitImage").css("left",$("#metrics").position().left-690+"px")
	}
	if(navigator.appVersion.indexOf("MSIE 7.") != -1 && st=="app"){
	$("#waitImage").css("left",$("#metrics").position().left-490+"px")
	}
	if(!processing){
	processing=true; 
	return true;
	}
	else{
		$("#underProcess").show();
		$("#underProcess").css("left",$("#metrics").position().left+100+"px");
	$("#underProcess").css("top",$("#metrics").position().top-20+"px");
	
	return false;
	}
}
function hasImageCheck(){
var hasImage=$("#imagePresent").is(":checked");
	if(hasImage){
	return "true";
	}else{
	return "false";	
	}	
}
function submitDataUnstructuredFreeForm() { 

	//$("#userReqZip").val($zip);	
	//$("#selectedDVDLimit").val($("#selectedDefaultVicinityDistanceLimit").val());
	//var hasImg = hasImageCheck();
	//$("#imgPresent").val(hasImg);	
	//var selectedSortType=$("#udxCarsInfoSortTypeId option:selected").val();	
	//$("#udxCarSortTypeId").val(selectedSortType);
	//alert(reSemantificationProcess);
	if(reSemantificationProcess){
		
		jConfirm('Are you sure you want to stop resemantification ?', 'Confirmation Dialog', 
				
				    function(r) {
					if(r==true)
					{
					    $("#box").fadeOut(300);
						reSemantificationProcess=false;
						$("#searchBtnFreeForm").click();
					}else{
						$("#metrics").val(oldQuery);
						return false;
					}
					});
		
		return false;
	}else{
	$("div.ac_results").hide();
	tempImg = $("#waiting_img").attr("src");
    var data=$("#metrics").val();    
    var type=$("#type").val();
	if((data=="")||(data.indexOf(str)>-1)){
		alert("Please Enter Data"); processing=false; $("#metrics").focus(); $("#searchBtnFreeForm").removeClass("imgBorder1");
						$("#searchBtnFreeForm").addClass("imgNoBorder1"); 
		return false
	}
	if(disableCache){
	   $("#disableCache").val('true'); 
	}
	$('#request').val(data);
	$('#type').val(type);
	$("#selectedDVDLimit").val($("#distance").val()); //$("#selectedDefaultVicinityDistanceLimit").val()); // selectedDefaultVicinityDistanceLimit changed to distance
	var hasImg = hasImageCheck();
	$("#imgPresent").val(hasImg);
	
	$("#locationSuggestTerm_locationBedId").val($("#locationBedId").val());
	$("#locationSuggestTerm_latitude").val($("#latitude").val());
	$("#locationSuggestTerm_longitude").val($("#longitude").val());
	$("#locationSuggestTerm_id").val($("#locationId").val());
	$("#locationSuggestTerm_displayName").val($("#location").val());
	//$('#query').hide();
	//hideAll();
	//$("#showAllContainer").hide();
	$("#pleaseWaitDiv").show();
	setTimeout('document.images["waiting_img"].src = tempImg', 1); 
	setTimeout(function(){$("div.ac_results").hide();},1000);
	$("#waitImage").css("left",$("#metrics").position().left+205+"px");
	$("#waitImage").css("top",$("#metrics").position().top+"px");
	if(!processing){
	processing=true;
	return true;
	}
	else{
		$("#underProcess").show();
		$("#underProcess").css("left",$("#metrics").position().left+100+"px");
	$("#underProcess").css("top",$("#metrics").position().top-20+"px");
		//window.history.go(0); 
	return false;
	}
	
	}
	/*if(apId=="1508"){ //todo this is hard coded id . we need to think other solution
		var $formData=$("#searchFormId").serialize(); 
		$.get("<c:out value="${basePath}"/>/semanticSearch.action?"+$formData,function(data){
					displayDataInDiv(data);	  
						  });
		return false;
	}*/
}
--></script>
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
 
 
 autoSelect=false;
 enterPressed=false;

 

var example;
var jsonExamples;
var jsonVerticals;

function showHide(){
	$('#queries').hide();
	$("#metrics").focus();
}


function getAppExamples(){
	var openTable = '<table cellpadding="3" id="queriesDiv">'
					+ '<tr><td align="left" style="border-bottom:#CCC thin dashed;">'
					+ '<span style="padding-left:2px;color:#666;float:left;text-align:left;" >'
					+ '<b>General Questions</b></span></td></tr>';
	var closeTable = '</table>';
	$("#queries").empty();
	if(jsonExamples){
		jsonExamples.abort();
	}
	var applicationId='<s:property value="applicationId"/>';	
	if(applicationId != -1){
		jsonExamples = $.get("<c:out value="${basePath}"/>/querySuggest/showAppExamples.action?applicationId="+applicationId,
		function(data) {
		    var data=eval(data);
		    $("#queries").html(openTable);
		     $.each(data, function(i, query) {
		    	prepareExamplesTable(query);
		    });
		    $("#queries").append(closeTable);
		    $("#queriesDiv td a").bind("click",function(){
				$("#metrics").val($(this).text());
				$("#searchBtnFreeForm").click();
			});
	  	});
	}else{
		jsonExamples = $.get("<c:out value="${basePath}"/>/querySuggest/showAppExamples.action",function(data) {
		    var data=eval(data);
		    $("#queries").html(openTable);	    
		     $.each(data, function(i, query) {
		    	prepareExamplesTable(query); 
		    });
		    $("#queries").append(closeTable);
		    $("#queriesDiv td a").bind("click",function(){
				$("#metrics").val($(this).text()); 
				//example=setTimeout(showHide,200);
				$("#searchBtnFreeForm").click();
			});
	  	});
  	}
}
function prepareExamplesTable(query){
	var openTag = '<tr><td align="left"><a style="cursor: pointer;style: underline;" class="box2-link">';
	var closeTag = '</a></td></tr>';
	var rowTag = openTag + query + closeTag;
	$("#queriesDiv").append(rowTag);
}
function cancelRequest()
{
	if ( navigator.userAgent.indexOf("MSIE") > 0 )
		document.execCommand('Stop');
	else
		window.stop();
	
	
	$("#pleaseWaitDiv").hide();
	$("#underProcess").hide();
	$("#metrics").focus();
	processing=false;
	submitRequest = 0;
}

 $(document).ready(function () { 
	 processing=false;
	autoSuggestClicked=false;
	
	$('#processYes').click(function(){
								cancelRequest();
									});
	$('#processNo').click(function(){
									$("#underProcess").hide();
									});
	
	$('#queries').hide();
		$('#examplesId').mouseover(function(){
			$('#queries').css("left",$(this).position().left+"px");
			$('#queries').css("top",$(this).position().top+79+"px");
			getAppExamples();
			clearTimeout(example);
			$('#queries').show();
		});   
		$('#examplesId-freeform').mouseover(function(){
			$('#queries').css("left",$(this).position().left+"px");
			$('#queries').css("top",$(this).position().top+15+"px");
			$('#queries a').css({color:"#333",fontSize:"12px"});
			getAppExamples();
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
			//alert("g");
			$("#searchBtnFreeForm").click();
			 });		
	var applicationId='<s:property value="applicationId"/>';
	var url=""
	if(applicationId){
	  url='<c:out value="${basePath}"/>/querySuggest/semanticSuggest.action?applicationId='+applicationId;
	}else{
	   url='<c:out value="${basePath}"/>/querySuggest/semanticSuggest.action';
	}
		$("#metrics").autocomplete(url, {
		multiple: true,
		mustMatch: false,
		formatMatch: false,
		selectFirst : false,
		cacheLength: 0,
		autoFill: false,
		multipleSeparator: " "//,
		//width:604
	});

$("#metrics").bind('keydown', function(e) {
					if (e.keyCode == 13) {
							
							//alert(autoSelect);
							
						    if(autoSuggestClicked){$("#searchBtnFreeForm").trigger('click'); return false;}
						if(autoSelect){ autoSelect=false; return false;}
						else{
						$("#searchBtnFreeForm").focus();
						$("#searchBtnFreeForm").removeClass("imgNoBorder1");
						$("#searchBtnFreeForm").addClass("imgBorder1");
						$("#searchBtnFreeForm").trigger('click');
						}
						//$("#searchBtn").trigger('click');
					}
					else if(e.keyCode==9){
						setFocusOnSearch();
						//$("#searchBtn").trigger('click');
					}
					
					else if(e.keyCode==8){
					   
						var displayString=$("#metrics").val();
						if(displayString.indexOf("Ask simple questions.")>-1){
												  $("#metrics").val("");
												  return;
												  }
						//$("#searchBtn").trigger('click');
					}

				}).result(function(event, data, formatted) {  autoSelect=true; event.stopPropagation(); return false;});

$ht=screen.height;
//$htMargin=($ht-($ht-160))/4;
$htSearchDiv=($ht-160)-($ht/6);

$("#tdHeight").css("height",$htSearchDiv+"px");


						 
 $("a#bookmarksId").click(function(){
 data=$("#metrics").val();
 $("#requestId").val(data);
   // alert($("#requestId").val());
	Pane_Left=$(this).position().left;
	Pane_Top=$(this).position().top;
	maxRecords=2;
	$("#showBookmarksLink").hide(); 	
	$("#loadingShowBookmarksLink").show();
	if(data==""){
		alert("please enter query");
		$("#showBookmarksLink").show();
		$("#loadingShowBookmarksLink").hide();
		return false; 
	}else{
	
	$.get("<c:out value="${basePath}"/>/bookmark/showUserFolders.action", {ajax: 'true'}, function(data) { 											  
		$("#loadingShowBookmarksLink").hide(); 
		$("#hiddenPaneContent").empty(); 
		$("#hiddenPaneContent").append(data);
		$("#hiddenPane").css("left",Pane_Left); 
		$("#hiddenPane").css("top",Pane_Top+20); 
		$("#hiddenPane").fadeIn("slow"); 
		$("#userName").select().focus();
		$("#hiddenPaneContent").css("height",130+"px");
		
		$("#hiddenPaneContent").css("width",290+"px");
		$("#showBookmarksLink").show();
		$("#loadingShowBookmarksLink").hide();
		//$("#bookmarkName").focus();
		$("#txtId").select().focus();
		$("#bookmarkVal").val(xmlQueryData);
		//doDrag();
		//alert($("#bookmarkVal").val(xmlQueryData));
		//alert(data.indexOf("SIGN IN"));
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
 
  $("a#bookmarksSearchId").click(function(){
	Pane_Left=$(this).position().left-200;
	Pane_Top=$(this).position().top;
	maxRecords=2;
	//$("#clearPage").trigger("click");
	$("#hiddenPane").hide();
	$("#showBookmarksSearchLink").hide(); 	
	$("#loadingShowBookmarksSearchLink").show();
	$.get("<c:out value="${basePath}"/>/bookmark/showBookmarkSearch.action", {ajax: 'true'}, function(data) { 
		$("#loadingShowBookmarksSearchLink").hide(); 
		$("#hiddenPaneContent").empty(); 
		$("#hiddenPaneContent").append(data);
		$("#hiddenPane").css("left",Pane_Left); 
		$("#hiddenPane").css("top",Pane_Top+20); 
		$("#hiddenPane").fadeIn("slow"); 
		$("#hiddenPane").css("height","250px");
		$("#hiddenPane").css("width","320px");
		$("#userName").select().focus();
	    $("#hiddenPaneContent").css("height","150px");
		$("#hiddenPaneContent").css("width",290+"px");
		$("#showBookmarksSearchLink").show();
		$("#loadingShowBookmarksSearchLink").hide();
		$("#searchText").focus();
		//doDrag();

		if(data.indexOf("SIGN IN")==-1){
			if(data.indexOf("###searchBookMarksBox####")>-1){
			$("ul#browser li ul").hide();
			$("#hiddenPaneContent").css("width",320+"px");
			$("#hiddenPaneContent").css("height","auto");
			$("#hiddenPane").css("height","250px");
		$("#hiddenPane").css("width","320px");
			}
		}
		
		$.each($("li span.file"),function(){		   
			if($(this).find('.dType').text()!='S'){
				//$(this).parent('li').hide();
			$(this).find('.dVal').attr("title","Not applicable for this page");
			$(this).find('.dVal').css("color","#DFDFDF");
			$(this).find('.dVal').unbind("click");
			$("#hiddenPane").css("height","250px");
		    $("#hiddenPane").css("width","320px");
		 }else{
		   var applicationId='<s:property value="applicationId"/>';
		   if(applicationId){			
			  var bookmarkAppId=$(this).attr("appId");		
			  if(bookmarkAppId != -1 && applicationId != bookmarkAppId){
				   $(this).find('.dVal').attr("title","Not applicable for this page");
				   $(this).find('.dVal').css("color","#DFDFDF");
				   $(this).find('.dVal').unbind("click");
			  }	
			}
		 }
		});
		
		
	});
});
 
/* $(".qiHeaderDiv_new a#loginId").click(function(){
Pane_Left=$(this).position().left;
Pane_Top=$(this).position().top;
$("#showLoginLink").hide(); 	
$("#loadingShowLoginLink").show(); 							   
$.get("ajaxlogin.jsp", {ajax: 'true', noRedirect : 'true'}, function(data) { 
		$("#loadingShowLoginLink").hide(); 

		$("#hiddenPaneContent").empty(); 
		$("#hiddenPaneContent").append(data);
		$("#hiddenPane").css("left",Pane_Left+300); 
		$("#hiddenPane").css("top",Pane_Top+23); 
		$("#hiddenPane").fadeIn("slow");
		$("#hiddenPaneContent").css("width",290+"px");
		$("#hiddenPaneContent").css("height",130+"px");
		$("#showLoginLink").show();
		$("#userName").select().focus();
	});
});*/
function setOpacity(value) {
	testObj.style.opacity = value/10;
	testObj.style.filter = 'alpha(opacity=' + value*10 + ')';
}


 function sim(l,t){
	//alert("hi");
var shimmer = document.createElement('iframe');
        shimmer.id='shimmer';
        shimmer.style.position='absolute';
        // normally you would get the dimensions and 
        // positions of the sub div dynamically. For demo 
        // purposes this is hardcoded
        shimmer.style.width='220px';
        shimmer.style.height='100px';
		//alert(l+"::"+t)
        shimmer.style.top=t+105+'px';
        shimmer.style.left=l-240+'px';
        shimmer.style.zIndex='999';
		shimmer.setAttribute('frameborder','0');
		value=1;
		shimmer.style.opacity = value/10;
	    shimmer.style.filter = 'alpha(opacity=' + value*10 + ')';

        //shimmer.setAttribute('src','javascript:false;');
        document.body.appendChild(shimmer);
}
  $("a#loginId").click(function(){
Pane_Left=$(this).position().left;
Pane_Top=$(this).position().top;
if($("#PivotTable").html()!=null){
	sim(Pane_Left,Pane_Top);	
	}
$("#showLoginLink").hide(); 	
$("#loadingShowLoginLink").show(); 							   
$.get("<c:out value="${basePath}"/>/ajaxlogin.jsp", {ajax: 'true', noRedirect : 'true'}, function(data) { 
		$("#loadingShowLoginLink").hide(); 

		$("#hiddenPaneContent").empty(); 
		$("#hiddenPaneContent").append(data);
		$("#hiddenPane").css("left",Pane_Left-280); 
		$("#hiddenPane").css("top",Pane_Top+23); 
		$("#hiddenPane").fadeIn("slow");
		$("#hiddenPaneContent").css("width",290+"px");
		$("#hiddenPaneContent").css("height",160+"px");
		$("#showLoginLink").show();
		$("#hiddenPane").css("height","160px");
		$("#userName").select().focus();
	});
});
							 
$("#hiddenPane a#closeButtonId").click(function(){

		$("#hiddenPane").fadeOut("slow");
		$('#shimmer').remove();
        
	});
	

//Query population on back button click
  function convert(str){
  str = str.replace(/&amp;/g, "&");
  str = str.replace(/&gt;/g, ">");
  str = str.replace(/&lt;/g, "<");
  str = str.replace(/&quot;/g, '"');
  str = str.replace(/&#039;/g, "'");
  return str;
}
var data='<s:property value="requestString"/>';
var reqString=convert(data);
if(reqString){
   $("#metrics").empty();
   var type = '<s:property value="type"/>'; 
   if(convert(type) == 'SI'){   
   		$("#metrics").val(reqString);  
   	} 
}
  showLoginInfo('<security:authentication property="principal.fullName"/>');
  showPublisherInfo('<security:authentication property="principal.admin"/>','<security:authentication property="principal.publisher"/>');
 
 
 var loginUserId='<security:authentication property="principal.username"/>';
if(loginUserId=="guest"){
	//$("#publisherConsoleId").hide();
	//$("#publishAppId").show();
	
}else{
    var isPublisher='<security:authentication property="principal.user.isPublisher"/>'; 
	     if("YES"==isPublisher){
	          $("#publishAppId").hide();
			  $("#publisherConsoleId").show();			 		  			 
	       }else{
	        //  $("#publishAppId").show();
			 // $("#publisherConsoleId").hide();
	       }	
}
});// close of ready
</script>
<script>

	function showLoginInfo(name) {
		name = name.replace(/^\s+|\s+$/g,"");
		if(name && name.length > 0) {
			$("#showWelcome").empty().append('<s:text name="execue.login.welcome.message"/> ' + name);
			$("#userNameForComments").val(name);
			$("#loginId").hide();
			$("#logoutId").show();
			$("#showBookmarksSearchLink").show();
			$("#changePassword").show();
			$("#moredata").hide();
			$("#publishAppId").hide();
			  $("#publisherConsoleId").show();			 
		}	
	}
   function showPublisherInfo(admin,publisher) {
			if(admin=="true" ||publisher=="true"){
			$("#publisherTdSeperator").show();
			$("#publisherTd").show();
			$("#seperatorId").show();	
			$("#changePassword").show();
			$("#adminId").attr("href","<c:out value='${adminPath}' />swi/showSearchAppsDashboard.action");
			}
		}
function setDetailedResultsPerPage(resultsPerPage){
 var srcType = $("#typeId").val(); 
 var xmlValue = $("#requestedStringId").val();
 //alert(xmlValue);
   //alert(srcType); 
   if(srcType=="QI"){
  	 window.location="<%="./"+baseURL%>&resultsPerPage="+resultsPerPage+"&srcType=QI";
   }else{
	window.location="<%="./"+baseURL%>&resultsPerPage="+resultsPerPage+"&srcType=SI";
	}
}
/*$(document).mousemove(function(){
							   if($("#at15s")){
				$("#at15s").remove();
}
});*/
window.onload=function(){
processing=false;	
}
function js_RemoveChar(str){
	charToRemove = '"';
	
	regExp = new RegExp("["+charToRemove+"]","g");
	
	return str.replace(regExp,"'");

}
function showRecordsDiv(){

	var resultsPerPage = 50;
   	var requestedPage = 1;
   	var startRecord = 1;
   	var endRecord = 1;
   	var totalRecrods = 1;
   	var userPageSize = '<%=request.getSession().getAttribute("USER_DETAIL_PAGESIZE")%>';
   
   if('<%=baseURL%>'.length > 0 ) {
	   requestedPage = '<%=requestedPage%>';
	   totalRecrods = '<%=totalRecrods%>';
	   
	   if (userPageSize.length > 0){
	      resultsPerPage = userPageSize;
	   }
	   if(requestedPage == 1){
	      startRecord = 1;
	      endRecord = resultsPerPage * requestedPage;
	   } else {
	      startRecord = (resultsPerPage * requestedPage - resultsPerPage)+ 1;
	      endRecord = resultsPerPage * requestedPage;
	   }
	   if(endRecord > totalRecrods)
	      endRecord = totalRecrods;
	   
	   $("#recordsDiv").html('<b>Displaying ' + startRecord + ' - ' + endRecord + ' of ' + totalRecrods + ' records</b>');
   }
}
function  removeSpecialChar(text){
	$text=text;
$text=$text.replace(/#b9;/gi,"");		
			$text=$text.replace(/#b9;/gi,"");			
			$text=$text.replace(/#e0;/gi,"");		
			$text=$text.replace(/#e0;/gi,"");			
			$text=$text.replace(/#ae;/gi,"");		
			$text=$text.replace(/ae;/gi,"");
			$text=$text.replace(/#ff;/gi,"");				
			$text=$text.replace(/#c3;/gi,"");		
			$text=$text.replace(/##/gi,"");
			$text=$text.replace(/@@/gi,"");
			$text=$text.replace(/\^/gi,"");
			$text=$text.replace(/__/gi,"");
			$text=$text.replace(/\*/gi,"");
			$text=$text.replace(/~/gi,"");
			$text=$text.replace(/¿/gi,"");
			$text=$text.replace(/#be;/gi,"");		
			$text=$text.replace(/&#16;/gi,"");		
			$text=$text.replace(/#b6;/gi,"");		
			$text=$text.replace(/#d3/gi,"");			
			$text=$text.replace(/|/gi,"");			
			$text=$text.replace(/&amp;/gi,"");			
			$text=$text.replace(/;/gi,"");
			return $text;
}

$(document).ajaxError(function(e,xhr,settings) {
		$("#dynamicPane").text("Unable to process");
		$(".dynamicPaneBgLoader").css("backgroundImage","none");
		//alert(settings.url);
		

});

function showApplications(){	 
	$.get("qi/showCommunityApplicationsIncludingUserSpecificApps.action?type=SI",function(data){	
		$("#applicationListDiv").empty().append(data);		
		
	});


}
function getInternetExplorerVersion() {

    var rv = -1; // Return value assumes failure.

    if (navigator.appName == 'Microsoft Internet Explorer') {

        var ua = navigator.userAgent;

        var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");

        if (re.exec(ua) != null)

            rv = parseFloat(RegExp.$1);

    }

    return rv;

}

function checkVersion() {

    var msg = "You're not using Windows Internet Explorer.";

    var ver = getInternetExplorerVersion();

    if (ver > -1) {

        if (ver >= 8.0)

            msg = "You're using a recent copy of Windows Internet Explorer."

        else

            msg = "You should upgrade your copy of Windows Internet Explorer.";

    }

    alert(msg);

}
</script>
