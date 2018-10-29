<link href="../css/styles.css" rel="stylesheet" type="text/css">
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="../css/thickbox.css" type="text/css"
	media="screen" />
<link href="../css/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/roundedSearch.css" rel=stylesheet>


<script src="../js/SpryTabbedPanels.js" type="text/javascript"></script>
<script language="JavaScript" src="../js/jquery.js"></script>
<script language="JavaScript" src="../js/jquery.execue.js"></script>
<script language="JavaScript" src="../js/menun.js"></script>
<script type="text/javascript" src="../js/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/jquery.layout.js"></script>
<script language="JavaScript" src="../js/mm_menu.js"></script>
<script type="text/javascript" src="../js/thickbox.js"></script>
<script src="../js/script.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/searchList.js" type="text/javascript"
	language="javascript" /></script>

<script type="text/javascript">
var keyWord="";
var txId="";
var myLayout;

$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
	//,	west__fxSettings_open: { easing: "easeOutBounce", duration: 750 }
  }); 
 getExistingSFLTerms();
});
 function getExistingSFLTerms(){
 $("#loadingSFLTLink").show();
  $.post("swi/showExistingSFLTerms.action", function(data) {  
    $("#dynamicPaneSFLTerms").empty();
    $("#dynamicPaneSFLTerms").append(data);
    $("#dynamicPaneSFLTerms").show();
    $("#loadingSFLTLink").hide();
  });
}
function showSFLTermTokens(term)
{
	$("#showSFLTermToken"+term+"Link").hide();
	$("#loadingShowSFLTermTokens"+term+"Link").show();	
	$.post("swi/showSFLTermTokens.action",{sflTerm:term}, function(data) {    
    $("#dynamicPane").empty();
    $("#dynamicPane").append(data);
    $("#dynamicPane").show();
    $("#loadingShowSFLTermTokens"+term+"Link").hide();
    $("#showSFLTermToken"+term+"Link").show();
  });	
}
function updateSFLTerms(){	
	sflTermsTokens=$("#sflTermForm").serialize();
	$("#enableUpdateSFLTerm").hide();
	$("#updateSFLTermProcess").show();
	$.post("swi/updateSFLTermTokens.action",sflTermsTokens,function(data){
	$("#dynamicPane").empty();
    $("#dynamicPane").append(data);
    $("#dynamicPane").show();
    $("#imageField").hide();
    $("#enableUpdateSFLTerm").show();
    $("#updateSFLTermProcess").hide();
    
	});	
}	
</script>