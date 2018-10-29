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
        <td valign="top" class="descriptionText"><s:text name="absorbDatasets.description" /></td>
      </tr>
      <tr>
        <td valign="top">

        <table width="400" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td>


            <div id="container"
              style="width: 950px; height: 365px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
            <!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
            <!--div class="ui-layout-west" style="overflow-x: hidden;width:10px">
            ss

            </div-->
            <div class="ui-layout-center" style="width:800px;overflow:hidden;">
            <div id="dynamicPane">
           
           
           
           
           		<div class="innerPane" style="width:99%" >
                
                
                <!---- form starts -------------------------------------->
<s:form namespace="/swi" method="POST" action="defineAsset">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="51%">
    
    		<table width="90%" border="0" align="center" cellpadding="3" cellspacing="0">
              <tr>
                <td class="fieldNames">&nbsp;</td>
                <td height="40">&nbsp;</td>
              </tr>
              <tr>
                <td width="50%" class="fieldNames">App Name</td>
                <td width="50%"><select name="asset.dataSource.name" id="dataSource.name" >
                  <option value="ABC Cards ">ABC Cards</option>
                </select></td>
              </tr>
              <tr>
                <td class="fieldNames">App Name</td>
                <td><input name="asset.name" type="text" class="textBox" id="name"></td>
              </tr>
              <tr>
                <td class="fieldNames">App Description</td>
                <td><input name="asset.displayName" type="text" class="textBox" id="displayName"></td>
              </tr>
              <tr>
                <td height="50" colspan="2" class="fieldNames"><table width="auto" border="0" cellspacing="0" cellpadding="0" align="center">
                  <tr>
                    <td width="7%"><label>
                      <input type="radio" name="radio" id="radio" value="radio" />
                    </label></td>
                    <td width="25%">Refresh</td>
                    <td width="6%"><input type="radio" name="radio" id="radio2" value="radio" /></td>
                    <td width="26%">Amend</td>
                    <td width="6%"><input type="radio" name="radio" id="radio3" value="radio" /></td>
                    <td width="30%">New</td>
                  </tr>
                </table></td>
                </tr>
              <!--<tr>
                <td class="fieldNames">Detail Level</td>
                <td><input name="textfield12" type="text" class="textBox" id="textfield12"></td>
              </tr>
              -->
              <tr>
                <td class="fieldNames">Data Set Name</td>
                <td><select name="asset.dataSource.name3" id="asset.dataSource.name2" >
                  <option value="ABC Cards DW">ABC Cards DW</option>
                </select></td>
              </tr>
              <tr>
                <td class="fieldNames">Data Set Name</td>
                <td><input name="asset.description" type="text" class="textBox" id="descriotion" /></td>
              </tr>
              <tr>
                <td class="fieldNames">Data Set Description</td>
                <td><input name="descriotion2" type="text" class="textBox" id="descriotion3" /></td>
              </tr>
              <tr>
                <td class="fieldNames">&nbsp;</td>
                <td height="40"><span class="fieldNames">
                  <input type="image" name="imageField" id="imageField" src="../images/absorbButton.jpg" />
                <input type="image" name="imageField2" id="imageField2" src="../images/cancelButton.jpg" />
                </span></td>
              </tr>
              <tr>
                <td height="35" colspan="2" class="fieldNames">&nbsp;</td>
              </tr>
            </table>
    
    </td>
    <td width="49%" valign="top"><table width="90%" border="0" align="center" cellpadding="3" cellspacing="0">
      <tr>
        <td class="fieldNames">&nbsp;</td>
        <td height="40">&nbsp;</td>
      </tr>
      <tr>
        <td width="24%" class="fieldNames">Upload File</td>
        <td width="76%" align="left"><label>
          <input type="file" name="fileField" id="fileField" />
          </label></td>
      </tr>
      <!--<tr>
                <td class="fieldNames">Detail Level</td>
                <td><input name="textfield12" type="text" class="textBox" id="textfield12"></td>
              </tr>
              -->
      <tr>
        <td height="35" colspan="2" class="fieldNames">
        <div style="width:90%;border:1px solid #CCC;padding:10px;">
        <table width="80%" border="0" align="center" cellpadding="4" cellspacing="0">
          <tr>
            <td colspan="2"><table width="auto" border="0" cellspacing="0" cellpadding="4" align="center">
              <tr>
                <td ><label>
                  <input type="radio" name="radio" id="radio4" value="radio" />
                </label></td>
                <td >CSV</td>
                <td ><input type="radio" name="radio" id="radio5" value="radio" /></td>
                <td >TSV</td>
                </tr>
            </table></td>
            </tr>
          <tr>
            <td width="71%">Columns Specified</td>
            <td width="29%"><label>
              <input type="checkbox" name="checkbox" id="checkbox" />
            </label></td>
          </tr>
          <tr>
            <td>String Quoted with</td>
            <td><label>
              <input name="textfield" type="text" id="textfield" size="10" />
            </label></td>
          </tr>
          <tr>
            <td>Null values specified as</td>
            <td><input name="textfield2" type="text" id="textfield2" size="10" /></td>
          </tr>
        </table>
        </div>
        </td>
      </tr>
    </table></td>
  </tr>
</table>

          </s:form>
          
          
           <!---- form ends -------------------------------------->
           
           
          </div>
           
           
           
           
           
            </div>
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