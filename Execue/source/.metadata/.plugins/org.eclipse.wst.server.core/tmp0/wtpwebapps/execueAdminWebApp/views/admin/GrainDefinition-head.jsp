<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/common/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css" />
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet>
<s:hidden name="fileTableInfoId" id="fileTableInfoId" />
<script src="../js/common/jquery.js" language="JavaScript" /></script>
<script src="../js/menu.js" language="JavaScript" /></script>
<script src="../js/common/mm_menu.js" language="JavaScript" /></script>
<script src="../js/common/jquery.ui.all.js" type="text/javascript" /></script>
<script src="../js/common/jquery.layout.js" type="text/javascript" /></script>
<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript" /></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/admin/searchList.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/admin/jquery.execue.js" language="JavaScript" /></script>

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