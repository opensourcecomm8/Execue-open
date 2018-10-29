var selBookmarkId
var selfolderId;
var bookmarkId;
var folderID;
var bookMarkName;
var strName;
var strID;
var folderInfo;
var urlUserFolderJSON = "bookmark/getUserFolders!getUserFolders.action";
var dispFolders;
var selFoldersList;
var keyvalue = false;
var bmflag = false;
var newFolderName = ""
var bName = true;
var keydownformetrics=false;
$.keyCode = {
	BACKSPACE : 8,
	CAPS_LOCK : 20,
	COMMA : 188,
	CONTROL : 17,
	DELETE : 46,
	DOWN : 40,
	END : 35,
	ENTER : 13,
	ESCAPE : 27,
	HOME : 36,
	INSERT : 45,
	LEFT : 37,
	NUMPAD_ADD : 107,
	NUMPAD_DECIMAL : 110,
	NUMPAD_DIVIDE : 111,
	NUMPAD_ENTER : 108,
	NUMPAD_MULTIPLY : 106,
	NUMPAD_SUBTRACT : 109,
	PAGE_DOWN : 34,
	PAGE_UP : 33,
	PERIOD : 190,
	RIGHT : 39,
	SHIFT : 16,
	SPACE : 32,
	TAB : 9,
	UP : 38
};
$(document).ready(function() {

	$("#navigation").treeview({
		collapsed : true,
		unique : true,
		persist : "location"
	});
	$("#browser").treeview({
		animated : "normal",
		persist : "cookie"
	});

	$("input.openBookmarksButton").mouseover(function() {
		$("#tooltip").show();
		if (bmflag) {
			$("#tooltip").text("Hide Folders");
		} else {
			$("#tooltip").text("Show Folders");
		}
		$("#tooltip").css({
			display : "inline",
			position : "absolute",
			padding : "5px",
			color : "#000",
			background : "#FFFFD7",
			border : "1px solid #000",
			left : "290px"
		});
	});

	$("input.closeBookmarksButton").mouseover(function() {
		$("#tooltip").show();

		$("#tooltip").css({
			display : "inline",
			position : "absolute",
			padding : "5px",
			color : "#000",
			background : "#FFFFD7",
			border : "1px solid #000"
		});
	});
	$("input.openBookmarksButton").mouseout(function() {
		$("#tooltip").hide();
	});
	$("input.closeBookmarksButton").mouseout(function() {
		$("#tooltip").hide();
	});

	var pScroll = 500;// pOffset - divOffset;

	$('#scrollable').animate({
		scrollTop : '+=' + pScroll + 'px'
	}, 'easeOutBounce');

	$("#bookmarkName").bind('keypress', function(e) {
		if (e.keyCode == $.keyCode.ENTER || e.which == $.keyCode.ENTER) {
			$("#bookmarkID").trigger('click');
			return false;
		}
	});
	$("#folders").bind("change", function(e) {
		setColorForFolder("folder_" + $("#folders").val());
	});

	$("form#form1 input#bookmarkName").focus();
});

function createFolder() {
	if (bName) {
		var branches = $("<li class='closed'><span class='folder'><input type=text name='ts'  id='txtId' value='New Folder' onblur=saveFolderForUser(); onkeypress='keyPressFun(event);'/></span><ul>")
				.appendTo("#browser").bind('click', function(e) {
					$('#txtId').val("");
				});
		$("#browser").treeview({
			add : branches
		});
		$("#txtId").select().focus();
		// $("#createNewFolder").text($("#saveFolderDiv").text());
		var pScroll = 500;// pOffset - divOffset;

		$('#scrollable').animate({
			scrollTop : '+=' + pScroll + 'px'
		}, 'easeOutBounce');

		bName = false;
	} else {
		var fValue = $.trim($("#txtId").val());
		if (fValue != "") {
			// $("#createNewFolder").text($("#createFolderDiv").text());
			saveFolder();
			bName = true;
		} else {
			$("#actionMessage").text($("#fNameRequired").text());
			return false;
		}
	}
}
function keyPressFun(e) {
	if (e.keyCode == $.keyCode.ENTER || e.which == $.keyCode.ENTER) {
		saveFolder();
	}
}
function saveFolderForUser() {
	saveFolder();
}

$('#metrics').keydown(function(e) {
	keydownformetrics=true;
});
$('#metrics').blur(function(e) {
	keydownformetrics=false;
});
$('#bookmarkName').keydown(function(e) {
	keydownformetrics=true;
});
$('#bookmarkName').blur(function(e) {
	keydownformetrics=false;
});
$(document).keydown(function(e) {
	//keydownformetrics=false;
	if (e.keyCode == $.keyCode.DELETE || e.which == $.keyCode.DELETE) {
		removeBookmarkAndFolder();
	}
});

function saveFolder() {
	var urlFolderJSON = "bookmark/createFolder.action";
	var paramVal = $.trim($("#txtId").val());
	var qry=$("#bookmarkName").val();
	if (paramVal && paramVal != null && paramVal != '') {
		$.get(urlFolderJSON, {
			folderName : paramVal
		}, function(data) {
			$("#hiddenPaneContent").empty();
			$("#hiddenPaneContent").append(data);
			$("#DispList").show();
			$("#deleteBookmarkId").show();
			$("#bookmarksList").show();
			/*
			 * $("#hiddenPaneContent").animate({ height : "290px", width :
			 * "250px" });
			 */
			$("#hiddenPaneContent").height(290);
			$("#openBookmarksB").removeClass("openBookmarksButton");
			$("#openBookmarksB").addClass("closeBookmarksButton");
			$("#tooltip").text("Hide Folders");
			bmflag = true;
			$("ul#browser li ul").hide();
			$count = $("#count").val();
			$lastFid = "folder_" + $("#lastFid" + ($count - 1)).val();
			setColorForFolder($lastFid);
			$("#bookmarkName").val(qry);
		});
	} else {
		alert("Please enter folder name");
		return false;
	}

}
function removeBookmarkAndFolder() {
	var url;
	if (strName != null && strID != null && !keydownformetrics) {
		
		var removeRecords = confirm("Are you sure you want to remove Bookmark/Folder?");
		if (!removeRecords) {
			return;
		} else {
			if (strName == 'bookmark') {
				url = "bookmark/deleteBookmark.action?bookmarkId=" + strID;
				strName = null;
				strID = null;
			} else {
				url = "bookmark/deleteFolder.action?folderId=" + strID;
				strName = null;
				strID = null;

			}

			$.get(url, function(data) {
				$("#hiddenPaneContent").empty();
				$("#hiddenPaneContent").append(data);
				$("#DispList").show();
				$("#deleteBookmarkId").show()
				$("#bookmarksList").show();
				$("#openBookmarksB").removeClass("openBookmarksButton");
				$("#openBookmarksB").addClass("closeBookmarksButton");
				$("#tooltip").text("Hide Folders");
				bmflag = true;
				/*
				 * $("#hiddenPaneContent").animate({ height : "290px", width :
				 * "200px" });
				 */
				$("#hiddenPaneContent").height(290);
				$("ul#browser li ul").hide();

			});
		}
	}

}
function showFolder() {
	if (bmflag) {
		// alert("1 true")
		$("#openBookmarksB").removeClass("closeBookmarksButton");
		$("#openBookmarksB").addClass("openBookmarksButton");
		$("#tooltip").text("Show Folders");
		// $("#DispList").hide();
		$("#samplebutton").hide()
		$("#deleteBookmarkId").hide();
		$("#bookmarksList").hide();
		/*
		 * $("#hiddenPaneContent").animate({ height : "100px", width : "300px"
		 * });
		 */
		$("#hiddenPaneContent").height(100);
		bmflag = false;
	} else {
		// alert("2 false")
		$("#openBookmarksB").removeClass("openBookmarksButton");
		$("#openBookmarksB").addClass("closeBookmarksButton");
		$("#tooltip").text("Hide Folders");
		// $("#DispList").show();
		$("#samplebutton").show()
		$("#deleteBookmarkId").show()
		$("#bookmarksList").show();
		/*
		 * $("#hiddenPaneContent").animate({ height : "290px", width : "300px"
		 * });
		 */
		$("#hiddenPaneContent").height(290);
		$("#txtId").select().focus();
		bmflag = true;
	}
	setColorForFolder("folder_" + $("#folders").val());

}
$("#createNewFolder").click(function() {
	$("#actionMessage").empty();
	createFolder();
});
/* delete bookmark / folder */
$("#deleteBookmarkId").click(function() {
	removeBookmarkAndFolder();
});
/* Create new Bookmark */
$("#bookmarkID").click(function() {

	$("#actionMessage").empty();
	var value = $("#requestId").val();
	var bookmarkType = $("#bookmarkType").val();
//	alert('bookmarkType:'+bookmarkType);
	var bookmarkName = $("#bookmarkName").val();
	var folderId = $('#folders').val();
	var applicationId = $('#applicationId').val();
	var modelId = $('#modelId').val();
	var flag = false;
	if (folderId == undefined) {
		$("#actionMessage").text($("#fNameRequired").text());
		flag = true;
		return false;
	} else if (bookmarkName == undefined || bookmarkName == '') {
		$("#errorMessage").text($("#bNameRequired").text());
		flag = true;
		return false;
	}
	if (bookmarkType == "S"
			|| (applicationId == undefined && modelId == undefined)) {
		applicationId = 0;
		modelId = 0;
	}

	if (!flag) {
		var urlBookmarkJSON = "bookmark/createBookmark.action";
		$.get(urlBookmarkJSON, {
			"bookmark.name" : bookmarkName,
			"bookmark.value" : value,
			"bookmark.type" : bookmarkType,
			"bookmark.folder.id" : folderId,
			"bookmark.model.id" : modelId,
			"bookmark.application.id" : applicationId
		}, function(data) {
			$("#hiddenPaneContent").empty();
			$("#hiddenPaneContent").append(data);
			$("#DispList").show();
			$("#deleteBookmarkId").show()
			$("#bookmarksList").show();
			/*
			 * $("#hiddenPaneContent").animate({ height : "290px", width :
			 * "250px" });
			 */
			$("#hiddenPaneContent").height(290);
			$("ul#browser li ul").hide();
			if (data.indexOf('already exist') <= -1) {
				setTimeout('$("#hiddenPane").hide()', 1000)
			} else {
				// alert("Name already exist");
				$("#tooltip").show();
				$("#tooltip").text("Name already exists");
				$("#bookmarkName").focus().select();
				bmflag = true;
				$("#tooltip").css({
					display : "inline",
					position : "absolute",
					padding : "5px",
					color : "#000",
					background : "#FFFFD7",
					border : "1px solid #000",
					left : "-30px"
				});
				return false;
			}
		});
	}
});

function setColorForFolder(folderId) {
	var foId = folderId.substring(7);
	strID = foId;
	strName = 'folder';
	$("div.bookmarkKlass").css("background-color", "#ffffff");
	$("div.folderKlass").css("background-color", "#ffffff");
	$("#" + folderId).css("background-color", "#DBE7FB");
	$('#folders').val(foId);
	$("#folders :selected").text($("#" + folderId).text());

}
function setColorForBookmark(bmkId) {
	var bookmarkId = bmkId.substring(9);
	strID = bookmarkId;
	strName = 'bookmark';
	$("div.folderKlass").css("background-color", "#ffffff");
	$("div.bookmarkKlass").css("background-color", "#ffffff");
	$("#" + bmkId).css("background-color", "#DBE7FB");
}