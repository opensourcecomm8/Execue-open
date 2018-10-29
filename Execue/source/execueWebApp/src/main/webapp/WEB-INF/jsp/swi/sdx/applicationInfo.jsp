<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/roundedSearch.css" rel=stylesheet>

<script src="../js/jquery.js" language="JavaScript" /></script>
<script src="../js/menu.js" language="JavaScript" /></script>
<script src="../js/mm_menu.js" language="JavaScript" /></script>
<script src="../js/jquery.ui.all.js" type="text/javascript" /></script>
<script src="../js/jquery.layout.js" type="text/javascript" /></script>
<script src="../js/SpryTabbedPanels.js" type="text/javascript" /></script>
<script src="../js/script.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/searchList.js" type="text/javascript"
	language="javascript" /></script>

<script type="text/javascript"><!--


var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method
var jsDomainId;



$(document).ready(function() {
  /*myLayout = $('#container').layout({
    west__showOverflowOnHover: true
  });*/
  
  jsDomainId = $("#domainId").val();
 
 /*$.get("editApp.htm",{},function(data){
		$("#dynamicPane").html(data)	;					 
								 });
 */

});

function showSearch(divName){
	obj=" ";
  $("#"+divName+obj+"#searchTables").hide("");
  $("#"+divName+obj+"#roundedSearch").fadeIn("slow");
  $('#searchText').val("");
   $('#searchText').focus(function(){
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
  });
  
  $('#searchText').keydown(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
     showAll();
    }
  });
  
}
function showSearch2(divName){
	obj=" ";
  $("#"+divName+obj+"#searchTables2").hide();
  $("#"+divName+obj+"#roundedSearch2").fadeIn("slow");
   $('#searchText2').val("");
  $('#searchText2').focus(function(){
    if($(this).attr('value') == ''|| $(this).attr('value') == 'Search'){  //test to see if the search field value is empty or 'search' on focus, then remove our holding text
      $(this).attr('value', '');
    }
    $(this).css('color', '#000');
    
  });
  
  $('#searchText2').blur(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
      $(this).attr('value', 'Search');
      $(this).css('color', '#777');
    }
  });
  
  $('#searchText2').keydown(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
     showAll(2);
    }
  });
}



function addNewKeyword() {
  $.post("addNewKeyword.jsp", {}, function(data) {
    $("#dynamicPane").fadeIn("fast");
    $("#dynamicPane").empty();
    $("#dynamicPane").append(data);
    $("#dynamicPane").show();
  });
}




--></script>
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text name='execue.app.heading' /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder" style="height:auto">
		<table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" >
                
                 <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"> 
			   <tr>
				<td valign="top" width="70%" class="descriptionText"><s:text	name="execue.application.description" /></td>
                    
                    
                  
			</tr>
            <s:if test="application.id != null">
            <tr>
				 <td align="left" height="20" width="30%">
                    
                    <div id="addNewUserLink"><!--div><a class="arrowBg"
			href="javascript:addNewApplication();"><s:text name='application.AddNewApplication.link' /></a></div-->
            <div style="padding-top:3px;padding-bottom:3px;"><A class="arrowBg" href="../swi/showAppInfo.action?applicationId=<s:property value='application.id'/>"><s:text name="execue.application.appInfo" ></s:text></a>
            </div>
            </div>
								<div id="loadingAddNewUserLink" style="display: none;"><img
									src="../images/loadingBlue.gif" width="25" height="25"></div>
                                    
                                    	</td>
                    
             </s:if>       
                   
			</tr>
            </table> 
                
                </td>
			</tr>
			<tr>
				<td valign="top">

				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>


						<div id="container"
							style="width: 100%; min-height: 180px;height:auto; margin: auto; border-top: #CCC dashed 1px;margin-bottom:20px;margin-top:3px; ">
						<!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
						<!-- <div class="ui-layout-west" style="overflow-x: hidden;padding-left:0px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td>

								<div class="tableBorder"
									style="padding-top: 5px; height: 305px;border:none;">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="30" align="left" valign="top">
										<div id="divProfiles">
										<DIV id=roundedSearch2 >
										<div class=searchStart></div>
										<INPUT class=searchField id=searchText2 type=search
											value=Search>
										<div class=searchEnd id=searchIcon1><a href="#"><img
											src="../images/searchEnd.gif" name="Image2" border="0"
											id="Image2"
											onMouseOver="MM_showMenu(window.mm_menu_0113140999_1,0,25,null,'Image2')"
											onMouseOut="MM_startTimeout();" /></a></div>
										</DIV>
									
										</td>
									</tr>
									<tr>
										<td>
										
										<div id="dynamicPaneApplications"></div>
											
										
										</td>
									</tr>
								</table>

								</div>
								</td>
							</tr>
						</table>

						</div>-->
						
						<div id="dynamicPane"></div>
						

						<!--div class="ui-layout-east" style="overflow-x: hidden;"></div-->
						</div>
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