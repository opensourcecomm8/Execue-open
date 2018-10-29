<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method

$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
  });
  $("#businessEntityTermTypesId").change(function(){ 
  $("#divBT").empty(); 
    $("#divBT").searchRecordsComponent({actionName:"swi/getBusinessTermsForPWsBySearchString.action?businessEntityTermType="+$(this).val(),targetDivName:"dynamicPaneBTerms"}); 
   showBusinessTerms($(this).val());
});

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
</script>
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name='execue.keywords.keywords.heading' /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText"><s:text
					name="execue.keywords.description" /> <div style="float:right;" ><s:text name="execue.keywords.businessTerm"/> : <s:select list="businessEntityTermTypes"  listValue="description" id="businessEntityTermTypesId"></s:select></div></td>
			</tr>
			<tr>
				<td valign="top">

				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>
						<div id="container"
							style="width: 100%; height: 365px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
						<!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
						<div class="ui-layout-west"
							style="overflow-x: hidden; width: 220px; padding-left: 0px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="30" class="tableHeading">
								<table width="100%" border="0" cellspacing="0" cellpadding="3">
									<tr>
										<td width="80%" valign="bottom" class="tableSubHeading"><s:text
											name='execue.keywords.BusinessTerms.heading' />
										</td>
									</tr>
								</table>
								</td>
							</tr>
							<tr>
								<td>
								<div class="tableBorder"
									style="padding-top: 5px; height: 305px; width: 235px; border: none;">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="30" align="left" valign="top">
										<div id="divBT"><!--DIV id=roundedSearch2 >
										<div class=searchStart></div>
										<INPUT class=searchField id=searchText2 type=search
											value=Search>
										<div class=searchEnd id=searchIcon1><a href="#"><img
											src="../images/searchEnd.gif" name="Image2" border="0"
											id="Image2"
											onMouseOver="MM_showMenu(window.mm_menu_0113140999_1,0,25,null,'Image2')"
											onMouseOut="MM_startTimeout();" /></a></div>
										</DIV>

										<div id="searchTables2" style="display: none"><a
											href="javascript:showSearch2('divBT');" class="links"><s:text
											name="global.search" /></a></div--></div>
										</td>
									</tr>
									<tr>
										<td>
										<div id="dynamicPaneBTerms"></div>
										</td>
									</tr>
								</table>

								</div>
								</td>
							</tr>
						</table>

						</div>
						<div class="ui-layout-center" style="width: 500px;">
						<div id="dynamicPane"></div>
						</div>

						<div class="ui-layout-east"
							style="overflow-x: hidden; padding-left: 0px;">
						<div id="rightPane"></div>

						</div>
						</div>
						&nbsp;</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>