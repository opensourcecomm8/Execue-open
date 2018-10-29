var openImg = new Image();
openImg.src = "../images/open.gif";
var closedImg = new Image();
closedImg.src = "../images/closed.gif";

function showBranch(branch) {
	if (branch.length > 0) {
		var objBranch = document.getElementById(branch).style;
		if (objBranch.display == "block")
			objBranch.display = "none";
		// $("#"+branch).slideUp();
		else
			objBranch.display = "block";
		// $("#"+branch).slideDown();
		swapFolder('I_' + branch);
	}
}

function showGroup(branch) {
	if (branch.length > 0) {
		var objBranch = document.getElementById(branch).style;

		if (objBranch.display != "block"){
			objBranch.display = "block";		
			swapFolder('I_' + branch);
		}
	}
}

function swapFolder(img) {
	objImg = document.getElementById(img);
	if (objImg.src.indexOf('closed.gif') > -1)
		objImg.src = openImg.src;
	else
		objImg.src = closedImg.src;
}
/*
function showMenu(path) {
	alert('you should not be here!!');
	$tokens = path.split("~,");
	$.each($tokens, function(index, data) {
		var id = data.substring(1, data.length);
		if (id.indexOf(']') > -1) {
			id = id.substring(0, id.length - 1);
			$("#" + id).attr('style', 'font-weight: bolder;');
			if ($("#appNameDiv").text() != "") {
				$("#appDivArrow").attr('style', 'display: inline;');
				$("#appDiv").attr('style', 'display: inline;');
			}
			$("#currentPageDiv").html($("#" + id).text());
			$("#currentPageDivArrow").html(" &gt;&gt; ");
		} else {
			showBranch(id);
		}
	});
}*/