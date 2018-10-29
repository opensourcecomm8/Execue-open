<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<title>Upload status</title>
</head> 
<body>
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
    <td >
    <div id="greyBorder">
    <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" align="center">
      <tr>
        <td valign="top" ><table><tr><td class="descriptionText" style="padding-left:5px;"><s:text name="execue.upload.main.description" /></td><td><!-- <a href="../publisher/showJobRequestStatus.action">Back</a>--></td>
      </tr>
      <tr>
        <td valign="top">

        <table width="400" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td>


            <div id="container"
              style="width: 950px; height: 305px; margin: auto; border-top: #CCC dashed 1px; ">
            <!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->

            <!-- 
            <div class="ui-layout-west" style="overflow-x: hidden; width: 10px">
            <div id="dynamicPaneWest">
            
            </div>
            
            </div>
             -->
            <div class="ui-layout-center" style="width: 800px; overflow: hidden;">
            <div id="dynamicPane">

            <div id="errorMessage"><s:fielderror /> <s:actionmessage /> <s:actionerror /></div>

            <div class="innerPane" style="width: 99%"><s:form  name="uploadStatusForm" namespace="/publisher" action="showUploadStatus"
              method="post">
              <div class="statusDetails" id="statusDetails">
              </div>
              <s:hidden name="jobRequest.id" id="jobRequestId" />
            </s:form></div>
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
    </td>
  </tr>
</table> </div> </td>
  </tr>
</table>
</body>
