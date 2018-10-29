<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/SpryTabbedPanels.css" rel="stylesheet" type="text/css" />
<link href="../css/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/ui-layout-styles.css" rel="stylesheet" type="text/css" />
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/roundedSearch.css" rel=stylesheet>

<script src="../js/jquery.js" language="JavaScript" /></script>
<script src="../js/menu.js" language="JavaScript" /></script>
<script src="../js/mm_menu.js" language="JavaScript" /></script>
<script src="../js/jquery.ui.all.js" type="text/javascript" /></script>
<script src="../js/jquery.layout.js" type="text/javascript" /></script>
<script src="../js/SpryTabbedPanels.js" type="text/javascript" /></script>
<script src="../js/script.js" type="text/javascript" language="javascript" /></script>
<script src="../js/searchList.js" type="text/javascript" language="javascript" /></script>

<style type="text/css">

li {
  line-height:22px;
}
</style>

<script type="text/javascript"><!--


var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method
var jsDomainId;



$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
  });
  
  jsDomainId = $("#domainId").val();
  
 
});









--></script>
<table width="990" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="30" valign="bottom">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="29" class="titleWithBackground">Absorb Data Sets</td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td>
    <div id="greyBorder">
    <table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td valign="top" class="descriptionText" height="20"><s:text name="absorbDatasets.description" /></td>
      </tr>
      <tr>
        <td valign="top">

        <table width="400" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td>


            <div id="container"
              style="width: 950px; height: 365px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
            <!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
            <div class="ui-layout-west" style="overflow-x: hidden;width:200px">
            <ul>
            <li><a href="#">Details</a>
            <li><a href="#">Status</a>
            </ul>

            </div>
            <div class="ui-layout-center" style="width:600px;overflow:hidden;">
            
            
            <!------------------ center content starts ----------------->
            
            
            <div id="dynamicPane">
           
           		<div class="innerPane" style="width:99%" >
                
                
                <!---- form starts -------------------------------------->
<s:form namespace="/swi" method="POST" action="defineAsset">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="51%">
      
      <table width="90%" border="0" align="center" cellpadding="3" cellspacing="0">
        <tr>
          <td width="28%" class="fieldNames"><strong class="tableSubHeading">Status</strong></td>
          <td width="72%" height="40">&nbsp;</td>
        </tr>
        <tr>
          <td class="fieldNames">File name</td>
          <td><input name="name" type="text" class="textBox" id="name2" /></td>
        </tr>
        <tr>
          <td class="fieldNames">Data set name</td>
          <td><input name="asset.name" type="text" class="textBox" id="name"></td>
        </tr>
        <tr>
          <td height="50" colspan="2" class="fieldNames">
          
          <!--- table starts here -------->
           <div style="width:100%;border:1px solid #CCC;padding:10px;margin:auto">
          <table width="100%" border="0" cellspacing="0" cellpadding="5" align="center">
            <tr>
              <td>1)</td>
            </tr>
            <tr>
              <td>2)</td>
            </tr>
            <tr>
              <td>3)</td>
            </tr>
            </table>
            </div>
             <!--- table ends here -------->
            </td>
        </tr>
        <!--<tr>
                <td class="fieldNames">Detail Level</td>
                <td><input name="textfield12" type="text" class="textBox" id="textfield12"></td>
              </tr>
              -->
        <tr>
          <td class="fieldNames">&nbsp;</td>
          <td height="40"><span class="fieldNames">
            <input type="image" name="imageField" id="imageField" src="../images/refreshButton.jpg" />
          </span></td>
        </tr>
        <tr>
          <td height="35" colspan="2" class="fieldNames">&nbsp;</td>
          </tr>
        </table>
      
    </td>
    </tr>
</table>

          </s:form>
          
          
           <!---- form ends -------------------------------------->
           
           
          </div>
           
           
           
           
           
            </div>
            
            
            
            <!------------------ center content ends ----------------->
            </div>

            <!--div class="ui-layout-east" style="overflow-x: hidden;width:10px">
            ss
            </div-->
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
<s:hidden name="domainId"/>