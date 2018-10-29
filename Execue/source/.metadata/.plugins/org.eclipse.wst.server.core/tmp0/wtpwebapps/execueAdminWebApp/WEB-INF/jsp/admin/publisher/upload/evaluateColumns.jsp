<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="aw" uri="/WEB-INF/tlds/absorptionWizard.tld"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" >
  <tr>
    <td class="uploadOuterBg"><div  id="uploadFileInfoOuterDiv">
    <div class="uploadTopBox" ><div style="margin:auto;text-align:center;"> <span class="no-active"><s:text name="execue.global.locate" /></span> <span class="arrow-no-active"> >></span><span class="active"><s:text name="execue.global.describe" /></span> <span class="arrow-active">>></span> <span class="no-active"><s:text name="execue.global.publish" /></span></div>
</div>
                                        
                                       
<div  class="uploadFileMainBox" >



<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	
	<tr>
		<td>
		
		<div id="TabbedPanels1" class="TabbedPanels"
			style="margin-bottom: 10px; margin-top: 10px;">
		<ul class="TabbedPanelsTabGroup">
			<li class="TabbedPanelsTab" tabindex="0"><s:text name="execue.review.metadata.field.tab.heading"/></li>
			<li class="TabbedPanelsTab" tabindex="1"><s:text name="execue.review.metadata.lookup.tab.heading"/></li>
		</ul>
		<div class="TabbedPanelsContentGroup" style="border-color:#999999 #FFFFFF #FFFFFF; #ffffff;">
		<div class="TabbedPanelsContent"
			style="min-height: 200px; height: auto; width: 100%;"><!-- Table content for column confirmation scrren goes here -->
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText" style="padding-left: 20px;"><s:text
					name="execue.evaluatedColumn.description" /></td>
			</tr>
			<tr>
				<td valign="top">

				<table border="0" align="center" cellpadding="0" cellspacing="0" width="98%">
					<tr>
						<td>
						<div id="container"
							style="min-width: 940px; min-height: 300px; height: 410px; margin: auto; border: none; padding-top: 7px; padding-left: 7px; padding-bottom: 0px; overflow: hidden;">
						<!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
						<s:if test="%{publishedFileTables.size > 1}">
							<div class="ui-layout-west"
								style="overflow-x: hidden; min-width: 215px; width: 20%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="27"><span class="tableSubHeading">Tables</span></td>
								</tr>
								<tr>
									<td height="30" align="left" valign="top">
									<div id="divPublishedFileTables">
									<DIV id=roundedSearch style="display: none">
									<div class=searchStart></div>
									<INPUT class=searchField id=searchText type=search value=Search>
									<div class=searchEnd id=searchIcon><a href="#"><img
										src="../images/admin/searchEnd.gif" name="Image2" border="0"
										id="Image2"
										onMouseOver="MM_showMenu(window.mm_menu_0113140999_0,0,25,null,'Image2')"
										onMouseOut="MM_startTimeout();" /></a></div>
									</DIV>
									<div id="searchTables"><a
										href="javascript:showSearch('divPublishedFileTables');"
										class="links">Search</a></div>
									</div>
									</td>
								</tr>
								<tr>
									<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td>
											<div class="tableList"
												style="height: 260px; width: 196px; margin-left: 5px; margin-bottom: 5px;">
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">
												<s:iterator value="publishedFileTables" status="inst"
													id="publishedFileTable">
													<tr>
														<td width="1%" class="dotBullet">&nbsp;</td>
														<td width="99%" class="fieldNames">
														<div id="showPublishedFileLink"><a
															href="javascript:showEvaluatedColumns(<s:property value="%{#publishedFileTable.id}"/>);"
															class="links"
															id=<s:property value="%{#publishedFileTable.id}"/>)><s:property
															value="%{#publishedFileTable.displayTableName}" /></a></div>
														<div
															id="loadingShowPublisheFiledLink<s:property value="%{#publishedFileTable.id}"/>"
															style="display: none;"><img
															src="../images/admin/loadingBlue.gif" width="25" height="25" /></div>
														</td>
													</tr>
												</s:iterator>
											</table>
											</div>
											</td>
										</tr>
									</table>
									</td>
								</tr>
							</table>
							</div>
							<!-- end of ui-layout-west -->
						</s:if> <s:if test="%{publishedFileTables.size > 1}">
							<div class="ui-layout-center"
								style="min-width: 720px; width: 76%; overflow: auto;">
						</s:if><s:else>
							<div class="ui-layout-center"
								style="min-width: 720px; width: 100%;">
						</s:else> <!--table border="0" cellpadding="0" cellspacing="0" width="100%">
	                            <tbody><tr>
	                                <td align="left" height="25">
	                                <div id="absorbEvaluatedDataLink" style="display: none;width:180px;float:left;">
	                                    <%--aw:absorptionWizardNextTag sourceType="${requestScope.fileInfo.sourceType}" currentPage="COLUMN-METADATA"/--%>
	                                </div>
	                                
	                                 <div id="absorbtionDiv" style="display:none;background-color:#FFF;width:300px;float:left;"></div>
	                                 
	                                 
	                                <div id="loadingAbsorbEvaluatedDataLink" style="display: none;"><img src="../images/admin/loadingBlue.gif" height="25" width="25"></div>
	                                </td>
	                            </tr>
	                        </tbody></table-->

						<div id="dynamicPane"><jsp:include page="columnMetaData.jsp" /></div>
						</div>
						&nbsp;</td>
					</tr>
				</table>
				</td>
			</tr>
			<s:if test="infoSource != 'DC'">
			<tr>
				<td>
				<!-- div id="absorbEvaluatedDataLink"
					style="display: none; width: 210px; float: left; padding-left: 17px;"-->
				<!--%aw:absorptionWizardNextTag sourceType="${requestScope.fileInfo.sourceType}" currentPage="COLUMN-METADATA"/%-->
				<!--div style="float:left;width:35px;padding-top:7px;" >Next : </div>
				<div style="float: left; width: 170px;"><input type="button"
					class="singleButton" value="Absorb Evaluated Dataset"
					onclick="javascript:submitForAbsorption();" /></div>
				</div>

				<div id="absorbtionDiv"
					style="display: none; background-color: #FFF; width: 300px; float: left; padding-left: 17px;"></div-->
				</td>
			</tr>
			</s:if>
		</table>
		</div>
		<div class="TabbedPanelsContent"
			style="min-height: 20px; height: auto; width: 100%;">
			<DIV id="memberMetaPane"></DIV>
		</div>
		</div>
		</div>

		
		</td>
	</tr>

</table>
</div>	
                                        
</div></td>
  </tr>
</table>
<s:hidden name="publishedFileInfo.publisherProcessType"></s:hidden>

<s:hidden name="publishedFileId" id="fileInfoId" />
<s:hidden name="jobRequestId" id="jobRequestId" />
<s:hidden name="fileInfo.datasetCollectionCreation"
	id="datasetCollectionCreationId" />
<s:hidden name="assetId" id="assetId" />
<s:hidden name="tableId" id="tableId" />
<s:hidden name="infoSource" id="infoSource" />
<script type="text/javascript">
$(document).ready(function() {

	var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
	$("#TabbedPanels1").show();

	var assetId = '<c:out value="${assetId}"/>';
	var infoSource = '<c:out value="${infoSource}"/>';

	if(infoSource == "DC"){
		//$("#TabbedPanels1 li[tabindex='0']").hide();
		//TabbedPanels1.showPanel(1);
		$("#TabbedPanels1 li[tabindex='1']").bind("click", function () { 
				$.get("showMemberEvaluation.action?assetId="+assetId, function(data, status){
				if(status == "success")
					$("#memberMetaPane").html(data);
				});
		});
		
	} else {
		$("#TabbedPanels1 li[tabindex='1']").hide();
	}
});
</script>