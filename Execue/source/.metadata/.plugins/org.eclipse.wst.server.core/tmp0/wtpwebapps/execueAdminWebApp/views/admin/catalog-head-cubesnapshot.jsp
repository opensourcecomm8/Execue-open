<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css">
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />

<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript"></script>
<script src="../js/common/jquery.js" language="JavaScript"></script>
<script src="../js/admin/ranges.js" language="JavaScript"></script>
<script src="../js/admin/menun.js" language="JavaScript"></script>
<script src="../js/mm_menu.js" language="JavaScript"></script>
<script src="../js/common/jquery.ui.all.js" type="text/javascript"></script>
<script src="../js/common/jquery.layout.js" type="text/javascript"></script>
<script src="../js/admin/jquery.execue.js" type="text/javascript"></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript"></script>

<%@ taglib prefix="s" uri="/struts-tags"%>
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
var existingAssetId= '<s:property value="existingAssetId"/>';
  $.get("swi/showTargetAsset.action?existingAssetId="+existingAssetId,{},function(data) {
    $("#targetAssetDynaminPane").fadeIn("fast");
    $("#targetAssetDynaminPane").empty();
    $("#targetAssetDynaminPane").append(data);
    $("#targetAssetDynaminPane").show();
  });
  }


function back(){
	  $("#backLink").hide();
	  $("#loadingBackLink").show();
	  window.history.back();
	 // $("#loadingBackLink").hide();
	  //$("#backLink").show();
  	
  }
</script>