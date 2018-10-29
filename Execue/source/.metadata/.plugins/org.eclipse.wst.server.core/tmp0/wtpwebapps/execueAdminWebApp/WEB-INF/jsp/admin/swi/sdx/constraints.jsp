<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="aw" uri="/WEB-INF/tlds/absorptionWizard.tld"%>
<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
    <tr>
      <td height="30" valign="bottom"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="29" class="titleWithBackground"><s:text name="execue.constraints.heading"/></td>
        </tr>
      </table></td>
    </tr>
    <tr>
      <td><div id="greyBorder">
      
      <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-bottom:15px;">
        <tr>
          <td colspan="3" valign="top" class="descriptionText">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="70%" >  <s:text name="execue.constraints.description"/></td>
                    <td width="30%" align="right" valign="middle">
                    <!--
                    <%--aw:absorptionWizardNextTag sourceType="${requestScope.asset.originType}" currentPage="CONSTRAINTS"/--%>
                    <br/>
                    -->
                    <jsp:include page='/views/admin/showSelectAsset.jsp' flush="true" />
                    </td>
                  </tr>
                </table>
          </td>
          </tr>
        <tr id="dottedLine" style="display:none">
          <td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
          </tr>
        <tr id="container" style="display:none">
          <td width="22%" valign="top">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td height="12">&nbsp;</td>
            </tr>
            <tr>
              <td><s:text name="execue.constraints.ws.asset" />&nbsp;&nbsp;<strong><s:property value="asset.displayName" /></strong></td>
            </tr>
           
          <tr>
              <td align="left">
              <div  class="tableBorder" style="padding-top:5px;height:275px;width:225px;margin-bottom:5px;margin-top:5px;border:none;" >
                <table width="99%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td height="30"  align="left" valign="top"><div id="divAssetTables">
                      <div id="roundedSearch"  >
                        <div class="searchStart" ></div>
                        <input class="searchField" id="searchText" 
type="search" value="Search" />
                        <div class="searchEnd" id="searchIcon"><a href="#"><img
                                            src="../images/admin/searchEnd.gif" name="Image2" border="0"
                                            id="Image2"
                                            onclick="MM_showMenu(window.mm_menu_0113140999_0,0,25,null,'Image2')"
                                            onmouseout="MM_startTimeout();" /></a></div>
                        </div>
                      <!--div id="searchTables" ><a href="javascript:showSearch('divAssetTables');" class="links"><s:text name="global.search"/></a></div-->
                      </div></td>
                    </tr>
                  <tr>
                    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><div class="tableList" style="height:255px;width:210px;margin-left:0px;margin-bottom:5px;overflow-y:hidden;">
                          <div id="dynamicPaneAssetTables"> </div>
                          </div></td>
                        </tr>
                      </table></td>
                    </tr>
                  </table>
                </div></td>
            </tr>
          </table>
          </td>
          <td width="3%" align="center" class="blueLineVeritical" style="height:340px;">&nbsp;</td>
          <td width="75%">
          <table border="0" cellpadding="0" cellspacing="0">
            <!-- fwtable fwsrc="blueBox.png" fwpage="Page 1" fwbase="blueBox.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->
            <tr>
              <td>&nbsp;</td>
              </tr>
            <tr>
              <td  height="300" align="center" valign="top"><div id="dynamicPane" style="width:670px"></div></td>
              </tr>
            <tr>
              <td>&nbsp;</td>
              </tr>
          </table>
          </td>
        </tr>
       
        <tr id="dottedLine" >
          <td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
          </tr>
      </table>
     
      </div></td>
    </tr>
  </table>
<s:hidden id="assetId" name="asset.id"/>
    <s:hidden id="tableId" name="table.id"/>