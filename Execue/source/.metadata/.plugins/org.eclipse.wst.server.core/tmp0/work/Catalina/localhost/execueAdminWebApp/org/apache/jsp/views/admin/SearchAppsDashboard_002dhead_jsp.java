package org.apache.jsp.views.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class SearchAppsDashboard_002dhead_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<link href=\"../css/common/styles.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
      out.write("<link href=\"../css/common/ui-layout-styles.css\" rel=\"stylesheet\"\r\n");
      out.write("\ttype=\"text/css\">\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\"\r\n");
      out.write("\thref=\"../css/admin/jquery-ui-admin-1.7.1.custom.css\" />\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\"\r\n");
      out.write("\thref=\"../css/common/ui.jqgrid.css\" />\r\n");
      out.write("<style type=\"text\">\r\n");
      out.write("   html, body {\r\n");
      out.write("   margin: 0;\t\t\t/* Remove body margin/padding */\r\n");
      out.write("   padding: 0;\r\n");
      out.write("   overflow: hidden;\t/* Remove scroll bars on browser window */\r\n");
      out.write("   font-size: 75%;\r\n");
      out.write("   }\r\n");
      out.write("</style>\r\n");
      out.write("<script src=\"../js/common/jquery.js\" type=\"text/javascript\"></script>\r\n");
      out.write("<script src=\"../js/common/i18n/grid.locale-en.js\" type=\"text/javascript\"></script>\r\n");
      out.write("<script src=\"../js/common/jquery.jqGrid.min.js\" type=\"text/javascript\"></script>\r\n");
      out.write("<!--  TODO: remove the loader.js after all the js files have been merged into one min file --> \r\n");
      out.write("<script src=\"../js/admin/jquery.execue.upload.js\" type=\"text/javascript\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"../js/admin/jquery.execue.jobStatus.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("var paginationType=\"availableAssets\";\r\n");
      out.write("$(document).ready(function() { \r\n");
      out.write("\tvar applIdFromSession = '");
      if (_jspx_meth_s_005fproperty_005f0(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("\tvar modIdFromSession = '");
      if (_jspx_meth_s_005fproperty_005f1(_jspx_page_context))
        return;
      out.write("';\t\r\n");
      out.write("\t\tgetUploadDatasetInfo();\r\n");
      out.write("\tif(applIdFromSession){\r\n");
      out.write("\t\t$.each($(\"input[name='applicationId']\"),function(k,v){\r\n");
      out.write("\t\t\tif($(this).attr(\"value\")==applIdFromSession){\r\n");
      out.write("\t\t\t\t$(this).attr(\"checked\",true);\t\t\t\t\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\t} \r\n");
      out.write("\t/*if($(\"input[name='applicationId']\")){\r\n");
      out.write("\t\t$(\"input[name='applicationId']\").bind(\"click\", function(){\r\n");
      out.write("\t\t\t// no need to reload the page any more.\r\n");
      out.write("\t\t\tsetAppInfo(true);\r\n");
      out.write("\t\t});\r\n");
      out.write("\t}*/\r\n");
      out.write("\t\r\n");
      out.write("\t$(\"a[name='appEdit']\").click(function(){\r\n");
      out.write("\t\t\t\t\t\t  \r\n");
      out.write("\t\t$radioSelected=$(this).parent().prev().children();\r\n");
      out.write("\t\t$radioSelected.attr(\"checked\",true);\r\n");
      out.write("\t});\r\n");
      out.write("});\r\n");
      out.write("function createAppFromFile(){\r\n");
      out.write("\t$(\"#searchAppDashboardDiv\").hide();\r\n");
      out.write("\t$(\"#uploadFileInfoOuterDiv\").show();\r\n");
      out.write("\t\r\n");
      out.write("\t$(\"#uploadFileInfo\").slideDown(\"slow\");\r\n");
      out.write("\r\n");
      out.write("\t$(\"#createAppFromFileLink\").removeAttr(\"href\").css(\"color\",\"#000\");\r\n");
      out.write("\r\n");
      out.write("\t$(\"#uploadFileInfo\").fadeIn(\"fast\");\r\n");
      out.write("    $(\"#uploadFileInfo\").show();\r\n");
      out.write("\t//$(\"#closeFileUpload\").show();\r\n");
      out.write("}\r\n");
      out.write("function closeFileUpload(){\r\n");
      out.write("\t$(\"#createAppFromFileLink\").attr(\"href\",\"#\").css(\"color\",\"#003399\");\r\n");
      out.write("\t$(\"#uploadFileInfo\").slideUp(\"slow\");\r\n");
      out.write("}\r\n");
      out.write("function getUploadDatasetInfo() {\r\n");
      out.write(" var publisherProcessType='");
      if (_jspx_meth_s_005fproperty_005f2(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write(" var url='';\r\n");
      out.write("     url=\"../publisher/showSimplifiedUpload.action?publisherProcessType=SIMPLIFIED_PUBLISHER_PROCESS\";\r\n");
      out.write("  $.post(url,{}, function(data) { \r\n");
      out.write("    \r\n");
      out.write("    $(\"#uploadFileInfo\").empty();\r\n");
      out.write("    $(\"#uploadFileInfo\").append(data);\r\n");
      out.write("    \r\n");
      out.write("    /* set the form to be submitted like ajax for uploading file(s) */\r\n");
      out.write("    ajaxUpload(\"uploadCSV\", \"execueBody\", \"../publisher/uploadCSV.action\");\r\n");
      out.write("   // showDSConnection();\r\n");
      out.write("  });  \r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("function setSelected(applId, appName, modId, appSrcType){\r\n");
      out.write("\t\tif(applId > 0){\r\n");
      out.write("\t\t\t$.post(\"../swi/setAppInfo.action\",{applicationId:applId, modelId:modId, applicationName:appName, appSourceType:appSrcType});\r\n");
      out.write("\t\t\t$(\"#appSelectedMessageDiv\").text(appName+\" App selected successfully\");\r\n");
      out.write("\t\t\t$(\"#appSelectedMessageDiv\").slideDown('normal');\r\n");
      out.write("\t\t\t$(\"#pleaseSelectApplication\").empty();\r\n");
      out.write("\t\t}\r\n");
      out.write("}\r\n");
      out.write("function deleteApp (deleteMessage, appId){\r\n");
      out.write("\tconfirmed = confirm(deleteMessage);\r\n");
      out.write("\tif (confirmed) {\r\n");
      out.write("\t\t$(\"#\"+appId+\"Div\").empty();\r\n");
      out.write("\t\t$(\"#showStatus\"+appId).empty();\r\n");
      out.write("\t\t$(\"#showStatus\"+appId).jobStatus({\r\n");
      out.write("\t\t\trequestJobURL : \"../swi/deleteAppJob.action?application.id=\" + appId,\r\n");
      out.write("\t\t\tpostDataForJobParam : function(){var data = {}; return data;},\r\n");
      out.write("\t\t\tpostCall : function(jobId, status) {updatePublishedFileInfo(appId, status)}\r\n");
      out.write("\t\t});\r\n");
      out.write("\t}\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("function updatePublishedFileInfo(appId, status) {\r\n");
      out.write("\t$(\"#appSelectedMessageDiv\").text(\"\");\r\n");
      out.write("\t$pubDiv = $(\"#showStatus\"+appId);\r\n");
      out.write("\t$pubDiv.show();\r\n");
      out.write("\tif(status == \"SUCCESS\"){\r\n");
      out.write("\t\t$pubDiv.empty().append(\"App Deleted Sucessfully\");\r\n");
      out.write("\t\tsetTimeout(function(){$(\"#grid\").delRowData( appId );  $(\"#grid\").prev(\"div\").css(\"height\",\"0px\"); \t},3000);\r\n");
      out.write("\t}\r\n");
      out.write("\telse if(status == \"FAILURE\")\r\n");
      out.write("\t\t$pubDiv.empty().append(\"App Deletion Failed\");\r\n");
      out.write("}\r\n");
      out.write("/*function setAppInfo(fromChangeEvent){\r\n");
      out.write("\tvar applIdFromSession = '");
      if (_jspx_meth_s_005fproperty_005f3(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("\tvar modIdFromSession = '");
      if (_jspx_meth_s_005fproperty_005f4(_jspx_page_context))
        return;
      out.write("';\t\r\n");
      out.write("\tvar applId = \"\";\r\n");
      out.write("\tvar modId = \"\";\r\n");
      out.write("\t\r\n");
      out.write("\tif (fromChangeEvent){\r\n");
      out.write("\t\t// a change event from the select box has been triggered.\r\n");
      out.write("\t\t$radioSelected = $(\"input[name='applicationId']:checked\");\r\n");
      out.write("\t\tsetSelected($radioSelected)\r\n");
      out.write("\t}\r\n");
      out.write("}*/\r\n");
      out.write("function handleUploadRequest () {\r\n");
      out.write("  $(\"#errorMessage\").empty(); \r\n");
      out.write(" $(\"#uploadButtonSpan\").hide();\r\n");
      out.write(" $(\"#uploadButtonSpanLoader\").show();\r\n");
      out.write("} // End of handleUploadRequest()\r\n");
      out.write("\r\n");
      out.write("/*\r\n");
      out.write(" * handling the response for upload file functionality \r\n");
      out.write(" */\r\n");
      out.write("function handleUploadResponse (returnedData) {\r\n");
      out.write("\r\n");
      out.write("  if (returnedData.status) {\r\n");
      out.write("\r\n");
      out.write("    $(\"#uploadCsvDynamicContent\").empty();\r\n");
      out.write("    $(\"#uploadCsvDynamicContent\").hide();\r\n");
      out.write("\r\n");
      out.write("\tvar htmlContent = buildResponse(returnedData);\r\n");
      out.write("\t\r\n");
      out.write("\t$(\"#uploadCsvDynamicContent\").css(\"paddingLeft\",\"10px\").append(htmlContent);\r\n");
      out.write("    $(\"#uploadCsvDynamicContent\").show();\r\n");
      out.write("\tif (returnedData.absorbAsset) {\r\n");
      out.write("\tcheckAbsorptionStatus();\r\n");
      out.write("\t}\r\n");
      out.write("\r\n");
      out.write("  } else {\r\n");
      out.write("\r\n");
      out.write("    $.each(returnedData.errorMessages, function () {\r\n");
      out.write("      $(\"#errorMessage\").append(\"<li>\"+this+\"<\\li>\");\r\n");
      out.write("\t     $(\"#uploadButtonSpan\").show();\r\n");
      out.write("         $(\"#uploadButtonSpanLoader\").hide();\r\n");
      out.write("    });\r\n");
      out.write("\r\n");
      out.write("  }\r\n");
      out.write("\r\n");
      out.write("} // end of handleUploadResponse()\r\n");
      out.write("\r\n");
      out.write("function checkAbsorptionStatus () {\r\n");
      out.write("  var jobRequestId = $(\"#jobRequestId\").val();\r\n");
      out.write("  var $checkAbsorbtionStatus=$('<div id=\"checkAbsorbtionStatusDiv\" style=\"width:150px;float:left;display:none;margin-top:5px;margin-bottom:5px;\"><a href=\"javascript:checkAbsorptionStatus();\"  >Check Absobtion Status </a></div>');\r\n");
      out.write("  \r\n");
      out.write("  var $showAbsorbtionStatus=$('<div id=\"showAbsorbtionStatusDiv\" style=\"background-color:#FFF;width:600px;float:left;margin-top:5px;margin-bottom:5px;display:none;\"></div>');\r\n");
      out.write("  \r\n");
      out.write(" $(\"#errorMessageOuter\").append($checkAbsorbtionStatus);\r\n");
      out.write("  $showAbsorbtionStatus.insertAfter($checkAbsorbtionStatus);\r\n");
      out.write("  \r\n");
      out.write("  $(\"#checkAbsorbtionStatus\").hide();\r\n");
      out.write("  $(\"#checkAbsorbtionStatusDiv\").hide();\r\n");
      out.write("  $(\"#uploadAnotherFile\").hide();\r\n");
      out.write("  $(\"#showAbsorbtionStatusDiv\").empty();\r\n");
      out.write("  // showUploadStatus\r\n");
      out.write("  //location.href=\"../publisher/showUploadStatus.action?jobRequest.id=\"+jobRequestId;\r\n");
      out.write("  $(\"#showAbsorbtionStatusDiv\").jobStatus({\r\n");
      out.write("\t\t//requestJobURL : \"../swi/invokePublishAssetsMaintenaceJob.action\",\r\n");
      out.write("\t\tjobId:jobRequestId,\r\n");
      out.write("\t\t/*postDataForJobParam : function () {\r\n");
      out.write("\t\t\t\tvar data = {};\r\n");
      out.write("\t\t\t\tdata.selectedAssetId = assetId;\r\n");
      out.write("\t\t\t\tdata.publishMode = publishMode;\r\n");
      out.write("\t\t\t\treturn data;\r\n");
      out.write("\t\t\t\t},*/\r\n");
      out.write("\t\tpostCall : function(jobRequestId,status) {updateAbsorbtionInfo(jobRequestId,status)}\r\n");
      out.write("\t});\r\n");
      out.write("  \r\n");
      out.write("} // end of checkAbsorptionStatus()\r\n");
      out.write("function updateAbsorbtionInfo(jobid,status) {\r\n");
      out.write("\t// specifc code for showing specific\r\n");
      out.write("\t//alert(status);\r\n");
      out.write("\tif(status==\"SUCCESS\"){\r\n");
      out.write("\t$pubDiv= $(\"#showAbsorbtionStatusDiv\").css(\"textAlign\",\"left\");\r\n");
      out.write("\t$pubDiv.show();\r\n");
      out.write("\t//$pubDiv.empty()\r\n");
      out.write("\t\t   //.append($(\"<a>\").html(\"Metadata Screen\").attr(\"href\",\"javascript:showConfirmationScreen()\"));\r\n");
      out.write("\t\t   \r\n");
      out.write("\tshowConfirmationScreen();\r\n");
      out.write("\t}else{\r\n");
      out.write("\t$(\"#checkAbsorbtionStatusDiv\").show();\r\n");
      out.write("\t}\r\n");
      out.write("\t//$(\"#uploadAnotherFile\").show().css({marginLeft:\"0px\"});\r\n");
      out.write("}\r\n");
      out.write("function showConfirmationScreen() {\r\n");
      out.write("    var fileInfoId = $(\"#publishedFileInfoId\").val();\r\n");
      out.write("    var jobRequestId = $(\"#jobRequestId\").val();\r\n");
      out.write("    document.location = \"../publisher/showPublishedFileTables.action?publishedFileId=\"+fileInfoId+\"&jobRequestId=\"+jobRequestId;\r\n");
      out.write("}\r\n");
      out.write("function showAllUploadedFiles () {\r\n");
      out.write("  location.href=\"../swi/showAssetsDashboard.action\";\r\n");
      out.write("} // end of showAllUploadedFiles()\r\n");
      out.write("\r\n");
      out.write("function buildResponse(returnedData) {\r\n");
      out.write("\tvar htmlContent = staticMsga;\r\n");
      out.write("\t$.each(returnedData.messages, function () {\r\n");
      out.write("\t  htmlContent = htmlContent + \"<li><span class='green'>\"+this+\"<\\span><\\li>\";\r\n");
      out.write("\t});\r\n");
      out.write("\thtmlContent = htmlContent + staticMsgb;\r\n");
      out.write("\thtmlContent = htmlContent + static1 + tableStart  + staticNamea;\r\n");
      out.write("\thtmlContent = htmlContent + returnedData.fileName;\r\n");
      out.write("\thtmlContent = htmlContent + staticNameb + tableEnd + static2 + static3 ;\r\n");
      out.write("\r\n");
      out.write("\thtmlContent = htmlContent + staticFilea;\r\n");
      out.write("  if (returnedData.sourceDataFileName) {\r\n");
      out.write("      htmlContent = htmlContent + returnedData.sourceDataFileName;\r\n");
      out.write("  } else {\r\n");
      out.write("  htmlContent = htmlContent + \"Not Provided\";\r\n");
      out.write("  }\r\n");
      out.write("  htmlContent = htmlContent + staticFileb;\r\n");
      out.write("\r\n");
      out.write("  htmlContent = htmlContent + staticURLb + staticSourceTypea;\r\n");
      out.write("  if(returnedData.isCompressedFile==\"YES\"){\r\n");
      out.write("\t  htmlContent = htmlContent + returnedData.sourceType + \" (ZIP)\";\r\n");
      out.write("\t}else{\r\n");
      out.write("\t  htmlContent = htmlContent + returnedData.sourceType;\r\n");
      out.write("  }\t\r\n");
      out.write("  htmlContent = htmlContent + staticSourceTypeb;\r\n");
      out.write("\r\n");
      out.write("\t/* + staticDesca;\r\n");
      out.write("\tif(returnedData.fielDescription){\r\n");
      out.write("\t  htmlContent = htmlContent + returnedData.fielDescription;\r\n");
      out.write("\t}else{\r\n");
      out.write("\t  htmlContent = htmlContent + \" \";\r\n");
      out.write("\t}\r\n");
      out.write("\thtmlContent = htmlContent + staticDescb;\r\n");
      out.write("  \r\n");
      out.write("\thtmlContent = htmlContent + staticTagsa;\r\n");
      out.write("\tif (returnedData.tag) {\r\n");
      out.write("\t\thtmlContent = htmlContent + returnedData.tag;\r\n");
      out.write("\t} else {\r\n");
      out.write("\t  htmlContent = htmlContent + \"Not Provided\";\r\n");
      out.write("\t}\r\n");
      out.write("\thtmlContent = htmlContent + staticTagsb;\r\n");
      out.write("\r\n");
      out.write("\tif(!returnedData.applicationName){\r\n");
      out.write("\t   returnedData.applicationName=\" \";\r\n");
      out.write("\t}\r\n");
      out.write("\tif (returnedData.absorbAsset) {\r\n");
      out.write("\t  htmlContent = htmlContent + staticAppa + returnedData.applicationName + staticAppb;\r\n");
      out.write("\t}\r\n");
      out.write("\t*/\r\n");
      out.write("\thtmlContent = htmlContent + static8 + static9;\r\n");
      out.write("\r\n");
      out.write("\t\r\n");
      out.write("\r\n");
      out.write("\t/* + staticURLa;\r\n");
      out.write("\tif (returnedData.fileURL) {\r\n");
      out.write("\t\thtmlContent = htmlContent + returnedData.fileURL;\r\n");
      out.write("\t} else {\r\n");
      out.write("\t\t\thtmlContent = htmlContent + \"Not Provided\";\r\n");
      out.write("\t}\r\n");
      out.write("\t*/\r\n");
      out.write("\t\r\n");
      out.write("\thtmlContent = htmlContent + staticColumnNamesSpecifieda;\r\n");
      out.write("\thtmlContent = htmlContent + returnedData.columnNamesPresent;\r\n");
      out.write("\thtmlContent = htmlContent + staticColumnNamesSpecifiedb + staticStringsQuotea;\r\n");
      out.write("\thtmlContent = htmlContent + returnedData.stringEnclosure;\r\n");
      out.write("\thtmlContent = htmlContent + staticStringsQuoteb + staticNullValuesa;\r\n");
      out.write("\thtmlContent = htmlContent + returnedData.nullIdentifier;\r\n");
      out.write("\thtmlContent = htmlContent + staticNullValuesb;\r\n");
      out.write("\r\n");
      out.write("\t/* + staticAbsorbChecka;\r\n");
      out.write("\tif (returnedData.absorbAsset) {\r\n");
      out.write("\t  htmlContent = htmlContent + \"Yes\";\r\n");
      out.write("\t} else {\r\n");
      out.write("\t  htmlContent = htmlContent + \"No\";\r\n");
      out.write("\t}\r\n");
      out.write("\thtmlContent = htmlContent + staticAbsorbCheckb;\r\n");
      out.write("    */\r\n");
      out.write("    \r\n");
      out.write("\thtmlContent = htmlContent + static20 + static21;\r\n");
      out.write("\t\r\n");
      out.write("\thtmlContent = htmlContent + staticButton1;\r\n");
      out.write("\tif (returnedData.absorbAsset) {\r\n");
      out.write("\t  htmlContent = htmlContent + staticCheckStatusButton;\r\n");
      out.write("\t} else {\r\n");
      out.write("\t  htmlContent = htmlContent + staticAllFilesButton;\r\n");
      out.write("\t}\r\n");
      out.write("\thtmlContent = htmlContent + staticUploadFileButton + staticButton2;\r\n");
      out.write("\t\r\n");
      out.write("\thtmlContent = htmlContent + static22;\r\n");
      out.write("\t\r\n");
      out.write("\thtmlContent = htmlContent + staticHidden1a;\r\n");
      out.write("\thtmlContent = htmlContent + returnedData.jobRequestId;\r\n");
      out.write("\thtmlContent = htmlContent + staticHidden1b;\r\n");
      out.write("\t\r\n");
      out.write("\thtmlContent = htmlContent + staticHidden2a;\r\n");
      out.write("\thtmlContent = htmlContent + returnedData.publishedFileInfoId;\r\n");
      out.write("\thtmlContent = htmlContent + staticHidden2b + staticEnd;\r\n");
      out.write("\treturn htmlContent;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("/* Static content declaration Start */\r\n");
      out.write("\r\n");
      out.write("var staticMsga = \"<div id='errorMessageOuter'><div id='errorMessage' style='padding-top:5px;'>\";\r\n");
      out.write("var staticMsgb = \"</div></div>\";\r\n");
      out.write("var static1 = \"<div style='width: 100%; white-space: nowrap; min-height: 60px; height: auto;' class='innerPane'> <form name='uploadStatusDetails' style='float:left;'>\";\r\n");
      out.write("var tableStart=\"<table width='120' cellspacing='0' cellpadding='0' border='0' style='float:left;'>\";\r\n");
      out.write("var tableEnd=\"</table>\";\r\n");
      out.write("var static2 = \"<br/><table id='fileDetailsDiv' align='left' width='70%' cellspacing='0' cellpadding='0' border='0' style='display:none;'>\";\r\n");
      out.write("var static3 = \"<tr><td width='43%' valign='top' align='right'><table width='70%' cellspacing='0' cellpadding='3' border='0' align='left'><tbody>\";\r\n");
      out.write("var staticNamea = \"<tr><td width='66%' class='fieldNames'>Name :</td><td width='34%' style='padding-left:5px;'>\";\r\n");
      out.write("var staticNameb = \"</td><td style='padding-left:10px;'><a id='moreLinkDiv' href='javascript:showMoreDetails();'>More</a></td></tr>\";\r\n");
      out.write("var staticDesca = \"<tr><td class='fieldNames'>Description :</td><td>\";\r\n");
      out.write("var staticDescb = \"</td></tr>\";\r\n");
      out.write("var staticTagsa = \"<tr><td class='fieldNames'>Tags :</td><td>\";\r\n");
      out.write("var staticTagsb = \"</td></tr>\";\r\n");
      out.write("var staticAppa = \"<tr id='applicationSelectId'><td class='fieldNames'>Application :</td><td>\";\r\n");
      out.write("var staticAppb = \"</td></tr>\";\r\n");
      out.write("var static8 = \"</tbody></table></td><td width='57%' valign='top' align='left'><table width='80%' cellspacing='0' cellpadding='3' border='0' align='left' style='margin-left:20px;'>\";\r\n");
      out.write("var static9 = \"<tbody><tr><td ><table border='0'><tbody>\";\r\n");
      out.write("var staticFilea = \"<tr><td class='fieldNames'>Source File :</td><td>\";\r\n");
      out.write("var staticFileb = \"</td></tr>\";\r\n");
      out.write("var staticURLa = \"<tr><td class='fieldNames'>Source URL :</td><td>\";\r\n");
      out.write("var staticURLb = \"</td></tr>\";\r\n");
      out.write("var staticSourceTypea = \"<tr><td class='fieldNames'>Source Type :</td><td>\";\r\n");
      out.write("var staticSourceTypeb = \"</td></tr>\";\r\n");
      out.write("var staticColumnNamesSpecifieda = \"<tr><td width='71%' class='fieldNames'>Columns Specified :</td><td width='29%'>\";\r\n");
      out.write("var staticColumnNamesSpecifiedb = \"</td></tr>\";\r\n");
      out.write("var staticStringsQuotea = \"<tr><td class='fieldNames'>Strings Quoted With :</td><td>\";\r\n");
      out.write("var staticStringsQuoteb = \"</td></tr>\";\r\n");
      out.write("var staticNullValuesa = \"<tr><td class='fieldNames'>NULL Values Represented As :</td><td>\";\r\n");
      out.write("var staticNullValuesb = \"</td></tr>\";\r\n");
      out.write("var staticAbsorbChecka = \"<tr><td class='fieldNames'>Absorb Dataset Requested :</td><td>\";\r\n");
      out.write("var staticAbsorbCheckb = \"</td></tr>\";\r\n");
      out.write("var static20 = \"</tbody></table></td></tr>\";\r\n");
      out.write("var static21 = \"</tbody></table></td></tr>\";\r\n");
      out.write("\r\n");
      out.write("var staticButton1 = \"<tr><td valign='bottom' colspan='2' align='left'>\";\r\n");
      out.write("var staticCheckStatusButton = \"<input type='button' value='Check Abosorption Status' id='checkAbsorbtionStatus' name='imageField' class='singleButton' onclick='javascript:checkAbsorptionStatus();'>&nbsp;\";\r\n");
      out.write("var staticAllFilesButton = \"<input type='button' value='View Uploaded Files' id='imageField' name='imageField' class='singleButton' onclick='javascript:showAllUploadedFiles();'>\";\r\n");
      out.write("var staticUploadFileButton = \"&nbsp;<input type='button' value='Upload Another File' id='uploadAnotherFile' name='imageField2' class='singleButton' style='margin-left:10px;' onclick='javascript:getUploadDatasetInfo();'>\";\r\n");
      out.write("var staticButton2 = \"</td></tr>\";\r\n");
      out.write("\r\n");
      out.write("var static21a = \"</td><td align='left' valign='bottom'>\";\r\n");
      out.write("\r\n");
      out.write("var static22 = \"</tbody></table>\";\r\n");
      out.write("\r\n");
      out.write("var staticHidden1a = \"<input type='hidden' id='jobRequestId' name='jobRequestId' value='\";\r\n");
      out.write("var staticHidden1b = \"'>\";\r\n");
      out.write("\r\n");
      out.write("var staticHidden2a = \"<input type='hidden' id='publishedFileInfoId' name='publishedFileInfoId' value='\";\r\n");
      out.write("var staticHidden2b = \"'>\";\r\n");
      out.write("\r\n");
      out.write("var staticEnd = \"</form></div>\";\r\n");
      out.write("\r\n");
      out.write("/* Static content declaration End */\r\n");
      out.write("\r\n");
      out.write("function showMoreDetails(){\r\n");
      out.write("\tif($(\"#moreLinkDiv\").text()==\"More\"){\r\n");
      out.write("\t$(\"#fileDetailsDiv\").slideDown(\"slow\");\t\r\n");
      out.write("\t$(\"#moreLinkDiv\").text(\"Less\");\r\n");
      out.write("\t}else{\r\n");
      out.write("\t\t$(\"#fileDetailsDiv\").slideUp(\"slow\");\t\r\n");
      out.write("\t$(\"#moreLinkDiv\").text(\"More\");\r\n");
      out.write("\t}\r\n");
      out.write("}\r\n");
      out.write("</script>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_s_005fproperty_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f0 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f0.setParent(null);
    // /views/admin/SearchAppsDashboard-head.jsp(26,26) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f0.setValue("applicationContext.appId");
    int _jspx_eval_s_005fproperty_005f0 = _jspx_th_s_005fproperty_005f0.doStartTag();
    if (_jspx_th_s_005fproperty_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f0);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f1 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f1.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f1.setParent(null);
    // /views/admin/SearchAppsDashboard-head.jsp(27,25) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f1.setValue("applicationContext.modelId");
    int _jspx_eval_s_005fproperty_005f1 = _jspx_th_s_005fproperty_005f1.doStartTag();
    if (_jspx_th_s_005fproperty_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f1);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f2 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f2.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f2.setParent(null);
    // /views/admin/SearchAppsDashboard-head.jsp(66,27) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f2.setValue("publisherProcessType");
    int _jspx_eval_s_005fproperty_005f2 = _jspx_th_s_005fproperty_005f2.doStartTag();
    if (_jspx_th_s_005fproperty_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f2);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f3 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f3.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f3.setParent(null);
    // /views/admin/SearchAppsDashboard-head.jsp(114,26) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f3.setValue("applicationContext.appId");
    int _jspx_eval_s_005fproperty_005f3 = _jspx_th_s_005fproperty_005f3.doStartTag();
    if (_jspx_th_s_005fproperty_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f3);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f4(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f4 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f4.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f4.setParent(null);
    // /views/admin/SearchAppsDashboard-head.jsp(115,25) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f4.setValue("applicationContext.modelId");
    int _jspx_eval_s_005fproperty_005f4 = _jspx_th_s_005fproperty_005f4.doStartTag();
    if (_jspx_th_s_005fproperty_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f4);
    return false;
  }
}
