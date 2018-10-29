<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="../css/admin/thickbox.css" type="text/css"
	media="screen" />
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet>
<%@ taglib prefix="s" uri="/struts-tags"%>

<style>
a.snapLink{
	padding:3px;
    text-decoration:underline;
}
a:hover.snapLink{
	padding:3px;
    text-decoration:none;
}
#snapShot{
margin-left:5px;
}
#snapShot ul{
list-style-position:outside;
margin:0px;
padding:0px;
padding-left:16px;
}
#snapShot ul li{
	width:100%;
	list-style-type:none;
	float:left;
	padding:4px;
}
.snapLinkDiv{
width:auto;
min-width:20px;
float:left;
}

.snapBit{
width:20px;
float:left;
background:url(../images/admin/deleteButt1.gif) no-repeat;
	cursor:pointer;
}
#snapShot{
width:370px;
}
</style>
<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript"></script>
<script language="JavaScript" src="../js/common/jquery.js"></script>
<script language="JavaScript" src="../js/admin/jquery.execue.js"></script>
<script language="JavaScript" src="../js/admin/menun.js"></script>
<script type="text/javascript" src="../js/common/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/admin/jquery.layout2.js"></script>
<script language="JavaScript" src="../js/mm_menu.js"></script>
<script type="text/javascript" src="../js/admin/thickbox.js"></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/admin/searchList.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/admin/jquery.execue.upload.js" type="text/javascript"></script>
<script type="text/javascript" src="../js/admin/jquery.execue.htmlResponse.js"></script>
<script type="text/javascript" src="../js/admin/execue.commons.js"></script>
<script type="text/javascript">
var myLayout;
var mode;
var paginationType="applicationsPagination";
$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
	//,	west__fxSettings_open: { easing: "easeOutBounce", duration: 750 }
  }); 
    var appId='<s:property value="application.id"/>';
  if(appId){
   	   $("#container").layout({
	    	west__initClosed: true
	  });
    showApplication(appId);
    //$(".ui-layout-west").hide();
  }else{
    getAppliations();
    addNewApplication();
  }

});


function showSearch(divName){
	obj=" ";
  $("#"+divName+obj+"#searchTables").hide("");
  $("#"+divName+obj+"#roundedSearch").fadeIn("slow");
  $('#searchText').val("");
   $('#searchText').focus(function(){
    if($(this).attr('value') == ''|| $(this).attr('value') == 'Search'){  //test to see if the search field value is empty or 'search' on focus, then remove our holding text
      $(this).attr('value', '');
    }
    $(this).css('color', '#000');
    
  });
  
  $('#searchText').blur(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
      $(this).attr('value', 'Search');
      $(this).css('color', '#777');
    }
  });
  
  $('#searchText').keydown(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
     showAll();
    }
  });
  
}
function showSearch2(divName){

	obj=" ";
  $("#"+divName+obj+"#searchTables2").hide();
  $("#"+divName+obj+"#roundedSearch2").fadeIn("slow");
   $('#searchText2').val("");
  $('#searchText2').focus(function(){
    if($(this).attr('value') == ''|| $(this).attr('value') == 'Search'){  //test to see if the search field value is empty or 'search' on focus, then remove our holding text
      $(this).attr('value', '');
    }
    $(this).css('color', '#000');
    
  });
  
  $('#searchText2').blur(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
      $(this).attr('value', 'Search');
      $(this).css('color', '#777');
    }
  });
  
  $('#searchText2').keydown(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
     showAll(2);
    }
  });
}

 function getAppliations(){
 $("#loadingAppsLink").show();
  //$.post("swi/getAllApplications.action?paginationType="+paginationType, function(data) {
  $.post("swi/getAllApplications.action", function(data) {      
    $("#dynamicPaneApplications").empty();
    $("#dynamicPaneApplications").append(data);
    $("#dynamicPaneApplications").show();
    $("#loadingAppsLink").hide();
  });
}
function showApplication(appId){	
	$.post("swi/getApplicationWithExample.action?application.id="+appId,function(data){
		$("#dynamicPane").fadeIn("fast");
	    $("#dynamicPane").empty();	    
	    $("#dynamicPane").append(data);
	    $("#dynamicPane").show();
	     /* set the form to be submitted like ajax for uploading file(s) */
        ajaxUpload("appDefinitionForm", "execueBody", "../swi/createApp.action");
	});
}
function addNewApplication(){
	$.post("swi/getApplicationWithExample.action",function(data){
		$("#dynamicPane").fadeIn("fast");
	    $("#dynamicPane").empty();	    
	    $("#dynamicPane").append(data);
	    $("#dynamicPane").show();
		$("#addNewUserLink").hide();
		 $("#applicationQueryExampleTable").hide();	 
		 /* set the form to be submitted like ajax for uploading file(s) */
        ajaxUpload("appDefinitionForm", "execueBody", "../swi/createApp.action");
	});	
}

function deleteApplication(appId){
    if(!appId){
      appId= '<s:property value="applicationContext.appId"/>'
    }
	var status=confirm("Are you sure ?");
	if(status){
	$("#deleteAppButton").hide();
	$("#showStatus").empty().show();
	$("#showStatus").jobStatus({
			requestJobURL : "../swi/deleteAppJob.action?application.id=" + appId,
			postDataForJobParam : function(){var data = {}; return data;},
			postCall : function(jobId,status) {updateDeleteInfo(jobId,status)}
	});
	}
}
function updateDeleteInfo(jobId,status) {
	$pubDiv = $("#showStatus");
	$pubDiv.show();
	if(status == "SUCCESS"){
		$pubDiv.empty().append("<span style='color:green' >App Deleted Sucessfully</span>");
		$("#deleteAppButton").hide();
		$("#enableUpdateApplication").hide();
	} else if(status == "FAILURE"){
		$pubDiv.empty().append("<span style='color:red' >App Deletion Failed, Please try again later</span>");
		$("#deleteAppButton").show();
	}
}

function showAll(searchBox){
  searchString=$("#searchText"+searchBox).val().toLowerCase();	
  if(searchString=="search"){
    alert("please enter search string");
	$("#searchText"+searchBox).focus();
	}else{
	$.get("swi/getAllSubApplications.action",{}, function(data) {
       $("#dynamicPaneApplications").empty();
       $("#dynamicPaneApplications").append(data);
     });//End of populate BusinessTerms
	}  
}

function startsWithString(searchBox){

  searchString=$("#searchText"+searchBox).val().toLowerCase();	
  if(searchString=="search"){
    alert("please enter search string");
	$("#searchText"+searchBox).focus();
	}else{
	$.get("swi/getAllSubApplications.action?searchString="+searchString+"&searchType=startWith",{}, function(data) {
       $("#dynamicPaneApplications").empty();
       $("#dynamicPaneApplications").append(data);
     });//End of populate BusinessTerms
	}  
}

function containsString(searchBox){

searchString=$("#searchText"+searchBox).val().toLowerCase();	
 if(searchString=="search"){
    alert("please enter search string");
	$("#searchText"+searchBox).focus();
	}else{
	$.get("swi/getAllSubApplications.action?searchString="+searchString+"&searchType=contains",{}, function(data) {
       $("#dynamicPaneApplications").empty();
       $("#dynamicPaneApplications").append(data);
     });//End of populate BusinessTerms
	}  
}

function subList(index_id){

requestedPage=index_id;
var actionName="";
		
	actionName="getAllSubApplications.action";
	$.post("swi/"+actionName+"?requestedPage="+index_id+"&paginationType="+paginationType, function(data) {
     $("#dynamicPaneApplications").empty();
           $("#dynamicPaneApplications").append(data);
    var len1,len2=0;
   
   // $("#columnListForm .col").width(130);	
   // $("#columnIno").show();
   

  });
}
function handleUploadRequest () {
  $("#errorMessage").empty(); 
    $("#appSourceTypeId").attr("disabled", false);
      $("#appSourceTypeId").attr("readonly", true);
  //$("#enableCreateApplication").show();
 // $("#updateProcess").show();
} // End of handleUploadRequest()

/*
 * handling the response for upload file functionality 
 */
 
function handleUploadResponse (returnedData) {
	 
	checkMode();

	if (returnedData.status) {
		$("#uploadImageDynamicContent").empty();   
		var htmlContent = buildResponse(returnedData);	
		$("#uploadImageDynamicContent").append(htmlContent);
		$("#uploadImageDynamicContent").show();
		$("#dynamicImageOuterDivBorder").show();
		$("#applicationQueryExampleTable").show();	 
		$("#dynamicImageOuterDiv").show().empty().html("<img src='../images/admin/loading.gif'  style='padding-left:0px;' />");
		if(returnedData.applicationImageId!=-1){
			setTimeout(function(){$("#dynamicImageOuterDiv img").width(90).height(90).css("paddingLeft","5px").attr("src","getImage.action?applicationId="+returnedData.applicationId+"&appImageId="+returnedData.applicationImageId);},2000);
		}else{
			setTimeout(function(){$("#dynamicImageOuterDiv img").width(90).height(90).css("paddingLeft","5px").attr("src","../images/admin/noImage-icon.gif");},2000);	
		}
	} else {  
		$.each(returnedData.errorMessages, function () {
			$("#errorMessage").append("<li>"+this+"<\li>");
		    // $("#updateProcess").hide();
	        // $("#enableCreateApplication").hide();
    	});
	}
} // end of handleUploadResponse()

function buildResponse(returnedData) {
	var htmlContent = staticMsga;	
	if(returnedData.message){
		htmlContent = htmlContent + "<span class='green'>"+returnedData.message+"<\span>";	
		$("#enableCreateApplication").hide();
		$("#enableUpdateApplication").show();
		$("#deleteAppButton").show();	
		$("#resetButton").hide();	  
		$("#updateProcess").hide();
		//$("#appName").attr("readonly","true");
		$("#applicationId").val(returnedData.applicationId);
		$("#appUrl").val(returnedData.applicationURL);
		$("#applicationExampleAppId").val(returnedData.applicationId);
		$("#appSourceTypeId").attr("disabled", true);
		showApplicationURLLink ();
		mode = 'update';
	}	
	return htmlContent;
}


/* Static content declaration Start */
var staticMsga = "<div id='errorMessage'>";
/* Static content declaration end */

function setAppExampleValue(name,id,type){
	$("#applicationExampleId").val(id);
	$("#userInterfaceTypeId").val(type);
	$("#example").val(name);
}
function resetAppExampleValue(){
	$("#applicationExampleId").val("");
	$("#userInterfaceTypeId").val("");
	$("#example").val("").focus();
}




function deleteAppExample(exampleId,appId){
    $.ajax({
      type: "POST",
      dataType: "json",
      url: "swi/deleteAppExample.action?applicationExample.id="+exampleId+"&applicationExample.application.id="+appId,
      success: addToSnapShot
    });
}
function isEmpty(str) {
    return (!str || 0 == str.length);
}
function saveAppExmaple(){
var example =$("#example").val();
if(isEmpty(example)){
	$("#alertMessaage").html("Please enter example");
	$("#alertMessaage").show();
	 setTimeout(function(){$("#alertMessaage").empty();},3000);
	return false;
}else{
	$("#alertMessaage").hide();
}
if($("#snapShot ul li").length>=10){
	$("#alertMessaage").show().html("You can not save examples more than 10 ");
	return false;
}else{ $("#alertMessaage").hide(); }

 var appExampleForm=$("#appExampleForm").serialize();
    $.ajax({
      type: "POST",
      dataType: "json",
      url: "swi/saveUpdateAppExample.action",
      data: appExampleForm,
      success: addToSnapShot
    });
}
function addToSnapShot(data){
$("#snapShot").empty();
resetAppExampleValue();
var $ul=$("<ul></ul>");
      $.each(data,function(k,v){
     var $li=$("<li></li>");
     var $div=$("<div style='float:left;width:auto;'></div>");
	 var $div2=$('<div style="float:left;width:18px;padding-top:3px;">&bull;</div>');
      var $a=$("<a href='#' class='snapLink'></a>").click(function(){
      setAppExampleValue(v.queryName,v.id,v.type);
      });
      $a.html(v.queryName);
      $div.append($a);
	  $li.append($div2);
      $li.append($div);
      $ul.append($li);
      
      var $snapBit = $("<div class='snapBit' alt='Delete' title='Delete' >&nbsp;</div>").click(function(){
										deleteAppExample(v.id,v.applicationId);			  
																  });;
		$li.append($snapBit);

      });
$("#snapShot").append($ul);
}

</script>
