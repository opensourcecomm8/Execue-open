<%@ taglib prefix="s" uri="/struts-tags"%>

<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet" type="text/css">
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet" type="text/css">
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<Link href="../css/common/roundedSearch.css" rel="stylesheet" type="text/css">

<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript" language="JavaScript"></script>
<script src="../js/common/jquery.js" type="text/javascript" language="JavaScript"></script>
<script src="../js/admin/menun.js" type="text/javascript" language="JavaScript"></script>
<script src="../js/common/mm_menu.js" type="text/javascript" language="JavaScript"></script>
<script src="../js/common/jquery.ui.all.js" type="text/javascript" language="JavaScript"></script>
<script src="../js/common/jquery.layout.js" type="text/javascript" language="JavaScript"></script>
<script src="../js/admin/jquery.execue.upload.js" type="text/javascript" language="JavaScript"></script>
<script src="../js/admin/searchList.js" type="text/javascript" language="javascript" /></script>
<script src="../js/admin/script.js" type="text/javascript" language="javascript"></script>
<script src="../js/admin/jquery.execue.jobStatus.js" type="text/javascript" language="JavaScript"></script>
<script src="../js/admin/jquery.execue.helpMessage.js" type="text/javascript" language="JavaScript"></script>

<style>
.nolink {
	text-decoration: none;
	color: gray;
	cursor: text;
	a: active :         gray;
}
#configDynamicPane select{
	width: 105px;
	float:left;
}
</style>

<script type="text/javascript" language="JavaScript">

// Tabbed Panels setup -- Start

var tabbedPanelsRef;

var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method

$(document).ready(function() {
	
	myLayout = $('#container').layout({
    	west__showOverflowOnHover: true
  	});
  
  	//Tabbed Panels should only be defined when the corresponding div is loaded
  	tabbedPanelsRef = new Spry.Widget.TabbedPanels("TabbedPanelsId"); 
   	
});

//Tabbed Panels setup -- End

// Page handling functionality -- Start

function saveCommonConfigurations () {
	clearStatusMessages();
	
	var formData = $("#commonConfigurationsForm").serialize();

	$.post("../swi/saveCommonConfigurations.action",formData,function(data){
		$("#filler").hide();
		if (data != null && data.status == 'success') {
			setSuccessMessage ("Common", data.message);
		} else {
			setFailureMessage ("Common", data.message);
		}
	});
}

function saveAnswersCatalogfigurations () {
	clearStatusMessages();
	
	var formData = $("#answersCatalogConfigurationsForm").serialize();;
	
	$.post("../swi/saveAnswersCatalogConfigurations.action",formData,function(data){
		$("#filler").hide();
		if (data != null && data.status == 'success') {
			setSuccessMessage ("AnsCat", data.message);
		} else {
			setFailureMessage ("AnsCat", data.message);
		}
	});
}

function saveReportAggregationfigurations () {
	clearStatusMessages();
	
	var formData = $("#reportAggregationConfigurationsForm").serialize();;

	$.post("../swi/saveReportAggregationConfigurations.action",formData,function(data){
		$("#filler").hide();
		if (data != null && data.status == 'success') {
			setSuccessMessage ("RepAgg", data.message);
		} else {
			setFailureMessage ("RepAgg", data.message);
		}
	});	
}

function saveReportPresentationfigurations () {
	clearStatusMessages();
	
	var formData = $("#reportPresentationConfigurationsForm").serialize();;
	
	$.post("../swi/saveReportPresentationConfigurations.action",formData,function(data){
		$("#filler").hide();
		if (data != null && data.status == 'success') {
			setSuccessMessage ("RepPre", data.message);
		} else {
			setFailureMessage ("RepPre", data.message);
		}
	});	
}

function clearStatusMessages () {
	$("#successMessage"+"Common").empty();
	$("#successMessage"+"Common").append("&nbsp;");
	$("#failureMessage"+"Common").empty();
	$("#failureMessage"+"Common").append("&nbsp;");

	$("#successMessage"+"AnsCat").empty();
	$("#successMessage"+"AnsCat").append("&nbsp;");
	$("#failureMessage"+"AnsCat").empty();
	$("#failureMessage"+"AnsCat").append("&nbsp;");

	$("#successMessage"+"RepAgg").empty();
	$("#successMessage"+"RepAgg").append("&nbsp;");
	$("#failureMessage"+"RepAgg").empty();
	$("#failureMessage"+"RepAgg").append("&nbsp;");

	$("#successMessage"+"RepPre").empty();
	$("#successMessage"+"RepPre").append("&nbsp;");
	$("#failureMessage"+"RepPre").empty();
	$("#failureMessage"+"RepPre").append("&nbsp;");
}

function setSuccessMessage (suffix, message) {
	$("#successMessage"+suffix).append(message);
	$("#failureMessage"+suffix).hide();
	$("#successMessage"+suffix).show();
}

function setFailureMessage (suffix, message) {
	$("#failureMessage"+suffix).append(message);
	$("#successMessage"+suffix).hide();
	$("#failureMessage"+suffix).show();
}

//Page handling functionality -- End

</script>