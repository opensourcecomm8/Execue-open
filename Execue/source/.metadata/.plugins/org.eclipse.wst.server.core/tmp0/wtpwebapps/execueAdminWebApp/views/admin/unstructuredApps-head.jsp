<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="../css/admin/thickbox.css" type="text/css"
	media="screen" />
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/admin/styleToolTip.css" rel="stylesheet"
	type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel="stylesheet"
	type="text/css" />
<LINK href="../css/admin/cssPopupMenu.css" rel="stylesheet"
	type="text/css" />

<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript"></script>
<script language="JavaScript" src="../js/common/jquery.js"></script>
<script language="JavaScript" src="../js/admin/jquery.execue.js"></script>
<script language="JavaScript" src="../js/admin/menun.js"></script>
<script type="text/javascript" src="../js/common/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/admin/jquery.layout2.js"></script>
<script language="JavaScript" src="../js/common/mm_menu.js"></script>
<script type="text/javascript" src="../js/admin/thickbox.js"></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript" /></script>
<script type="text/javascript"
	src="../js/admin/jquery.execue.searchRecordsComponent.js"></script>

<script type="text/javascript">
var keyWord="";
var txId="";
var myLayout;
var searchTypeClicked="";
$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
  });
     $("#divBT").searchRecordsComponent({actionName:"swi/getUnstructuredApps.action",targetDivName:"dynamicPaneUnstructuedApps"});
     showUnstructuredApps();

 
	
});

function showUnstructuredApps() { 
	
  $.post("getUnstructuredApps.action",{}, function(data) {    
    $("#dynamicPaneUnstructuedApps").empty();
    $("#dynamicPaneUnstructuedApps").append(data);
    $("#dynamicPaneUnstructuedApps").show();
    
  });
}

function showUnstructuredAppDatasource(applicationId){
	$("a#"+applicationId).css("background","url(../images/admin/loaderTrans.gif) no-repeat center center");
 $.post("showUnstructuredAppDatasources.action?applicationId="+applicationId,{}, function(data) {    
    $("#dynamicPane").empty();
    $("#dynamicPane").append(data);
    $("#dynamicPane").show();
	$("a#"+applicationId).css("background","none");
   });
}

function autoHighlight(html, highlight) {
				return html.replace(new RegExp(highlight, 'gi'),
						function(match) {
							return '<b>' + match + '</b>';
						});
			}
</script>
