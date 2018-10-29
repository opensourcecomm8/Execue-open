var autoSuggestClicked=false;
var tabIn=0;
var xmlQueryData;
var toggle=true;
var selectTextProcessed;
var processing=false;
var str="Search all Published Apps ("+appCount+" & growing...)";
var disableCache=false;

$(document).ready(function(){
		if($("#metrics").val()==""){
			$("#metrics").val(str);	
			$("#metrics").blur();
			//setTimeout(function(){  $("#metrics").blur();},3000);
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



$(".exampleQueries a").click(function(){
		$("#metrics").val($(this).attr("title")); 
			example=setTimeout(showHide,200);
			//$("#searchBtnFreeForm").click();							  
  });



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
	
	
	
	if(applicationId){
	  url=basePath+'querySuggest/semanticSuggest.action?applicationId='+applicationId;
	}else{
	   url=basePath+'querySuggest/semanticSuggest.action';
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
							
						    if(autoSuggestClicked){$("#searchBtnFreeForm").click(); return false;}
						if(autoSelect){ autoSelect=false; return false;}
						else{
						$("#searchBtnFreeForm").focus();
						$("#searchBtnFreeForm").removeClass("imgNoBorder1");
						$("#searchBtnFreeForm").addClass("imgBorder1");
						//$("#searchBtnFreeForm").trigger('click');
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
	
	$.get(basePath+"bookmark/showUserFolders.action", {ajax: 'true'}, function(data) { 											  
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
	$.get(basePath+"bookmark/showBookmarkSearch.action", {ajax: 'true'}, function(data) { 
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
			$("#hiddenPane").css("height","150px");
		$("#hiddenPane").css("width","320px");
			}
		}
		$.each($("li span.file"),function(){		   
			if($(this).find('.dType').text()!='S'){
				$(this).parent('li').hide();
			$(this).find('.dVal').attr("title","Not applicable for this page");
			$(this).find('.dVal').css("color","#DFDFDF");
			$(this).find('.dVal').unbind("click");
			$("#hiddenPane").css("height","250px");
		$("#hiddenPane").css("width","320px");
		 }
		});
		
		
	});
});


$("a#loginId").click(function(){
Pane_Left=$("#searchBtnFreeForm").position().left-200;
Pane_Top=$("#searchBtnFreeForm").position().top+143;
if($("#PivotTable").html()!=null){
	sim(Pane_Left,Pane_Top);	
	}
$("#showLoginLink").hide(); 	
$("#loadingShowLoginLink").show(); 							   
$.get("ajaxlogin.jsp", {ajax: 'true', noRedirect : 'true'}, function(data) { 
		$("#loadingShowLoginLink").hide(); 

		$("#hiddenPaneContent").empty(); 
		$("#hiddenPaneContent").append(data);
		$("#hiddenPane").css("left",Pane_Left-240); 
		$("#hiddenPane").css("top",Pane_Top+23); 
		$("#hiddenPane").fadeIn("slow");
		$("#hiddenPaneContent").css("width",290+"px");
		$("#hiddenPaneContent").css("height",160+"px");
		$("#showLoginLink").show();
		$("#hiddenPane").css("height","160px");
		$("#userName").select().focus();
	});
});
							 
	$("#hiddenPane a#closeButtonId,img#boxTemplate_r1_c2").click(function(){

		$("#hiddenPane").fadeOut("slow");
		$('#shimmer').remove();
        
	});
	
	
	
 });


window.onload=function(){
processing=false;	
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
			$text=$text.replace(/#be;/gi,"");		
			$text=$text.replace(/&#16;/gi,"");		
			$text=$text.replace(/#b6;/gi,"");		
			$text=$text.replace(/#d3/gi,"");			
			$text=$text.replace(/|/gi,"");			
			$text=$text.replace(/&amp;/gi,"");			
			$text=$text.replace(/;/gi,"");
			return $text;
}


 function showPublisherInfo(admin,publisher) {
			if(admin=="true" ||publisher=="true"){
			$("#publisherTdSeperator").show();
			$("#publisherTd").show();
			$("#changePassword").show();
			$("#adminId").attr("href",adminPath+"swi/showSearchAppsDashboard.action");
			}
		}
function setDetailedResultsPerPage(resultsPerPage){
 var srcType = $("#typeId").val(); 
 var xmlValue = $("#requestedStringId").val();
 //alert(xmlValue);
   //alert(srcType); 
   if(srcType=="QI"){
  	 window.location=basePath+"biHome.action?type=QI&resultsPerPage="+resultsPerPage+"&srcType=QI";
   }else{
	window.location=basePath+"execueHome.action?type=SI&resultsPerPage="+resultsPerPage+"&srcType=SI";
	}
}

$(document).ajaxError(function(e,xhr,settings) {
		$("#dynamicPane").text("Unable to process");
		$(".dynamicPaneBgLoader").css("backgroundImage","none");
		//alert(settings.url);
		

});


function refreshClicked(){
disableCache=true; 
$("#searchBtnFreeForm").click();
}
function submitDataFreeForm() {
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
	
	return false;
	}
}

function submitDataCraigslistFreeForm() { 
	$("div.ac_results").hide();
	tempImg = $("#waiting_img").attr("src");
    var data=$("#metrics").val();    
    var type=$("#type").val();
	if((data=="")||(data.indexOf(str)>-1)){
		alert("Please Enter Data"); processing=false; $("#metrics").focus(); $("#searchBtnFreeForm").removeClass("imgBorder1");
						$("#searchBtnFreeForm").addClass("imgNoBorder1"); return false}
	if(disableCache){$("#disableCache").val('true'); }
	$('#request').val(data);
	$('#type').val(type);
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
	
	
	/*if(apId=="1508"){ //todo this is hard coded id . we need to think other solution
		var $formData=$("#searchFormId").serialize(); 
		$.get("<c:out value="${basePath}"/>/semanticSearch.action?"+$formData,function(data){
					displayDataInDiv(data);	  
						  });
		return false;
	}*/
}


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
		jsonExamples = $.get(basePath+"querySuggest/showAppSpecificExamples.action?applicationId="+applicationId,
		function(data) {
		    var data=eval(data);
		    $("#queries").html(openTable);
		     $.each(data, function(i, query) {
		    	prepareExamplesTable(query.queryName);
		    });
		    $("#queries").append(closeTable);
		    $("#queriesDiv td a").bind("click",function(){
				$("#metrics").val($(this).text());
				$("#searchBtnFreeForm").click();
			});
	  	});
	}else{
		jsonExamples = $.get(basePath+"querySuggest/showAppExamples.action",function(data) {
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
  
	
//$("#metrics").focus();
//setTimeout(function(){$("#metrics").val("");},100);
//Query population on back button click
  function convert(str){
  str = str.replace(/&amp;/g, "&");
  str = str.replace(/&gt;/g, ">");
  str = str.replace(/&lt;/g, "<");
  str = str.replace(/&quot;/g, '"');
  str = str.replace(/&#039;/g, "'");
  return str;
}
function showLoginInfo(name) {
		name = name.replace(/^\s+|\s+$/g,"");
		if(name && name.length > 0) {
			$("#showWelcome").empty().append(welcomeMessage + name);
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
	
	
	function showVerticalPopup() { 
	if(popupTimer){
		clearTimeout(popupTimer);
	}
	var $left=$("#verticalMoreImg").position().left;
	$("#verticalPopup").css("left",$left-90+"px");
	$("#verticalPopup").show();
	if(!jsonVerticals) {
		getVerticals();
	}
	$("#verticalMoreImg").attr("src","images/moreButtonHover.png");
}

function hideVerticalPopup() {
	$("#verticalMoreImg").attr("src","images/moreButton.png"); 
	popupTimer=setTimeout(function(){$("#verticalPopup").hide();}, 3000);
} 


function getVerticals() {
	if($("#otherVerticals table[id='cat_menu'] td a").length == 0){
		jsonVerticals = $.get(basePath+"querySuggest/showVisibleVerticals.action", function(data) {
		    var data=eval(data);
		     $.each(data, function(i, item) {
		    	prepareTable(item);
		    });
		    $("#otherVerticals table[id='cat_menu'] td a").bind("click", function() {
				$("#searchString").val(str);
				$(this).blur();
				window.location=basePath+"vertical/verticalHome.action?verticalId="+$(this).attr("id");
			});
	  	});
  	}
}

function prepareTable(item) {
	var openTag = '<tr><td> <span class="arrowOrange" > > </span> <a href="#" id=' + item.id + ' name=' + item.name + ' class="no-active">' + item.name + '</a></td></tr>';
	$("#otherVerticals table[id='cat_menu']").append(openTag);
}

 

function showPopupVideo(){
	        $("#showBigImage").slideDown("slow");
			var $left=$("#playVideoImage").position().left;
			var $top=$("#playVideoImage").position().top;
			$("#showBigImage").css("left",$left-450+"px");
			$("#showBigImage").css("top",$top+58+"px");
}
function closeImage(){
$("#showBigImage").slideUp("slow");	
}
 function CheckText(e, text){ 
        if(e.value == text){ 
            e.value = ''; 
        } else{
			e.value = text; 
		}
    } 
	
	
var popupTimer;


$(document).ready(function(){
		
	
		$("#closeLinkDiv").click(function(){
			$(this).blur();
			$("#hiddenPane").fadeOut();								 
		});
		
		$("#verticalPopup").live("mouseover",function(){
		clearTimeout(popupTimer);
		});
	
		$("#verticalPopup").live("mouseout",function(){
		popupTimer=setTimeout(function(){$("#verticalPopup").hide();  },2000);
		});

var reqString=convert(data);
if(reqString){
   $("#metrics").empty();
   
   if(convert(type) == 'SI'){   
   		$("#metrics").val(reqString);  
   	} 
}

if(loginUserId!="guest"){
    
	     if("YES"==isPublisher){
	          $("#publishAppId").hide();
			  $("#publisherConsoleId").show();			 		  			 
	       }
}
  showLoginInfo(fullName);
  showPublisherInfo(pricipalAdmin,principalPublisher);
  $("#metrics").blur();


});