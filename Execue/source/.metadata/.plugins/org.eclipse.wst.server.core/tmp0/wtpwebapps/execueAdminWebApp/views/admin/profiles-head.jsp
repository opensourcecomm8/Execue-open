<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet" type="text/css" />
<link href="../css/common/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/common/ui-layout-styles.css" rel="stylesheet" type="text/css" />
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet>
<LINK href="../css/admin/cssPopupMenu.css" rel="stylesheet" type="text/css">

<script src="../js/common/jquery.js" language="JavaScript" /></script>
<script src="../js/admin/menun.js" language="JavaScript" /></script>
<script src="../js/common/mm_menu.js" language="JavaScript" /></script>
<script src="../js/common/jquery.ui.all.js" type="text/javascript" /></script>
<script src="../js/admin/jquery.layout2.js" type="text/javascript" /></script>
<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript" /></script>
<script src="../js/admin/script.js" type="text/javascript" language="javascript" /></script>
<script src="../js/admin/searchList.js" type="text/javascript" language="javascript" /></script>
<script src="../js/admin/jquery.execue.helpMessage.js" ></script>
<script type="text/javascript" src="../js/admin/jquery.execue.searchRecordsComponent.js"></script>
<script type="text/javascript">


var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method
var paginationType="conceptsForProfiles";


$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
  });
  
  showConcepts();
  getUserProfiles();
   $("#divProfiles").searchRecordsComponent({actionName:"swi/showSubConceptsforProfiles.action?paginationType="+paginationType,targetDivName:"dynamicPaneProfileConcepts"});
   $("#divUserDefinedProfiles").searchRecordsComponent({actionName:"",targetDivName:"profilesForm"});
});
function showConcepts(){
  $.get("swi/showConceptsforProfiles.action?paginationType="+paginationType, function(data) {
      $("#dynamicPaneProfileConcepts").empty();
      $("#dynamicPaneProfileConcepts").append(data);
    });//End of populate BusinessTerms
}
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
$tmpProfileStatus="";
$tmpNewProfileStatus="";
function getProfileDefinition(profileId, profileType) {	
  if($tmpProfileStatus!=profileId){
	var userProfile="#showUserProfile";
	var loadingUserProfile="#loadingShowUserProfile";
	$(userProfile+profileId+"Link").hide();
	$(loadingUserProfile+profileId+"Link").show();	
  $.post("swi/profileDefinition.action?profileId="+profileId+"&profileType="+profileType, {}, function(data) {
  	$(loadingUserProfile+profileId+"Link").hide();
    $("#dynamicPane").fadeIn("fast");
    $("#dynamicPane").empty();
    $("#dynamicPane").append(data);
    $("#dynamicPane").show();
    $(userProfile+profileId+"Link").show();
  });
   $tmpProfileStatus=profileId;
  }
}

function getNewProfileDefinition(conceptId, profileType,hybrid) {
var conceptTempId='0';
if(conceptId){
//this check is for the concept profile creation.
  conceptTempId=conceptId;
}
if($tmpNewProfileStatus!=conceptTempId){  
	var showProfile="#showProfile";
	var loadingProfile="#loadingShowProfile";
	if(hybrid=='YES'){
       showProfile="#showProfileHybrid";
       loadingProfile="#loadingShowProfileHybrid";
    }
	$(showProfile+conceptId+"Link").hide();
	$(loadingProfile+conceptId+"Link").show();	
  $.post("swi/newProfileDefinition.action?conceptId="+conceptId+"&profileType="+profileType+"&hybridProfile="+hybrid, {}, function(data) {
  	$(loadingProfile+conceptId+"Link").hide();	
    $("#dynamicPane").fadeIn("fast");
    $("#dynamicPane").empty();
    $("#dynamicPane").append(data);
    $("#dynamicPane").show();
    $(showProfile+conceptId+"Link").show();
    $("#displayName").select().focus();
  });
  	$tmpNewProfileStatus=conceptId;
  }
}

/* to get the exisiting user profiles */
function getUserProfiles(){
  $.post("swi/userProfiles.action" , function(data) { 
    $("#rightPane").empty();
    $("#rightPane").append(data);
    $("#rightPane").show();
    $tmpNewProfileStatus="";
  });//End of populating existing user profiles
}
function displayMessage(msg){
	$("#errorMessage").empty();
	$("#errorMessage").append(msg);
	$("#errorMessage").show();
}
function updateProfile() {
	var profName=$("#displayName").val();
	var invalid=/[^A-Za-z\d_ ]/;
	if(parseInt(profName.substring(0,1))>=0){
		$msg="Profile name has to be alpha numeric.";
		displayMessage($msg);
		return false;
	}else if(invalid.test(profName)){
		$msg="Profile name should not contain any special characters except underscore and space.";
		displayMessage($msg);
		return false;
	}else if(profName.length<3 || profName.length>25){ 
		$msg="Profile name should be min 3 and max 25 characters.";
		displayMessage($msg);
		return false;
	}	
  var profileDefinition = $("#profileDefinition").serialize();
  // alert(profileDefinition);
  $("#updateProcess").show();
  $("#enableupdateProfile").hide();
  $.getJSON("swi/createProfile.action", profileDefinition ,function(data) {
    if (data.status == 'success') {
      if ($("#profileDefinition_profileType").val() == 'CONCEPT' && data.conceptProfileId != null && $("#profileDefinition_conceptProfile_id").val() == '') {
        // getProfileDefinition(data.conceptProfileId, 'CONCEPT');
        //$("#profileDefinition_conceptProfile_id").val(data.conceptProfileId);
        //$("#profileDefinition_conceptProfile_name").val(data.conceptProfileName);
      } else if ($("#profileDefinition_profileType").val() == 'INSTANCE' && data.instanceProfileId != null && $("#profileDefinition_instanceProfile_id").val() == '') {
        // getProfileDefinition(data.instanceProfileId, 'INSTANCE');
        //$("#profileDefinition_instanceProfile_id").val(data.instanceProfileId);
        //$("#profileDefinition_instanceProfile_name").val(data.instanceProfileName);
      }
      $("#updateProcess").hide();
  	  $("#enableupdateProfile").show();
      $("#dynamicPane").fadeIn("fast");
      $("#dynamicPane").empty();
      $("#dynamicPane").append("<div id='errorMessage' STYLE='color: red' align='left'>"+data.message+"</div>");
      $("#dynamicPane").show();
    } else {    
      $("#updateProcess").hide();
  	  $("#enableupdateProfile").show();
      $("#errorMessage").empty();
      $("#errorMessage").append(data.message);
    }    
    $tmpProfileStatus="";  
    getUserProfiles();
  });
}

function deleteProfile() {
var answer = confirm("Are you sure?")
    if (answer){ 
	  var profilesFormData = $("#profilesForm").serialize();
	  //alert(profilesFormData);
	  $("#deleteProcess").show();
	  $("#enableDeleteSelect").hide();
	  $.getJSON("swi/deleteProfiles.action", profilesFormData ,function(data) {
	    getUserProfiles();
	    $("#deleteProcess").hide();
	  	$("#disableDeleteSelect").show();
	    $("#dynamicPane").fadeIn("fast");
	    $("#dynamicPane").empty();
	    $("#dynamicPane").append("<div id='errorMessage' STYLE='color: red' align='left'>"+data.message+"</div>");
	    $("#dynamicPane").show();
	    $tmpProfileStatus="";
	  });
  }
  return false;
 
}    
function checkedProfile(){
	 var checkd=false;	
     checkd= $("#profilesForm input:checkbox").is(':checked');
     if(checkd){
	    $("#enableDeleteSelect").show();
	    $("#disableDeleteSelect").hide();
	 }else{
	    $("#enableDeleteSelect").hide();
	    $("#disableDeleteSelect").show();
	}
}

function subList(index_id){

requestedPage=index_id;
var actionName="";
	
		
			actionName="showSubConceptsforProfiles.action";
			$.post("swi/"+actionName+"?requestedPage="+index_id+"&paginationType="+paginationType, function(data) {
		     $("#dynamicPaneProfileConcepts").empty();
             $("#dynamicPaneProfileConcepts").append(data);
		    var len1,len2=0;
		   
		   // $("#columnListForm .col").width(130);	
		   // $("#columnIno").show();
		   

		  });
			
	   
	 
}
</script>