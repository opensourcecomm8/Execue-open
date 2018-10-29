<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="aw" uri="/WEB-INF/tlds/absorptionWizard.tld"%>
<jsp:include page="/WEB-INF/jsp/admin/swi/sdx/sdxPopupTemplate.jsp"></jsp:include>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="30" valign="bottom">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="29" class="titleWithBackground"><s:text name="execue.asset.heading"/></td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td>
    <div id="greyBorder">
    <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr><td>
      
       <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"><tr>
        
      
      <td height="20"  align="left" width="100%" valign="middle">
      <!--div style="padding-bottom:6px;">
      <%--aw:absorptionWizardNextTag sourceType="${requestScope.asset.originType}" currentPage="DATASET-DEFINITION"/--%>
      </div-->
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
      
     
        <td height="23" width="100%" align="left" ><div id="showDasetCollectionLink"><a href="../swi/showAssets.action" class="arrowBg"><s:text name="execue.showAssets.showAssets.linkName" /></a></div></td>
      </tr>
      <tr>
        <td height="23" width="100%"><jsp:include page='/views/admin/showSelectAsset.jsp' flush="true" /></td>
     
       
      </tr>
       
    </table>

      </td>
      
    </tr>
     <tr><td valign="top" class="descriptionText" width="70%"><s:text name="execue.showAsset.description"/></td></tr>
      <tr>
    </table>
    </td></tr>
   
        <td valign="top">
        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td>


            <div id="container"
              style="width: 100%; min-height: 375px;height:auto; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
            <!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
            <div class="ui-layout-west" style="overflow-x: hidden;height:400px;padding-left:0px;" >
            <table border="0" cellpadding="0" cellspacing="0" height="300">
              <tr>
                <td height="23" colspan="2" class="tableHeading" >
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="80%" valign="bottom" class="tableSubHeading"><s:text name="execue.asset.left-pane.heading"/></td>
                  </tr>
                  <tr>
                    <td height="20" class="fieldNames"><s:text name="execue.asset.source.name"/>: <s:property value="asset.dataSource.name"/></td>
                  </tr>
                 
                  <tr>
                    <td height="20" class="fieldNames"><s:text name="execue.asset.source.description"/>: <s:property value="asset.dataSource.description"/></td>
                  </tr>
                  
                   <tr>
                    <td height="20" class="searchComponentTd">
                   
                    <div id="divSearchSourceTables">
										
										</div>
                                        
                       
                    
                    </td>
                  </tr>
                </table>
                </td>
              </tr>
              <tr>
                <td width="158" valign="top">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                    <div id="sourceTableList" class="sourceTableList" style="height: 235px; width: 210px;overflow:hidden;border:none;">
                     
                    </div><div id="loadingSTLink" style="display:none">
						<img src="../images/admin/loaderAT.gif"/>
					</div>
                    </td>
                  </tr>
                </table>
                </td>
                <td width="15">&nbsp;</td>
              </tr>
             
            </table>
            </div>
            <div class="ui-layout-center" style="overflow:hidden;height:350px;">
            <div id="dynamicPane">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td height="20" class="tableSubHeading"><s:text name="execue.asset.ws.heading"/></td>
              </tr>
              <tr>
                <td>
                <div id="TabbedPanels1" class="TabbedPanels" style="margin-bottom:30px;">
                <ul class="TabbedPanelsTabGroup">
                  <li class="TabbedPanelsTab" tabindex="0"><s:text name="execue.asset.ws.tab.asset.heading"/></li>
                  <li class="TabbedPanelsTab" tabindex="0"><s:text name="execue.asset.ws.tab.table.heading"/></li>
                  <li class="TabbedPanelsTab" tabindex="0"><s:text name="execue.asset.ws.tab.column.heading"/></li>
                  <li class="TabbedPanelsTab" tabindex="0"><s:text name="execue.asset.ws.tab.member.heading"/></li>
                </ul>
                <div class="TabbedPanelsContentGroup">
                <div class="TabbedPanelsContent" style="height:295px;">
                <div id="assetIno">
                </div>
                </div>
                <div class="TabbedPanelsContent" style="height:295px;">
                <div id="tableIno">
                </div>
                </div>
                <div class="TabbedPanelsContent" style="height:295px;">
                <div id="columnIno">
                </div>
                </div>
                <div class="TabbedPanelsContent" style="height:295px;">
                <div id="memberIno">
                </div>
                </div>
                </div>
                </div>
                </td>
              </tr>
            </table>
            </div>
            </div>

            <div class="ui-layout-east" style="overflow: hidden;">
            <table border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="25" colspan="2" class="tableHeading">
                <table width="200" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="80%" valign="bottom" class="tableSubHeading"><s:text name="execue.asset.right-pane.heading"/></td>
                  </tr>
                  <tr>
                    <td height="22" class="fieldNames"><s:text name="execue.asset.asset.name"/>: <s:property value="asset.displayName"/></td>
                  </tr>
                  
                  <tr>
                    <td height="22" class="fieldNames"><s:text name="execue.asset.asset.description"/>: <s:property value="asset.description"/></td>
                  </tr>
                  <tr>
                    <td height="22" class="searchComponentTd">
                    
                   
                    
                    <div id="divAssetTableList">
										
										</div>
				    </td>
                  </tr>
                </table>
                </td>
              </tr>
              <tr>
                <td width="158" valign="top">
                <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td>
                    <div id="assetTableList" >
                    
                    </div>
                    <div id="loadingATLink" style="display:none">
						<img src="../images/admin/loaderAT.gif"/>
					</div>
                    </td>
                  </tr>
                  <tr>
                    <td align="center"><div style="display: none" id="enableDeleteAsset">
                    	<input type="button"	class="buttonSize108" value="<s:text name='execue.global.deleteSelectedButton.name' />"  onclick="javascript:deleteTables();"/></div>
                        <span id="deleteProcess" style="display:none">
                        <img src="../images/admin/loadingWhite.gif" hspace="3" vspace="5" /></span>
                        <div id="disableDeleteAsset">
                        <input type="button" disabled="disabled" class="buttonSize108" value="<s:text name='execue.global.deleteSelectedButton.name' />" /></div>
                    </td>
                  </tr>
                </table>
                </td>
                <td width="15">&nbsp;</td>
              </tr>

            </table>
            </div>
            </div>
            &nbsp;</td>
          </tr>
        </table>
        </td>
      </tr>
    </table>
    </div>
    </td>
  </tr>
</table>
<form id="requiredValuesForm">
  <s:hidden id="assetDataSourceId" name="asset.dataSource.id"/>
  <s:hidden id="assetId" name="asset.id"/>  
  <s:hidden id="assetName" name="asset.name"/>
</form>
<s:hidden id="assetOrigin" name="asset.originType"/>