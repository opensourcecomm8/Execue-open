<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
#dynamicPane select {
	width: 180px;
}
</style>
<!-- table width="100%" border="0" cellspacing="0" cellpadding="3"
		align="center" ><tr><td align="right"><div id="closeDisclaimerInfo" style="width:40px;height:20px;background-color:#fff;cursor:pointer;color:#0000FF;padding-right:3px;">Close</div></td></tr></table-->
	<table width="40%" border="0" cellspacing="0" cellpadding="3"
		align="center" id="accountTable" style="white-space:nowrap;">
		<!--<tr>
			<td colspan="3" align="left"> 
			<div id="errorMessage"
				   style="color: red; padding: 10px; font-size: 10px;"><s:actionerror /><s:fielderror /></div>
			 
			<div id="actionMessage" style="color: green"><s:actionmessage /></div>
			 </td>
		</tr>-->
		<!-- tr>
			<td width="10%"><s:text name="asset.asset.short.disclaimer" /></td>
			<td width="2%">:</td>
			<td><strong><s:property value="uiAssetDetail.shortDisclaimer"/></strong></td>
		</tr>
		<tr>
			<td colspan="3"><s:text name="asset.asset.extended.disclaimer" /></td>
			
			
		</tr-->
		<tr>
			
			<td width="30%" colspan="3" style="white-space:normal;"><div id="disclaimerDiv" style="padding:3px;margin:5px;margin-top:10px;width:436px;height:265px;overflow:scroll;border:solid 1px #ccc"><s:property value="uiAssetDetail.extendedDisclaimer"/></div></td>
		</tr>
		
	<tr>
			<td>&nbsp;</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	</table>
	
<script>
$(document).ready(function(){
$("#closeUserInfo").click(function(){
$("#userInfoDynamicDivOuter").hide();
});

if($("#disclaimerDiv").text().length==0){
$("#disclaimerDiv").text("No Disclaimer Available");
}
});
</script>
