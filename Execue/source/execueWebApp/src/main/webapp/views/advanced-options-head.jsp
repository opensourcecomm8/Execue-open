<link href="../css/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css">
<link href="../css/styles.css" rel="stylesheet" type="text/css">
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/roundedSearch.css" rel=stylesheet type="text/css">

<script src="../js/SpryTabbedPanels.js" type="text/javascript"></script>
<script src="../js/jquery.js" language="JavaScript"></script>
<script src="../js/menun.js" language="JavaScript"></script>
<script src="../js/mm_menu.js" language="JavaScript"></script>
<script src="../js/jquery.ui.all.js" type="text/javascript"></script>
<script src="../js/jquery.layout.js" type="text/javascript"></script>
<script src="../js/script.js" type="text/javascript"
	language="javascript"></script>
<script src="../js/searchList.js" type="text/javascript"
	language="javascript" /></script>

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
  showAdvancedOptions();
});

		
function showAdvancedOptions(){
	$.get("getAdvancedOptions.action",{},function(data){
			$("#dynamicPane").empty().html(data);
			
	 });
}

function createUpdateAdvancedOptions(){
var userAdvancedData = $("#updateAdvancedForm").serialize(); 
  $.post("account/createAdvancedOptions.action", userAdvancedData, function(data) {    
        $("#dynamicPane").empty().html(data);

  });  
	
}



</script>
