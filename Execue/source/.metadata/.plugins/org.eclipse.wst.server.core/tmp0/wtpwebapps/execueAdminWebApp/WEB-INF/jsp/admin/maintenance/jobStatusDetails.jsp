<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="aw" uri="/WEB-INF/tlds/absorptionWizard.tld"%>
<style>

</style>
<div id="statusPaneDiv" class="statusPane" style="width:440px;overflow: auto; min-height: 140px;height:auto; border: 1px solid #ccc;padding-bottom:10px;margin:5px;">
    <table width="98%" border="0" align="center" cellpadding="3" cellspacing="0">
    <tr>
        <td class="fieldNames" width="25%">&nbsp;</td>
        <td class="fieldNames" width="25%">&nbsp;</td>
        <td align="right" width="50%" ><a href="javascript:closeStatusScreen();"><img src="../images/admin/deleteButton1.jpg" border="0" /></a></td>
      </tr>
      </table>
    <table width="98%" border="0" align="center" cellpadding="3" cellspacing="1" id="tableGridMemberInfo"  bgcolor="#FFFFFF">
     
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
    
<script>

function closeStatusScreen(){
	$("#statusPaneDiv").parent().hide();
}
</script>