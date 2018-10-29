<%@ taglib prefix="s" uri="/struts-tags"%>

<style type="text/css">
<!--
td.heading {background-image:url(../images/rowBg.jpg);
font-size:11px;
padding-left:5px;
}
-->
</style>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
  <tr>
    <td height="30" valign="bottom"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="29" class="titleWithBackground"><!--s:text  name="publishInfo.publish.heading" /-->
          <s:text name='execue.showAppInfo.datasetsInformation' /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><div id="greyBorder" style="min-height:430px;height:auto;">
      <table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0">
        <tr>
          <td valign="top">
          
          <table width="100%" cellspacing="0" cellpadding="10" border="0" align="center">
    
        <tr>
          <td valign="top"><table width="100%" cellspacing="0" cellpadding="0" border="0" align="center">
            <tr>
              <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="71%" height="30" style="padding-left:3px;"><span class="descriptionText">
                   <s:if test="assets.size > 0"> 
                  <s:text name='execue.showAppInfo.numberOfDatasetCollection' /> <s:property value="assets.size"/>
                  </s:if>
                  <s:else>
                  <s:text name='execue.showAppInfo.noDatasetCollectionAvailable' />
                  </s:else>
                  </span></td>
                  <td width="29%" align="right">&nbsp;</td>
                </tr>
              </table></td>
            </tr>
            <tr>
              <td>
               <s:if test="assets.size > 0"> 
              
               <div style="width:100%;margin-bottom:10px;">
			          <table width="50%" cellspacing="1" cellpadding="0" border="0" id="tableGridMemberInfo">
                       <tr id="tableGridTitle">
                  <td width="35%" height="25" class="heading"><strong><s:text name='execue.showAppInfo.datasetCollectionName' /></strong></td>
                  <td width="50%" class="heading"><strong><s:text name='execue.showAppInfo.datasetCollectionDesc' /></strong></td>
                  <td width="15%" class="heading"><strong><s:text name='execue.showAppInfo.status' /></strong></td>
                </tr>
			          <s:iterator value="assets">
				    <tr>
					<td  ><A href="../qi/showAssetMetaInfo.action?assetId=<s:property value='id'/>"><s:property value="name" /></A></td>
					<td  >
					    <s:property value="description" />
					   </td>
					<td  ><s:property value="status" /></td>
		    	  </tr>
				</s:iterator>
                
		       </table>
		       </div>
		        </s:if>
		    
   </td>
  </tr>
</table>
</td></tr>

</table></td></tr></table></td></tr></table>
