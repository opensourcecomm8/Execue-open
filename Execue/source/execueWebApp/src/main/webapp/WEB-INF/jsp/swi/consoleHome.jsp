<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
a.thickbox:hover{
color:#CF030E;	
}
a.thickbox{
text-decoration:none;color:#003399;
}


</style>
<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
  <tr>
    <td height="30" valign="bottom">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="29" class="titleWithBackground"><s:text name="execue.console.home.heading" /></td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td>
    <div id="greyBorder" style="padding-left:3px;height:450px;">
    <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top" class="descriptionText"><s:text name="execue.console.home.description" /></td>
      </tr>
      <tr>
        <td>
        <div id="message" STYLE="color: green;" align="center"></div>
        </td>
      </tr>
      <tr>
        <td valign="top">
          <table  border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
              <td>
              <div id="container"
                style="width: 950px; height: 400px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
              <!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
              <!--div class="ui-layout-east" style="overflow-x: hidden; width: 21%; float: left">
              <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td class="fieldNames" height="22"><s:text name="console.home.dataModel.heading"/></td>
                </tr>
               
                <tr>
                  <td><a class="links" href="../swi/showApplications.action"><s:text name="console.home.view.applications"/></a></td>
                </tr>
                <tr>
                  <td><a class="links" href="../swi/showDataSources.action"><s:text name="console.home.view.dataSources"/></a></td>
                </tr>
                <tr>
                  <td><a class="links" href="../swi/showAssets.action"><s:text name="console.home.view.assets"/></a></td>
                </tr>
                <tr> 
                  <td><a class="links" href="../swi/showAssetSelectionForJoins.action"><s:text name="console.home.view.joins"/></a></td>
                </tr>
                 <tr>               
                  <td><a class="links" href="../swi/showAssetsGrain.action"><s:text name="console.home.view.asset.grain"/></a></td>
                </tr>
                 <tr>               
                  <td><a class="links" href="../swi/showAssetDetail.action"><s:text name="console.home.view.asset-details"/></a></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td class="fieldNames" height="22"><s:text name="console.home.business.heading"/></td>
                </tr>
               
                <tr>
                  <td><a class="links" href="../swi/showBusinessTerms.action"><s:text name="console.home.view.businessTerms"/></a></td>
                </tr>
                <tr>
                  <td><a class="links" href="../swi/showAssetSelectionForMappings.action?sourceName=mapping"><s:text name="console.home.view.mappings"/></a></td>
                </tr>
                <tr>
                  <td><a class="links" href="../swi/showProfiles.action"><s:text name="console.home.view.profiles"/></a></td>
                </tr>
                <tr>
                  <td><a class="links" href="../swi/showRanges.action"><s:text name="console.home.view.ranges"/></a></td>
                </tr>
                <tr>
                  <td><a class="links" href="../swi/showParallelWords.action"><s:text name="console.home.view.parallelwords"/></a></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td class="fieldNames" height="22"><s:text name="console.home.security.heading"/></td>
                </tr>
                
                <tr>
                  <td><a class="links" href="../swi/showUsers.action"><s:text name="console.home.view.users"/></a></td>
                </tr>
                <tr>
                  <td><a class="links" href="../swi/showRoles.action"><s:text name="console.home.view.roles"/></a></td>
                </tr>
                <tr>
                  <td><a class="links" href="../swi/showGroups.action"><s:text name="console.home.view.groups"/></a></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td class="fieldNames" height="22"><s:text name="console.home.catalog.heading"/></td>
                </tr>
               
                <tr>
              
                 <td><a class="links" title="Show Datasets" href='showAssetSelectionForCubes.action?sourceName=cubeRequest'><s:text name="console.home.view.catalog.cubeCreation"/></a></td>                     
                </tr-->
                
                 <!-- Link back to Search Home -->
                 <!-- 
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td><a class="links" href="../execueHome.action"><strong><s:text name="console.home.view.search-home"/></strong></a></td>
                </tr>
                 -->
              <!--/table>
              </div>
              <div id="seperatorLeft"></div-->
              <div class="ui-layout-center" style="width: 75%; float: left">
                <table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr><td colspan="3">&nbsp;</td></tr>
                  <tr>
                    <td width="33%" align="right" >&nbsp;Select Application</td>
                    <td width="2%">&nbsp;</td>
					<td width="65%" align="left"><s:select list="applications" id="appId" 
									name="applicationName" listKey="applicationId" listValue="applicationName" 
									headerKey="" headerValue="Select App"></s:select>
						<s:iterator value="applications" status="ct">
						<input type="hidden" name="applications[<s:property value="#ct.index"/>].modelId"
								id="applications<s:property value="#ct.index"/>modelId"
								value="<s:property value="modelId"/>" />
						</s:iterator>		
						</td>				
                  </tr>
                  <tr><td colspan="3" align="center">&nbsp;<div id="appError" style="color:red;"><s:actionerror /> </div></td></tr>
                  <tr><td colspan="3" align="center"><div id="appStatus" style="color:green;"></div></td></tr>
                  </table>
              </div>
              
              <div id="seperatorLeft" style="height:100px;"></div>
              <div class="ui-layout-west" style="overflow-x: hidden; width: 23%; float: left">
                <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td align="center">&nbsp;</td>
                    <td>&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="29%" height="45" align="center"><img src="../images/CSV1.png" width="35" height="35" /></td>
                    <td width="71%"><a class="links" href="../publisher/showUploadCSV.action?wizardBased=Y&sourceType=CSV">Upload CSV file</a><br /></td>
                  </tr>
                  <tr>
                    <td height="45" align="center"><img src="../images/EXCEL1.png" width="35" height="35" /></td>
                    <td><a class="links" href="../publisher/showUploadXL.action?sourceType=CSV">Upload EXCEL with single worksheet</a></td>
                  </tr>
                  <!--tr>
                    <td height="45" align="center"><img src="../images/EXCEL2.png" width="35" height="35" /></td>
                    <td><a class="links" href="#">Upload EXCEL with multiple worksheets</a></td>
                  </tr>
                  <tr>
                    <td height="45" align="center"><img src="../images/ASSETS2.png" width="35" height="35" /></td>
                    <td><a class="links" href="#">Absorbing an asset with a RDBMS connection</a></td>
                  </tr-->
                  <tr>
                    <td height="45" align="center"><img src="../images/checkStatus.jpg" width="35" height="35" /></td>
                    <td><a class="links" href="../swi/showJobRequests.action">Check  Request Status</a></td>
                  </tr>
                </table>
              </div>
              </div>
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
							 
	if($("#appId")){
		setAppInfo(false);
		$("#appId").bind("change", function(){
			setAppInfo(true);
		});
	}
	/*$("#container-1 a").css("fontWeight","normal");	
	$("#container-1 a#PublisherConsole").css("fontWeight","bold");	
	$("#currenPageDiv").html("Publisher Console");*/
	
});
function setAppInfo(fromChangeEvent){
	var applIdFromSession = '<s:property value="applicationContext.appId"/>';
	var modIdFromSession = '<s:property value="applicationContext.modelId"/>';	
	var applId = "";
	var modId = "";
	if(applIdFromSession.length == 0 && !fromChangeEvent){
		// 1st time when the page is loading.
		// not setting on load into session any more.		
		indx = $("select#appId").attr("selectedIndex")-1;
		size = $("select#appId").find("option").length;
		appName = $("select#appId").find("option:selected").text();
		applId = $("select#appId").find("option:selected").val();
		modId = $("#applications"+indx+"modelId").val();
		if(applId != "" && size > 1){
			//$("#appNameDiv").text("( "+appName+" )");
			//$.post("../swi/setAppInfo.action",{applicationId:applId, modelId:modId, applicationName:appName});
		}
		else if(applId == "" && size == 1){
			//$.post("../swi/setAppInfo.action",{applicationId:0, modelId:0, applicationName:0});
			showNoApplication();
		} else if(applId == ""){
			$("#appDiv").attr('style', 'display: none;');
		}
	}
	else if (fromChangeEvent){
		// a change event from the select box has been triggered.
		indx = $("select#appId").attr("selectedIndex")-1;
		appName = $("select#appId").find("option:selected").text();
		applId = $("select#appId").find("option:selected").val();
		modId = $("#applications"+indx+"modelId").val();
		if($("select#appId option").length > 0 && appName != "Select App"){
			$.post("../swi/setAppInfo.action",{applicationId:applId, modelId:modId, applicationName:appName});
			$("#appError").html('');
			$("#appStatus").html(appName + ': selected successfully');
			$("#appNameDiv").text("( "+appName+" )");			
			$("#appDiv").attr('style', 'display: inline;');
		} else if(appName == "Select App"){
			// remove every div text no needed to be displayed.
			$("#appStatus").html("");
			$("#appError").html("");
			$("#appDiv").attr('style', 'display: none;');
			$.post("../swi/setAppInfo.action",{applicationId:applId});
		}
	} else {
	 // the else situation would be landing up again on console home or reloading the console home page,
	 // in such situations no need to hit the server again. Just update the list.
	 	if($("select#appId").find("option").length > 0){
		 	applId = applIdFromSession;
		 	modId = modIdFromSession;
			$.each($("select#appId option"),function(i,v){
				 if($(this).val() == applId){
					$(this).attr("selected", true);
			 	 }
			});
			if($("select#appId").find("option:selected").text() != "Select App"){
				$("#appDivArrow").attr('style', 'display: inline;');
				$("#appDiv").attr('style', 'display: inline;');
			}
			// remove every div text no needed to be displayed.
			$("#appStatus").html("");
		}
		else
			showNoApplication();
	}
}
function showNoApplication(){
	var createAppLink = '<br/><div align="center"><font style="font-size: 11pt;"><s:text name="execue.console.home.create.active.applications"><s:param>' 
						+ '<a style="color: #003399; text-decoration: none;font-size: 11pt;font-weight: bold;" href="../swi/showApplications.action">'
						+ '<s:text name="execue.console.home.create.active.applications.link"/></a></s:param></s:text></font></div>';
	$(".ui-layout-center").html(createAppLink);
}
  </script>