<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="30" valign="bottom">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="29" class="titleWithBackground"><s:text name="execue.upload.main.heading" /></td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td>
    <div id="greyBorder">
    <table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top" class="descriptionText"><s:text name="execue.upload.main.description" /></td>
      </tr>
      <tr>
        <td valign="top">

        <table width="400" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td>


            <div id="container"
              style="width: 950px; height: 365px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
            <!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->

            <!-- 
            <div class="ui-layout-west" style="overflow-x: hidden; width: 10px"></div>
             -->
            <div class="ui-layout-center" style="width: 800px; overflow: hidden;">
            <div id="dynamicPane">

            <div id="errorMessage"><s:fielderror /> <s:actionmessage /> <s:actionerror /></div>

            <div class="innerPane" style="width: 99%"><s:form namespace="/publisher" action="uploadXL"
              method="post" enctype="multipart/form-data">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="51%">

                  <table width="90%" border="0" align="center" cellpadding="3" cellspacing="0">
                    <tr>
                      <td class="fieldNames"><!--a id="addNewApplication" href="../swi/showApplications.action"><s:text name='execue.uploadCSV.CreateApplication.link' /></a--></td>
                      <td height="40">&nbsp;</td>
                    </tr>
                    <tr>
                      <td class="fieldNames"><s:text name="execue.upload.main.asset.name" /></td>
                      <td><s:textfield name="execue.asset.displayName" id="asset.displayName" /></td>
                    </tr>
                    <tr>
                      <td class="fieldNames"><s:text name="execue.upload.main.asset.description" /></td>
                      <td><s:textfield name="execue.asset.description" id="asset.description" /></td>
                    </tr>
                    <tr>
                      <td height="35" colspan="2" class="fieldNames">&nbsp;</td>
                    </tr>
                  </table>

                  </td>
                  <td width="49%" valign="top">
                  <table width="90%" border="0" align="center" cellpadding="3" cellspacing="0">
                    <tr>
                      <td class="fieldNames">&nbsp;</td>
                      <td height="40">&nbsp;</td>
                    </tr>
                    <tr>
                      <td width="24%" class="fieldNames"><s:text name="execue.upload.main.source-file" /></td>
                      <td width="76%" align="left"><label> <s:file name="sourceData" label="File" /> </label></td>
                    </tr>
                    <tr>
                      <td height="35" colspan="2" class="fieldNames">
                      <div style="width: 90%; border: 1px solid #CCC; padding: 10px;">
                      <table width="80%" border="0" align="center" cellpadding="4" cellspacing="0">
                        <tr>
                          <td colspan="2">
                         
                          </td>
                        </tr>
                        <tr>
                          <td width="71%"><s:text name="execue.upload.main.columns.specified" /></td>
                          <td width="29%"><label> <s:checkbox name="columnNamesPresent" value="true"
                            fieldValue="true" /> </label></td>
                        </tr>
                       
                        <tr>
                          <td><s:text name="execue.upload.main.null.specified.as" /></td>                         
                           <td><s:select name="nullIdentifier" id="nullIdentifier"
						list="CSVEmptyFields" /></td>
                        </tr>                       
                      </table>
                      </div>
                      </td>
                    </tr>
                  </table>
                <tr>
                  <td colspan="2">&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="2" align="center" height="40" valign="bottom"><span class="fieldNames"> <input type="submit" class="buttonSize108" name="imageField" id="imageField" value="<s:text name='execue.global.absorbButton' />" /> <input type="button" class="buttonSize51"  name="imageField2" id="imageField2" value="<s:text name='execue.global.cancel' />" /> </span></td>
                </tr>
              </table>
              
              <s:hidden name="wizardBased"/>
              <s:hidden name="sourceType"/>
              <s:hidden name="operationType"/>
           </s:form>
           </div>
            </div>
            </div>
            <!-- 
            <div class="ui-layout-east" style="overflow-x: hidden;width:10px">
            </div>
            --></div>
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