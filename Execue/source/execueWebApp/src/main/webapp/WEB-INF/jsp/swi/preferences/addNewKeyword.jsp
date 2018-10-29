<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:if test="keyWord.id == null">
	<s:set name="mode" value="%{'add'}" />
</s:if>
<s:else>
	<s:set name="mode" value="%{'update'}" />
</s:else>

<div class="innerPane2" style="width: 99%;">
<div id="errorMessage" style="color: red"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage" style="color: red"><s:actionmessage /></div>
<s:form id="keyWordDefinitionForm">
<table width="90%" border="0" align="center" cellpadding="5" cellspacing="0">
  <tr>
    <td colspan="2"><div id="errorMessage" STYLE="color: red" align="left"></div></td>
  </tr>
  <tr>
    <td colspan="2" >
      <table width="79%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>
                 <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>                 
                    <td width="25%"  class="fieldNames" style="text-align:right"><s:text name='execue.keywords.keyword.heading' /></td>
                    <td width="3%">:</td>                    
                    <s:if test="#mode == 'add'">
						<td width="68%"><s:textfield name="keyWord.word" 
							cssClass="textBox" id="keyWord.word"/></td>
					</s:if>
					<s:else>
						<td width="68%"><s:textfield name="keyWord.word"
							cssClass="textBoxDisabled" id="keyWord.word" readonly="true" /></td>
					</s:else>
                  </tr>
                  
                </table>
             </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
            <tr>
        <td>
               <%int ct=0;%>
               <div style="margin-left:70px;">
                   <table width="60%" border="0" cellspacing="0" cellpadding="3" id="pWordsTable">
                  <tr>
                    <td width="15%">&nbsp;</td><td height="30" width="85%"   class="fieldNames"><strong><a href="#" id="addParallalWords" ><s:text name='execue.keywords.addParallelWords.heading' /></a></strong>	
                     <div id="pWords">
                    <s:iterator value="uiParallelWords" status="ct" id="pwords">
                  	<tr>
                    <td width="15%" align="right"  class="fieldNames"><input name="uiParallelWords[<s:property value="#ct.index"/>].checkedState" type=checkbox value=Y  checked id="uiParallelWords[<s:property value="#ct.index"/>].checkedState"></td>  
                    <s:if test='%{#pwords.UsersParallelWord=="YES" || #pwords.isInvalidParallelWord=="YES" }'>                    
                       <td width="85%"><input  name="uiParallelWords[<s:property value="#ct.index"/>].parallelWord" type=text id="uiParallelWords[<s:property value="#ct.index"/>].parallelWord" value="<s:property value="parallelWord"/>"></td>
                    </s:if>
                    <s:else>                    
                       <td width="85%"><input  name="uiParallelWords[<s:property value="#ct.index"/>].parallelWord" type=text readonly="readonly" id="uiParallelWords[<s:property value="#ct.index"/>].parallelWord" value="<s:property value="parallelWord"/>"></td>
                    </s:else> 
                    <td width="80%"><input type="hidden" name="uiParallelWords[<s:property value="#ct.index"/>].id" value="<s:property value='id'/>"/></td>
                  </tr>
                  <%ct=ct+1;%>
                  </s:iterator>
                  </div> 
                                               
              </table>
              </div>
              
              </td>
               </tr>
           <tr>      
        <td>  
        <s:if test="keyWord.Id !=null">
          <div style="margin-left:95px;">        
            <div id="addNewParallWord">
               <span id="updateParallelWords"> 	<input type="button"	class="buttonSize160" style="width:162px;"  value="<s:text name='execue.keywords.updateParallelWords.button' />" id="submitKeyword" onclick="updateParallelWords();"/></span><span id="updateParallelWordsProcess"   style="display:none"><input type="button"	 disabled="disabled"class="buttonLoaderSize160" style="width:162px;"  value="<s:text name='execue.keywords.updateParallelWords.button' />" id="submitKeyword1" /> </span> </div> </div>
        </s:if>
        <s:else>
          <div style="margin-left:95px;">        
            <div id="addNewParallWord">
                	<span id="createParallelWords"><input type="button"	class="buttonSize160" style="width:162px;"  value="<s:text name='execue.keywords.createParallelWords.button' />" id="submitKeyword" onclick="createParallelWords();"/></span><span id="createParallelWordsProcess"  style="display:none"><input type="button" disabled="disabled"	class="buttonLoaderSize160" style="width:162px;"  value="<s:text name='execue.keywords.createParallelWords.button' />" id="submitKeyword1" /></span></div> </div>
        </s:else>
                 
         
        </td>
         </tr>
        </table>
      </td>
  </tr>
</table>
<s:hidden name="keyWord.Id"></s:hidden>
<input type='hidden' value='<%=ct%>' id="count"/>
</s:form>
</div>
<script>
$("#keyWord").val(keyWord);
$(document).ready(function() {
	$("#pWords").empty();	
});
$("#chkbox").click(function(){
   $(this).attr("value",$(this).is(':checked'));
});

//rowContent="<tr><td width='20%' align='right'  class='fieldNames'><input name='' type='checkbox' value='' checked ></td><td width='80%' ><input name='x' type='text' id='pWord'></td></tr>";
var cnt=parseInt($("#count").val());
$("#addParallalWords").click(function(){
	$("#imageField").show();	
	rowContent="<tr><td width='20%' align='right'  class='fieldNames'><input name='uiParallelWords["+cnt+"].checkedState' type='checkbox' value='Y' checked id='uiParallelWords["+cnt+"].checkedState'></td><td width='80%' >"
			   +"<input name='uiParallelWords["+cnt+"].parallelWord' type='text' id='uiParallelWords["+cnt+"].parallelWord'></td></tr>";									   
		$("#pWordsTable").append(rowContent);							 
		cnt++;							  });
		
</script>
