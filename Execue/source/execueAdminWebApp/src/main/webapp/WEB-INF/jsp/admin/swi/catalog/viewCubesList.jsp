<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
#tableGridMemberInfo td{
padding-top:0px;

}
</style>
         <s:if test="cubes.size>0">				     
				<table width="100%" cellspacing="1" cellpadding="0" border="0" id="tableGridMemberInfo">                    
                    <tbody><tr id="tableGridTitle">
						<td width="11%" height="28" bgcolor="#EEEEEE" align="left"><strong><s:text name="execue.catalog.name"></s:text> </strong></td>						
						<td width="22%" bgcolor="#EEEEEE" align="left"><strong><s:text name="execue.catalog.description"></s:text></strong></td>
						<td width="15%" bgcolor="#EEEEEE" align="left"><strong><s:text name="execue.catalog.lastModifiedDate"></s:text> </strong></td>
						<td width="15%" bgcolor="#EEEEEE" align="left"><strong><s:text name="execue.catalog.source"></s:text> </strong></td>
                        <td width="7%" bgcolor="#EEEEEE" align="left"><strong><s:text name="execue.catalog.edit"></s:text></strong></td>
					</tr>	
					 <s:iterator value="cubes">			   
                     		<tr>
								<td width="11%"><s:property value="name"/></td>								
								<td width="22%"><s:property value="description"/></td>
								<td width="15%"><s:property value="lastModifiedDate"/></td>
								<td width="15%">&nbsp;	<s:property value="parentAssetName"/></td>
								<td width="7%" align="center">
								<div id='showEdit1Link_<s:property value="id"/>'><a id="1" class="links" href="javascript:editCube('<s:property value="id"/>');"><img height="16" border="0" title="Edit Record" alt="Edit Record" src="../images/admin/editIcon.gif"></a></div>
								<div style="display: none;" id='loadingShowEdit1Link_<s:property value="id"/>'><img height="16" src="../images/admin/loadingWhite.gif"></div>
								</td>	
						 </tr>  
						  </s:iterator>                                
					
				  </tbody></table>
				   
				   </s:if>
				   <s:else>
				   <table>
				       <tr>
				         
						  <td width="11%"><s:text name="execue.cube.empty.list"></s:text></td>
						</tr>
						</table>
				   </s:else>   
	<script>
	function editCube(assetId){	
	    $("#showEdit1Link_"+assetId).hide();
	    $("#loadingShowEdit1Link_"+assetId).show();	
		window.location="editCube.action?existingAssetId="+assetId;
	}
	</script>