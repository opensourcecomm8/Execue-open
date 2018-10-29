(function() {
	$.fn.showUnstructuredReport = function(o) {
		var defaults = {
			reportStatus : 'processing',
			queryName : '',
			statusCheckURL : 'uss/unstructuredResemantificationStatus!checkStatus.action',
			msgId : 1			
		};
		var $me;
		var iterationcount = 0;
		
		var options = $.extend(defaults, o);
		var $img = "<img src='images/main/loaderTrans.gif'>";
		if (options.imgPath != '') {
			$img = "<img src='../images/main/loaderTrans.gif'>";
		}
		return this.each(function() {
			$me = $(this);
			//alert('status: '+options.reportStatus);
			if (options.reportStatus != 'COMPLETED') {
				reSemantificationProcess=true;
				displayProcessing();
				checkStatusAndData();
			}
		});

		function checkStatusAndData() {
			if (iterationcount == 10) {			
				dispalyCancelMessage();
				return;
			}
			displayProcessing();
			if (options.reportStatus != 'COMPLETED') {
				// ajax call
				$.getJSON(options.statusCheckURL, {					
					messageId : options.msgId,					
					ts : new Date().getTime()
				}, function(data) {					
					receiveStatusData(data);
				});

			}
			iterationcount++;
		}

		function receiveStatusData(data) {			
			if (data == 'PROCESSING') {
				setTimeout(checkStatusAndData, 3500);
				return;
			} else if (data == 'ERROR') {
				dispalyErrorMessage();			
			} else {
				//alert(data);
				getResults();
				//setTimeout(function(){getResults();},30000);
			}
		}

		
		
		function dispalyCancelMessage() {
		var baseid = $me.attr('id');
			id = '#' + baseid ;
			// $(id).text('CANCELLED');
			$(id).html('<ul><li>The data source is taking too long to respond [CANCELLED]</li></ul>');
			reSemantificationProcess=false;
			$("#"+baseid).removeClass('dynamicPaneBgLoader').addClass('dynamicPaneBgNoLoader')
		}
		function dispalyErrorMessage() {
		var baseid = $me.attr('id');
			id = '#' + baseid ;
			// $(id).text('ERROR');
			$(id).html('<ul><li> ERROR occured while resemantification</li></ul>');
			reSemantificationProcess=false;
			$("#"+baseid).removeClass('dynamicPaneBgLoader').addClass('dynamicPaneBgNoLoader')
		}

		function displayProcessing() {
			var baseid = $me.attr('id');
			//alert(baseid);
			$("#"+baseid).removeClass('dynamicPaneBgNoLoader').addClass('dynamicPaneBgLoader').html('<ul><li> [Processing... Resemantification]</li></ul>'); 
			//$(id).html(' [Processing...]');
		
		}
	

		function display() {

		}
	}
})(jQuery);


