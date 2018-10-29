
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<LINK href="../css/common/roundedSearch.css" rel="stylesheet">
<link href="../css/admin/treeDrag.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="../css/admin/thickbox.css" type="text/css"
	media="screen" />
<script type="text/javascript" src="../js/common/jquery.js"></script>

<script language="JavaScript" src="../js/admin/menun.js"></script>
<script language="JavaScript" src="../js/common/mm_menu.js"></script>
<script type="text/javascript" src="../js/common/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/admin/thickbox.js"></script>
<script type="text/javascript">
  var jsAssetId;
  $(document).ready(function () {
    jsAssetId = $("#assetId").val();
    var sourceName="join";
	if(jsAssetId==0){
	tb_show("Show Datasets ","swi/showAllAssets.action?sourceName="+sourceName+"&keepThis=true&TB_iframe=true&height=50&width=50 class=thickbox title=Dataset Selection");
	//tb_show("Show Datasets ","../views/showAssets.jsp?keepThis=true&amp;TB_iframe=true&amp;height=260&amp;width=360 class=thickbox title=Dataset Selection");
	}
  });
</script>
<SCRIPT language=JavaScript type=text/javascript>
  var client_id = 1;
</SCRIPT>
<SCRIPT language=JavaScript src="" type=text/javascript>
</SCRIPT>
<NOSCRIPT>
<P><IMG height=1 alt="" src="" width=1></P>
</NOSCRIPT>
