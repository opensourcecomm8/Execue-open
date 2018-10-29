
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>

		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td><s:if
					test="universalSearchResult.unstructuredSearchResultItem.size==0">
                                No Results Found
                                </s:if> <s:else>

					<s:property value="universalSearchResult.totalCount" /> Results found  - Best  Matches ( <s:property
						value="universalSearchResult.perfectMatchCount" /> ) 
                                </s:else></td>
				
			</tr>
		</table>


		</td>
	</tr>
	<tr>
		<td height="520" valign="top"> 



		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tbody>
				<tr>
					<td valign="top" height="520">
					<div id="listViewU" style="">
					<div style="margin-bottom: 5px; width: 100%; float: left;">
					<div class="colHeadBg" id="colHeadings" style="display: none;">
					<table width="100%" border="0" cellspacing="0" cellpadding="3"
						style="margin: 0px 0px 1px 0px; table-layout: fixed;">
						<tr>
							<td style="width: 20%;border:1px solid #CBD2DE; " height="26" align="center" class="colHead">&nbsp;</td>
							

							<s:iterator value="universalSearchResult.universalSearchResultFeatureHeaders" id="universalSearchResultHeader" status="statusHeader">
                               
									<td width="4%" align="center" class="colHead" style="padding-left:5px;padding-right:10px;border:1px solid #CBD2DE; ">
                                    
                                  
                                    <s:if test="sortable.value=='N'">
                                    <strong><s:property value="name" /></strong>
                                    </s:if>
                                    <s:else>
                                      <a href="#" class="colHeadLink"  columnName='<s:property value="columnName" />' defaultSortOrder='<s:property value="defaultSortOrder" />'><s:property value="name" /></a>  
                                      </s:else>
                                      
                                      
                                    </td>
								
							</s:iterator>

						</tr>
					</table>
					</div>



					<table width="99%" border="0" cellspacing="0"  cellpadding="3"
						class="tableGrid"
						style="margin: 0px 0px 1px 0px; table-layout: fixed; border-collapse:collapse;">

						<s:iterator value="universalSearchResult.unstructuredSearchResultItem"  id="universalSearchResult2" status="status2">
							
							<tr>
								<td   style="width:20%;border:1px solid #D3E1F9; " >
                                         <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                              <tr>
                                                <td width="100"><a alt="" class="headerLink" href="javascript:;">  
                                                <img class="listimage" count="0" src="<s:property value='#universalSearchResult2.unstructuredSearchResultDataHeader.imageUrl' />" width="90" height="60"
                                                                                border="0"></a>
                                                                                </td>
                                                <td style="padding:0px 10px 0px 10px;">
                                                
                                               <div> <a class="titleLink" target="_blank" href="<s:property value='#universalSearchResult2.unstructuredSearchResultDataHeader.url' />"><s:property value='#universalSearchResult2.unstructuredSearchResultDataHeader.shortDescription' /></a></div>
                                              
                                                <s:if test="%{#universalSearchResult2.unstructuredSearchResultDataHeader.location!=null}">
                                               <div> Location: <s:property value='#universalSearchResult2.unstructuredSearchResultDataHeader.location' /></div>
                                               </s:if>
                                 
                                   <s:iterator value="#universalSearchResult2.unstructuredSearchResultDataHeader.unstructuredSearchResultData"  id="unstructuredData" status="unstructuredDataStatus">
                                        <s:property value='universalSearchResult.universalSearchResultFeatureHeaders[headerIndex].name' /> : <s:property value="value" />
                                   </s:iterator>
                                                </td>
                                                                                
                                                                               
                                              </tr>
                                            </table>

                                    </td>
								
								<s:iterator value="%{#universalSearchResult2.unstructuredSearchResultData}" status="s1">

										<td width="4%" align="<s:property value='universalSearchResult.universalSearchResultFeatureHeaders[#s1.index].diplayableFeatureAlignmentType.value' />" style="padding-left:5px;padding-right:10px;border:1px solid #D3E1F9; "    valign="top">
                                        <s:if test='value!="UNKNOWN_VALUE" && value!="-1.0" '>
                                         <s:property value="value" /> 
                                        </s:if>
                                        <s:else>NA
                                        </s:else>
                                        </td>
									
								</s:iterator>

							</tr>
						</s:iterator>

					</table>


					</div>
					</div>






					<div id="imageViewU" style=""><s:if test="universalSearchResult.unstructuredSearchResultItem.size==1">
						<div style="margin-bottom: 5px; width: 33%; float: left;">
					</s:if> <s:if
						test="universalSearchResult.unstructuredSearchResultItem.size==2">
						<div style="margin-bottom: 5px; width: 66%; float: left;">
					</s:if> <s:if
						test="universalSearchResult.unstructuredSearchResultItem.size>2">
						<div style="margin-bottom: 5px; width: 100%; float: left;">
					</s:if>
					<table width="99%" border="0" cellspacing="2" cellpadding="5"
						class="tableGrid"
						style="margin: 5px 0px 10px 0px; table-layout: fixed;">

						<s:iterator value="universalSearchResult.unstructuredSearchResultItem" id="universalSearchResult3" status="statusImage">

							<s:if test="#statusImage.count%3==1">
								<tr>
							</s:if>

							<td width="33%" style="border:1px solid #D3E1F9" valign="top"><a alt="" class="headerLink" target="_blank"
								href="<s:property value='url' />">
                                <img class="gridimage" src="<s:property value='#universalSearchResult3.unstructuredSearchResultDataHeader.imageUrl' />" width="60" height="40" align="right" border="0"></a></span> 
                                <a href="<s:property value='#universalSearchResult3.unstructuredSearchResultDataHeader.url' />" target="_blank" class="titleLinkGrid" style="white-space: normal;">
                                
                                <s:property value="#universalSearchResult3.unstructuredSearchResultDataHeader.shortDescription" /> , 
                                <strong>Location :</strong> 
                                
                                <s:if test='location!=null'>
                         <s:property value='location' /> 
                                        </s:if>
                                        <s:else> NA
                                        </s:else>
                                        
                                        
                                <input type="hidden" name="adTitle" value="<s:property value="shortDescription" />" /> 
                                
                                <s:iterator value="%{#universalSearchResult3.unstructuredSearchResultData}" status="gridValues">
								
                                       <s:if test='value!="UNKNOWN_VALUE" && value!="-1.0" '>
                          ,  <strong><s:property value="universalSearchResult.universalSearchResultFeatureHeaders[#gridValues.index].name" />:</strong> <s:property value="value" />
                                        </s:if>
                                        <s:else>,  <strong><s:property value="universalSearchResult.universalSearchResultFeatureHeaders[#gridValues.index].name" />:</strong> NA
                                        </s:else>
								</s:iterator> 
                            
                            </a></td>



							<s:if test="#statusImage.count%3==0">
								</tr>
							</s:if>



						</s:iterator>
					</table>


					</div>




					</div>




					</td>
				</tr>

			</tbody>
		</table>

		</td>
	</tr>
	<tr>
		<td style="border-top: 1px dotted #D3E1F9;">
        
        <s:set name="fullUrl" value="%{'getUnstructuredSearchPageResult.action'}" /> 

        <s:iterator value="userRequestedLocations" status="locationStatus">
			<s:set name="queryString" value="%{'&userRequestedLocations['+#locationStatus.index+']='+userRequestedLocations[#locationStatus.index]}" />
		</s:iterator> 
         <s:set name="url" value="%{#fullUrl}" />          
         <s:set name="qString" value="%{#queryString}" />
		<div id="paginationDiv2" style="float: right"><pg:page
			targetURL='${url}?applicationId=${applicationId}&userQuery=${userQuery}&userQueryId=${userQueryId}&modelId=${modelId}&selectedDefaultVicinityDistanceLimit=${selectedDefaultVicinityDistanceLimit}&imagePresent=${imagePresent}&resultViewType=${resultViewType}&userQueryFeatureCount=${userQueryFeatureCount}&userQueryRecordCount=${userQueryRecordCount}&recordCount=${universalSearchResult.totalCount}&perfectMatchCount=${universalSearchResult.perfectMatchCount}&mightMatchCount=${universalSearchResult.mightMatchCount}&partialMatchCount=${universalSearchResult.partialMatchCount}&sortingInfo.columnName=${sortingInfo.columnName}&sortingInfo.defaultSortOrder=${sortingInfo.defaultSortOrder}&fromPagination=true'
			targetPane="dynamicPage" /></div>
		</td>
	</tr>
</table>


<div id="showBigImageGrid"
	style="display: none; width: 420px; min-height: 120px; height: auto; position: absolute; z-index: 10000; left: 0px; top: 0px; border: 1px solid #333;">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td height="30" colspan="2" align="center" bgcolor="#ffffff"
				id="imageTd2"
				style="border: 0px solid rgb(211, 225, 249); padding: 2px;"><a
				id="gridPopupTitle" style="color: #036" href="#"></a></td>
		</tr>
		<tr>
			<td id="imageTd"
				style="border: 0px solid rgb(211, 225, 249); padding: 2px;"
				align="center" bgcolor="#ffffff" height="140">
			<div id="bigImgGrid"
				style="display: none; border: 0px solid #EAEAEA; padding: 2px;"></div>
			</td>
			<td id="imageTd"
				style="border: 0px solid rgb(211, 225, 249); padding: 2px;"
				align="center" bgcolor="#ffffff"><!--div class="makeModel">Honda, Accord</div-->
			<table width="50%" border="0" cellspacing="0" cellpadding="2">
				<tr>
					<td width="10%" style="white-space: nowrap; padding-right: 5px;"><strong>Model
					Year :</strong></td>
					<td width="90%"><span class="gridPopupYear">2007</span></td>
				</tr>
				<tr>
					<td><strong>Price :</strong></td>
					<td style="white-space: nowrap"><span class="gridPopupPrice">$10,000</span>
					$</td>
				</tr>
				<tr>
					<td><strong>Mileage :</strong></td>
					<td><span class="gridPopupMiles">10,000</span> Miles</td>
				</tr>
			</table>
			</td>
		</tr>
	</tbody>
</table>
<input id="requestedStringId" value="honda" type="hidden"></div>


<script>
			$(function(){
					 var recordSize="<s:property value='universalSearchResult.unstructuredSearchResultItem.size' />";  
					 if(recordSize>0){
						 $("#sortDiv").show();
						 $("#colHeadings").show();
						 $("table#hasImageDiv").show();
						 $("#imageButton").show();
					 }
					substringShortDesc();
					substringShortDescForList();
					enableImageClick(); 
					var aId=$("#applicationId").val();
					//alert(aId+"::");
					addNoImage(aId);
					addNoImageGrid(aId);
					
					$("#imageViewU table.tableGrid td").mouseover(function(){
										   if($(this).attr("class")!="colHead"){
						$(this).css("background-color","#E6F4FF");	
														   }
					});
				$("#imageViewU table.tableGrid td").mouseout(function(){
														   if($(this).attr("class")!="colHead"){
						$(this).css("background-color","#fff");	
														   }
					});
				
				var timr=undefined;
				$("#imageViewU table.tableGrid td").mouseover(function(){
							var left=$(this).position().left;
							var top=$(this).position().top;
							$wth=$(this).width();
							left=left+($wth/2)-210;
							$("#showBigImageGrid").css("left",(left)+"px");
															   
							if(top>300){$("#showBigImageGrid").css("top",(top-180)+"px");}
							else{$("#showBigImageGrid").css("top",(top+70)+"px"); }
							
							var $img=$("<img width='200' height='120'></img>");
							$img.attr("src",$(this).find("img").attr("src"));
							$("#bigImgGrid").empty().append($img).show();
							$("#gridPopupTitle").empty().html($(this).find("input[name='adTitle']").val());
							$("#gridPopupTitle").css("font-weight","bold");
							$(".gridPopupYear").empty().html($(this).find("input[name='YEAR']").val());
							$(".gridPopupPrice").empty().html($(this).find("input[name='PRICE']").val());
							$(".gridPopupMiles").empty().html($(this).find("input[name='MILEAGE']").val());
							clearTimeout(timr);
							timr= setTimeout(function(){ $("#showBigImageGrid").fadeIn();},1000);
					});
				$("#imageViewU table.tableGrid td").mouseout(function(){ clearTimeout(timr);
							$("#showBigImageGrid").hide();
					});
					
					$('table.tableGrid img').fixBroken();
					
					
					if($view=="gridView"){
					//$("#viewLink").text("List View");
					$("#listViewU").hide();
					$("#imageViewU").show();
					$("#gridViewDiv").css("cursor","default");
						$("#listViewDiv").css("cursor","pointer");
						$("#gridViewDiv").css("background","url('images/main/uss/gridView.png') no-repeat");
						$("#listViewDiv").css("background","url('images/main/uss/listViewLink.png') no-repeat");
						$("#listViewDiv").css("color","#0000FF");
						$("#gridViewDiv").css("color","#000000");
						
					}else{ 
						//$("#viewLink").text("Grid View");
						$("#listViewU").show();
						$("#imageViewU").hide();
						$("#gridViewDiv").css("cursor","pointer");
						$("#listViewDiv").css("cursor","default");
						$("#gridViewDiv").css("background","url('images/main/uss/gridViewLink.png') no-repeat");
						$("#listViewDiv").css("background","url('images/main/uss/listView.png') no-repeat");
						$("#listViewDiv").css("color","#000000");
						$("#gridViewDiv").css("color","#0000FF");
						
					}
					$("#listViewDiv").css("paddingLeft","19px");
					$("#gridViewDiv").css("paddingLeft","19px");
					$("#gridViewDiv").css("paddingTop","2px");
					$("#listViewDiv").css("paddingTop","2px");
					$("#listViewDiv").css("marginRight","7px");
					$("#gridViewDiv").css("marginRight","5px");
					
					$("#gridViewDiv").bind("click",function(){
						if($view=="listView"){									
						$view="gridView";
						$("#resultViewTypeId").val("gridView");							
						processView('');
						}
						
					});
					$("#listViewDiv").bind("click",function(){
						if($view=="gridView"){		
						$view="listView";
						$("#resultViewTypeId").val("listView");							
						processView('');
						}
					});

				});
			
	$(".colHeadLink").click(function(){
	  var columnName=$(this).attr("columnName");
	  var defaultSortOrder=$(this).attr("defaultSortOrder");
	  
	  var sortParams= "&sortingInfo.columnName="+columnName+"&sortingInfo.defaultSortOrder="+defaultSortOrder;
	  processView(sortParams);
	
	});		
				// wait for page to load
	 
		
			</script>