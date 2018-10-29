<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script>  
$(document).ready(function(){   
/*if($("#errorMessage")){
		 var message=$("#errorMessage").html().toString();	
				var x=message.replace(/<BR>/gi,"**");
				$("#errorMessage").html(x);
		 }*/
     onSelectChange();  
    $("#connectionType").change(onSelectChange);   
	$("#dname").select().focus();     
	
	
	$("#createDataSourceForm").submit(function(){
				$("#updateConnectionButton").hide();
               $("#updateConnectionButtonLoader").show();							   
											   });
});   
function onSelectChange(){ 

    var selected = $("#connectionType option:selected");        
    var output = "";   
	$("#propertiesDiv").hide();
	$("#jndiDiv").hide();
   // if(selected.val() != 0){   
        output = selected.text(); 
     	if(output=="Properties"){
			$("#propertiesDiv").show();	
			//$("#username").show();
			//$("#userPwd").show();		
		}
		if(output=="Jndi"){
			$("#jndiDiv").show();
			//$("#username").hide();
			//$("#userPwd").hide();
				
		}
   // }
     
    
} 


function resetSelect(){

  var selected = document.getElementById("connectionType").value
  javascript:document.createDataSource.reset();
 		if(selected=="JNDI"){
		$("#propertiesDiv").show()
			$("#jndiDiv").hide();
		}    
 }
</script>

<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground">
                <s:if test="dataSource.id != null">
                <span id="newConnectionHeading" ><s:text	name="execue.defineDataSource.edit.pageHeading" /></span>
                </s:if><s:else>
                <span id="editConnectionHeading"  ><s:text	name="execue.defineDataSource.pageHeading" /></span>
                </s:else>
                </td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
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
										src="../images/loadingBlue.gif" width="25" height="25"></div></td>
			</tr>
            </table> 
                
                    
                    </td>
			</tr>
			<tr>
				<td valign="top" background="../images/blueLine.jpg"><img
					src="../images/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				<td width="72%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<!-- fwtable fwsrc="blueBox.png" fwpage="Page 1" fwbase="blueBox.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td height="200" align="center" valign="top">
						<div id="dynamicPane">
						<div class="innerPane" style="width: 99%;min-height:240px;height:auto;padding-bottom:20px;"><s:form
							namespace="/swi" method="POST" id="createDataSourceForm"
							action="createDataSource">
							
							<table width="70%" border="0" align="left" cellpadding="0"
								cellspacing="0" style="margin-left:20px;">
								
								<tr>
									<td height="2" colspan="4"><div id="errorMessages" style="color: red;float:left;"><s:actionerror /><s:fielderror /></div>
							<div id="actionMessages" style="color: green;float:left;"><s:actionmessage /></div></td>
								</tr>
								<tr>
									<td width="15%" height="25" class="fieldNames"><s:text
										name="execue.global.name" /></td>
									<td width="35%" height="22"><s:textfield
										name="dataSource.displayName" cssClass="textBox" id="name"
										tabindex="1" id="dname" /></td>
									<td width="15%" class="fieldNames"><s:text
										name="execue.defineDataSource.connectionModel" /></td>
									<td width="35%"><s:select name="dataSource.connectionType"
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
											<td><s:select name="dataSource.providerType"
												id="providerType" list="assetProviderTypes" tabindex="3" listValue="description"/></td>

										</tr>
									</table>
									</td>
									<td colspan="2" class="fieldNames">
									<div id="jndiDiv">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="30%" height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.jndiConnectionFactory" /></td>
											<td width="70%"><s:textfield
												name="dataSource.jndiConnectionFactory" cssClass="textBox"
												id="jndiConnectionFactory" tabindex="5" /></td>
										</tr>
										<tr>
											<td height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.jndiProviderUrl" /></td>
											<td><s:textfield name="dataSource.jndiProviderUrl"
												cssClass="textBox" id="jndiProviderUrl" tabindex="6" /></td>
										</tr>
										<tr>
											<td height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.jndiName" /></td>
											<td><s:textfield name="dataSource.jndiName"
												cssClass="textBox" id="jndiName" tabindex="7" /></td>
										</tr>
									</table>
									</div>


									<div id="propertiesDiv">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="30%" height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.location" /></td>
											<td width="70%"><s:textfield name="dataSource.location"
												cssClass="textBox" id="location" tabindex="5" /></td>
										</tr>
										<tr>
											<td height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.port" /></td>
											<td><s:textfield name="portStr" cssClass="textBox"
												id="port" tabindex="6" /></td>
										</tr>
										<tr>
											<td height="25" class="fieldNames"><s:text
												name="execue.defineDataSource.schemaName" /></td>
											<td><s:textfield name="dataSource.schemaName"
												cssClass="textBox" id="schemaName" tabindex="7" /></td>
										</tr>
									</table>
									</div>
									</td>
								</tr>



								<!-- 
		<td class="fieldNames"><s:text name="defineDataSource.type"/></td>
		<td><s:select name="dataSource.type" id="select" list="assetTypes"/></td>
	  -->

								<tr id='username'>
									<!-- 
      <td class="fieldNames"><s:text name="defineDataSource.subType"/></td>
	  <td><s:select name="dataSource.subType" id="subType" list="assetSubTypes"/></td>
	  -->
									<td height="25">&nbsp;</td>
									<td>&nbsp;</td>
									<td class="fieldNames"><s:text
										name="execue.defineDataSource.username" /></td>
									<td class="fieldNames"><s:textfield
										name="dataSource.userName" cssClass="textBox" id="userName"
										tabindex="8" /></td>
								</tr>
								<tr id='userPwd'>
									<td height="25">&nbsp;</td>
									<td>&nbsp;</td>
									<td class="fieldNames"><s:text
										name="execue.defineDataSource.password" /></td>
									<td><s:password name="dataSource.password"
										cssClass="textBox" id="password" showPassword="true"
										tabindex="9" /></td>
								</tr>
								<tr>
									<td colspan="4" align="center">&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2" align="left"><s:if
										test="dataSource.id != null">
                                        <span id="updateConnectionButton" > 
										<input type="submit" name="imageField" id="imageField"
										class="singleButton"	value='<s:text name="execue.defineDataSource.updateDataSource.linkName"/>' tabindex="10" /></span><span id="updateConnectionButtonLoader" style="display:none">
                                        <input type="submit" name="imageField" id="imageField"
										class="singleButtonLoader" disabled="disabled"	value='<s:text name="execue.defineDataSource.updateDataSource.linkName"/>' tabindex="10" />
                                        </span>
									</s:if> <s:else>
										<input type="submit" name="imageField" id="imageField"
											class="singleButton" tabindex="10" value='<s:text name="execue.defineDataSource.createDataSource.linkName"/>' />
									</s:else>  <input type="button"	class="singleButton" style="margin-left:10px;"
										onclick="javascript:resetSelect();" tabindex="11" value="Reset" /></td>
								</tr>
							</table>
							 <s:hidden name="dataSource.name" />
					         <s:hidden name="dataSource.id" />
                            <s:hidden name="dataSource.type" />
                           
						</s:form></div>
						</div>
						</td>
					</tr>
					<tr>
						<td colspan="3" valign="top" background="../images/blueLine.jpg"><img
							src="../images/blueLine.jpg" width="10" height="1" /></td>
					</tr>
					<tr>
						<td>&nbsp;
						
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>