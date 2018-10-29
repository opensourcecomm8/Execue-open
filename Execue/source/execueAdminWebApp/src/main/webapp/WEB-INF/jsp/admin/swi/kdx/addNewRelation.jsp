<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="s" uri="/struts-tags"%>

<div class="innerPane2" style="width:430px;height:auto" >
<div id="errorMessage" style="color: red;padding-top:10px;"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage" style="color: green;padding-top:10px;"><s:actionmessage /></div>
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0" style="margin-left:20px;margin-top:15px;">
    <tr>
		<td colspan="4">
		<div id="relationDiv"
			style="width: 565px; height: 240px; border: 1px solid #ccc; padding: 40px;">
		<s:form namespace="/swi" method="POST"
			action="addRelation" id="relationForm" name="relationForm">
			<table width="100%" border="0" align="center" cellpadding="2"
				cellspacing="0">
				         
                <s:if test="relation.id != null">
                 <tr>
                   <td width="20%" class="fieldNames"><s:text name="execue.global.name"/></td>
				   <td width="80%"><s:textfield name="relation.displayName"
							readonly="true" cssClass="textBox" id="rname" /></td>
				 </tr>
               </s:if>
               <s:else>   
				 <tr>
					<td width="20%" class="fieldNames"><s:text
							name="execue.global.name" /><s:text name="execue.global.mandatory"></s:text></td>
					<td width="80%"><s:textfield name="relation.displayName"
							cssClass="textBox" id="relation" /></td>
                </tr>
               </s:else>
                <tr>
                    <td class="fieldNames"><s:text name="execue.global.description"/> </td>
					<td><s:textfield name="relation.description" cssClass="textBox"
						id="description" /></td>
                </tr>
          </table>
         <s:hidden id ="name" name="relation.name"/>
         <s:hidden id="currRelationId" name="relation.id"/>          
      </s:form>
      </div>
     </td>
  </tr>
 
    <tr>        
            <s:if test="relation.id != null"> 
          			<td  width="100%" colspan="2" height="40" align="left" class="buttonsPadding"><span
					id="enableUpdateRelation"><input type="button" class="buttonSize108" alt="Update Relation"
                        onclick="javascript:createRelation()"  value="<s:text name='execue.relation.updateRelation.button' />" /></span> <span
					id="updateProcess" style="display: none"><input type="button" class="buttonLoaderSize108" disabled="disabled" value="<s:text name='execue.relation.updateRelation.button' />" /></span> 
                          
                         <a
			       href="#" style="margin-left: 20px;" id="resetButton"
			         onClick="javascript:resetRelation();"> <s:text name='execue.global.resetButton.name' /> </a> <img
			         id="resetButtonLoader" style="display: none; margin-left: 20px;"
			         src="../images/admin/loadingBlue.gif" />	
		           
		           <a class="ask"
					href="#" style="margin-left: 20px;" id="deleteButton"
					onClick='return deleteRelationHeirarchy(<s:property value="relation.id"/>);'> <s:text name='execue.global.delete' /> </a> <img
					id="deleteButtonLoader" style="display: none; margin-left: 20px;"
					src="../images/admin/loadingBlue.gif" />
		           </td>
					
			</s:if>
            <s:else>     
                  
           	            <td height="40" valign="bottom" colspan="2" class="buttonsPadding" align="left"><span id="enableUpdateRelation">
						<input type="button" class="buttonSize108" alt="Add Relation"
							onclick="javascript:createRelation()"  value="<s:text name='execue.relation.createRelation.button' />" /></span> <span id="updateProcess"
							style="display: none"><input type="button" class="buttonLoaderSize108" disabled="disabled" value="<s:text name='execue.relation.createRelation.button' />" /> </span> <!--span>
                            <input type="button"	class="buttonSize51" onclick="resetRelation();" value="Reset" /></span-->
                            <a
					       href="#" style="margin-left: 20px;" id="resetButton"
					         onClick="javascript:resetRelation();"> <s:text name='execue.global.resetButton.name' /> </a> <img
					         id="resetButtonLoader" style="display: none; margin-left: 20px;"
					         src="../images/admin/loadingBlue.gif" />	
                            </td>

            </s:else>
          </tr>
</table>
</div>
