(function($) {
	$.fn.qiSuggestCondition = function(options) {
		var $picker=options.datePicker;
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
			var $formatInput = undefined;
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
			var jsonRequestObj = null;
			var $clrButton = undefined;
			var $conButton = undefined;
			var hasInstanceVar=false;
			onloadFalg = false;
			enterPressed = false;
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
			var delConditionFlag = false;
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
			var $preDiv = $('<li name="preDiv"></li>');
			var $postDiv = $('<li name="postDiv" style="padding-right:0px"></li>');
			var $messageDiv = $('<div></div>');
			var $unitsDiv = $('<li style="padding-top:1px;float:left;" class="units-box"></li>');
			var $defaultFrmLink = $('<a href="#"></a>');
			var $dateSelection = undefined;
			$seperatorFlag=false;
			$seperator=$("<li style='width:501px;height:1px;'><img src='images/main/1pix.gif' id='sep1' width='1' height='1' border='0' /></li>");
			$seperator1=$("<li style='width:502px;height:1px;'><img src='images/main/1pix.gif' id='sep2'  width='1' height='1' border='0' /></li>");
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
						// $(document).trigger('blur');
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
					hasInstanceVar=false;
					$.getJSON("querySuggest/getSampleValAndConvDetail.action", {
						businessTerm : $lhsTerm.name,
						displayType : $lhsTerm.datatype
					}, function(data) {
						if (data) {						
							if (data.qiConversion) {
							  if (data.qiConversion.dataFormats) {

								$.each(data.qiConversion.dataFormats, function(i,
										dataFormats) {
									$formatArrayAsso = {};
									$formatArrayAsso["id"] = dataFormats.conversionId
											+ "";
					
									$formatArrayAsso["displayName"] = dataFormats.displayName;
									$formatArrayAsso["name"] = dataFormats.name;
									$formatArrayAsso["datType"] = dataFormats.datatype;
									$formatArray.push($formatArrayAsso);

								});
							  }
							}
							if (data.qiConversion) {
							  if (data.qiConversion.units) {
								$.each(data.qiConversion.units, function(i, units) {
									$unitArrayAsso = {};
									$unitArrayAsso = {};
									$unitArrayAsso["id"] = units.conversionId
											+ "";
									$unitArrayAsso["displayName"] = units.displayName;
									$unitArrayAsso["name"] = units.name;
									$unitArrayAsso["datType"] = units.datatype;
									$unitArray.push($unitArrayAsso);
								});
							}
							if(data.sampleValue){	
								$("#"+$opts.sampleElementId).empty();
							  	$("#"+$opts.sampleElementId).append("( Sample Values : e.g. "+data.sampleValue+" ) ");
							}														
							prepareFormatUnitLink();
						  }
						}
					});

				} else {
					$formatObject = null;
					$unitObject = null;
					hasInstanceVar=true;
				}

			}
			function prepareFormatUnitLink(){
				$formatInput.hide();
				$unitsInput.hide();
				$unitsDiv.empty();				
				$defaultFrmLink.empty();
				var $cFormat='';
				if ($lhsTerm.datatype == 'CURRENCY') {					
					if(!$formatObject){						
						if(!$lhsTerm.dataFromat){
						$cFormat="Dollar";
						$unitsDiv.append($defaultFrmLink
										.append('in ').append($cFormat));
						}else{
							$cFormat=$lhsTerm.dataFromat.toLowerCase();	
							if (!$lhsTerm.unitType) {
							$cUnits="Dollar";	
							}else{
								$cUnits=$lhsTerm.unitType.toLowerCase()
							}
							$unitsDiv.append($defaultFrmLink.append('in&nbsp;')
									.append($cFormat)
									.append('&nbsp;of&nbsp;')
									.append($cUnits));
						}
					}else{
						if ($formatObject.displayName == 'Ones') {							
							$unitsDiv.append($defaultFrmLink.append('in ')
									.append($unitObject.displayName));
						} else {							
							$unitsDiv.append($defaultFrmLink.append('in&nbsp;')
									.append($formatObject.displayName)
									.append('&nbsp;of&nbsp;')
									.append($unitObject.displayName));
						}
					}					
					$defaultFrmLink.bind('click', prepareUnitsDiv)					
					$unitsDiv.show();								
				} else if($lhsTerm.datatype =='NUMBER'){					
						if($formatObject){
								$cFormat=$formatObject.displayName;
							}else{
								
								
								for (var i = 0; i < $formatArray.length; i++) {
									if ($formatArray[i].name.toLowerCase() == $lhsTerm.dataFromat
											.toLowerCase()) {										
											$cFormat = $formatArray[i].displayName;									
										break;
									}
								}
								
							
							}
								$unitsDiv.append($defaultFrmLink
										.append('in ').append($cFormat));
								$defaultFrmLink.bind('click', prepareUnitsDiv)
								$unitsDiv.show();								
				}
				else {
					prepareUnitsDiv();
				}
				setTimeout(function(){$conButton.focus();},200); //added 
				return false;
			}
			function showConditionButton() {
				$conButton.show();
				$clrButton.show();
			}
			function hideConditionButton() {
				$conButton.hide();
				$clrButton.hide();
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
						$currentState = "operator";
						if($lhsTerm.datatype !='DATE'){
						 // getConversionData();
						  getConversionData();
						  createUnitInput();
						  $unitsDiv.empty();						
						  $unitsDiv.show();
						}
					}
				} else if ($currentState == "lhs-stat") {
					$lhsTerm = text;
					$currentState = "operator";
					if($lhsTerm.datatype !='DATE'){
						 // getConversionData();
						  getConversionData();
						  createUnitInput();
						  $unitsDiv.empty();						
						  $unitsDiv.show();
						}
					$seperator.remove(); 
				} else if ($currentState == "operator") {
					$currentState = "rhs";
					operatorType = 3;
					if (text.name == "BETWEEN") {
						operatorType = 1;
					} else if (text.name == "IN") {
						operatorType = 2;
					}
					showConditionButton();
					$seperator.remove(); 
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
					
					/*if($opCount==1){
							$("<div style='width:500px;'><img src='images/main/1pix.gif' width='1' height='1' /></div>").insertBefore($conButton);
					}*/
				} else if (operatorType == 2) {
					$opCount++;
					
					//if($opCount==1){
							
					//}
					
						
						
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
				
				checkLayout();

			}
			function checkLayout(){
				//alert("1a");
				if ($currentState == 'rhs' ) {
					if(!$seperatorFlag){
					$seperator.remove(); }
				    conTop=Number($conButton.position().top);
					clrTop=Number($clrButton.position().top);
					
					conLeft=Number($conButton.position().left);
					unitsLeft=Number($unitsDiv.position().left);
					
					unitsTop=Number($unitsDiv.position().top);
					
					inputLeft=$maininput.find('li input#maininp').position().left;
					inputTop=$maininput.find('li input#maininp').position().top;
					showAllDivLeft=$("#showAll").position().left;
					qiSuggestConditionHolderWidth=$element.width();
					inputWidth=$maininput.find('li input#maininp').width();
				//	alert("1");
					$("#left").empty();
					//$("#top").html(":"+y);
					if( (clrTop-conTop) >0  ){$seperator.remove(); $seperator.insertBefore($conButton);}
					if(inputLeft-showAllDivLeft >=qiSuggestConditionHolderWidth-inputWidth){ $seperator.remove();$seperator.insertBefore($maininput.find('li').attr('name','preDiv').parent('li'));}
					
  					var k1=0; var k2=0; countk=0; 
					$.each( $element.find('li.startCamma,li.bit-box,li.units-box,li.condition-box'),function(i,el){
													k2=k1;
														k1=$(this).position().left;
														//$("#left").append(":"+(k1-k2));
														if((k1-k2)<=0  & (k1-k2)>=-80 ){ //alert("z1:");
														$seperator.remove(); 
														$seperator.insertBefore($(this));}
														
														});
					}
					
			}
			function handleValue() {
				addToken = false;
				// alert("in handle");
				if ($lhsTerm.datatype == 'DATE') {
					if ($maininput.data('input').val() != 'value' ) {
						//validateDate();
						if ($messageDiv.text() == '') {
							addToken = true;
						}
					} else { 
						addToken = true;
						$messageDiv.empty();
					}
					
					if($maininput.data('input').val() == 'operator'){addToken = false;} //Bug 1627 - Invalid text 'operator' included in 'query snapshot' via advanced search (edit) 

				} else if ($lhsTerm.datatype == 'CURRENCY'
						|| $lhsTerm.datatype == 'NUMBER' || $lhsTerm.datatype=="PERCENTAGE") {
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
					if ((inputVal==Number(inputVal) && Number(inputVal)>=0) || (inputVal==parseFloat(inputVal) && parseFloat(inputVal) >= 0)) {
						addToken = true;
					} else {
						$inValue2.hide();
					}
				} else if ($lhsTerm.hasInstances) {					
					//var inputVal = $maininput.data('input').val();
					//alert(inputVal);
					/*if ($maininput.data('input').val() == 'value') {
						addToken = true;
					}*/
					if (!addToken) {
						$inValue2.hide();
						dispMessage('Please select valid entry');
					}
				} else { 
					if ($maininput.data('input').val() != "value 1") {
						if ($maininput.data('input').val() != "value 2") {
							addToken = true;
						}
					}
				}
				// alert("in handle2"+addToken);
				if (addToken) {  
					// if($maininput.data('input').val()!='value'){
					autoAddString($maininput.data('input').val());
					$autocurrent = false;
					$autoenter = true;
					if (operatorType == 1 && $opCount == 3) {
						$conButton.focus().find("img").removeClass("imgNoBorder").addClass("imgBorder");
					}
					if (operatorType == 3 && $opCount == 2) {
						$conButton.focus().find("img").removeClass("imgNoBorder").addClass("imgBorder");
					}
					// }
				} else {
					$maininput.data('input').select();
					$maininput.data('input').focus();
					showConditionButton();
					return false;
				}
				return true;
			}

			function handleValue2onAddCondition() {
				$autoSelected = false;
				if (($maininput.data('input').val() != 'value 1')
						&& ($maininput.data('input').val() != 'value' && $maininput
								.data('input').val() != '')) {

					if (!handleValue()) {
						showConditionButton();
						return false;
					}
					// $opCount++;
					$postDiv.empty();
					$autoholder.css('left',
							$maininput.data('input').position().left + 'px'); // added
					$autoholder
							.css('top', $maininput.data('input').position().top
									+ 15 + 'px');
					// e.stopPropagation();
				} else {
					// alert("Please enter first value")
					$maininput.data('input').val('value').select().focus();
					return false;
				}
			}
			function addCondition(x, f, u) {
				// hideConditionButton();
				$conButton.find("img").removeClass("imgBorder").addClass("imgNoBorder");	
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
					$("#"+$opts.sampleElementId).hide();
					fromXml = true;
					$autoSelected = true;
					$unitsDiv.empty();
					$unitsDiv.hide();
					$defaultFrmLink.hide();
				}

				if ($maininput.data('input').val() == 'value'
						&& operatorType != 2 && operatorType != 3
						&& $currentState != 'Format') {
					// $maininput.data('input').val("Enter Metrics").select();
				}

				if ($currentState == 'rhs' && operatorType == 1
						&& $opCount == 1) {
					// alert("in new");
					// ///////////////////////
					handleValue2onAddCondition();
					// ///////////////////////
				}
				if (($currentState == 'rhs' && $autoSelected)
						|| ($currentState == 'rhs' && !$autoSelected && $maininput
								.data('input').val() != '')
						|| ($maininput.data('input').css("display") == "none")
						|| (($currentState == 'Format' && $autoSelected))) {
					$autoSelected = false;

					// alert("$lhsTerm.hasInstances::"+$lhsTerm.hasInstances);
					if (($currentState == 'rhs' && !$autoSelected
							&& $maininput.data('input').val() != '' && $maininput
							.data('input').val() != 'value')) {
						$opCount++;
					}
					// alert("count::"+$opCount);

					var inputVal = $maininput.data('input').val();
					if ((operatorType <= 1 && $opCount < 3)
							|| (operatorType <= 2 && $opCount < 2 && (inputVal != '' && inputVal == 'value'))
							|| (operatorType <= 3 && $opCount < 2 && (inputVal == 'value' || inputVal == ''))) {

						// alert("1");
						autoHide();
						$maininput.data('input').val('value');
						$maininput.data('input').select();
						$maininput.data('input').focus();
						showConditionButton();
						return false;
					} else {

						if ($maininput.data('input').css("display") != "none") {
							// alert("2b");
							if (!handleValue()) {
								// alert("2a");
								showConditionButton();
								if (!x) {
									// alert("3");
									return false;
								}
							}
						} else {
							$conButton.focus().find("img").removeClass("imgNoBorder").addClass("imgBorder");
						}
					}// alert("4");
					if (operatorType >= 1) {
						clearRemoveBox();
						$maininput.data('input').css('width', '150px');
						// $bitsConditionArray.pop();
						if ($lhsTerm.datatype != 'DATE' && $lhsTerm.datatype != 'DATETIME') {
						prepareUnitsDiv();
						}
						if ($formatObject != null) {
							$bitsCondition.formatObject = $formatObject;
						}
						if ($unitObject != null) {
							$bitsCondition.unitObject = $unitObject;
						}
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
						$dispDName='';
						$selectedDateFormat = 0;
						prepareTemplate();
						$maininput.data('input').show();
						
						$dateSelection.hide();
						$dateSelection.attr("selectedIndex", 0);
						hideConditionButton();
						setHeight();
						$removeBoxesArray.length=0;
						$maininput.data('input').val('Enter Metrics').select()
								.focus();
								$seperator.remove(); 
						$conButton.find("img").removeClass("imgBorder").addClass("imgNoBorder");
						$seperator.remove();
					$seperator1.remove();
					}
				} else {
					var currState = '';
					if ($currentState == 'lhs-start') {
						currState = 'Enter Metrics'
					} else if ($currentState == 'rhs') {
						currState = 'value'
					} else {
						currState = 'operator'
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
					$("#"+$opts.sampleElementId).empty();
					$messageDiv.empty();
					hideConditionButton();					
				}

				$(".ui-daterangepickercontain").hide();

			}
			
			function clearSpecificCondition() {
				hideConditionButton();
				operatorType = 0;
				$messageDiv.empty();
				clearRemoveBox();
				$dispDName='';
				$maininput.data('input').css('width', '100px');			
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
				$seperator.remove(); 
			}
			
			function clearCondition() {			
				clearSpecificCondition();				
				$bitsConditionArray.length = 0;
				$bitsCondition = {};
			}
			function hideSelect() {
				$dateSelection.hide();
			}
			function deleteCondition(no) {
				$bitsConditionArray.splice(no, 1);
				generateSnapShot();
				delConditionFlag = false;
				// clearCondition();
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
				$conButton.find("img").removeClass("imgBorder").addClass("imgNoBorder");
				var bitid = el.attr('id').substring(4);
				var colid = getColId(bitid);
				var deletedItem = $bitsCondition[el.attr('id')];				
				removeLinkElement(el);
				delete $bitsCondition[el.attr('id')];
				resetState(el.data('state'));
				for (var j = bitid; j < $count; j++) {
					delete $bitsCondition['bit-' + j];
				}
				$count--
				// el.prev(".startCamma").remove();
				if ($currentState == 'lhs-start') {
					clearSpecificCondition();
				}
				if ($currentState == 'operator') {
					$element.find("li.startCamma").remove();
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
						removeComma(removeIndex, $removeBoxesArray.length);
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
			function removeComma(ind, lth) {
				l = $element.find("li.startCamma:0").length;
				x = lth - ind;
				for (i = ind; i < lth; i++) {
					$element.find("li.startCamma:0").eq(l - x).remove();

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

				blur();

				el.addClass($opts.className + '-' + el.data('type') + '-focus');

				if (el.data('type') == 'input') {
					var input = el.data('input');
					input.trigger('inputFocus');
					setTimeout(function() {
									if(input[0]){
										try{
						               input[0].focus();
										}catch(err){}
									}
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

				else if ($currentState == 'rhs') {
					if (!handleValue()) {
						showConditionButton();

						return false;
					} else {

						if ($maininput.data('input').css("display") == "none") {
							$conButton.focus().find("img").removeClass("imgNoBorder").addClass("imgBorder");

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
				if (Number(strMonth) > 0) {
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
						|| Number(qtr) > 4 || qtr.length < 1 || Number(qtr) < 1) {
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
				$messageDiv.css('color','red');
				$messageDiv.append(msg);
				$inValue2.hide();
				if($dispDName){
					$maininput.data('input').val($dispDName).select().focus();
				}else{
					$maininput.data('input').val('value').select().focus();
				}
				setHeight();
			}
			function displayDname() {
				if ($lhsTerm.datatype == 'Date') {
					if (!$formatObject) {
						$formatObject = $formatArray[0];
					}
					if ($formatObject.displayName == 'Year') {
						$dispDName = 'yyyy';
					} else if ($formatObject.displayName == 'Qtr Year') {
						$dispDName = 'qtr/yyyy'
					} else if ($formatObject.displayName == 'Year Month') {
						$dispDName = 'yyyy/MM'
					} else if ($formatObject.displayName == 'Year Month Date') {
						$dispDName = 'yyyy/mm/dd'
					}
					$maininput.data('input').val($dispDName).select().focus();
				}
				$conButton.focus().find("img").removeClass("imgNoBorder").addClass("imgBorder");
			}
			function createUnitInput() {
			$formatInput = $('<input type="text"></input>').autocomplete(
						$formatArray, {
							inputClass : "qiCondition_units_ac_input",
							resultsClass : "qiCondition_units_ac_results",
							loadingClass : "qiCondition_units_ac_loading",
							multiple : true,
							mustMatch : false,
							formatMatch : false,
							matchContains: true,
							selectFirst : true,
							cacheLength : 0,
							formatMatch : function(row, i, max) {
								return row.displayName;
							},
							formatResult : function(row) {
								return row.displayName;
							},
							autoFill : false,
							width : 120
						}).css('width', '65px');
				$unitsInput = $('<input type="text"></input>').autocomplete(
						$unitArray, {
							inputClass : "qiCondition_units_ac_input",
							resultsClass : "qiCondition_units_ac_results",
							loadingClass : "qiCondition_units_ac_loading",
							multiple : true,
							mustMatch : false,
							formatMatch : false,
							matchContains: true,
							selectFirst : true,
							cacheLength : 0,
							formatMatch : function(row, i, max) {
								return row.displayName;
							},
							formatResult : function(row) {
								return row.displayName;
							},
							autoFill : false,
							width : 120
						}).css('width', '50px');
				$formatInput.bind('keydown', function(e) {
					if (e.keyCode == 13) {
						// $inValue2.hide();
						if ($unitObject) {
							$unitsInput.focus().select();
									return false;
						}
						
						
						return false;
					}
					if(e.keyCode==9){
						
						if ($unitObject) {
							$unitsInput.focus().select();
						}
						return false;
					}
				}).bind('blur',function(){
					$formatInput.val($formatObject.displayName);
				});
				$unitsInput.bind('keydown', function(e) {
													 
					if (e.keyCode == 13) {
						// $inValue2.hide();						
						prepareFormatUnitLink();
						$conButton.focus().find("img").removeClass("imgNoBorder").addClass("imgBorder");	
						e.stopPropagation();
						return false;
					}
					
					if(e.keyCode==9){
						prepareFormatUnitLink();
						$conButton.focus().find("img").removeClass("imgNoBorder").addClass("imgBorder");	
						e.stopPropagation();
			            return false;
			
						
						
					}
				}).bind('blur',function(){
					$unitsInput.val($unitObject.displayName);
				});
				
			}
			
			function showAutoSuggest() {
				$maininput.data('input').val('a');
				autoShow("a");
			}
			function createInput() {
				$("#clearCondition").hide();
				var li = $('<li class="' + $opts.className + '-input"></li>');

				$dateSelection = $('<select></select>');
				$.each($dateSelectionArray, function(index, value) {
					$dateSelection.append($('<option value="' + index + '">'
							+ value + '</option>'))
				});

				$dateSelection.bind("change", selectDateFormat)
						.addClass("dateCombo");

				var input = $('<input type="text" ></input>').attr("id",
						"maininp").attr("Qualifier","");

				input.bind('click', function(e) { 
					$(".qiSuggestTerm-auto").hide();
					$(".qiSuggestCondition-auto").hide();
					
					if($currentState=="rhs"){
						if (input.val() == "value" || input.val() == "value 1") {
							input.val("");
						}
	
						if($lhsTerm.datatype == 'DATE'){
							input.select();							
							showDatePicker();
								
						}
					}else{
						input.val('');
					autoShow("a");
					$autoholder.css('left',
							$maininput.data('input').position().left + 'px'); // added
					$autoholder
							.css('top', $maininput.data('input').position().top
									+ 15 + 'px');
					}
					e.stopPropagation();
				}).bind('blur', function() {

					timer = setTimeout(autoHide, 3000);
					return;
				}).bind('focus', function(e) {
					// focus(li);
					$seperatorFlag=false;
					return false;
				}).bind('blur', function(e) {
					if ($current == li) {
						// $current = undefined;
						// blur();
					}
					if($currentState=="rhs"){
						if (input.val() == "" ) {
						input.val('value').select().focus();
						}
					}
				}).bind('keydown', function(e) {
					$(this).data('lastValue', this.value).data('lastCaret',
							getCaretPosition(input));
					if ($autoholder) {
						$dosearch = false;
						switch (e.keyCode) {
							case 9 :
								if (operatorType == 2 && $opCount > 1) {
									if ($maininput.data('input').val() == "value"
											|| $maininput.data('input').val() == "") {
										$conButton.focus().find("img").removeClass("imgNoBorder").addClass("imgBorder");
										return false;
									}
								}
								if (($resultsshown && $autocurrent && $autocurrent
										.data('result'))
										|| $currentState == "rhs") {
									handleEnterKey();
									return false;
								} else {
									return true;
								}
								break;

							case 38 :
								return autoMove('up');
							case 40 :
							
							if($currentState=="rhs"){
								if($lhsTerm.datatype == 'DATE'){
									input.select();							
									showDatePicker();
										
								}
							}
								return autoMove('down');
							case 13 : // enter
								if (operatorType == 2 && $opCount > 1) {
									if ($maininput.data('input').val() == "value"
											|| $maininput.data('input').val() == "") {
										$conButton.focus().find("img").removeClass("imgNoBorder").addClass("imgBorder");
										return false;
									}
								}
								if (($resultsshown && $autocurrent && $autocurrent
										.data('result'))
										|| $currentState == "rhs") {
									handleEnterKey();
									return false;
								} else {
									//return true;
								}
								break;
							case 27 : // esc

								if (!$resultsshown && $current
										&& $current.data('input')) {
									setTimeout(function() {
										setDefaultText();
									}, 0);
								} else {
									e.stopPropagation();
									 autoHide();
									return false;// skip IE default behavior
									// (clear field)
								}
								if($currentState=="rhs"){
								if($lhsTerm.datatype != 'DATE'){
									$("div.ui-daterangepickercontain").hide();
										
									}
								}
								
								break;
							default :
								$dosearch = true;
						}

						if ($autoenter && $.browser.msie) {
							e.stopPropagation();
							$autoenter = false;
						}
						if (e.keyCode == 8 || e.which == 8) {
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
					} else if (!$autoSelected && e.keyCode != 13) {
						// $inValue2.show();
					}
					if (e.keyCode == 8 || e.which == 8) {
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
				$inputLi=$("<li></li>").append(input);
				bindCustomEvents(input);
				return li.data('type', 'input').data('input', input)
						.append($preDiv).append($inputLi);
			}
			
			function showDatePicker(){
				$("#currentDiv").val($element.attr("id"));
			$picker.trigger("click");
						$(".ui-daterangepickercontain").css("left",$maininput.data('input').position().left+"px");
						$(".ui-daterangepickercontain").css("top",($maininput.data('input').position().top+25)+"px");
						$(".ui-daterangepickercontain").show();
						//$maininput.data('input').attr("readonly",true);
						var df="MonthYearDate";
						if(df=="MonthYearDate"){
							//$("li.ui-daterangepicker-MonthYearDate a").trigger("click");
							//$("ul.ui-component-content").hide();
						}
						else if(df=="MonthYear"){
							//$("li.ui-daterangepicker-MonthYear a").trigger("click");
							//$("ul.ui-component-content").hide();
						}
						else if(df=="Year"){
							//$("li.ui-daterangepicker-Year a").trigger("click");
							//$("ul.ui-component-content").hide();
						}
						else if(df=="Quarter"){
							//$("li.ui-daterangepicker-Quarter a").trigger("click");
							//$("ul.ui-component-content").hide();
						}	
			}
			function prepareTemplate() {
				$preDiv.empty();
				$postDiv.empty();
				// $span=$("<span>,</span>");

				$v2 = $('<span style="padding-right:10px;text-decoration:underline;color:#3C71A1;">value 2</span>').bind('click', function(e) {
					handleValue2onAddCondition();
				}).addClass('link_blue');
				$inValue2 = $('<span >Value</span>').bind('click', function() {
					$ht = $("#showAll").height() + 10;
					$("#showAllContainer .divBorderWhite").css("height",
							$ht + "px");
					if ($maininput.data('input').val() != 'value') {
						if (!handleValue()) {
							showConditionButton();
							return false;
						}
					} else {
						// alert("Please enter first value")
						$maininput.data('input').val('value').select().focus();
						return false;
					}
				}).addClass('link_blue');
				if ($currentState == "lhs-start" || $currentState == "lhs-stat") {
					$(".ui-daterangepickercontain").hide();
					$maininput.data('input').width(80);
					$("#"+$opts.sampleElementId).empty();
					$maininput.data('input').val('Enter Metrics').select()
							.focus();
					$postDiv
							.append('<span class="suggestTemplate">operator</span>&nbsp;<span class="suggestTemplate">value(s)</span>')
							.addClass($opts.className + '-box');
					$unitsDiv.empty();
					$element.find("li.startCamma").remove();

				} else if ($currentState == "operator") {
					$maininput.data('input').attr("Qualifier","");
					$(".ui-daterangepickercontain").hide();
					$("ul.ui-component-content").show();
					$maininput.data('input').width(50);
					$maininput.data('input').val('operator').select();
					$("#"+$opts.sampleElementId).show();
					$defaultFrmLink.show();
					$preDiv.empty();
					$postDiv
							.empty()
							.append('<span class="suggestTemplate">value(s)</span>')
							.addClass($opts.className + '-box');

				} else if ($currentState == "rhs") {
					
					$conButton.find("img").removeClass("imgBorder").addClass("imgNoBorder");
					if ($lhsTerm.datatype == 'DATE') {	 						
						$maininput.data('input').width(80);
						//displayDname();
						showDatePicker();
					}else{ 
						$maininput.data('input').width(65);
						$maininput.data('input').val('value').select().focus();						
					}
					$preDiv.empty();
					$postDiv.empty();
				}
				if (operatorType == 1) {
					if ($opCount == 1) {
						$postDiv
								.empty()
								.append('<span class="suggestTemplate"> and </span>')
								.append($v2).addClass($opts.className + '-box');
						if ($lhsTerm.datatype == 'DATE') { 
							//displayDname();
							$maininput.data('input').attr("Qualifier","");
							$("ul.ui-component-content").show();
							}else{
								$maininput.data('input').val('value 1').select().focus();
								
							}
					} else if ($opCount >= 1) {
						$preDiv.empty();
						$postDiv.empty();
					}
				}
				if (operatorType == 2) {
					$preDiv.empty();
					$inValue2.hide();
					$bracketspan = $("<div style='float:left;'> ) </div>");
					$postDiv.empty().append($inValue2).append($bracketspan);
							//.addClass($opts.className + '-box');// .append('in');
				}
				if (operatorType == 3) {
					$preDiv.empty();
					$postDiv.empty();
				}

			}
			function prepareUnitsDiv() {
				// possible template
				// in thousands of US Dollars
				// in month year(s)
				//$formatObject = null;
				//$unitObject = null;
				$seperatorFlag=true;
				$conButton.find("img").removeClass("imgBorder").addClass("imgNoBorder");
				if ($unitArray.length == 0 && $formatArray.length == 0) {
					return;
				}
				if ($formatInput.val() != null && $unitsInput.val()!=null) {
					createUnitInput();
				}			
				$defaultFrmLink.empty();
				
				$unitsDiv.addClass('units-box');
				$unitsDiv.append(" in ");
				if ($formatArray.length > 0) {
					if (!$formatObject) {
						if (!$lhsTerm.dataFromat) {							
							if($lhsTerm.datatype == 'NUMBER'){								
								$formatObject = $formatArray[2];						
							}else{								
								$formatObject = $formatArray[0];
							}
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
					//$formatLink = $('<a></a>')
							//.append($formatObject.displayName).bind('click',
							//		function() {
										$formatInput.show();
										$formatInput
												.val($formatObject.displayName)
												.setOptions({
													data : $formatArray
												}).result(function(event, data,
														formatted) {
												if ($unitObject) {											
													//$unitLink.show();
													$unitsInput.show();	
												}else{													
													$unitsInput.hide();
												}
													$formatObject = data;
													//$formatLink.show();
													$formatInput
															.val($formatObject.displayName);
													if($currentState == "rhs" && $lhsTerm.datatype == 'DATE'){
														if(operatorType==1 || operatorType==3){															
															indx=2;															
															$deleteBit=indx;															
															$maininput.data('input').show();
														}else if(operatorType==2){
															indx=3;
															$deleteBit=indx-1
															removeComma(indx, $removeBoxesArray.length);
														}
														$count--;
														removeLength = $removeBoxesArray.length
														for (var i = indx; i < removeLength; ++i) {
															clear($removeBoxesArray.pop());															
														}														
														for (var j = $deleteBit; j < removeLength; j++) {
																delete $bitsCondition['bit-' + j];
														}															
														if(operatorType==1 || operatorType==3){															
															$opCount=1
															$currentState == "rhs";
															prepareTemplate();					
														}
														//displayDname();	
														//prepareFormatUnitLink();
													}else if($lhsTerm.datatype=='NUMBER'){
														prepareFormatUnitLink();
													}
													
													// $formatSpan.remove($formatInput);
												});
										//$formatLink.hide();
										/*if ($unitObject) {											
											//$unitLink.show();
											$unitsInput.show();
										}*/
										$formatSpan.append($formatInput);
										
										//displayDname();
								//	}).addClass('link_units').attr("alt",
								//	"Click to change").attr("title",
								//	"Click to change").appendTo($formatSpan);
					/*$formatLink.bind('click', function(e) {
						$inValue2.hide();
						if ($formatObject) {
							$unitsInput.val($formatObject.displayName).select()
									.focus();
						}
					});*/
					setHeight();
					
				}
				if ($unitArray.length != 0) {
					$of = $("<span> of </span>");
					if ($lhsTerm.datatype != "PERCENTAGE") {
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
					//$unitLink = $('<a></a>').append($unitObject.displayName)
						//	.bind('click', function() {
								$unitsInput.show();
								$unitsInput.val($unitObject.displayName)
										.setOptions({
											data : $unitArray
										}).result(function(event, data,
												formatted) {
											//$unitsInput.hide();
											$unitObject = data;
											//$unitLink.show();
											$unitsInput
													.val($unitObject.displayName);
											// $unitSpan.remove($unitsInput);
											//FormatUnitLink()
											prepareFormatUnitLink();
											$conButton.find("img").removeClass("imgNoBorder").addClass("imgBorder");	
										});
								/*if ($formatObject) {
									$formatLink.show();
								}*/
								//$unitLink.hide();
								$unitSpan.append($unitsInput);
								
							//}).addClass('link_units').attr("alt",
							//		"Click to change").attr("title",
							//		"Click to change").appendTo($unitSpan);
					/*$unitLink.bind('click', function(e) {
						$unitsInput.val($unitObject.displayName).select()
								.focus();
					});*/

				}
				setHeight();
				checkLayout();
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
						$modelId=$("#modelId").val();
						jsonRequestObj = $.getJSON(o.suggestLHSURL, {
							search : search,
							modelId : $modelId
						}, populateAutoSuggest);
					} else if ($currentState == "lhs-stat") {

						showLoader();
						$modelId=$("#modelId").val();
						$.getJSON(o.suggestLHSURL, {
							search : search,
							modelId : $modelId,
							stat : true,
							statName : $statTerm.name
						}, populateAutoSuggest);
					} else if ($currentState == "operator") {
						showLoader();
						searchOperator(search);
						// populateAutoSuggest($operatorList);
					} else if ($currentState == "rhs") {
						/*
						 * if($lhsTerm.dataType == $dateType ) {
						 * populateAutoSuggest($dateSelectionList); } else {
						 */
						 if($lhsTerm.datatype != 'DATE'){
						$modelId=$("#modelId").val();
						$.getJSON(o.suggestRHSURL, {
							search : search,
							businessTerm : $lhsTerm.name,
							modelId : $modelId
						}, populateAutoSuggest);
						// }
						 }
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
						$operatorListArray1=addToOperatorArray($operatorListArray1,this);
					} else {
						$operatorListArray1=addToOperatorArray($operatorListArray1,this);
					}
				});
				
				populateAutoSuggest($operatorListArray
						.concat($operatorListArray1));
			}
			
			function addToOperatorArray($operatorListArray1,x){
				var $opKey = {};
			            $opKey["name"] = x.name;
						$opKey["displayName"] = x.displayName;
						$opKey["type"] = x.type;
						if(!hasInstanceVar || $lhsTerm.datatype =='DATE'){
						    $operatorListArray1.push($opKey);
						 }else{						 	
						   if(x.name!="BETWEEN" && x.name!="GREATER_THAN" && x.name!="LESS_THAN" ){						   	
						      $operatorListArray1.push($opKey);
						    }
						 }
						 $opKey = {}	
				return $operatorListArray1;
			}
			function removeLoader() {
				$maininput.find('input#maininp').removeClass('maininput1');
				$maininput.find('input#maininp').addClass('maininput');
			}
			function showLoader() {
				$maininput.find('input#maininp').removeClass('maininput');
				$maininput.find('input#maininp').addClass('maininput1');
			}
			function populateAutoSuggest(data) {
				if (!data[0]) {
					removeLoader()
				}
				var j = 0;
				$autoresults.empty();
				$.each(data, function(item) {

					var el = $('<li></li>').bind('click', function(e) {
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

			function setDefaultText() {
				if ($maininput.data('input').val() == "") {
					if ($currentState == "lhs-stat") {
						$maininput.data('input').val("Enter Metrics").select();
					} else if ($currentState == "operator") {
						$maininput.data('input').val("operator").select();
					} else if ($currentState == "rhs") {
						$maininput.data('input').val("value").select();
					} else {
						$maininput.data('input').val("Enter Metrics").select();
					}
				}else if ($maininput.data('input').val() == "operator") {
					//setTimeout($maininput.data('input').val("operator").select(),1);
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
					// /////////////////////////////
					if (operatorType == 1) {
						if ($opCount > 2) {
							$conButton.focus().find("img").removeClass("imgNoBorder").addClass("imgBorder");
						}
					} else if (operatorType == 3) {
						if ($opCount > 1) {
							$conButton.focus().find("img").removeClass("imgNoBorder").addClass("imgBorder");
						}
					}
					// ///////////////////////////////////////
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
			function hideButtons() {
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
			// alert($maininput.html());
			
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
			$clrButton = $('<span class="clrConditionSpan"><a tabindex="-1" alt="Clear Condition" title="Clear Condition" style="text-decoration:underline;" >clear</a></span>')
					.bind('click', function(e) {
						clearSpecificCondition();
					}).bind($.browser.msie ? 'keydown' : 'keypress',
							function(e) {
								if (e.keyCode == 13) { // 8=BACKSPACE
									clearSpecificCondition();
									return false;
								}
							});
			$conButton = $('<a href="javascript:;" class="conditionSpan"><img hspace="3" src="images/main/addConditionNew.gif" align="left" alt="Add Condition" title="Add Condition" border="0" class="imgNoBorder" /></a>')
					.bind('click', function(e) {
						addCondition();
							// hideButtons();

						}).bind('keydown', function(e) {
						if (e.keyCode == 13) { // 8=BACKSPACE
							addCondition();
							// hideButtons();
							return false;
						}
						if (e.keyCode == 9) { // 8=BACKSPACE
							addCondition();
							// hideButtons();
							return false;
						}
					});
			//holder
					//.append("<div style='width:100%;float:left;'><img src='images/main/1pix.gif' width='1' height='1' /></div>");
			$buttons1 = $("<li class='condition-box' style='white-space:nowrap;display:inline;'></li>");
			//$buttons2 = $("<li class='condition-box'></li>");
			// $buttons.addClass($opts.className + '-box');
			holder.append($postDiv);
			holder.append($unitsDiv);
			holder.append($buttons1);
			//holder.append($buttons2);
			$buttons1.append($conButton);
			$buttons1.append($clrButton);

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
				var cnt=0;
				$snapShot.empty();
				$snapShot.css('width','670px');
				$maininputSnapShot.empty();
				if ($bitsConditionArray.length > 0) {
					$('#tr_'+o.snapShotId).show();
					$snapShot.append("<div class='c1'  style='width:110px;float:left;'>"
							+ o.snapShotText + "</div>");
					if (o.snapShotId != 'ss_condition') {
					$snapShot.find("div.c1").width(60);
					}
				}else{
					$('#tr_'+o.snapShotId).hide();
				}
               
				$.each($bitsConditionArray, function(index, bitsCondition) {
					 cnt++;								 
						$snapBit=$("<div class='snapBit' style='float:left;' ></div>");
				$snapBit.bind("mouseover",function(){
									$(this).find("img").attr("src","images/main/deleteButtNew.gif");
									}).bind("mouseout",function(){
								    $(this).find("img").attr("src","images/main/deleteButtNewEmpty.gif");
									});
				
					// $snapShot.append('<table width=100% ><tr><td>');
					$link_bulleted = $("<span style='float:left;padding-left:10px;padding-top:3px;'>&#8226;</span>")
							.bind('click', {
								id : index
							}, function(e) {
								e.stopPropagation();
									// deleteCondition(e.data.id);
								});
					$link =  $('<div class="delLink">&nbsp;</div>')
							.bind('click', {
								id : index
							}, function(e) {
								e.stopPropagation();
								delConditionFlag = true;
								deleteCondition(e.data.id);
									// $ht=$("#showAllContainer
									// .divBorderWhite").height()-25;
									// $("#showAllContainer
									// .divBorderWhite").css("height",$ht+"px")
								});

					
					// $maininputSnapShot.append($link);
					spanStart = "<span style='float:left;line-height:20px;vertical-align:middle;width:auto;height:auto;white-space:normal'>&nbsp;";
					spanEnd = "</span>";
					var content = "";
					tokenCount = 0;
					totalTokens = 0;
					betweenCheck = false;
					inCheck = false;
					statCheck=false;
					var rows=1;
					for (key in bitsCondition) {
						totalTokens++;

					}
					
					for (key in bitsCondition) {
						tokenCount++;
						if(bitsCondition[key]["displayName"]!="Ones"){
							content += bitsCondition[key]["displayName"] + "&nbsp;";
						}
						if (bitsCondition[key]["displayName"] == "between") {
							betweenCheck = true;
						}
						
						if(tokenCount==1){
								if (bitsCondition[key]["type"] == "STAT") {
									statCheck = true;
								}
						}
						
						if (bitsCondition[key]["displayName"] == "in") {
							inCheck = true;
						}
							if (bitsCondition.unitObject) {
						
							if ((tokenCount >= 3 && !statCheck)
									&& (tokenCount < totalTokens - 2)) {
								if (betweenCheck) {
									content += "and" + "&nbsp;";
								} else {
									content += "," + "&nbsp;";   /////in comes here for general condition
									if(tokenCount/(8*rows)==1){content += "<br>"; rows++; $snapShot.find("div.c1").css("width","500px")}
									
								}
							}

							if (tokenCount == totalTokens - 2) {
								if (bitsCondition.unitObject.displayName != "Ratio" && bitsCondition.formatObject.displayName != "Ones") {
									content += "in" + "&nbsp;";
								} else if(bitsCondition.formatObject.displayName != "Ones"){
									if (betweenCheck) {
										content += "and" + "&nbsp;";
									} else {
										if (inCheck) {
											content += "," + "&nbsp;";
											
										}
									}
								}
							} else if (tokenCount == totalTokens - 1) {

								 if (bitsCondition.unitObject.displayName != "Ratio" && bitsCondition.formatObject.displayName != "Ones" ) {										
											content += "of" + "&nbsp;";
									} else {
										content += "in" + "&nbsp;";
									}
								
							}
						} else if (bitsCondition.formatObject) {
								if ((tokenCount >= 3 &&  !statCheck)
										&& (tokenCount < totalTokens - 1)) {
									if (betweenCheck) {
										content += "and" + "&nbsp;";
	
									} else {
										content += "," + "&nbsp;";
										
									}
								}
								if (tokenCount == totalTokens - 1) {
									if(bitsCondition.formatObject.displayName != "Ones"){
										content += "in" + "&nbsp;";
									}
								}
						} else if ((tokenCount >= 3) && (tokenCount < totalTokens) && !statCheck) {
							if (betweenCheck) {
								content += "and" + "&nbsp;";
							} else {
								content += "," + "&nbsp;";
								if(((tokenCount-1)/(8*rows)==1)){content += "<br>"; rows++; $snapShot.find("div.c1").css("width","500px")}
							}
						}
						
						if((tokenCount >= 3)){statCheck=false;}

					}
					//$snapShot.append(spanStart + "&nbsp;" + content + "&nbsp;"
							//+ spanEnd);
					x='<a href="javascript:;" id="del"><img  src="images/main/deleteButtNewEmpty.gif" border="0"/></a>';
					$snapBit.append("<div style='white-space:nowrap;' class='c1'>"+content+x+"</div>");
					
					$snapBit.find("a#del").bind('click', {
								id : index
							}, function(e) {
								e.stopPropagation();
								delConditionFlag = true;
								deleteCondition(e.data.id);
									// $ht=$("#showAllContainer
									// .divBorderWhite").height()-25;
									// $("#showAllContainer
									// .divBorderWhite").css("height",$ht+"px")
								});
					
					//$snapBit.find('div.c1').append('<img  src="images/deleteButt1.gif"></img>');
					//$snapBit.append($link);
					 $snapShot.append($snapBit);
					// $maininputSnapShot.append(spanStart + "&nbsp;" + content
					// + "&nbsp;" + spanEnd);
					wordCount += 1;
					if( wordCount>0 && cnt < $bitsConditionArray.length ){
							$snapBit.find("div.c1")
									.append(" , ");
							}
							
					if (wordCount == 1) {
						$snapShot
								.append("<div style='width:100%;float:left;'><img src='images/main/1pix.gif' width='150' height='1' /></div>");
						// $maininputSnapShot
						// .append("<div style='width:100%;float:left;'><img
						// src='images/main/1pix.gif' width='150' height='1'
						// /></div>");
						wordCount = 0;

					}
						// $snapShot.append('</td></tr></table>');
				});
				$("#" + o.snapShotId).append($snapShot);
				$maininput.data('input').show();
				// $maininput.data('input').val("Enter
				// Metrics").select().focus();
				if (o.snapShotId == 'ss_condition') {
					// $("#condition").append($maininputSnapShot);
					// $("#advOpt_condition
					// .qiSuggestCondition-holder").css("border-bottom","none");
				} else {
					$("#ss_cohort_condition div.snapBit").css("padding-left","5px");
					// $("#advOpt_cohort_condition").append($maininputSnapShot);
					// $("#advOpt_cohort_condition
					// .qiSuggestCondition-holder").css("border-bottom","none");
				}

				
				setHeight();
				// if(!delConditionFlag){
				showHideQuerySnapShot();
				// }
				// alert("::"+$snapShot.html());
			}
			function getQualifier(dT){
				var dType=dT.split("-");
				var dateQualifier="";
							if(dType.length==3){
										dateQualifier="Day";
									}
									if(dType.length==2){
										year=dType[1];
										if(dType[0].indexOf("Quarter")>-1){
										dateQualifier="Quarter";
										}else{
										dateQualifier="Month";
										}
									}
									if(dType.length==1){
										dateQualifier="Year";
									}
									
						return 	dateQualifier;		
			}
			function setHeight() {
				$ht = $("#showAll").height() + 10;
				$("#showAllContainer .divBorderWhite").height($ht);
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
			this.showAutoSuggest = showAutoSuggest;
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
					var conceptDataType="";
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
								conceptDataType=conditionTerm[key]["datatype"];
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
							stat="";
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
								|| conditionTerm[key]["type"] == "CONCEPT_LOOKUP_INSTANCE"
								|| conditionTerm[key]["type"] == "CONCEPT_LOOKUP_INSTANCE_PROFILE") {
							if (rhsStart) {
								XML.BeginNode("rhsValue");
								if (lhsHasInstance && conceptDataType !="DATE") {							
									XML.BeginNode("terms");
								} else {
									XML.BeginNode("values");
								}
								rhsStart = false;
							}
							if (conditionTerm[key]["type"] == "CONCEPT_LOOKUP_INSTANCE_PROFILE") {
								XML.BeginNode("term");
								XML.Node("name", conditionTerm[key]["name"]);
								XML.Node("displayName",
										conditionTerm[key]["displayName"]);
								XML.Node("type", 'CONCEPT_LOOKUP_INSTANCE_PROFILE');
								XML.EndNode();
							} else if (conditionTerm[key]["type"] == "CONCEPT_LOOKUP_INSTANCE") {
								XML.BeginNode("term");
								XML.Node("name", conditionTerm[key]["name"]);
								XML.Node("displayName",
										conditionTerm[key]["displayName"]);
								XML.Node("type", 'CONCEPT_LOOKUP_INSTANCE');
								XML.EndNode();
							} else {
								XML.BeginNode("qiValue");	
								if(conceptDataType=="DATE"){
									var dateQualifier="NA";
									var dType=conditionTerm[key]["name"].split("-");							
									var year="NA";
									var quarter="NA"
									var month="NA";
									var day="NA";
									var hours="NA";
									var mins="NA";
									var secs="NA";
									
									var dateQualifierValue="NA";									
									 if(dType.length==3){
							          year=dType[2];
							          month=dType[0];
							          day=dType[1];
							          dateQualifier="Day";
							          dateQualifierValue=month+"-"+day+"-"+year;
							         }
							         if(dType.length==2){
							          year=dType[1];
							          if(dType[0].indexOf("Quarter")>-1){
							          quarter=dType[0];
							          dateQualifier="Quarter";
							          dateQualifierValue=quarter+"-"+year;
							          }else{
							          month=dType[0]; 
							          dateQualifier="Month";
							          dateQualifierValue=month+"-"+year;
							          }
							         }
							         if(dType.length==1){
							          year=dType[0];
							          dateQualifier="Year";
							          dateQualifierValue=year;
							         }
									//TODO:-JT need to cover minute,hr sec
									if(secs != "NA"){
										dateQualifierValue=month+"-"+day+"-"+hours+"-"+mins+"-"+secs+"-"+year;
									}else if(mins !="NA"){
										dateQualifierValue=month+"-"+day+"-"+hours+"-"+mins+"-"+year;
									}else if(hours !="NA"){
										dateQualifierValue=month+"-"+day+"-"+hours+"-"+year;
									}
									//dateQualifierValue=year+"-"+quarter+"-"+month+"-"+day+"-"+hours+"-"+mins+"-"+secs;
									//alert("value::"+dateQualifierValue+":::Qualifier::::"+dateQualifier);	
								   XML.Node("value",dateQualifierValue);
								   XML.Node("dateQualifier",dateQualifier);
								}else{
									XML.Node("value", conditionTerm[key]["name"]);
								}
								
								if (conditionTerm.formatObject) {
									// XML.Node("conversionId",conditionTerm.formatObject.id);
									XML.BeginNode("qiConversion");
									XML.Node("name",
											conditionTerm.formatObject.name);
									XML.Node("displayName",
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
		sampleElementId : 'sampleValueId',
		snapShotText : 'dummy Text',
		// auto
		autocomplete : {
			opacity : 1,
			maxresults : 10,
			minchars : 1
		}
	};

})(jQuery);