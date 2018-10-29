<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="../css/admin/thickbox.css" type="text/css"
	media="screen" />
<LINK href="../css/common/roundedSearch.css" rel="stylesheet" type="text/css" />
<LINK href="../css/admin/cssPopupMenu.css" rel="stylesheet" type="text/css" />

<script language="JavaScript" src="../js/common/jquery.js"></script>
<script language="JavaScript" src="../js/admin/jquery.execue.js"></script>
<script type="text/javascript" src="../js/admin/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/admin/jquery.layout.js"></script>
<script type="text/javascript" src="../js/admin/execue.commons.js"></script>
<script type="text/javascript" src="../js/admin/thickbox.js"></script>
<script type="text/javascript" src="../js/admin/jquery.execue.htmlResponse.js"></script>
<script type="text/javascript"
	src="../js/admin/jquery.execue.searchRecordsComponent.js"></script>


<script type="text/javascript">
// this is a static head page which will be loaded only once whn the vertical screen is shown.
// rest of the navigation updation deletion etc will be ajax based.
var movedIds = new Array;
var isCopied = false;

// user navigational methods begin
$(document).ready(function() {
	searcTextFocusInitialise();
	showVerticals();
	$("#filteredVerticalSelect").live("change", function() {
		// handle the case of change of filtered list when not saved
		var confirmed = true;
		if(movedIds.length > 0 || isCopied) {
			var msg = "You have unsaved changes, proceed any way?";
			confirmed = confirm(msg);
		}
		if(confirmed){
			var selectedVerticalId = $("#selectedVerticalId").val();
			populateVerticalApps (selectedVerticalId);
			movedIds = new Array;
			isCopied = false;
		}
	});
	
	// invoke a bind function for right select box on pagination to prevent data loss
	$("#paginationExistingAppsDiv a").live("click", function() {
		return validateUserNav();
	});
	
	// invoke a bind function for right select box on searching via enter key to prevent data loss
	$("#divExistingSearch input").live("keypress", function(event) {
		if(movedIds.length > 0 || isCopied) {
			$("#userInterruptInputExisting").val("true");
   		}
   	});
});

function addVertical(){
	var validationFlag = doValidation($("#vertical_name"));
	if(validationFlag){
		$.post("addVertical.action?" + $("#verticalForm").serialize(), function(data) {
			$("#dynamicPane").empty().html(data);
			showVerticals();
			invokeClear();
		});
	}
}

function showVerticals(){
  showDetails("getVerticals.action","divVerticals","get");
}

function createNewVertical(){
  showDetails("createVertical.action","dynamicPane","get");
  setTimeout("invokeClear()", 500);
}

function deleteVertical (verticalId) {
	var msg = "You might have associated applications, deleting this vertical will associate those apps to default, proceed any way?";
	if(confirm(msg)){
		$.post("deleteVertical.action?selectedVerticalId="+verticalId, function(data) {
			$("#dynamicPane").empty().html(data);
			showVerticals();
			invokeClear();
		});
		return true;
  	}
  	return false;
}
// function to load the vertical details screen
function getVerticalDetails (verticalId) {
	movedIds = new Array;
	showDetails("getVertical.action?selectedVerticalId="+verticalId,"dynamicPane","get");
}
// function to load the app asociation screen
function getVerticalAssociation (verticalId) {
	$.get("showVerticalAppAssociation.action?selectedVerticalId="+verticalId, function(data) {
		$("#dynamicPane").empty().html(data);
		populateVerticalApps (verticalId);
	});
}
// function to load left and right select boxes with search and pagination code every time a search pagiantion or select change on vertical combo is done.
function populateVerticalApps (verticalId) {
	var filteredName = $("#filteredVerticalSelect").find("option:selected").text();
	var selectedVerticalName = $("#divVerticals table[id='searchList'] a[id='"+verticalId+"']").html();
	if(filteredName) {
		$("#divFilteredApps").css({width:"250px",height:"200px",border:"1px solid #ccc"}).css("background","url(../images/loaderAT.gif) no-repeat center center");
		$("#divExistingApps").css({width:"250px",height:"200px",border:"1px solid #ccc"}).css("background","url(../images/loaderAT.gif) no-repeat center center");
		
		$.get("getVerticalApps.action?selectedVerticalName="+filteredName+"&verticalSelectName=Filtered", function(data){
			$("#divFilteredApps").css({background:"none",border:"none"}).empty().html(data);
		});
		$.get("getVerticalApps.action?selectedVerticalName="+selectedVerticalName+"&verticalSelectName=Existing", function(data){
			$("#divExistingApps").css({background:"none",border:"none"}).empty().html(data);
		});
	}
}


function deleteApp() {
	var $existingApps = "";
	var confirmed = true;
	var selectedVerticalId = $("#selectedVerticalId").val();
	if(movedIds.length > 0) {
		var msg = "You have unsaved changes, proceed any way?";
		confirmed = confirm(msg);
	}
	if(confirmed){
		$.each($("#appsExisting option"), function (k, v) {
			$existingApps += "&applicationIds["+k+"]="+$(this).val();		
		});
		//alert($existingApps);
		var params = '?selectedVerticalId=' + selectedVerticalId + $existingApps;
		var filteredId = $("#filteredVerticalSelect").find("option:selected").attr("value");
		params = params + "&filteredVerticalId=" + filteredId;
		// submit the data and reload the association screen
		$.post("removeVerticalAppAssociation.action"+params, function(data){
			$("#dynamicPane").empty().html(data);
			populateVerticalApps(selectedVerticalId);
			invokeClear();
		});
		movedIds = new Array;
	}
}

function updateAssociation() {
	var $existingApps = "";
	var selectedVerticalId = $("#selectedVerticalId").val();
	$.each($("#appsExisting").find("option"), function (k, item) {
		$existingApps += "&applicationIds["+k+"]="+$(this).attr('value');		
	});	
	var params = '?selectedVerticalId=' + selectedVerticalId + $existingApps;
	var filteredId = $("#filteredVerticalSelect").find("option:selected").attr("value");
	if(movedIds.length > 0) {
		var movedApps = "";
		$.each(movedIds, function (k, item) {
			movedApps += "&movedApplications["+k+"]="+item;		
		});
		params = params + movedApps;
	}
	params = params + "&filteredVerticalId=" + filteredId;
	// submit the data and reload the association screen
	$.post("addVerticalAppAssociation.action"+params, function(data){
		$("#dynamicPane").empty().html(data);
		populateVerticalApps(selectedVerticalId);
		invokeClear();
	});
	
	movedIds = new Array;
}
// user navigational methods end

// user interaction methods begin
function copyApp() {
	var selectedFilteredApps = $("#appsFiltered").find("option:selected");
	$.each(selectedFilteredApps, function() {
		var id = $(this).attr('value');
		var existingFilteredAppId = $("#appsExisting option[value='"+id+"']");
		if(!existingFilteredAppId.attr("value")){
			var name = $(this).text();
			var $option = '<option value="'+id+'">'+name+'</option>';
			$("#appsExisting").append($option);
			isCopied = true;
		}
	});
}

function moveApp() {
	var selectedFilteredApps = $("#appsFiltered").find("option:selected");
	$.each(selectedFilteredApps, function(k, item) {
		var id = $(this).attr('value');
		var existingFilteredAppId = $("#appsExisting option[value='"+id+"']");
		if(!existingFilteredAppId.attr("value")) {
			var name = $(this).text();
			var $option = '<option value="'+id+'">'+name+'</option>';
			$("#appsExisting").append($option);
			movedIds.push(id);
			$("#appsFiltered option[value='"+id+"']").remove();
		}
	});	
}

function doValidation(obj){
	var top=obj.position().top;
	var left=obj.position().left;
	var stringEmpty=checkEmptyString(obj);
				$("#verticalNameMessage").css("top",top-8+"px");
				$("#verticalNameMessage").css("left",left+obj.width()+10+"px");
	if(stringEmpty){$("#verticalNameMessage").text("Vertical Name "+stringEmptyMessage).show(); 
				setFocus(obj);
				return false; }
	var splCharExist=checkSpecialChar(obj);
			if(splCharExist){				
				$("#verticalNameMessage").text(specialCharMessage).show();
				setFocus(obj);
				return false;

			}else{
				$("#verticalNameMessage").hide();
			} 
			return true;
}

function invokeClear(){
	setTimeout("clearMsgs()",3000);
}

function clearMsgs() {
	$("#errorMessage").empty();
	$("#actionMessage").empty();
}

function validateUserNav() {
	var confirmed = true;
	if(movedIds.length > 0 || isCopied) {
		var msg = "You have unsaved changes, proceed any way?";
		confirmed = confirm(msg);
	}
	if(confirmed == true){
		movedIds = new Array;
		isCopied = false;
	}
	
	return confirmed;
}
// user interaction methods ends
</script>

