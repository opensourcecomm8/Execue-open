<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/roundedSearch.css" rel=stylesheet>
<s:hidden name="fileTableInfoId" id="fileTableInfoId" />
<script src="../js/jquery.js" language="JavaScript" /></script>
<script src="../js/menu.js" language="JavaScript" /></script>
<script src="../js/mm_menu.js" language="JavaScript" /></script>
<script src="../js/jquery.ui.all.js" type="text/javascript" /></script>
<script src="../js/jquery.layout.js" type="text/javascript" /></script>
<script src="../js/SpryTabbedPanels.js" type="text/javascript" /></script>
<script src="../js/script.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/searchList.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/jquery.execue.js" language="JavaScript" /></script>

<script type="text/javascript">


var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method
var jsDomainId;
$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
  });  
  jsDomainId = $("#domainId").val();
 
});

$(document).ready(function(){
  var tableId=$("#fileTableInfoId").val();  
  $.post("publisher/getGrainDefinitions.action?fileTableInfoId="+tableId,function(data) {
		   $("#dynamicPane").empty();
			$("#dynamicPane").fadeIn("fast");    
			$("#dynamicPane").append(data);
			$("#dynamicPane").show();	
	});
});
function updateGrainDef(){  
    alert($('#grainDefinitionForm').serialize());      
    var fileInfoTabId= $("#fileTableInfoId").val();  
      $.post("publisher/updateGrainDefinitions.action?fileTableInfoId="+fileInfoTabId,$('#grainDefinitionForm').serialize(),function(data) {
		   $("#dynamicPane").empty();
			$("#dynamicPane").fadeIn("fast");    
			$("#dynamicPane").append(data);
			$("#dynamicPane").show();	
	});
     
    /* $("#dynamicPane").createInput('<s:url action="updateGrainDefinitions"/>',"","",$('#grainDefinitionForm').serialize()); //passing URL, user clicked link, loader div	
     return false;*/
}

</script>