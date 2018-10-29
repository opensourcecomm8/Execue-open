<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="30" class="tableHeading">
    <table width="100%" border="0" cellspacing="0" cellpadding="3">
      <tr>
        <td width="80%" valign="bottom" class="tableSubHeading"><s:text
          name="execue.cubeCreation.dimension.selection.selectedDimensions.heading" /></td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td width="220" valign="top">
    <div class="tableBorder" style="width: 188px;border:none;">
    <s:form id="selectedDimensionsForm">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
       <tr>
          <td height="30" valign="top"><div id="divSelectedDimensions">
										
										</div></td>
        </tr>
			<tr>
        <tr>
          <td>
          <div id="divDimensionsTable" class="tableListWithoutBorder" style="height: 220px; width: 180px;padding-left:3px;padding-top:3px;">
          <table width="100%" border="0" cellspacing="0" cellpadding="2" id="searchList">
            <s:iterator value="localUICubeCreation.selectedConcepts" status="inst" id="dimension">
              <s:if test="#inst.even == true">
                <tr class="joinRowEven" id="tableRow3">
                  <td width="10%" class="dotBullet"><input
                    name="dimensionIndexesForDeletion[<s:property value="#inst.index"/>]" type="checkbox" onclick="checkedCube();"
                    id="dimensionIndexesForDeletion[<s:property value="#inst.index"/>]" value="<s:property value="id"/>" /></td>
                  <td width="90%" colspan="2" class="fieldNames">
                  	<div id="showSelectedConcept<s:property value="#inst.index"/>Link">
                  <a  href="javascript:showSelectedDimensionDetails('SL', <s:property value="#inst.index"/>);" class="links"><s:property
                    value="displayName" /></a></div>
                    <div id="loadingShowSelectedConcept<s:property value="#inst.index"/>Link" style="display: none;">
                    	<img src="../images/admin/loadingBlue.gif" width="25" height="25" /></div>
                    </td>
                </tr>
              </s:if>
              <s:else>
                <tr id="tableRow3">
                  <td width="10%" class="dotBullet"><input
                    name="dimensionIndexesForDeletion[<s:property value="#inst.index"/>]" type="checkbox" onclick="checkedCube();"
                    id="dimensionIndexesForDeletion[<s:property value="#inst.index"/>]" value="<s:property value="id"/>" /></td>
                  <td width="90%" colspan="2" class="fieldNames">
                  	<div id="showSelectedConcept<s:property value="#inst.index"/>Link">
                  <a href="javascript:showSelectedDimensionDetails('SL', <s:property value="#inst.index"/>);" class="links"><s:property
                    value="displayName" /></a></div>
                    <div id="loadingShowSelectedConcept<s:property value="#inst.index"/>Link" style="display: none;">
                    	<img src="../images/admin/loadingBlue.gif" width="25" height="25" /></div>
                    </td>
                </tr>
              </s:else>
            </s:iterator>
            <s:iterator value="localUICubeCreation.selectedRanges" status="inst" id="dimension">  
              <s:if test="#inst.even == true">
                <tr class="joinRowEven" id="tableRow3">
                  <td width="10%" class="dotBullet"><input
                    name="rangeIndexesForDeletion[<s:property value="#inst.index"/>]" type="checkbox" onclick="checkedCube();"
                    id="rangeIndexesForDeletion[<s:property value="#inst.index"/>]" value="<s:property value="conceptBedId"/>" /></td>
                  <td width="90%" colspan="2" class="fieldNames">
                  <div id="showSelectedRIConcept<s:property value="#inst.index"/>Link">
                  <a href="javascript:showSelectedDimensionDetails('RL',<s:property value="#inst.index"/>);" class="links"><s:property
                    value="name" /></a></div>
                    <div id="loadingShowSelectedRIConcept<s:property value="#inst.index"/>Link" style="display: none;">
                    	<img src="../images/admin/loadingBlue.gif" width="25" height="25" /></div>
                    </td>
                </tr>
              </s:if>
              <s:else>
                <tr id="tableRow3">
                  <td width="10%" class="dotBullet"><input
                    name="rangeIndexesForDeletion[<s:property value="#inst.index"/>]" type="checkbox" onclick="checkedCube();"
                    id="rangeIndexesForDeletion[<s:property value="#inst.index"/>]" value="<s:property value="conceptBedId"/>" /></td>
                  <td width="90%" colspan="2" class="fieldNames">
                  <div id="showSelectedRIConcept<s:property value="#inst.index"/>Link">
                  <a href="javascript:showSelectedDimensionDetails('RL', <s:property value="#inst.index"/>);" class="links"><s:property
                    value="name" /></a></div>
                    <div id="loadingShowSelectedRIConcept<s:property value="#inst.index"/>Link" style="display: none;">
                    	<img src="../images/admin/loadingBlue.gif" width="25" height="25" /></div>
                    </td>
                </tr>
              </s:else>
            </s:iterator>
          </table>
          </div>
          </td>
        </tr>
        <tr>
        <td align="center"><div style="display: none" id="enableDeleteCube">
            <img src="../images/admin/deleteSelectedButton.gif" width="136" height="27" hspace="3" vspace="5" onclick="javascript:deleteSelectedDimensions();"/></div>
            <span id="deleteProcess" style="display:none">
            <img src="../images/admin/loadingWhite.gif" hspace="3" vspace="5" /></span>
            <div id="disableDeleteCube">
            <img src="../images/admin/deleteSelectedButton_disabled.gif" width="136" height="27" hspace="3" vspace="5" /></div>
        </td>
          <!--td align="center"><img src="../images/deleteSelectedButton.gif" width="136" height="27" hspace="3"
            vspace="5" onclick="javascript: deleteSelectedDimensions();" /></td-->
        </tr>
      </table>
    </s:form></div>
    </td>
  </tr>
</table>
<script>
$(function(){
		   $("#divSelectedDimensions").searchRecordsComponent({actionName:"",targetDivName:"divDimensionsTable"});
		   if($("table#searchList tr").length>0){
		   $("#nextButton input[type='button']").attr("disabled",false);
		   }else{
		   $("#nextButton input[type='button']").attr("disabled",true);
		   }
		   });
</script>