<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="aw" uri="/WEB-INF/tlds/absorptionWizard.tld"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="left" height="30">
    <s:if test='%{jobRequest.jobStatus.value == 3 && jobRequest.jobType.value == 10}'>
      <%--aw:absorptionWizardNextTag sourceType="${requestScope.fileInfo.sourceType}" currentPage="EVALUATE-STATUS" /--%>
    </s:if>
    <s:elseif test='%{jobRequest.jobStatus.value == 3 && jobRequest.jobType.value == 9}'>
      <%--aw:absorptionWizardNextTag sourceType="${requestScope.fileInfo.sourceType}" currentPage="ABSORB-STATUS" /--%>
    </s:elseif>
    <s:elseif test='%{jobRequest.jobStatus.value == 4}'>
      <%--aw:absorptionWizardNextTag sourceType="${requestScope.fileInfo.sourceType}" currentPage="PUBLISH" /--%>
    </s:elseif>
    </td>
  </tr>
  <tr>
    <td width="51%">

    <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td class="fieldNames" width="15%" ><strong><s:text name="execue.upload.main.file.name" /> :</strong></td>
        <td class="fieldNames"><s:property value="fileInfo.originalFileName" /></td>
      </tr>
      <!--
                    <tr>
                      <td class="fieldNames"><s:text name="upload.main.asset.name" /></td>
                      <td><s:property value="asset.displayName" /></td>
                    </tr>
                    <tr>
                      <td class="fieldNames"><s:text name="upload.main.asset.description" /></td>
                      <td><s:property value="asset.description" /></td>
                    </tr>
                    -->
      <tr>
        <td class="fieldNames"><strong><s:text name="execue.upload.main.request.status" /> :</strong></td>
        <td class="fieldNames"><div id="requestStatusLabel" style="min-width:60px;width:auto;float:left;white-space:nowrap;"><strong><s:property value="jobRequest.jobStatus" /></strong></div>
        <div id="requestStatusLabelLoader" style="display:none;width:60px;float:left;padding-left:10px;"><img src='../images/admin/loading.gif' width='25' height='25' /></div>
        <div id="detailsDiv" style="padding-left:10px;float:left;width:100px;">(  <a href="javascript:showStatusScreen();">Details</a> ) </div></td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td height="10">&nbsp;</td>
  </tr>
  <tr>
    <td width="49%" valign="top">
    <div id="statusPaneDiv" class="statusPane" style="overflow: auto; width: 60%; min-height: 140px;height:auto; border: 1px solid #ccc;display:none;padding-bottom:10px;margin-left:50px;">
    <table width="98%" border="0" align="center" cellpadding="3" cellspacing="0" >
    <tr>
        <td class="fieldNames" width="25%">&nbsp;</td>
        <td class="fieldNames" width="25%">&nbsp;</td>
        <td align="right" width="50%" ><a href="javascript:closeStatusScreen();"><img src="../images/admin/deleteButton1.jpg" border="0" /></a></td>
      </tr>
      </table>
    <table width="98%" border="0" align="center" cellpadding="3" cellspacing="1" id="tableGridMemberInfo">
     
      <tr id="tableGridTitle">
        <td class="fieldNames"  width="35%"><strong><s:text name="execue.upload.main.request.stage" /></strong></td>
        <td class="fieldNames"  width="25%"><strong><s:text name="execue.upload.main.request.stage.status" /></strong></td>
        <td class="fieldNames"  width="40%"><strong><s:text name="execue.upload.main.request.stage.statusDetail" /></strong></td>
      </tr>
      <s:iterator value="jobStatusSteps" status="even_odd" id="jobStatusStep">
        <tr>
          <td class="fieldNames"  width="35%"><s:property value="operationalStage" /></td>
          <td class="fieldNames"  width="25%"><s:property value="jobStatus" /></td>
          <s:if test="#jobStatusStep.jobStatus.value==1 || #jobStatusStep.jobStatus.value==2 ">
              <input type="hidden" id="operationalStageId" value="<s:property value='operationalStage' />" /> 
              
          </s:if>
          <td width="40%" class="fieldNames"><s:property value="statusDetail" /></td>
        </tr>
      </s:iterator>
    </table>
    </div>
    </td>
  </tr>
 
  <tr>
    <s:if test='%{jobRequest.jobStatus.value == 3 && jobRequest.jobType.value == 10}'>
      <td colspan="1" align="center" height="40" valign="bottom"><!-- 
                    <span class="fieldNames">
                        <input type="button" class="buttonSize160" value="<s:text name='global.viewDatasetDetails' />" name="imageField" onClick="javascript:showAssetScreen();"/>
                    </span>
                    --> &nbsp;</td>
    </s:if>
    <s:elseif test='%{jobRequest.jobStatus.value == 3 && jobRequest.jobType.value == 9}'>
      <td colspan="1" align="center" height="40" valign="bottom"><!-- 
                    <span class="fieldNames">
                        <input type="button" class="buttonSize160" value="<s:text name='global.reviewMetaData' />" name="imageField" onClick="javascript:showConfirmationScreen();" />
                    </span>
                    --> &nbsp;</td>
    </s:elseif>
    <s:elseif test='%{jobRequest.jobStatus.value == 4}'>
      <td colspan="1" align="center" height="40" valign="bottom"><span class="fieldNames"> <input
        type="button" class="buttonSize108" value="<s:text name='execue.console.consoleHome.button' />" name="imageField"
        onClick="javascript:showConsoleHome();" /> </span></td>
    </s:elseif>
    <s:else>
      <td colspan="1" align="left" height="40" style="padding-left:50px;padding-top:10px;" ><span class="fieldNames"> <input
        type="submit" class="buttonSize108" name="imageField" id="imageField"
        value="<s:text name='execue.global.refreshStatus.button' />" /> </span></td>
    </s:else>
  </tr>
</table>

<s:hidden name="wizardBased" id="wizardBased" />
<s:hidden name="sourceType" id="sourceType" />
<s:hidden name="jobRequest.jobStatus" id="jobRequestStatus" />
<s:hidden name="fileInfo.fileId" id="fileInfoId" />
<s:hidden name="asset.id" id="assetId" />
<s:hidden name="asset.name" id="assetName" />
<script>
$(function(){
		   
  showHideLoader();		   
		   });

function showHideLoader(){
	var statusText=$("#requestStatusLabel").text();
	if((statusText=="PENDING")||(statusText=="INPROGRESS")){
		$("#requestStatusLabelLoader").show();
		$("#requestStatusLabel").text($("#operationalStageId").val());
	}
}
</script>