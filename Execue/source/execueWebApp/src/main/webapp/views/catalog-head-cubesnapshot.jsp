<link href="../css/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css">
<link href="../css/styles.css" rel="stylesheet" type="text/css">
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />

<script src="../js/SpryTabbedPanels.js" type="text/javascript"></script>
<script src="../js/jquery.js" language="JavaScript"></script>
<script src="../js/ranges.js" language="JavaScript"></script>
<script src="../js/menun.js" language="JavaScript"></script>
<script src="../js/mm_menu.js" language="JavaScript"></script>
<script src="../js/jquery.ui.all.js" type="text/javascript"></script>
<script src="../js/jquery.layout.js" type="text/javascript"></script>
<script src="../js/jquery.execue.js" type="text/javascript"></script>
<script src="../js/script.js" type="text/javascript"
	language="javascript"></script>

<script type="text/javascript">
var myLayout;
var assetId;

$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
  });
 
  showTargetAsset();
});
function showTargetAsset() {
  $.get("swi/showTargetAsset.action",{},function(data) {
    $("#targetAssetDynaminPane").fadeIn("fast");
    $("#targetAssetDynaminPane").empty();
    $("#targetAssetDynaminPane").append(data);
    $("#targetAssetDynaminPane").show();
  });
  }
function validateData(){
 var name=$("#targetAssetNameId").val();
 var dispName=$("#targetAssetDispNameId").val();
 var des=$("#targetAssetDesId").val();
  if(name=="" || dispName=="" || des ==""){
     $("#errorMessage").empty().append("Please enter required fields");
      return false;
  }
    return true;
}

function back(){
	  $("#backLink").hide();
	  $("#loadingBackLink").show();
	  window.history.back();
	 // $("#loadingBackLink").hide();
	  //$("#backLink").show();
  	
  }
</script>