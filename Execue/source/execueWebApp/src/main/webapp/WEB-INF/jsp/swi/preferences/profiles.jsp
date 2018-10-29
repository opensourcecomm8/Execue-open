<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="30" valign="bottom">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="29" class="titleWithBackground"><s:text name="execue.profiles.heading" /></td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td>
    <div id="greyBorder">
    <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top" class="descriptionText"><s:text name="execue.profiles.description" /></td>
      </tr>
      <tr>
        <td valign="top">

        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-bottom:15px;">
          <tr>
            <td>


            <div id="container"
              style="width: 100%; height: 345px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
            <!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
            <div class="ui-layout-west" style="overflow-x: hidden;padding-left:0px;width:200px;padding-right:0px;">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td height="27"><span class="tableSubHeading"><s:text name="execue.profiles.base.heading" /></span></td>
              </tr>
              <tr>
                <td>

                <div class="tableBorder" style="padding-top: 5px; height: 305px;border:none;">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                 <tr>
    <td height="30"  align="left" valign="top"><div id="divProfiles">
										
										</div>
</td>
  </tr>
                  <tr>
                    <td> <div id="dynamicPaneProfileConcepts"></div>
                   
                    </td>
                  </tr>
                </table>

                </div>
                </td>
              </tr>
            </table>

            </div>
            <div class="ui-layout-center" style="width:500px;">
            <div id="dynamicPane">
            </div>
            </div>

            <div class="ui-layout-east" style="overflow-x: hidden;padding-left:0px;">
            <table border="0" cellpadding="0" cellspacing="0">
              <!-- fwtable fwsrc="blueBox.png" fwpage="Page 1" fwbase="blueBoxLeft.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->
              <tr>
                <td height="30" class="tableHeading">
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td width="80%" valign="bottom" class="tableSubHeading"><s:text name="execue.profiles.user-defined.heading"/></td>
                  </tr>
                </table>
                </td>
              </tr>
              <tr>
                <td width="200" valign="top" >
                <div class="tableBorder" style="width: 188px;border:none;">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
          <td height="30" valign="top"><div id="divUserDefinedProfiles">
										
										</div></td>
        </tr>
			<tr>
                  <tr>
                    <td>
                    <div id="rightPane" class="tableList" style="height: 220px; width: 190px; margin: auto; margin-left: 3px;"></div>
                    </td>
                  </tr>
                  <tr>
                    <td align="left" ><div style="display: none;float:left;margin-left:45px;" id="enableDeleteSelect">
                   <input type="button"	class="buttonSize108" 
					value="<s:text name='execue.global.deleteSelectedButton.name' />" onclick="javascript: deleteProfile();"/></div>
                      <span id="deleteProcess" style="display:none">
                       <img src="../images/loadingWhite.gif" hspace="3" vspace="5" /></span>
                        <div id="disableDeleteSelect" style="margin-left:45px;">
                        <input type="button"	class="buttonSize108"  disabled="disabled"
					value="<s:text name='execue.global.deleteSelectedButton.name' />" /></div>
                      </td>
                  </tr>
                </table>
                </div>
                </td>
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