<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib prefix="s" uri="/struts-tags"%>
<table border="0" cellpadding="0" cellspacing="0" width="195">
    <!-- fwtable fwsrc="blueBox.png" fwpage="Page 1" fwbase="blueBoxLeft.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->
    <tr>
      <td height="30" class="tableHeading"><table width="100%" border="0" cellspacing="0" cellpadding="3">
        <tr>
          <td width="80%" valign="bottom" class="tableSubHeading">User Defined Ranges</td>
        </tr>
      </table></td>
    </tr>
    <tr>
      <td  valign="top">
      <div class="tableBorder" style="width:195px;border:none;">
      <s:form id="userDefinedRangesForm">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="30" valign="top"><div id="divDefineRanges">
										<!--DIV id=roundedSearch >
										<div class=searchStart></div>
										<INPUT class=searchField id=searchText type=search
											value=Search>
										<div class=searchEnd id=searchIcon><a href="#"><img
											src="../images/admin/searchEnd.gif" name="Image2a" border="0"
											id="Image2a"
											onMouseOver="MM_showMenu(window.mm_menu_0113140999_0,0,25,null,'Image2a')"
											onMouseOut="MM_startTimeout();" /></a></div>
										</DIV-->

										<!--div id="searchTables"><a
											href="javascript:showSearch('divDefineRanges');" class="links"><s:text
											name="global.search" /></a></div-->
										</div></td>
        </tr>
        <tr>
          <td><div id="existingRangesTarget" class="tableListWithoutBorder" style="height:220px;width:190px;">
            <table width="100%" border="0" cellspacing="0" cellpadding="2" id="searchList">
              <s:iterator value="userDefinedRanges" status="inst" id="range">
              <s:if test="#inst.even == true">
                            <tr class="joinRowEven" id="tableRow3" >
                <td width="10%" class="dotBullet" valign="top"><input name="rangeIdsForDeletion[<s:property value="#inst.index"/>]" type="checkbox" id="rangeIdsForDeletion[<s:property value="#inst.index" />]" value="<s:property value="id"/>" onclick="checkedRanges();"/></td>
                <td width="90%" colspan="2" class="fieldNames">
                <div id="showUserRange<s:property value="id"/>Link">
                <a href="javascript:getRangeById(<s:property value="%{#range.id}" />);" class="links"><s:property value="%{#range.name}" /></a></div>
                <div id="loadingShowUserRange<s:property value="id"/>Link" style="display: none;"><img src="../images/admin/loading.gif" width="20"height="20"></div>
                </td>
              </tr>
              </s:if>
              <s:else>
                 <tr id="tableRow3">
                <td width="10%" class="dotBullet"><input name="rangeIdsForDeletion[<s:property value="#inst.index"/>]" type="checkbox" id="rangeIdsForDeletion[<s:property value="#inst.index"/>]" value="<s:property value="id"/>" onclick="checkedRanges();"/></td>
                <td width="90%" colspan="2" class="fieldNames">
                 <div id="showUserRange<s:property value="id"/>Link">
                <a href="javascript:getRangeById(<s:property value="%{#range.id}" />);" class="links"><s:property value="%{#range.name}" /></a></div>
                <div id="loadingShowUserRange<s:property value="id"/>Link" style="display: none;"><img src="../images/admin/loading.gif" width="20"height="20"></div>
                </td>
              </tr>
              </s:else>
              </s:iterator>
              
            </table>
          </div></td>
        </tr>
        <tr>
          <td align="left">
          	<div style="display: none" id="enableDeleteSelect" style="margin-left:45px;">
                <input type="button"	class="buttonSize108" 
					value="<s:text name='execue.global.deleteSelectedButton.name' />" onclick="return deleteRange();"/></div>
             <span id="deleteProcess" style="display:none">
                  <img src="../images/admin/loadingWhite.gif" hspace="3" vspace="5" /></span>
              <div id="disableDeleteSelect" style="margin-left:45px;">
                   <input type="button"	class="buttonSize108" disabled="disabled"
					value="<s:text name='execue.global.deleteSelectedButton.name' />" /></div>
        </tr>
      </table>
      </s:form>
      </div></td>
    </tr>
  </table>
  <script>

  $(function(){
			 $("#divDefineRanges").searchRecordsComponent({actionName:"",targetDivName:"existingRangesTarget"});
			 });
  </script>