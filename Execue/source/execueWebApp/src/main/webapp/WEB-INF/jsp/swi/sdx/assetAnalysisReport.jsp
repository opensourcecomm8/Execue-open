<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
a.thickbox:hover{
color:#CF030E;	
}
a.thickbox{
text-decoration:none;color:#003399;
}


</style>
<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
  <tr>
    <td height="30" valign="bottom">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="29" class="titleWithBackground"><strong><s:text name="execue.assetAnalysisReport.main.description"></s:text> </strong> </td>
      </tr>
       <tr>       
        <td height="29" class="titleWithBackground"><strong><a href="showPublishDataset.action"><s:text name="execue.assetAnalysisReport.main.link.publish"></s:text> </a> </strong> </td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td>
    <div id="greyBorder" style="padding-left:3px;height:450px;">
    <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top" class="descriptionText"><s:text name="execue.assetAnalysisReport.main.reportType"></s:text> </td>
      </tr>
      <tr>
        <td>
        <div id="message" STYLE="color: green;" align="center"></div>
        </td>
      </tr>
      <tr>
        <td valign="top">
          <table  border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
              <td>
              <div id="container"
                style="width: 950px; height: 400px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">              
              <div class="ui-layout-east" style="overflow-x: hidden; width: 21%; float: left">
              <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <s:iterator value="assetAnalysisOperationTypes">
                 <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td class="fieldNames" height="22"><a href="#" onclick="javascript:getAssetAnalysisReport('<s:property/>',<s:property value="selectedAssetId"/>);"> <s:property value="description"/></a> </td>
                </tr> 
              </s:iterator>
                 
              </table>
              </div>
              <div id="seperatorLeft"></div>
              <div class="ui-layout-center" style="width: 75%; float: left">
              <div id="dynamicPane"/>    
              </div>
              
             <!--  <div id="seperatorLeft" style="height:100px;"></div>
              <div class="ui-layout-west" style="overflow-x: hidden; width: 23%; float: left">               
              </div>
              </div>-->
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

