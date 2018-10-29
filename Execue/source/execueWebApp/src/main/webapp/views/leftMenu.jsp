<%@taglib uri="/WEB-INF/tlds/adminConsole.tld" prefix="admin"%>

<style>
.bcLinks a {
	padding-left: 5px;
	padding-right: 5px;
	font-size: 11px;
	color: #039;
}

.bcArrows {
	padding-left: 3px;
	padding-right: 3px;
	font-size: 11px;
	font-family: "Lucida Sans Unicode", "Lucida Grande", sans-serif;
}

.bcNoLinks {
	padding-left: 4px;
	font-size: 11px;
}
</style>
<div id="pageLoader" style="padding:4px;padding-top:5px;display:none;z-index:1000000;position:absolute;border:none;width:50px;height:50px;"><div style="width:32px;;margin:auto;"><img id="waiting_img2"   src="../images/Loader-main-page-3.gif" width="32" height="32" /></div><div style="width:100%;text-align:center;">Loading</div></div>
<form id="leftMenuForm" name="leftMenuForm">
<DIV id="container-1"
	style="width: auto;  margin-top: 1px; margin-left: 0px;margin-right: 3px; padding: 2px; padding-top: 0px; padding-bottom: 6px; padding-left: 0px; background-color: #FFF; min-height: 420px; height: auto;">

<!-- %=request.getSession().getAttribute("adminHtmlMenu")%-->
<admin:showMenu/>
</DIV>
<input type="hidden" id="clickedMenuItem" name="clickedMenuItem"/>
</form>

<script>
var left=(screen.width)-(screen.width/2)-20;
var top=(screen.height)-(screen.height/2)-180;
$(document).ready(function() {

	$("#container-1 a").click(function() {
	
	if($(this).attr("target")!="_blank"){
		showLoaderImageOnLoad();
		var openBranchs = "";
		var clickedPath = "";
		
		clickedPath = $(this).attr("id");
		
		$.each($("div .branch:visible"),function(index, data){
			var openPath = $(this).attr("id");
			openBranchs +=  ", " + openPath;
		});
		var clickedMenuPath = clickedPath +"#" +openBranchs;
		$("#clickedMenuItem").val(clickedMenuPath);
		document.leftMenuForm.action = $(this).attr("href");
		document.leftMenuForm.method = "post";
		//$(this).attr("href", "#");
		document.leftMenuForm.submit();
		return false;	
		}	
	});
});
function showLoaderImageOnLoad(){
tempImg = $("#waiting_img2").attr("src");
		$("#pageLoader").show().css("left",left+"px");
		$("#pageLoader").css("top",top+"px");
		setTimeout('document.images["waiting_img2"].src = tempImg', 1); 	
}
</script>