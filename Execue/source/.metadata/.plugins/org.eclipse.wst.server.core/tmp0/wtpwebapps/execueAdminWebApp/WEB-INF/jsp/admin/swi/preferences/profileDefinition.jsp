<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<div class="innerPane2" style="width: 99%;">
<div id="errorMessage" style="color: red"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage" style="color: red"><s:actionmessage /></div>
<s:form id="profileDefinition">
<table width="90%" border="0" align="center" cellpadding="5" cellspacing="0">
  <tr>
    <td width="97%" height="40" class="innerPaneHeading"><strong><s:text name="execue.profiles.workspace.heading"/></strong></td>
    <td width="3%" align="center" class="fieldNames"></td>
  </tr>
  <tr>
    <td colspan="2"><div id="errorMessage" STYLE="color: red" align="left"></div></td>
  </tr>
  <tr>
    <td colspan="2" class="fieldNames"><strong><s:text name="execue.profiles.base.name"/> :</strong> 
    <s:if test="profileType == 'CONCEPT'">
    Model Profile <!-- This is BAD, needs to be picked up from configuration -->
    </s:if>
    <s:else>
    <s:property value="instanceProfile.concept.displayName"/>
    </s:else>
    </td>
  </tr>
  <tr>
    <td colspan="2">
    <div style="height: 216px; width: 98%; border: #D3D3D3 1px solid; padding: 3px;">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="27%">
        <div class="tableList" style="height: 210px; width: 250px;margin-bottom:6px;margin-right:3px;">
        <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <s:if test="profileType == 'CONCEPT'">
          <s:iterator value="conceptProfileConcepts" status="even_odd" id="profileConcept">
          <tr>
            <td width="15%" class="dotBullet"><label> <input type="checkbox" name="conceptProfileConcepts[<s:property value="#even_odd.index" />].id" id="checkbox" value="<s:property value="id" />" checked/>
            </label></td>
            <td width="85%" class="fieldNames"><s:property value="displayName"/></td>
          </tr>
          </s:iterator>
          </s:if>
          <s:else>
          <s:iterator value="instanceProfileInstances" status="even_odd" id="profileInstance">
          <tr>
            <td width="15%" class="dotBullet"><label> <input type="checkbox" name="instanceProfileInstances[<s:property value="#even_odd.index" />].id" id="checkbox" value="<s:property value="id" />" checked/>
            </label></td>
            <td width="85%" class="fieldNames"><s:property value="displayName"/></td>
          </tr>
          </s:iterator>
          </s:else>
          
          <s:if test="profileType == 'CONCEPT'">
          <s:iterator value="domainProfileConcepts" status="even_odd" id="concept">
          <tr>
            <td width="15%" class="dotBullet"><label> <input type="checkbox" name="newConceptProfileConcepts[<s:property value="#even_odd.index" />].id" value="<s:property value="id" />" id="checkbox"/>
            </label></td>
            <td width="85%" class="fieldNames"><s:property value="displayName"/></td>
          </tr>
          </s:iterator>
          </s:if>
          <s:else>
          <s:iterator value="conceptInstances" status="even_odd" id="instance">
          <tr>
            <td width="15%" class="dotBullet"><label> <input type="checkbox" name="newInstanceProfileInstances[<s:property value="#even_odd.index" />].id" value="<s:property value="id" />" id="checkbox"/>
            </label></td>
            <td width="85%" class="fieldNames"><s:property value="displayName"/></td>
          </tr>
          </s:iterator>
          </s:else>
        </table>
        </div>
        </td>
        <td width="73%" valign="top">
        <table width="90%" border="0" cellspacing="0" cellpadding="5" align="center">
          <tr>
            <td class="fieldNames"><s:text name="execue.profiles.profile.name"/><s:text name="execue.global.mandatory"></s:text></td>
          </tr>
          <s:if test="profileType == 'CONCEPT'">
          <tr>
            <td>
            <s:textfield name="conceptProfile.displayName" cssClass="textBox" id="displayName" maxlength="255"/>
             <a href="#" id="helpLink" title="Help" alt="Help">?</a>
            </td>
          </tr>
          </s:if>
          <s:else>
          <tr>
            <td>
            <s:textfield name="instanceProfile.displayName" cssClass="textBox" id="displayName" maxlength="255"/>
             <a href="#" id="helpLink" title="Help" alt="Help">?</a>
            </td>
          </tr>
          </s:else>
          <tr>
            <td class="fieldNames"><s:text name="execue.profiles.profile.description"/></td>
          </tr>
          <s:if test="profileType == 'CONCEPT'">
          <tr>
            <td>
            <s:textfield name="conceptProfile.description" cssClass="textBox" id="description" maxlength="255"/>
            </td>
          </tr>
          </s:if>
          <s:else>
          <tr>
            <td>
            <s:textfield name="instanceProfile.description" cssClass="textBox" id="description" maxlength="255"/>
            </td>
          </tr>
          </s:else>
          <tr>
            <td class="fieldNames">
            <s:if test="profileType == 'CONCEPT'">
	            <s:if test="conceptProfile.enabled == true">
	               Enabled : <input type="checkbox" name="conceptProfile.enabled" value="<s:property value='conceptProfile.enabled'/>" checked="checked" id="chkbox"/>
	            </s:if>
	            <s:else>
	               Enabled : <input type="checkbox" name="conceptProfile.enabled" value="<s:property value='conceptProfile.enabled'/>" id="chkbox"/>
	            </s:else>
            </s:if>
            <s:else>
               	<s:if test="instanceProfile.enabled == true">
	               Enabled : <input type="checkbox"  name="instanceProfile.enabled" value="<s:property value='instanceProfile.enabled'/>" checked="checked" id="chkbox"/>
	            </s:if>
	            <s:else>
	              Enabled : <input type="checkbox"  name="instanceProfile.enabled" value="<s:property value='instanceProfile.enabled'/>" id="chkbox"/>
	            </s:else>
            </s:else>
            </td>
          </tr>
          <tr>
          <s:if test="profileType == 'CONCEPT'">
            <s:if test="conceptProfile.name != null">
            <td>
            	<div id="enableupdateProfile">
                	<input type="button"	class="buttonSize108"  id="imageField" value="<s:text name='execue.profileDefinitions.updateProfile.button' />" onclick="updateProfile();"/></div>
            	 <div id="updateProcess" style="display: none;">
					<input type="button"	class="buttonLoaderSize108" disabled="disabled" value="<s:text name='execue.profileDefinitions.updateProfile.button' />" ></div>
		 	</td>
            </s:if>
            <s:else>
            <td>
            	<div id="enableupdateProfile">
                	<input type="button"	class="buttonSize108" id="imageField" value="<s:text name='execue.profileDefinitions.createProfile.button' />" onclick="updateProfile();"/></div>
             	<div id="updateProcess" style="display: none;">
					<input type="button" disabled="disabled"	class="buttonLoaderSize108" value="<s:text name='execue.profileDefinitions.createProfile.button' />" ></div>
			</td>
            </s:else>
          </s:if>
          <s:else>
            <s:if test="instanceProfile.name != null">
            <td>
            	<div id="enableupdateProfile">
                	<input type="button"	class="buttonSize108" id="imageField" value="<s:text name='execue.profileDefinitions.updateProfile.button' />" onclick="updateProfile();"/></div>
            	 <div id="updateProcess" style="display: none;">
					<input type="button" disabled="disabled"	class="buttonLoaderSize108" value="<s:text name='execue.profileDefinitions.updateProfile.button' />" ></div>
			</td>
            </s:if>
            <s:else>
            <td>
            	<div id="enableupdateProfile">
                	<input type="button"	class="buttonSize108" id="imageField"  value="<s:text name='execue.profileDefinitions.createProfile.button' />" onclick="updateProfile();"/></div>
             	<div id="updateProcess" style="display: none;">
					<input type="button" disabled="disabled" class="buttonLoaderSize108" value="<s:text name='execue.profileDefinitions.createProfile.button' />" ></div>
             </td>
            </s:else>
          </s:else>
          </tr>
        </table>
        </td>
      </tr>
    </table>
    </div>
    </td>
  </tr>
</table>

<s:hidden name="profileType"/>
<s:hidden name="hybridProfile"/>
<s:hidden name="modelId"/>

<s:if test="profileType == 'CONCEPT_LOOKUP_INSTANCE'">
  <s:hidden name="instanceProfile.concept.id"/>
  <s:hidden name="instanceProfile.id"/>
  <s:hidden name="instanceProfile.name"/>
</s:if>
<s:else>
  <s:hidden name="conceptProfile.id"/>
  <s:hidden name="conceptProfile.name"/>
</s:else>

</s:form>
</div>
<script>
  $(document).ready(function() {
   $("#helpLink").helpMessage({messageKey:"execue.profile.help.name"}); 
   });
$("#chkbox").click(function(){
   $(this).attr("value",$(this).is(':checked'));
});
</script>