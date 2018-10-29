<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
#dynamicPane select {
    width: 200px;
}
</style><div id="dynamicPane" >
<table width="100%" border="0" align="center" cellpadding="0"
    cellspacing="0">

    <tr>
        <td>
        <table width="100%" border="0" align="center" cellpadding="0"
            cellspacing="0">
            <tr>
                <td colspan="3" valign="top" class="descriptionText"><s:text name="execue.assetAnalysisReport.main.join.description"></s:text> </td>
            </tr>
            <tr>
				<td valign="top" class="descriptionText"><a href='showJoins.action?assetId=<s:property value="selectedAssetId"/>&sourceURL=showAssetAnalysisReport.action?selectedAssetId=<s:property value="selectedAssetId"/>'><s:text name="execue.assetAnalysisReport.main.link.join"></s:text> </a></td>
			</tr>
            <tr>
                <td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img
                    src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
            </tr>
            <tr>

                <td width="98%" valign="top">
                
                 <div id="unMappedColumnDiv" style="width:705px;height:310px;overflow:auto;margin:20px;border:solid 1px #ccc;padding-top:10px;"> 
                <table border="0" cellpadding="0" cellspacing="0">
                    <!-- fwtable fwsrc="blueBox.png" fwpage="Page 1" fwbase="blueBox.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->
				<s:iterator value="assetAnalysisReportInfo.assetAnalysisTablesInfo" id="assetAnalysisTablesList" >
                    <tr>
                        <td align="left" valign="top" style="padding-bottom:5px;">
                      
                       <div style="width:600px;"><div class="tableName" style="width:20px;float:left;" > </div><div  style="width:auto;float:left;padding-left:10px;"> <s:property value="operationTable.name" /></div>
                                            
                         </div>
                         
                        </td>
                    </tr>
					</s:iterator>
			
                </table>
                </div>
                
                </td>
            </tr>
           
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>

        </table>
        </td>
    </tr>

</table></div>