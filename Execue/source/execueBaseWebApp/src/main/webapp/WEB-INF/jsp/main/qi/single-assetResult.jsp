<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="pg" uri="/WEB-INF/tlds/pagination.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<title>Semantifi Results</title>
<c:set var="count" value="${startIndex}" />
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<link href="css/common/pagination.css" rel="stylesheet" type="text/css" />
<jsp:include page="../selectCss.jsp" />
<style>
td[id='dynaReportTd'] {
	border-left: 0px solid #D3E1F9;
	padding-left: 10px;
}
div[id='company'] td[class='column0'] {
	width: 190px;
}
.resultsmatch{margin-bottom:4px;font-weight:bold;}

#showKnowledgePopup{
	
	display: none;
	background-color:#FFF;
	box-shadow: 0 0 6px 0 rgba(0, 0, 0, 0.65), 0 1px 0 rgba(255, 255, 255, 0.35) inset;
	border: 1px solid #414141;
	border-radius: 10px 10px 10px 5px;
	padding:20px;  overflow: auto; 
	width:260px ; 
	min-height: 100px;height:auto;position:absolute;z-index:10001;left:100px;top:100px;border:3px soild #333;
	
}

</style>
<link rel="stylesheet" href="css/main/jquery.autocomplete.css" type="text/css" />
<script>
var globalType=[];
var wordCount=0;
var tokenCandidateWords=[];
var rCount=0;
var addthis_config = {
 services_compact: 'facebook, linkedin, email, more',
 services_exclude: 'print'
}
</script>
<SCRIPT src="<c:out value="${basePath}"/>/js/common/jquery.autocompleteKbase.js" type=text/javascript ></SCRIPT>
<script language="JavaScript"
	src="<c:out value="${basePath}"/>/js/main/uss/showUnstructuredReport.js"></script>
<script type="text/javascript"
	src="http://s7.addthis.com/js/250/addthis_widget.js?pub=xa-4ab35570247e075a"></script>
    <div id="pageLoader" style="display:none;padding:4px;padding-top:5px;z-index:1000000;position:absolute;border:none;width:50px;height:50px;"><div style="width:32px;;margin:auto;"><img id="waiting_img2" src="images/main/Loader-main-page-3.gif" width="32" height="32" /></div><div style="width:100%;text-align:center;">Loading</div></div>
<div  class="outerDiv">
<form name="backToHome" method="post">
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="10%" >
	<tr>
		<td height="20" class="normalText" width="50%"><span
			style="padding-left: 1px; color: #3F5356;"> <a href="#"
			id="backTohome" onclick="backToHomePage();" class="breadCrumbLink"><s:text name='execue.home.link' /></a>
		</span></td><td align="right" width="50%"><!--span id="moredata" style="color:#333;font-size:12px;padding-right:10px;">( <a href="biSignUp.action" class="linkWhite">Signup</a> )</span--></td>
	</tr>

</table>
<s:hidden id="requestString" name="requestString" /> <s:hidden
	id="typeId" name="type" /></form>
<s:hidden id="unstructureAppRequestId" name="request" /> 
</div>
<div style="margin: auto; margin-top: 3px; margin-bottom: 0px;" id="greyBoxDiv">
<table border="0" cellpadding="0" cellspacing="0" width="100%" >
	<!-- fwtable fwsrc="message-bar.png" fwpage="Page 1" fwbase="message-bar.png" fwstyle="Dreamweaver" fwdocid = "1001237948" fwnested="1" -->
	<tr>
		<td background="images/main/message-bar_r1_c1.png"><img name="messagebar_r1_c1" src="images/main/message-bar_r1_c1.png" width="400" height="4" border="0" id="messagebar_r1_c1" alt="" /></td>
	</tr>
	<tr>
		<td background="images/main/message-bar_r2_c1.png" height="auto">


		<table width="99%" border="0" cellspacing="0" cellpadding="0"
			align="center">


			<tr id="trMessage" height="18">
				<td>
				<div id="messageDiv"><s:if test="result.messages.size > 0">
					<a class="messageTitle"><s:text name='execue.global.message.text' /></a>
				</s:if><s:iterator value="result.messages">
					<a class="normalText"><s:property value="top" /></a>
				</s:iterator></div>
				</td>
			</tr>


			<tr id="trRequest">
				<td height="22"><a class="requestTitle"><s:text name='execue.global.request.text' /> </a><s:property
					value="result.queryName" escape="html" /><span
					style="padding-left: 5px; white-space: nowrap;color:#3F5356;">[<span
					id="showSaveQueryLink" style="padding-top: 11px"><a
					href="javascript:;" id="bookmarksIdResults"><s:text name='execue.global.save.link' /></a></span><span
					id="loadingShowSaveQueryLink"
					style="display: none; padding-left: 20px; padding-right: 20px;"><img
					src="images/main/loaderTrans.gif"></span><span style="color:#3F5356;">]</span>
                    <!-- cachec code -->
                    <s:if test="result.cacheResultPresent" > [ <a href="javascript:" onclick="refreshClicked();" alt="Refresh" title="Refresh" >Refresh</a> ] </s:if></span></td>
			</tr>

			<s:if test="result.error !=null">
				<tr>
					<td>
					<div id="resultError"><s:property value="result.error" /></div>
					</td>
				</tr>
			</s:if>
		</table>





		</td>
	</tr>
	<tr>
		<td background="images/main/message-bar_r3_c1.png"><img name="messagebar_r3_c1" src="images/main/message-bar_r3_c1.png" width="400" height="1" border="0" id="messagebar_r3_c1" alt="" /></td>
	</tr>
</table>
</div>

<table width="100%" border="0" cellspacing="0"  cellpadding="0" height="90%">
	<tr>
	<td height="99%" valign="top">


		<div style="margin:auto;height:auto;" class="outerDiv" >
		<table border="0" cellspacing="0" width="100%" height="100%" cellpadding="0">
          <tr>
            <td width="85%" valign="top">
            
            <!--center content  starts-->
            
            
            
            <table width="100%" border="0" align="left" cellpadding="0"
			cellspacing="0" bgcolor="#FFFFFF">
			
			<tr>
				<td height="400" valign="top" class="contentBg">
				<div id="reportResults" style="width:100%;">
                <s:iterator value="result.possibilites" id="possibility" status="stPossible">
                   <s:iterator value="assets" id="assetResult" status="status">
                      <div id="report_<s:property value="id"/>_<s:property value="businessQueryId"/>">
						<table border=0 bordercolor=pink width="96%">
							<!-- PINK-->
							<tr valign="middle">
						         <s:if test="%{#assetResult.error != null}">
									<td><font color="red"><s:property value="error" /></font></td>
								</s:if> 
								<s:else>
									<!-- AJAX DATA -->
									<table border="0" bordercolor=yellow cellpadding="0"
										cellspacing="0">
										<tr>
											<td class="result" width="18" style="padding-top:3px;" valign="top">											
											</td>
											<td valign="bottom" style="padding-bottom: 1px;">
											<div
												id="report_<s:property value="id"/>_<s:property value="businessQueryId"/>_header" style="display:none"></div>
											<div
												id="report_<s:property value="id"/>_<s:property value="businessQueryId"/>_processing"></div>
											</td>
										</tr>
									</table>

									<table border=0 bordercolor=#666 width="98%" cellpadding="0"
										cellspacing="0">
									
										<tr>
											<td></td>
											<td>
											<div
												id="report_<s:property value="id"/>_<s:property value="businessQueryId"/>_query"
												class="sql" style="display: none; padding-left: 15px; overflow: auto; width: 100%; height: 150px">
											</div>
											<div
												id="report_<s:property value="id"/>_<s:property value="businessQueryId"/>_assetInfo"
												class="sql" style="display: none; padding-left: 15px; overflow: auto; width: 100%; height: 150px">
											</div>
											</td>
										</tr>
									</table>
								</s:else></td>
							</tr>
						</table>
						</div>
						
						<input type="hidden"
							id="asset_<s:property value='assetId'/>_<s:property value="businessQueryId"/>" />
						<input type="hidden"
							id="reportId_<s:property value='assetId'/>_<s:property value="businessQueryId"/>" />
						<input type="hidden"
							id="reportId_<s:property value='assetId'/>_<s:property value="businessQueryId"/>_psStmt" />
						<input type="hidden"
							id="reportId_<s:property value='assetId'/>_<s:property value="businessQueryId"/>_query" />
							
					     	<SCRIPT type="text/javascript">  
				               $(document).ready(function(){                  
									rCount+=1;
									var $rqn=$("#resultQueryName").val();
									$rqn=js_RemoveChar($rqn);
						           if($("#asset_<s:property value='assetId'/>_<s:property value='businessQueryId'/>").val()!='COMPLETED' ){
						                $('#report_<s:property value="id"/>_<s:property value="businessQueryId"/>').showExecueReport( {
						                reportStatus : '<s:property value="reportStatus"/>',				
										qid : '<s:property value="result.id"/>',
										msgId : '<s:property value="messageId"/>',
										aid : '<s:property value="assetId"/>',
										source : '<s:property value="description"/>',
										queryName : $rqn,
										appId : '<s:property value="#possibility.appId"/>',
										reportTitle : '<s:property value="reportHeader"/>', 
										bqId : '<s:property value="businessQueryId"/>',
										assetSize: 1
										} );
										
									}else{	
									
									  $("#report_<s:property value='id'/>_<s:property value='businessQueryId'/>_header").html($("#reportId_<s:property value='assetId'/>_<s:property value='businessQueryId'/>").val());
									 //$("#report_<s:property value='id'/>_<s:property value='businessQueryId'/>_pseudoStmt").hide();					 
									  //$("#report_<s:property value='id'/>_<s:property value='businessQueryId'/>_pseudoStatement").html($("#reportId_<s:property value='assetId'/>_<s:property value='businessQueryId'/>_psStmt").val());
									  if($("#reportId_<s:property value='assetId'/>_<s:property value='businessQueryId'/>_psStmt").val()){					  
									     $("#report_<s:property value='id'/>_<s:property value='businessQueryId'/>_pseudoStatement").html($("#reportId_<s:property value='assetId'/>_<s:property value='businessQueryId'/>_psStmt").val());
									  }else{					  
									     // $("#report_<s:property value='id'/>_<s:property value='businessQueryId'/>_pseudoStatement").html('');
									  }		
									  
									  $("#report_<s:property value='id'/>_<s:property value='businessQueryId'/>_query").html($("#reportId_<s:property value='assetId'/>_<s:property value='businessQueryId'/>_query").val());
									  addthis.button('.sharing-button');
									}
								});
							</SCRIPT>
                   </s:iterator>
                
                </s:iterator>   
        </div>                    
         </div>
				<table border="0">
					<tr>
						<td><!--<a>What other users are asking:</a> --></td>
						<td></td>
						<td></td>
					</tr>
				</table>
				</div>
				</td>
			</tr>
			<tr>
				<td><!-- Start Pagination Code 
			<div align="center">
				<span style='font-size:16px'><pg:paginate /></span>
			</div>
		 End Pagination Code --></td>

			</tr>
		</table>
            <!--               center content ends -->
            
            
            </td>
            <td id="resultsRightTd" width="15%" valign="top">
            <!--               ad content starts -->
<table width="290" border="0" cellspacing="0" cellpadding="0"   >
			<tr>
                <td style="padding-left:10px;">
                 
                </td>
                 
              </tr>

				<tr>
					<td style="border-left: 1px solid #D3E1F9; padding-left: 10px;">
					<c:if test="${!empty result.unstructuredSearchResult}">
					<c:set var="nCount" value="0" />

						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							>
							
					<!-- Start of news feeds -->
					
					<TR>
						<TD  align=left style="padding-left:10px;">
						<div id="resultsNewsContainer" class="container">
						<div
							style="height: auto; width: 290px; ">
						<div id="tab1" style="margin: 5px" >
						 
							<s:iterator value="result.unstructuredSearchResult" id="usResult">							
								<s:iterator value="%{#usResult.unstructuredSearchResultItem}"
									id="usrItem">
                                    
                                    <c:if test="${nCount < 4}">
                                      <c:if test="${nCount == 0}">
								<div colspan="3"><strong>Related News</strong></div>
							
                            
                                      <ul style="margin:0px; padding:0px 0px 0px 3px;">
                                      </c:if>
										<li><A target="_blank"
											href="<s:property value="%{#usrItem.url}"/>"><s:property
											value="%{#usrItem.shortDescription}" /></A>
											<div style="font-size: 10px;color:#999;line-height: 14px;">
											<i>Ref : <s:property value="contentSource"/></i></div>
                                            </li>
									 <c:if test="${nCount == 3}">
                                     </ul>
                                     </c:if>
                                    </c:if>
                                    <c:if test="${nCount >= 4}">
                                    <c:if test="${nCount == 4}">
                                    <ul id="moreNewsDiv" style="margin:0px; padding:0px 0px 0px 3px;display:none;">
                                    </c:if>
                                    <li><A target="_blank"
											href="<s:property value="%{#usrItem.url}"/>"><s:property
											value="%{#usrItem.shortDescription}" /></A>
											<div style="font-size: 10px;color:#999;line-height: 14px;">
											<i>Ref : <s:property value="contentSource"/></i></div>
											</li>
                                    </c:if>
                                    <c:set var="nCount" value="${nCount+1}" />
                                    </s:iterator>
							</s:iterator>
						</div>
						<c:if test="${nCount > 4}">
                                     </ul>
                                     
                                      <div style="float:right;width:100%;padding-top:3px;margin-top:3px;border-top:1px dotted #D3E1F9"><a style="float:right;" href="javascript:showMore('moreNewsDiv','showMoreLink2');" id="showMoreLink2" onfocus="this.blur()"><img height="15" border="0" width="50" src="<c:out value="${basePath}"/>/images/main/more-2.png"></a></div>
										
							</c:if>
						</div>
						</div>
						</TD>
					</TR>

					<!-- End of news feeds -->
				</table>

					</c:if></td>

					</tr>
					<!-- Start of dynamic report -->
					<tr><td id='dynaReportTd'>						
					</td></tr>
					<!-- End of dynamic report -->
              <tr>
                <td>
                
                
            <!--               ad content starts -->
           <table width="60%" border="0" cellspacing="0" align="left" cellpadding="0">
                       
              <tr>
                <td valign="top" style="border-left:1px solid #D3E1F9;padding-left:10px;">
                
			
                
                </td>
              </tr>
            </table>

             </td>
              </tr>
            </table>
             <!--               ad content ends -->
                
                
                
                </td>
              </tr>
            </table>

		</div>


		</td>
	</tr>
	<tr>
		<td>


		<div
			style="margin: auto; margin-top: 10px; margin-bottom: 10px; " id="paginationSpace">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td height="30" background="images/main/results_bg.jpg">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>


						<td width="3%">&nbsp;</td>
						<td width="80%">
						<s:if test="(result.pageCount>=1)">
						<span
							style="padding-left: 5px; color: #3F5356; font-size: 9pt;">
						<b><span id="from"> <s:property value="startIndex"
							escape="html" /></span></b> - <b><span class="to">0</span></b> of 
							</s:if> <b><span
							class="total" id="totalRecords"><s:property value="result.pageCount"
							escape="html" /></span> </b> <s:text name='execue.resultspage.resultsFoundFor.text' /> <b> <s:property
							value="result.queryName" escape="html" /> </b> </span></td>
						<td width="4%"><!-- select name=""><option>50</option></select-->
						<s:select cssClass="textBox1" id="resultsPerPage"
							name="resultsPerPage"
							onchange="javascript:setResultsPerPage(this.value)"
							list="#{'3':'3','5':'5','7':'7','10':'10','20':'20','50':'50','100':'100' }" />

						</td>
						<td width="10%"
							style="color: #3F5356; white-space: nowrap; padding-left: 5px; padding-right: 15px;"><s:text name='execue.resultspage.resultsPerPage.text' /></td>
						<td width="1%" style="color: #3F5356">
						<div align="center"
							style="white-space: nowrap; padding-top: 1px; padding-right: 10px;">
						<span style='font-size: 10pt'><pg:paginate /></span></div>
						</td>
					</tr>
				</table>

				</td>
			</tr>

		</table>
		</div>


		</td>
	</tr>
</table>
<div id="userInfoDynamicDivOuter" style="display:none;z-index:10000;width:400px;height:335px;position:absolute;left:150px;top:150px;background-color:#ffffff;padding:5px;" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td background="images/main/user_tab.png" width="151" height="22" valign="bottom">
    
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="left"> <span id="lightBoxHeading" class="whiteFont" >User Profile</span></td>
    <td align="right" valign="bottom" style="padding-right:5px;"><a href="#"  id="closeUserInfo" ><img src="images/main/closeUserInfo.png" border="0"  /></a></td>
  </tr>
</table>

   
    
    </td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
</td>
  </tr>
</table>

<table width="96%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><div style="border:5px solid #666666;background-color:#ffffff;width:100%;height:300px;">
    <div id="userInfoDynamicDiv"></div>
    </div></td>
  </tr>
</table>

</div>
<div id="showBigImage" 	style="display: none;  overflow: auto; width: auto;; min-height: 1px;height:auto;position:absolute;z-index:10000;left:0px;top:0px;border:3px soild #333;"><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#666666" align="right" style="padding-right:3px;"><a href="javascript:closeImage();"><img src="images/main/closeUserInfo.png" border="0" /></a></td>
  </tr>
  <tr>
    <td width="200" height="200" bgcolor="#FFFFFF" align="center" id="imageTd" style="border:1px solid #D3E1F9;padding:2px;background:#ffffff url(images/main/uss/Loader.gif) center center no-repeat"> <div id="bigImg" style="display:none;" ></div></td>
  </tr>
</table>

<input type="hidden" value="<s:property value='result.queryName' escape='html' />" id="resultQueryName" />
</div>




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
var fromunrecog=true;
var modelId="";
var page="results";
function closeImage(){
$("#showBigImage").hide();	
}
function showHideImageViewU(index){
	$linkText=$("#viewLink"+index).text();
	if($linkText=="Image View"){
	$("#viewLink"+index).text("List View");
	$("#listViewU"+index).hide();
	$("#imageViewU"+index).show();
	}else{
		$("#viewLink"+index).text("Image View");
	$("#listViewU"+index).show();
	$("#imageViewU"+index).hide();
	}
}
$(document).ready(function(){					
		
		
		$.each($("#reportResults img.listimage"),function(k,v){
		 if($(this).attr("src")==""){ 
			$(this).attr("src","images/main/noImage.png");
			$(this).css({"width":"90px","height":"60px"});
		 }
		});
		$.each($("a.reportHeaderLink"),function(){
		var $text=$.trim($(this).text());
		$(this).attr("title",$text);
		$(this).attr("alt",$text);
		$(this).text($text.substring(0,14));
		$(this).append("...");
		
		});
		$("img.listimage").click(function(){
			if($(this).attr("src")!="images/main/noImage.png"){ 
			//$("#bigImg").attr("src",$(this).attr("src"));	
			$img=$(this);
			$("#bigImg").empty().append($img.clone().removeAttr("width").removeAttr("height").css("cursor","default")).show();
			$("#showBigImage").show();
			var $left=$(this).position().left;
			var $top=$(this).position().top;
			$("#showBigImage").css("left",$left+"px");
			$("#showBigImage").css("top",$top+"px");
			}
			
		});
		
		
				Pane_Left=0;
				Pane_Top=0;	
	            var left=(screen.width)-(screen.width/2)-100;
				var top=(screen.height)-(screen.height/2)-280;
				$("a.reportLink").click(function(){	
						tempImg = $("#waiting_img2").attr("src");
						if($(this).find('img').attr('src')	!="images/main/CSVFILE.jpg"){					 
				//$("#pageLoader").show().css("left",left+"px");
		        $("#pageLoader").css("top",top+"px");
				setTimeout('document.images["waiting_img2"].src = tempImg', 1); 
						}
		        });
    $("a#bookmarksIdResults").click(function(){
       var sourceType = $("#typeId").val();       
       var requestString=$("#requestedStringId").val();    
     
      var queryName=  '<s:property value="result.queryName" escape="html" />';
      //alert(queryName +"  "+requestString);  
    
       var queryToBeBookmarked = $("#requestedStringId").val();
       $("#requestId").val(queryToBeBookmarked);
       if(sourceType=="QI"){
        $("#bookmarkType").val("<%=com.execue.core.common.type.BookmarkType.QUERY_INTERFACE %>");
       }else if(sourceType=="SI"){
         $("#bookmarkType").val("<%=com.execue.core.common.type.BookmarkType.SEARCH_INTERFACE%>");
       }       
   		Pane_Left=$("#bookmarksIdResults").position().left;
		Pane_Top=$("#bookmarksIdResults").position().top;
		if(Pane_Left>800){Pane_Left=800;}
		if(Pane_Left<200){Pane_Left=200;}
		maxRecords=2;
		$("#showSaveQueryLink").hide(); 	
		$("#loadingShowSaveQueryLink").show(); 	
		$.get("bookmark/showUserFolders.action", {ajax: 'true'}, function(data) { 
																		  
			$("#loadingShowSaveQueryLink").hide(); 
			$("#showSaveQueryLink").show(); 	
			
			$("#hiddenPaneContent").empty(); 
			$("#hiddenPaneContent").append(data);
			$("#hiddenPane").css("left",Pane_Left-120); 
			$("#hiddenPane").css("top",Pane_Top+20); 
			$("#hiddenPane").fadeIn("slow"); 
			$("#hiddenPaneContent").css("height",160+"px");
		//$("#hiddenPaneContent").css("height","auto");
		$("#hiddenPaneContent").css("width",290+"px");
			//$("ul#browser li ul").hide();

			doDrag();
			if(data.indexOf("SIGN IN")>-1){
			$("form#s_form input#userName").select().focus();
			}else{
			$("ul#browser li ul").hide();
			$("form#form1 input#bookmarkName").focus();
			$("#hiddenPaneContent").css("height",100+"px");
			}
			if(sourceType=="SI"){
			  $("#bookmarkName").val(requestString);
			}else{
			  $("#bookmarkName").val(queryName);			
			}
		});
		
	});
				if($("#requestRecognisedDiv").html() && $("#requestRecognisedDiv").html().length==0  ){ $("#trRequest").hide();}
				if( $("#messageDiv").html().length==0 ){ $("#trMessage").hide();}
				//alert($("span#Gfrom").html()+"%)^)%%");
				//alert(rCount+" rCount");
				$("span.to").html((rCount+parseInt($("span#from").html())-1));
				
				/*if(rCount>0){
					$("span#from").html("1");
				}*/
		var windowUrl = window.location;
        $.each($("a.addthis_button"),function(){
        	var currentUrl = $(this).attr("addthis:url");
        	var sharedUrl = windowUrl + currentUrl;
        	$(this).attr("addthis:url" ,sharedUrl);
        });
        
        
						   for(i=0;i<tokenCandidateWords.length;i++){
						   $divContent=$("<div style='width:auto;float:left;padding:2px;'></div>").attr("id","token"+i);
						   $divContent.html(tokenCandidateWords[i]);
						   $("#tokenStrings").append($divContent);
						   
						   }
	// code for dynamic reports
/*	var tkrName='<s:property value="result.entityBasedRedirection.tkrName"/>';
	if(tkrName){
	  var url= '<s:property value="result.entityBasedRedirection.redirectUrl"/>'; 
	  $.get("finance/"+url+"?tkrName="+tkrName, function(data) { 
		$("#dynaReportTd").hide();
		$("#dynaReportTd").empty(); 
		$("#dynaReportTd").append(data);
		$("#dynaReportTd").show();
	});
	}*/
	
	// end of code for dynamic reports
	
});
</script>
<script>
function setValues(entityId,idx){
	var $data=$("#hiddenData"+idx).data("data");
	var x=Number(idx)+1;
	var $data2=$("#hiddenData"+x).data("data");
	//alert("data:;"+$data.length);
	//alert("idx::"+idx);
 var name=document.getElementById("entities["+idx+"].name");
 var type=document.getElementById("entities["+idx+"].type");	
 name.value=entityId.options[entityId.selectedIndex].text;
 type.value=globalType[idx][entityId.selectedIndex];
 var selIndex=entityId.selectedIndex;

 $("#HiddenFieldsToServer"+idx).empty();
 						   			for(j=0;j<$data[selIndex].length;j++){
										//alert("word:::"+$data[entityId.selectedIndex][j]);
										
										$hiddenFields=$("<input type='hidden'></input>").attr("id","entities["+idx+"].words["+j+"]");
										$hiddenFields.attr("name","entities["+idx+"].words["+j+"]");
										$hiddenFields.val($data[entityId.selectedIndex][j]);
										$("#HiddenFieldsToServer"+idx).append($hiddenFields);
									}
matchList= compareListOfValues($data,$data2,idx,selIndex);
 if(matchList){
 $("#token"+idx).html(name.value);
 $("#token"+1).html("");
 }
 
}
function compareListOfValues($data,$data2,idx,selIndex){
	
	//alert($data[selIndex].length);
	//alert($data2[0].length);
	matchList=false;
								for(j=0;j<$data[selIndex].length;j++){
										//alert("::"+j);
										d=$data[selIndex][j];
										d1=$data2[0][j];
										
										//alert(d+":::::::::"+d1);
										
										if(d.toString==d1.toString){
										//alert("in");
										matchList= true;
										}else{
											matchList= false;
										}
									
									}
	 //alert("matchList::"+matchList);
	 return matchList;
 }
function createRequest(){
var req=$("#requestedUserQueryId").val().toLowerCase();
var newReq="";
for(i=0;i<wordCount;i++){
	var name=document.getElementById("entities["+i+"].name").value.toLowerCase();
	var original=document.getElementById("entities["+i+"].original").value.toLowerCase();
	req=req.replace(original,name);
	
}
$("#requestedUserQueryId").val(req);
//alert("req::"+req);   
//alert($("#requestedStringId").val());
//alert($("#requestedStringId").attr("id"));
//alert($("#requestedStringId").val());
//alert($("#dForm").serialize());
return true;
}

if($("#totalRecords").text()==""){$("#paginationSpace").hide(); $("#trRequest").hide(); }	

function showMore(divName, id){
	$("#"+divName).slideDown(); 
	$("#"+id+" img").attr("src","<c:out value="${basePath}"/>/images/main/less-2.png");
	$("#"+id).attr("href","javascript:hideMoreLink('"+divName+"','"+id+"');");
}
function hideMoreLink(divName, id){
	$("#"+divName).slideUp(); 
	$("#"+id+" img").attr("src","<c:out value="${basePath}"/>/images/main/more-2.png");
	$("#"+id).attr("href","javascript:showMore('"+divName+"','"+id+"');");
	
}

function getDynamicUnstructuredForm(){
 var unstructuredForm = document.createElement("FORM");
 document.body.appendChild(unstructuredForm);
 unstructuredForm.method = "POST";
 return unstructuredForm;
}
$(".moreResult,.moreResultList").click(function(){
   var uAppId=$(this).attr("uAppId");
   var request=$("#unstructureAppRequestId").val(); 
   var submitForm = getDynamicUnstructuredForm();
   //alert(uAppId);
   var appId = document.createElement("input");
   appId.setAttribute("type", "hidden");
   appId.setAttribute("name", "applicationId");
   appId.setAttribute("value",uAppId);
   
   var requestString = document.createElement("input"); 
   requestString.setAttribute("type", "hidden");
   requestString.setAttribute("name", "userQuery");
   requestString.setAttribute("value", request);
   
   var fromResemantification = document.createElement("input"); 
   fromResemantification.setAttribute("type", "hidden");
   fromResemantification.setAttribute("name", "fromResemantification");
   fromResemantification.setAttribute("value", "N");
  
   
   submitForm.appendChild(appId);
   submitForm.appendChild(requestString); 
   submitForm.appendChild(fromResemantification); 
   submitForm.action= "unstructuredSearch.action";
   submitForm.submit();
   
});



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
function submitforResemantification(){
	
   var uAppId=$("#moreResult").attr("uAppId");
   var request=$("#unstructureAppRequestId").val(); 
   var submitForm = getDynamicUnstructuredForm();
   
   var appId = document.createElement("input");
   appId.setAttribute("type", "hidden");
   appId.setAttribute("name", "applicationId");
   appId.setAttribute("value",uAppId);
   
   var requestString = document.createElement("input"); 
   requestString.setAttribute("type", "hidden");
   requestString.setAttribute("name", "userQuery");
   requestString.setAttribute("value", request);
  
    var fromResemantification = document.createElement("input"); 
   fromResemantification.setAttribute("type", "hidden");
   fromResemantification.setAttribute("name", "fromResemantification");
   fromResemantification.setAttribute("value", "Y");
  
   submitForm.appendChild(appId);
   submitForm.appendChild(requestString); 
   submitForm.appendChild(fromResemantification); 
   submitForm.action= "unstructuredSearch.action";
   submitForm.submit();
   
}
</script>
  <script src="js/main/uss/knowledge.js" ></script>
<div></div>
