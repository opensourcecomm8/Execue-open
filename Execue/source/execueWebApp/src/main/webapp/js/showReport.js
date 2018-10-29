(function() {

	$.fn.showExecueReport = function(o) {
		var defaults = {
			reportStatus : 'processing',
			source : 'Source Name',
			relevance : '100',
			sqlQuery : 'Query',
			psudoEnglish : 'Eglish Query',
			header : 'Default Header',
			statusCheckURL : 'reportStatus!checkStatus.action',
			reportTypes : ['GRID', 'CHART'],
			queryName : '',
			description : '',
			reportTitle : '',
			name : '',
			agQid : [1, 2],
			aid : 1,
			qid : 1,
			msgId : 1,
			bqId : 1,
			type : 1,
			imgPath : '',
			appId : -1,
			vertical : false
		};
		var $me;
		var iterationcount = 0;

		var options = $.extend(defaults, o);
		var $img = "<img src='images/loaderTrans.gif'>";
		if (options.imgPath != '') {
			$img = "<img src='../images/loaderTrans.gif'>";
		}
		return this.each(function() {
			$me = $(this);
			if (options.reportStatus != 'COMPLETED') {
				// make ajax calls
				// alert('options.aid:::'+options.aid);
				displayProcessing();
				checkStatusAndData();
			}
		});

		function checkStatusAndData() {
			// alert('iterationcount:'+iterationcount);
			if (iterationcount == 10) {
				$("#asset_" + options.aid + "_" + options.bqId)
						.val("COMPLETED");
				dispalyCancelMessage();
				return;
			}
			displayProcessing();
			if (options.reportStatus != 'COMPLETED') {
				// ajax call
				$.getJSON(options.statusCheckURL, {
					queryId : options.qid,
					messageId : options.msgId,
					assetId : options.aid,
					businessQueryId : options.bqId,
					ts : new Date().getTime()
				}, function(data) {
					receiveStatusData(data);
				});

			}
			iterationcount++;
		}

		function receiveStatusData(data) {
			if (data.reportStatus == 'PROCESSING') {
				setTimeout(checkStatusAndData, 3500);
				return;
			} else if (data.reportStatus == 'ERROR') {
				dispalyErrorMessage();
				$("#asset_" + options.aid + "_" + options.bqId)
						.val("COMPLETED");
			} else {
				// competed dispaly data
				$("#asset_" + options.aid + "_" + options.bqId)
						.val("COMPLETED");
				options.reportStatus = data.reportStatus;
				options.reportHeader = data.reportHeader;
				options.reportList = data.reportGroupList;
				options.sqlQuery = data.query;
				options.pseudoStatement = data.pseudoStatement;
				options.dataPresent = data.dataPresent;

				/*
				 * if (options.dataPresent != true) { // alert("data not
				 * present"); options.reportStatus = 'PROCESSING';
				 * setTimeout(checkStatusAndData, 3500); return; }
				 */// this recursive block is no more needded
				options.agQid = data.aggregateQueryId;
				// options.relevance = data.relevance;
				options.error = data.error;
				// options.description = data.description;
				// options.name = data.name;
				display();
			}
		}

		function displayPseudoStatement() {
			var baseid = $me.attr('id');
			id = '#' + baseid + "_pseudoStatement";
			var pseudoStatement = options.pseudoStatement;
			// pseudoId = '#' + baseid + "_pseudoStmt";
			// $(pseudoId).hide();
			$(id).empty();
			$(id).append(pseudoStatement);
			$("#reportId_" + options.aid + "_" + options.bqId + "_psStmt")
					.val(pseudoStatement);
		}
		/*
		 * function displaySource() { var baseid = $me.attr('id'); id = '#' +
		 * baseid + "_source"; var source = "<span class='sourceText'>Source:<b>"+options.description+"
		 * </b> ["+options.name+"]<b>"+options.relevance+"%</b> match
		 * </span>"; $(id).append(source); }
		 */
		function displayQuery() {
			var baseid = $me.attr('id');
			id = '#' + baseid + "_query";
			var sqlQuery = options.sqlQuery;
			$(id).append(unescape(sqlQuery));
			$("#reportId_" + options.aid + "_" + options.bqId + "_query")
					.val(sqlQuery);
			if (sqlQuery != null) {
				$("#a_" + options.aid).show();
			}
		}

		function dispalyCancelMessage() {
			var baseid = $me.attr('id');
			id = '#' + baseid + "_header";
			// $(id).text('CANCELLED');
			$(id)
					.text('The data source is taking too long to respond [CANCELLED]');
			$("#reportId_" + options.aid + "_" + options.bqId)
					.val('The data source is taking too long to respond [CANCELLED]');
		}
		function dispalyErrorMessage() {
			var baseid = $me.attr('id');
			id = '#' + baseid + "_header";
			// $(id).text('ERROR');
			$(id).html(options.reportTitle + ' [ERROR]');
			$("#reportId_" + options.aid + "_" + options.bqId)
					.val(options.reportTitle + ' [ERROR]');
		}

		function displayProcessing() {
			var baseid = $me.attr('id');
			id = '#' + baseid + "_header";
			// $(id).html('PROCESSING...' + $img);
			$(id).html(options.reportTitle + ' [Processing...' + $img + ']');
		}
		/*
		 * function displayError() { var baseid = $me.attr('id'); id = '#' +
		 * baseid + "_error"; $(id).text(options.error); }
		 */

		function display() {
			displayHeader();
			displayPseudoStatement();
			// displaySource();
			displayQuery();
		}

		function displayHeader() {
			var baseid = $me.attr('id');
			id = '#' + baseid + "_header";
			// $(id).text(options.reportHeader);
			var reportList = options.reportList;
			// if (reportList.length > 0) {
			$(id).empty();
			// }
			var reportHeader = "";
			if (options.dataPresent == true) {
				var request = $("#requestedStringId").val();
				var srcType = $("#typeId").val();
				var applicationId = $("#applicationId").val();
				if (!applicationId) {
					applicationId = -1;
				}
				var verticalId = $("#verticalId").val();
				if (!verticalId) {
					verticalId = -1;
				}
				var imageResult = '';
				var chartsExist = false;
				var gridsExist = false;
				var detailReportExist = false;
				$.each(reportList, function(i, reportGroupList) {
					if (reportGroupList.groupType == 2)
						chartsExist = true;
					if (reportGroupList.groupType == 1)
						gridsExist = true;
					if (reportGroupList.reportTypes == 20) {
						detailReportExist = true;
					}
				});
				if (!chartsExist && !gridsExist) {
					reportHeader = options.reportHeader;
				}

				/*
				 * reportHeader = "<a href='reportView.action?queryId=" +
				 * options.qid + "&assetId=" + options.aid + "&businessQueryId=" +
				 * options.bqId + "&type=" + srcType + "&title=" +
				 * options.queryName + "&source=" + options.source +
				 * "&requestedString=" + request + "&hasDetailReport=" +
				 * detailReportExist + "'>" + options.reportHeader + "</a>";
				 */

				if (options.vertical) {

					reportHeader = '<a class="reportLink" href="javascript:showReports(\'reportView.action?agQueryIdList='
							+ options.agQid
							+ '&type='
							+ srcType
							+ "&verticalId="
							+ verticalId
							+ "&applicationId="
							+ applicationId
							+ '&title='
							+ options.queryName
							+ '&source='
							+ options.source
							+ '&requestedString='
							+ request
							+ '\',\''
							+ options.vertical
							+ '\');">'
							+ options.reportHeader + '</a>';

				} else {
					// go by normal route.
					// window.location = url;

					// options.relatedUserQueryIds =
					// options.relatedUserQueryIds.substring(0,
					// options.relatedUserQueryIds.length-1);

					reportHeader = '<a class="reportLink" href="reportView.action?agQueryIdList='
							+ options.agQid
							+ '&type='
							+ srcType
							+ "&verticalId="
							+ verticalId
							+ "&applicationId="
							+ applicationId
							+ '&title='
							+ options.queryName
							+ '&source='
							+ options.source
							+ '&requestedString='
							+ request + '">' + options.reportHeader + '</a>';
				}

				var windowUrl = window.location;
				var aggregateIds = options.agQid;
				imageResultForShareThis = '<a class="sharing-button" addthis:title="'
						+ options.reportHeader
						+ '" '
						/*
						 * +
						 * 'addthis:url="'+windowUrl+'?userQueryId='+options.qid+'&assetId='+options.aid+'&businessQueryId='+options.bqId+'">'
						 */
						+ 'addthis:url="'
						+ windowUrl
						+ '?agQueryIdList='
						+ options.agQid
						+ '">'
						+ '<img src="'
						+ options.imgPath
						+ 'images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a>';
				var csvExist = false;
				var pivotExist = false;
				$.each(reportList, function(i, reportGroupList) {
					var actionURL = '<a class="reportLink" href="javascript:showReports(\''
							+ reportGroupList.linkUrl + '?agQueryIdList=';
					/*
					 * var params = options.qid + "&assetId=" + options.aid +
					 * "&businessQueryId=" + options.bqId + "&title=" +
					 * options.queryName + "&source=" + options.source +
					 * "&type=" + srcType + "&requestedString=" + request + "'><img
					 * hspace=4 width=18 height=18 border=0 src=" +
					 * reportGroupList.imageUrl + "></a>";
					 */
					var params = options.agQid
							+ "&type="
							+ srcType
							+ "&verticalId="
							+ verticalId
							+ "&applicationId="
							+ applicationId
							+ "&title="
							+ options.queryName
							+ "&source="
							+ options.source
							+ "&requestedString="
							+ request
							+ "','"
							+ options.vertical
							+ "');\"><img  hspace=4 width=18 height=18 border=0 src="
							+ options.imgPath + reportGroupList.imageUrl
							+ "></a>";
					if (reportGroupList.reportTypes == 98) {
						// skip this type
					} else if (reportGroupList.reportTypes == 99) {
						if (csvExist) {
							// detail report type csv action.
							imageResult = imageResult + actionURL
									+ aggregateIds[1];
						} else {
							// business summary type csv action.
							imageResult = imageResult + actionURL
									+ aggregateIds[0];
						}
						imageResult = imageResult
								+ "&type="
								+ srcType
								+ "&verticalId="
								+ verticalId
								+ "&applicationId="
								+ applicationId
								+ "&title="
								+ options.queryName
								+ "&source="
								+ options.source
								+ "&requestedString="
								+ request
								+ "','"
								+ options.vertical
								+ "');\"><img  hspace=4 width=18 height=18 border=0 src="
								+ options.imgPath + reportGroupList.imageUrl
								+ "></a>";
						csvExist = true;
						if (reportList.length == 1) {
							// only csv is present hence reverting the a text
							// link with the image link.
							reportHeader = actionURL + aggregateIds[0]
									+ "&type=" + srcType + "&verticalId="
									+ verticalId + "&applicationId="
									+ applicationId + "&title="
									+ options.queryName + "&source="
									+ options.source + "&requestedString="
									+ request + "','" + options.vertical
									+ "'); \">" + options.reportHeader + "</a>";
						}
					} else if (reportGroupList.reportTypes == 4) {

						if (pivotExist) {
							// detail report type csv action.
							imageResult = imageResult + actionURL
									+ aggregateIds[1];
						} else {
							// business summary type csv action.
							imageResult = imageResult + actionURL
									+ aggregateIds[0];
						}
						imageResult = imageResult
								+ "&type="
								+ srcType
								+ "&verticalId="
								+ verticalId
								+ "&applicationId="
								+ applicationId
								+ "&title="
								+ options.queryName
								+ "&source="
								+ options.source
								+ "&requestedString="
								+ request
								+ "','"
								+ options.vertical
								+ "');\"><img  hspace=4 width=18 height=18 border=0 src="
								+ options.imgPath + reportGroupList.imageUrl
								+ "></a>";
						pivotExist = true;
					} else if (reportGroupList.reportTypes[0] == 22) {
						var actionURL = '<a class="reportLink" href="javascript:showReports(\''
								+ reportGroupList.linkUrl + '?agQueryIdList=';
						imageResult = imageResult + actionURL + aggregateIds;
						imageResult = imageResult
								+ "&type="
								+ srcType
								+ "&verticalId="
								+ verticalId
								+ "&applicationId="
								+ applicationId
								+ "&aggQueryAppId="
								+ options.appId
								+ "&title="
								+ options.queryName
								+ "&source="
								+ options.source
								+ "&requestedString="
								+ request
								+ "','"
								+ options.vertical
								+ "');\"><img  hspace=4 width=18 height=18 border=0 src="
								+ options.imgPath + reportGroupList.imageUrl
								+ "></a>";
					} else {
						if (chartsExist && reportGroupList.groupType == 1) {
							// both charts and grids are present - display the
							// combo icon (grid & chart)
							// This means we are skipping the printing of grid
							// icons here.
						} else {
							imageResult = imageResult + actionURL + params;
						}
					}
				});
				imageResult = imageResult + imageResultForShareThis;
				$(id).append(reportHeader + imageResult);
				$("#reportId_" + options.aid + "_" + options.bqId)
						.val(reportHeader + imageResult);
				addthis.button('.sharing-button');
				var left = (screen.width) - (screen.width / 2) - 20;
				var top = (screen.height) - (screen.height / 2) - 180;
				$("a.reportLink").click(function() {
					tempImg = $("#waiting_img2").attr("src");
					hrefValue = $(this).attr('href');
					// represents not found condition for csv type action
					// as the page2 naviagation is not needed for such types.
					if (hrefValue.indexOf("showCSV.action") == -1) {
						$("#pageLoader").show().css("left", left + "px");
						$("#pageLoader").css("top", top + "px");
						setTimeout(
								'document.images["waiting_img2"].src = tempImg',
								1);
					}
				});
			} else {
				$(id).append("Data is not available");
				$("#reportId_" + options.aid + "_" + options.bqId)
						.val("Data is not available");
			}

		}
	}
})(jQuery);

function showReports(url, verticalFlag) {
	if (verticalFlag == "true") {
		$("#transparentPane").show();
		$.get(url, function(data, status) {
			if (status == "success") {
				var left = (screen.width) - (screen.width / 2)
						- ($("#hiddenPane").width() / 2) - 50;
				var top = (screen.height) - (screen.height / 2)
						- ($("#hiddenPane").height() / 2) - 150;
				if (top < 0) {
					top = 0;
				}
				if (left < 0) {
					left = 0;
				}
				$("#verticalResultDiv").empty();
				$("#verticalResultDiv").html(data);
				$("#verticalResultDiv").show();

				$("#hiddenPane").css("left", left + "px");
				$("#hiddenPane").css("top", top + "px");
				$("#hiddenPane").show();
				$("#verticalResultCloseDiv").show();
				$("#pageLoader").hide();
				$("#closeReportsDiv").bind("click", function() {
					$("#verticalResultDiv").hide();
					$("#hiddenPane").hide();
					$("#transparentPane").hide();
					$("#verticalResultCloseDiv").hide();
				});
			}
		});
	} else {
		// go by normal route.
		window.location = url;
	}
}
