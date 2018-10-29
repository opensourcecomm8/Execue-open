(function() {

	$.fn.dynamicTable = function(o) {
		var defaults = {
			linked:false
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
		
		return this.each(function() {
				 $me=$(this);
				 
				callData();	   
			});
		
			function callData(){
			var $inputURL=options.inputURL;
			var $params="";
			$me.css({"background":"url('../images/loadingWhite.gif') center center no-repeat"});
		    $.ajax({url:$inputURL,
						success:display,
						type:"get",
						dataType:"json"
					   });	
			}

		function display(data){
			if(data){
				//$me.css({"background":"url('../images/loadingWhite.gif') center center no-repeat"});
				var $tempDiv=$('<div></div>');
			var $table=$('<table></table>');
			$tempDiv.append($table);
			var price
			
			var $tr=$("<tr></tr>"); 
				$table.append($tr);
				
				for(var i=0; i<options.numberOfColums; i++){
					var $td=$("<td class='tableheading'></td>");
					var col=options.columnHeadings[i];
					$td.append(col);
					$tr.append($td);
					}
			
			$.each(data,function(k,v){
				var odd=false;
				if(k%2==1){odd = true;}	
				
				var $tr=$("<tr></tr>"); 
				var $a=$("<a href=''></a>"); 
				$table.append($tr);

					for(var i=0; i < options.numberOfColums; i++){
						var $td=$("<td class='column"+i+"'></td>");
						var col=options.columnVals[i].val;
						var colorCode=options.columnVals[i].colorCode;
						var linked=false;;
						linked=options.columnVals[i].linked;
						linkHref=options.columnVals[i].linkHref;
						var cellVal=eval('v.'+col);
						if(linked){
							var region = "";
							for(var j=0; j < options.attributeVals.length; j++){
								region = eval('v.'+options.attributeVals[j].val);
								$a.attr(options.attributeVals[j].name, region);
								$a.attr("id","indiceLink_"+k+region);
							}
							$a.attr("href","javascript:executeFunction('"+linkHref+"','indiceLink_"+k+region+"');"); 
							$td.append($a); 
							$a.append(cellVal);
						}
						else{
							$td.append(cellVal);
						}
						if(cellVal){
							cellVal=cellVal.toString();
						}
						if(colorCode && cellVal.substring(0,1)=="+"){ $td.removeClass("col"+i); $td.addClass("green"); }
						else if(colorCode && cellVal.substring(0,1)=="-"){ $td.removeClass("col"+i); $td.addClass("red"); }
						else if(colorCode && cellVal.substring(0,1)!="+" && cellVal.substring(0,1)!="-"){ $td.addClass("green"); }
						$tr.append($td);
					}
		 });
		$me.empty().html($tempDiv.html());
		$me.css("backgroundImage","none");
		
		setTimeout(callData, 60000);
		}
		
		}
		//$me.css("backgroundImage","none");
	};
})(jQuery);// $ is the main jQuery object
