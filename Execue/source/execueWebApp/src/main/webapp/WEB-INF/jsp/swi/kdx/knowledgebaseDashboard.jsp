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
          <s:text name='execue.knowledgeBaseDashboard.title' /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><div id="greyBorder" style="height:400px;">
      <table width="96%" border="0" align="center" cellpadding="5"
			cellspacing="0">
        <tr>
          <td valign="top"><table width="100%" cellspacing="0" cellpadding="0" border="0" align="center">
            <tr>
              <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                
                  <td width="100%" align="left" style="padding-left:3px;" height="25"><a class="arrowBg" href="showBusinessTerms.action?domain.id=1"><s:text name='execue.knowledgeBaseDashboard.createEntityLink' /></a></td>
                  </tr>
                   <tr>
                  <td width="71%" height="20" style="padding-left:3px;"><span class="descriptionText">
                  <s:if test="concepts.size > 0"> 
                  <s:text name='execue.knowledgeBaseDashboard.numberOfEntities' /> <s:property value="concepts.size"/>
                  </s:if>
		       <s:else>
              <s:text name='execue.knowledgeBaseDashboard.noEntitiesFound' />
               </s:else> 
                  </span></td>
                  
                  </tr>
                </table></td>
              </tr>
               <s:if test="concepts.size > 0"> 
            <tr>
              <td>
              
              <table width="70%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="71%" height="22"  style="padding-left:3px;"><s:text name='execue.knowledgeBaseDashboard.mostCommonEntities' /></td>
                  <td width="29%" align="right">&nbsp;</td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                    
                    <table width="70%" cellspacing="0" cellpadding="0" border="0" >
                <tr id="tableGridTitle">
                  <td width="33%" height="25" class="heading"><strong><s:text name='execue.knowledgeBaseDashboard.conceptName' /></strong></td>
                  <td width="67%" class="heading"><strong> <s:text name='execue.knowledgeBaseDashboard.description' /></strong></td>
                  </tr>
                 </table></td>
                  </tr>
                  <tr>
                    <td><div style="width:70%;height:290px;overflow-y:auto;">
			          <table width="100%" cellspacing="1" cellpadding="0" border="0" id="tableGridMemberInfo">
			          <s:iterator value="concepts">
				    <tr>
					<td height="25" width="33%"><s:property value="name" /></td>
					<td height="25" width="67%"><s:property value="description" /></td>
				  </tr>
				</s:iterator>
		       </table>
		       </div></td>
                  </tr>
                </table>

                 </td></tr>
                 
               
		       </s:if>
		      
        </table>
   </td>
  </tr>
</table></div></td></tr></table>
