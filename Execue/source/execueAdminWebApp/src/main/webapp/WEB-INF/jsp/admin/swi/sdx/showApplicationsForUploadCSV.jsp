<script type="text/javascript">


var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method




$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
  });
  

 
 /*$.get("editApp.htm",{},function(data){
		$("#dynamicPane").html(data)	;					 
								 });
 */
});
</script>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground">Applications</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="96%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText"><s:text
					name="execue.application.description" /></td>
			</tr>
			<tr>
				<td valign="top">

				<table width="400" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>


						<div id="container"
							style="width: 950px; height: 365px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
						<!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
						<div class="ui-layout-west" style="overflow-x: hidden;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							
							<tr>
								<td>

								<div class="tableBorder"
									style="padding-top: 5px; height: 305px;">
								<table width="99%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="30" align="left" valign="top">
										<div id="divProfiles">
										<DIV id=roundedSearch2 style="display: none">
										<div class=searchStart></div>
										<INPUT class=searchField id=searchText2 type=search
											value=Search>
										<div class=searchEnd id=searchIcon1><a href="#"><img
											src="../images/admin/searchEnd.gif" name="Image2" border="0"
											id="Image2"
											onMouseOver="MM_showMenu(window.mm_menu_0113140999_1,0,25,null,'Image2')"
											onMouseOut="MM_startTimeout();" /></a></div>
										</DIV>

										<div id="searchTables2"><a
											href="javascript:showSearch2('divProfiles');" class="links"><s:text
											name="execue.global.search" /></a></div>
										</div>
										</td>
									</tr>
									<tr>
										<td>
										<div class="tableList"
											style="height: 265px; width: 190px; margin: auto; margin-bottom: 5px; margin-left: 4px;">
										<table width="100%" border="0" cellspacing="0" cellpadding="2">
											<div id="dynamicPaneApplications" style="padding-left:10px"></div>
											<div id="loadingAppsLink" style="display: none"><img
												src="../images/admin/loaderAT.gif" />
										</table>
										</div>
										</td>
									</tr>
								</table>

								</div>
								</td>
							</tr>
						</table>

						</div>
						<div class="ui-layout-center">
						<div style="float:right;"><a href="#" onclick="showUploadCSVFileInfo();">Goto Upload CSVDataSet</a></div>
						<div id="centerPane"></div>
						
						</div>

						<!--div class="ui-layout-east" style="overflow-x: hidden;"></div-->
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