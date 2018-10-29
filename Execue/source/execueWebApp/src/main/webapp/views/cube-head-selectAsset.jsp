
<link href="../css/styles.css" rel="stylesheet" type="text/css">
<LINK href="../css/roundedSearch.css" rel="stylesheet">
<link href="../css/treeDrag.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="../css/thickbox.css" type="text/css"
	media="screen" />
<script type="text/javascript" src="../js/jquery.js"></script>

<script language="JavaScript" src="../js/menun.js"></script>
<script language="JavaScript" src="../js/mm_menu.js"></script>
<script type="text/javascript" src="../js/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/thickbox.js"></script>
<script type="text/javascript">
 $(document).ready(function () {	   
     var srcName=document.getElementById("srcName").value;     
	  tb_show("Show Datasets ","swi/showAllAssets.action?sourceName="+srcName+"&keepThis=true&TB_iframe=true&height=50&width=50 class=thickbox title=CD ROM Order Support");
	 //tb_show("Show Assets ","../views/showAssets.jsp?keepThis=true&amp;TB_iframe=true&amp;height=260&amp;width=360 class=thickbox title=CD ROM Order Support");
    
 });
</script>
<s:hidden name="sourceName" id="srcName" />


