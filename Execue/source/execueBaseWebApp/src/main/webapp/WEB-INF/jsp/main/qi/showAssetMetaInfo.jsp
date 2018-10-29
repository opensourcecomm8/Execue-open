<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>semantifi | meaning based search | business intelligence |
search databases | search data sets | bi tools</title>
</head>
<style>
td{
font-family:Arial, Helvetica, sans-serif;
font-size:12px;
}
</style>
<body>

<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
  <tr>
    <td height="30" valign="bottom"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="29" class="titleWithBackground"><!--s:text  name="publishInfo.publish.heading" /-->
          <s:text name='execue.showAssetMetaInfo.datasetDetails' /><s:property value="applicationId"/></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><div id="greyBorder" style="min-height:340px;height:auto;margin-bottom:20px;">
      <table width="98%" border="0" align="center" cellpadding="10"
			cellspacing="0">
        <tr>
          <td valign="top">
          
          <table width="100%" border="0" cellspacing="0" cellpadding="2">
  <tr>
    <td style="padding-left:4px;"><b><s:text name='execue.showAssetMetaInfo.datasetName' /></b><span style="padding-left:20px"><s:property value="assetName"/></span></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>
    <div style="width:55%;margin-top:0px;">
     <table width="100%" border="0" cellspacing="0" cellpadding="4"  >
      <tr id="tableGridTitle">
                  <td width="47%" height="25" class="heading"><strong><s:text name='execue.showAssetMetaInfo.conceptName' /></strong></td>
                  <td width="53%" class="heading"><strong><s:text name='execue.showAssetMetaInfo.instances' /></strong></td>
                 
                </tr></table>
    </div>
    <div style="width:55%;min-height:100px;height:auto;overflow:auto;">
    <table width="100%" border="0" cellspacing="1" cellpadding="4" id="tableGridMemberInfo" >
      
       <s:iterator value="conceptInstaceMap" status="inst" id="concept">         
      <tr>
        <td width="47%"><s:property value="key.name" /></td>
        <s:if test="value.size>0">
           <td width="53%"><select>     
          <s:iterator value="value" status="inst" id="instance">           
            
            <option value="<s:property value= "%{#instance.displayName}"/>"><s:property value= "%{#instance.displayName}"/></option>
            
          </s:iterator></select>
            </td>
          </s:if>
          <s:else>
          <td width="53%">&nbsp;</td>
          </s:else>
        </tr>
     </s:iterator>       
    </table>
    </div>
    
    
    </td>
  </tr>
  <tr>
    <td><div style="width:55%;margin-top:20px;">
    <table width="100%" border="0" cellspacing="0" cellpadding="4"  >
      <tr class="tableGridTitle">
                  <td width="30%" height="25" class="heading"><strong><s:text name='execue.showAssetMetaInfo.tableName' /></strong></td>
                  <td width="35%" class="heading"><strong><s:text name='execue.showAssetMetaInfo.columns' /></strong></td>
                  <td width="35%" class="heading"><strong><s:text name='execue.showAssetMetaInfo.members' /></strong></td>
                 
                </tr></table></div>
    <div style="width:55%;min-height:100px;height:auto;overflow:auto;margin-top:0px;">
    <table width="100%" border="0" cellspacing="1" cellpadding="4" id="tableGridMemberInfo" >
      
       <s:iterator value="tableInfoMap" status="colum" id="table">         
      <tr>
        <td width="30%"><s:property value="key.name" /></td>
        <s:set name="tableColumns" value="%{value.columns}"/>
        <s:set name="tableMembers" value="%{value.members}"/>
        
           <td width="35%">
            <s:select list="tableColumns" listKey="name" listValue="name"></s:select> 
          	  </td>
           <td width="35%">
           <s:select list="tableMembers" listKey="lookupDescription" listValue="lookupDescription"></s:select>
            </td>
            
        </tr>
     </s:iterator>       
    </table>
    </div>
    
    
    </td>
  </tr>
  
</table>
</td></tr>

</table></td></tr></table></td></tr></table>
</body>
</html>
<script>
$(function(){
$.each($("select"),function(){
if($(this).val()==null){
$(this).hide();
}
});
});
</script>
