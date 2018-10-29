<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script>
	function showJobs(){
		var jobType = $("#jobType").val();		
		$.get("showJobsByType.action",{selectedJobType:jobType}, function(data){
			//var data=eval(data);	
			$(".TabbedPanelsContentGroup").html(""+data);
		});
	}
	
</script>
<style>
select{
width:300px;	
}
</style>

<div id="outerPane">
<table width="950" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="50" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name="execue.showRequestStatus.pageHeading" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder" style="height: 405px;">
		<table width="96%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText"><s:text
					name="execue.showRequestStatus.description" /></td>
			</tr>
			<tr>
			   
				<td valign="top" align="center"><s:select list="jobTypes"
					id="jobType" name="jobType" value="%{selectedJobType}"
					onchange="javascript:showJobs();"></s:select></td>
			</tr>
			
			
			<tr>
				<td width="72%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">

					
					<tr>
						<td height="300" align="center" valign="top">
						<div id="dynamicPane">

						<table width="100%" border="0" cellspacing="0" cellpadding="0">

							<tr>
								<td>
								<div class="TabbedPanelsContentGroup"><jsp:include
									page="jobRequestStatusInfo.jsp" flush="true" /></div>
								</td>
							</tr>
						</table>
						</div>
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>
</div>
