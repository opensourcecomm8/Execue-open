
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
 $(document).ready(function () {	   
     var srcName=document.getElementById("srcName").value;     
	  tb_show("Show Datasets ","swi/showAllAssets.action?sourceName="+srcName+"&keepThis=true&TB_iframe=true&height=50&width=50 class=thickbox title=CD ROM Order Support");
	 //tb_show("Show Assets ","../views/showAssets.jsp?keepThis=true&amp;TB_iframe=true&amp;height=260&amp;width=360 class=thickbox title=CD ROM Order Support");
    
 });
</script>
<s:hidden name="sourceName" id="srcName" />


