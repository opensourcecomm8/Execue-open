<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="50" align="center" cellpadding="10" cellspacing="0">
	<tr>
		<td>
		<div id="greyBorder1">
		<table width="50%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText" height="70"><s:text
					name="execue.indexFormManagement.description" /></td>
			</tr>
			<tr>
				<td valign="top">

				<table width="200" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>


						<div id="container" style="width: 700px; height: 205px;">

						<div class="ui-layout-center"
							style="width: 750px; overflow: hidden;">
						<div id="dynamicPane">

						<div id="errorMessage"><s:fielderror /> <s:actionmessage />
						<s:actionerror /></div>

						<div class="innerPane" style="width: 99%"><s:form
							id="indexFormManagementForm" namespace="/swi"
							action="scheduleIndexFormManagementJob" method="post"
							enctype="multipart/form-data">
							<table width="80%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td valign="top" align="center">
									<table width="10%" border="0" align="center" cellpadding="3"
										cellspacing="0">
										<tr>
											<td ></td>

										</tr>										
									</table>
								<tr>
									<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2" align="center" height="40" valign="bottom"><span
										class="fieldNames"><input type="submit" class="buttonSize108"
										name="imageField" id="imageField"
										value="<s:text name='execue.global.scheduleJob' />" />  <input type="button" class="buttonSize51"
										name="imageField2" id="imageField2"
										value="<s:text name='execue.global.cancel' />" /> </span></td>
								</tr>
							</table>

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
		</div>
		</td>
	</tr>
</table>