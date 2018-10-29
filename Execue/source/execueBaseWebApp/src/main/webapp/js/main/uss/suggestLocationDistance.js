(function($) {
	// resizable textbox

	$.fn.resizableTextbox = function(el, options) {
		var $opts = $.extend({
			min : 5,
			max : 500,
			step : 7
		}, options);

		var $width = el.attr('offsetWidth');

		el.bind('keydown', function(e) {
			$(this).data('rt-value', this.value.length);
		}).bind('keyup', function(e) {
			var $this = $(this);
			var newsize = $opts.step * $this.val().length;
			if (newsize <= $opts.min) {
				newsize = $width;
			}
			if (!($this.val().length == $this.data('rt-value')
					|| newsize <= $opts.min || newsize >= $opts.max)) {
				$this.width(newsize);
			}
		});
	};

	$.fn.suggestLocationDistance = function(options) {

		return this.each(function() {

			var $count = 0;
			var $current = undefined; // currently focused element
			var $opts = {
				className : 'bit'
			};
			var $maininput = undefined;
			var $dataElement = undefined;
			var $element = undefined; // this element
			var $autoholder = undefined;
			var $autoresults = undefined;
			var $resultsshown = undefined;
			var $data = [];
			var $dosearch = false;
			var $autocurrent = undefined;
			var $autoenter = false;
			var $statName = "";
			var stat = false;
			var $auto = false;
			var $gCount = 0;
			var timer;
			var jsonRequestObj = null;
			// member methods
			
			
			function setEvents() {
				$(document).bind($.browser.msie ? 'keydown' : 'keypress',
						function(e) {
							if ($current && $current.data('type') == 'box'
									&& e.keyCode == 8) { // 8=BACKSPACE
								e.stopPropagation();
							}
						});

				$(document).bind('keyup', function(e) {
					e.stopPropagation();
					if (!$current) {
						return;
					}
					switch (e.keyCode) {

						case 46 :
						case 8 :
							if ($current.data('type') == 'box') {
								return dispose($current);
							}
							break;
					}
				});

				$(document).bind('click', function(e) {
					$(document).trigger('blur');
				});
			}

			function add(text, html) {
				if (text.displayName != undefined) {
					$displayText.text(text.displayName);
						$maininput.data("input").val(text.displayName);
					//alert(text.latitude+"::"+text.longitude+"::"+text.id);
					if(o.suggestURL!=""){
					$("#latitude").val(text.latitude);
					$("#longitude").val(text.longitude);
					$("#locationId").val(text.id);
					$("#locationBedId").val(text.locationBedId);
					$("#location").val(text.displayName);
					}else{
					$("#distance").val(text.name);
					}
				} else {
					// alert("invalid data");
					// $maininput.data('input').val('invalid');
				}
			}


			function createInput() {
				var li = $('<div class="' + $opts.className + '-input"></div>');

				var input = $('<input style="border:0px" type="text"></input>');
				$("input[type=text]").select().focus();
				input.bind('click', function(e) {
					$(".qiSuggestTerm-auto").hide();
					$(".qiSuggestCondition-auto").hide();
					autoShow("a");
					$autoholder.css('left',
							$maininput.data('input').position().left + 'px'); // added
					$autoholder
							.css('top', $maininput.data('input').position().top
									+ 15 + 'px');
					e.stopPropagation();
				}).bind('blur', function() {
					timer = setTimeout(autoHide, 100);

				}).bind('focus', function(e) {
					//setTimeout(function(){if($(this).val()==""){autoShow("a");}},100);
				}).bind('blur', function(e) {
					$(this).hide();	
					$displayText.show();				
				}).bind('keydown', function(e) {
					$(this).data('lastValue', this.value); //.data('lastCaret',getCaretPosition(input));
					if ($autoholder) {
						$dosearch = false;
						switch (e.keyCode) {
							case 38 :
								return autoMove('up');
							case 40 :
								return autoMove('down');
							case 9 :
								if ($resultsshown && $autocurrent
										&& $autocurrent.data('result')) {
									autoAdd($autocurrent);
									$autocurrent = false;
									return false;
								} else {
									$maininput.data('input')
											.val("Enter Metrics");
									return true;
								}
							case 13 : // enter
									if ($resultsshown && $autocurrent && $autocurrent.data('result')) {

									autoAdd($autocurrent);
									$autocurrent = false;
									$autoenter = true;
								} 
								$(this).hide();	
								$displayText.show();	
								break;
							case 27 : // esc
								if (!$resultsshown && $current
										&& $current.data('input')) {
									setTimeout(function() {
										$current.data('input')
												.val('Enter Metrics').select();
									}, 0);
								} else {
									e.stopPropagation();
									autoHide();
									return false;// skip IE default behavior
									// (clear field)
								}
								break;
							default :
								$dosearch = true;
						}

						if ($autoenter && $.browser.msie) {
							e.stopPropagation();
							$autoenter = false;
						}

						return e.keyCode != 13; // skip IE default behavior
						// (beeps)
					}
				}).bind('keypress', function(e) {
					if ($autoenter && !$.browser.msie) {
						e.stopPropagation();
						$autoenter = false;
					}
				}).bind('keyup', function(e) {

					$autoholder.css('left',
							$maininput.data('input').position().left + 'px'); // added
					$autoholder
							.css('top', $maininput.data('input').position().top
									+ 15 + 'px'); // added on 10 feb 2009 by
					// raj
					if ($autoholder) { 
						if ($dosearch) {
							autoShow(input.val());
						}
						// added on may 11 2009 for keys up & down ///
						switch (e.keyCode) {
							case 38 :
								return autoMove('up');
							case 40 :
								return autoMove('down');
						}
					}
				});

				$dataElement = $('<span id="ss_term"></span>');
				return li.data('type', 'input').data('input', input)
						.append($dataElement).append(input);
			}

			function makeResizable(li) {
				var el = li.data('input');
				el.data('resizable', $.fn.resizableTextbox(el, $.extend(
						$opts.resizable, {
							min : el.attr('offsetWidth'),
							max : $element.width()
						})));
				return this;
			}

			function isObjEmpty(obj) {
				for (var i in obj) {
					return false;
				}
				return true;
			}
			var specials = ['/', '.', '*', '+', '?', '|', '(', ')', '[', ']',
					'{', '}', '\\'];
			var specialsRE = new RegExp('(\\' + specials.join('|\\') + ')', 'g');

			function escRE(text) {
				return text.replace(specialsRE, '\\$1');
			}
			function removeLoader() {
				$maininput.data('input').removeClass('myinput1');
				$maininput.data('input').addClass('myinput');
			}
			function showLoader() {
				$maininput.data('input').removeClass('myinput');
				$maininput.data('input').addClass('myinput1');
			}
			function autoShow(search) {
				$autoholder.show();
				$autoholder.children().hide();

				if (!search
						|| !$.trim(search)
						|| (!search.length || search.length < $opts.autocomplete.minchars)) {
					$autoholder.find('.default').show();
					$resultsshown = false;
				} else {
					$resultsshown = true;
					$autoresults.show().empty();
					var j = 0;

					search = escRE(search);
					
					
					if (jsonRequestObj) {
						jsonRequestObj.abort();
					}
					showLoader();
					$modelId = $("#modelId").val();
					
					if(o.suggestURL!=""){
					
					jsonRequestObj = $.getJSON(o.suggestURL, {
						search : search,
						stat : stat,
						statName : $statName,
						modelId : $modelId
					}, function(jsonData) {
						if (!jsonData[0]) {
							removeLoader()
						}
						$autoresults.empty();
						$.each(jsonData, function(item) {
							var el = $('<li></li>').bind('click', function(e) {
								e.stopPropagation();
								autoAdd($(this));
							}).html(autoHighlight(this.displayName, search))
									.data('result', this);
							$autoresults.append(el);
							el.mouseover(function(){
								autoFocus($(this));				  
								}).mousedown(function(){autoAdd($(this));});
							removeLoader();
							if (j == 0) {
								++j;
								autoFocus(el);
							}
						});

						$autoresults.css('zIndex', 1100);
						$("#menuLayer0").css('zIndex', 2010);

					});
					
					}else{
						
						searchOperator(search);

					}
					
					
				}
				return this;
			}
			
			function searchOperator(search) {
				var $distanceListArray = [];
				var $distanceListArray1 = []
				var $opKey = {};
				$.each(o.distanceList, function(data) {
					var opName = this.displayName;
					if (search == opName.substring(0, search.length)
							|| search == opName) {
						$distanceListArray1=addToOperatorArray($distanceListArray1,this);
					} else {
						$distanceListArray1=addToOperatorArray($distanceListArray1,this);
					}
				});
				
				populateAutoSuggest($distanceListArray
						.concat($distanceListArray1));
			}
			
			function addToOperatorArray($distanceListArray1,x){
				var $opKey = {};
			            $opKey["name"] = x.name;
						$opKey["displayName"] = x.displayName;
						$opKey["type"] = x.type;
						
						    $distanceListArray1.push($opKey);
						
						 $opKey = {}	
				return $distanceListArray1;
			}
			
			function populateAutoSuggest(data) {
				if (!data[0]) {
					removeLoader()
				}
				var j = 0;
				$autoresults.empty();
				$.each(data, function(item) {

					var el = $('<li></li>').bind('click', function(e) {
						//$messageDiv.empty();
						//if (operatorType == 1 || ($currentState == "Format")) {
							$autoSelected = true;
						//}
						e.stopPropagation();
						autoAdd($(this));
					}).html(autoHighlight(this.displayName, $maininput
							.data('input').val())).data('result', this);
					$autoresults.append(el);
					el.mouseover(function(){
								autoFocus($(this));				  
								}).mousedown(function(){autoAdd($(this));});
					removeLoader()
					if (j == 0) {
						++j;
						autoFocus(el);
					}
				});
			}

			function autoHighlight(html, highlight) {
				return html.replace(new RegExp(highlight, 'gi'),
						function(match) {
							return '<em>' + match + '</em>';
						});
			}

			function autoHide() {
				$resultsshown = false;
				$autoholder.hide();
			}
			
			function autoMove(direction) {

				if ($resultsshown && $autocurrent) {
					if (direction == 'up') {
						if ($autocurrent.prev().length > 0)
							autoFocus($autocurrent.prev());
					} else {
						if ($autocurrent.next().length > 0)
							autoFocus($autocurrent.next());
					}
				}
				return this;
			}

			function autoFeed(text) {
				for (var i = 0; i < $data.length; ++i) {
					if ($data[i] == text)
						return this;
				}
				$data.push(text);
				return this;
			}
			function autoFocus(el) {

				if (el) {
					if ($autocurrent) {
						$autocurrent.removeClass('auto-focus');
					}

					$autocurrent = el.addClass('auto-focus');
				}
			}

			function autoAdd(el) {

				if (el && el.data('result')) {
					add(el.data('result'));
					autoHide();
				}
			}

			$(this).parent('li').addClass('qiSuggestTerm-input-text');

			$opts = $.extend({
				// auto
				onBoxDispose : function(item) {
					if ($autoholder) {
						//autoFeed($(this).data('text'));
					}
				},
				onInputFocus : function() {
					if ($autoholder) {
						autoShow();
					}
				},
				onInputBlur : function(e) {
					if ($autoholder) {
						$lastinput = $(this);
						setTimeout(function() {
							autoHide();
						}, 200);
					}
				}
			}, $.fn.suggestLocationDistance.defaults, options);

			// support metadata plugin (element specific options)
			var o = $.meta ? $.extend({}, $opts, $(this).data()) : $opts;
			var $snapShot = $("#" + o.snapShotId)
			$element = $(this);
			$(this).hide();

			$maininput = createInput();
			$maininput.find('input').addClass('myinput');
			var $displayText=$("<div>"+o.displayText+"</div>").addClass("displayText");
			if(o.multiple=="true"){
				$displayText=$("<div>Multiple</div>").addClass("displayText");
			}
			
			var holder = $('<div></div>').addClass(o.holderClass).bind(
					'click', function(e) {
						e.stopPropagation();
						if ($current != $maininput) {
							focus($maininput);
						}
						return false;
					}).append($maininput.hide()).insertBefore($(this)).append($displayText);
			
			$displayText.click(function(){
								
			autoShow("a");
			
			$("#featuresAlert").hide();
			$(this).hide();	$maininput.show();  $maininput.data("input").show(); setTimeout(function(){$maininput.data("input").focus(); },100);	 		
										}).mouseover(function(){/// alert(":::::"+$(this).text());
						holder.removeClass(o.holderClass).addClass(o.holderClass+"-hover");	
						holder.attr("alt",o.titleText).attr("title",o.titleText);
					if(o.multiple=="true" &&  $(this).text()=="Multiple"){
						$("#featuresAlert").empty().html(o.displayText).show();
						$("#featuresAlert").css("top",($("#showButton").position().top-35)+"px");
						$("#featuresAlert").css("left",($("#showButton").position().left-70)+"px");
					}
			}).mouseout(function(){
				holder.removeClass(o.holderClass+"-hover").addClass(o.holderClass);			
			if(o.multiple=="true" ){
						$("#featuresAlert").hide();
						
					}
			});
			$autoholder = $('<div id ="#' + this.id + '-auto"></div>');
			$maininput.append($autoholder);
			
			if ($autoholder) {
				$autoholder.addClass('qiSuggestTerm-auto').css('opacity',
						$opts.autocomplete.opacity)
						.hide();

				$autoresults = $('<ul></ul>').mouseout(function() {
					timer = setTimeout(autoHide, 1000);
				}).mouseover(function() {
					clearTimeout(timer);
				});
				$autoholder.append($autoresults);
				$autoholder.children($autoresults);
				//
			}

			makeResizable($maininput);
			setEvents();

			function ie7Compat() {
				var zIndexNumber = 1000;
				$('div').each(function() {
					$(this).css('zIndex', zIndexNumber);
					zIndexNumber -= 10;
				})
				$("#menuLayer0").css('zIndex', 2010);
			}

		});

	};

	$.fn.suggestLocationDistance.defaults = {
		resizable : {},
		className : 'bit',
		separator : ',',
		startInput : true,
		hideEmpty : true,
		noRemove : false, // set to true if no remove boxes
		distanceList:[],
		suggestURL : 'querySuggest/suggestSelect!suggestSelect.action',
		displayText : 'dummy Text',
		multiple:'false',
		titleText:'Click here to change the zip',
		holderClass:'',
		autocomplete : {
			opacity : 1,
			maxresults : 10,
			minchars : 1
		}
	};

})(jQuery);
