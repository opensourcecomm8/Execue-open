<%@ page language="java" contentType="text/html; charset=iso-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="f" %>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<SCRIPT src="js/common/jquery.autocompleteKbase.js" type=text/javascript ></SCRIPT>
<script language="JavaScript"
	src="<c:out value="${basePath}"/>/js/main/uss/showUnstructuredReport.js"></script>
<link rel="stylesheet" href="css/main/jquery.autocomplete.css" type="text/css" />
<link rel="stylesheet" href="css/main/stylesSearchResult.css" type="text/css" />
<script  type="text/javascript" language="JavaScript" src="js/main/uss/suggestLocationDistance.js"></script>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
    <c:set var="basePath" value="<%=request.getContextPath()%>" />
    <style>
	.qiSuggestTerm-auto ul li.auto-focus { background: #CCE3D3; color: #000; }
	.ac_results li.ac_over{background: #CCE3D3; color: #000; } 
	</style>
    
     <input type="hidden" name="QID" id="QID"  value="<s:property value='userQueryId' />" />
     <input type="hidden" name="keyWordBasedResults" id="keyWordBasedResults"  value="<s:property value='keyWordBasedResults' />" />
                  <input type="hidden" name="AFC" id="AFC"  value="<s:property value='userQueryFeatureCount' />" />
                  <input type="hidden" name="UQRC" id="UQRC"  value="<s:property value='userQueryRecordCount' />" />
                 <input type="hidden" name="selectedDefaultVicinityDistanceLimit" id="selectedDVDLimitFaceted" value="<s:property value='selectedDefaultVicinityDistanceLimit' />" />
                
<table width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed">
          <tr>
            <td width="160" valign="top" align="left" style="border:1px solid #D3E1F9;padding:5px 0px 5px 0px;">
            
          
            <!-- facets -->
             <div id="facetsPane" style="width:100%;min-height:500px;height:auto;" class="dynamicPaneBgNoLoader">
				&nbsp;
			</div>
            <!-- facets end -->
            
            </td>
            <td  valign="top" width="75%">
            
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td style="height: 27px; padding: 2px 0px 4px 5px; background: #E0E0E0;">
                        
                        <div id="pageHeader" style="width:100%;height:32px;">
                        <input name="latitude" type="hidden" id="latitude" value=""  />
                         <input name="longitude" type="hidden" id="longitude" value=""  />
                          <input name="locationId" type="hidden" id="locationId" value=""  />
                          <input name="locationBedId" type="hidden" id="locationBedId" value=""  />
                          <input name="distance" type="hidden" id="distance" value=""  />
                          <input name="location" type="hidden" id="location" value=""  />
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:6px;">
                  <tr>
                  
                    <td width="10%" style="white-space:nowrap;padding-right:5px;"> <strong>Search Options</strong></td>
                    <td width="51%">
                    
                    
                                        <div style="width:350px;border:#BBB solid 1px;padding:2px;background-color:#FFF;margin-top:1px;">
                                        <table width="100%;" border="0" cellpadding="1" cellspacing="0"  style="">
                      <tbody><tr>
                        
                        <td style="padding-left: 3px;">within </td>
                        <td style="padding-left: 5px;">  <div id="selectDistance"	style="padding-top: 0px; float: left; padding-left: 0px; display: none;"></div> <s:select name="selectedDefaultVicinityDistanceLimit" cssStyle="display:none" list="defaultVicinityDistanceLimits"/></td>
                        <td style="padding-left: 6px;white-space:nowrap">Miles of Zip</td>
                        <td style="padding-left: 6px;">
                          <div id="selectLocation"	style="padding-top: 0px; float: left; padding-left: 0px; display: none;"></div>
                        
                        </td>
                        <td align="right">
                        <a onclick="processShow();this.blur();" href="#"><img height="20" border="0" src="images/main/show.png" id="showButton"></a>
                        </td>
                        
                       
                       
                      </tr>
                    </tbody></table>
                    
                    </div>

					</td>
                    <td width="25%" >
                    
                    <table width="100" border="0" align="right" cellpadding="2" cellspacing="0" id="hasImageDiv">
  <tr>
    
    <td align="right" width="30%" style="padding-left: 5px;"><s:checkbox name="imagePresent" id="imagePresent"  /></td>
    <td width="70%"  align="left" style="padding-left:3px;padding-right:20px;white-space:nowrap;"> Has Image</td>
  </tr>
</table>
</td>
                    <td width="14%" style="white-space:nowrap">
                    
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td><div id="listViewDiv" class="viewLink">List View</div></td>
                        
                            <td><div id="gridViewDiv" class="viewLink" >Grid View</div></td>
                          </tr>
                        </table>

                    
                    
                    
                    </td>
                  </tr>
                </table>
                </div>
                <div id="pageHeaderLoader" style="width:100%;height:32px;opacity:0.6;filter:alpha(opacity=60);display:none;position:absolute;top:97px;z-index:1000000000;background-color:#CCC;"></div>
				</td>
			</tr>
		
 <tr>
            <td style="padding:2px 0px 2px 5px;height:27px;font-weight:bold;">
            
                   
            
            
            <div id="requestRecognisedDiv"
						style="margin-top: 0px; margin-bottom: 0px;">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						background="images/main/line-bg.jpg">
						
							<tr id="trRequest">
								<td  width="715" height="20" style="padding-left: 2px;">
                               
                                <a class="requestTitle"  ><s:text name='execue.resultspage.requestRecognisedBy.text' /></a> 
                                                <s:property value="applicationName"/><a class="requestTitle"
									style="padding-left: 0px;">&nbsp;<s:text name='execue.resultspage.as.text' />&nbsp;</a><span style="font-size:9pt;"><s:property value="result.colorCodedRequest" escape="html" /></span>
                                    
                         
                              
                                    
                                    </td>
                                    
							</tr>
						
					</table>
					</div>
                    
                    
            
            </td>
          </tr>
			<tr>
				<td style="border-top: 0px dotted #CCC; padding-left: 5px;"><!--  dynamic page for pagination starts  -->
				<div id="dynamicPage" class="dynamicPaneBgNoLoader"
					style="width: 100%; min-height: 500px; height: auto;margin-top:0px;">&nbsp;
				</div>
				<!--  dynamic page for pagination ends  --></td>
				
			</tr>
		</table>
           
            </td>
            
            
            <td width="200"   style="wborder-left:1px solid #D3E1F9;display:block" valign="top">
            
            
            
		<div id="dynamicRelatedSearches">
        
        
        
        			<table cellspacing="0" cellpadding="0" border="0" align="left"
			width="60%">
            <tr>
                <td style="padding-left:10px;">
                
                <c:if test="${!empty relatedUserQueries}">
                <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-bottom:20px;" >
                      <tr>
                        <td style="padding-top:5px;"><strong>Related Searches</strong></td>
                      </tr>
                      <tr>
                        <td height=25 align=left style="padding-left:10px;">
                        <div class="container" id="resultsRightpanel">
                        <div style="height: auto; width: 150px; ">
						<div id="tab1" style="margin: 5px" >
                          <s:iterator id="rQuery" value="relatedUserQueries"  status="st">
                      			<s:if test="#st.index < 4">
                                	<s:if test="#st.index == 0">
                                      <ul style="margin:0px; padding:0px 0px 0px 3px;">
                                    </s:if>
										<li>
                        					<a title="<s:property value='userQuery'/>" href="javascript:relatedSearch('<s:property value="userQuery"/>')"> <s:property value="userQuery"/> </a>
                        				</li>
                        			<s:if test="#st.index == 3">
                                      </ui>
                                    </s:if>
                      		</s:if>
                            <s:else>
                            	<s:if test="#st.index == 4">
                                    <ul id="moreRSearchDiv" style="margin:0px; padding:0px 0px 0px 0px;display:none;list-style-type:disc;">
                                </s:if>
                                <li>
                     				<a title="<s:property value='userQuery'/>" href="javascript:relatedSearch('<s:property value="userQuery"/>')"> <s:property value="userQuery"/> </a>
                     			</li>
			                	<s:if test="#st.last == true">
			                	   </ul>
				                    <div style="float:right;width:100%;padding-top:3px;margin-top:3px;border-top:1px dotted #D3E1F9">
				                    <a style="float:right;" href="javascript:showMore('moreRSearchDiv', 'showMoreLink1');" id="showMoreLink1" onFocus="this.blur()">
				                    <img height="15" border="0" width="50" src="<c:out value="${basePath}"/>/images/main/more-2.png"></a>
				                    </div>
				                </s:if>              
                            </s:else>
                       </s:iterator>
                       </div>
                       </div>
                      </div>
                      
						</td>
                      </tr>
                     
                    </table>
				</c:if>
				
                </td>
                 
              </tr>

				<tr>
					<td valign="top"
						style=" padding-left: 10px;">
						
			
					</td>
				</tr>
			
		</table>
        		
        </div>
        
        
        
		</td>
          </tr>
        </table>
        <div id="featuresAlert" style="display: none; position: absolute; padding: 5px; color: rgb(0, 0, 0); background: none repeat scroll 0% 0% rgb(255, 255, 215); border: 1px solid rgb(0, 0, 0); left: 200px;z-index:100000;">Please select at least one option from features</div>
  
   <div id="showAdd" 	style="display: none;  overflow: auto; width:181px ; height:48px;position:absolute;z-index:10000;left:100px;top:100px;border:3px soild #333;cursor:pointer">
<img src="images/main/addtoKnowledge.png" border="0" />
</div>

<div id="tempResemantificationId"> </div>

<div id="showKnowledgePopup"  	>
</div>

   
  <script>
var inst="";
var autoSuggestClicked=false;
var $left_v=0;
var $top_v=0;
var fromunrecog=false;
var page="cc";
   function getSelected() {
  if(window.getSelection) { return window.getSelection(); }
  else if(document.getSelection) { return document.getSelection(); }
  else {
    var selection = document.selection && document.selection.createRange();
    if(selection.text) { return selection.text; }
    return false;
  }
  return false;
}
/* create sniffer */

  $(function(){
             userQueryId='<s:property value="userQueryId" />'; //alert(qid);
			 locationSuggestTerm="";
			 userQueryFeatureCount=""
             userQueryRecordCount="";
			  modelId=$("#modelId").val();
			 $("#imagePresent").click(function(){ 
				userQueryFeatureCount=$("#AFC").val();
                userQueryRecordCount=$("#UQRC").val();
				processClickAction('fromHasImage');
			});
			 var location ='<s:property value="result.locationDisplayName"/>';
			 var isMultiple='<s:property value="result.multipleLocation"/>';
			 //alert(isMultiple);
			 var $distanceList = [];
			 
			 $.each($("select#selectedDefaultVicinityDistanceLimit option"),function(k,v){
						$d={};		
						//alert(v);
					$d.name= $(this).text();
					$d.displayName= $(this).text();
					$d.type="DISTANCE";
					
					$distanceList.push($d);																	  
				});
			 

			SelectLocation = $('#selectLocation').suggestLocationDistance({
				suggestURL: 'uss/suggestLocations.action',
				displayText: location ,
				multiple : isMultiple,
				distanceList:$distanceList,
				holderClass:'suggest-holder',
				titleText:'Click here to change the zip'
				}).get(0);	
			var distance=$("select#selectedDefaultVicinityDistanceLimit").val();
			$("#distance").val(distance);
			$("#location").val(location);
			SelectDistance = $('#selectDistance').suggestLocationDistance({
				suggestURL: '',
				displayText: distance ,
				multiple : false,
				distanceList:$distanceList,
				holderClass:'suggest-distance-holder',
				titleText:'Click here to change the Distance'
				}).get(0);	
			
	});
  
  /*function performResemantification(){     
     var applicationId=$("#applicationId").val();
     var userQuery=$("#metrics").val();	
     //alert(userQuery);
      $.post("uss/performResemantification.action",{applicationId:applicationId,userQuery:userQuery} ,function(data){	
       // alert(data.messageId);	  
	    $("#dynamicPane").empty().html(data);
	   }); 
  }*/
 
  function processShow(){
		 $("#showClicked").val("Yes");
		 // alert($("#featuresOuterDiv input[type='checkbox']:checked").length);
		  var locSuggest=$.trim($("#location").val());
		  mylocation ='<s:property value="result.locationDisplayName"/>';
		  if(locSuggest!=""  ){ 
				$("#selectedDVDLimitFaceted").val($("#distance").val());
				locationSuggestTerm = returnLocationSuggestTerm(); 
				userQueryFeatureCount=$("#AFC").val();
                userQueryRecordCount=$("#UQRC").val();
				processClickAction("fromShow");	
		  }else{
			//alert("please select at least one feature "); 
			$("#featuresAlert").empty().html("Please select valid zip code from list").show();
			$("#featuresAlert").css("top",($("#showButton").position().top+25)+"px");
			$("#featuresAlert").css("left",($("#showButton").position().left-70)+"px");
			setTimeout('$("#featuresAlert").hide()',3000);
		  }
	  }
	  
	var messageId= '<s:property value="messageId"/>';
	qString='<s:property value="userQuery" />';
	userQueryId='<s:property value="userQueryId" />';
	if(messageId){ 
		//$("#dynamicPage").removeClass('dynamicPaneBgNoLoader').addClass('dynamicPaneBgLoader'); 
	   $('#dynamicPage').showUnstructuredReport({
						msgId : messageId				
			});
	
	}
	
  </script>
  
  <script src="js/main/uss/knowledge.js" ></script>