(function($) {
	$.fn.qiSuggestCondition = function(options) {

		return this.each(function() {
			// member vars

			var $count = 0;
			var $current = undefined; // currently focused element
			var $bitsCondition = {};
			var $bitsConditionArray = [];
			var $opts = {
				className : 'bit'
			};
			var $dispDName = ''
			var $maininput = undefined;
			var $unitsInput = undefined;
			var $element = undefined; // this element
			// auto
			var $autoholder = undefined;
			var $autoresults = undefined;
			var $resultsshown = undefined;
			var $data = [];
			var $removeBoxesArray = [];
			var $dosearch = false;
			var $autocurrent = undefined;
			var $autoenter = false;
			var $lastinput = undefined;
			var $countBox = 0;
			var $s = 0;
			var operatorType = 0;
			var $conditionIdArray = [];
			var temp;
			var $statName = "";
			var $statTerm = undefined;
			var $lhsTerm = undefined;
			var $formatObject = null;
			var $unitObject = null;
			// control variables
			var $currentState = "lhs-start";
			var $dateType = "Date";
			var $opCount = 0;
			var $selectedDateFormat = 0;
			var $autoSelected = false;
			var $unitArray = new Array();
			var $formatArray = new Array();
			var fromXml = false;
			var timer;
			var jsonRequestObj=null;
			onloadFalg=false;
			enterPressed=false;
			var $operatorList = [{
				name : "BETWEEN",
				displayName : "between",
				type : "OPERATOR"
			}, {
				name : "IN",
				displayName : "in",
				type : "OPERATOR"
			}, {
				name : "EQUALS",
				displayName : "equal to",
				type : "OPERATOR"
			}, {
				name : "NOT_EQUALS",
				displayName : "not equal to",
				type : "OPERATOR"
			}, {
				name : "GREATER_THAN",
				displayName : "greater than",
				type : "OPERATOR"
			}, {
				name : "LESS_THAN",
				displayName : "less than",
				type : "OPERATOR"
			}];
			// var $dateSelectionList = [ { name: "1", displayName: "Year",
			// type: "DS" },{ name: "2", displayName: "Qtr/Year", type: "DS" },{
			// name: "3", displayName: "Month/Year", type: "OPERATOR" },{ name:
			// "4", displayName: "Day/Month/Year", type: "DS" }];
			// var $dateSelectionArray = ["Select
			// DateFormat","Year","Qtr/Year","Year/Month","Month/Day/Year"];
			var $dateSelectionArray = [{
				name : "Year",
				displayName : 'Year',
				type : 'Year'
			}, {
				name : "Qtr/Year",
				displayName : 'Qtr/Year',
				type : 'Qtr/Year'
			}, {
				name : "Year/Month",
				displayName : 'Year/Month',
				type : 'Year/Month'
			}, {
				name : "Month/Day/Year",
				displayName : 'Month/Day/Year',
				type : 'Month/Day/Year'
			}];
			var months = {
				January : '1',
				February : '2',
				March : '3',
				April : '4',
				May : '5',
				June : '6',
				July : '7',
				August : '8',
				September : '9',
				October : '10',
				November : '11',
				December : '12',
				january : '1',
				february : '2',
				march : '3',
				april : '4',
				may : '5',
				june : '6',
				july : '7',
				august : '8',
				september : '9',
				october : '10',
				november : '11',
				december : '12',
				jan : '1',
				feb : '2',
				mar : '3',
				apr : '4',
				aug : '8',
				sep : '9',
				oct : '10',
				nov : '11',
				dec : '12',
				1 : '1',
				2 : '2',
				3 : '3',
				4 : '4',
				5 : '5',
				6 : '6',
				7 : '7',
				8 : '8',
				9 : '9',
				10 : '10',
				11 : '11',
				12 : '12',
				Jan : '1',
				Feb : '2',
				Mar : '3',
				Apr : '4',
				Aug : '8',
				Sep : '9',
				Oct : '10',
				Nov : '11',
				Dec : '12',
				01 : '1',
				02 : '2',
				03 : '3',
				04 : '4',
				05 : '5',
				06 : '6',
				07 : '7',
				08 : '8',
				09 : '9'
			};
			var $preDiv = $('<span name="preDiv"></span>');
			var $postDiv = $('<span name="postDiv"></span>');
			var $messageDiv = $('<div></div>');
			var $unitsDiv = $('<span></span>');
			var $dateSelection = undefined;
			$(this).data("dataArray", $bitsConditionArray);
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
					//$(document).trigger('blur');
				});
			}

			function resetState(status) {
				$currentState = status.state;
				operatorType = status.opType;
				$opCount = status.opValue;
				$selectedDateFormat = status.selectedDF;
			}
			function getConversionData() {
				$formatArray.length = 0;
				$unitArray.length = 0;
				if ($lhsTerm.hasInstances == false) {
					$.getJSON("querySuggest/qiConversion.action", {
						displayType : $lhsTerm.datatype
					}, function(data) {
					if(data){
						if (data.dataFormats) {

							$.each(data.dataFormats, function(i, dataFormats) {
								$formatArrayAsso = {};
								$formatArrayAsso["id"] = dataFormats.conversionId
										+ "";
								/*if ($lhsTerm.datatype == 'Date') {
									$formatArrayAsso["displayName"] = dataFormats.name;
								} else {
									$formatArrayAsso["displayName"] = dataFormats.displayName;
								}*/
								$formatArrayAsso["displayName"] = dataFormats.displayName;
								$formatArrayAsso["name"] = dataFormats.name;
								$formatArrayAsso["datType"] = dataFormats.datatype;
								$formatArray.push($formatArrayAsso);

							});
						}
						if (data.units) {
							$.each(data.units, function(i, units) {
								$unitArrayAsso = {};
								$unitArrayAsso = {};
								$unitArrayAsso["id"] = units.conversionId + "";
								$unitArrayAsso["displayName"] = units.displayName;
								$unitArrayAsso["name"] = units.name;
								$unitArrayAsso["datType"] = units.datatype;
								$unitArray.push($unitArrayAsso);
							});
						}
						prepareUnitsDiv();
						}
					});
					
				} else {
					$formatObject = null;
					$unitObject = null;
				}
				
			}
			function showConditionButton() {
				$element.find(".conditionSpan").show();
				$("#clearCondition").show();
				$element.find(".clrConditionSpan").show();
			}
			function hideConditionButton() {
				$element.find(".conditionSpan").hide();
				$element.find(".clrConditionSpan").hide();
			}
			function update(text, el) {
				var status = {
					state : '',
					opType : 0,
					opValue : 0,
					selectedDF : 0
				};
				status.state = $currentState;
				status.opType = operatorType;
				status.opValue = $opCount;
				status.selectedDF = $selectedDateFormat;
				el.data('state', status);
				if ($currentState == "lhs-start") {
					if (text["type"] == "STAT") {
						$currentState = "lhs-stat"
						$statTerm = text;
					} else {
						$lhsTerm = text;
						$currentState = "Operator";
						getConversionData();
						createUnitInput();
						$unitsDiv.empty();
						$unitsDiv.show();
						
					}
				} else if ($currentState == "lhs-stat") {
					$lhsTerm = text;
					$currentState = "Operator";
				} else if ($currentState == "Operator") {
					$currentState = "rhs";
					operatorType = 3;
					if (text.name == "BETWEEN") {
						operatorType = 1;
					} else if (text.name == "IN") {
						operatorType = 2;
					}
					showConditionButton()
				}

				/*
				 * if ($currentState == "rhs") { if($lhsTerm.datatype ==
				 * $dateType ) { if($selectedDateFormat == 0 ) {
				 * $maininput.data('input').hide(); $dateSelection.show(); }else {
				 * $maininput.data('input').val($dateSelectionArray[$selectedDateFormat]).select().focus(); } } }
				 */
				if (operatorType == 1) {
					$opCount++;
					if ($opCount > 2) {
						$maininput.data('input').hide();
					}
				} else if (operatorType == 2) {
					$opCount++;
				} else if (operatorType == 3) {
					$opCount++;
					if ($opCount > 1) {
						$maininput.data('input').hide();
					}
				}
				setHeight();
			}

			function add(text, html) {
				if (text.type == 'OPERATOR') {
					$.each($operatorList, function(i, operator) {
						if (text.name == operator.name) {
							text.displayName = operator.displayName;
						}
					});
				}
				// alert(text.type);

				var id = $opts.className + '-' + $count++;
				$conditionIdArray.push($countBox + "$#$" + ($count - 1));
				// added on may 12 2009 check for text value
				if (text.displayName == '' || text.displayName == 'value') {
					$maininput.data('input').val('value').select().focus();
					showConditionButton();
					return false;
				}
				// added on may 12 2009 check for text value///

				var el = createBox(text ? text.displayName : html, $maininput);
				$removeBoxesArray.push(el);
				el.attr('id', id).bind('click', function(e) {
					e.stopPropagation();
					focus(el);
				}).insertBefore($maininput);

				$bitsCondition[id] = text;
				update(text, el);
				createTemplateBox(el);
				prepareTemplate();
				setHeight();
				
			}
			function handleValue() {
				addToken = false;
				//alert("in handle");
				if ($lhsTerm.datatype == 'Date') {
					if ($maininput.data('input').val() != 'value') {
						validateDate();
						if ($messageDiv.text() == '') {
							addToken = true;
						}
					} else {
						addToken = true;
						$messageDiv.empty();
					}

				} else if ($lhsTerm.datatype == 'Currency'
						|| $lhsTerm.datatype == 'Number') {
					var inputVal = $maininput.data('input').val();
					// /////////added
					
					if ($maininput.data('input').val() != 'value') {

						if ($messageDiv.text() == '') {
							if ($autoSelected) {
								addToken = true;
								$autoSelected = false;
							}
						}
					} else {
						addToken = true;
						$messageDiv.empty();
					}
					// /////////added
					if (inputVal == parseInt(inputVal)
							&& parseInt(inputVal) > 0) {
						addToken = true;
					}else{
						$inValue2.hide();
					}
				} else if($lhsTerm.hasInstances){
					var inputVal = $maininput.data('input').val();
					if ($maininput.data('input').val() == 'value') {
						addToken = true;
					}
					if(!addToken){
						$inValue2.hide();
						dispMessage('Please select valid entry');
					}
				}
				else {
					addToken = true;
				}
				//alert("in handle2"+addToken);
				if (addToken) {
					// if($maininput.data('input').val()!='value'){
					autoAddString($maininput.data('input').val());
					$autocurrent = false;
					$autoenter = true;
					// }
				} else {
					$maininput.data('input').select();
					$maininput.data('input').focus();
					showConditionButton();
					return false;
				}
				return true;
			}
			
			function handleValue2onAddCondition(){
			$autoSelected = false;
							if (($maininput.data('input').val() != 'Value 1')
									&& ($maininput.data('input').val() != 'value' && $maininput
											.data('input').val() != '')) {
		
								if (!handleValue()) {
									showConditionButton();
									return false;
								}
								//$opCount++;
								$postDiv.empty().addClass($opts.className + '-box');
								$autoholder.css('left', $maininput.data('input')
										.position().left
										+ 'px'); // added
								$autoholder.css('top', $maininput.data('input')
										.position().top
										+ 15 + 'px');
								// e.stopPropagation();
							} else {
								//alert("Please enter first value")
								$maininput.data('input').val('value').select().focus();
								return false;
							}
			}
			function addCondition(x, f, u) {
				//hideConditionButton();
				fromXml = false;
				if (x) {

					if (f.type == 'FORMAT') {
						// $formatArray.length=0;
						$formatObject = {};
						$formatObject["id"] = f.id + "";
						$formatObject["displayName"] = f.displayName;
						$formatObject["name"] = f.name;
						$formatObject["type"] = 'FORMAT';
						// $formatArray.push($formatArrayAsso);
						// $formatObject = $formatArray[0];
					}
					if (u.type == 'UNIT') {
						$unitObject = {};
						$unitObject["id"] = u.id + "";
						$unitObject["displayName"] = u.displayName;
						$unitObject["name"] = u.name;
						$unitObject["type"] = 'UNIT';

					}

					fromXml = true;
					$autoSelected = true;
					$unitsDiv.empty();
					$unitsDiv.hide();
				}

				if ($maininput.data('input').val() == 'value'
						&& operatorType != 2 && operatorType != 3
						&& $currentState != 'Format') {
					//$maininput.data('input').val("Enter Metrics").select();
				}
				
				if ($currentState == 'rhs' &&  operatorType == 1 && $opCount ==1){
						//alert("in new");
						/////////////////////////
								handleValue2onAddCondition();
						/////////////////////////
					}
						if (($currentState == 'rhs' && $autoSelected)
						|| ($currentState == 'rhs' && !$autoSelected && $maininput.data('input').val() != '')
						||($maininput.data('input').css("display")=="none")|| (($currentState == 'Format' && $autoSelected))) {
					$autoSelected = false;
					//alert("$lhsTerm.hasInstances::"+$lhsTerm.hasInstances);
					if(($currentState == 'rhs' && !$autoSelected && $maininput.data('input').val() != '' && $maininput.data('input').val() != 'value' )){$opCount++;}
					//alert("count::"+$opCount);
					
					var inputVal = $maininput.data('input').val();
					if ((operatorType <= 1 && $opCount < 3)
							|| (operatorType <= 2 && $opCount < 2 && (inputVal != '' && inputVal == 'value'))
							|| (operatorType <= 3 && $opCount < 2 && (inputVal == 'value' || inputVal == ''))) {
						
					//	alert("1");
						autoHide();
						$maininput.data('input').val('value');
						$maininput.data('input').select();
						$maininput.data('input').focus();
						showConditionButton();
						return false;
					} else {
					
						if($maininput.data('input').css("display")!="none"){
							//alert("2b");
						if (!handleValue()) {
							//alert("2a");
							showConditionButton();
							if (!x) {
							//	alert("3");
								return false;
							}
						}
						}else{$("#addCondition").focus();}
					}//alert("4");
					if (operatorType >= 1) {
						clearRemoveBox();
						$maininput.data('input').css('width', '150px');
						// $bitsConditionArray.pop();
						if ($formatObject != null) {
							$bitsCondition.formatObject = $formatObject;
						}
						if ($unitObject != null) {
							$bitsCondition.unitObject = $unitObject;
						}
						//alert("adding");
						$bitsConditionArray.push($bitsCondition);
						$bitsCondition = {};
						generateSnapShot();
						$formatObject = null;
						$unitObject = null;
						focus($maininput);
						$currentState = "lhs-start";
						operatorType = 0;
						$opCount = 0;
						$count = 0;
						$selectedDateFormat = 0;
						prepareTemplate();
						$maininput.data('input').show();
						$maininput.data('input').val('Enter Metrics').select()
								.focus();
						$dateSelection.hide();
						$dateSelection.attr("selectedIndex", 0);
						hideConditionButton();
						setHeight();
					}
				} else {
					var currState = '';
					if ($currentState == 'lhs-start') {
						currState = 'Enter Metrics'
					} else if ($currentState == 'rhs') {
						currState = 'value'
					} else {
						currState = 'Operator'
					}
					$maininput.data('input').val(currState);
					$maininput.data('input').select();
					$maininput.data('input').focus();
					showConditionButton();
					$autoSelected = false;
					return false;

					/*
					 * if((operatorType <= 1 && $opCount< 3) ||(operatorType <=
					 * 2 && $opCount< 2) ||(operatorType <= 3 && $opCount<2)) {
					 * alert("Invalid Data") clearCondition(); }
					 */

				}
				setHeight();
				if (x) {
					$messageDiv.empty();
					hideConditionButton();
				}
			}
			function clearCondition() {
				hideConditionButton();
				operatorType = 0;
				$messageDiv.empty();
				clearRemoveBox();
				$maininput.data('input').css('width', '100px');
				// $bitsConditionArray.pop();
				// $bitsConditionArray.push($bitsCondition);
				$bitsConditionArray.length = 0;
				$bitsCondition = {};
				$formatObject = null;
				$unitObject = null;
				// generateSnapShot();
				focus($maininput);
				$currentState = "lhs-start";
				operatorType = 0;
				$opCount = 0;
				$count = 0;
				$selectedDateFormat = 0;
				prepareTemplate();
				$maininput.data('input').show();
				$maininput.data('input').val('Enter Metrics').select().focus();
				$dateSelection.hide();
				$dateSelection.attr("selectedIndex", 0);
				setHeight();
			}
			function hideSelect() {
				$dateSelection.hide();
			}
			function deleteCondition(no) {
				$bitsConditionArray.splice(no, 1);
				generateSnapShot();
				//clearCondition();
			}
			function clearAll() {
				$bitsConditionArray.length = 0;
				generateSnapShot();

			}
			function clear(el) {
				if (el.data('type') == 'box') {
					el.trigger('boxDispose');
				}
				el.remove();
			}
			function getColId(bitid) {
				colid = "";
				for (var i = 0; i < $conditionIdArray.length; i++) {
					var obj = $conditionIdArray[i].substring(4);
					if (obj == bitid) {
						colid = $conditionIdArray[i].substring(0, 1);
					}
				}
				return colid;
			}
			function dispose(el) {
				var bitid = el.attr('id').substring(4);
				var colid = getColId(bitid);
				var deletedItem = $bitsCondition[el.attr('id')];
				removeLinkElement(el);
				delete $bitsCondition[el.attr('id')];
				resetState(el.data('state'));
				for(var j=bitid;j<$count;j++){					
					delete $bitsCondition['bit-'+j];
				}
				$count--
				//el.prev(".startCamma").remove();
				if ($currentState == 'lhs-start') {
					clearCondition();
				}
				if ($currentState == 'Operator') {
					hideConditionButton();
				}
				if ($current == el) {
					focus(el.next());
				}
				
				if (el.data('type') == 'box') {
					$dateSelection.attr("selectedIndex", 0);
					el.trigger('boxDispose');
				}
				// delete the chain below
				var removeIndex = -1;
				for (var i = 0; i < $removeBoxesArray.length; ++i) {
					if ($removeBoxesArray[i] == el) {
						removeIndex = i;
						removeComma(removeIndex,$removeBoxesArray.length);
						break;
					}
				}
				removeLength = $removeBoxesArray.length
				for (var i = removeIndex; i < removeLength; ++i) {
					clear($removeBoxesArray.pop());
				}

				// clear are elements that are removed

				$maininput.data('input').show();
				prepareTemplate();
			}
			function removeComma(ind,lth){
				l=$element.find("li.startCamma:0").length;
				x=lth-ind;
				for(i=ind;i<lth;i++){
					$element.find("li.startCamma:0").eq(l-x).remove();
				
				}
				
			}
			function removeLinkElement(el) {
				var linkEL = el.data("link-el");
				if (linkEL != null) {
					linkEL.remove();
				}
			}
			function clearRemoveBox() {
				for (var i = 0; i < $removeBoxesArray.length; ++i) {
					clear($removeBoxesArray[i]);
				}
			}

			function bitLength($b) {
				cnt = 0;
				for (var key in $b) {
					cnt++;
				}
				return cnt;
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
			/*function blur() {
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
			}*/

			function createBox(text, focusElementOnRemove) {
				var li = $('<li>' + text + '</li>').addClass($opts.className
						+ '-box').data('type', 'box');

				if (!$opts.noRemove) {
					li.bind('mouseenter', function(e) {
						$(this).css("position", "relative");
						li.addClass('bit-hover');
						li.append($('<a href="#" class="closebutton"></a>')
								.bind('click', function(e) {
									e.stopPropagation();
									if (!$current) {
										focus(focusElementOnRemove);
									}
									dispose(li);
									$dateSelection.hide();
									$messageDiv.empty();
								})).data('text', text);
					}).bind('mouseleave', function(e) {
						$(this).css("position", "static");
						li.removeClass('bit-hover');
						$('a.closebutton').remove();
					});
				}
				// bindCustomEvents(li);
				return li;
			}

			function createTemplateBox(el) {
				if (operatorType == 1 && $opCount == 2) {
					var li = $('<li> And</li>').addClass($opts.className
							+ '-box').insertBefore($maininput);
					el.data('link-el', li);
					$removeBoxesArray.push(li);
				}
				if (operatorType == 2 && $opCount == 1) {
					var li = $('<li>(</li>').addClass('startBrace')
							.insertBefore($maininput);
					el.data('link-el', li);
					$removeBoxesArray.push(li);
				}
				if (operatorType == 2 && $opCount > 1) {
					var li = $('<li>,</li>').addClass('startCamma')
							.insertBefore($maininput);
				}
			}

			function selectDateFormat() {
				$selectedDateFormat = $dateSelection.val();
				$maininput.data('input').show();
				$dateSelection.hide();
				$maininput.data('input').show();
				$maininput.data('input')
						.val($dateSelectionArray[$selectedDateFormat]).select()
						.focus();
			}

			function handleEnterKey() {
				$messageDiv.empty();
				$messageDiv.addClass("messageDiv");
				if ($resultsshown && $autocurrent
						&& $autocurrent.data('result')) {
					autoAdd($autocurrent);
					if (operatorType == 1 || operatorType == 2) {
						$autoSelected = true;
					}

					$autocurrent = false;
					$autoenter = true;
				}
				
					 else if ($currentState == 'rhs'){ 
					 if (!handleValue()) {
							showConditionButton();
							
								return false;
					 }else{
					 	
						 if($maininput.data('input').css("display")=="none"){ 
						 $("#addCondition").focus();
						 
						 }
					 }
						}
					
			}

			function validateDate() {
				// dateType=$dateSelectionArray[$selectedDateFormat]
				typeValue = $maininput.data('input').val();
				if ($formatObject.displayName == 'Year') {
					checkValidYear(typeValue);
				} else if ($formatObject.displayName == 'Qtr Year') {
					checkkValidQtrYear(typeValue);
				} else if ($formatObject.displayName == 'Year Month') {
					checkValidYearMonth(typeValue);
				} else if ($formatObject.displayName == 'Year Month Date') {
					checkValidFullDate(typeValue);
				}
			}
			function checkValidYear(year) {
				currYear = new Date().getFullYear()
				if (year.length > 4 || year.length < 4
						|| parseInt(year) != year) {
					dispMessage("invalid year");
				}
				if (year > currYear) {
					dispMessage("year should be less than  or equal to current year"
							+ (currYear));
				}
				if (year.length == 4 && parseInt(year) <= currYear
						&& parseInt(year) != 0 && parseInt(year) == year)
					$messageDiv.empty();
			}
			function checkValidMonth(strMonth) {
				if (strMonth.length < 1 || (strMonth < 0 && strMonth > 12)) {
					dispMessage("invalid Month:");
				}
				if (Number(strMonth) > 0 ) {
					month = Number(strMonth);
					month = months[month];
				} else {
					month = months[strMonth];
				}
				// monthValue = months[strMonth]
				if (strMonth != null && typeof month == "undefined") {
					dispMessage("invalid Month:");
				}
			}
			function checkkValidQtrYear(qtrYear) {

				var pos1 = qtrYear.indexOf("/");
				var qtr = qtrYear.substring(0, pos1)
				var strYear = qtrYear.substring(pos1 + 1);
				if (qtr == "Q" && strYear == "YYYY") {
					dispMessage("invalid quarter and year:");
				}
				if (qtr != "Q" && strYear == "YYYY") {
					dispMessage("in valid year:");

				}
				if ((qtr == "Q" && strYear != "YYYY") || qtr != Number(qtr)
						|| Number(qtr) > 4 || qtr.length < 1
						|| Number(qtr) < 1) {
					dispMessage("invalid Quarter:");
				}
				if (pos1 == -1) {
					dispMessage("The quarter format should be : Q/YYYY");
				}
				if (qtr.length == 1 && Number(qtr) >= 1 && Number(qtr) < 5) {
					checkValidYear(strYear);
				}
			}
			function checkValidYearMonth(monthYear) {
				var pos1 = monthYear.indexOf("/");
				var strYear = monthYear.substring(0, pos1)
				var strMonth = monthYear.substring(pos1 + 1);

				checkValidYear(strYear);
				checkValidMonth(strMonth);
				if (pos1 == -1) {
					dispMessage("The year month format should be : yyyy/MM");
					return false;
				}
				$maininput.data('input').val(Number(strYear) + "/"
						+ months[month]);
			}
			function checkValidFullDate(dtStr) {
				if (!isDate(dtStr)) {
					return false;
				}
				return true

			}
			function isDate(dtStr) {
				flag = true;
				currYear = new Date().getFullYear()
				var daysInMonth = DaysArray(12)
				var pos1 = dtStr.indexOf("/")
				var pos2 = dtStr.indexOf("/", pos1 + 1)
				var strYear = dtStr.substring(0, pos1)
				var strMonth = dtStr.substring(pos1 + 1, pos2)
				var strDay = dtStr.substring(pos2 + 1)
				var strYr = strYear

				month = Number(strMonth)
				day = Number(strDay)
				year = Number(strYr)
				if (pos1 == -1 || pos2 == -1) {
					dispMessage("The date format should be : yyyy/mm/dd");
					return false;
				}
				checkValidYear(strYear);

				if (strMonth.length < 1 || (strMonth < 0 && strMonth > 12)) {
					dispMessage("invalid Month:");
				}
				if (Number(strMonth) > 0) {
					month = Number(strMonth);					
				} else {
					month = strMonth
				}
				if (strMonth != null && typeof months[month] == "undefined") {
					dispMessage("invalid Month:");
				}
				if (strDay.length < 1 || day < 1 || day > 31
						|| (month == 2 && day > daysInFebruary(year))
						|| day > daysInMonth[parseInt(months[month])]
						|| day != parseInt(day)) {
					dispMessage("invalid day:");

				}
				/*
				 * if (dtStr.indexOf("/",pos2+1)!=-1 ||
				 * isInteger(stripCharsInBag(dtStr, "/"))==false) {
				 * dispMessage("Please enter a valid date") flag=false; }
				 */
				// $messageDiv.empty();
				$maininput.data('input').val(year + "/" + months[month] + "/"
						+ day);

			}
			function daysInFebruary(year) {
				return (((year % 4 == 0) && ((!(year % 100 == 0)) || (year
						% 400 == 0))) ? 29 : 28);
			}
			function DaysArray(n) {
				for (var i = 1; i <= n; i++) {
					this[i] = 31
					if (i == 4 || i == 6 || i == 9 || i == 11) {
						this[i] = 30
					}
					if (i == 2) {
						this[i] = 29
					}
				}
				return this
			}
			function isInteger(s) {
				var i;
				for (i = 0; i < s.length; i++) {
					// Check that current character is a number or not.
					var c = s.charAt(i);
					if (((c < "0") || (c > "9")))
						return false;
				}
				// All characters are numbers.
				return true;
			}
			function stripCharsInBag(s, bag) {
				var i;
				var returnString = "";
				// Search through string's characters one by one.
				// If character is not in bag, append to returnString.
				for (i = 0; i < s.length; i++) {
					var c = s.charAt(i);
					if (bag.indexOf(c) == -1)
						returnString += c;
				}
				return returnString;
			}
			function dispMessage(msg) {
				$messageDiv.empty();
				$messageDiv.append(msg);
				$inValue2.hide();
				$maininput.data('input').val($dispDName).select().focus();
			}
			function displayDname() {
				if ($lhsTerm.datatype == 'Date') {
					if ($formatObject.displayName == 'Year') {
						$dispDName = 'yyyy';
					} else if ($formatObject.displayName == 'Qtr Year') {
						$dispDName = 'qtr/yyyy'
					} else if ($formatObject.displayName == 'Year Month') {
						$dispDName = 'yyyy/MM'
					} else if ($formatObject.displayName == 'Year Month Date') {
						$dispDName = 'yyyy/mm/dd'
					}
					$maininput.data('input').val($dispDName);
				}
				$("#addCondition").focus();
			}
			function createUnitInput() {
				$unitsInput = $('<input type="text"></input>').autocomplete(
						$formatArray, {
							inputClass : "qiCondition_units_ac_input",
							resultsClass : "qiCondition_units_ac_results",
							loadingClass : "qiCondition_units_ac_loading",
							multiple : true,
							mustMatch : false,
							formatMatch : false,
							selectFirst : true,
							cacheLength : 0,
							formatMatch : function(row, i, max) {
								return row.displayName;
							},
							formatResult : function(row) {
								return row.displayName;
							},
							autoFill : false,
							width : 200
						});
				$unitsInput.bind('keypress', function(e) {
					if (e.keyCode == 13 ) {
						//$inValue2.hide();
						if($formatObject){
						$unitsInput.val($formatObject.displayName).select()
								.focus();
						}
						displayDname();
						return false;
					}
				});

			}
			function showAutoSuggest(){
				$maininput.data('input').val('a');
					autoShow("a");
			}
			function createInput() {
				//alert("::::"+tabIn);
				$("#clearCondition").hide();
				var li = $('<li class="' + $opts.className + '-input"></li>');

				$dateSelection = $('<select></select>');
				$.each($dateSelectionArray, function(index, value) {
					$dateSelection.append($('<option value="' + index + '">'
							+ value + '</option>'))
				});

				$dateSelection.bind("change", selectDateFormat)
						.addClass("dateCombo");

				var input = $('<input type="text" ></input>').attr("id","maininp");

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
				}).bind('blur', function() {
					
					timer = setTimeout(autoHide, 3000);
					return;
				}).bind('focus', function(e) {
						// focus(li);

						return false;
					}).bind('blur', function(e) {
					if ($current == li) {
						// $current = undefined;
						//blur();
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
								handleEnterKey();
								break;
							case 27 : // esc
							 
								if (!$resultsshown && $current
										&& $current.data('input')) {
									setTimeout(function() {
										setDefaultText();
									}, 0);
								} else {
									e.stopPropagation();
									//autoHide();
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
						if(e.keyCode == 8 || e.which == 8){							
							$inValue2.hide();
						}
						return e.keyCode != 13; // skip IE default behavior
						// (beeps)
					}
				}).bind('keypress', function(e) {
					if ($autoenter && !$.browser.msie) {
						e.stopPropagation();
						$autoenter = false;
					}
					if ($autoSelected) {
						$inValue2.hide();
					} else if(!$autoSelected && e.keyCode!=13){
						//$inValue2.show();
					}
					if(e.keyCode == 8 || e.which == 8){
						$inValue2.hide();
					}
				}).bind('keyup', function(e) {
					
					$autoSelected = false;
					$autoholder.css('left',
							$maininput.data('input').position().left + 'px'); // added
					// on 10 feb 2009 by raj
					$autoholder
							.css('top', $maininput.data('input').position().top
									+ 15 + 'px'); // added
					// on 10 feb 2009 by raj
					if ($autoholder) {
						if ($dosearch) {
							autoShow(input.val());
						}
					}
				});

				bindCustomEvents(input);
				return li.data('type', 'input').data('input', input)
						.append($preDiv).append($dateSelection).append(input)
						.append($postDiv).append($unitsDiv);
			}
			function prepareTemplate() {
				$preDiv.empty();
				$postDiv.empty();
				// $span=$("<span>,</span>");

				$v2 = $('<a>Value 2</a>').bind('click', function(e) {
					handleValue2onAddCondition();
				}).addClass('link_val2');
				$inValue2 = $('<a>Value</a>').bind('click', function() {
					$ht = $("#showAll").height() + 10;
					$("#showAllContainer .divBorderWhite").css("height",
							$ht + "px");
					if ($maininput.data('input').val() != 'value') {
						if (!handleValue()) {
							showConditionButton();
							return false;
						}
					} else {
						//alert("Please enter first value")
						$maininput.data('input').val('value').select().focus();
						return false;
					}
				}).addClass('link_val2');
				if ($currentState == "lhs-start" || $currentState == "lhs-stat") {
					$maininput.data('input').width(80);
					$maininput.data('input').val('Enter Metrics').select()
							.focus();
					$postDiv
							.append('&nbsp;<span class="suggestTemplate">Operator</span>&nbsp;<span class="suggestTemplate">Value(s)</span>')
							.addClass($opts.className + '-box');
					$unitsDiv.empty();
					$element.find("li.startCamma").remove();
					
				} else if ($currentState == "Operator") {
					$maininput.data('input').width(50);
					$maininput.data('input').val('Operator').select();
					$preDiv.empty();
					$postDiv
							.empty()
							.append('&nbsp;<span class="suggestTemplate">Value(s)</span>')
							.addClass($opts.className + '-box');
							
				} else if ($currentState == "rhs") {
					$maininput.data('input').width(65);
					$maininput.data('input').val('value').select().focus();
					$preDiv.empty();
					$postDiv.empty();
				}
				if (operatorType == 1) {
					if ($opCount == 1) {
						$postDiv
								.empty()
								.append('<span class="suggestTemplate"> And</span>')
								.append($v2).addClass($opts.className + '-box');
						$maininput.data('input').val('Value 1').select()
								.focus();
					} else if ($opCount >= 1) {
						$preDiv.empty();
						$postDiv.empty();
					}
				}
				if (operatorType == 2) {
					$preDiv.empty();
					$inValue2.hide();
					$bracketspan=$("<span> ) </span>");
					$postDiv.empty().append($inValue2).append($bracketspan)
							.addClass($opts.className + '-box');// .append('in');
				}
				if (operatorType == 3) {
					$preDiv.empty();
				}

			}
			function prepareUnitsDiv() {
				// possible template
				// in thousands of US Dollars
				// in month year(s)
				$formatObject = null;
				$unitObject = null;
				if ($unitArray.length == 0 && $formatArray.length == 0) {
					return;
				}
				$unitsDiv.append(" in ");
				if ($formatArray.length > 0) {
					if (!$formatObject) {
						if (!$lhsTerm.dataFromat) {
							$formatObject = $formatArray[0];
						} else {
							for (var i = 0; i < $formatArray.length; i++) {
								if ($formatArray[i].name.toLowerCase() == $lhsTerm.dataFromat
										.toLowerCase()) {
									$formatObject = $formatArray[i];
									break;
								}
							}
							if (!$formatObject) {
								$formatObject = $formatArray[0];
							}
						}
					}
					$formatSpan = $('<span></span>').appendTo($unitsDiv);
					$formatLink = $('<a></a>')
							.append($formatObject.displayName).bind('click',
									function() {
										$unitsInput.show();
										$unitsInput
												.val($formatObject.displayName)
												.setOptions({
													data : $formatArray
												}).result(function(event, data,
														formatted) {
													$unitsInput.hide();
													$formatObject = data;
													$formatLink
															.text($formatObject.displayName);
													displayDname();
													$formatLink.show();
													$formatSpan
															.remove($unitsInput);
												});
										$formatLink.hide();
										if ($unitObject) {
											$unitLink.show();
										}
										$formatSpan.append($unitsInput);
										displayDname();
									}).addClass('link_units').attr("alt",
									"Click to change").attr("title",
									"Click to change").appendTo($formatSpan);
					$formatLink.bind('click', function(e) {
						$inValue2.hide();
						if($formatObject){
						$unitsInput.val($formatObject.displayName).select()
								.focus();
						}
					});
				}
				if ($unitArray.length != 0) {
					$of=$("<span> of </span>");
					if($lhsTerm.datatype!="Percentage"){
						$unitsDiv.append($of);
					}
					
					
					if (!$unitObject) {
						if (!$lhsTerm.unitType) {
							$unitObject = $unitArray[0];
						} else {
							for (var i = 0; i < $unitArray.length; i++) {
								if ($unitArray[i].name.toLowerCase() == $lhsTerm.unitType
										.toLowerCase()) {
									$unitObject = $unitArray[i];
									break;
								}
							}
							if (!$unitObject) {
								$unitObject = $unitArray[0];
							}
						}
					}
					$unitSpan = $('<span></span>').appendTo($unitsDiv);
					$unitLink = $('<a></a>').append($unitObject.displayName)
							.bind('click', function() {
								$unitsInput.show();
								$unitsInput.val($unitObject.displayName)
										.setOptions({
											data : $unitArray
										}).result(function(event, data,
												formatted) {
											$unitsInput.hide();
											$unitObject = data;
											$unitLink
													.text($unitObject.displayName);
											$unitLink.show();
											$unitSpan.remove($unitsInput);
										});
										if($formatObject){
											$formatLink.show();
										}
								$unitLink.hide();
								$unitSpan.append($unitsInput);
							}).addClass('link_units').attr("alt",
									"Click to change").attr("title",
									"Click to change").appendTo($unitSpan);
					$unitLink.bind('click', function(e) {
						$unitsInput.val($unitObject.displayName).select()
								.focus();
					});
				}
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

					if ($currentState == "lhs-start") {
						if (jsonRequestObj) {
							jsonRequestObj.abort();
							}
							showLoader();
						jsonRequestObj=$.getJSON(o.suggestLHSURL, {
							search : search
						}, populateAutoSuggest);
					} else if ($currentState == "lhs-stat") {
						
								showLoader();
						$.getJSON(o.suggestLHSURL, {
							search : search,
							stat : true,
							statName : $statTerm.name
						}, populateAutoSuggest);
					} else if ($currentState == "Operator") {
						showLoader();
						searchOperator(search);
						// populateAutoSuggest($operatorList);
					} else if ($currentState == "rhs") {
						/*
						 * if($lhsTerm.dataType == $dateType ) {
						 * populateAutoSuggest($dateSelectionList); } else {
						 */
						$.getJSON(o.suggestRHSURL, {
							search : search,
							businessTerm : $lhsTerm.name
						}, populateAutoSuggest);
						// }
					} else if ($currentState == "Format") {
						// populateAutoSuggest($dateSelectionArray);

						populateAutoSuggest($formatArray);
						// populateAutoSuggest($operatorList);
					}
				}

				return this;
			}
			function searchOperator(search) {
				var $operatorListArray = [];
				var $operatorListArray1 = []
				var $opKey = {};
				$.each($operatorList, function(data) {
					var opName = this.displayName;
					if (search == opName.substring(0, search.length)
							|| search == opName) {
						$opKey["name"] = this.name;
						$opKey["displayName"] = this.displayName;
						$opKey["type"] = this.type;
						$operatorListArray.push($opKey);
						$opKey = {}
					} else {
						$opKey["name"] = this.name;
						$opKey["displayName"] = this.displayName;
						$opKey["type"] = this.type;
						$operatorListArray1.push($opKey);
						$opKey = {}
					}
				});
				populateAutoSuggest($operatorListArray
						.concat($operatorListArray1));
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
				var j = 0;
				$autoresults.empty();
				$.each(data, function(item) {

					var el = $('<li></li>').bind('mouseenter', function(e) {
						autoFocus($(this));
					}).bind('click', function(e) {
						$messageDiv.empty();
						if (operatorType == 1 || ($currentState == "Format")) {
							$autoSelected = true;
						}
						e.stopPropagation();
						autoAdd($(this));
					}).html(autoHighlight(this.displayName, $maininput
							.data('input').val())).data('result', this);
					$autoresults.append(el);
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
				
				setDefaultText()
				
			}
			
			function setDefaultText(){
				if($maininput.data('input').val()==""){
							if ($currentState == "lhs-stat") {
								$maininput.data('input').val("Enter Metrics").select();
							} else if ($currentState == "Operator") {
								$maininput.data('input').val("Operator").select();
							}else if ($currentState == "rhs") {
								$maininput.data('input').val("value").select();
							}else{
								$maininput.data('input').val("Enter Metrics").select();
							}	
				}
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
				if (el) {
					if (el.data('result')) {
						add(el.data('result'));
						for (var i = 0; i < $data.length; ++i) {
							if ($data[i] == el.data('result')) {
								delete $data[i];
								break;
							}
						}
					}
					autoHide();

					focus($maininput);
					///////////////////////////////
				   if (operatorType == 1) {
				   if ($opCount > 2) {
					$("#addCondition").focus();
				   }
				  }  else if (operatorType == 3) {
				   if ($opCount > 1) {
				   $("#addCondition").focus();
				   }
				  } 
				 /////////////////////////////////////////
					$autoholder.css('left',
							$maininput.data('input').position().left + 'px');

					$autoholder
							.css('top', $maininput.data('input').position().top
									+ 15 + 'px');
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
			function hideButtons(){
				$element.find(".clrConditionSpan").hide();
						$element.find(".conditionSpan").hide();
			}
			// body
			$(this).parent('li').addClass('qiSuggestCondition-input-text');

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
						// clever
						// hack?
						setTimeout(function() {
							autoHide();
						}, 3000);
					}
				}
			}, $.fn.qiSuggestCondition.defaults, options);

			// support metadata plugin (element specific options)
			var o = $.meta ? $.extend({}, $opts, $(this).data()) : $opts;

			$element = $(this);
			// $(this).hide();

			$maininput = createInput();
			//alert($maininput.html());
			$maininput.find('input').addClass('maininput');
			$dateSelection.hide();
			prepareTemplate();
			var holder = $('<ul></ul>').addClass('qiSuggestCondition-holder')
					.bind('click', function(e) {
						e.stopPropagation();
						if ($current != $maininput) {
							focus($maininput);
						}
						return false;
					}).append($maininput);
			var clrButton = $('<span id="clrButton" class="clrConditionSpan"><img hspace="3" vspace="2" tabindex="-1" src="images/clearConditionNew.gif" align="left" id="clearCondition" alt="Clear Condition" title="Clear Condition" /></span>')
					.bind('click', function(e) {
						clearCondition();
					}).bind($.browser.msie ? 'keydown' : 'keypress',
							function(e) {
								if (e.keyCode == 13) { // 8=BACKSPACE
									clearCondition();
									return false;
								}
							});
			var conBurtton = $('<span id="conditionButton" class="conditionSpan"><img hspace="3" tabindex="0" src="images/addConditionNew.gif" align="left" id="addCondition" alt="Add Condition" title="Add Condition" /></span>')
					.bind('click', function(e) {
						addCondition();
						// hideButtons();
						
					}).bind('keydown',
							function(e) {
								if (e.keyCode == 13) { // 8=BACKSPACE
									addCondition();
									// hideButtons();
									return false;
								}
							});
			//	holder.append("<div style='width:100%;float:left;'><img src='images/1pix.gif' width='1' height='1' /></div>");
				$buttons=$("<div style='display:inline;float:left;width:150;'></div>");
				holder.append($buttons);
				$buttons.append(conBurtton);
			    $buttons.append(clrButton);
			

			$(this).append($messageDiv).append(holder);
			$autoholder = $('<div id ="#' + this.id + '-auto"></div>');
			$maininput.append($autoholder);
			if ($autoholder) {
				$autoholder.addClass('qiSuggestCondition-auto').css('opacity',
						$opts.autocomplete.opacity).hide();

				$autoresults = $('<ul></ul>').mouseout(function() {
					timer = setTimeout(autoHide, 3000);
				}).mouseover(function() {
					clearTimeout(timer);
				});
				$autoholder.append($autoresults);
				$autoholder.children($autoresults);
			}

			makeResizable($maininput);
			setEvents();

			function generateSnapShot() {
				spacer = "&nbsp;";
				var wordCount = 0;
				$snapShot.empty();
				$maininputSnapShot.empty();
				if ($bitsConditionArray.length > 0) {
					$snapShot.append("<div  style='width:100%;'>"
							+ o.snapShotText + "</div>");
					// $snapShot.append('<br>');
				}

				$.each($bitsConditionArray, function(index, bitsCondition) {
					// $snapShot.append('<table width=100% ><tr><td>');
					$link_bulleted = $("<span style='float:left;padding-left:10px;padding-top:3px;'>&#8226;</span>")
							.bind('click', {
								id : index
							}, function(e) {
								e.stopPropagation();
									// deleteCondition(e.data.id);
								});
					$link = $("<span style='float:left;padding-left:10px;'><a href='#' tabIndex='-1'> <img src='images/deleteButt.gif' valign='bottom' border='0' alt='Delete' title='Delete'/></a></span>")
							.bind('click', {
								id : index
							}, function(e) {
								e.stopPropagation();
								deleteCondition(e.data.id);
									// $ht=$("#showAllContainer
									// .divBorderWhite").height()-25;
									// $("#showAllContainer
									// .divBorderWhite").css("height",$ht+"px")
								});
					$snapShot.append($link_bulleted);
					$maininputSnapShot.append($link);
					spanStart = "<span style='float:left;line-height:20px;vertical-align:middle;width:500px;height:auto;white-space:normal'>&nbsp;";
					spanEnd = "</span>";
					var content = "";
					tokenCount = 0;
					totalTokens = 0;
					betweenCheck = false;
					inCheck = false;
					for (key in bitsCondition) {
						totalTokens++;

					}
					// alert("totalTokens::"+totalTokens);

					/*
					 * if(fromXml){ if($lhsTerm.datatype=='Currency'){
					 * totalTokens=totalTokens-2; }else if ($lhsTerm.datatype ==
					 * 'Number' || $lhsTerm.datatype == 'Date') {
					 * totalTokens=totalTokens-1; }
					 *  }
					 */
					// totalTokens=5;
					// alert("fromXml::"+fromXml);
					for (key in bitsCondition) {
						tokenCount++;

						content += bitsCondition[key]["displayName"] + "&nbsp;";
						if (bitsCondition[key]["displayName"] == "between") {
							betweenCheck = true;
						}
						if (bitsCondition[key]["displayName"] == "in") {
							inCheck = true;
						}
						if (bitsCondition.unitObject) {
							
							
							if ((tokenCount >= 3)
									&& (tokenCount < totalTokens - 2)) {
								if (betweenCheck) {
									content += "and" + "&nbsp;";
								} else {
									content += "," + "&nbsp;";
								}
							}

							if (tokenCount == totalTokens - 2) {
								if(bitsCondition.unitObject.displayName!="Ratio"){
								content += "in" + "&nbsp;";
								}else{
									if (betweenCheck) {
									content += "and" + "&nbsp;";
									} else {
										if (inCheck) {
										content += "," + "&nbsp;";
										}
									}
								}
							} else if (tokenCount == totalTokens - 1) {
								
								if(bitsCondition.unitObject.displayName!="Ratio"){
								content += "of" + "&nbsp;";
								}else{
									content += "in" + "&nbsp;";
								}
							}
						} else if (bitsCondition.formatObject) {

							if ((tokenCount >= 3)
									&& (tokenCount < totalTokens - 1)) {
								if (betweenCheck) {
									content += "and" + "&nbsp;";

								} else  {
									content += "," + "&nbsp;";
								}
							}
							if (tokenCount == totalTokens - 1) {
								content += "in" + "&nbsp;";
							}
						} else if ((tokenCount >= 3)
								&& (tokenCount < totalTokens)) {
							if (betweenCheck) {
								content += "and" + "&nbsp;";
							} else {
								content += "," + "&nbsp;";
							}
						}

					}
					$snapShot.append(spanStart + "&nbsp;" + content + "&nbsp;"
							+ spanEnd);
					$maininputSnapShot.append(spanStart + "&nbsp;" + content
							+ "&nbsp;" + spanEnd);
					wordCount += 1;
					if (wordCount == 1) {
						$snapShot
								.append("<div style='width:100%;float:left;'><img src='images/1pix.gif' width='150' height='1' /></div>");
						$maininputSnapShot
								.append("<div style='width:100%;float:left;'><img src='images/1pix.gif' width='150' height='1' /></div>");
						wordCount = 0;

					}
						// $snapShot.append('</td></tr></table>');
				});
				showHideQuerySnapShot();
				$maininput.data('input').show();
				//$maininput.data('input').val("Enter Metrics").select().focus();
				if (o.snapShotId == 'ss_condition') {
					//$("#condition").append($maininputSnapShot);
					// $("#advOpt_condition
					// .qiSuggestCondition-holder").css("border-bottom","none");
				} else {
					$("#advOpt_cohort_condition").append($maininputSnapShot);
					// $("#advOpt_cohort_condition
					// .qiSuggestCondition-holder").css("border-bottom","none");
				}

				$("#" + o.snapShotId).append($snapShot);
				setHeight();

			}

			function setHeight() {
				$ht = $("#showAll").height();
				$("#showAllContainer .divBorderWhite")
						.height($ht);
			}
			var $actionName = o.suggestLHSURL;
			var $snapShot = $("<div ></div>").attr("id", "ss_cond");// $("#" +
			// o.snapShotId);
			var $maininputSnapShot = $("<div></div>").attr("id", "conditionSS");
			// public methods are added to this
			this.add = add;
			this.addCondition = addCondition;
			this.clearCondition = clearCondition;
			this.hideSelect = hideSelect;
			this.deleteCondition = deleteCondition;
			this.clearAll = clearAll;
			this.showAutoSuggest=showAutoSuggest;
			this.qiConditionGenerateXML = function(o) {
				var stat = "";
				statDisplayName = "";
				var valueCount = 0;
				var XML = new XMLWriter();
				XML.BeginNode("conditions");
				$.each($bitsConditionArray, function(index, conditionTerm) {
					XML.BeginNode("condition");
					lhsHasInstance = false;
					rhsStart = true;
					for (key in conditionTerm) {
						if (conditionTerm[key]["type"] == "STAT") {
							stat = conditionTerm[key]["name"];
							statDisplayName = conditionTerm[key]["displayName"];
						} else if (conditionTerm[key]["type"] == "CONCEPT") {
							XML.BeginNode("lhsBusinessTerm");
							XML.BeginNode("term");
							XML.Node("name", conditionTerm[key]["name"]);
							XML.Node("displayName",
									conditionTerm[key]["displayName"]);
							XML.Node("type", conditionTerm[key]["type"]);
							lhsHasInstance = conditionTerm[key]["hasInstances"];
							XML.EndNode();
							XML.Node("hasInstances",
									conditionTerm[key]["hasInstances"]
											.toString());
							if (conditionTerm[key]["datatype"]) {
								XML.Node("datatype",
										conditionTerm[key]["datatype"]);
							} else {
								// TODO-JT- Currenty put some dummy datatype.
								XML.Node("datatype", 'String');
							}
							if (stat != "") {
								XML.Node("stat", stat);
								XML.Node("statDisplayName", statDisplayName);
							}
							XML.EndNode();
							if (conditionTerm.unitObject) {
								// XML.Node("conversionId",conditionTerm.unitObject.id);
								XML.BeginNode("qiConversion");
								XML.Node("name", conditionTerm.unitObject.name);
								XML.Node("displayName",
										conditionTerm.unitObject.displayName);
								XML.Node("conversionId",
										conditionTerm.unitObject.id);
								XML.EndNode();
							}
						} else if (conditionTerm[key]["type"] == "OPERATOR") {
							XML.Node("operator", conditionTerm[key]["name"]);
						} else if (conditionTerm[key]["type"] == "VALUE"
								|| conditionTerm[key]["type"] == "INSTANCE"
								|| conditionTerm[key]["type"] == "INSTANCE_PROFILE") {
							if (rhsStart) {
								XML.BeginNode("rhsValue");
								if (lhsHasInstance) {
									XML.BeginNode("terms");
								} else {
									XML.BeginNode("values");
								}
								rhsStart = false;
							}
							if (conditionTerm[key]["type"] == "INSTANCE_PROFILE") {
								XML.BeginNode("term");
								XML.Node("name", conditionTerm[key]["name"]);
								XML.Node("displayName",
										conditionTerm[key]["displayName"]);
								XML.Node("type", 'INSTANCE_PROFILE');
								XML.EndNode();
							} else if (conditionTerm[key]["type"] == "INSTANCE") {
								XML.BeginNode("term");
								XML.Node("name", conditionTerm[key]["name"]);
								XML.Node("displayName",
										conditionTerm[key]["displayName"]);
								XML.Node("type", 'INSTANCE');
								XML.EndNode();
							} else {
								XML.BeginNode("qiValue");
								XML.Node("value", conditionTerm[key]["name"]);
								if (conditionTerm.formatObject) {
									// XML.Node("conversionId",conditionTerm.formatObject.id);
									XML.BeginNode("qiConversion");
									XML.Node("name",
											conditionTerm.formatObject.name);
									XML
											.Node(
													"displayName",
													conditionTerm.formatObject.displayName);
									XML.Node("conversionId",
											conditionTerm.formatObject.id);
									XML.EndNode();
								}
								XML.EndNode();
							}

						}
					}
					XML.EndNode();
					XML.EndNode();
					XML.EndNode();
				});
				XML.EndNode();
				return XML.ToString();
			}
		});

	};

	$.fn.qiSuggestCondition.defaults = {
		resizable : {},
		className : 'bit',
		separator : ',',
		startInput : true,
		hideEmpty : true,
		noRemove : false, // set to true if no remove boxes
		suggestLHSURL : 'querySuggest/suggestWhereLHS!suggestWhereLHS.action',
		suggestRHSURL : 'querySuggest/suggestWhererRHS!suggestBTAndValuesForWhereRHS.action',
		snapShotId : 'ss_metrics1',
		snapShotText : 'dummy Text',
		// auto
		autocomplete : {
			opacity : 1,
			maxresults : 10,
			minchars : 1
		}
	};

})(jQuery);