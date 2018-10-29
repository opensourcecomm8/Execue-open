suggestConceptMappingURL = "mapsSuggestedNew.html";
btListURL = "mapsBusinessTerms.html";
instanceListURL = "mapsInstances.html";

suggestConceptURL = "mapsBusinessTerms.html";
existingConceptMappingURL = "mapsSuggestedExisting.html";
storeConceptMappingURL = "mapsSuggestedExisting.html";
editConceptURL = "editConcept.htm";
newConceptURL = "addNewConcep.html";
saveConceptURL = "saveConcept.html";
editInstanceURL = "editInstance.html";
newInstanceURL = "addNewInstance.htm";
saveInstanceURL = "saveInstance.html";
tableColumnsURL = "mapsTableColumns.html";
columnMembersURL = "mapsColumnMembers.html";

conceptShowPageURL = "";
instanceShowPageURL = "";

suggestInstanceURL = "";
existingInstanceMappingURL = "mapsInstanceSuggestedExisting.html";
suggestInstanceMappingURL = "";
storeInstanceMappingURL = "mapsInstanceSuggestedExisting.html";

$conceptMappingMain = undefined;
$conceptMappingPage = undefined;
$instanceMappingMain = undefined;
$instanceMappingPage = undefined;

$businessTermMain = undefined;
$instancesMain = undefined;
$tableColumnsMain = undefined;
$columnMembersMain = undefined;

$conceptInput = undefined;
$instanceInput = undefined;
currentMapping = undefined;
currentInstanceMapping = undefined;
currentConceptMappingForInstances = undefined;
dragColumnElement = undefined;
dragConceptElement = undefined;
dragConceptData = undefined;
dragColumnData = undefined;
$conceptClicked = false;
$instanceClicked = false;
$previousClick = [];
$clickCount = 0;
$conceptsState = [];
$previousInstanceClick = [];
$clickInstanceCount = 0;

$dispNames = [];
$dispInstanceNames = [];
$tempBTData = [];
$tempInstanceData = [];
$current = "concept";
$newConcept = {};
$newInstance = {};
$requestParam = {};
$instanceState = true;
$currentLink = false;
$mode = undefined;
$addConceptFlag=false;
// var to store all configuration data
options = {
	btClassName : "concept",
	btConceptClassName : "conceptHolder",
	suggestsClassName : "sugClassName",
	tableClassName : "mytable",
	columnClassName : "column",
	messageClassName : "messageClass"
};
function getJSONData(url, data, functionName,errorFunctionName) {
	$.ajax({url:url, data:data, success:functionName, dataType:"json",type:"post",error:errorFunctionName});
	//$.post(url,data,functionName,"")
}
function errorFunction(){
	alert("error");
}
function initializeMappings() {
	$conceptMappingPage = $('#conceptMappingPage');
	$conceptMappingMain = $('#conceptsSuggestedDiv');
	$conceptInput = $('<input type=text>');
	$instanceInput = $('<input type=text>');

	$instanceMappingMain = $('#InstancesSuggestedDiv');
	$instanceMappingPage = $('#instanceMappingPage');
	$instanceInput = $('<input type=text>');

	$instancesDiv = $('#instancesDiv');
	$instancesMain = $('<span></span>');

	$tableColumnsDiv = $('#tableColumnsDiv');
	$tableColumnsMain = $('<span></span>');

	$columnMembersDiv = $('#columnMembersDiv');
	$columnMembersMain = $('<span></span>');

	$("#confirmMappingsId").click(confirmMapping);
	$("#confirmInstancesMappingsId").click(confirmMapping);

	$businessTermDiv = $('#businessTermDiv');
	$businessTermMain = $('<span></span>');
	$businessTermDiv.append($businessTermMain);

	$tableColumnsDiv.append($tableColumnsMain);
	$columnMembersDiv.append($columnMembersMain);
	$instancesDiv.append($instancesMain);
	//setTimeout(hideConceptLoaders, 10000);
	getJSONData(btListURL,{}, processBTListJsonData,processBTListJsonDataError);
	getJSONData(existingConceptMappingURL, {
		assetId : $('#assetId').val()
	}, processConceptMappingJsonData,processConceptMappingJsonDataError);
	getJSONData(tableColumnsURL, {
		assetId : $('#assetId').val()
	}, processTableColumnsJsonData,processTableColumnsJsonDataError);

	$("#newConcept").click(newConcept);
	$("#newInstance").click(newInstance);
	$("#newInstance").hide();
	$("#instancePage").hide();
	showNavControl("dinstanceNavFirst", "dinstanceNavPrevious",
			"dinstanceNavNext", "dinstanceNavLast", "dinstanceNavAll");
	hideNavControl("instanceNavFirst", "instanceNavPrevious",
			"instanceNavNext", "instanceNavLast", "instanceNavAll");
}
function hideConceptLoaders() {
	$("#loadingBTLink").hide();
	$("#loadingCMLink").hide();
	$("#loadingATLink").hide();
}
function getTableConceptParams() {
	requestedParams = "";
	indexToAdd = 0;
	values = "";
	$.each($("input[name='assetColumns']:checked"), function(i, el) {
		requestedParams = requestedParams + "&selColAedIds=" + el.value;
	});
	$.each($("input[name='conceptNames']:checked"), function(i, item) {
		data = $(item).parent('li').data('data');
		requestedParams = requestedParams + "&selConBedIds=" + data.bedId;
	});
	requestedParams = requestedParams.substring(1, requestedParams.length);

	return requestedParams;
}
function confirmMapping() {
	requestData = "";
	if ($current == "concept") {
		$.each($("input[name='cMapCheck']"), function(index, item) {
			data = $(item).data('data');
			requestData = requestData + "&saveMappings[" + index + "].id="
					+ data.id;
			requestData = requestData + "&saveMappings[" + index + "].aedId="
					+ data.aedId;
			requestData = requestData + "&saveMappings[" + index + "].bedId="
					+ data.bedId;
			requestData = requestData + "&saveMappings[" + index + "].bedType="
					+ data.conType;
			requestData = requestData + "&saveMappings[" + index + "].delMap="
					+ !$(this).is(':checked');
			requestData = requestData + "&saveMappings[" + index
					+ "].dispName=" + data.conDispName;
			requestData = requestData + "&saveMappings[" + index + "].mapType="
					+ data.mappingType;

		});
		
		requestData = requestData.substring(1, requestData.length);
		
		$('#instanceMapsSelected').hide();
		$("#instanceColumnConcept").hide();
		// alert("request Data "+requestData);
		$("#enableConfirmMappings").hide();
		$("#disableConfirmMappings").hide();
		$("#enableConfirmMappingsLoader").show();
		getJSONData(storeConceptMappingURL, requestData,
				processConceptMappingJsonData,processConceptMappingJsonDataError);
		$("#conceptFilter").attr("selectedIndex", "0");
		// $instanceClicked=false;
		$editFlag = true;
		$previousClick.length = 0;
		$clickCount = 0;
		$conceptClicked = false;
	}
	if ($current == "instance") {
		
		requestData = "selColAedId=" + currentConceptMappingForInstances.aedId
				+ "&selConBedId=" + currentConceptMappingForInstances.bedId;
		$.each($("input[name='iMapCheck']"), function(index, item) {
			data = $(item).data('data');
			requestData = requestData + "&saveMappings[" + index + "].id="
					+ data.id;
			requestData = requestData + "&saveMappings[" + index + "].aedId="
					+ data.aedId;
			requestData = requestData + "&saveMappings[" + index + "].bedId="
					+ data.bedId;
					if(data.instanceType==undefined){data.instanceType="E";}
			requestData = requestData + "&saveMappings[" + index + "].bedType="
					+ data.instanceType;
					
			requestData = requestData + "&saveMappings[" + index + "].delMap="
					+ !$(this).is(':checked');
			requestData = requestData + "&saveMappings[" + index
					+ "].dispName=" + data.instanceDispName;
			requestData = requestData + "&saveMappings[" + index + "].mapType="
					+ data.mappingType;
		});
		// $("#instanceMapsSelected").empty();

		// $("#InstancesSuggestedDiv").empty();
		// alert("request Data "+requestData);
		var saveAllPages = $("#saveAllPagesInstances").is(":checked");
		getJSONData(storeInstanceMappingURL, requestData + "&saveAll="
				+ saveAllPages + "&pageNo=" + $("#instancePageno").text(),
				processInstanceMappingJsonData);
				$("#instanceMapsContent").empty();
		$("#instanceFilter").attr("selectedIndex", "0");
		$("#instanceMapsContent").empty();
		$("#enableInstanceConfirmMappings").hide();
		$("#disableInstanceConfirmMappings").hide();
		$("#enableInstanceConfirmMappingsLoader").show();

		// $conceptClicked=false;
		$instanceClicked = false;
		$editFlag = true;
		$previousInstanceClick.length = 0;
		$clickInstanceCount = 0;
	}
}
function processTableColumnsJsonDataError(){
	$("#loadingATLink").hide();
	$tableColumnsMain.append("Error while loading Tables");
}
function processTableColumnsJsonData(data) {
	if (data != null) {
		$tableColumnsMain.empty();
		$.each(data, function(i, tables) {
			rowNum = i + 1;
			// alert("checkValue----"+checkValue)
			checkId = tables.id;

			$data = $("<li></li>").addClass(options.tableClassName);
			$("<input type=checkbox name='assetTables'  >").attr("id", checkId)
					.data("data", tables).bind("click", highlightTables)
					.appendTo($data);
			$("<span></span>").html(tables.tableDisplayName)
					.addClass("tableHolder").appendTo($data);
			$data.appendTo($tableColumnsMain);
			$innerUL = $("<ul style='display: none;'></ul>").attr("id",
					"table_" + checkId);

			$.each(tables.cols, function(i, cols) {
				// rowNum=i+1;

				checkId = cols.id;
				liId = tables.tableDisplayName + "#" + cols.dispName;

				$inner = $("<li></li>").addClass(options.columnClassName).attr(
						"id", liId).data("data", cols);
				$("<input type=checkbox name='assetColumns'  >").attr("id",
						checkId).attr("value", cols.aedId).bind("click",
						highlightColumns).appendTo($inner);
				$("<span></span>").html(cols.dispName).addClass("columnHolder")
						.appendTo($inner);

				$inner.appendTo($innerUL);

			});

			$innerUL.appendTo($data);
		});
		$("#loadingATLink").hide();
		$data.appendTo($tableColumnsMain);

		// TODO: iterate through new concepts
		createToggle();
		makeTableColumnsDraggable();
	}
}

function highlightTables() {
	$me = $(this);
	state = $me.is(':checked');
	$.each($me.parent().children().children()
			.children("input[name='assetColumns']"), function(i, v) {

		$(this).attr("checked", state);
		$(this).trigger("click");
		$(this).attr("checked", state);

	});

}

function highlightColumns() {
	bgColor = "#ffffff";
	$me = $(this);
	state = $me.is(':checked');
	if (state) {
		bgColor = "#00ff00";
	}
	columnName = $(this).parent().data("data").dispName;
	tableName = $(this).parent().parent().parent().children("input:checkbox")
			.data("data").tableDisplayName;
	lhs = tableName + "-" + columnName;
	$.each($("span.tablecolDisp"), function(i, v) {
		if ($(this).html() == lhs) {
			$(this).css("background-color", bgColor)
		}
	});

}
function highlightMembers() {
	bgColor = "#ffffff";
	$me = $(this);
	state = $me.is(':checked');
	if (state) {
		bgColor = "#00ff00";
	}
	memberName = $(this).parent().data("data").dispName;
	// tableName=$(this).parent().parent().parent().children("input:checkbox").data("data").dispName;
	// lhs=tableName+"-"+columnName;
	$.each($("span.memberDisp"), function(i, v) {
		if ($(this).html() == memberName) {
			$(this).css("background-color", bgColor)
		}
	});

}
function highlightBT() {
	bgColor = "#ffffff";
	$me = $(this);
	state = $me.is(':checked');
	if (state) {
		bgColor = "#00ff00";
	}
	btName = $(this).parent().data("data").dispName;

	$.each($("span.conceptName"), function(i, v) {
		if ($(this).html() == btName) {
			$(this).css("background-color", bgColor)
		}
	});

}
function highlightBTFromPage(chkBox) {
	bgColor = "#ffffff";
	$me = chkBox;
	state = $me.is(':checked');
	if (state) {
		bgColor = "#00ff00";
	}
	btName = $me.parent().data("data").dispName;

	$.each($("span.conceptName"), function(i, v) {
		if ($(this).html() == btName) {
			$(this).css("background-color", bgColor)
		}
	});

}
function highlightInstances() {
	bgColor = "#ffffff";
	$me = $(this);
	state = $me.is(':checked');
	if (state) {
		bgColor = "#00ff00";
	}
	btName = $(this).parent().data("data").dispName;

	$.each($("span.instanceName"), function(i, v) {
		if ($(this).html() == btName) {
			$(this).css("background-color", bgColor)
		}
	});

}
function highlightInstancesFromPage(chkBox) {
	bgColor = "#ffffff";
	$me = chkBox;
	state = $me.is(':checked');
	if (state) {
		bgColor = "#00ff00";
	}
	btName = $me.parent().data("data").dispName;

	$.each($("span.instanceName"), function(i, v) {
		if ($(this).html() == btName) {
			$(this).css("background-color", bgColor)
		}
	});

}
function processColumnMembersJsonDataError(){
	$("#loadingCMLink").hide();
	$columnMembersMain.append("Error while loading Members");
}
function processColumnMembersJsonData(data) {
	if (data != null) {
		$columnMembersMain.empty();		
		if (data.header) {			
			buildPaginationController("memberSDX", columnMembersURL,
					processColumnMembersJsonData, data.header.currentPage,
					data.header.totalPages, {
						"selColAedId" : data.aedId
					});
		  }
		// $tableName=$("<li></li>").html(data.tblDispName).addClass(options.tableClassName);

		$data = $("<li></li>").addClass(options.tableClassName);
		// $("<input type=checkbox name='assetmembers'
		// >").attr("id",data.id).appendTo($data);
		$("<span></span>")
				.html('<img src="../images/minusImage.jpg" width="16" height="16" align="middle"  />')
				.appendTo($data);
		$("<span></span>").html(data.tblDispName + "-" + data.colDispName)
				.addClass("tableHolder").appendTo($data);
		$data.appendTo($tableColumnsMain);
		$colMem=$("<div id='colMemsDiv'></div>");
		$innerUL = $("<ul style=''></ul>").attr("id", "table_" + data.id);
		$.each(data.members, function(i, mems) {
			checkId = mems.id;
			liId = data.colDispName + "#" + mems.dispName;
			$inner = $("<li></li>").addClass(options.columnClassName).attr(
					"id", liId).data("data", mems);
			$("<input type=checkbox name='assetColumns'  >")
					.attr("id", checkId).bind("click", highlightMembers).attr(
							"value", mems.aedId).appendTo($inner);
			$("<span></span>").html(mems.dispName).addClass("columnHolder")
					.appendTo($inner);
			$inner.appendTo($innerUL);
		});
		$innerUL.appendTo($colMem);
		$colMem.appendTo($data);
		$data.appendTo($columnMembersMain);
		// $columnMembersMain.prepend($tableName);
		// TODO: iterate through new concepts
		// alert("out"+$columnMembersDiv.html());
		$("#loadingCMLink").hide();
		makeTableColumnsDraggable();
	}
}
function processBTListJsonDataError(){
	$("#loadingBTLink").hide();
	$businessTermMain.append("Error while loading Business Terms");
}
function processBTListJsonData(jsonData) {
	// $businessTermMain.empty();
	// TODO: check for errors
	if (jsonData != null) {
		$dispNames.length = 0;
		$tempBTData.length = 0;
		$.each(jsonData.terms, function(index, data) {
			$dispNames[index] = data.dispName;
			$tempBTData[index] = data;
		});
		sortAndDisplay($businessTermMain, $dispNames, $tempBTData);
		$("#loadingBTLink").hide();
		if (!$currentLink) {
			makeConceptDraggable();
		}
	}else{
		$("#loadingBTLink").hide();
		$businessTermMain.append("No Concepts Available");
	}
}
function processInstanceListJsonDataError(){
	$("#loadingITLink").hide();
	$instancesMain.append("error while loading Instances");
}
function processInstanceListJsonData(jsonData) {
	if (jsonData != null) {
		
		if(jsonData.terms!= null) {
			$("#instancesPagination").show();
		if (jsonData.header) {
			buildPaginationController("instanceKDX", instanceListURL,
					processInstanceListJsonData, jsonData.header.currentPage,
					jsonData.header.totalPages, {
						"conBedId" : jsonData.bedId
					});
		}
		$instancesMain.empty();
		$dispInstanceNames.length = 0;
		$tempInstanceData.length = 0;
		$.each(jsonData.terms, function(index, data) {
			$dispInstanceNames[index] = data.dispName;
			$tempInstanceData[index] = data;

		});
		sortAndDisplay($instancesMain, $dispInstanceNames, $tempInstanceData);
		$("#loadingITLink").hide();
		makeConceptDraggable();
		
		}else{
			$("#loadingITLink").hide();
	$instancesMain.append("No Instances available");
	$("#instancesPagination").hide();
		}
	}
	
}

function sortAndDisplay($MainVar) {
	if ($current == "concept") {
		// alert($dispNames.length="::");
		$sortedNames = $dispNames.sort(charOrdA);
		$.each($sortedNames, function(index, data) {
			$mydata = getDataForConcept(data);
			$li = $('<li></li>').addClass(options.btClassName).attr('id', data)
					.data("data", $mydata);
			$('<input type=checkbox name=conceptNames>').attr("id", "id").bind(
					"click", highlightBT).appendTo($li);
			$('<span></span>').addClass(options.btConceptClassName).html(data)
					.appendTo($li);
			$MainVar.append($li);

		});
	}
	if ($current == "instance") {
		$sortedNames = $dispInstanceNames.sort(charOrdA);
		$.each($sortedNames, function(index, data) {
			$mydata = getDataForInstance(data);
			$li = $('<li></li>').addClass(options.btClassName).attr('id', data)
					.data("data", $mydata);
			$('<input type=checkbox name=instanceNames>').attr("id", "id")
					.bind("click", highlightInstances).appendTo($li);
			$('<span></span>').addClass(options.btConceptClassName).html(data)
					.appendTo($li);
			$MainVar.append($li);

		});
	}
	// alert("out ins"+$MainVar.html());
}

function getDataForConcept(dispName) {
	$t = undefined;
	$.each($tempBTData, function(index, data) {

		if ($tempBTData[index].dispName == dispName) {

			$t = $tempBTData[index];
		}

	});
	return $t;
}
function getDataForInstance(dispName) {
	$t = undefined;
	$.each($tempInstanceData, function(index, data) {

		if ($tempInstanceData[index].dispName == dispName) {

			$t = $tempInstanceData[index];
		}

	});
	return $t;
}
function charOrdA(a, b) {
	if ((a != null) && (b != null)) {
		a = a.toUpperCase();
		b = b.toUpperCase();

		if (a > b) {
			return 1;
		}
		if (a < b) {
			return -1;
		}
	}
	return 0;
}

function changeCSS($Div, msg) {
	if (msg == "E") {
		$Div.css("color", "#FF0000");
	}
	if (msg == "W") {
		$Div.css("color", "#FFCC00");
	}
}
function enableDiableButtonsConcepts() {
	cboxes = $("input[name='cMapCheck']").length;
	if (cboxes > 0) {
		$("#enableConfirmMappings").show();
		$("#disableConfirmMappings").hide();
		$("#enableConfirmMappingsLoader").hide();
		$("#conceptMappingPage").show();
	} else {
		$("#enableConfirmMappings").hide();
		$("#disableConfirmMappings").show();
		$("#enableConfirmMappingsLoader").hide();
		$("#conceptMappingPage").hide();
	}
}
function processConceptMappingJsonDataError(){
	$("#loadingConceptMappingsLink").hide();
	$conceptMappingMain.append("Error while loading Concept Mappings ");
}
function processConceptMappingJsonData(data) {
	if (data != null) {
		if (data.header) {
			buildPaginationController("concept", conceptShowPageURL,
					processConceptMappingJsonData, data.header.currentPage,
					data.header.totalPages);
		}
		$("#mapsContent").empty();
		$("#mapsInstanceContent").empty();
		$("#loadingSuggestConceptsLink").hide();
		$("#suggestConceptsLink").show();

		$conceptMappingMain.empty();
		$("#mapsContent").empty();
		$("#messagesAndWarnings").empty().append(data.msg);
		setTimeout(function() {
			$("#messagesAndWarnings").empty();
		}, 9000);

		$conceptsState.length = 0;
		if (data.mappings != null) {
			$.each(data.mappings, function(i, mapping) {
				$conceptsState[i] = mapping.conDispName;
				$data = createMappingDataElement(mapping);
				$data.appendTo($conceptMappingMain);
				$("#loadingConceptMappingsLink").hide();
			});
		}

		$.each($("input[name='conceptNames']:checked"), function(i, v) {
			$me = $(this);
			highlightBTFromPage($me);
		});
		$("#loadingConceptMappingsLink").hide();

		if (data.newConcepts != null) {
			$.each(data.newConcepts, function(i, concept) {
				$tempConcept = {};
				$tempConcept.bedId = concept.bedId;
				// $tempConcept.conType=concept.type;
				$tempConcept.dispName = concept.dispName;
				bedIdNotExist = checkBedIdExist(concept.bedId);
				if (bedIdNotExist) {
					$tempBTData[$tempBTData.length] = $tempConcept;
					$dispNames[$dispNames.length] = concept.dispName;
				}

			});
			addConceptsToBT();

		}
		enableDiableButtonsConcepts();
	}
}
function checkBedIdExist(bedId) {

	$.each($tempBTData, function(i, v) {

		if (v.bedId == bedId) {
			return false;
		}
	})
	return true;
}
function addConceptsToBT() {
	$businessTermDiv.empty();
	$businessTermMain = $('<span></span>');
	$businessTermDiv.append($businessTermMain);
	sortAndDisplay($businessTermMain);
	makeConceptDraggable();
}

function createMappingDataElement(mapping) {
	checkId = "SMChk" + mapping.aedId;
	checkValue = checkId + "#" + mapping.conType;
	// alert("checkValue----"+checkValue)
	$data = $("<div ></div>").addClass(options.suggestsClassName).bind(
			"mouseover", conceptMouseOver).bind("mouseout", conceptMouseOut);
	changeCSS($data, mapping.msgType);
	$("<input type=checkbox name='cMapCheck' checked value=" + checkValue
			+ " onclick=checkedMap();>").attr("id", checkId).data("data",
			mapping).appendTo($data);
	$("<span class='tablecolDisp'></span>").html(mapping.tableDisplayName + "-"
			+ mapping.colDispName).bind("mouseover", conceptMouseOver).bind(
			"mouseout", conceptMouseOut).appendTo($data);

	$data.append("<span class='mapArrow'></span>")
			.append("<img src='../images/arrowRight.jpg' align='middle' />");
	$conceptDiv = $("<span></span>").appendTo($data);
	$conceptSpan = $("<span class='conceptName' >" + mapping.conDispName
			+ "</span>").attr("id", "conDispName" + mapping.aedId)
			.addClass("conceptLink").data("data", mapping).bind("mouseover",
					conceptMouseOver).bind("mouseout", conceptMouseOut)
			.appendTo($conceptDiv)
	changeCSS($conceptSpan, mapping.msg);
	$editFlag = true;
	// alert(mapping.msg);
	// alert(mapping.msgType);
	$conceptSpan.bind("click", conceptClick);
	$data.append("<img src='../images/1pix.jpg' width='5' height='5' >");
	// $conceptSpan.bind("mouseover",conceptMouseOver).bind("mouseout",conceptMouseOut);

	// add instance link
	if (mapping.mapInstance) {
		$mapInstance = $("<span class='mapInstanceLink'>Map Instance</span>")
				.click(function() {
								mapInstanceClicked=true;
					 $("#instanceMappingId").show();
					$("#instanceMapsContent").empty();
					$("#instancePage").show();
					$("#newInstance").show();
					$('#instanceMapsSelected').show();
					$("#instanceColumnConcept").show();
					currentConceptMappingForInstances = mapping;
					showInstanceMapping();
					$editFlag = true;
				});
		$mapInstance.appendTo($data);

	}

	$message = $("<div></div>").addClass("messageClass").hide();
	$message.html(mapping.msg);
	$message.appendTo($data);

	return $data;
	// TODO: iterate through new concepts

	// if(rowNum>=1){ $("#enableConfirmMappings").show();
	// $("#disableConfirmMappings").hide();}
}

function showInstanceMapping() {
	$current = "instance";
	TabbedPanels1.showPanel(1);
	// $("#btDiv").empty().append("Instances");
	// $("#atDiv").empty().append("Column Members");
	$("#loadingITLink").show();
	$("#loadingCMLink").show();

	showHidePanes();
	$instancesMain.empty();
	$columnMembersMain.empty();
	$instanceMappingMain.empty();
	//$("#loadingInstanceMappingsLink").show();
	//setTimeout(hideLoaders, 10000);
	getJSONData(instanceListURL, {
		conBedId : currentConceptMappingForInstances.bedId
	}, processInstanceListJsonData,processInstanceListJsonDataError);
	$("#loadingITLink").hide();
	getJSONData(columnMembersURL, {
		SelColAedId : currentConceptMappingForInstances.aedId
	}, processColumnMembersJsonData,processColumnMembersJsonDataError);
	$("#columnMembersDiv").show();
	$("#instancesDiv").show();
	$("#loadingITLink").show();

	getJSONData(existingInstanceMappingURL, {
		assetId : $('#assetId').val(),
		selConBedId : currentConceptMappingForInstances.bedId,
		selColAedId : currentConceptMappingForInstances.aedId
	}, processInstanceMappingJsonData,processInstanceMappingJsonDataError);

	$("#disableInstanceConfirmMappings").hide();
	$("#enableInstanceConfirmMappings").show();

}
function hideLoaders() {
	$("#loadingITLink").hide();
	$("#loadingCMLink").hide();
	$("#loadingInstanceMappingsLink").hide();
}

function enableDiableButtonsInstances() {
	cboxes = $("input[name='iMapCheck']").length;
	if (cboxes > 0) {
		$("#enableInstanceConfirmMappings").show();
		$("#disableInstanceConfirmMappings").hide();
		$("#enableInstanceConfirmMappingsLoader").hide();
	} else {
		$("#enableInstanceConfirmMappings").hide();
		$("#disableInstanceConfirmMappings").show();
		$("#enableInstanceConfirmMappingsLoader").hide();
	}
}
function processInstanceMappingJsonDataError(){
	$("#loadingSuggestInstancesLink").hide();
	$instanceMappingMain.append("Error while loading instance mappings");
}
function processInstanceMappingJsonData(jsonData) {
	if (jsonData != null) {
		$("#loadingSuggestInstancesLink").hide();
		$("#suggestInstancesLink").show();

		$instanceState = true;
	    var SelColAedId = undefined;
        var $mappingFilter = undefined;
        
		SelColAedId = currentConceptMappingForInstances.aedId;
		//filter records
		var mappingFilterText = $("#instanceFilter option:selected").val();		
		if (mappingFilterText == 'confirmed') {
			$mappingFilter = 'E';
		} else if (mappingFilterText == 'un-confirmed') {
			$mappingFilter = 'S';
		} else {
			$mappingFilter = 'A';
		}		
		if (jsonData.header) {
			buildPaginationController("instance", instanceShowPageURL,
					processInstanceMappingJsonData,
					jsonData.header.currentPage, jsonData.header.totalPages,{"selColAedId":SelColAedId,"mappingFilter":$mappingFilter});
		}
		$instanceMappingMain.empty();
		$header = $("#instanceColumnConcept");
		$header.empty();

		$("#messagesAndWarnings").empty().append(jsonData.msg);
		if ($("#messagesAndWarnings").text().length > 0
				&& jsonData.jobRequestId == null) {
			getJSONData(instanceListURL, {
				conBedId : currentConceptMappingForInstances.bedId
			}, processInstanceListJsonData,processInstanceListJsonDataError);
			// setTimeout(function(){$("#messagesAndWarnings").empty(); },9000);
		}

		if (jsonData.jobRequestId != null) {
			$("#enableInstanceConfirmMappingsLoader").hide();
			$("#enableInstanceConfirmMappings").hide();
			$("#disableInstanceConfirmMappings").show();
		}
		$("<span class='tableColDisp'></span>")
				.html(currentConceptMappingForInstances.tblDispName + "-"
						+ currentConceptMappingForInstances.colDispName)
				.appendTo($header);
		$header
				.append("<span class='mapArrow'></span>")
				.append("<img src='../images/arrowRight.jpg' align='middle' />");
		$("<span class='memberDisp'></span>")
				.html(currentConceptMappingForInstances.conDispName)
				.appendTo($header);
		$.each(jsonData.mappings, function(i, mapping) {
			$data = createInstanceMappingDataElement(mapping);
			$data.appendTo($instanceMappingMain);
		});

		$.each($("input[name='instanceNames']:checked"), function(i, v) {
			$me = $(this);
			highlightInstancesFromPage($me);
		});

		// $("#loadingInstanceMappingsLink").hide();
		if ($("#saveAllPagesInstances").is(":checked")) {
			$("#saveAllPagesInstances").attr("checked", false)
		}
		// TODO: iterate through new concepts
		if (jsonData.newInstances != null) {
			$.each(jsonData.newInstances, function(i, instance) {
				$tempInstance = {};
				$tempInstance.bedId = instance.bedId;
				// $tempConcept.conType=concept.type;
				$tempInstance.dispName = instance.dispName;
				bedIdNotExist = checkBedIdExistForInstance(instance.bedId);
				if (bedIdNotExist) {
					$tempInstanceData[$tempInstanceData.length] = $tempInstance;
					$dispInstanceNames[$dispInstanceNames.length] = instance.dispName;
				}
			});
			addInstancesToIT();
		}

		enableDiableButtonsInstances();
	}
}
function addInstancesToIT() {
	$instancesDiv.empty();
	$instancesMain = $('<span></span>');
	$instancesDiv.append($instancesMain);
	sortAndDisplay($instancesMain);
	makeConceptDraggable();
}

function checkBedIdExistForInstance(bedId) {

	$.each($tempInstanceData, function(i, v) {

		if (v.bedId == bedId) {
			return false;
		}
	})
	return true;
}
function createInstanceMappingDataElement(mapping) {
	checkId = "ISMChk" + mapping.aedId;
	checkValue = checkId + "#" + mapping.instanceType;
	// alert("checkValue----"+checkValue)
	$data = $("<div ></div>").addClass(options.suggestsClassName).data("data",
			mapping);

	changeCSS($data, mapping.msgType);

	$("<input type=checkbox onclick=checkedInstanceMap(); name='iMapCheck' checked value="
			+ checkValue + ">").attr("id", checkId).data("data", mapping)
			.appendTo($data);
	$("<span class='memberDisp'></span>").html(mapping.memDispName).bind(
			"mouseover", instanceMouseOver).bind("mouseout", instanceMouseOut)
			.appendTo($data);
	$data.append("<span class='mapArrow'></span>")
			.append("<img src='../images/arrowRight.jpg' align='middle' />");
	$instanceSpan = $("<span class='instanceName' >" + mapping.instanceDispName
			+ "</span>").attr("id", "instanceDispName" + mapping.aedId)
			.addClass("instanceLink").bind("mouseover", instanceMouseOver)
			.bind("mouseout", instanceMouseOut).appendTo($data);
	changeCSS($instanceSpan, mapping.msg);
	$instanceSpan.bind("click", instanceClick);
	if (mapping.msg) {
		$message = $("<span></span>").addClass("messageInsClass");
		$message.html(mapping.msg).hide();
		$message.appendTo($data);
	}

	return $data;
}
function instanceClick() {

	$(".ac_results").hide();
	$me = $(this);
	// alert($me.parent().parent().children("span.messageClass").html());
	$me.parent().parent().children("div.messageClass").hide();
	$me.parent().parent().unbind("mouseover");
	instancedata = $me.parent().children("input[type='checkbox']").data("data");
	$inputVal = instancedata.instanceDispName;
	if ($instanceInput.val() != $inputVal) {
		$instanceInput.val($inputVal);
		$parent = $(this.parentNode);
		$("div.sugClassName").css("display", "inline");
		$parent.data("data", instancedata);
		$previousInstanceClick[$clickInstanceCount] = $parent;

		if ($instanceClicked) {

			removeInput(2);

		}

		$clickInstanceCount++;
		$instanceClicked = true;

		$me.empty();
		$me.append($instanceInput);
		$instanceInput.show();
		$instanceInput.css("display", "inline");
		$instanceInput.css("width", ($inputVal.length * 10) + "px");
		$instanceInput.focus();
		$me
				.append("<span class='noUL'><img src='../images/1pix.jpg' width='5' height='5' ></span>");
		$instanceInput.autocomplete(suggestInstanceURL, {
			selectionCallBack : function alertme(selectedObject) {
				$newInstance = selectedObject;
				if (typeof(selectedObject) == 'string') {
					$instanceInput.css("width", (selectedObject.length * 7)
							+ "px");
					removeInput(1);
					reflectInIT(selectedObject, instancedata.bedId);
					$("#enableInstanceConfirmMappings").show();
					$("#disableInstanceConfirmMappings").hide();
					$instanceClicked = false;
					$editFlag = true;
					$previousInstanceClick.length = 0;
					$clickInstanceCount = 0;
				} else {
					// alert('got message'+ selectedObject.dispName);
					$instanceInput.css("width",
							(selectedObject.dispName.length * 7) + "px");
					removeInput(1);
					$("#enableInstanceConfirmMappings").show();
					$("#disableInstanceConfirmMappings").hide();
					$instanceClicked = false;
					$editFlag = true;
					$previousInstanceClick.length = 0;
					$clickInstanceCount = 0;
				}
			},
			extraParams : {
				conBedId : currentConceptMappingForInstances.bedId
			}
		});

		$instanceInput.bind("keypress", function(e) {
			if (e.keyCode == 13) {
				$parent = $(this.parentNode.parentNode);
				removeInput(1);
				e.stopPropagation();
				$(".ac_results").hide();
			}

		});

		// alert("$editFlag"+$editFlag);
		if ($editFlag) {
			// add new link
			$editFlag = false;
			// if(instancedata.conType == "N") {
			$newInstanceLink = $("<span class='addInstanceLink'>New Instance</span>")
					.click(function() {
						$(this).html('<img src=../images/loaderTrans.gif  />');
						currentInstanceMapping = instancedata;
						addInstance(instancedata.bedId, $(this));
					});

			$newInstanceLink.appendTo($(this.parentNode));
			$(this.parentNode)
					.append("<img src='../images/1pix.jpg' width='5' height='5' border='0' >");
			$newInstanceLinkLoader = $("<span id=loadingAddInstances"
					+ instancedata.bedId
					+ "Link style=display:none;><img src=../images/loaderTrans.gif  /></span>");
			$newInstanceLinkLoader.appendTo($(this.parentNode));
			// }

			// add edit link
			if (instancedata.mappingType == "E") {
				// $(this.parentNode).append("&nbsp;");
				$editInstanceLink = $("<span class='editInstanceLink'>Edit</span>")
						.click(function() {
							$(this).html('<img src=../images/loaderTrans.gif  />');
							currentInstanceMapping = instancedata;
							editInstance(instancedata.insId, $(this));
						});
				// $editInstanceLink.prepend("<img src='../images/1pix.jpg'
				// width='5' height='5' >");
				$editInstanceLink.appendTo($(this.parentNode));
				$editInstanceLinkLoader = $("<span id=loadingEditInstances"
						+ instancedata.bedId
						+ "Link style=display:none;> <img src=../images/loaderTrans.gif /></span>");
				$editInstanceLinkLoader.appendTo($(this.parentNode));
			}
		}
	}
}

function removeInput(x) {
	$(".ac_results").hide();
	if ($current == "instance") {

		$preElm = $previousInstanceClick[$previousInstanceClick.length - x];
		$preElm.children('.instanceName').empty();
		$preElm.children('.editInstanceLink').empty();
		$preElm.children('.addInstanceLink').empty();
		$preElm.children('.closeInstanceLink').empty();
		if (x == 1) {
			$preElm.children('.instanceName').prepend($instanceInput.val());
			if ($newInstance.bedId) {
				instancedata.bedId = $newInstance.bedId;
			}
		}
		if (x == 2)
			$preElm.children('.instanceName')
					.prepend($preElm.data("data").instanceDispName);
		instancedata.instanceDispName = $instanceInput.val();

		$instanceInput.hide();
		$editFlag = true;
		$preElm.parent().bind("mouseover", instanceMouseOver);
	}
	if ($current == "concept") {

		$preElm = $previousClick[$previousClick.length - x];
		$preElm.children('.conceptName').empty();
		$preElm.children('.editConceptLink').empty();
		$preElm.children('.addConceptLink').empty();
		$preElm.children('.closeConceptLink').empty();
		if (x == 1) {
			$preElm.children('.conceptName').prepend($conceptInput.val());
			if ($newConcept.bedId) {
				conceptdata.bedId = $newConcept.bedId;
			}
		}
		if (x == 2)
			$preElm.children('.conceptName')
					.prepend($preElm.data("data").conDispName);
		conceptdata.conDispName = $conceptInput.val();

		$conceptInput.hide();
		$editFlag = true;
		$preElm.parent().bind("mouseover", conceptMouseOver);
	}
}
function conceptMouseOver() {
	$me = $(this).parent().children("div.messageClass");
	if ($me != null) {
		if ($me.html() != "") {
			$me.show();
			$me.css("width", "400px");
			$me.css("white-space", "normal");
			// $me.css("left",$me.css("left"));
		}
	}
}
function instanceMouseOver() {
	$me = $(this).parent().children("div.messageClass");
	if ($me != null) {
		if ($me.html() != "") {
			$me.show();
			$me.css("width", "400px");
			$me.css("white-space", "normal");
			// $me.css("left",$me.css("left"));
		}
	}
}

function findPos(obj) {
	var curleft = obj.offsetLeft || 0;
	var curtop = obj.offsetTop || 0;
	while (obj = obj.offsetParent) {
		curleft += obj.offsetLeft
		curtop += obj.offsetTop
	}
	return {
		x : curleft,
		y : curtop
	};
}

function conceptMouseOut() {
	$(this).parent().children("div.messageClass").hide();
}
function instanceMouseOut() {
	$(this).parent().children("div.messageClass").hide();
}

function showMapsSuggested() {
	cState = checkConceptsState();
	if (!cState) {
		state = confirm("Do you want save changes");
	}
	arrowImage = "<img src=images/arrowRight.jpg alt='Edit Concept' width=25 align=middle  height=25 border=0 />"
	$("#conceptsSuggestedDiv").empty();
	$("#conceptsSuggestedDiv").fadeIn("fast");
	$("#conceptsSuggestedDiv").show();
	requestedParams = getTableConceptParams();
	var assetId = $('#assetId').val();
	
	if (requestedParams != '') {
	  requestedParams = requestedParams + "&";
	}
	requestedParams = requestedParams + "assetId=" + assetId;
	
	getJSONData(suggestConceptMappingURL, requestedParams,
			processConceptMappingJsonData);
	$("#enableConfirmMappings").show();
	$("#disableConfirmMappings").hide();
	$("#enableConfirmMappingsLoader").hide();
}
function showMapsInstancesSuggested() {
	//
	if (!$instanceState) {
		state = confirm("Do you want save changes");
	}
	$("#instanceSuggestedDiv").empty();
	$("#instanceSuggestedDiv").fadeIn("fast");
	$("#instanceSuggestedDiv").show();
	getJSONData(suggestInstanceMappingURL, {
		selColAedId : currentConceptMappingForInstances.aedId,
		selConBedId : currentConceptMappingForInstances.bedId
	}, processInstanceMappingJsonData);
}
function createToggle() {
	tree = $('#myTree');
	$('li', tree.get(0)).each(function() {
		subbranch = $('ul', this);
		if (subbranch.size() > 0) {
			if (subbranch.eq(0).css('display') == 'none') {
				// if($(this).children(".expandImage")[0]==undefined){
				$(this)
						.prepend('<img src="../images/bullet_toggle_plus.png" width="16" height="16" class="expandImage" />');
				// }
			} else {
				// if($(this).children(".expandImage")[0]==undefined){
				$(this)
						.prepend('<img src="../images/bullet_toggle_minus.png" width="16" height="16" class="expandImage" />');
				// }
			}
		} else {
			$(this)
					.prepend('<img src="../images/spacer.gif" width="1" height="1" class="expandImage" />');
		}
	});
	$('img.expandImage', tree.get(0)).click(function() {
		if (this.src.indexOf('spacer') == -1) {
			subbranch = $('ul', this.parentNode).eq(0);
			if (subbranch.css('display') == 'none') {
				subbranch.show();
				this.src = '../images/bullet_toggle_minus.png';
			} else {
				subbranch.hide();
				this.src = '../images/bullet_toggle_plus.png';
			}
		}
	});
}

function makeTableColumnsDraggable() {

	$('li.column').draggable({
		helper : 'clone',
		start : function() {
			dragColumnElement = this;
			dragColumnData = $(this).data("data");
			// alert(dragColumnData.dispName);
		}
	});

	$("li.column").bind("mouseover", function(e) {
		if (hitFlagConcept) {
			hitFlagColumn = false;
			hitFlagConcept = false;
			dragColumnElement = this;
			dragColumnData = $(this).data("data");
			obj = checkDuplicates();
			if (!obj) {
				createMap();
			}
		}
	});

	$('span.columnHolder').droppable({
		accept : '.concept',
		hoverClass : 'dropOver',
		over : function(dragged) {

			if (!this.expanded) {
				subbranches = $('ul', this.parentNode);
				if (subbranches.size() > 0) {
					subbranch = subbranches.eq(0);
					this.expanded = true;
					if (subbranch.css('display') == 'none') {
						var targetBranch = subbranch.get(0);
						this.expanderTime = window.setTimeout(function() {
							$(targetBranch).show();
							$('img.expandImage', targetBranch.parentNode).eq(0)
									.attr('src',
											'images/bullet_toggle_minus.png');
							$.recallDroppables();
						}, 500);
					}
				}
			}
		},
		out : function() {

			if (this.expanderTime) {
				window.clearTimeout(this.expanderTime);
				this.expanded = false;
			}
		},
		drop : function(dropped) {
			// alert("right column selected::"+rightColumn);
			hitFlagConcept = true;

			$('span.conceptHolder').each(function() {
				subbranches = $('ul', this.parentNode);
				subbranches.show();
			});
		}
	});

	$("li.column").mousedown(function() {
		// alert("1::"+this.id);
		hitFlagConcept = false;
		leftColumn = this.id;
	});

	$('#checkAllMembers').click(function() {

		$("#columnMembersDiv  INPUT[type='checkbox']").attr('checked',
				$('#checkAllMembers').is(':checked'));
		$("#columnMembersDiv  INPUT[type='checkbox']").trigger("click");
		$("#columnMembersDiv  INPUT[type='checkbox']").attr('checked',
				$('#checkAllMembers').is(':checked'));
	});

}

function reflectInBT(selectedObject, bedId) {
	// alert(bedId);
	$.each($("li." + options.btClassName), function() {
		if ($(this).data("data").bedId == bedId) {
			$(this).children("span").html(selectedObject);
		}

	});
	$.each($("#mapsSelected span.conceptName"), function() {
		if ($(this).data("data").bedId == bedId) {
			$(this).html(selectedObject);
			$(this).data("data").conDispName = selectedObject;
		}

	});
}

function reflectInIT(selectedObject, bedId) {
	// alert(selectedObject+"::"+bedId);
	$.each($("li." + options.btClassName), function() {
		if ($(this).data("data").bedId == bedId) {
			$(this).children("span").html(selectedObject);
		}

	});
	// $.each($("#instanceMapsSelected span.instanceName"),function(){
	// if($(this).parent().children("input:checkbox").data("data").bedId==bedId){$(this).html(selectedObject);
	// $(this).parent().children("input:checkbox").data("data").instanceDispName=selectedObject;
	// }
	//		
	// });
}

function conceptClick() {

	$(".ac_results").hide();
	$me = $(this);
	// alert($me.parent().parent().children("span.messageClass").html());
	$me.parent().parent().children("div.messageClass").hide();
	$me.parent().parent().unbind("mouseover");
	conceptdata = $me.data("data");
	$inputVal = conceptdata.conDispName;
	$currentAedId = conceptdata.aedId;
	$preAedId = 0;
	if ($previousClick.length > 0) {
		$preElm = $previousClick[$previousClick.length - 1];
		$preAedId = $preElm.data("data").aedId;

	}

	if ($currentAedId != $preAedId) {
		$conceptInput.val($inputVal);
		$parent = $(this.parentNode);
		$("div.sugClassName").css("display", "inline");
		$parent.data("data", conceptdata);
		$previousClick[$clickCount] = $parent;

		if ($conceptClicked) {

			removeInput(2);

		}

		$clickCount++;
		$conceptClicked = true;

		$me.empty();
		$me.append($conceptInput);
		$conceptInput.show();
		$conceptInput.css("display", "inline");
		$conceptInput.css("width", ($inputVal.length * 10) + "px");
		$conceptInput.focus();
		$me
				.append("<span class='noUL'><img src='../images/1pix.jpg' width='5' height='5' ></span>");
		$conceptInput.autocomplete(suggestConceptURL, {
			selectionCallBack : function conceptSuggestCallBack(selectedObject) {
				$newConcept = selectedObject;
				if (typeof(selectedObject) == 'string') {
					// alert('got message :'+ selectedObject);
					$conceptInput.css("width", (selectedObject.length * 7)
							+ "px");
					removeInput(1);
					reflectInBT(selectedObject, conceptdata.bedId);
					$("#enableConfirmMappings").show();
					$("#disableConfirmMappings").hide();
					$("#enableConfirmMappingsLoader").hide();
					$previousClick.length = 0;
					$clickCount = 0;
					$conceptClicked = false;
				} else {
					// alert('got message :'+ selectedObject.bedId);
					$conceptInput.css("width",
							(selectedObject.dispName.length * 7) + "px");
					removeInput(1);
					$("#enableConfirmMappings").show();
					$("#disableConfirmMappings").hide();
					$("#enableConfirmMappingsLoader").hide();
					$previousClick.length = 0;
					$clickCount = 0;
					$conceptClicked = false;
				}

			}
		});

		$conceptInput.bind("keypress", function(e) {
			if (e.keyCode == 13) {
				$parent = $(this.parentNode.parentNode);
				removeInput(1);
				e.stopPropagation();
				$(".ac_results").hide();
			}

		});

		if ($editFlag) {
			// add new link
			$editFlag = false;
			// if(conceptdata.conType == "N") {
			$newConceptLink = $("<span class='addConceptLink'>New Concept</span>")
					.click(function() { $addConceptFlag=true;
						currentMapping = conceptdata;

						addConcepts(conceptdata.bedId, $(this));
					});

			$newConceptLink.appendTo($(this.parentNode));
			$(this.parentNode)
					.append("<span class='noUL'><img src='../images/1pix.jpg' width='5' height='5' ></span>");
			$newConceptLinkLoader = $("<span id=loadingAddConcepts"
					+ conceptdata.bedId
					+ "Link style=display:none;><img src=../images/loaderTrans.gif  /></span>");
			$newConceptLinkLoader.appendTo($(this.parentNode));
			// }

			// add edit link
			if (conceptdata.conType == "E") {
				// $(this.parentNode).append("&nbsp;");
				$editConceptLink = $("<span class='editConceptLink'>Edit</span>")
						.click(function() {
							$(this).html('<img src=../images/loaderTrans.gif />');
							currentMapping = conceptdata;

							editConcepts(conceptdata.conId, $(this));
						});
				// $editConceptLink.prepend("<img src='../images/1pix.jpg'
				// width='5' height='5' >");
				$editConceptLink.appendTo($(this.parentNode));
				$editConceptLinkLoader = $("<span id=loadingEditConcepts"
						+ conceptdata.bedId
						+ "Link style=display:none;> <img src=../images/loaderTrans.gif /></span>");
				$editConceptLinkLoader.appendTo($(this.parentNode));

			}
		}

	}
}

function buildPaginationController(type, pageURL, processFunction, currentPage,
		totalPages, $requestParam) {			
	if (!$requestParam) {
		$requestParam = {};
	}
	if (totalPages == 0) {
		currentPage = totalPages;
	}
	$("#" + type + "Pageno").empty().append(currentPage).show();
	$("#" + type + "Numberofpages").empty().append(totalPages).show();
	$nav = $("#" + type + "NavFirst");
	$nav.unbind();
	if (currentPage == 1) {
		$nav.attr("disabled", true);
		showNavControl("d" + type + "NavFirst", "d" + type + "NavPrevious",
				type + "NavNext", type + "NavLast", type + "NavAll");
		hideNavControl(type + "NavFirst", type + "NavPrevious", "d" + type
				+ "NavNext", "d" + type + "NavLast", "d" + type + "NavAll");
	} else {
		$nav.attr("disabled", false);
		showNavControl(type + "NavFirst", type + "NavPrevious", type
				+ "NavLast", type + "NavLast", type + "NavAll");
		hideNavControl("d" + type + "NavFirst", "d" + type + "NavPrevious", "d"
				+ type + "NavNext", "d" + type + "NavLast", "d" + type
				+ "NavAll");
		$nav.click(function() {
			$requestParam.pageNo = 1;		  	
			getJSONData(pageURL, $requestParam, processFunction);
		});
	}
	$nav = $("#" + type + "NavPrevious");
	$nav.unbind();
	if (currentPage <= 1) {
		$nav.attr("disabled", true);
		hideNavControl(type + "NavFirst", type + "NavPrevious", 'd' + type
				+ 'NavNext', 'd' + type + 'NavLast', "d" + type + "NavAll")
		showNavControl("d" + type + "NavFirst", "d" + type + "NavPrevious",
				type + 'NavNext', type + 'NavLast', type + "NavAll")
	} else {
		$nav.attr("disabled", false);
		hideNavControl("d" + type + "NavFirst", "d" + type + "NavPrevious", 'd'
				+ type + 'NavNext', 'd' + type + 'NavLast', "d" + type
				+ "NavAll")
		showNavControl(type + "NavFirst", type + "NavPrevious", type
				+ 'NavNext', type + 'NavLast', type + "NavAll")
		$nav.click(function() {
			$requestParam.pageNo = currentPage - 1;
			getJSONData(pageURL, $requestParam, processFunction);
		});
	}

	$nav = $("#" + type + "NavNext")
	$nav.unbind()
	if (currentPage >= totalPages) {
		$nav.attr("disabled", true);
		hideNavControl("d" + type + "NavFirst", "d" + type + "NavPrevious",
				type + 'NavNext', type + 'NavLast', "d" + type + "NavAll")
		showNavControl(type + "NavFirst", type + "NavPrevious", 'd' + type
				+ 'NavNext', 'd' + type + 'NavLast', type + "NavAll")
	} else {
		$nav.attr("disabled", false);

		$nav.click(function() {
			$requestParam.pageNo=currentPage + 1;
			getJSONData(pageURL,$requestParam , processFunction);
		});
	}
	$nav = $("#" + type + "NavLast");
	$nav.unbind()
	if (currentPage >= totalPages) {
		$nav.attr("disabled", true);
		showNavControl(type + "NavFirst", type + "NavPrevious", "d" + type
				+ "NavNext", "d" + type + "NavLast", type + "NavAll");
		hideNavControl("d" + type + "NavFirst", "d" + type + "NavPrevious",
				type + "NavNext", type + "NavLast", "d" + type + "NavAll");
	} else {
		$nav.attr("disabled", false);
		$nav.click(function() {
			 $requestParam.pageNo=totalPages,
			getJSONData(pageURL,$requestParam, processFunction);
		});
	}
	if (currentPage == 1 && currentPage == totalPages) {
		showNavControl("d" + type + "NavFirst", "d" + type + "NavPrevious", "d"
				+ type + "NavNext", "d" + type + "NavLast", "d" + type
				+ "NavAll");
		hideNavControl(type + "NavFirst", type + "NavPrevious", type
				+ "NavNext", type + "NavLast", type + "NavAll");
	}
}

function showNavControl(first, prev, next, last, all) {
	$("#" + first).show();
	$("#" + prev).show();
	$("#" + next).show();
	$("#" + last).show();
	$("#" + all).show();
}
function hideNavControl(first, prev, next, last, all) {
	$("#" + first).hide();
	$("#" + prev).hide();
	$("#" + next).hide();
	$("#" + last).hide();
	$("#" + all).hide();
}

function makeConceptDraggable() {
	$('li.concept').draggable({
		helper : 'clone',
		start : function() {
			dragConceptElement = this;
			dragConceptData = $(this).data("data");
			// alert($conceptData.dispName);
			// if((leftColumn=="Concept1")||(leftColumn=="Concept2")){
			$('span.conceptHolder').each(function() {
				subbranches = $('ul', this.parentNode);
				subbranches.hide();
			});
			// }
		},
		stop : function() {

			// if((leftColumn=="Concept1")||(leftColumn=="Concept2")){
			$('span.conceptHolder').each(function() {
				subbranches = $('ul', this.parentNode);
				subbranches.show();
			});
			// }
		}
	});

	$("li.concept").bind("mouseover", function(e) {
		if (hitFlagColumn) {
			hitFlagColumn = false;
			hitFlagConcept = false;
			rightColumn = this.id;
			dragConceptElement = this;
			dragConceptData = $(this).data("data");
			var mapRowText = leftColumn + seperator + rightColumn;
			obj = checkDuplicates();
			if (!obj) {
				createMap();
			}

		}
	});

	$("li.concept").mousedown(function() {
		// alert("1::"+this.id);
		hitFlagColumn = false;
		rightColumn = this.id;
	});

	$('span.conceptHolder').droppable({
		accept : '.column',
		hoverClass : 'dropOver',
		tollerance : 'pointer',
		over : function(dragged) {

			if (!this.expanded) {
				subbranches = $('ul', this.parentNode);
				if (subbranches.size() > 0) {
					subbranch = subbranches.eq(0);
					this.expanded = true;
					if (subbranch.css('display') == 'none') {
						var targetBranch = subbranch.get(0);
						this.expanderTime = window.setTimeout(function() {
							$(targetBranch).show();
							$('img.expandImage', targetBranch.parentNode).eq(0)
									.attr('src',
											'images/bullet_toggle_minus.png');
							$.recallDroppables();
						}, 500);
					}
				}
			}

		},
		out : function() {

			if (this.expanderTime) {
				window.clearTimeout(this.expanderTime);
				this.expanded = false;
			}
		},
		drop : function(dropped) {
			// alert("right column selected::"+rightColumn);
			hitFlagColumn = true;

		}
	});

	$('#checkAllConcepts').click(function() {
		$("#businessTermDiv li.concept INPUT[type='checkbox']").attr('checked',
				$('#checkAllConcepts').is(':checked'));
		$("#businessTermDiv li.concept INPUT[type='checkbox']")
				.trigger("click");
		$("#businessTermDiv li.concept INPUT[type='checkbox']").attr('checked',
				$('#checkAllConcepts').is(':checked'));
	});
	$('#checkAllInstances').click(function() {
		$("#instancesDiv li.concept INPUT[type='checkbox']").attr('checked',
				$('#checkAllInstances').is(':checked'));
		$("#instancesDiv li.concept INPUT[type='checkbox']").trigger("click");
		$("#instancesDiv li.concept INPUT[type='checkbox']").attr('checked',
				$('#checkAllInstances').is(':checked'));
	});
}

function createMap() {

	// TODO: CHECK FOR hasInstance
	$dragConceptElement = $(dragConceptElement);
	$dragColumnElement = $(dragColumnElement);
	colid = $dragColumnElement.attr("id");
	tblName = colid.split('#')[0];

	if ($current == "concept") {

		newMapping = {
			id : -1,
			aedId : dragColumnData.aedId,
			colDispName : dragColumnData.dispName,
			conDispName : dragConceptData.dispName,
			conId : dragConceptData.id,
			conType : "E",
			bedId : dragConceptData.bedId,
			hasInstance : false,
			mappingType : "N",
			msg : "",
			msgType : "",
			relevance : 100,
			tblDispName : tblName,
			tableDisplayName:tblName
		};
		$data = createMappingDataElement(newMapping);
		$("#mapsContent").show();
		$("#mapsContent").prepend($data);
		$("#enableConfirmMappings").show();
		$("#disableConfirmMappings").hide();
		$("#enableConfirmMappingsLoader").hide();
	}
	if ($current == "instance") {
		$instanceState = false;
		newMapping = {
			id : -1,
			aedId : dragColumnData.aedId,
			memDispName : dragColumnData.dispName,
			conDispName : dragConceptData.dispName,
			conId : dragConceptData.id,
			conType : "E",
			bedId : dragConceptData.bedId,
			hasInstance : false,
			mappingType : "N",
			msg : "",
			msgType : "",
			relevance : 100,
			instanceDispName : dragConceptData.dispName,
			instanceType : dragConceptData.instanceType,
			tblDispName : tblName
		};
		$data = createInstanceMappingDataElement(newMapping);
		$("#instanceMapsContent").show();
		$("#instanceMapsContent").prepend($data);
		$("#enableInstanceConfirmMappings").show();
		$("#disableInstanceConfirmMappings").hide();
		$("#enableConfirmMappingsLoader").hide();
	}
}

function checkDuplicates() {
	aId = dragColumnData.aedId;
	dId = dragConceptData.bedId;
	state = false;
	if ($current == "concept") {
		$.each($("#mapsSelected input[type='checkbox']"), function(i, v) {
			if (($(this).data("data").aedId == aId)
					&& ($(this).data("data").bedId == dId)) {
				alert("Selected mapping already exists. Cannot be mapped again");
				state = true;
			}
		});
	}
	if ($current == "instance") {
		$.each($("#instanceMapsSelected input[type='checkbox']"),
				function(i, v) {
					if (($(this).data("data").aedId == aId)) { // &&($(this).data("data").bedId==dId)
						alert("Selected mapping already exists. Cannot be mapped again");
						state = true;
					}
				});
	}
	return state;
}
function checkDuplicateName(strName,id) {
	state = false;
	if ($current == "concept") {
		
		$.each($("#mapsSelected input[type='checkbox']"), function(i, v) {
			//alert(id +" "+ $(this).data("data").conId);
			if (($(this).data("data").conDispName.toLowerCase() == strName
					.toLowerCase()) && $(this).data("data").conId !=id) {
				state = true;
			}
		});
		$.each($("#businessTermDiv input[type='checkbox']"), function(j, u) {
			//alert("business div "+id +" "+ $(this).parent().data("data").id);
			if ($(this).parent().data("data").dispName.toLowerCase() == strName
					.toLowerCase() && $(this).parent().data("data").id != id) {
				state = true;
			}
		});
	}
	if ($current == "instance") {
		$.each($("#instanceMapsSelected input[type='checkbox']"),
				function(i, v) {
					if (($(this).data("data").instanceDispName.toLowerCase() == strName
							.toLowerCase())) {
						state = true;
					}
				});
		$.each($("#instancesDiv input[type='checkbox']"), function(i, v) {
			if (($(this).parent().data("data").dispName.toLowerCase() == strName
					.toLowerCase())) {
				state = true;
			}
		});
	}

	return state;
}
function addConcepts(id, addLink) {
	Pane_Left = $("#suggestConcepts").position().left;
	Pane_Top = $("#suggestConcepts").position().top;
	// maxRecords=2;
	addLink.hide();
	$mode = 'add';
	$("#loadingAddConcepts" + id + "Link").show();
	$.get(newConceptURL, {
		mode : 'add'
	}, function(data) {
		$("#loadingAddConcepts" + id + "Link").hide();
		$("#hiddenPaneContent").empty();
		$("#hiddenPaneContent").append(data);

		$("#hiddenPane").css("left", Pane_Left - 45);
		$("#hiddenPane").css("top", Pane_Top + 23);
		$("#hiddenPane").fadeIn("slow");

		$("#hiddenPaneContent").css("height", "auto");
		$("#hiddenPaneContent").css("width", 270 + "px");
		$("#conceptForm table").css("width", "100%");
		addLink.show();
		$("#closeInnerPaneLink").hide();
	});
}
$("#resetConceptId").bind('click', resetConcept);
function resetConcept() {
	getConcept(conceptdata.conId)
}
function getConcept(conceptID) {
	if ($mode == 'add') {
		document.conceptForm.reset();
		$("#dataFormats").empty();
		$("#units").empty();
	} else {
		$.get("swi/showConcept.action?concept.id=" + conceptID, {}, function(
				data) {
			$("#hiddenPaneContent").empty();
			$("#hiddenPaneContent").append(data);
			$("#hiddenPane").css("left", Pane_Left - 45);
			$("#hiddenPane").css("top", Pane_Top + 23);
			$("#hiddenPane").fadeIn("slow");

			$("#hiddenPaneContent").css("height", "auto");
			$("#hiddenPaneContent").css("width", 270 + "px");
			$("#conceptForm table").css("width", "100%");

			$("a#showInstance").hide();
			$ftype = $("#displayTypeList :selected").text();
			if ($ftype != null) {
				changeFormat();
			}
			// editLink.show();
			$("#closeInnerPaneLink").hide();
			$("#backButton").hide();
		});
	}
}
function editConcepts(id, editLink) {

	Pane_Left = $("#suggestConcepts").position().left;
	Pane_Top = $("#suggestConcepts").position().top;
	// maxRecords=2;
	//editLink.hide();
	$("#loadingEditConcepts" + id + "Link").show();

	data = "concept.id=" + id;
	$.get(editConceptURL, data, function(data) {
		$("#loadingEditConcepts" + id + "Link").hide();
		$("#hiddenPaneContent").empty();
		$("#hiddenPaneContent").append(data);
		$("#hiddenPane").css("left", Pane_Left - 45);
		$("#hiddenPane").css("top", Pane_Top + 23);
		$("#hiddenPane").fadeIn("slow");

		$("#hiddenPaneContent").css("height", "auto");
		$("#hiddenPaneContent").css("width", 270 + "px");
		$("#conceptForm table").css("width", "100%");
		$("#cname").css("width", "150px");
		$("a#showInstance").hide();
		$ftype = $("#displayTypeList :selected").text();
		if ($ftype != null) {
			changeFormat();
		}
		editLink.show();
		editLink.html('Edit');
		$("#closeInnerPaneLink").hide();
		$("#backButton").hide();
	});
}
function addInstance(id, addLink) {

	Pane_Left = $("#suggestInstances").position().left;
	Pane_Top = $("#suggestInstances").position().top;
	// maxRecords=2;
	addLink.hide();
	$("#loadingAddInstances" + id + "Link").show();

	$.get(newInstanceURL, {
		"concept.id" : currentConceptMappingForInstances.conId,
		mode : 'add'
	}, function(data) {
		$("#loadingAddInstances" + id + "Link").hide();
		$("#hiddenPaneContent").empty();
		$("#hiddenPaneContent").append(data);

		$("#hiddenPane").css("left", Pane_Left - 45);
		$("#hiddenPane").css("top", Pane_Top + 23);
		$("#hiddenPane").fadeIn("slow");

		$("#hiddenPaneContent").css("height", 200 + "px");
		$("#hiddenPaneContent").css("width", 270 + "px");
		$("#conceptForm table").css("width", "100%");
		addLink.show();
		$("#closeInnerPaneLink").hide();
		$("#backButtonLink").hide();
	});
}

function editInstance(id, editLink) {
	Pane_Left = $("#suggestInstances").position().left;
	Pane_Top = $("#suggestInstances").position().top;
	// maxRecords=2;
	//editLink.hide();
	$("#loadingEditInstances" + id + "Link").show();
	data = "instance.id=" + id;
	$.get(editInstanceURL, {
		"instance.id" : id,
		"concept.id" : currentConceptMappingForInstances.conId
	}, function(data) {
		$("#loadingEditInstances" + id + "Link").hide();
		$("#hiddenPaneContent").empty();
		$("#hiddenPaneContent").append(data);
		$("#hiddenPane").css("left", Pane_Left - 45);
		$("#hiddenPane").css("top", Pane_Top + 23);
		$("#hiddenPane").fadeIn("slow");
		$("#cname").css("width", "150px");
		$("#hiddenPaneContent").css("height", 200 + "px");
		$("#hiddenPaneContent").css("width", 270 + "px");
		$("#conceptForm table").css("width", "100%");

		editLink.show();
		$("#closeInnerPaneLink").hide();
		$("#backButton").hide();
		editLink.html("Edit");
	});
}
// save concept
function createConcept() {
	var conceptData = $("#conceptForm").serialize();
	var conceptUpdateId= $("#conceptUpdateId").val();	
	if ($("#concept").val() == '') {
		alert("Enter concept name");
		return false;
	} else if (checkSpecialChar($("#concept"))) {
		alert("Concept Name can not contain special characters");
		return false;
	} else {
		consName = checkDuplicateName($("#concept").val(),conceptUpdateId);
		if (!consName) {
			getJSONData(saveConceptURL, conceptData, processConceptSaveJsonData);
			$("#hiddenPane").fadeOut();
			$(".addConceptLink").fadeOut();
			$(".editConceptLink").fadeOut();

		} else {
			alert("Concept Already Exists");
			return false;
		}
	}
}
function processConceptSaveJsonData(jsonData) {
	if (jsonData != null) {
		if (!$currentLink) {
			if (jsonData.dispName != null) { 
				currentMapping.conId = jsonData.id;
				currentMapping.bedId = jsonData.bedId;
				currentMapping.conType = jsonData.type;
				currentMapping.conDispName = jsonData.dispName;

				$("#conDispName" + currentMapping.aedId)
						.html(currentMapping.conDispName);
				$len = $tempBTData.length;
				
				if($addConceptFlag){
				$tempConcept = {};
				$tempConcept.bedId = jsonData.bedId;
				$tempConcept.conType = jsonData.type;
				$tempConcept.dispName = jsonData.dispName;
				$tempConcept.id = jsonData.id;
				$tempBTData[$len] = $tempConcept;
				$dispNames[$dispNames.length] = jsonData.dispName; 
				$addConceptFlag=false;
				}
				
				$businessTermDiv.empty();
				$businessTermMain = $('<span></span>');
				$businessTermDiv.append($businessTermMain);
				sortAndDisplay($businessTermMain);
				$previousClick.length = 0;
				$clickCount = 0;
				$conceptClicked = false;
				$editFlag = true;
				makeConceptDraggable();
			}
		} else {  
			$len = $tempBTData.length;
			$tempConcept = {};
			$tempConcept.bedId = jsonData.bedId;
			$tempConcept.conType = jsonData.type;
			$tempConcept.dispName = jsonData.dispName;
			$tempBTData[$len] = $tempConcept;
			$dispNames[$dispNames.length] = jsonData.dispName;
			$businessTermDiv.empty();
			$businessTermMain = $('<span></span>');
			$businessTermDiv.append($businessTermMain);
			sortAndDisplay($businessTermMain);
			$currentLink = false;
			makeConceptDraggable();
		}
	}
}
// save Instance
function createUpdateInstance() {
	var instanceData = $("#instanceForm").serialize();
	if ($("#iname").val() == '') {
		alert("Enter Instance name");
		return false;
	} else if ($instanceInput.val() == $("#iname").val()) {
		alert("Instance Name already Exist");
		return false;
	} else {
		newInsName = checkDuplicateName($("#iname").val(),'');
		if (!newInsName) {
			getJSONData(saveInstanceURL, instanceData,
					processInstanceSaveJsonData);
			$("#hiddenPane").fadeOut();
			$(".addConceptLink").fadeOut();
			$(".editConceptLink").fadeOut();
		} else {
			alert("Instance Name already Exist");
			return false;
		}
	}
}
function processInstanceSaveJsonData(jsonData) {
	if (jsonData != null) {
		if (!$currentLink) {
			if (jsonData.dispName != null) {
				currentInstanceMapping.conId = jsonData.id;
				currentInstanceMapping.bedId = jsonData.bedId;
				currentInstanceMapping.instanceType = jsonData.type;
				currentInstanceMapping.instanceDispName = jsonData.dispName;
				$("#instanceDispName" + currentInstanceMapping.aedId)
						.html(currentInstanceMapping.instanceDispName);
				$len = $tempInstanceData.length;
				$tempInstance = {};
				$tempInstance.bedId = jsonData.bedId;
				$tempInstance.conId = jsonData.id;
				$tempInstance.dispName = jsonData.dispName;
				$tempInstanceData[$len] = $tempInstance;
				$dispInstanceNames[$dispInstanceNames.length] = jsonData.dispName;
				$instancesDiv.empty();
				$instancesMain = $('<span></span>');
				$instancesDiv.append($instancesMain);
				sortAndDisplay($instancesMain);
				makeConceptDraggable();
			}
		} else {
			$len = $tempInstanceData.length;
			$tempInstance = {};
			$tempInstance.bedId = jsonData.bedId;
			$tempInstance.conId = jsonData.id;
			$tempInstance.dispName = jsonData.dispName;
			$tempInstance.instanceType = jsonData.type;
			$tempInstanceData[$len] = $tempInstance;
			$dispInstanceNames[$dispInstanceNames.length] = jsonData.dispName;
			$instancesDiv.empty();
			$instancesMain = $('<span></span>');
			$instancesDiv.append($instancesMain);
			sortAndDisplay($instancesMain);
			$currentLink = false;
			makeConceptDraggable();
		}
	}
}
function showHidePanes() {
	if ($current == "instance") {
		$("#assetTablesPane").hide();
		$("#businessTermsPane").hide();
		$("#columnMembersPane").show();
		$("#instancesPane").show();
	}
	if ($current == "concept") {
		$("#assetTablesPane").show();
		$("#businessTermsPane").show();
		$("#columnMembersPane").hide();
		$("#instancesPane").hide();
	}
}
function checkConceptsState() {
	cState = false;
	orgLength = $conceptsState.length;
	presentLength = 0;
	presentLength = $("#mapsSelected input:checked").length;
	if (orgLength == presentLength) {
		cState = true;
	}
	cState = checkConceptsValues();

	return cState;
}
function checkConceptsValues() {
	state = true;
	$.each($("#mapsSelected input:checked"), function(i, v) {
		// alert($(this).data("data").conDispName);
		if ($(this).data("data").conDispName != $conceptsState[i]) {

			state = false;
		}

	});
	return state;
}
function addConcept() { $addConceptFlag=true;
	Pane_Left = $("#suggestConcepts").position().left;
	Pane_Top = $("#suggestConcepts").position().top;
	$.get(newConceptURL, {
		mode : 'add'
	}, function(data) {
		$("#hiddenPaneContent").empty();
		$("#hiddenPaneContent").append(data);

		$("#hiddenPane").css("left", Pane_Left - 45);
		$("#hiddenPane").css("top", Pane_Top + 23);
		$("#hiddenPane").fadeIn("slow");

		$("#hiddenPaneContent").css("height", "auto");
		$("#hiddenPaneContent").css("width", 270 + "px");
		$("#conceptForm table").css("width", "100%");
		$("#closeInnerPaneLink").hide();
		$("#concept").select().focus();
	});
}
function addInstance(id,addLink) {
	Pane_Left = $("#suggestInstances").position().left;
	Pane_Top = $("#suggestInstances").position().top;
	$.get(newInstanceURL, {
		"concept.id" : currentConceptMappingForInstances.conId,
		mode : 'add'
	}, function(data) {
		$("#hiddenPaneContent").empty();
		$("#hiddenPaneContent").append(data);

		$("#hiddenPane").css("left", Pane_Left - 45);
		$("#hiddenPane").css("top", Pane_Top + 23);
		$("#hiddenPane").fadeIn("slow");
		$("#cname").css("width", "150px");
		$("#hiddenPaneContent").css("height", 200 + "px");
		$("#hiddenPaneContent").css("width", 270 + "px");
		$("#conceptForm table").css("width", "100%");
		$("#closeInnerPaneLink").hide();
		$("#backButtonLink").hide();
		$("#iname").select().focus();
				addLink.html('New Instance');
	});

}
function newConcept() {
	$currentLink = 'true';
	addConcept();
}
function newInstance() {
	$currentLink = 'true';
	addInstance();
}
function mappingFilter() {
	$mappingFilter = $("#" + $current + "Filter :selected").text();
	if ($mappingFilter == 'confirmed') {
		$mappingFilter = 'E';
	} else if ($mappingFilter == 'un-confirmed') {
		$mappingFilter = 'S';
	} else {
		$mappingFilter = 'A';
	}
	if ($current == "concept") {
		// alert("$current----"+$current);
		// alert("mappingFilter---"+mappingFilter);
		getJSONData(displayConceptMappingsURL, {
			mappingFilter : $mappingFilter
		}, processConceptMappingJsonData);
	} else {
		// alert("$current----"+$current);
		// alert("mappingFilter---"+$mappingFilter);

		selColAedId = currentConceptMappingForInstances.aedId;
		getJSONData(displayInstanceMappingsURL, {
			mappingFilter : $mappingFilter,
			selColAedId : selColAedId
		}, processInstanceMappingJsonData);
	}
}
