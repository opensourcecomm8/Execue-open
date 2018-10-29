<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script src="../js/admin/datasource.js" ></script>
<script src="../js/admin/jquery.execue.helpMessage.js"></script>
<script>  
$(document).ready(function(){ 
     onSelectChange();  
    $("#connectionType").change(onSelectChange);   
	$("#dname").select().focus();

	// Help Link registration(s)
	$("#locationHelpLink").helpMessage({messageKey:"execue.connect.help.localtion"});
	$("#schemaNameHelpLink").helpMessage({messageKey:"execue.connect.help.schema.name"});
	$("#ownerHelpLink").helpMessage({messageKey:"execue.connect.help.owner"});
});   

</script>
		<table width="98%" height="*" border="0" align="center"
			cellpadding="0" cellspacing="0">
			<tr>
				<td valign="top" >
                 <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"> 
			   <tr>
				<td valign="top" width="70%" class="descriptionText"><s:text	name="execue.defineDatasource.description" /></td>
			</tr>
            <tr>  
                <td align="left" height="30" width="30%"><div id="addNewUserLink"><a
							href="../swi/showAssetsDashboard.action" class="arrowBg"
								tabindex="-1"><s:text
							name="execue.showDataSources.showDataSources.linkName" /></a></div>
				<div id="loadingAddNewUserLink" style="display: none;"><img
					src="../images/admin/loadingBlue.gif" width="25" height="25"></div></td>
			</tr>
            </table> 
            </td>
			</tr>
			<tr>
				<td valign="top" background="../images/admin/blueLine.jpg"><img
					src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				<td width="72%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<!-- fwtable fwsrc="blueBox.png" fwpage="Page 1" fwbase="blueBox.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->
					
					<tr>
						<td height="200" align="center" valign="top">
						<div id="deleteMessagesPane" style="color: green;float:center;display:none"></div>
						<div id="deleteErrorMessagesPane" style="color: red;float:center;display:none"></div>
						<div id="dynamicPaneDatasource">
						<div class="innerPane" style="width: 99%;height:auto;padding-bottom:20px;border:none;"><s:form
							 method="POST" id="createDataSourceForm">
							<div id="errorMessages" style="color: red;float:left;padding-left:20px;width:100%;text-align:left;"><s:actionerror /><s:fielderror /></div>
							<div id="actionMessages" style="color: green"><s:actionmessage /></div>
							<table width="70%" border="0" align="left" cellpadding="0"
								cellspacing="0" style="margin-left:20px;">
								
								<tr>
									<td height="2" colspan="4" height="20">&nbsp;</td>
								</tr>
								<tr>
									<td width="15%" height="25" class="fieldNames"><s:text
										name="execue.global.name" /><s:text name="execue.global.mandatory"></s:text></td>
									<td width="35%" height="22"><s:textfield
										name="dataSource.displayName" cssClass="textBox" id="name"
										tabindex="1" id="dname" /></td>
									<td width="20%" class="fieldNames"><s:text
										name="execue.defineDataSource.connectionModel" /></td>
									<td width="30%"><s:select name="dataSource.connectionType"
										id="connectionType" list="connectionTypes" tabindex="4" listValue="description" /></td>
								</tr>
								<tr>
									<td colspan="2" valign="top">
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td width="30%" height="25" class="fieldNames"><s:text
												name="execue.global.description" /></td>
											<td width="70%"><s:textfield
												name="dataSource.description" cssClass="textBox"
												id="description" tabindex="2" /></td>
										</tr>
										<tr>
											<td height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.providerType" /></td>
											<td><s:select name="dataSource.providerType" onchange="javascript:showOwner();"
												id="providerType" list="assetProviderTypes" tabindex="3" listValue="description"/></td>

										</tr>
									</table>
									</td>
									<td colspan="2" class="fieldNames">
									<div id="jndiDiv">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="40%" height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.jndiConnectionFactory" /><s:text name="execue.global.mandatory"></s:text></td>
											<td width="60%"><s:textfield
												name="dataSource.jndiConnectionFactory" cssClass="textBox"
												id="jndiConnectionFactory" tabindex="5" /></td>
										</tr>
										<tr>
											<td height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.jndiProviderUrl" /><s:text name="execue.global.mandatory"></s:text></td>
											<td><s:textfield name="dataSource.jndiProviderUrl"
												cssClass="textBox" id="jndiProviderUrl" tabindex="6" /></td>
										</tr>
										<tr>
											<td height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.jndiName" /><s:text name="execue.global.mandatory"></s:text></td>
											<td><s:textfield name="dataSource.jndiName"
												cssClass="textBox" id="jndiName" tabindex="7" /></td>
										</tr>
									</table>
									</div>


									<div id="propertiesDiv">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="40%" height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.location" /><s:text name="execue.global.mandatory"></s:text>
											<a href="#" id="locationHelpLink" title="Help" alt="Help" tabindex="-1">?</a>
											</td>
											<td width="60%"><s:textfield name="dataSource.location"
												cssClass="textBox" id="location" tabindex="5" /></td>
										</tr>
										<tr>
											<td height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.port" /><s:text name="execue.global.mandatory"></s:text></td>
											<td><s:textfield name="portStr" cssClass="textBox"
												id="port" tabindex="6" /></td>
										</tr>
										<tr>
											<td height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.schemaName" /><s:text name="execue.global.mandatory"></s:text>
											<a href="#" id="schemaNameHelpLink" title="Help" alt="Help" tabindex="-1">?</a>
											</td>
											<td><s:textfield name="dataSource.schemaName"
												cssClass="textBox" id="schemaName" tabindex="7" /></td>
										</tr>
										<tr style="display:none" id="dataSourceOwnerId">
											<td height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.owner" /><s:text name="execue.global.mandatory"/>
											<a href="#" id="ownerHelpLink" title="Help" alt="Help" tabindex="-1">?</a>
											</td>
											<td><s:textfield name="dataSource.owner"
												cssClass="textBox" id="ownerId" tabindex="7" /></td>
										</tr>	
									</table>
									</div>
									</td>
								</tr>
								<tr id='username'>									
									<td height="25">&nbsp;</td>
									<td>&nbsp;</td>
									<td class="fieldNames"><s:text
										name="execue.defineDataSource.username" /><s:text name="execue.global.mandatory"/></td>
									<td class="fieldNames"><s:textfield
										name="dataSource.userName" cssClass="textBox" id="userName"
										tabindex="8" /></td>
								</tr>
								<tr id='userPwd'>
									<td height="25">&nbsp;</td>
									<td>&nbsp;</td>
									<td class="fieldNames"><s:text
										name="execue.defineDataSource.password" /><s:text name="execue.global.mandatory"/></td>
									<td><s:password name="dataSource.password"
										cssClass="textBox" id="password" showPassword="true"
										tabindex="9" /></td>
								</tr>
								<tr>
									<td colspan="4" align="center">&nbsp;</td>
								</tr>
								<tr>
									<td colspan="4" align="left">
									<s:if
										test="dataSource.id != null">
                                        <span id="updateConnectionButton" > 
										<input type="button" name="imageField" id="createConnectionButton"  class="singleButton"	value='<s:text name="execue.defineDataSource.updateDataSource.linkName"/>' tabindex="10" /></span><span id="updateConnectionButtonLoader" style="display:none"><input type="button" name="imageField" id="imageField" disabled="disabled" class="singleButtonLoader"	value='<s:text name="execue.defineDataSource.updateDataSource.linkName"/>' tabindex="10" /> </span>
									</s:if> 
									<s:else>
                                      <span id="createConnectionButton">
						              <input type="button" name="imageField" id="createConnectionButton"  class="singleButton" tabindex="10" value='<s:text name="execue.defineDataSource.createDataSource.linkName"/>' /> </span>                        
                                      <span id="createConnectionButtonLoader" style="display:none">   
                                      <input type="button" name="imageField" id="imageField" disabled="disabled"  class="singleButtonLoader" tabindex="10" value='<s:text name="execue.defineDataSource.createDataSource.linkName"/>' /></span>                        
									</s:else> 
									  <s:if test="dataSource.type.value=='REGULAR' && dataSource.id>0">								
									        <input type="button"	class="singleButton" style="margin-left:10px;"
										    onclick="javascript:deleteDatasource(<s:property value='dataSource.name'/>,'${pageContext.request.contextPath}');" tabindex="11" value="Delete" />
									   </s:if>	
									  <span class="rightButton">									   
									    <input type="reset"	class="singleButton"  id="imageField2"
				                          value="<s:text name='execue.editApp.clearAllFieldsButton.name' />"
				                          onclick="document.createDataSourceForm.reset();" /> 
				                        </span>				                          
				                     </td>
								</tr>
							</table>
						    <s:hidden name="dataSource.name" />
					        <s:hidden name="dataSource.id" />
                            <s:hidden name="dataSource.type" />                           
						</s:form></div>
						</div>
						</td>
					</tr>
					
					
				</table>
				</td>
			</tr>
		</table>
	<script>
	$(function(){
	  $("#createConnectionButton").click(function(){
	     createConnection();
	  });	  
	    var selectedProviderType= $("#providerType option:selected").val(); 		 
		  if(selectedProviderType=="Teradata" || selectedProviderType=="SAS_SHARENET" || selectedProviderType=="SAS_WORKSPACE"){
		    $("#dataSourceOwnerId").show();
		  }  
		  $("#providerType").bind("change",function(){
				var opt=$(this).val();			
				if(opt=="Teradata" || opt=="SAS_SHARENET" || opt=="SAS_WORKSPACE" || opt=="MSSql" || opt=="DB2"){
					$("#dataSourceOwnerId").show();
				}else{
					$("#dataSourceOwnerId").hide();
					$("#ownerId").val(null);
				}
			});
	});
	</script>	