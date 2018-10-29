<%@ taglib prefix="s" uri="/struts-tags"%>

<s:iterator value="applications" status="list">
	<s:if test="#list.index==0">
		<option selected="selected" modelId="<s:property value='modelId'/>"
			applicationUrl="<s:property value='applicationURL'/>" value="<s:property value='applicationId'/>"><s:property
			value="applicationName" /></option>
	</s:if>
	<s:else>
		<option modelId="<s:property value='modelId'/>"
			applicationUrl="<s:property value='applicationURL'/>" value="<s:property value='applicationId'/>"><s:property
			value="applicationName" /></option>
	</s:else>
</s:iterator>

<script>
	$(document).ready(function() {
		$('#modelId').val($("#selectApp option:selected").attr("modelId"));
		$('#applicationId').val($("#selectApp").val());
	});
</script>
