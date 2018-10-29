<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="aw" uri="/WEB-INF/tlds/absorptionWizard.tld"%>
<style>
li.mytable ul{
margin:0px;
padding:0px;
padding-left:20px;
border:none;
}
.columnHolder,.conceptHolder,.tableHolder,.tablecolDisp,.memberDisp{
padding-left:4px;	
}
#mapsContent div.sugClassName,#instanceMapsContent div.sugClassName{
font-weight:bold;	
}
</style>
<div id="hiddenPane"
	style="position: absolute; width: 350px; height: 350px; z-index: 2; display: none;">	
	<input type="hidden" name="assetId" id="assetId"/>
<table border="0" cellpadding="0" cellspacing="0" width="335" align="left" >
        <!-- fwtable fwsrc="boxTemplate.png" fwpage="Page 1" fwbase="boxTemplate.gif" fwstyle="Dreamweaver" fwdocid = "901881679" fwnested="0" -->
        
        <tr>
          <td width="15"><img name="boxTemplate_r1_c1"
     src="../images/boxTemplate_r1_c1.gif" width="15" height="40"
     border="0" id="boxTemplate_r1_c1" alt="" /></td>
          <td background="../images/boxTemplate_r1_c2.gif"><img
     name="boxTemplate_r1_c2" src="../images/boxTemplate_r1_c2.gif"
     width="179" height="40" border="0" id="boxTemplate_r1_c2" alt=""/></td>
          <td width="15"><a href="#" id="closeButtonId" ><img
     name="boxTemplate_r1_c3" src="../images/boxTemplate_r1_c3_1.gif"
     width="15" height="40" border="0" id="boxTemplate_r1_c3"
     alt="Close" title="Close"/></a></td>
          <td width="13"><img src="../images/spacer.gif" width="1" height="40"
     border="0" alt="" /></td>
          </tr>
        <tr>
          <td background="../images/boxTemplate_r2_c1.gif"><img
     name="boxTemplate_r2_c1" src="../images/boxTemplate_r2_c1.gif"
     width="15" height="10" border="0" id="boxTemplate_r2_c1" alt="" /></td>
          <td width="1045" valign="top" bgcolor="#FFFFFF" >
            <div id="hiddenPaneContent"></div>
            </td>
          <td background="../images/boxTemplate_r2_c3.gif" ><img
     name="boxTemplate_r2_c3" src="../images/boxTemplate_r2_c3.gif"
     width="15" height="10" border="0" id="boxTemplate_r2_c3" alt="" /></td>
          <td><img src="../images/spacer.gif" width="1" height="10"
     border="0" alt="" /></td>
          </tr>
        <tr>
          <td><img name="boxTemplate_r3_c1"
     src="../images/boxTemplate_r3_c1.gif" width="15" height="13"
     border="0" id="boxTemplate_r3_c1" alt="" /></td>
          <td background="../images/boxTemplate_r3_c2.gif" valign="top"><img
     name="boxTemplate_r3_c2" src="../images/boxTemplate_r3_c2.gif"
     width="25" height="13" border="0" id="boxTemplate_r3_c2" alt="" /></td>
          <td><img name="boxTemplate_r3_c3"
     src="../images/boxTemplate_r3_c3.gif" width="15" height="13"
     border="0" id="boxTemplate_r3_c3" alt="" /></td>
          <td><img src="../images/spacer.gif" width="1" height="13"
     border="0" alt="" /></td>
          </tr>
        </table>
</div>

<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name="execue.mapping.pageHeading" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">
            <tr><td> <table width="98%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td height="23" width="50%"> <jsp:include page='/views/showSelectAsset.jsp' flush="true"/></td>
     
        <td height="20" width="50%" align="right"><!--div><a href="../swi/showAssets.action" class="arrowBg"><s:text name="showAssets.showAssets.linkName" /></a></div--></td>
      </tr>
    </table></td></tr>
			<tr><td style="padding-left:0px;padding-top:3px;padding-bottom:3px;">				
				<table width="98%" border="0" cellspacing="0" cellpadding="0" align="center" >
                  <tr>
                    <td width="100%"> <s:text	name="execue.mappings.description" /></td>
                    
                  </tr>
                </table></td>
			</tr>
			<!-- tr>
          <td valign="top" background="images/blueLine.jpg"><img src="../images/blueLine.jpg" width="10" height="1" /></td>
          </tr -->
			<tr>
				<td valign="top">

				<table width="400" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>


						<div id="container"
							style="width: 970px; height: 400px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;display:none;">
						<!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
						<div class="ui-layout-west"
							style="width: 230px; float: left; padding-left: 7px;">
						<!-- ////////////////////Asset tables pane  starts ////////////////////////////////////////////////////////////// -->
                        <div id="assetTablesPane" style="padding-left:3px;">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="27"><span class="tableSubHeading"><div id="atDiv"><s:text name="execue.mapping.asset.tables"/></div></span></td>
							</tr>
							<tr>
								<td>

								<div class="tableBorder" style="margin-top:3px;padding-top: 5px; width: 223px;height:355px;">
								<table width="99%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="30" align="left" valign="top">
										<div id="divAssetTables" style="margin-left:5px;">
                                            <div id="divAssetTablesSearch" style="position:relative;"></div>
										</div>
										</td>
									</tr>
									<tr>
										<td>
										<div class="tableListWithoutBorder"
											style="height: 315px; width: 210px;margin-left:5px;overflow-x:auto;padding-left:2px;">
										<div
											style="width: 190px; border-bottom: #E0E0E0 dashed 1px; height: 22px;"><input
											name="checkAllTables" type="checkbox" value=""
											id="checkAllTables" /> <span class="descriptionText"><s:text
											name="execue.global.selectAll" /></span></div>
										<div id="tableColumnsDiv" style="padding-top: 5px;">
                                         <!-- loader starts -->
                                       
                                       <div id="loadingATLink" style="display:none"  ><img
									src="../images/loaderAT.gif" ></div>
                                    
                                    <!-- loader ends -->
                                    
                                        </div>
                                        
										</div>
										</td>
									</tr>
								</table>

								</div>
								</td>
							</tr>
						</table>
                        
                        </div>
                        <!--/////////////////////////////// Asset tables pane close////////////////////////////////////// -->
                        
                        <!--/////////////////////////////// Column members pane starts ////////////////////////////////////-->
                        <div id="columnMembersPane" style="display:none;padding-left:2px;">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="27"><span class="tableSubHeading"><div id="atDiv"><s:text name="execue.mapping.column.members"/></div></span></td>
							</tr>
							<tr>
								<td>

								<div class="tableBorder" style="padding-top: 5px; width: 223px;height:355px;">
								<table width="99%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="30" align="left" valign="top">
										<div id="divColumnMembers" style="margin-left:5px;">
                                            <div id="divColumnMembersSearch" style="position:relative;"></div>
										</div>
										</td>
									</tr>
									<tr>
										<td>
										<div class="tableListWithoutBorder"
											style="height:  285px; width: 210px;margin-left:5px;padding-left:2px;">
										<div
											style="width: 200px; border-bottom: #E0E0E0 dashed 1px; height: 22px;"><input
											name="checkAllMembers" type="checkbox" value=""
											id="checkAllMembers" /> <span class="descriptionText"><s:text
											name="execue.global.selectAll" /></span></div>
										
                                        <div id="columnMembersDiv" style="padding-top: 5px;">
                                        
                                        <!-- loader starts -->
                                       
                                       <div id="loadingCMLink" style="display:none"  ><img
									src="../images/loaderAT.gif" ></div>
                                    
                                    <!-- loader ends -->
                                        </div>
										</div>
										</td>
									</tr>
								</table>
                                    <span id="memberSDXData"
											style="float: left; padding-left: 10px;">
											<table width="100" border="0" cellspacing="2" cellpadding="0" style="margin-top:3px;border:1px solid #CCC;padding-left:5px;padding-bottom:2px;">
												<tr>
													<td><span><img src="../images/previmgnew2.jpg"
														alt="First Page" id="memberSDXNavFirst" style="cursor:pointer;padding-top:2px;"/></span>
														<span><img src="../images/previmgnew2_disabled.jpg"
														alt="First Page" title="First Page" id="dmemberSDXNavFirst" style="padding-top:2px;"/></span></td>
													<td><span><img src="../images/previmgnew.jpg"
														alt="Previous Page" title="Previous Page" style="cursor:pointer;padding-top:1px;" id="memberSDXNavPrevious"/></span>
														<span><img src="../images/previmgnew_disabled.jpg"
														alt="Previous Page" title="Previous Page" id="dmemberSDXNavPrevious" style="padding-top:1px;"/></span></td>													
													<td><span><img src="../images/nxtimgnew.jpg"
														alt="Next Page" title="Next Page" style="cursor:pointer" id="memberSDXNavNext"/></span>
														<span><img src="../images/nxtimgnew_disabled.jpg"
														alt="Next Page" title="Next Page" id="dmemberSDXNavNext"/></span></td>
													<td><span><img src="../images/nxtimgnew2.jpg"
														alt="Last Page" title="Last Page"  style="cursor:pointer" id="memberSDXNavLast"/></span>
														<span><img src="../images/nxtimgnew2_disabled.jpg"
														alt="Last Page" title="Last Page" id="dmemberSDXNavLast"/></span></td>
												</tr>
								
											</table>
										</span>										
									<span class="fieldNames" id="memberSDXPage" style="padding-left: 10px;float:left;padding-top:5px;">page <span id="memberSDXPageno"></span>
										of <span id="memberSDXNumberofpages"></span></span>
								</div>
								</td>
							</tr>
						</table>
                        </div>
                         <!--/////////////////////////////// Column members pane Ends ////////////////////////////////////-->
						</div>
						<div id="seperatorLeft"></div>
						<div class="ui-layout-center" style="width: 230px; float: left"
							id="centerDiv">
                             <!--/////////////////////////////// Business Terms pane Starts ////////////////////////////////////-->
                             <div id="businessTermsPane" >
						<table border="0" cellpadding="0" cellspacing="0">
							<!-- fwtable fwsrc="../blueBox.png" fwpage="Page 1" fwbase="blueBoxLeft.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->
							<tr>
								<td height="30" class="tableHeading">
								<table width="100%" border="0" cellspacing="0" cellpadding="3">
									<tr>
										<td width="80%" valign="bottom" class="tableSubHeading"><div id="btDiv"><s:text name="execue.mapping.businessterms"/></div></td>
									</tr>
								</table>
								</td>
							</tr>
							<tr>
								<td width="158" valign="top">
								<div class="tableBorder" style="width: 228px;height:355px;margin-left:3px;">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="30" valign="top">
										<div id="divConcepts" style="margin-left:5px;">
                                            <div id="divConceptsSearch" style="position:relative;"></div>
										</div>
										</td>
									</tr>
									<tr>
										<td>
										<div class="tableListWithoutBorder"
											style="height: 315px; width: 215px;margin-left:5px;padding-left:2px;">
										<div
											style="width: 230px; border-bottom: #E0E0E0 dashed 1px; height: 22px;"><input
											name="checkAllConcepts" type="checkbox" value=""
											id="checkAllConcepts" /> <span class="descriptionText"><s:text
											name="execue.global.selectAll" /></span>
											</div>
										<div id="businessTermDiv" style="padding-top: 5px;width:100%;white-space:nowrap;">
                                         <!-- loader starts -->
                                       
                                       <div id="loadingBTLink" style="display:none" ><img
									src="../images/loaderBT.gif" ></div>
                                    
                                    <!-- loader ends -->
                                        </div>
                                       
										</div>
										</td>
									</tr>
								</table>
								</div>
								</td>
							</tr>
						</table>
                        </div>
                         <!--/////////////////////////////// Business Terms Pane Ends ////////////////////////////////////-->
                         
                          <!--/////////////////////////////// Instances pane Starts ////////////////////////////////////-->
                             <div id="instancesPane" style="display:none">
                             <table border="0" cellpadding="0" cellspacing="0">
							<!-- fwtable fwsrc="../blueBox.png" fwpage="Page 1" fwbase="blueBoxLeft.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->
							<tr>
								<td height="27" class="tableHeading">
								<table width="100%" border="0" cellspacing="0" cellpadding="3">
									<tr>
										<td width="80%" valign="bottom" class="tableSubHeading"><div id="btDiv"><s:text name="execue.mapping.instances"/></div></td>
									</tr>
								</table>
								</td>
							</tr>
							<tr>
								<td width="158" valign="top">
								<div class="tableBorder" style="width: 230px;height:355px;">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="30" valign="top">
										<div id="divInstances" style="margin-left:5px;">
                                            <div id="divInstancesSearch" style="position:relative;"></div>
										</div>
										</td>
									</tr>
									<tr>
										<td>
										<div class="tableListWithoutBorder"
											style="height:  285px; width: 220px;margin-left:5px;padding-left:2px;">
										<div
											style="width: 230px; border-bottom: #E0E0E0 dashed 1px; height: 22px;"><input
											name="checkAllInstances" type="checkbox" value=""
											id="checkAllInstances" /> <span class="descriptionText"><s:text
											name="execue.global.selectAll" /></span>
											</div>
										
                                        <div id="instancesDiv" style="padding-top: 5px;width:100%;white-space:nowrap;">
                                        
                                           <!-- loader starts -->
                                       
                                       <div id="loadingITLink" style="display:none" ><img
									src="../images/loaderBT.gif" ></div>
                                    
                                    <!-- loader ends -->
                                    
                                        </div>
										</div>
										</td>
									</tr>
								</table><span id="instancesPagination">
									<span id="instanceKDXData"
											style="float: left; padding-left: 10px;">
											<table width="100" border="0" cellspacing="2" cellpadding="0" style="margin-top:3px;border:1px solid #CCC;padding-left:5px;padding-bottom:2px;">
												<tr>
													<td><span><img src="../images/previmgnew2.jpg"
														alt="First Page" id="instanceKDXNavFirst" style="cursor:pointer;padding-top:2px;"/></span>
														<span><img src="../images/previmgnew2_disabled.jpg"
														alt="First Page" title="First Page" id="dinstanceKDXNavFirst" style="padding-top:2px;"/></span></td>
													<td><span><img src="../images/previmgnew.jpg"
														alt="Previous Page" title="Previous Page" style="cursor:pointer;padding-top:1px;" id="instanceKDXNavPrevious"/></span>
														<span><img src="../images/previmgnew_disabled.jpg"
														alt="Previous Page" title="Previous Page" id="dinstanceKDXNavPrevious" style="padding-top:1px;"/></span></td>													
													<td><span><img src="../images/nxtimgnew.jpg"
														alt="Next Page" title="Next Page" style="cursor:pointer" id="instanceKDXNavNext"/></span>
														<span><img src="../images/nxtimgnew_disabled.jpg"
														alt="Next Page" title="Next Page" id="dinstanceKDXNavNext"/></span></td>
													<td><span><img src="../images/nxtimgnew2.jpg"
														alt="Last Page" title="Last Page"  style="cursor:pointer" id="instanceKDXNavLast"/></span>
														<span><img src="../images/nxtimgnew2_disabled.jpg"
														alt="Last Page" title="Last Page" id="dinstanceKDXNavLast"/></span></td>
												</tr>
								
											</table>
										</span>
									<span class="fieldNames" id="instanceKDXPage" style="padding-left: 10px;float:left;padding-top:5px;">page <span id="instanceKDXPageno"></span>
										of <span id="instanceKDXNumberofpages"></span></span></span>
								</div>
								</td>
							</tr>
						</table>
                             
                             </div>
                             <!--/////////////////////////////// Instances pane Ends ////////////////////////////////////-->
						</div>
                        
						<div id="seperatorRight"></div>
						<div class="ui-layout-east"
							style="overflow-x: hidden; float: left">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-left:3px;"> 
							<tr>
								<td height="25" class="fieldNames">
                                
								<div id="messagesAndWarnings" ></div>
								</td>
							</tr>
							<!--  <tr>
      <td><div id="mapsContent" class="descriptionText" style="width:400px;display:none;" ></div></td>
    </tr>-->
							<tr>
								<td>
								<div id="dynamicPane">

								<table width="470" border="0" cellspacing="0" cellpadding="0">

									<tr>
										<td>
										<div id="TabbedPanels1" class="TabbedPanels">
										<ul class="TabbedPanelsTabGroup">
											<li class="TabbedPanelsTab" tabindex="0"><s:text
												name="execue.mapping.conceptMapping.subheading" /></li>
											<li class="TabbedPanelsTab" tabindex="1"
												id="instanceMappingId" style="display:none;"><s:text
												name="execue.mapping.instanceMapping.subheading" /></li>
										</ul>
										<div class="TabbedPanelsContentGroup">
										<div class="TabbedPanelsContent"
											style="height: 323px; width: 400px;">
                                      <!-- Step1 suggest concepts link-->      
									<div id="step1Concepts"><div id="suggestConceptsLink"><s:text name="execue.mapping.step1" />
								  <a href="javascript:;" id="suggestConcepts"><s:text
									name="execue.mapping.link.suggestConcepts" /></a></div>
								<div id="loadingSuggestConceptsLink" style="display: none;"><img
									src="../images/loadingWhite.gif" width="25" height="25"></div></div>
                                      <!-- Step1 suggest concepts link ends-->      
                                    <!-- filter starts-->
                                    <div id="filterBox" ><select name="" id="conceptFilter" onChange="mappingFilter();">
                                <option value="0">Select..</option>
                                  <option value="confirmed">confirmed</option>
                                  <option value="un-confirmed">un-confirmed</option>
                                  <option value="Show All">Show All</option>
                                </select></div>
                                 <!-- filter  ends-->
										<div style="width: 400px;">
										<div style="width: 430px;float:left;"><span class="fieldNames"
											style="padding-left: 8px;"><s:text
											name="execue.mapping.step2" /></span> <span class="fieldNames" id="conceptPage"
											style="padding-left: 120px;">page <span id="conceptPageno"></span>
										of <span id="conceptNumberofpages"></span></span></div>
										<div class="tableListWithoutBorder"
											style="height: 235px; width: 450px; float: left; overflow-x: scroll;margin-left:6px;padding-left:2px;" id="columnsConceptsDiv">
										<form name="form1" method="post" action="">

										<div
											style="width: 430px; border-bottom: #E0E0E0 dashed 1px; height: 22px;"><input
											name="checkAllConceptSuggestions" type="checkbox" value=""
											id="checkAllConceptSuggestions" onClick="checkConcepts();" />
										<span class="descriptionText"> <s:text
											name="execue.global.selectAll" /></span>
											<span class="descriptionText" style="padding-left: 220px;"><a href='#' id='newConcept'><s:text name="execue.mapping.add.new.concept"/></a></span>
											</div>
										<div id="mapsSelected">
                                        	<div id="mapsContent" class="descriptionText" style="width: 400px; display: none;"></div>
										<!-- concepts suggested starts -->
                                       <div id="conceptsExistingDiv">
                                       <!-- loader starts -->
                                       
                                       <div id="loadingConceptMappingsLink" style="display:none" ><img
									src="../images/loaderMappings.gif" ></div>
                                    
                                    <!-- loader ends -->
                                       </div>
										   <div id="conceptsSuggestedDiv"></div>
										</div>
										<!-- concepts suggested  ends--></form>
										</div>

										<!--form id='form2' -->
										<div style="float: left; width: 440px;"><span
											class="fieldNames"
											style="padding-left: 8px; float: left; padding-top: 10px;padding-right:3px;">Step
										3. </span><span style="float: left">
										<div style="display: none" id="enableConfirmMappings">
                                         <input type="button"	class="buttonSize130" 
											value="<s:text name='execue.mapping.confirmMappings.button' />" 
											 align="middle" id="confirmMappingsId" /></div>
                                             <!-- loader -->
                                             <div style="display: none" id="enableConfirmMappingsLoader">
                                            <input type="button"	class="buttonLoaderSize130" disabled="disabled"
											value="<s:text name='execue.mapping.confirmMappings.button' />" 
											 align="middle" id="confirmMappingsId" /></div>
                                             <!-- loader ends -->
										<div id="disableConfirmMappings"> <input type="button"	class="buttonSize160"  disabled="disabled"
											value="<s:text name='execue.mapping.confirmMappings.button' />"
											  /></div>
										<span id="conformProcess" style="display: none">
                                        <input type="button"	class="buttonSize130" disabled="disabled"
											value="<s:text name='execue.mapping.confirmMappings.button' />"  /></span> </span> 
                                            <span id="conceptMappingPage"
											style="float: left; padding-left: 10px;">
                                            
										<table width="100" border="0" cellspacing="2" cellpadding="0" style="margin-top:3px;border:1px solid #CCC;padding-left:5px;padding-bottom:2px;">
											<tr>
												<td><span><img src="../images/previmgnew2.jpg"
													alt="First Page" title="First Page" id="conceptNavFirst" style="padding-top:2px;cursor:pointer;display:none;"/></span>
													<span><img src="../images/previmgnew2_disabled.jpg"
													alt="First Page" title="First Page" id="dconceptNavFirst" style="padding-top:2px;"/></span></td>
												<td><span><img src="../images/previmgnew.jpg"
													alt="Previous Page" title="Previous Page" id="conceptNavPrevious" style="padding-top:2px;cursor:pointer;display:none;"/></span>
													<span><img src="../images/previmgnew_disabled.jpg"
													alt="Previous Page" title="Previous Page" id="dconceptNavPrevious" style="padding-top:2px;"/></span></td>
												<!--td><span><img src="../images/navAll_blue.jpg"
													alt="Show All" title="Show All" id="conceptNavAll"/></span>
													<span><img src="../images/navAll_blue_disabled.jpg"
													alt="Show All" title="Show All" id="dconceptNavAll"/></span></td-->
												<td><span><img src="../images/nxtimgnew.jpg"
													alt="Next Page" title="Next Page"  id="conceptNavNext" style="cursor:pointer;display:none;" /></span>
													<span><img src="../images/nxtimgnew_disabled.jpg"
													alt="Next Page" title="Next Page"   id="dconceptNavNext"/></span></td>
												<td><span><img src="../images/nxtimgnew2.jpg"
													alt="Last Page" title="Last Page" style="cursor:pointer;display:none;"  id="conceptNavLast"/></span>
													<span><img src="../images/nxtimgnew2_disabled.jpg"
													alt="Last Page" title="Last Page" id="dconceptNavLast"  /></span></td>
											</tr>
										</table>
										</span></div>
										<!--/form -->
										</div>

										</div>

										<div class="TabbedPanelsContent" id="instancesPane" style="height: 320px; width: 400px;display:none;">
										<form name="instanceForm" method="post" action="">

										<div style="width: 400px;">
                                       <!-- Step1 suggest instance link-->      
									<div id="step1Instance">
                                        <div id="suggestInstancesLink"><s:text name="execue.mapping.step1" />
								<a href="javascript:;" id="suggestInstances">Suggest Instances</a></div>
                                <div id="loadingSuggestInstancesLink" style="display: none;"><img
									src="../images/loadingWhite.gif" width="25" height="25"></div></div>
                                     <!-- Step1 suggest instance link-->  
                                         
                                     <!-- filter starts-->
                                    <div id="filterBox" ><select name="" id='instanceFilter' onChange="mappingFilter();">
                                <option value="0">Select..</option>
                                  <option value="confirmed">confirmed</option>
                                  <option value="un-confirmed">un-confirmed</option>
                                  <option value="Show All">Show All</option>
                                </select></div>
                                 <!-- filter  ends-->
                                 
										<div style="width: 430px;float:left;"><span class="fieldNames"
											style="padding-left: 8px;"><s:text
											name="execue.mapping.step2" /></span> 
											<span class="fieldNames" id="instancePage"
											style="padding-left: 120px;">page <span id="instancePageno"></span>
										of <span id="instanceNumberofpages"></span></span>
										<div id="instanceColumnConcept"/>
										</div>
										<div class="tableListWithoutBorder"
											style="height: 215px; width: 450px; float: left; overflow-x: scroll;margin-bottom:0px;margin-left:5px;padding-left:2px;" id="membersInstanceDiv">
										
										<div
											style="width: 430px; border-bottom: #E0E0E0 dashed 1px; height: 22px;"><input
											name="checkAllInstanceMappings" type="checkbox" value=""
											id="checkAllInstanceMappings" onClick="checkInstancesMapping();" />
										<span class="descriptionText"> <s:text
											name="execue.global.selectAll" /></span>
											<span class="descriptionText" style="padding-left: 220px;"><a href='#' id='newInstance'><s:text name="execue.mapping.add.new.instance"/></a></span>
											</div>
										<div id="instanceMapsSelected">
                                        	<div id="instanceMapsContent" class="descriptionText" style="width: 400px; display: none;"></div>
										<!-- concepts suggested starts -->
                                       <div id="InstancesExistingDiv"></div>
										   <div id="InstancesSuggestedDiv">
                                           <!-- loader starts -->
                                       
                                      
                                    
                                    <!-- loader ends -->
                                           </div>
                                            <div id="loadingInstanceMappingsLink" style="display:none" ><img
									src="../images/loaderMappings.gif" ></div>
										</div>
										<!-- concepts suggested  ends-->
										</div>

										<div style="float: left; width: 450px;">
										<span class="fieldNames" style="padding-left: 2px; float: left; padding-top: 10px;padding-right: 5px;">
                                        <table width="100%" border="0" cellspacing="0" cellpadding="3">
                                          <tr>
                                            <td ><s:text name="execue.mapping.step3" /></td>
                                            <td> <input name="saveAllPages" type="checkbox"  id="saveAllPagesInstances"></td>
                                            <td>Save All </td>
                                          </tr>
                                        </table>

										 
                                        </span>
										<span style="float: left">
										<div style="display: none" id="enableInstanceConfirmMappings">
                                       <input type="button"	class="buttonSize160"
											value="<s:text name='execue.mapping.confirmMappings.button' />"  
											 align="middle" id="confirmInstancesMappingsId" />
										</div>
                                         <!-- loader -->
                                             <div style="display: none" id="enableInstanceConfirmMappingsLoader">
                                             <input type="button"	class="buttonLoaderSize130" disabled="disabled"
											value="<s:text name='execue.mapping.confirmMappings.button' />" 
											 align="middle" id="confirmMappingsId" /></div>
                                             <!-- loader ends -->
                                             	 
										<div id="disableInstanceConfirmMappings">
                                      <input type="button"	class="buttonSize160" disabled="disabled"
											value="<s:text name='execue.mapping.confirmMappings.button' />" 
											  />
										</div>
										<span id="conformInstancesProcess" style="display: none"><img
											src="../images/loadingWhite.gif" width="136" height="27"
											hspace="3" vspace="5" />
										</span> 
										</span> 
										<span id="instanceMappingPage"
											style="float: left; padding-left: 10px;">
											<table width="100" border="0" cellspacing="2" cellpadding="0" style="margin-top:3px;border:1px solid #CCC;padding-left:5px;padding-bottom:2px;">
												<tr>
													<td><span><img src="../images/previmgnew2.jpg"
														alt="First Page" id="instanceNavFirst" style="cursor:pointer;padding-top:2px;"/></span>
														<span><img src="../images/previmgnew2_disabled.jpg"
														alt="First Page" title="First Page" id="dinstanceNavFirst" style="padding-top:2px;"/></span></td>
													<td><span><img src="../images/previmgnew.jpg"
														alt="Previous Page" title="Previous Page" style="cursor:pointer;padding-top:1px;" id="instanceNavPrevious"/></span>
														<span><img src="../images/previmgnew_disabled.jpg"
														alt="Previous Page" title="Previous Page" id="dinstanceNavPrevious" style="padding-top:1px;"/></span></td>
													<!--td><span><img src="../images/navAll_blue.jpg"
														alt="Show All " title="Show All" id="instanceNavAll"/></span>
														<span><img src="../images/navAll_blue_disabled.jpg"
														alt="Show All " title="Show All" id="dinstanceNavAll"/></span></td-->
													<td><span><img src="../images/nxtimgnew.jpg"
														alt="Next Page" title="Next Page" style="cursor:pointer" id="instanceNavNext"/></span>
														<span><img src="../images/nxtimgnew_disabled.jpg"
														alt="Next Page" title="Next Page" id="dinstanceNavNext"/></span></td>
													<td><span><img src="../images/nxtimgnew2.jpg"
														alt="Last Page" title="Last Page"  style="cursor:pointer" id="instanceNavLast"/></span>
														<span><img src="../images/nxtimgnew2_disabled.jpg"
														alt="Last Page" title="Last Page" id="dinstanceNavLast"/></span></td>
												</tr>
											</table>
										</span>
										</div>
										
										</form>
										</div>
										</div>
										</td>
									</tr>
								</table>

								</div>
								</td>
							</tr>
						</table>
						</div>

						</div>
						</td>
					</tr>

				</table>


				</td>
			</tr>
			<tr>
				<td valign="top">&nbsp;</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>
<div id="dynamicTextBox" style="position: absolute; z-index: 10;"></div>
