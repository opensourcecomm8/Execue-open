<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/styles.css" rel="stylesheet" type="text/css">
<LINK href="../css/roundedSearch.css" rel="stylesheet">
<LINK href="../css/cssPopupMenu.css" rel=stylesheet>
<link href="../css/treeDrag.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="../css/thickbox.css" type="text/css"
	media="screen" />
<script type="text/javascript" src="../js/jquery.js"></script>
<script src="../js/menun.js" language="JavaScript"></script>
<script src="../js/mm_menu.js" language="JavaScript"></script>
<script type="text/javascript" src="../js/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/thickbox.js"></script>
<script src="../js/searchList.js" type="text/javascript"
	language="javascript" /></script>
<script type="text/javascript"
	src="../js/jquery.execue.searchRecordsComponent.js"></script>
<script type="text/javascript">

  var jsAssetId;
  $(document).ready(function () {
    jsAssetId = '<s:property value="applicationContext.assetId"/>';  
    if(jsAssetId){
      showAssetsTables(jsAssetId);
    }  	
  });
  
  function showAssetsTables(assetId) {
   $.post("showDefaultMetricAssetTables.action?asset.id="+assetId, function(data) {    
    $("#dynamicPaneAssetTables").fadeIn("fast");
    $("#dynamicPaneAssetTables").empty();
    $("#dynamicPaneAssetTables").append(data);
    $("#dynamicPaneAssetTables").show(); 
  });
 }
  
 function getDefaultMetric(tableId,tableName,systemMetrics,tableDisplayName) {
     $("#loadingATLink").show();
	 $.post("getDefaultMetricByAssetTable.action",{"asset.id":jsAssetId,"currentTable.id":tableId,"currentTable.name":tableName,"currentTable.eligibleSystemDefaultMetric":systemMetrics,"currentTable.displayName":tableDisplayName}, function(data) {    
	  $("#dynamicPaneDefaultMetrics").fadeIn("fast");
	  $("#dynamicPaneDefaultMetrics").empty();
	  $("#dynamicPaneDefaultMetrics").append(data);
	  $("#dynamicPaneDefaultMetrics").show();
	  $("#loadingATLink").hide();
  });
}
</script>