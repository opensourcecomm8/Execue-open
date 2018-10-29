<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div class="innerPane2" style="width: 430px; height: auto">
<table width="97%" border="0" align="center" cellpadding="0"
	cellspacing="3">
	<tr>
		<td width="80%" height="30" class="fieldNames"><strong><s:text name="execue.asset.operation.status.message"></s:text> </strong></td>
	</tr>
	<tr>
		<td colspan="2">
		<TABLE BORDER=0>
			<td class="fieldNames" valign="top"></td>			
			<tr>
				<td colspan='2'>

				<table width="100%">
					<s:iterator value="underProcessAssetMap">
						<tr>
							<td width="48%"><strong><s:property value="key" />
							:</strong></td>
							<td width="46%"></td>
						</tr>						
						<s:iterator value="value" status="index">
							<tr>
								<td></td>
								<td><strong> <s:property value="%{#index.index+1}"/> .</strong> <s:property value="displayName" /></td>
								<td width="6%"></td>
							</tr>
						</s:iterator>
					</s:iterator>
				</table>
				</td>
			</tr>

		</TABLE>
		</td>
	</tr>
</table>
</div>
<script>


</script>