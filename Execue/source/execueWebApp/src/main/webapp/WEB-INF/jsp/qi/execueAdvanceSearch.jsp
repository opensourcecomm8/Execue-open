<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
.top-bg {
	background-image: url(images/results_header_bg.jpg);
	background-repeat: repeat-x;
	background-position: left bottom;
	height: 25px;
	background-color: #FBFBFB;
}

.technology-1 {
	background-image: url(images/top-bg-inner-2.png);
	background-repeat: repeat-x;
	background-position: left top;
}
</style>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td class="top-bg">
		<div style="margin: auto; width: 91%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="8%">&nbsp;</td>
				<td width="92%" align="center">
				<table width="100%" border="0" cellspacing="0" cellpadding="3"
					align="right">
					<tr>
						<td width="56%" height="20" align="center"><!-- sub links starts -->
						&nbsp;</td>
						<td width="30%" align="center">
						<div id="showWelcome"
							style="padding-right: 20px; color: #333; padding-top: 0px; padding-right: 18px; font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif; text-align: right; width: 500px; margin: auto;">
						</div>
						</td>
						<td width="7%" align="right"><a href="feedback.jsp"><s:text name="execue.feedback.link" /></a></td>
						
                        
                        <td width="1%" id="publisherTdSeperator" style="display:none;padding-left:10px;padding-right:10px;" >|</td>
						<td width="6%" align="center" id="publisherTd" style="display:none">
						<div id="showAdminLink"><a href="swi/showConsole.action"
							id="adminId"><s:text name="execue.publisher.link" /></a></div>
						<div id="loadingShowLoginLink1" style="display: none;"><img
							src="images/loaderTrans.gif"></div>
						</td>
                        
                        
                        
						<td width="2%" style="padding-left:10px;padding-right:10px;">|</td>
						<td width="6%" " style="white-space: nowrap;"><span
							id="showLoginLink" style="padding-left: 3px;"><a
							href="javascript:;" class="links_sem3" id="loginId"><s:text
							name="execue.login.link" /></a> <a
							href="<c:url value='/j_spring_security_logout'/>"
							class="links_sem3" id="logoutId" style="display: none;"><s:text
							name="execue.logout.link" /></a></span> <span id="loadingShowLoginLink"
							style="display: none;"><img src="images/loaderTrans.gif"></span>

						</td>
						
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
	<tr>
		<td class="technology-1" height="52">
		<div style="margin: auto; width: 91%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="8%" height="50" valign="top">
				<div
					style="padding-top: 10px; padding-left: 0px; padding-right: 0px; padding-bottom: 1px;"><a
					href="index.jsp"><a href="index.jsp"><img
					name="index_r1_c1" src="images/inner-page-logo.png" border="0"
					id="index_r1_c1" alt="" /></a></a></div>
				</td>
				<td width="92%" align="left"><!--jsp:include page="../../../freeFormSearch.jsp"  flush="true" /--></td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>

<div style="width: 90%; margin: auto;">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td height="25" class="normalText" background="images/bc-bg.jpg">
		<span style="padding-left: 1px; color: #333;"> <a
			href="execueHome.action?type=SI" class="breadCrumbLink">Home</a><Span
			class="textArrow"> >> </Span> <span class="breadCrumbNoLink">Advanced
		Search</span> </span></td>
	</tr>
</table>
<table width="806" border="0" align="center" cellpadding="2"
	cellspacing="0">
	<tr>
		<td width="100" align="left">
		<div><span style="font-weight:bold; white-space:nowrap;">
		Select an app</span></div>
		</td>
		<td width="650" align="left"><select id="selectApp"
			style='height: 22px; font-size: 14px; color: #525252; margin-top: 2px;'>
			<s:iterator value="applications" status="list">
				<s:if test="#list.index==0">
					<option selected="selected" modelId="<s:property value='modelId'/>"
						applicationUrl="<s:property value='applicationURL'/>" value="<s:property value='applicationId'/>"><s:property
						value="applicationName" /></option>
				</s:if>
				<s:else>
					<option modelId="<s:property value='modelId'/>"
						applicationUrl="<s:property value='applicationURL'/>" value="<s:property value='applicationId'/>"><s:property
						value="applicationName" /></option>
				</s:else>
			</s:iterator>
		</select> <input type="text" value="Choose a Date" style="display:none;" id="rangeA" />	
        <!--input type="text" value="Choose a Date" style="display:none;" id="rangeA_Cohort" /-->	
        <input type="hidden" value="<s:property value='qiJSONString' />"  id="qiJSONString" />
        <input type="hidden" value=""  id="currentDiv" />
        </td>
	</tr>

</table>
</div>

<div id="content">

<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
	<tr>
		<td valign="top" bgcolor="#FFFFFF" id="tdHeight"><!-- height imp -->
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr> <td id="tdHeightMargin" align="center" valign="bottom"></td>
				<!-- td id="tdHeightMargin" align="center" valign="bottom">
				<div id="pleaseWaitDiv"
					style="display: none; margin: auto; width: 133px; margin-top: 5px; margin-bottom: 5px;"><img
					id="waiting_img" src="images/wait.gif" width="133" height="11" /></div>
				</td-->
			</tr>
			<tr>
				<td valign="top">
				<form id="qiSearchForm" action='querySearch.action' onSubmit="return submitData(true)"
					accept-charset="utf-8" method="post"><input type="hidden"
					name="request" id="request" /><input type="hidden" name="type"
					id="type" value="QI" />
					<input type="hidden" name="modelId" id="modelId" value=""/>
					<input type="hidden" name="applicationId" id="applicationId" value=""/>
				<table width="96%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td align="center">
						<table width="802" border="0" cellspacing="0" cellpadding="0"
							align="center">
							<tr>
								<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="68%" height="25" align="left">
										<div id="div_caption" ><strong>Search databases for business metrics including ..</strong></div>

										<!-- sub links starts --></td>
										<td width="8%" align="right" style="padding-right:10px;" ><!--div class="sub_links_qs" style="padding-left:0px;float:left"><img src="images/toggle_show1.jpg"  vspace="3" alt="Show Query Snapshot" id="toggleSnapshot"></div-->
										<a href="javascript:;" class="links_blue" id="showQS"
											style="display: none">Show Query Snapshot</a></td>
										<td width="8%" align="right">
										<div><a href="#" class="links_blue" id="clearPage">Clear
										Page</a></div>

										</td>
										<td width="8%" align="right" style="padding-left:20px;padding-right:20px;">
										<div id="showBookmarksLink"><a href="javascript:;"
											class="links_blue" id="bookmarksId">Save Query</a></div>
										<div id="loadingShowBookmarksLink"
											style="display: none; padding-left: 20px; padding-right: 20px;"><img
											src="images/loaderTrans.gif"></div>

										</td>
										<td width="8%" align="right">
										<div id="showBookmarksSearchLinkBi" style="float: right;"><a
											href="javascript:;" class="links_blue" id="bookmarksSearchIdBi">Saved
										Queries</a></div>
										<div id="loadingShowBookmarksSearchLinkBi"
											style="display: none; padding-left: 33px; padding-right: 33px;"><img
											src="images/loaderTrans.gif"></div>
										</td>
									</tr>
								</table>

                           </td>
                        </tr>
                      </table>

						</td>
					</tr>
					<tr>
						<td>

						<div id="divcontainer" >
                <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td height="20" align="left" >&nbsp;</td>
                    <td width="530" rowspan="2" align="left" height="50" valign="top" >
                    <!-- Select metrics  starts -->
                     <div id="business_metrics" style="padding-bottom:0px;padding-top:10px;" >
                    <!--div class="search_header_text_qi_metrics" style="width: 530px"><span
									style="padding-right: 65px;">Search databases for
								business metrics including .. </span><a href="" id="adv_metrics"
									style="font-size: 9px; color: #D1DFEF;">Show Advanced Metrics</a></div-->
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
  <td width="2"><img src='images/1pix.gif' width='10' height='1' /></td>
    <td width="530"><div id="selectMetrics"	style="padding-top: 0px; float: left; padding-left: 0px; display: none;"></div></td>
    <td align="left" valign="top"><div id="search_image"><input type="image" name="search"
							id="searchBtn" src="images/qi_search.jpg" alt="Search" title="Search" tabindex="-1" width="90" height="27" /></div></td>
  </tr>
  <tr>
  <td width="2"><img src='images/1pix.gif' width='10' height='1' /></td>
    <td width="536" align="right"><span class="smallTextWhite"><s:text name="execue.metrics.example" /></span></td>
    <td align="left"></td>
  </tr>
</table>

								<div id="selectMetrics_tb"
									style="padding-top: 0px; float: left; padding-left: 0px;"><input
									id="selectMetrics_textbox" type="text"
									 value="" /></div>
                                     
                                    <!--messages starts -->
						<div id="messagesDiv" style="width: 530px; height:0;height:auto;display:none">
						<div class="divBorderWhite" style="height: auto; width: 530px;float:left;text-align:left">
						<table width="530" border="0" cellspacing="0" cellpadding="0"
							align="center">
							<tr>
								<td align="left">
								<div id="messages"
									style="width: 500px; margin-left: 10px; float: left;">
									<!-- error message starts-->								
								<div id="errorMessage"
									style="color: #fff; width: 500px; text-align: left; padding: 5px; padding-left: 12px;">
								Error :<span id="errorId"></span> </div>							
								<!-- error message ends--> <!-- error suggestion  message starts-->

								
									<div id="suggestionMessage"
										style="width: 500px; text-align: left; padding: 5px;">
									<script>
										/* $(document).ready(function () {
										$("#business_metrics ul.qiSuggestTerm-holder").hide();
										$("#selectMetrics_tb").show();
										$("#adv_metrics").html("Close Advanced Metrics");
										$("#messagesDiv").show(); 
																	 });*/
										</script> 
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="20%" valign="top"><span style="color: #fff;">Suggestion
											:</span></td>

											<td width="80%"><!-- list start -->
											<table width="100%" border="0" cellspacing="0"
												cellpadding="2">
																							
													<tr>
														<td width="4%"><div id="suggestionsId"></div></td>
													</tr>
											
											</table>
											<!-- list ends --></td>
										</tr>
									</table>
									</div>
							</div>
								</td>
							</tr>
						</table>
						</div>
						</div>
						<!-- messages ends -->
								</div>
                    <!-- Select metrics ends -->
               <div id="showAllComps">        
                    
                     <!-- advanced option population  start -->
              <div id="population" >
               <div class="search_header_text_qi" >For a Population of .. </div>
                  
                     <div class="pad10px"  >
                     <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="535"><div id="advOpt_population" style="padding-top: 0px; float: left; padding-left: 0px;wisth:530px "> </div></td>
                            <td align="left"> <span class="smallTextBlack"><s:text name="execue.population.example" /></span></td>
                          </tr>
                        </table>
                     </div>
                    
                
                <!-- advanced option population  ends -->
                
                <!-- advanced option condition  start -->
              </div>
              <div id="condition" style="display:none;">
               
                 <div class="search_header_text_qi" >Which meets these criteria or conditions ..<span id="sampleValueId" ></span></div>
                 <div class="pad10px"  >
                   <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="535"><div id="advOpt_condition" style="padding-top: 0px; float: left; width: 530px;"> </div>
                   </td>
    <td  align="left"><span class="smallTextBlack" ><s:text name="execue.condition.example" /></span></td>
  </tr>
</table>

                   </div>
                  
               
              </div>
              <!-- advanced option condition  ends -->
              
               <div class="search_header" id="moreDiv" style="display:none;padding-bottom:3px;"><div style="width:17px;float:left;"><img src="images/toggle_show.jpg" vspace="3" alt="More Options" title="More Options" id="toggleMore"></div><div class="cohort_heading" id="more" style="width:600px;"><strong>More...</strong></div></div>
               
              <!-- advanced option groups  start -->
              <div id="groups" style="display:none;">
                <div class="search_header_text_qi" >Summarize and present by..</div>
                <div class="pad10px" > 
                <table width="10" border="0" cellspacing="0" cellpadding="0">
  <tr>
   <td width="535"><div id="advOpt_summarize" style="width:530px;"> </div></td> <td width="100"><div class="smallTextBlack" style="width:150px"><s:text name="execue.summerize.example" /></div></td>
  </tr>
</table>

                 </div>
                
              </div>
              <!-- advanced option groups  ends -->
              
              <!-- advanced option tb  start -->
              <div id="tb" style="display:none;width:681px;">
                <div class="search_header_text_qi" >Top & Bottom ..</div>
                  <div class="pad10px" ><div id="advOpt_tb" > </div></div>
                   </div>
              
              <!-- advanced option tb  ends -->
              
              <!-- advanced option cohort  start -->
              <div id="cohort" style="display:none;">
               
                 <div class="search_header" id="cohort_title" style="display:inline;padding-bottom:3px;"><div style="width:17px;float:left;"><img src="images/toggle_close.jpg" vspace="3" alt="Cohort Options" title="Cohort Options" id="toggleSnapshotCohort"></div><div class="cohort_heading"><strong>Limit results to a cohort group of ...</strong></div></div>
                 <div id="cohortToggleDiv">
                  <div class="search_header_text_qi" style="padding-top:5px;" >Which meets these criteria or conditions ..<span id="cohortSampleValueId" ></span></div>
                    <div class="pad10px"><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="535"><div id="advOpt_cohort_condition" style="width:530px;"></div>
                    </td>
    <td align="left"><span class="smallTextBlack"><s:text name="execue.cohort.condition.example" /></span></td>
  </tr>
</table>

                    </div>
                    <!-- <a href="#"><img src="images/celar.jpg" alt="Clear Condition" name="clearCohortCondition"  border="0" id="clearCohortCondition" title="Clear Condition" /></a>
                    <img src="images/addCondition.gif" align="middle"
							id="addCohortCondition" alt="Add Condition" title="Add Condition"/> -->
                            
                            <div id="clearAndConditionButtonsCohort" >
                   <!--div id="clearButton"><a href="#"><img id="clearCondition" src="images/celar.jpg" title="Clear Condition" alt="Clear Condition"  border="0" /></a> </div-->
                   <!--div id="conditionButtonCohort"><img hspace="3" src="images/addConditionNew.gif" align="left" id="addCohortCondition" alt="Add Condition" title="Add Condition" /></div--></div>
                   
                   
                             <div class="search_header_text_qi" >Summarize and present by..</div>
                            <div class="pad10px" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="535"><div id="advOpt_cohort_summarize" style="width:530px;" ></div>
                             </td>
    <td align="left" ><span class="smallTextBlack"><s:text name="execue.cohort.summerize.example" /></span></td>
  </tr>
</table>

                            </div>
                  </div>          
              </div>
              
          </div>
              <!-- advanced option cohort  ends -->
                    </td>
                    <td width="155" rowspan="2" align="left" valign="top" >&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="24" align="left" >&nbsp;</td>
                    </tr>
                  <!--TR >
                    <TD  >&nbsp;</TD>
                    <TD  colspan="2" align="left"  >
                      <div style="display:inline;">
                        <div style="width:160px;float:left;paddin-right:5px;">
                          <a href="#" >
                            <img src="images/c1_qi_advanced_options.gif" alt="Advanced Options" name="Image3" width="147" height="27" border="0" align="left" id="Image3" title=								"Advanced Options" onMouseOver="MM_showMenu(window.mm_menu_0105220345_0,0,27,null,'Image3')"
	onMouseOut="MM_startTimeout();" />
                            </a>
                          </div>
                          
                          
                         
                      </div>
                      
                      </TD>
                    </TR -->
                  <tr>
                    <td>
                </TABLE>
              </div>
						</td>
					</tr>
					<!--tr>
            <td height="35" ><div id="div_qs">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="7%">&nbsp;</td>
                  <td width="4%"><img src="images/toggle_show.jpg" width="19" height="16" alt="Show Query Snapshot" id="toggleSnapshot"></td>
                  <td width="89%">Query Snapshot</td>
                </tr>
              </table>
            </div></td>
          </tr-->
					<tr id="showAllContainer">
						<td height="35" align="left" valign="top">
						<div style="width: 802px; margin: auto;">
						<div class="divBorderWhite" style="height: auto; width: 800px;">
						<div id="showAll"></div>
						</div>
						</div>
						</td>
					</tr>
					<tr>
						<td  align="left" valign="top"><!-- div id="pleaseWaitDiv" style="display:none;margin:auto;width:133px;margin-top:5px;margin-bottom:5px;"><img  id="waiting_img" src="images/wait.gif" width="133" height="11" /></div-->
						<div id="query" style="width: 802px; margin: auto;margin-bottom:20px;">
						<div class="divBorderWhite" style="height: auto; width: 800px;z-index:0;background-color:#fff">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="97%"><div class="sub_links_qs" style="padding-left:17px;"><img src="images/arr1.jpg"  vspace="3"></div><div class="sub_links_qs" style="padding-left:3px;"><b>Query Snapshot</b></div></td><td width="3%"><span id="closeQS"><img src="images/deleteButton1.jpg" width="13" height="13" alt="Close" border="0" /></span></td>
  </tr>
</table>

						<table width="100%" border="0" cellspacing="0" cellpadding="0" id="qs">
							<tr>
								<td><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><div id="ss" style="z-index:0;position:static">
                          
								<table width="95%" border="0" align="right" cellpadding="0"
									cellspacing="0">
									<tr id="tr_ss_metrics">
										<td >
										<div id="ss_metrics" class="snapshotText"></div>
										</td>
									</tr>
									
									<tr id="tr_ss_condition">
										<td>
										<div id="ss_condition" class="snapshotText"></div>
										</td>
									</tr>
									<tr id="tr_ss_summarize">
										<td>
										<div id="ss_summarize" class="snapshotText"></div>
										</td>
									</tr>
									<tr id="tr_ss_TB">
										<td>
										<div id="ss_TB" class="snapshotText"></div>
										</td>
									</tr>
								<tr id="tr_ss_population">
										<td>
										<div id="ss_population" class="snapshotText"></div>
										</td>
									</tr>
								</table>
								</div></td>
  </tr>
  <tr>
    <td><div id="ss_cohort" class="search_header"
									style="width: 100%; float: left; margin-top: 10px; padding-left: 5px;z-index:0;position:static">
								<!--span style="padding-left:33px;color:#666"><strong><s:text name="execue.cohort.snapshot.description"/></strong></span-->
								<table width="92%" border="0" align="center" cellpadding="0"
									cellspacing="0">
									<tr id="tr_ss_cohort_condition">
										<td>
										<div id="ss_cohort_condition" class="snapshotText"></div>
										</td>
									</tr>
									<tr id="tr_ss_cohort_summarize">
										<td>
										<div id="ss_cohort_summarize" class="snapshotText"></div>
										</td>
									</tr>
								</table>
								</div></td>
  </tr>
</table>

								
								
								</td>
							</tr>
						</table>

						<!-- cohort starts --> <!-- cohort ends --></div>
						</div>


						
                        
                        
                        </td>
					</tr>
					<tr>
						<td ><div style="width: 802px; margin: auto;">
						<!--div class="divBorderWhite4" style="height: 190px; width: 750px;">
						<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
  <tr>
    <td width="58">&nbsp;</td>
    <td height="25" align="left" ><span style="color:#036"><strong>Featured Examples</strong></span></td>
  </tr>
  <tr>
    <td height="25">&nbsp;</td>  
    <td align="left"> &nbsp; &#8226; &nbsp; <a href="#" id="companyFinancialId">Company Financials</a></td>
  </tr>
</table<table width="90%" align="left" border="0" cellpadding="0" cellspacing="0">
  <tbody><tr>
    <td width="20">&nbsp;</td>
    <td align="left" height="25"><span style="color: rgb(0, 51, 102);"><strong>Company Financials </strong></span><span id="moredata" style="color:#333;font-size:12px;"></span></td>
  </tr>
  <tr>
    <td height="15">&nbsp;</td>
    <td align="left">You can search on the fundamentals of any traded company. Enter query to request Balance Sheet, Income Statement and Cash Flow information.
    Each query gets you multiple presentation ready reports.
    Login to access many years of historical company financials. Select one of the following samples and click on the search button: </td>
  </tr>
    <tr>
      <td height="25">&nbsp;</td>
      <td align="left"> &nbsp; &bull; &nbsp; <a href="#" id="companyFinancialIdQ1" tabindex="-1"><s:text name="execue.advancesearch.query1"/></a></td>
  </tr>
      <tr>
        <td height="25">&nbsp;</td>
        <td align="left"> &nbsp; &bull; &nbsp; <a href="#" id="companyFinancialIdQ2" tabindex="-1"><s:text name="execue.advancesearch.query2"/></a></td>
  </tr>
        <tr>
          <td height="25">&nbsp;</td>
          <td align="left"> &nbsp; &bull; &nbsp; <a href="#" id="companyFinancialIdQ3" tabindex="-1"><s:text name="execue.advancesearch.query3"/></a></td>
  </tr>
        <tr>
          <td height="25">&nbsp;</td>
          <td align="left"> &nbsp; &bull; &nbsp; <a href="#" id="companyFinancialIdQ4" tabindex="-1"><s:text name="execue.advancesearch.query4"/></a></td>
  </tr>
</tbody>
</table>

						</div-->
						</div></td>
					</tr>
					<tr>
						<td align="center" valign="middle">
							<div id="pleaseWaitDiv" style="display: none; margin: auto; width: 133px; margin-top: 5px; margin-bottom: 5px;">
								<img id="waiting_img" src="images/wait.gif" width="133" height="11" />
							</div>
						</td>
					</tr>
					
					<tr>
						<td><input type="hidden" name="request" id="requestId" /></td>
						<td><input type="hidden" value="<%=com.execue.core.common.type.BookmarkType.QUERY_INTERFACE%>" id="bookmarkType" /></td>
					</tr>
                    <!--tr>
						<td><div id="left"></div><div id="top"></div></td>
					</tr-->
				</table>
				</form>
				</td>
			</tr>
		</table>


		</td>
	</tr>
</table>
</div>
 <div class="smallTextBlack" style="width: 145px; height:10px; display:none;position:relative;top:-20px;" id="tbText"><s:text
											name="execue.topbottom.example" /></div>  
<div class="ui-default-message">Sorry ! Follow Same Date format </div>                      
<script type="text/javascript">
/*  var uservoiceJsHost = ("https:" == document.location.protocol) ? "https://uservoice.com" : "http://cdn.uservoice.com";
  document.write(unescape("%3Cscript src='" + uservoiceJsHost + "/javascripts/widgets/tab.js' type='text/javascript'%3E%3C/script%3E"))
  */
</script>
<script type="text/javascript">
/*UserVoice.Tab.show({ 
  key: 'semantifi',
  host: 'semantifi.uservoice.com', 
  forum: '31752', 
  alignment: 'right',
  background_color:'#E8E6C0', 
  text_color: 'white',
  hover_color: '#FFFFFF',
  lang: 'en'
})
 
$("a#uservoice-feedback-tab").css("background-image","url(images/feedback_tab_white.png)");
*/
</script>
