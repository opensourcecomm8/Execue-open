(function() {

	$.fn.helpMessage = function(o) {
		var defaults = {
			message:""
		     };

		function preProcess() {
			//alert('pre processing');
		};

		function postProcess() {
			//alert('post processing');
		};
		
		function emptyParam() {
			
		};
		
		
		
		var options = $.extend(defaults, o);
		var $me;
		var $uploadHelpPopup=$("<div class='popup' style='display: none; '></div>");
		var $popupOuter=$("<div class='popupOuter'></div>");
		var messageMap=new Array();
		var $message="";
		var left=undefined;
		var top=undefined;
		return this.each(function() {
				 $me=$(this);
				var $messageKey=options.messageKey;
					showUploadHelp($messageKey)
					$me.bind("mouseover",function(){
						displayPopup($message);						
					});
					
					$me.bind("mouseout",function(){
						hidePopup();						
					});
			});
		
		
			function showUploadHelp(messageKey){
			// show the values stored
			// need to check in map first if value is avilabale will get it else get it from the server
			  for (var key in messageMap) {
				if(key==messageKey){      
				  $message=messageMap[key];
				   return ;
				}
			  }
			 $.get('../swi/showHelpMessages.action?helpMessage='+messageKey,function(data){
				 messageMap[messageKey]=data;
				 $message=data;
			  });
			}
		
		function displayPopup(data){ 
		$uploadHelpPopupText=$("<div style='margin-right: 20px;'></div>"); $uploadHelpPopupText.empty().append(data);
		$closeImg=$("<img border='0' src='../images/admin/closeButtonForMessage.png'>");
		$closeButtonLink=$("<a href='#'  style='font-weight: bold;'></a>");
		
		$closeButtonLinkDiv=$("<div style='width: 16px; float: right; color: rgb(255, 255, 255); font-weight: bold;'>");
		$closeButtonLink.append($closeImg);
		$closeButtonLinkDiv.append($closeButtonLink);
		$uploadHelpPopup.empty().append($closeButtonLinkDiv);
		
		$uploadHelpPopup.append($uploadHelpPopupText);
		//$me.append($uploadHelpPopup);
		$popupOuter.empty();
		
		$popupOuter.append($uploadHelpPopup);
		$popupOuter.insertAfter($me);
		$closeButtonLink.bind("click",function(){hidePopup();});
		
		left=$me.position().left;
		top=$me.position().top;
		$popupOuter.css("left",(left+40)+"px");
		$popupOuter.css("top",(top-20)+"px");
		$uploadHelpPopup.show();
		}
		function hidePopup(){
			$uploadHelpPopup.hide();
		}
		//$me.css("backgroundImage","none");
	};
})(jQuery);// $ is the main jQuery object
