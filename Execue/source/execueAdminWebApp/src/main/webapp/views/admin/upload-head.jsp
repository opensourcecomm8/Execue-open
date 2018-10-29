<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/common/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css" />
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet>

<script src="../js/common/jquery.js" language="JavaScript" /></script>
<script src="../js/admin/menun.js" language="JavaScript" /></script>
<script src="../js/common/mm_menu.js" language="JavaScript" /></script>
<script src="../js/common/jquery.ui.all.js" type="text/javascript" /></script>
<script src="../js/common/jquery.layout.js" type="text/javascript" /></script>
<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript" /></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/admin/searchList.js" type="text/javascript"
	language="javascript" /></script>

<script type="text/javascript">


var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method



$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
  });
  
  
 
});

function showApplications(){	
	$.get("../publisher/showApplications.action",function(data){
		$("#contentInner").fadeIn("fast");
	    $("#contentInner").empty();
	    $("#contentInner").append(data);
	    $("#contentInner").show();
	});	  
  	getAllApplication();
 	addNewApplication(); 
}
function getAllApplication(){
	$("#loadingAppsLink").show();
	$.post("../swi/getAllApplications.action", function(data) {   
	    $("#dynamicPaneApplications").empty();
	    $("#dynamicPaneApplications").append(data);
	    $("#dynamicPaneApplications").show();
	    $("#loadingAppsLink").hide();
	});		 
}
function showApplication(){	
	$.post("../swi/getApplicationInfo.action",function(data){
		$("#centerPane").fadeIn("fast");
	    $("#centerPane").empty();	    
	    $("#centerPane").append(data);
	    $("#centerPane").show();
	});
}
function addNewApplication(){
	$.post("../swi/getApplicationInfo.action",function(data){	
		$("#centerPane").fadeIn("fast");
	    $("#centerPane").empty();
	    $("#centerPane").append(data);
	    $("#centerPane").show();
	});
}
function createUpdateApplication(operation){
		if(operation=="create"){
			enableApplication="#enableCreateApplication";
			if($("#appName").val()=='' || $("#appName").val()==null){
				alert("please enter application name");
				return false;
			}		
		}else{
			enableApplication="#enableUpdateApplication";
		}
		loadingProcess="#updateProcess"	
		$(enableApplication).hide();
		$(loadingProcess).show();	
		$appInfo=$("#appDefinitionForm").serialize();
		$.post("../swi/createUpdateApp.action",$appInfo , function(data) {
		    $("#centerPane").fadeIn("fast");
		    $("#centerPane").empty();
		    $("#centerPane").append(data);
		    $("#centerPane").show();
		    $(enableApplication).show();
		    $("#appName").select().focus();
		    getAllApplication();
	   		//showUploadCSVFileInfo();
  	    });  
}
function showUploadCSVFileInfo(){	
		$.post("../publisher/uploadStatus.action",function(data){
			$("#contentInner").fadeIn("fast");
		    $("#contentInner").empty();
		    $("#contentInner").append(data);
		    $("#contentInner").show();
		});
	}
</script>
