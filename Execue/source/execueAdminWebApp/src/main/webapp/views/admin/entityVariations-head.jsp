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
  showBusinessTerms(''); 
  var businessTerm= $("#businessEntityTermTypesId").val();
   $("#divBT").searchRecordsComponent({actionName:"swi/showEntityVariationBTs.action?businessEntityTermType="+businessTerm,targetDivName:"dynamicPaneBTerms"});
    $("#businessEntityTermTypesId").change(function(){ 
     var businessTermType= $("#businessEntityTermTypesId").val();
	   $("#divBT").empty(); 
	   $("#dynamicPane").empty();
	   $("#divBT").searchRecordsComponent({actionName:"swi/showEntityVariationBTs.action?businessEntityTermType="+businessTermType,targetDivName:"dynamicPaneBTerms"});
	   
	   showBusinessTerms($(this).val());
   });
});



function showBusinessTerms(businessTerm){
  businessTerm= $("#businessEntityTermTypesId").val();
  $("#dynamicPaneBTerms").css("background","url(../images/admin/loaderTrans.gif) no-repeat center center");
  $.get("swi/showEntityVariationBTs.action?businessEntityTermType="+businessTerm, function(data) {
      $("#dynamicPaneBTerms").empty();
      $("#dynamicPaneBTerms").append(data);
	  $("#dynamicPaneBTerms").css("background","none");
    });//End of populate BusinessTerms
}

 

function showEntityVariations(entityName,bedId) {
	 $("a#"+bedId).css("background","url(../images/admin/loaderTrans.gif) no-repeat center center");
  $.post("getBusinessEntityVariation.action",{entityName:entityName,entityBedId:bedId}, function(data) {    
    $("#dynamicPane").empty();
    $("#dynamicPane").append(data);
    $("#dynamicPane").show();
    $("#imageField").hide();
	$("a#"+bedId).css("background","none");
  });
}

function autoHighlight(html, highlight) {
				return html.replace(new RegExp(highlight, 'gi'),
						function(match) {
							return '<b>' + match + '</b>';
						});
			}
</script>
