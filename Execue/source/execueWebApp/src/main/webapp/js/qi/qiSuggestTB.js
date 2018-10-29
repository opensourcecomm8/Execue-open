

(function($) {
	// resizable textbox

	/*
	 * $.fn.resizableTextbox = function(el, options) { var $opts = $.extend({
	 * min : 5, max : 500, step : 7 }, options);
	 * 
	 * var $width = el.attr('offsetWidth');
	 * 
	 * el.bind('keydown', function(e) { $(this).data('rt-value',
	 * this.value.length); }).bind('keyup', function(e) { var $this = $(this);
	 * var newsize = $opts.step * $this.val().length; if (newsize <= $opts.min) {
	 * newsize = $width; } if (!($this.val().length == $this.data('rt-value') ||
	 * newsize <= $opts.min || newsize >= $opts.max)) { $this.width(newsize); }
	 * }); };
	 */

	$.fn.qiSuggestTB = function(options) {

		return this.each(function() {
			// member vars

			var $count = 0;
			var $current = undefined; // currently focused element
			var $bitsOrder = {};
			var $bitsOrderArray = [];
			var $opts = {
				className : 'bit'
			};
			var $maininput = undefined;
			var $element = undefined; // this element
			// auto
			var $autoholder = undefined;
			var $autoresults = undefined;
			var $resultsshown = undefined;
			var $data = [];
			var $removeBitsArray = [];// remove boxes
			var $dosearch = false;
			var $autocurrent = undefined;
			var $autoenter = false;
			var $lastinput = undefined;
			var $actionName = 'querySuggest/suggestOrderBy!suggestBTsForOrderBy.action'
			var $countBox = 0;
			var tokenArray = []; // boxes array
			// var temp;
			var $currentState = "start";
			var $preDiv = $('<span name="preDiv"></span>');
			var $postDiv = $('<span name="postDiv"></span>');
			var $messageDiv = $('<div></div>');
			var jsonRequestObj=null;
			var $operatorList = [{
				name : "TOP",
				displayName :"TOP",
				type : "operator"
			}, {
				name : "BOTTOM",
				displayName :"BOTTOM",
				type : "operator"
			}];
			var $opCount = 0;
			var $preDiv = $('<span name="preDiv"></span>');
			var $postDiv = $('<span name="postDiv"></span>');
			var timer;
			// member methods
			$(this).data("orderDataArray", $bitsOrderArray);
			/*
			 * function setEvents() { $(document).bind($.browser.msie ?
			 * 'keydown' : 'keypress', function(e) { if ($current &&
			 * $current.data('type') == 'box' && e.keyCode == 8) { //
			 * 8=BACKSPACE e.stopPropagation(); } });
			 * 
			 * $(document).bind('keyup', function(e) { e.stopPropagation(); if
			 * (!$current) { return; } switch (e.keyCode) { case 37 : return
			 * move('left'); // left arrow case 39 : return move('right');//
			 * right arrow case 46 : case 8 : if ($current.data('type') ==
			 * 'box') { return clearToken($current); } break; } });
			 * 
			 * $(document).bind('click', function(e) {
			 * $(document).trigger('blur'); }); }
			 */

			function resetState(status) {
				$currentState = status.state;
				operatorType = status.opType;
			}
			function update(text, el) {
				var status = {
					state : ''
				};
				status.state = $currentState;
				el.data('state', status);
				if ($currentState == "start") {
					if (text["type"] == "CONCEPT") {
						$currentState = "businessTerm";
					}
				}
				if ($currentState == "businessTerm") {
					if (text["type"] == "operator") {
						$currentState = "operator";
					}
				}
				$opCount++
				/*
				 * if ($currentState == "operator") { $currentState = "value";
				 * //$maininput.data('input').hide() }
				 */
				/*
				 * var buf = ''; for (var key in $bitsOrder) { if (buf.length >
				 * 0) { buf += $opts.separator; }
				 * //alert("key::"+$bitsOrder[key]) buf += $bitsOrder[key];
				 *  } $bitsOrderArray.pop(); $bitsOrderArray.push($bitsOrder);
				 * $element.val(buf); return $element;
				 */
			}

			function add(text, html) {
				/*
				 * if ($actionName ==
				 * "querySuggest/suggestOrderBy!suggestBTsForOrderBy.action") {
				 * temp = text; }
				 */
				// $count=$count+1
				var id = $opts.className + '-' + $count++;
				tokenArray.push($countBox + "$#$" + ($count - 1));
				var li = $('<li>' + (text ? text.displayName : html) + '</li>')
						.addClass($opts.className + '-box').data('type', 'box');
				li.bind('mouseenter', function(e) {
					$(this).css("position","relative");		
					li.addClass('bit-hover');
					li.append($('<a href="#" class="closebutton"></a>').bind(
							'click', function(e) {
								e.stopPropagation();
								if (!$current) {
									focus($maininput);
								}
								clearToken(li);
							}));
				}).bind('mouseleave', function(e) {
					$(this).css("position","static");
					li.removeClass('bit-hover');
					$('a.closebutton').remove();
				});
				var el = li;
				el.attr('id', id).bind('click', function(e) {
					e.stopPropagation();
					focus(el);
				}).insertBefore($maininput);
				$removeBitsArray.push(el);
				$bitsOrder[id] = text// el.attr('id');
				update(text, el);
				prepareTemplate();

			}
			function clearTB() {
				$countBox = 0
				$opCount = 0;
				$bitsOrder = {}
				$bitsOrderArray.length=0
				$count=0;
				clearBitsArray();
				$currentState = "start";
				$snapShot.empty();
				showHideQuerySnapShot();
				prepareTemplate();
				$maininput.data('input').show();
				$maininput.data('input').width(150);
				$maininput.data('input').val("Enter Metrics").select().focus();
			}

			function deleteSnapshot(no) {
				$bitsOrderArray.splice(no, 1);
				generateSnapShot();
				clearTB();
			}
			function clear(el) {
				if (el.data('type') == 'box') {
					el.trigger('boxDispose');
				}
				el.remove();
			}
			function getBitId(bitid) {
				colid = "";
				for (var i = 0; i < tokenArray.length; i++) {
					var obj = tokenArray[i].substring(4);
					if (obj == bitid) {
						colid = tokenArray[i].substring(0, 1);
					}
				}
				return colid;
			}
			// clear the text box
			function clearToken(el) {
				var bitid = el.attr('id').substring(4);
				var colid = getBitId(bitid);
				var deletedElement = $bitsOrder[el.attr('id')];
				delete $bitsOrder[el.attr('id')];
				$count--
				/*
				 * if (deletedElement["type"] == "CONCEPT" ) { $countBox = 0; }
				 */
				resetState(el.data('state'));
				if ($currentState == 'start') {
					clearTB();
				}
				var removeIndex = -1;
				for (var i = 0; i < $removeBitsArray.length; ++i) {
					if ($removeBitsArray[i] == el) {
						removeIndex = i;
						break;
					}
				}
				removeLength = $removeBitsArray.length
				for (var i = removeIndex; i < removeLength; ++i) {
					clear($removeBitsArray.pop());
				}
				$maininput.data('input').show();
				prepareTemplate();
				return this;
			}
			function clearBitsArray() {
				for (var i = 0; i < $removeBitsArray.length; ++i) {
					clear($removeBitsArray[i]);
				}
			}
			/**
			 * Add focus class to current. If the current is input, then focus
			 * it. Set current to the element.
			 */
			function focus(el) {
				if ($current && $current == el) {
					return el;
				}
				//blur();
				el.addClass($opts.className + '-' + el.data('type') + '-focus');
				$current = el;
				return el;
			}
			/**
			 * If current is input, then blur the control. Remove focus class
			 * from current and set current to undefined..
			 */
			function blur() {
				if (!$current)
					return this;

				if ($current.data('type') == 'input') {
					var input = $current.data('input');
					setTimeout(function() {
						input[0].blur();
					}, 200);// causes recursive
					// call on blur() on
					// some browsers
					input.trigger('inputBlur');
				} else {
					$current.trigger('boxBlur');
				}

				$current.removeClass($opts.className + '-'
						+ $current.data('type') + '-focus');
				if ($current) { // if called recursively (see above), then
					// $current==null
				}
				$current = undefined;
				return this;
			}

			function createInput() {
				var input1 = "";
				var li = $('<li class="' + $opts.className + '-input"></li>');

				var input = $('<input type="text"></input>').attr("id","maininp");
				input.bind('click', function(e) {
					 $(".qiSuggestTerm-auto").hide();
					  $(".qiSuggestCondition-auto").hide();
					input.val('');
					autoShow("a");
					$autoholder.css('left',
							$maininput.data('input').position().left + 'px'); // added
					$autoholder
							.css('top', $maininput.data('input').position().top
									+ 15 + 'px'); 
					e.stopPropagation();
				}).bind('blur',function(){timer=setTimeout(autoHide,1000);}).bind('focus', function(e) {
						// focus(li);
					}).bind('blur', function(e) {
					if ($current == li) {
						// $current = undefined;
						blur();
					}
				}).bind('keydown', function(e) {
					$(this).data('lastValue', this.value).data('lastCaret',
							getCaretPosition(input));
					if ($autoholder) {
						$dosearch = false;
						switch (e.keyCode) {
							case 38 :
								return autoMove('up');
							case 40 :
								return autoMove('down');
							case 13 : // enter
								handleEnterKey()
								break;
							case 27 : // esc
								if (!$resultsshown && $current
										&& $current.data('input')) {
									setTimeout(function() {
										$current.data('input').val('')
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
					// on
					// 10
					// feb
					// 2009
					// by
					// raj
					$autoholder
							.css('top', $maininput.data('input').position().top
									+ 15 + 'px'); // added
					// on
					// 10
					// feb
					// 2009
					// by
					// raj
					if ($autoholder) {
						if ($dosearch) {
							autoShow(input.val());
						}
					}
				});

				// bindCustomEvents(input);
				return li.data('type', 'input').data('input', input)
						.append($preDiv).append(input).append($postDiv);
			}
			function prepareTemplate() {
				// $preDiv.empty();
				$postDiv.empty();
				if ($currentState == "start" || $currentState == "stat") {
					$maininput.data('input').val('Enter Metrics').select();
					$postDiv
							.append('&nbsp;<span id=operator class="suggestTemplate">Operator</span>&nbsp;<span id=value class="suggestTemplate">value</span>')
							.addClass($opts.className + '-box');
				} else if ($currentState == "businessTerm") {
					$maininput.data('input').val('Operator').select().focus();
					$maininput.data('input').width(50);
					$preDiv.empty();
					$postDiv
							.empty()
							.append('&nbsp;<span class="suggestTemplate">value</span>')
							.addClass($opts.className + '-box');
				} else if ($currentState == "operator") {
					$maininput.data('input').val('value').select().focus();
				
					$preDiv.empty();
					$postDiv.empty();
				}
			}
			function autoAddString(data) {
				add({
					name : data,
					displayName : data,
					type : 'VALUE'
				});
				autoHide();
				focus($maininput);
				$autoholder.css('left',
						$maininput.data('input').position().left + 'px');
				$autoholder.css('top', $maininput.data('input').position().top
						+ 15 + 'px');
			}
			function handleEnterKey() {
				$messageDiv.addClass("messageDiv");
				if ($resultsshown && $autocurrent
						&& $autocurrent.data('result')) {
					autoAdd($autocurrent);
					$autocurrent = false;
					$autoenter = true;
				} else if ($currentState == 'operator') {
					if (!checkOperatorValue()) {
						addToken = false;
					}else{
						addToken = true;
					}
					if (addToken) {
						$messageDiv.empty();
						autoAddString($maininput.data('input').val());
						addBitsToArray();
						$autocurrent = false;
						$autoenter = true;
					}
				} else {
					autoHide();
					$maininput.data('input').val('Please select valid entry');
					$maininput.data('input').select();
					$maininput.data('input').focus();
				}
			}
			function checkOperatorValue() {
				optValue = $maininput.data('input').val();				
				if (optValue != parseInt(optValue)|| parseInt(optValue)<=0) {						
					var msg="Enter Numeric value";
					dispMessage(msg);
					return false;
				}else{
					return true;
				}
				// $maininput.data('input').val(optValue);
			}
			function dispMessage(msg){
				$messageDiv.empty();
				$messageDiv.append(msg);
				$maininput.data('input').val("value").select().focus();				
			}
			function addBitsToArray() {
				/*bitsCount = 0;
				$.each($bitsOrder, function(i, val) {
					bitsCount++;
				});*/
				if ($opCount >= 3) {
					$maininput.data('input').css('width', '150px');
					$bitsOrderArray.pop();
					$bitsOrderArray.push($bitsOrder);
					// $bitsOrder = {};
					generateSnapShot();
					focus($maininput);
					// $currentState = "start";
					// $opCount = 0;
					$maininput.data('input').hide();
				} else {
					alert("Invalid Data");
				}
			}
			function generateSnapShot() {
				spacer = "&nbsp;";
				var wordCount = 0;
				$countBox = 0;
				$snapShot.empty();
				if ($bitsOrderArray.length > 0) {
					$snapShot.append("<div  style='width:100%;'>"
							+ o.snapShotText + "</div>");
					// $snapShot.append('<br>');
				}
				$.each($bitsOrderArray, function(index, bitsOrder) {
					// $snapShot.append('<table width=100% ><tr><td>');
					$link = $("<span style='float:left;padding-left:10px;padding-top:1px;'>&#8226;</span>")
							.bind('click', {
								id : index
							}, function(e) {
								e.stopPropagation();
									// deleteSnapshot(e.data.id);
								});
					$snapShot.append($link);
					count = 0;
					for (key in bitsOrder) {
						count++;
						$snapShot.append("<span style='float:left;'>&nbsp;"
								+ bitsOrder[key]["displayName"] + "</span>");
					}
					wordCount += 1;
					if (wordCount == 3) {
						$snapShot
								.append("<div style='width:100%;float:left;'><img src='images/1pix.gif' width='150' height='1' /></div>");
						wordCount = 0;
					}
						// $snapShot.append('</td></tr></table>');
				});

				showHideQuerySnapShot();
			}
			function getCaretPosition(input) {
				if (input.createTextRange) {
					var r = document.selection.createRange().duplicate();
					r.moveEnd('character', input.val().length);
					if (r.text === '') {
						return input.val().length;
					}
					return input.val().lastIndexOf(r.text);
				} else {
					return input.attr('selectionStart');
				}
			}

			var specials = ['/', '.', '*', '+', '?', '|', '(', ')', '[', ']',
					'{', '}', '\\'];
			var specialsRE = new RegExp('(\\' + specials.join('|\\') + ')', 'g');

			function escRE(text) {
				return text.replace(specialsRE, '\\$1');
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
					// var j = 0;
					search = escRE(search);
					if ($currentState == "start") {
						if (jsonRequestObj) {
							jsonRequestObj.abort();
							}
							showLoader();
						jsonRequestObj=$.getJSON($actionName, {
							search : search
						}, populateAutoSuggest);
					} else if ($currentState == "businessTerm") {
						populateAutoSuggest($operatorList);
					}
				}
				return this;
			}
			function removeLoader(){
				$maininput.find('input#maininp').removeClass('maininput1');
					$maininput.find('input#maininp').addClass('maininput');
			}
			function showLoader(){
				$maininput.find('input#maininp').removeClass('maininput');
					$maininput.find('input#maininp').addClass('maininput1');
			}
			function populateAutoSuggest(data) {
				if(!data[0]){
					removeLoader()
					}
					$autoresults.empty();
				var j = 0;
				$.each(data, function(item) {
					var el = $('<li></li>').bind('mouseenter', function(e) {
						autoFocus($(this));
					}).bind('click', function(e) {
						e.stopPropagation();
						autoAdd($(this));
					}).html(autoHighlight(this.displayName, $maininput.data('input')
							.val())).data('result', this);
					$autoresults.append(el);
					removeLoader();
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
			function autoFocus(el) {
				if (el) {
					if ($autocurrent) {
						$autocurrent.removeClass('auto-focus');
					}
					$autocurrent = el.addClass('auto-focus');
				}
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
			function autoAdd(el) {
				$maininput.data('input').val('');
				if (el && el.data('result')) {
					add(el.data('result'));
					for (var i = 0; i < $data.length; ++i) {
						if ($data[i] == el.data('result')) {
							delete $data[i];
							break;
						}
					}
					autoHide();
					focus($maininput);
					$countBox = $countBox + 1;
					if ($countBox == 1) {
						$("#operator").hide();
					}
					$maininput.data('input').show();
					if ($countBox > 3) {
						$maininput.data('input').hide();
					}
					$autoholder.css('left',
							$maininput.data('input').position().left + 'px');
					$autoholder
							.css('top', $maininput.data('input').position().top
									+ 15 + 'px'); // added
				}
			}
			/*
			 * function bindCustomEvents(el) { // bind custom events, events are
			 * in options with ^on[A-Z]\w* : // function for (var k in $opts) {
			 * if (k.search(/^on[A-Z]/) == 0 && typeof $opts[k] == 'function') {
			 * var ev = k.substr(2, 1).toLowerCase() + k.substr(3); el.bind(ev,
			 * $opts[k]); } } }
			 */
			// body
			$(this).parent('li').addClass('qiSuggestCondition-input-text');
			$opts = $.extend({
				// auto
				onBoxClear : function(item) {
					if ($autoholder) {
						autoFeed($(this).data('text'));
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
						// we need to call autoHide _after_ the click is called,
						// clever
						// hack?
						setTimeout(function() {
							autoHide();
						}, 200);
					}
				}
			}, $.fn.qiSuggestTB.defaults, options);

			// support metadata plugin (element specific options)
			var o = $.meta ? $.extend({}, $opts, $(this).data()) : $opts;

			$element = $(this);
			$(this).hide();
			$maininput = createInput();
			$maininput.find('input').addClass('maininput');
			prepareTemplate();
			var holder = $('<ul></ul>').addClass('qiSuggestCondition-holder')
					.bind('click', function(e) {
						e.stopPropagation();
						if ($current != $maininput) {
							focus($maininput);
						}
						return false;
					}).append($maininput).insertBefore($(this));
					
			$("#tb").append($messageDiv).append(holder);
			$autoholder = $('<div id ="#' + this.id + '-auto"></div>');
			$maininput.append($autoholder);
			if ($autoholder) {
				$autoholder.addClass('qiSuggestCondition-auto').css('opacity',
						$opts.autocomplete.opacity).hide();

				$autoresults = $('<ul></ul>').mouseout(function(){ timer=setTimeout(autoHide,1000);}).mouseover(function(){clearTimeout(timer);});
				$autoholder.append($autoresults);
				$autoholder.children($autoresults);
			}
			// setEvents();
			var $snapShot = $("#" + o.snapShotId);
			$("#tb .qiSuggestCondition-holder").css("width","530px");
			// public methods are added to this
			this.add = add;
			this.autoFeed = autoFeed;
			this.clearTB = clearTB;
			this.deleteSnapshot = deleteSnapshot;
			this.addBitsToArray = addBitsToArray;
			orderDataArray = $(this).data("orderDataArray");
			/*
			 * this.qiOrderByGenerateXML = function(o) { var XML = new
			 * XMLWriter();
			 * 
			 * $.each(orderDataArray, function(index, orderByTerm) {
			 * XML.BeginNode("orderBys"); XML.BeginNode("orderBy"); for (key in
			 * orderByTerm) { if (orderByTerm[key]["type"] == "CONCEPT") {
			 * XML.BeginNode("term") XML.Node("name", orderByTerm[key]["name"]);
			 * XML.Node("type", orderByTerm[key]["type"]); XML.EndNode(); } else
			 * if (orderByTerm[key]["type"] == "operator") { XML.Node("type",
			 * orderByTerm[key]["name"]); } else if (orderByTerm[key]["type"] ==
			 * "VALUE") { XML.Node("value", orderByTerm[key]["name"]); } }
			 * XML.EndNode(); XML.EndNode(); }); //alert('XML.ToString() ' +
			 * XML.ToString()); return XML.ToString(); }
			 */

			this.qiTopBottomGenerateXML = function(o) {
				var XML = new XMLWriter();
				$.each(orderDataArray, function(index, topBottomTerm) {
					XML.BeginNode("topBottom");
					for (key in topBottomTerm) {
						if (topBottomTerm[key]["type"] == "CONCEPT") {
							XML.BeginNode("term")
							XML.Node("name", topBottomTerm[key]["name"]);
							XML.Node("type", topBottomTerm[key]["type"]);
							XML.EndNode();
						} else if (topBottomTerm[key]["type"] == "operator") {
							XML.Node("type", topBottomTerm[key]["name"]);
						} else if (topBottomTerm[key]["type"] == "VALUE") {
							XML.Node("value", topBottomTerm[key]["name"]);
						}
					}
					XML.EndNode();
				});
				return XML.ToString();
			}
		});
	};
	$.fn.qiSuggestTB.defaults = {
		resizable : {},
		className : 'bit',
		separator : ',',
		startInput : true,
		hideEmpty : true,
		// auto
		snapShotId : 'ss_TB',
		snapShotText : 'snapShotText',
		autocomplete : {
			opacity : 1,
			maxresults : 10,
			minchars : 1
		}
	};

})(jQuery);
