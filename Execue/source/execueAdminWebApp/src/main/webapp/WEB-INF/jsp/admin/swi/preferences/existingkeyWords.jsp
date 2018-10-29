<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib prefix="s" uri="/struts-tags"%>
<table border="0" cellpadding="0" cellspacing="0" width="195" style="margin-left:4px;">
    <!-- fwtable fwsrc="blueBox.png" fwpage="Page 1" fwbase="blueBoxLeft.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->
    <tr>
      <td height="30" class="tableHeading"><table width="100%" border="0" cellspacing="0" cellpadding="3">
        <tr>
          <td width="80%" valign="bottom" class="tableSubHeading"><s:text name='execue.keywords.keywords.heading' /></td>
        </tr>
      </table></td>
    </tr>
    <tr>
      <td width="158" valign="top">
      <div class="tableBorder" style="width:195px;border:none;">
      <s:form id="keyWordForm">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="30" valign="top"><div id="divExistingKeyWords">
										<!--DIV id=roundedSearch >
										<div class=searchStart></div>
										<INPUT class=searchField id=searchText type=search
											value=Search>
										<div class=searchEnd id=searchIcon><a href="#"><img
											src="../images/admin/searchEnd.gif" name="Image2a" border="0"
											id="Image2a"
											onMouseOver="MM_showMenu(window.mm_menu_0113140999_0,0,25,null,'Image2a')"
											onMouseOut="MM_startTimeout();" /></a></div-->
										</DIV>

									</td>
        </tr>
        <tr>
          <td><div id="divExistingKeyWordsTarget" class="tableListWithoutBorder" style="height:230px;width:190px;">
            <table width="100%" border="0" cellspacing="0" cellpadding="2" id="searchList">
               <s:iterator value="keyWords" status="inst" id="keyWord">
              <s:if test="#inst.even == true">
                            <tr class="joinRowEven" id="tableRow3" >
                <td width="10%" class="dotBullet"><input name="keyWordIdsForDeletion[<s:property value="#inst.index"/>]" type="checkbox" id="keyWordIdsForDeletion[<s:property value="#inst.index" />]" value="<s:property value="id"/>" onclick="checkedKeyWord();"/></td>
                <td width="90%" colspan="2" ><a href="#" onclick="showKeyword('<s:property value='%{#keyWord.word}'/>');" class="links"><s:property value="%{#keyWord.word}" /></a></td>
              </tr>
              </s:if>
              <s:else>
                <tr id="tableRow3">
                <td width="10%" class="dotBullet"><input name="keyWordIdsForDeletion[<s:property value="#inst.index"/>]" type="checkbox" id="keyWordIdsForDeletion[<s:property value="#inst.index" />]" value="<s:property value="id"/>" onclick="checkedKeyWord();"/></td>
                <td width="90%" colspan="2" ><a href="#" onclick="showKeyword('<s:property value='%{#keyWord.word}'/>');" class="links"><s:property value="%{#keyWord.word}"/></a></td>
              </tr>
              </s:else>
              </s:iterator>              
            </table>
          </div></td>
        </tr>
        <tr>
          <td align="center">
          	<div style="display: none;float:left;padding-left:40px;" id="enableDeleteSelect" >
                 <input type="button"	class="buttonSize108" 
					value="<s:text name='execue.global.deleteSelectedButton.name' />"  onclick="return deleteKeywords();"/></div>
             <span id="deleteProcess" style="display:none">
                  <img src="../images/admin/loadingWhite.gif" hspace="3" vspace="5" /></span>
              <div id="disableDeleteSelect" style="float:left;padding-left:40px;">
                    <input type="button"	class="buttonSize108" disabled="disabled"
					value="<s:text name='execue.global.deleteSelectedButton.name' />"  /></div>
        </tr>
      </table>
      </s:form>
      </div></td>
    </tr>
  </table>
  
  <script>
/*  $('#searchText').focus(function(){
    if($(this).attr('value') == ''|| $(this).attr('value') == 'Search'){  //test to see if the search field value is empty or 'search' on focus, then remove our holding text
      $(this).attr('value', '');
    }
    $(this).css('color', '#000');
    
  });
  
  $('#searchText').blur(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
      $(this).attr('value', 'Search');
      $(this).css('color', '#777');
    }
  });*/
  $(function(){
			 $("#divExistingKeyWords").searchRecordsComponent({actionName:"",targetDivName:"divExistingKeyWordsTarget"});
			 });
  </script>