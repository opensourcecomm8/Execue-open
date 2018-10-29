<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="30" valign="bottom">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="29" class="titleWithBackground"><s:text name="execue.upload.main.heading"/></td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td>
    <div id="greyBorder" style="min-height:420px;height:auto;">

       


            <div id="container"
              style="width: 950px; min-height:430px;height:auto; margin: auto; ">
            <!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
            <!-- <div class="ui-layout-west" style="overflow-x: hidden;height:400px;" >    
            </div>-->
            <!--div class="ui-layout-center" style="overflow:hidden;height:auto;"-->
            <div id="dynamicPane" style="height:auto;">
            <table width="100%" border="0" cellspacing="0" align="left" cellpadding="0">
              <tr>
                <td height="20" class="fieldNames"><strong><s:text name="execue.upload.dataset.heading"/></strong></td>
              </tr>
              <tr>
                <td>
                <div id="TabbedPanels1" class="TabbedPanels" style="margin-bottom:10px;">
                <ul class="TabbedPanelsTabGroup">                  
                  <li class="TabbedPanelsTab" tabindex="0"><s:text name="execue.upload.upload.tab"/></li>
                  <li class="TabbedPanelsTab" tabindex="0"><s:text name="execue.upload.connect.tab"/></li>                 
                </ul>
                <div class="TabbedPanelsContentGroup">
                <div class="TabbedPanelsContent" style="min-height:375px;height:auto;">
                <div id="uploadFileInfoWrapper">
 
				<table width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">

	<tr>
		<td><!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->

		<!-- 
            <div class="ui-layout-west" style="overflow-x: hidden; width: 10px"></div>
             -->




                <div id="uploadFileInfo">                
                </div>
		</td>
	</tr>
</table>
                </div>
                </div>
                <div class="TabbedPanelsContent" style="min-height:355px;height:auto;">
                <div id="connectionInfo">
                </div>
                </div>          
                </div>
                </div>
                </td>
              </tr>
            </table>
            </div>
            <!--/div-->
            <!-- <div class="ui-layout-east" style="overflow: hidden;">     
            </div>-->
            </div>
         

    </div>
    </td>
  </tr>
</table>