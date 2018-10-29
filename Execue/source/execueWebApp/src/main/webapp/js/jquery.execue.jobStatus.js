(function() {
	/*
	 * Status values
	 * 1 - PENDING
	 * 2 - INPROGRESS
	 * 3 - SUCCESS
	 * 4 - FAILURE
	 */
	$.fn.jobStatus = function(o) {
		var defaults = {
			status : 'PENDING',
			preCall : preProcess,
			postCall : postProcess,
			postDataForJobParam : emptyParam,
			jobId : -1,
			requestJobURL : "requestJob.json",
			statusCheckURL : "../swi/currentJobStatus.action",
			statusDetailsURL : "../swi/refreshJobStatusDetails.action" // could be a html response .
		};

		function preProcess() {
			//alert('pre processing');
		};

		function postProcess() {
			//alert('post processing');
		};
		
		function emptyParam() {
			return {"id": "1234"};
		};
		
		
		
		var options = $.extend(defaults, o);

		var $me;
		var $meLeft=1;
		var $meTop=1;
		var	$mePrevWidth=30;
		
		var iterationcount = 0;
		var iterationTimer = 5000;
		var $left=(screen.width-(screen.width/2))-400;
		var $top=(screen.height-(screen.height/2))-300;
		
		$requestStatusHolder=$('<div></div>');
		$requestDetailsHolder=$('<div style="position:absolute;z-index:1000001;border:1px solid #666;background-color:#C9D0D6;width:450px;min-height:140px;height:auto;"></div>').css({left:$left+"px",top:$top+"px"});
		
		$requestStatus=$('<div style="border:none;float:left;width:40px;padding-top:0px;padding-left:2px;"></div>');	
		
		$message=$(' <div style="min-width: 60px; width: auto; float: left; white-space: nowrap;padding-top:0px;"></div>');
		
		$loader=$('<div style="width: 20px; float: left; padding-left: 10px;"><img height="20" width="20" src="../images/loading.gif"/></div>');
		
		$detailsLinkDiv=$('<div style="padding-left: 2px; float: left; width: 40px;padding-top:0px;" ></div>');
		
		
		
		//$closeLinkDiv=$('<div style="padding-left: 5px; float: left; width: 20px;padding-top:5px;" ></div>');
		//$closeLink=$('<a href="#"><img src="../images/deleteButton1.jpg" border="0" /></a>').click(function(){$(this).parent().parent().parent().hide();});
		//$closeLinkDiv.append($closeLink);
		
		
		return this.each(function() {
				$me = $(this);
				options.preCall();
				$me.show();
				$me.append($requestStatusHolder);
				$detailsLink=$('<a href="#">Details</a>').click(showJobDetails);
				processRequestStatusHolder();
		       $detailsLinkDiv.append($detailsLink);
				// make ajax call to post the initial data
				if (options.jobId == -1) {
					postDataForJob();
				}else if (options.status != 'SUCCESS' || options.status != 'FAILURE') {
					displayJobStatus();
					checkStatusAndData();
				}
			});
		function processRequestStatusHolder(){
			$requestStatusHolder.append($requestStatus).append($message).append($loader).append($detailsLinkDiv);
			//alert($me.prev().position().left);
			//alert($me.prev().position().top);
			if($me.prev()){
				$mePrevWidth=$me.prev().width();
					if($me.prev().position()){
						$meLeft=$me.prev().position().left-1;
						$meTop=$me.prev().position().top-2;
					}
				$me.css({left:$meLeft+"px"});
				$me.css({top:$meTop+"px"});
			}
		}
		
		
		function showJobDetails(){
		//alert("job details");
			// TODO: Change to execue ajax method call ?
			$.ajax({
				url: options.statusDetailsURL,
				data: "jobRequestId="+options.jobId,
				success: function (data) {
					//alert(data);
					$('body').prepend($requestDetailsHolder);
					$requestDetailsHolder.empty().show().append(data);
					}
			});
		}
		
		function postDataForJob() {
			// ajax call to post data
			data = options.postDataForJobParam();
			data.ts = new Date().getTime();
			$.ajax({url:options.requestJobURL, dataType:"json",data:data, success:function(data) {
				if (data.status && (data.status == 'PENDING' || data.status == 'INPROGRESS')) {
					options.jobId = data.jobId;
					options.status = data.status;
					checkStatusAndData(data);
				} else {
					// TODO: organize the code, Show error message
					$me.empty().show().html("unable to process " + data.errMsg);
				}
			},error:errorPostDataForJob});

		}
		function errorPostDataForJob(){
			data = options.postDataForJobParam();
			$me.empty().show().html("unable to process your request at this time, please try again later.");
			$me.css({left:$meLeft+$mePrevWidth+"px"});
			//$("#showStatus"+data.publishedFileInfoId).show();
			$me.prev().show();
			options.postCall(options.jobId,'FAILURE');
		}
		function checkStatusAndData() {
			if (iterationcount > 0 &&((iterationcount % 50) == 0)) {
				iterationTimer = iterationTimer * 2;
				//dispalyCancelMessage();
				//return;
			}
			displayJobStatus();
			if (options.status != 'SUCCESS' || options.status != 'FAILURE') {
				// ajax call
				$.getJSON(options.statusCheckURL, {
					jobRequestId : options.jobId,
					ts : new Date().getTime()
				}, function(data) {
					receiveStatusData(data);
				});

			}
			iterationcount++;
		}
		;

		function displayJobStatus() {
			//if data is null show just processing else show also current status
			
			if (options.currMsg) {
				$message.empty().append(options.currMsg.substring(0,100)+" ...").attr("title",options.currMsg);
			} else if (options.jobId != -1) {
			  $message.empty().append("Requesting ...");
			} else {
				$message.empty().append("Submiting Job ...");
			}
			//link to detail page.
		}
		;
		function dispalyErrorMessage(data){
			$me.empty().show().html(data.status);
		}
		function dispalyCancelMessage(){
			$me.empty().show().html("Cancelled");
		}
		function receiveStatusData(data) {
			options.currentMessage= data.currentMessage;
			options.status= data.status;
			options.currMsg= data.currMsg;
			//alert(data.status);
			if (data.status == 'PENDING' || data.status == 'INPROGRESS') {
				setTimeout(checkStatusAndData, iterationTimer);
				return;
			} else if (data.status == 'FAILURE') {
				dispalyErrorMessage(data);
				options.postCall(options.jobId,'FAILURE');
			} else {
				//hide the div
				//TODO: HOW TO PASS STATUS SUCCESS/FAIL TO CALL BACK FUNCTION
				//alert("SUCCESS");
				$me.hide();
				options.postCall(options.jobId,'SUCCESS');
			}
		}
		;

	};
})(jQuery);// $ is the main jQuery object
