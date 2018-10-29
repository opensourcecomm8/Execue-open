<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="30" valign="bottom">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="29" class="titleWithBackground"><s:text name="execue.asset.grain.heading" /></td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td>
    <div id="greyBorder">
    <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td colspan="3" valign="top" class="descriptionText"><s:text name="execue.showAssetGrain.description" /></td>
      </tr>
      <tr>
        <td  width="100%" align="left" >	  
	    <s:if test="sourceURL != null"><div id="assetAnalysisReportLink"><a href='<s:property value="sourceURL"/>' class="arrowBg">Dataset Collection Analysis Report</a></div>
	  </s:if></td>
      </tr>
      <tr>
        <td colspan="3" valign="top" background="../images/blueLine.jpg"><img src="../images/blueLine.jpg"
          width="10" height="1" /></td>
      </tr>
      <tr>
        <td width="25%" valign="top">



        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <!-- tr>
                      <td height="40"><div id="addNewConceptLink" ><a href="javascript:createNewConcept();" class="arrowBg" id="addNewConcept">Add New Concept</a></div>
                        <div id="loadingAddNewConceptLink" style="display:none;"><img src="../images/loadingBlue.gif" width="25" height="25" /></div></td>
             </tr-->

          <tr>
         
            <td align="center" valign="top" width="20%" >
                        <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
                        <tr><td height="30"> <span class="tableSubHeading" style="padding-left:2px;"><s:text name="execue.asset.grain.available.assets"/></span></td></tr>
                         <tr><td >
                        <div class="tableBorder" style="padding-top: 5px; height: auto; width: 200px; margin-bottom: 5px;border:none;">
                        
                        <table width="99%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                      <td height="30" valign="top"><div id="divGrain">
                                                    <DIV id=roundedSearch >
                                                    <div class=searchStart></div>
                                                    <INPUT class=searchField id=searchText type=search
                                                        value=Search>
                                                    <div class=searchEnd id=searchIcon1><a href="#"><img
                                                        src="../images/searchEnd.gif" name="Image2" border="0"
                                                        id="Image2"
                                                        onMouseOver="MM_showMenu(window.mm_menu_0113140999_0,0,25,null,'Image2')"
                                                        onMouseOut="MM_startTimeout();" /></a></div>
                                                    </DIV>
            
                                                    <!--div id="searchTables"><a
                                                        href="javascript:showSearch('divGrain');" class="links"><s:text
                                                        name="global.search" /></a></div>
                                                    </div--></td>
                    </tr>
                        <tr>
                          <tr>
                            <td>
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <td>
                               
                                <div id="dynamicPaneAssets"></div>
                              
                                </td>
                              </tr>
                            </table>
                            </td>
                          </tr>
                        </table>
                        </div>
                        </td></tr></table>
            </td>
            <td width="3%" height="auto" align="center" class="blueLineVeritical">&nbsp;</td>
            <td width="77%">
            <table border="0" cellpadding="0" cellspacing="0">
             
              <tr>
                <td height="335" align="center" valign="top">
                <div id="dynamicPane" style="width: 670px"></div>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
              </tr>
            </table>
        </table>
        </td>
      </tr>
      <tr>
        <td colspan="3" valign="top" background="../images/blueLine.jpg"><img src="../images/blueLine.jpg"
          width="10" height="1" /></td>
      </tr>
      <tr>
        <td colspan="3" valign="top" >&nbsp;</td>
      </tr>
    </table>
    </div>
    </td></tr></table>