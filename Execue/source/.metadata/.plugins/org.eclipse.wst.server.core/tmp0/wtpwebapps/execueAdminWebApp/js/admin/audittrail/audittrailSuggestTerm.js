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

	$.fn.qiSuggestTerm = function(options) {
		return this.each(function() {
			var $count = 0;
			var $current = undefined; // currently focused element
			var $bits = {};
			var $bitsArray = [];
			var $opts = {
				className : 'bit'
			};
			var $maininput = undefined;
			var $dataElement = undefined;
			var $element = undefined; // this element
			// auto
			var $autoholder = undefined;
			var $autoresults = undefined;
			var $resultsshown = undefined;
			var $data = [];
			var $dosearch = false;
			var $autocurrent = undefined;
			var $autoenter = false;
			var $lastinput = undefined;
			var $lastInput = [];
			var $auto = false;
			var $gCount = 0;
			var timer;
			var jsonRequestObj = null;
			var defaultText = 'Select User';
			var defaultAuditTypeText = 'Select Audit Type';
			$(this).data("dataArray", $bitsArray);

		
			// body
			$(this).parent('li').addClass('qiSuggestTerm-input-text');

			$opts = $.extend({
				// auto
				onBoxDispose : function(item) {
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
						// clever hack?
						setTimeout(function() {
							autoHide();
						}, 200);
					}
				}
			}, $.fn.qiSuggestTerm.defaults, options);

			// support metadata plugin (element specific options)
			var o = $.meta ? $.extend({}, $opts, $(this).data()) : $opts;
			var $snapShot = $("#" + o.snapShotId)
			$element = $(this);
			$(this).hide();

			$maininput = createInput();
			$maininput.find('input').addClass('maininput');

			var holder = $('<ul></ul>').addClass('qiSuggestTerm-holder').bind(
					'click', function(e) {
						e.stopPropagation();
						if ($current != $maininput) {
							focus($maininput);
						}
						return false;
					}).append($maininput).insertBefore($(this));

			$autoholder = $('<div id ="#' + this.id + '-auto"></div>');
			$maininput.append($autoholder);
			if ($autoholder) {
				$autoholder.addClass('qiSuggestTerm-auto').css('opacity',
						$opts.autocomplete.opacity)
						// .css('left',
						// $maininput.data('input').position().left+'px')

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

			// member methods

			function clearAll() {				
				$bits = {};
				$dataElement.empty();
				$bitsArray.length = 0;

			}

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
						case 37 :
							return move('left'); // left arrow
						case 39 :
							return move('right');// right arrow
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

			function update() {

				var buf = '';
				for (var key in $bits) {
					if (buf.length > 0) {
						buf += $opts.separator;
					}
					buf += $bits[key];
				}
				$bitsArray.pop();
				$bitsArray.push($bits);
				$element.val(buf);
				return $element;
			}

			function add(text, html) {
				if (text.displayName != undefined) {
					var id = $opts.className + '-' + $count++;
					var el = '';
					if (o.snapShotId == 'ss_summarize') {
						el = createBox(text ? text.displayName : html,
								$maininput).attr('id', id).attr('auditType',
								text.id).bind('click', function(e) {
							e.stopPropagation();
							focus(el);
						});
					} else {
						el = createBox(text ? text.displayName : html,
								$maininput).attr('id', id).attr('objId',
								text.id).bind('click', function(e) {
							e.stopPropagation();
							focus(el);
						});
					}
					
					if (text.id == -1 || text.id == -2 || text.id == -3) {
						$maininput.data('input').val('');
						$dataElement.empty();
						$dataElement.append(el);					
						$maininput.data('input').attr("disabled", "disabled"); 						
					} else {
						$dataElement.append(el);
						if (o.snapShotId == 'ss_summarize') {
							$maininput.data('input').val(defaultAuditTypeText)
									.select().focus();
						} else {
							$maininput.data('input').val(defaultText).select()
									.focus();
						}

					}
					

					// $dataElement.empty();
					text['element'] = el;
					$bits[id] = text;

					for (key in text) {
						if (key == "type") {
							$lastInput.push(text[key]);
						}
					}
					update();
					// $maininput.data('input').val(defaultText).select()
					// .focus();

				}
			}

			function bindEvents(input) {
				input.bind('blur', function() {
					timer = setTimeout(autoHide, 1000);

				}).bind('focus', function(e) {
					// focus(li);
					// $(this).val(defaultText).select();
					return false;
				}).bind('blur', function(e) {

					if ($current == li) {
						// $current = undefined;
						// blur();
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
							case 9 :
								if ($resultsshown && $autocurrent
										&& $autocurrent.data('result')) {
									autoAdd($autocurrent);
									$autocurrent = false;
									return false;
								} else {
									$maininput.data('input').val(defaultText);
									return true;
								}
							case 13 : // enter

								if ($resultsshown && $autocurrent
										&& $autocurrent.data('result')) {
									autoAdd($autocurrent);
									$autocurrent = false;
									$autoenter = true;
								} else if (this.value) {

									// add(this.value);
									// this.value = 'Please Enter valid Data';
									$maininput.data('input')
											.val('Please select valid entry')
											.select().focus();
								}
								break;
							case 27 : // esc
								if (!$resultsshown && $current
										&& $current.data('input')) {
									setTimeout(function() {
										$current.data('input').val(defaultText)
												.select();
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
						// added on may 11 2009 for keys up & down ///
					}
				});

			}

		/*	function unbindEvents() {
				$maininput.find('input').unbind("click").unbind("blur")
						.unbind("focus").unbind("keydown").unbind("keypress")
						.unbind("keyup");
			}*/

			function dispose(el) {	
				$maininput.data('input').removeAttr("disabled"); 
				$lastInput.pop();
				delete $bits[el.attr('id')];
				update();
				if ($current == el) {
					focus(el.next());
				}
				if (el.data('type') == 'box') {
					el.trigger('boxDispose');
				}
				el.remove();
				return this;
			}

			/**
			 * Add focus class to current. If the current is input, then focus
			 * it. Set current to the element.
			 */
			function focus(el) {
				if ($current && $current == el) {
					return el;
				}

				blur();

				el.addClass($opts.className + '-' + el.data('type') + '-focus');

				if (el.data('type') == 'input') {
					var input = el.data('input');
					input.trigger('inputFocus');
					setTimeout(function() {
						input[0].focus();
					}, 200);
				} else {
					el.trigger('boxFocus');
				}
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
					}, 200);// causes recursive call on blur() on some browsers
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

			function createBox(text, focusElementOnRemove) {

				if (text == undefined) {
					return;
				}
				var li = $('<li>' + text + '</li>').addClass($opts.className
						+ '-box').data('type', 'box');
				if (!$opts.noRemove) {
					li.bind('mouseenter', function(e) {
						li.addClass('bit-hover');
						// alert($(this).position().left);
						$(this).css("position", "relative");
						li.append($('<a href="#" class="closebutton"></a>')
								.bind('click', function(e) {
									e.stopPropagation();
									if (!$current) {
										focus(focusElementOnRemove);
									}
									dispose(li);
								}));
					}).bind('mouseleave', function(e) {
						$(this).css("position", "static");
						li.removeClass('bit-hover');
						$('a.closebutton').remove();
					}).bind('click', function(e) {
						e.stopPropagation();
						if (!$current) {
							focus(focusElementOnRemove);
						}
						// dispose(li);
						li.addClass('bit-hover');
					}).data('text', text);
				}
				bindCustomEvents(li);
				return li;
			}

			function createInput() {				
				var li = $('<li class="' + $opts.className + '-input"></li>');

				var input = $('<input type="text"></input>');				
				$("input[type=text]").val(defaultText).select().focus();
				$("#advOpt_summarize").val(defaultAuditTypeText);
				
				input.bind('click', function(e) {
					$(this).val("");
					$(".qiSuggestTerm-auto").hide();
					$(".qiSuggestCondition-auto").hide();
					autoShow("a");
					$autoholder.css('left',
							$maininput.data('input').position().left + 'px'); // added
					$autoholder
							.css('top', $maininput.data('input').position().top
									+ 15 + 'px');
					e.stopPropagation();
				});
				bindEvents(input);
				bindCustomEvents(input);
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

			function checkInput() {
				var input = $current.data('input');
				return !input.data('lastValue')
						|| (getCaretPosition(input) === 0 && input
								.data('lastCaret') === 0);
			}

			function move(direction) {
				var el = direction == 'left' ? $current.prev() : $current
						.next();
				if (el.length > 0
						&& (!$current.data('input') || checkInput() || direction == 'right')) {
					focus(el);
				}
				return this;
			}

			function isObjEmpty(obj) {
				for (var i in obj) {
					return false;
				}
				return true;
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
			function removeLoader() {
				$maininput.data('input').removeClass('maininput1');
				$maininput.data('input').addClass('maininput');
			}
			function showLoader() {
				$maininput.data('input').removeClass('maininput');
				$maininput.data('input').addClass('maininput1');
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
					jsonRequestObj = $.getJSON(o.suggestURL, {
						search : search
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
							removeLoader();
							// TODO: Need to move to css
							$autoresults.append($('<br>'));
							if (j == 0) {
								++j;
								autoFocus(el);
							}
						});

						$autoresults.css('zIndex', 1100);
						$("#menuLayer0").css('zIndex', 2010);

					});
				}
				return this;
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

				}
			}

			function bindCustomEvents(el) {
				// bind custom events, events are in options with ^on[A-Z]\w* :
				// function
				for (var k in $opts) {
					if (k.search(/^on[A-Z]/) == 0
							&& typeof $opts[k] == 'function') {
						var ev = k.substr(2, 1).toLowerCase() + k.substr(3);
						el.bind(ev, $opts[k]);
					}
				}
			}

			function ie7Compat() {
				var zIndexNumber = 1000;
				$('div').each(function() {
					$(this).css('zIndex', zIndexNumber);
					zIndexNumber -= 10;
				})
				$("#menuLayer0").css('zIndex', 2010);
			}

			function setHeight() {
				$ht = $("#showAll").height() + 20;
				$("#showAllContainer .divBorderWhite").height($ht);
			}

			function setAutoWidth(cnt) {
				if (o.snapShotId == "ss_metrics") {
					$w = $("#business_metrics .qiSuggestTerm-holder").width();
					if (cnt == 0) {
						$w = 0;
					}
					if (cnt == 1) {
						$w = 200;
					}
					if ($w < 400) {
						$("#business_metrics .qiSuggestTerm-holder").width(530);
					}

					if ($w >= 400) {
						$("#business_metrics .qiSuggestTerm-holder").css(
								"width", "530px");
					}

				}
				if (o.snapShotId == "ss_summarize") {

					$w = $("#groups .qiSuggestTerm-holder").width();
					if (cnt == 0) {
						$w = 0;
					}
					if (cnt == 1) {
						$w = 200;
					}
					if ($w < 400) {
						$("#groups .qiSuggestTerm-holder").width($w + 200);
					}

					if ($w >= 400) {
						$("#groups .qiSuggestTerm-holder")
								.css("width", "530px");
					}

				}

			}
			function setAuto(x) {
				$auto = x;
				if ($auto) {
					setAutoWidth($gCount);
				}
			}
			// public methods are added to this
			// ie7Compat();
			this.add = add;
			this.setAuto = setAuto;
			this.clearAll = clearAll;

		});

	};

	$.fn.qiSuggestTerm.defaults = {
		resizable : {},
		className : 'bit',
		separator : ',',
		startInput : true,
		hideEmpty : true,
		noRemove : false, // set to true if no remove boxes
		suggestURL : '',
		snapShotId : '',
		snapShotText : '',
		// auto
		autocomplete : {
			opacity : 1,
			maxresults : 10,
			minchars : 1
		}
	};

})(jQuery);