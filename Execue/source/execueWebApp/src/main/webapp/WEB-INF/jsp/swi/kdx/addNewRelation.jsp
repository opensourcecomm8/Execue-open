<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="s" uri="/struts-tags"%>

<div class="innerPane2" style="width:430px;height:auto" >
<div id="errorMessage" style="color: red;padding-top:10px;"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage" style="color: green;padding-top:10px;"><s:actionmessage /></div>
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0" style="margin-left:20px;margin-top:15px;">
 
  
  <tr>
		<td colspan="2">
		<s:form namespace="/swi" method="POST"
			action="addRelation" id="relationForm" name="relationForm">
			<table width="100%" border="0" align="center" cellpadding="2"
				cellspacing="0">
			 <tr>
          <td colspan="2">
          <div id="message" STYLE="color: red" align="left"></div>
          </td>
        </tr>
         <s:if test="mode == 'add'">
        <tr>
						<td width="32%" class="fieldNames"><s:text
							name="execue.global.name" /><s:text name="execue.global.mandatory"></s:text></td>
						<td width="68%"><s:textfield name="relation.displayName"
							cssClass="textBox" id="relation" /></td>
          </tr>
          </s:if>
          <s:else>
          <tr>
          <td width="32%" class="fieldNames"><s:text name="execue.global.name"/></td>
						<td width="68%"><s:textfield name="relation.displayName"
							readonly="true" cssClass="textBoxDisabled" id="rname" /></td>
          </s:else>	
        
        <tr>
          <td class="fieldNames"><s:text name="execue.global.description"/> </td>
					<td><s:textfield name="relation.description" cssClass="textBox"
						id="description" /></td>
          </tr>
          <tr>
         
           <s:if test="mode == 'add'">
          
             <s:hidden id="mode" name="mode" value="add" />
						<td height="40" colspan="2" class="buttonsPadding" align="left"><span id="enableUpdateRelation">
						<input type="button" class="buttonSize108" alt="Add Relation"
							onclick="javascript:createRelation()"  value="<s:text name='execue.relation.createRelation.button' />" /></span> <span id="updateProcess"
							style="display: none"><input type="button" class="buttonLoaderSize108" disabled="disabled" value="<s:text name='execue.relation.createRelation.button' />" /> </span> <!--span>
                            <input type="button"	class="buttonSize51" onclick="resetRelation();" value="Reset" /></span-->
                            
                             <span class="rightButton"><input type="button"	class="buttonSize90"  
				value="<s:text name='execue.editApp.clearAllFieldsButton.name' />"
				onclick="resetRelation();" /> </span>
                            
                            </td>
					</s:if>
            <s:else>
             
             <s:hidden name="mode" value="update" />
						<s:hidden id="currRelationId" name="relation.id"
							value="%{relation.id}" />
						<td width="100%" colspan="2" height="40" align="left" class="buttonsPadding"><span
							id="enableUpdateRelation"><input type="button" class="buttonSize108" alt="Update Relation"
	onclick="javascript:createRelation()"  value="<s:text name='execue.relation.updateRelation.button' />" /></span> <span
							id="updateProcess" style="display: none"><input type="button" class="buttonLoaderSize108" disabled="disabled" value="<s:text name='execue.relation.updateRelation.button' />" /></span> 
                            
                            <span class="rightButton"><input type="button"	class="buttonSize90"  id="resetRelationId"
				value="<s:text name='execue.editApp.clearAllFieldsButton.name' />"
				onclick="resetRelation();" /> </span>
            </s:else>
          </tr>
      </table>
       <s:hidden id ="name" name="relation.name"/>
      </s:form>
</td>
  </tr>

</table>
</div>
