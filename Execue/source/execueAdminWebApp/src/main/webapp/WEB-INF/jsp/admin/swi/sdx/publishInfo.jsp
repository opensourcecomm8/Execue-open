<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.execue.core.common.type.PublishAssetMode"%>
<script type="text/javascript">
// NOTE: -RG- Terms of service is taken off
/*
function checkTerms(){
	
	if(!$("#checkBoxTerms").is(":checked")){
		$("#absorbtionDiv").empty().show().html("<span style='color:red'><s:text name='execue.publishInfo.termsOfServiceMessage' /></span>");
		return false;
	}

	return true;
}
*/
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0" >
  <tr>
    <td class="uploadOuterBg"><div id="uploadFileInfoOuterDiv">
    <div class="uploadTopBox">
                                        
                                        <div style="margin:auto;text-align:center;"> <span class="no-active"><s:text name="execue.global.locate" /></span> <span class="arrow-no-active"> >> </span><span class="no-active"> <s:text name="execue.global.describe" /></span> <span class="arrow-no-active"> >> </span> <span class="active"><s:text name="execue.global.publish" /></span></div>
</div>
                                        
                                       
<div  class="uploadFileMainBox" >

<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	
	<tr>
		<td>
		
		<table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText" style="padding-left: 2px;"><s:text
					name="execue.publishInfo.description" /></td>
			</tr>
			<tr>
				<td valign="top">

				<table border="0" align="left" cellpadding="0" cellspacing="0">
					<tr>
						<td>


						<div id="container"
							style="width: 950px; min-height: 315px; height: auto; margin: auto; border-top: #CCC dashed 1px;">
						<!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
						<s:form action="showPublishDatasets" method="POST">
							<div>
							<div style="float: right;"><!--a href="#" onclick="showUploadCSVFileInfo();">Goto Upload CSVDataSet</a--></div>
							<table align="left" id="datasetDetailsMessageDiv"
								style="display: none;">
								<tr>
									<td colspan="2" align="center"><s:text
										name='execue.publishInfo.noDatasetAvailable' /></td>
								</tr>
							</table>
							<table id="datasetDetailsDiv"
								style="display: none; margin-left: 20px;" width="40%" border="0"
								align="left" cellpadding="5" cellspacing="0">

								<!--tr >
						    <td width="37%" ><s:text name='publishInfo.name' /></td>
                            <td width="63%" align="left" valign="top"><SPAN id="assetName"/></td>
					      </tr>
                           <tr >
						    <td width="37%"><s:text name='publishInfo.description' /> :</td>
                            <td width="63%" align="left" valign="top"><SPAN id="assetDescription"></SPAN></td>
					      </tr-->
								<tr>
									<td colspan="2">
									<div id="absorbtionDiv" class="green"
										style="display: none; background-color: #FFF; width: 300px; float: left;"></div>
									</td>
								</tr>
								<tr>
									<td width="30%" align="left" style="white-space: nowrap;"><s:text name='execue.publishInfo.app.name'/></td>

									<td width="70%" align="left"><s:textfield id="appNameId"
										name="appName" />&nbsp;&nbsp;<a
										href="../swi/showApplications.action?application.id=<s:property value='applicationId'/>"><s:text name='execue.publishInfo.app.details.edit.link'/></a>
									</td>
								</tr>
								<tr>
										<td width="30%" align="left" style="white-space: nowrap;">
										<s:text name='execue.publishInfo.selectDatasetCollection' /></td>
										<td width="70%" align="left"><select id="selectedAssetId"
											name="selectedAssetId">
											<s:iterator value="assets" id="datasetId">
												<option value="<s:property value='id'/>"
													description="<s:property value='description'/>"
													publishMode="<s:property value='publishMode'/>"
													<s:if test="id == selectedAssetId">selected="selected"</s:if>><s:property
													value="name" /></option>
											</s:iterator>
										</select></td>
								</tr>

								<tr>
									<td colspan="2">


									<div
										style="border: 1px solid rgb(204, 204, 204); padding: 10px; width: 490px; height: 90px; margin-left: 0px; white-space: nowrap;">
									<table width="55%" cellspacing="0" cellpadding="4" border="0"
										align="left">
										<tbody>
											<tr>
												<td colspan="2" align="left"><strong><s:text
													name="execue.publishInfo.publishMode.heading" /></strong></td>
											</tr>
											<tr>
												<td width="80%"><s:text
													name='execue.publishInfo.locallyAvailable' /></td>
												<td width="20%"><label> <input type="radio"
													id="publishModeLocalId" checked="checked"
													value="<%=PublishAssetMode.LOCAL%>" name="publishMode" /></label></td>
											</tr>
											<tr>
												<td><s:text name='execue.publishInfo.toCommunity' /></td>
												<td><input type="radio"
													id="publishModeCommunityId"
													value="<%=PublishAssetMode.COMMUNITY%>" name="publishMode" /></td>
											</tr>
                                            <!--
                                            <tr>
												<td><s:text name='execue.publishInfo.Pay' /></td>
												<td><s:text name='execue.publishInfo.Soon' /></td>
											</tr>
                                            -->
										</tbody>
									</table>
									</div>



									</td>
								</tr>

                                <!--
								<tr>


									<td align="left" valign="top" colspan="2"
										class="content-textInner">
									<div
										style="width: 500px; height: 190px; border: solid #ccc 1px; overflow: auto; padding: 5px; color: #333; font-size: 10px;"><span
										style="font-size: 12px;">Semantifi Additional Terms of
									Service for Application Publication </span><br>
									<br>
									In addition to the Semantifi Terms of Service, the following
									additional terms shall apply upon publishing an Application by
									you, the Publisher, to the Semantifi website.<br />
									<br />


									1. Any Application published by any Publisher shall remain the
									property of the publisher, and Publisher retains copyright and
									any other rights in any Application, and associated content,
									which Publisher submits, posts or displays via any service
									offered by Semantifi. By submitting, posting or displaying the
									Applications, Publisher gives Semantifi a perpetual,
									irrevocable, worldwide, royalty-free, and non-exclusive license
									to reproduce, adapt, modify, translate, publish, publicly
									perform, publicly display and distribute any such Applications.
									Publisher agrees that this license includes a right for
									Semantifi to make such Applications available to other
									companies, organizations or individuals with whom Semantifi has
									relationships, and to use such Applications in connection with
									the provision of those services.<br />
									<br />


									2. Any Application published by any Publisher to - If an App is
									published for free, it shall be available to any users, whether
									registered or not, of any of the Semantifi services. Such free
									publication indicates that Publisher has voluntarily agreed not
									to charge users any fee, and once published, such Application
									shall remain free. Publisher may, however, alternatively
									publish an Application and require payment for its use. Any
									such charges shall be determined by and retained by Publisher.
									Semantifi may charge a fee for publication of or purchase of
									any such Application.<br />
									<br />


									3. Upon publication of any Application by Publisher, Semantifi
									will provide attribution to the Publisher in any search results
									provided from any search engine. If any errors in such
									attribution are noted by Publisher, upon notification to
									Semantifi, such errors in attribution will attempt to be
									remedied within a reasonable timeframe. Semantifi shall have no
									liability for any such errors in attribution.<br />
									<br />

									4. Any files, datasets or any other content uploaded by
									Publisher are stored on the Semantifi website portal. Semantifi
									will maintaine such files, datasets or other content as long as
									the information is being utilized by at least one live
									Application. Any such filed, datasets or other content that is
									not being utilized by at least one live Application may, at the
									discretion of Semantifi, be purged from the Semantifi website
									portal after six months. Semantifi may provide a 30 day notice
									of such purge to Publisher, but Publisher should not rely on
									such notice and should be aware of the possible deletion of any
									such unused content without notice. Publisher may upload files,
									datasets and other content occupying up to 10 Gigabytes of disk
									space for free. Additional space may be made available by
									Semantifi for a charge, and at its complete discretion.
									Publisher acknowledges that Publisher alone is responsible for
									the backup of any uploaded files, datasets or other content.
									Semantifi does not back up any such content for the use by
									Publisher, and is not liable for loss of any files, datasets or
									other content for any reason, even if such loss occurs through
									the fault of Semantifi.<br />
									<br />

									5. Publisher understands that all information (such as data
									files, written text, or other information or content) to which
									Publisher may provide as part of, or through your use of any
									published Application are the sole responsibility of Publisher.
									Publisher agrees that Publisher is solely responsible for (and
									that Semantifi has no responsibility to Publisher) any Content
									that Publisher creates, transmits or displays in accordance
									with any Application, and for the consequences of doing so.<br />
									<br />

									6. Publisher is responsible for respecting any ownership rights
									over any datasets, content or other information that may be
									used in accordance with any Application. Publisher may use
									publicly available datasets, or may use any privately held
									datasets or other content, subject to an agreement between
									Publisher and any owner of such privately held datasets or
									other content. Semantifi is not responsible for securing any
									rights to use any dataset or other content on behalf of
									Publisher. Furthermore, Semantifi shall not be held liable for
									any failure on the Part of Publisher to secure such rights. <br />
									<br />

									7. Any knowledge, metadata, model or other content created in
									accordance with the publication process of an Application by
									Publisher may be used by Semantifi Search engine. Semantifi may
									use this information to update their semantic web indexes to
									make the search possible over the published Applications.
									Publisher further understands that Semantifi, in performing the
									required technical steps to provide various services to users,
									may (a) transmit or distribute this information over various
									public networks and in various media; and (b) make such changes
									to this information or Applications as are necessary to conform
									and adapt that information of Applications to the technical
									requirements of connecting networks, devices, services or
									media. You agree that this license shall permit Semantifi to
									take these actions.<br />
									<br />

									8. Any reports or other content that may be generated by any
									Application through the application of user search queries or
									through any other means may be stored in accordance with the
									Semantifi web portal, preferably organized by Application.
									Publisher of any such Application many request at any time a
									download of all such reports or other content for backup
									purposes. Any such reports or other content that are stored for
									more than one year in accordance with the Semantifi web portal
									may be archived or purged by Semantifi in its sole discretion.
									The Semantifi web portal may provide cached results to one or
									more user queries or other data requests from one or more
									reports stored in accordance with the Semantifi web portal
									repository, in the interest of providing fast response times or
									for any other reason.<br />
									<br />

									9. Semantifi reserves the right to delete or otherwise remove
									or disable any Application at any time for any reason, and in
									particular if Semantifi perceives any risk associated with the
									Publication and/or continued offering of any particular
									Application. Such reasons, without limitation, may comprise one
									or more of the following: <br />
									<br />
									(1) Illegal terminology emanating from an Application; <br />
									(2) Abuse of Semantifi resources by a Publisher or an
									Application; or <br />
									(3) Reported illegal usage of any dataset or other content from
									any third party.<br />
									<br />

									10. Semantifi may make changes to these Additional Terms from
									time to time. When these changes are made, Semantifi will make
									a new copy of the Additional Terms available at <a
										target="_blank" href="../AdditionalTerms.html">http://www.semantifi.com/AdditionalTerms.html</a>.
									Publisher understands and agrees that upon use of the Semantifi
									web portal, or the publication, updating, or other interaction
									with any Application stored on the Semantifi web portal after
									the date on which the Terms have changed, Semantifi will treat
									Publisher's use as acceptance of the updated Additional Terms.<br />

									</div>
									</td>

								</tr>
								<tr>


									<td align="left" colspan="2">

									<table border="0" cellspacing="5" cellpadding="0">
										<tr>
											<td><input type="checkbox" id="checkBoxTerms" value="1" /></td>
											<td>I accept Semantifi Terms of Service for Application
											Publication <span id="termsMessage"
												style="color: #F00; padding-left: 30px;"></span></td>
										</tr>
									</table>
									</td>

								</tr>
                                -->


								<tr>

									<td colspan="2" height="40" align="left">

									<div id="publishAppButtonDiv"><input type="button"
										class="buttonSize108"
										onClick="javascript:publishDatasetCollection();"
										style="width: 80px;"
										value="<s:text name='execue.publishInfo.publish.button' />"
										alt="Publish App" /><!-- &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<span><s:text name="publishInfo.runAssetAnalysisReport"></s:text>
										<s:if test="runAssetAnalysisReoprt.value=='Y'"> <input type="checkbox" checked="checked" id="runAssetAnalysisReoprtId"/></s:if>
										<s:else> <input type="checkbox"  id="runAssetAnalysisReoprtId"/></s:else></span>-->
									</div>
									<div id="publisherStatusDiv"
										style="display: none; background-color: #FFF; float: left;"></div>
								</tr>
								<!-- <tr>
									<td colspan="2" height="40" align="left">														
									<div id="publisherReportDiv"
										style="white-space:no-wrap; background-color: #FFF; float: left;"><s:if test="isAssetAnalysisReportExists.value=='Y'">										  
										   <a onclick='javascript:getAssetAnalysisReport(<s:property value="selectedAssetId"/>)'href='#'  class="arrowBg" ><s:text name="publishInfo.viewAssetAnalysisReport"></s:text></a>
										</s:if></div>
									</td>
								</tr-->
							</table>
							</div>
						</s:form> <!--div class="ui-layout-east" style="overflow-x: hidden;"></div-->
						</div>

						&nbsp;</td>
					</tr>

				</table>


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
<script>
$(document).ready(function() {
	var publishMode=$("#selectedAssetId option:selected").attr("publishMode");
	if(publishMode=='COMMUNITY'){
	  $("#publishModeCommunityId").attr("checked",true);
	}else{
	  $("#publishModeLocalId").attr("checked",true);
	}
	
	$("#selectedAssetId").change(function(){
	  publishMode=$("#selectedAssetId option:selected").attr("publishMode");
	   if(publishMode=='COMMUNITY'){
		  $("#publishModeCommunityId").attr("checked",true);
		}else{
		   $("#publishModeLocalId").attr("checked",true);
		}
	});
});


</script>