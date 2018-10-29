<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css">
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet type="text/css">

<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript"></script>
<script src="../js/common/jquery.js" language="JavaScript"></script>
<script src="../js/admin/menun.js" language="JavaScript"></script>
<script src="../js/common/mm_menu.js" language="JavaScript"></script>
<script src="../js/common/jquery.ui.all.js" type="text/javascript"></script>
<script src="../js/common/jquery.layout.js" type="text/javascript"></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript"></script>
<script src="../js/admin/searchList.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/admin/jquery.execue.helpMessage.js" ></script>

<style type="text/css">
#errorMessage {
	font-family: arial;
	font-style: italic;
	font-size: 10px;
	color: red;
	margin: 0px;
	padding: 0px;
	background-repeat: repeat-x;
	border: 0px solid #D0D9D9;
	background-color: #FFF;
	text-align: left;
}

li {
	list-style-type: none;
}
</style>

<script type="text/javascript">
var paginationType="availableAssets";
$(document).ready(function() {			 
  showAccount();
});

function showChangePass(){
  $.get("showChangePassword.action",{},function(data){									 
			$("#dynamicPane").empty().html(data);
			$("#myLink").text("Show Account");
			$("#myLink").attr("href","javascript:showAccount();");
	 });
}		
function showAccount(){
	$.get("getUserProfile.action",{},function(data){
			$("#dynamicPane").empty().html(data);
			$("#myLink").text("Change Password");
			$("#myLink").attr("href","javascript:showChangePass();");
	 });
}

function updateProfile() {  
  var userProfileData = $("#updateProfileForm").serialize();
  $.post("account/updateUserProfile.action", userProfileData, function(data) {    
        $("#dynamicPane").empty().html(data);
        $("#myLink").text("Change Password");
		$("#myLink").attr("href","javascript:showChangePass();");
  });  
}

function changePassword() {  
  var changePasswordData = $("#changePasswordForm").serialize();
  $.post("account/updatePassword.action", changePasswordData, function(data) {    
        $("#dynamicPane").empty().html(data);
	    $("#myLink").text("Show Account");
		$("#myLink").attr("href","javascript:showAccount();");
  });  
}

</script>
