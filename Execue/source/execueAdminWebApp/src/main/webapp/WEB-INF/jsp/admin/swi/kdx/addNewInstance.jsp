<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="s" uri="/struts-tags"%>
    <s:if test="instance==null">
	<s:set name="mode" value="%{'add'}" />
</s:if>
<s:else>
	<s:set name="mode" value="%{'update'}" />
</s:else>

<s:if test="mode == 'update'">
<title><s:text name="execue.instance.updateViewInstance"/></title>
</s:if>
<s:else>
<title><s:text name="execue.instance.addNewInstance"/></title>
</s:else>
</head>
<body>
<div id="errorMessage" style="color: red;padding-top:0px;"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage" style="color: green;padding-top:0px;"><s:actionmessage /></div>
<div class="innerPane" style="width:460px;height:auto" >

<table width="460" border="0" cellspacing="0" cellpadding="0" style="margin-left:2px;margin-top:0px;">
  
  <s:if test="mode == 'add'">
   <tr>
  <td width="55%"  height="25" class="fieldNames">
  
    <strong><s:text name="execue.instance.addNewInstance"/></strong></td>
     <td width="45%" align="center" class="fieldNames" style="padding-left:30px;">&nbsp;</td>
  
    </tr>
  <tr>
  <td width="55%"  height="25" class="fieldNames">&nbsp;
  
    </td>
     <td width="45%" align="center" class="fieldNames" ><div id="closeInnerPaneLink" ><a href="javascript:getConcept('<s:property value="concept.id"/>');" id="closeInnerPane"><s:text name='execue.businessterms.showConcept.link' /></a></div><div id="loadingCloseInnerPaneLink" style="display:none;"><img src="images/admin/loading.gif" width="25" height="25"></div></td>
  
    </tr>
    </s:if>
    <s:else>
     <td width="55%"  height="25" class="fieldNames">
  
     <strong><s:text name="execue.instance.updateViewInstance"/></strong></td>
     <td width="45%" align="center" class="fieldNames" style="padding-left:30px;">&nbsp;</td>
  
    </tr>
    <tr>
    <td width="55%" height="25"  class="fieldNames">&nbsp;
  
    
    </td>
    <td width="45%" align="center" class="fieldNames" ><div id="closeInnerPaneLink" ><a href="javascript:getConcept('<s:property value="concept.id"/>');" id="<s:property value="concept.id"/>"><s:text name='execue.businessterms.showConcept.link' /></a></div><div id="loadingCloseInnerPaneLink" style="display:none;"><img src="images/admin/loading.gif" width="25" height="25"></div></td>
  </tr>
  </s:else>
  
  

  
  <tr>
    <td colspan="2"><s:form namespace="/swi" method="POST" action="addInstance" id="instanceForm">
      <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
        <tr>
          <td colspan="2">
          <div id="message" STYLE="color: red" align="left"></div>
          </td>
        </tr>
        <tr>
          <td width="30%" class="fieldNames"><s:text name="execue.concept.name"/>  : </td>
          
          <td width="70%"><s:property value="concept.displayName" /></td>
        </tr>
        <tr>
          <td class="fieldNames"><s:text name="execue.instance.name"/><s:text name="execue.global.mandatory"></s:text></td>
          
          <td>
          <s:if test="instance.id != null">
                <s:hidden name="instance.id" value="%{instance.id}" />
                <s:hidden name = "mode" value="update"/>
          	<s:textfield name="instance.displayName" cssClass="textBox" id="iname" maxlength="255"/>          
          </s:if>          
          <s:else>
             <s:hidden name = "mode" value="add"/>
             <s:textfield name="instance.displayName" cssClass="textBox" id="iname" maxlength="255"/>
             <a href="#" id="helpLink" title="Help" alt="Help">?</a>
          </s:else>
          </td>
        </tr>
        <tr>
          <td class="fieldNames"><s:text name="execue.global.abbreviatedName"/> </td>
          <td><s:textfield name="instance.abbreviatedName"  cssClass="textBox" id="abbrname"/></td>
        </tr>
        <tr>
          <td class="fieldNames"><s:text name="execue.global.description"/> </td>
          <td><s:textfield name="instance.description"  cssClass="textBox" id="name"/></td>
        </tr>
        <tr>
         
           
          <td height="40"  colspan="2" align="left" class="buttonsPadding">
	         <s:if test="instance.id != null">
	         	<span id="enableUpdateInstance">   <input type="button"	class="buttonSize108" 
					value="<s:text name='execue.instance.updateInstance' />" onClick="createUpdateInstance()"/></span>
        			<span id="updateProcess" style="display:none">  <input type="button" disabled="disabled"	class="buttonLoaderSize108" 
					value="<s:text name='execue.instance.updateInstance' />"  />
    			</span>
	            
	         </s:if>
	         <s:else>
	         	<span id="enableUpdateInstance">
                  <input type="button"	class="buttonSize108" 
					value="<s:text name='execue.instance.createInstance' />" onClick="createUpdateInstance()"/></span>
        			<span id="updateProcess" style="display:none"><input type="button" disabled="disabled"	class="buttonLoaderSize108" 
					value="<s:text name='execue.instance.createInstance' />"  />
    			</span>
	         </s:else>
            <span class="rightButton"><input type="button"	class="buttonSize90" 
					value="<s:text name='execue.global.resetButton.name' />" onClick="javascript:document.addInstance.reset();"/></span></td>
        </tr>
      </table>
      <s:hidden name="concept.id" value="%{concept.id}" />
            <s:hidden name="instance.name" value="%{instance.name}" />
    </s:form></td>
  </tr>
</table>
</div>



</body>
</html>
<script>
$(function(){
 $("#helpLink").helpMessage({messageKey:"execue.instance.help.name"}); 
});
</script>