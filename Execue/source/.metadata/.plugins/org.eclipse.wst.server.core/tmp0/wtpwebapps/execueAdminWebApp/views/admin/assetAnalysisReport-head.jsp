<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="../css/admin/thickbox.css" type="text/css"
	media="screen" />

<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript"></script>
<script language="JavaScript" src="../js/common/jquery.js"></script>
<script language="JavaScript" src="../js/admin/jquery.execue.js"></script>
<script language="JavaScript" src="../js/admin/menun.js"></script>
<script type="text/javascript" src="../js/common/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/common/jquery.layout.js"></script>
<script language="JavaScript" src="../js/mm_menu.js"></script>
<script type="text/javascript" src="../js/admin/thickbox.js"></script>
</head>
<body>

</body>
</html>
<script>
$(document).ready(function() {
var assetId='<s:property value="selectedAssetId"/>';
var assetAnalysisOperationType= '<s:property value="assetAnalysisOperationType"/>';
if(assetAnalysisOperationType){
	 getAssetAnalysisReport(assetAnalysisOperationType,assetId);
  }
});
function getAssetAnalysisReport(assetAnalysisOperationType,assetId){
	$.post("getAssetAnalysisReport.action?assetAnalysisOperationType="+assetAnalysisOperationType+"&selectedAssetId="+assetId, function(data) {	   
    $("#dynamicPane").empty();    
    $("#dynamicPane").append(data);
    $("#dynamicPane").show();  
    });  
}
</script>