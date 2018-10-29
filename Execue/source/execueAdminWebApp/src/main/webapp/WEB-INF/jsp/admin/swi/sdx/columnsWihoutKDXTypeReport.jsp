<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">

$(document).ready(function() {
$("div.tableName").click(function(){
	// alert($(this).find("img").attr("src"));
	 if($(this).find("img").attr("src")=="../images/admin/bullet_toggle_plus.png"){
	    $(this).next().next().next().css("display","block");
	    $(this).find("img").attr("src","../images/admin/bullet_toggle_minus.png")
	 }else{
	    $(this).next().next().next().css("display","none");
	    $(this).find("img").attr("src","../images/admin/bullet_toggle_plus.png")
	 }
 });  
});


</script>
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
                <td colspan="3" valign="top" class="descriptionText"><s:text name="execue.assetAnalysisReport.main.kdxType.description"></s:text> </td>
            </tr>
            <tr>
				<td valign="top" class="descriptionText"><a href='showAsset.action?assetId=<s:property value="selectedAssetId"/>&sourceURL=showAssetAnalysisReport.action?selectedAssetId=<s:property value="selectedAssetId"/>'><s:text name="execue.assetAnalysisReport.main.link.metadata"></s:text> </a></td>
			</tr>

            <tr>
                <td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img
                    src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
            </tr>
            <tr>

                <td width="98%" valign="top">
                
                 <div id="columnsWithoutKDXTypeDiv" style="width:705px;height:310px;overflow:auto;margin:20px;border:solid 1px #ccc"> 
                <table border="0" cellpadding="0" cellspacing="0">                    
					<s:iterator value="assetAnalysisReportInfo.assetAnalysisTablesInfo" id="assetAnalysisTablesList" >
                    <tr>
                        <td align="left" valign="top" style="padding-bottom:5px;">                      
                        <div style="width:600px;">
                         <div class="tableName" style="width:20px;float:left;" > 
                           <img src="../images/admin/bullet_toggle_plus.png" />
                         </div>
                       <div  style="width:auto;float:left;"> 
                         <s:property value="operationTable.name" />
                       </div>                       
                       <br>   
                       <s:if test='assetAnalysisThresholdType.value =="UnderThreshold"'> 	                                                                        
			                  <div class="colBlock" style="padding-left:15px;display:none;float:left;">
				                    <s:iterator value="operationColumns">
				                      	<div class="colName" > <s:property value="name" /></div>
				                    </s:iterator>
			                 </div>                         
	                     </s:if> 
	                      <s:else >
	                         <span style="padding-left:20px;"><s:property value="thresholdMessage" /></span>
	                      </s:else>
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