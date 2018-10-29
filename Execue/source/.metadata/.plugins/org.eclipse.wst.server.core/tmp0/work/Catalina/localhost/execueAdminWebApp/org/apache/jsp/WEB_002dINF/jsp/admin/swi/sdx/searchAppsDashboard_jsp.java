package org.apache.jsp.WEB_002dINF.jsp.admin.swi.sdx;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class searchAppsDashboard_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.release();
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
      out.write("\r\n");
      out.write("<style type=\"text/css\">\r\n");
      out.write("<!--\r\n");
      out.write("td.heading {\r\n");
      out.write("\tbackground-image: url(../images/admin/rowBg.jpg);\r\n");
      out.write("\tfont-size: 11px;\r\n");
      out.write("\tpadding-left: 5px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("span.red {\r\n");
      out.write("\tcolor: red;\r\n");
      out.write("\tfont-size: 13px;\r\n");
      out.write("\tfont-weight: bold;\r\n");
      out.write("}\r\n");
      out.write("select{\r\n");
      out.write("width:40px;\r\n");
      out.write("}\r\n");
      out.write(".imgFade{\r\n");
      out.write("opacity: .50;\r\n");
      out.write("filter:Alpha(Opacity=50); \t\r\n");
      out.write("}\r\n");
      out.write(".imgDontFade{\r\n");
      out.write("opacity: 1;\r\n");
      out.write("filter:Alpha(Opacity=100); \t\r\n");
      out.write("}\r\n");
      out.write("-->\r\n");
      out.write("</style>\r\n");
      out.write("\r\n");
      out.write("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" >\r\n");
      out.write("  <tr>\r\n");
      out.write("    <td class=\"uploadOuterBg\"><div  id=\"uploadFileInfoOuterDiv\" style=\"display:none;\">\r\n");
      out.write("    <div class=\"uploadTopBox\" >\r\n");
      out.write("                                        \r\n");
      out.write("                                        <div style=\"margin:auto;text-align:center;\"> <span class=\"active\">");
      if (_jspx_meth_s_005ftext_005f0(_jspx_page_context))
        return;
      out.write("</span> <span class=\"arrow-active\"> >></span><span class=\"no-active\">");
      if (_jspx_meth_s_005ftext_005f1(_jspx_page_context))
        return;
      out.write("</span> <span class=\"arrow-no-active\">>></span> <span class=\"no-active\">");
      if (_jspx_meth_s_005ftext_005f2(_jspx_page_context))
        return;
      out.write("</span></div>\r\n");
      out.write("</div>\r\n");
      out.write("                                        \r\n");
      out.write("                                       \r\n");
      out.write("<div id=\"uploadFileInfo\" class=\"uploadFileMainBox\" >\r\n");
      out.write("</div>\t\r\n");
      out.write("                                        \r\n");
      out.write("</div></td>\r\n");
      out.write("  </tr>\r\n");
      out.write("</table>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("                                            \r\n");
      out.write("                                            \r\n");
      out.write("\r\n");
      out.write("<div id=\"searchAppDashboardDiv\">                                        \r\n");
      out.write("<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\"\r\n");
      out.write("\tcellspacing=\"0\">\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td height=\"30\" valign=\"bottom\">\r\n");
      out.write("\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td height=\"29\" class=\"titleWithBackground\">");
      if (_jspx_meth_s_005ftext_005f3(_jspx_page_context))
        return;
      out.write("</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td>\r\n");
      out.write("\t\t<div id=\"greyBorder\" style=\"min-height: 330px; height: auto;\">\r\n");
      out.write("\t\t<table width=\"99%\" border=\"0\" align=\"center\" cellpadding=\"10\"\r\n");
      out.write("\t\t\tcellspacing=\"0\">\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td valign=\"top\">\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"\r\n");
      out.write("\t\t\t\t\talign=\"center\">\r\n");
      out.write("\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t<td>\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"\r\n");
      out.write("\t\t\t\t\t\t\tstyle=\"white-space: nowrap;\">\r\n");
      out.write("\t\t\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("\t\t\t\t\t\t\t\t    ");
      if (_jspx_meth_s_005fif_005f0(_jspx_page_context))
        return;
      out.write(" \r\n");
      out.write("\t\t\t\t\t\t\t\t\t");
      if (_jspx_meth_s_005fif_005f1(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t\t\t</table>\r\n");
      out.write("\t\t\t\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t\t\t\t\t<td>&nbsp;</td>\r\n");
      out.write("\t\t\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t\t</table>\r\n");
      out.write("\t\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t<td width=\"50%\" align=\"center\" valign=\"top\">\r\n");
      out.write("\t\t\t\t\t\t<div style=\"padding-top: 5px;padding-bottom: 5px;display:none;\" id=\"appSelectedMessageDiv\"></div>\r\n");
      out.write("\t\t\t\t\t\t");
      if (_jspx_meth_s_005fif_005f2(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t\t<TABLE id=\"grid\" style=\"font-weight: normal;\"></TABLE>\r\n");
      out.write("\t\t\t\t\t\t<DIV id=pager></DIV>\r\n");
      out.write("\t\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t</table>\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</div>\r\n");
      out.write("<map name=\"Map\" id=\"Map\">\r\n");
      out.write("\t<area shape=\"circle\" coords=\"121,119,53\"\r\n");
      out.write("\t\thref=\"../publisher/showUploadDataset.action?show=upload\" alt=\"Upload\"\r\n");
      out.write("\t\ttitle=\"Upload\" />\r\n");
      out.write("\t<area shape=\"circle\" coords=\"122,248,55\"\r\n");
      out.write("\t\thref=\"../publisher/showUploadDataset.action?show=connect\"\r\n");
      out.write("\t\talt=\"Connect\" title=\"Connect\" />\r\n");
      out.write("\t<area shape=\"rect\" coords=\"256,70,494,117\"\r\n");
      out.write("\t\thref=\"../swi/showApplications.action\" alt=\"App\" title=\"App\" />\r\n");
      out.write("\t<area shape=\"rect\" coords=\"257,130,492,172\"\r\n");
      out.write("\t\thref=\"../swi/showAsset.action\" alt=\"Metadata\" title=\"Metadata\" />\r\n");
      out.write("\t<area shape=\"rect\" coords=\"258,190,494,240\"\r\n");
      out.write("\t\thref=\"../swi/showJoins.action\" alt=\"Joins\" title=\"Joins\" />\r\n");
      out.write("\t<area shape=\"rect\" coords=\"256,251,496,299\"\r\n");
      out.write("\t\thref=\"../swi/showMappings.action\" alt=\"Mapping\" title=\"Mapping\" />\r\n");
      out.write("\t<area shape=\"rect\" coords=\"578,77,698,301\"\r\n");
      out.write("\t\thref=\"../swi/showPublishDatasets.action\" alt=\"Publish\" title=\"Publish\" />\r\n");
      out.write("</map>\r\n");
      out.write("<SCRIPT type=text/javascript>\r\n");
      out.write("var applIdFromSession = '");
      if (_jspx_meth_s_005fproperty_005f0(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("var appSize=Number(\"");
      if (_jspx_meth_s_005fproperty_005f1(_jspx_page_context))
        return;
      out.write("\");\r\n");
      out.write("var isPublisher='");
      if (_jspx_meth_s_005fproperty_005f2(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("       \r\n");
      out.write("$(document).ready(function() {\r\n");
      out.write("$('#diagramImage,#diagramHeading').click(function(){\r\n");
      out.write("var src=null;\r\n");
      out.write("$obj=null;\r\n");
      out.write("if($(this).attr('id')==\"diagramImage\"){\r\n");
      out.write("src=$(this).find('img').attr('src');\r\n");
      out.write("$obj=$(this);\r\n");
      out.write("}else{\r\n");
      out.write("src=$(this).prev('div').find('img').attr('src');\r\n");
      out.write("$obj=$(this).prev('div');\r\n");
      out.write("}\r\n");
      out.write("/*if(src==\"../images/admin/btn-open.gif\"){\r\n");
      out.write("  $('#flowDiagramDiv').slideDown(); \r\n");
      out.write("  $obj.find('img').attr('src','../images/admin/btn-close.gif'); \r\n");
      out.write("  $('#diagramHeading').html('Hide Flow Diagram');\r\n");
      out.write("} else{\r\n");
      out.write("  $('#flowDiagramDiv').slideUp(); \r\n");
      out.write("  $obj.find('img').attr('src','../images/btn-open.gif'); \r\n");
      out.write("  $('#diagramHeading').html('Show Flow Diagram');\r\n");
      out.write("}*/\r\n");
      out.write("\r\n");
      out.write("});\r\n");
      out.write("  if(appSize>0){\r\n");
      out.write("    /*\r\n");
      out.write("  \tif(isPublisher==\"NO\"){\r\n");
      out.write("\t  $('#flowDiagramDiv').show();\r\n");
      out.write("\t}else{\r\n");
      out.write("\t  $('#flowDiagramDiv').hide();\r\n");
      out.write("\t}\t\r\n");
      out.write("\t$('#diagramHeading').show();\r\n");
      out.write("\t$('#diagramImage').show();\r\n");
      out.write("\t*/\r\n");
      out.write("\t$('#grid').jqGrid({\r\n");
      out.write("\t  url: 'applicationList.action',\r\n");
      out.write("      datatype: 'json',\r\n");
      out.write("      mtype: 'GET',\r\n");
      out.write("\t  width: 950,\r\n");
      out.write("\t  height: 270,\r\n");
      out.write("\t  rowNum: 10,\r\n");
      out.write("\t  scroll:1,\r\n");
      out.write("      rowList: [10,20,30],\r\n");
      out.write("      pager: $('#pager'),\r\n");
      out.write("      sortname: 'name',\r\n");
      out.write("      viewrecords: true,\r\n");
      out.write("\t  gridview: true,\r\n");
      out.write("\t  colNames:[ 'Select App','ModelId','");
      if (_jspx_meth_s_005ftext_005f7(_jspx_page_context))
        return;
      out.write('\'');
      out.write(',');
      out.write('\'');
      if (_jspx_meth_s_005ftext_005f8(_jspx_page_context))
        return;
      out.write("','Meta Data','");
      if (_jspx_meth_s_005ftext_005f9(_jspx_page_context))
        return;
      out.write('\'');
      out.write(',');
      out.write('\'');
      if (_jspx_meth_s_005ftext_005f10(_jspx_page_context))
        return;
      out.write("'],\r\n");
      out.write("\t  colModel:[\r\n");
      out.write("\t  \t\t\t{name:\"id\",index:'id',width:50,hidden:true,formatter:showRadio, search:false,sortable:false},\r\n");
      out.write("\t  \t\t\t{name:\"modelId\",index:'modelId',hidden:true,search:false,sortable:false},\r\n");
      out.write("\t\t\t\t{name:\"name\",index:'name',width:140,sortable:true,sorttype:\"text\", search:true, stype:'text', \r\n");
      out.write("\t\t\t\t\t\tformatter:showRadio, formatoptions:{baseLinkUrl:'../swi/showApplications.action', idName:'application.id'}},\r\n");
      out.write("\t\t\t\t{name:\"desc\",width:230,sortable:false, search:false,formatter:showShortDesc},\t\r\n");
      out.write("\t\t\t\t{name:\"metadataLink\",width:40,sortable:false, search:false,formatter:showMetadataLink},\t\r\n");
      out.write("\t\t\t\t{name:\"mode\",width:60,sortable:true,sorttype:\"text\", search:false,formatter:showMode},\r\n");
      out.write("\t\t\t\t{name:\"status\",width:90, sortable:true, search:false,formatter:showStatus}],\r\n");
      out.write("\tjsonReader: {\r\n");
      out.write("\t    repeatitems: false\r\n");
      out.write("\t},\r\n");
      out.write("\tprmNames:{page:\"requestedPage\",rows:\"pageSize\",sort:\"sortField\",order:\"sortOrder\",id:\"id\",nd:null,search:null},\r\n");
      out.write("\tpager:\"#pager\"});\r\n");
      out.write("\t\r\n");
      out.write("\t$('#grid').jqGrid('navGrid','#pager',\r\n");
      out.write("\t\t\t\t\t{\"edit\":false,\"add\":false,\"del\":false,\"search\":true,\"refresh\":true,\"view\":false,\"excel\":false}, \t\t   // options\r\n");
      out.write("\t\t\t\t\t{\"drag\":true,\"resize\":true,\"closeOnEscape\":true,\"dataheight\":150,\"mtype\":\"POST\",reloadAfterSubmit:false},  // edit\r\n");
      out.write("\t\t\t\t\t{\"drag\":true,\"resize\":true,\"closeOnEscape\":true,\"dataheight\":150,\"mtype\":\"POST\",reloadAfterSubmit:false},  // add\r\n");
      out.write("\t\t\t\t\t{\"drag\":true,\"resize\":true,\"closeOnEscape\":true,\"dataheight\":150,\"mtype\":\"POST\",reloadAfterSubmit:false},  // del\r\n");
      out.write("\t\t\t\t\t{\"drag\":true,\"closeAfterSearch\":true,\"afterShowSearch\":setFocus, sopt: ['cn','bw','ew','eq'],sOper:\"searchType\",\"closeOnEscape\":true,\"dataheight\":150}) // search\r\n");
      out.write("\t\t\t.navSeparatorAdd(\"#pager\",{})\r\n");
      out.write("\t\t\t.navButtonAdd('#pager',{\r\n");
      out.write("\t\t\t\t\tcaption:\"Delete\", \r\n");
      out.write("   \t\t\t\t\tbuttonicon:\"ui-icon-trash\", \r\n");
      out.write("   \t\t\t\t\tonClickButton: function(){\r\n");
      out.write("   \t\t\t\t\t\tif($(\"input[name='applicationId']:checked\").val() > 0){\r\n");
      out.write("   \t\t\t\t\t\t\tdeleteApp(\"Are you sure you want to delete ?\",$(\"input[name='applicationId']:checked\").val());\r\n");
      out.write("   \t\t\t\t\t\t} else {\r\n");
      out.write("   \t\t\t\t\t\t\talert(\"Please select a record to delete\");\r\n");
      out.write("   \t\t\t\t\t\t}\r\n");
      out.write("   \t\t\t\t\t}, \r\n");
      out.write("   \t\t\t\t\tposition:\"last\"\r\n");
      out.write("\t\t\t\t\t}); \r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#fbox_grid input[type='text']\").live(\"keydown\",function(e){ \r\n");
      out.write("\t\t\t\t\t\t\tif(e.keyCode==13 || e.which==13){\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#fbox_grid .ui-search\").click();\t\r\n");
      out.write("\t\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\t });\r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t}else{\r\n");
      out.write("\t\t  // $('#flowDiagramDiv').show();\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t$(\"input[name='applicationId']\").live(\"click\",function(){ \r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t   \r\n");
      out.write("\t\t\tvar applId=$(this).attr(\"applId\");\t\r\n");
      out.write("\t\t\tvar appName=$(this).attr(\"appName\");\r\n");
      out.write("\t\t\tvar modId=$(this).attr(\"modId\");\r\n");
      out.write("\t\t\tvar appSrcType=$(this).attr(\"appSrcType\");\r\n");
      out.write("\t\t\t setSelected(applId, appName, modId, appSrcType); });\r\n");
      out.write(" });\r\n");
      out.write("var sDesc=\"shortDesc\";\r\n");
      out.write("var lDescClose=\"longDescClose\";\r\n");
      out.write("var currentStatus=\"\";\r\n");
      out.write("var currentDiv=\"\";\r\n");
      out.write("function setFocus(){\r\n");
      out.write("$(\"#fbox_grid input[type='text']\").focus();\r\n");
      out.write("}\r\n");
      out.write("function showMode(cellvalue, options, rowObject){\r\n");
      out.write("var $metaDiv = $(\"<div ></div>\");\r\n");
      out.write("\tvar divId=\"mode\"+rowObject.id;\r\n");
      out.write("\tvar $innerDiv = $(\"<div id='\"+divId+\"' ></div>\");\r\n");
      out.write("\t\r\n");
      out.write("var $a=$(\"<a href='../swi/showPublishApp.action?applicationId=\"+rowObject.id+\"'></a>\"); \r\n");
      out.write("\tif(cellvalue!=\"None\"){\r\n");
      out.write("\t$innerDiv.append($a);\t$a.append(cellvalue);\r\n");
      out.write("\t}else{\r\n");
      out.write("\t$innerDiv.append(cellvalue);\t\r\n");
      out.write("\t}\r\n");
      out.write("\t\r\n");
      out.write("\t$metaDiv.append($innerDiv);\r\n");
      out.write("return $metaDiv.html();\t\r\n");
      out.write("}\r\n");
      out.write("function showMetadataLink(cellvalue, options, rowObject){\r\n");
      out.write("\tvar $metaDiv = $(\"<div ></div>\");\r\n");
      out.write("\tvar divId=\"meta\"+rowObject.id;\r\n");
      out.write("\tvar $innerDiv = $(\"<div id='\"+divId+\"' ></div>\");\r\n");
      out.write("\tvar $a=$(\"<a href='../publisher/editMetadata.action?applicationId=\"+rowObject.id+\"'></a>\"); \r\n");
      out.write("\tvar $editIcon=$('<img height=\"17\" border=\"0\" style=\"margin-left:15px;\" title=\"Edit\" alt=\"Edit\" src=\"../images/admin/editIcon.gif\">');\r\n");
      out.write("\t$a.append($editIcon);\r\n");
      out.write("\tif(cellvalue){ \r\n");
      out.write("\t\t$innerDiv.append($a);\r\n");
      out.write("\t}else{\r\n");
      out.write("\t\t$editIcon.attr(\"src\",\"../images/admin/editIconfaded.gif\");\r\n");
      out.write("\t\t$innerDiv.append($editIcon);\r\n");
      out.write("\t\t\r\n");
      out.write("\t}\r\n");
      out.write("\t\r\n");
      out.write("\t$metaDiv.append($innerDiv);\r\n");
      out.write("\treturn $metaDiv.html();\r\n");
      out.write("}\r\n");
      out.write("function showShortDesc(cellvalue, options, rowObject){\r\n");
      out.write("\tvar $descDiv = $(\"<div ></div>\");\r\n");
      out.write("\tif(cellvalue!=null && cellvalue!=\"\"){\r\n");
      out.write("\tvar $shortDescDiv = $(\"<div name='shortDesc' ></div>\");\r\n");
      out.write("\tvar $longDescDiv = $(\"<div name='longDesc' style='display:none;z-index:inherit;position:absolute;width:400px;height:60px;overflow-y:auto;min-height:50px;border:2px solid #ccc;background-color:#CFDDDD;color:#000;white-space:normal;padding:5px;' ></div>\");\r\n");
      out.write("\tvar $longDescContent= $(\"<div style='margin-right:20px;' ></div>\");\r\n");
      out.write("\tvar $longDescContentCloseButton= $(\"<div style='width:16px;float:right;color:#fff;font-weight:bold' ></div>\");\r\n");
      out.write("\t$longDescContentCloseButton.append(\"<a style='clocr:#fff;font-weight:bold;' id='\"+lDescClose+rowObject.id+\"' href=javascript:closeDesc('\"+rowObject.id+\"');><img border='0' src='../images/admin/closeButtonForMessage.png' /></a>\");\r\n");
      out.write("\t$descDiv.append($shortDescDiv);\r\n");
      out.write("\t$descDiv.append($longDescDiv);\r\n");
      out.write("\tvar shortDesc=cellvalue.substring(0,60);\r\n");
      out.write("\t\r\n");
      out.write("\tif(cellvalue.length>60){\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tshortDesc=cellvalue.substring(0,60)+\"...<a id='\"+sDesc+rowObject.id+\"' href=javascript:showDesc('\"+rowObject.id+\"');>More</a>\";\r\n");
      out.write("\t\t$longDescContent.append(cellvalue);\r\n");
      out.write("\t\t$longDescDiv.append($longDescContentCloseButton);\r\n");
      out.write("\t\t$longDescDiv.append($longDescContent);\r\n");
      out.write("\t}\r\n");
      out.write("\t$shortDescDiv.append(shortDesc);\r\n");
      out.write("\t}\r\n");
      out.write("\treturn $descDiv.html();\r\n");
      out.write("}\r\n");
      out.write("function closeDesc(id){\r\n");
      out.write("$longDescDiv=$(\"#\"+sDesc+id).parent().next();\t\r\n");
      out.write("$longDescDiv.hide();\r\n");
      out.write("}\r\n");
      out.write("function showDesc(id){\r\n");
      out.write("\t$(\"div[name='longDesc']\").hide();\r\n");
      out.write("$longDescDiv=$(\"#\"+sDesc+id).parent().next();\r\n");
      out.write("$longDescDiv.show();\r\n");
      out.write("top=$(\"#\"+id).position().top; \r\n");
      out.write("//gridTop=$(\"#gbox_grid\").position().top;\r\n");
      out.write("//alert(top+\"::\"+gridTop);\r\n");
      out.write("if(top<=90){\r\n");
      out.write("$longDescDiv.css(\"top\",top+\"px\");\r\n");
      out.write("}else{\r\n");
      out.write("\t$longDescDiv.css(\"top\",top-40+\"px\");\r\n");
      out.write("}\r\n");
      out.write("}\r\n");
      out.write("function showRadio(cellvalue, options, rowObject){\r\n");
      out.write("\t$radioDiv = $(\"<div ></div>\");\r\n");
      out.write("\t$table=$(\"<table border='0'></table>\");\r\n");
      out.write("\t$tr=$(\"<tr></tr>\");\r\n");
      out.write("\t$table.append($tr);\r\n");
      out.write("\t$radioSelectDiv = $(\"<td style='width:20px; float:left; margin-left:2px;border:none;padding-top:5px;height:15px;'></td>\");\r\n");
      out.write("\tif(applIdFromSession == rowObject.id)\r\n");
      out.write("\t\t$radioSelect = $(\"<input type='radio' checked  name='applicationId' value='\"+rowObject.id+\"' applId='\"+rowObject.id+\"'  appName='\"+rowObject.name+\"' modId='\"+rowObject.modelId+\"' appSrcType='\"+rowObject.appSourceType+\"' />\");\t\r\n");
      out.write("\telse\r\n");
      out.write("\t\t$radioSelect = $(\"<input type='radio'  name='applicationId' value='\"+rowObject.id+\"' applId='\"+rowObject.id+\"'  appName='\"+rowObject.name+\"' modId='\"+rowObject.modelId+\"' appSrcType='\"+rowObject.appSourceType+\"' />\");\t\r\n");
      out.write("\t$radioSelectDiv.append($radioSelect);\r\n");
      out.write("\t$radioDataDiv = $(\"<td style='float:left;border:none;padding-top:5px;height:15px;'></td>\");\r\n");
      out.write("\t$rowData = $(\"<a href='../swi/showApplications.action?application.id=\"+rowObject.id+\"'>\"+cellvalue+\"</a>\");\r\n");
      out.write("\t$radioDataDiv.append($rowData);\r\n");
      out.write("\t$radioDiv.append($table);\r\n");
      out.write("\t$tr.append($radioSelectDiv);\r\n");
      out.write("\t$tr.append($radioDataDiv);\r\n");
      out.write("\r\n");
      out.write("\treturn $radioDiv.html();\r\n");
      out.write("}\r\n");
      out.write("function showDelStatus(cellvalue, options, rowObject){\r\n");
      out.write("\t//alert(rowObject.statusLink);\r\n");
      out.write("\t$mainDiv = $(\"<div>\");\r\n");
      out.write("\t$rowData = $(\"<div style='width:150px; float:left; white-space:normal;text' id='showStatus\"+rowObject.id+\"'></div>\");\r\n");
      out.write("\t\r\n");
      out.write("\t$statusDiv = $(\"<div style='width:30px; float:left; margin-left:5px;' id='\"+rowObject.id+\"Div'>\");\r\n");
      out.write("\t$statusDiv.append(rowObject.status);\r\n");
      out.write("\t$mainDiv.append($statusDiv);\r\n");
      out.write("\t$mainDiv.append($rowData);\r\n");
      out.write("\treturn $mainDiv.html();\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("function showStatus(cellvalue, options, rowObject){\r\n");
      out.write("\tvar $mainDiv = $(\"<div ></div>\");\r\n");
      out.write("\tvar $outerDiv = $(\"<div style='position:relative' ></div>\");\r\n");
      out.write("\tvar $innerDiv = $(\"<div ></div>\");\r\n");
      out.write("\tvar $a=$('<a href=\"#\" ></a>');\r\n");
      out.write("\tvar operationType=\"\";\r\n");
      out.write("\tvar jobrequestId=100;\r\n");
      out.write("\tvar $statusDiv = $(\"<div style='width:30px; float:left; margin-left:5px;' id='\"+rowObject.id+\"Div'>\");\r\n");
      out.write("\t$a.append(rowObject.status);\r\n");
      out.write("\tif(rowObject.statusLink){ \r\n");
      out.write("\t\t$mainDiv.append($statusDiv);\r\n");
      out.write("\t\t$statusDiv.append($a);\r\n");
      out.write("\t\tif(!rowObject.inProgress){ \r\n");
      out.write("\t\toperationType=rowObject.operationType; //un comment when values are coming\r\n");
      out.write("\t\tjobrequestId=rowObject.jobRequestId; //un comment when values are coming\r\n");
      out.write("\t\t$a.attr(\"href\",\"test.action? operationType=\"+ operationType+\"&jobRequestId=\"+jobrequestId);\r\n");
      out.write("\t\t}else{\r\n");
      out.write("\t\t\tjobrequestId=rowObject.jobRequestId; //un comment when values are coming\r\n");
      out.write("\t\t\t$innerDiv.append($a);\r\n");
      out.write("\t\t\t$innerDiv.attr(\"id\",\"status\"+rowObject.id);\r\n");
      out.write("\t\t\t$outerDiv.append($innerDiv);\r\n");
      out.write("\t\t\t$mainDiv.append($outerDiv);\r\n");
      out.write("\t\t\tvar divId=\"status\"+rowObject.id; \r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$a.attr(\"href\",\"javascript:showJobstatus('\"+jobrequestId+\"');\");\r\n");
      out.write("\t\t\tcurrentStatus=$mainDiv.html(); \r\n");
      out.write("\t\t\tcurrentDiv=rowObject.id;\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t}else{\r\n");
      out.write("\t\t$mainDiv.append($statusDiv.append(rowObject.status));\r\n");
      out.write("\t\t\r\n");
      out.write("\t}\r\n");
      out.write("\t$rowData = $(\"<div style='width:150px; float:left; white-space:normal;text' id='showStatus\"+rowObject.id+\"'></div>\");\r\n");
      out.write("\t$mainDiv.append($rowData);\r\n");
      out.write("\treturn $mainDiv.html();\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("function showJobstatus(jobRequestId){\r\n");
      out.write("\r\n");
      out.write("$(\"#status\"+currentDiv).empty();\t\r\n");
      out.write("$(\"#status\"+currentDiv).jobStatus({\r\n");
      out.write("\t\t//requestJobURL : \"../swi/invokePublishAssetsMaintenaceJob.action\",\r\n");
      out.write("\t\tjobId:jobRequestId,\r\n");
      out.write("\t\tpostCall : function(jobRequestId,status) {updateStatusInfo(jobRequestId,status)}\r\n");
      out.write("\t});\t\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("function updateStatusInfo(jobid,status) {\r\n");
      out.write("\t// specifc code for showing specific\r\n");
      out.write(" \r\n");
      out.write("\tif(status==\"SUCCESS\"){\r\n");
      out.write("\tvar $statusDiv= $(\"#status\"+currentDiv).css(\"textAlign\",\"left\").css(\"marginLeft\",\"5px\");\r\n");
      out.write("\tvar $modeDiv= $(\"#mode\"+currentDiv);\r\n");
      out.write("\tvar $metaDiv= $(\"#meta\"+currentDiv);\r\n");
      out.write("\tvar $aMeta=$(\"<a href='../publisher/editMetadata.action?applicationId=\"+currentDiv+\"'></a>\"); \r\n");
      out.write("\tvar $editIcon=$('<img height=\"17\" border=\"0\" style=\"margin-left:15px;\" title=\"Edit\" alt=\"Edit\" src=\"../images/admin/editIcon.gif\">');\r\n");
      out.write("\t\r\n");
      out.write("\t$aMeta.append($editIcon);\r\n");
      out.write("\t$metaDiv.empty().html($aMeta);\r\n");
      out.write("\t$metaDiv.children(\"a img\").removeClass(\"imgFade\").addClass(\"imgDontFade\");\r\n");
      out.write("\t\r\n");
      out.write("\tvar $aMode=$(\"<a href='../swi/showPublishApp.action?applicationId=\"+currentDiv+\"'>Local</a>\"); \r\n");
      out.write("\t$modeDiv.empty().html($aMode);\r\n");
      out.write("\t\r\n");
      out.write("\t$statusDiv.show();\r\n");
      out.write("\t$statusDiv.html(\"Fulfilled\");\r\n");
      out.write("\t$metaDiv.html();\r\n");
      out.write("\t}else{ \r\n");
      out.write("\t$(\"#status\"+currentDiv).html(currentStatus);\r\n");
      out.write("\t}\r\n");
      out.write("\t//$(\"#uploadAnotherFile\").show().css({marginLeft:\"0px\"});\r\n");
      out.write("}\r\n");
      out.write(" </SCRIPT>\r\n");
      out.write("<BR>");
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

  private boolean _jspx_meth_s_005ftext_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:text
    org.apache.struts2.views.jsp.TextTag _jspx_th_s_005ftext_005f0 = (org.apache.struts2.views.jsp.TextTag) _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.TextTag.class);
    _jspx_th_s_005ftext_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftext_005f0.setParent(null);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(35,106) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f0.setName("execue.global.locate");
    int _jspx_eval_s_005ftext_005f0 = _jspx_th_s_005ftext_005f0.doStartTag();
    if (_jspx_th_s_005ftext_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f0);
    return false;
  }

  private boolean _jspx_meth_s_005ftext_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:text
    org.apache.struts2.views.jsp.TextTag _jspx_th_s_005ftext_005f1 = (org.apache.struts2.views.jsp.TextTag) _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.TextTag.class);
    _jspx_th_s_005ftext_005f1.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftext_005f1.setParent(null);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(35,213) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f1.setName("execue.global.describe");
    int _jspx_eval_s_005ftext_005f1 = _jspx_th_s_005ftext_005f1.doStartTag();
    if (_jspx_th_s_005ftext_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f1);
    return false;
  }

  private boolean _jspx_meth_s_005ftext_005f2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:text
    org.apache.struts2.views.jsp.TextTag _jspx_th_s_005ftext_005f2 = (org.apache.struts2.views.jsp.TextTag) _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.TextTag.class);
    _jspx_th_s_005ftext_005f2.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftext_005f2.setParent(null);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(35,325) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f2.setName("execue.global.publish");
    int _jspx_eval_s_005ftext_005f2 = _jspx_th_s_005ftext_005f2.doStartTag();
    if (_jspx_th_s_005ftext_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f2);
    return false;
  }

  private boolean _jspx_meth_s_005ftext_005f3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:text
    org.apache.struts2.views.jsp.TextTag _jspx_th_s_005ftext_005f3 = (org.apache.struts2.views.jsp.TextTag) _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.TextTag.class);
    _jspx_th_s_005ftext_005f3.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftext_005f3.setParent(null);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(57,48) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f3.setName("execue.search.apps.dashboard");
    int _jspx_eval_s_005ftext_005f3 = _jspx_th_s_005ftext_005f3.doStartTag();
    if (_jspx_th_s_005ftext_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f3);
    return false;
  }

  private boolean _jspx_meth_s_005fif_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f0 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f0.setParent(null);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(81,12) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f0.setTest("menuSelection.menuType.value != 1");
    int _jspx_eval_s_005fif_005f0 = _jspx_th_s_005fif_005f0.doStartTag();
    if (_jspx_eval_s_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f0.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t\t\t<tr>\r\n");
        out.write("\t\t\t\t\t\t\t\t\t\t<td align=\"left\" height=\"22\" style=\"white-space: nowrap;\"><a\r\n");
        out.write("\t\t\t\t\t\t\t\t\t\t\tstyle=\"font-size: 14px\" href=\"showApplications.action\">");
        if (_jspx_meth_s_005ftext_005f4(_jspx_th_s_005fif_005f0, _jspx_page_context))
          return true;
        out.write("</a></td>\r\n");
        out.write("\t\t\t\t\t\t\t\t\t</tr>\r\n");
        out.write("\t\t\t\t\t\t\t\t\t");
        int evalDoAfterBody = _jspx_th_s_005fif_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f0);
    return false;
  }

  private boolean _jspx_meth_s_005ftext_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:text
    org.apache.struts2.views.jsp.TextTag _jspx_th_s_005ftext_005f4 = (org.apache.struts2.views.jsp.TextTag) _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.TextTag.class);
    _jspx_th_s_005ftext_005f4.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftext_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f0);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(84,66) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f4.setName("execue.create.app");
    int _jspx_eval_s_005ftext_005f4 = _jspx_th_s_005ftext_005f4.doStartTag();
    if (_jspx_th_s_005ftext_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f4);
    return false;
  }

  private boolean _jspx_meth_s_005fif_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f1 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f1.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f1.setParent(null);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(88,9) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f1.setTest("menuSelection.menuType.value == 1");
    int _jspx_eval_s_005fif_005f1 = _jspx_th_s_005fif_005f1.doStartTag();
    if (_jspx_eval_s_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f1.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t\t\t<tr>\r\n");
        out.write("\t\t\t\t\t\t\t\t\t\t<td align=\"center\" style=\"padding-bottom: 5px;\">\r\n");
        out.write("\t\t\t\t\t\t\t\t\t\t<div style=\"width:955px;\"> \r\n");
        out.write("                                       \r\n");
        out.write("                                        <div style=\"width:830px;float:left;text-align:left;margin-top:3px;margin-bottom:3px;\">\r\n");
        out.write("                                        <a id=\"createAppFromFileLink\" href=\"#\" onclick=\"javascript:createAppFromFile();\" style=\"font-size: 14px;\">");
        if (_jspx_meth_s_005ftext_005f5(_jspx_th_s_005fif_005f1, _jspx_page_context))
          return true;
        out.write(" </a>\r\n");
        out.write("                                        </div>\r\n");
        out.write("                                        \r\n");
        out.write("\r\n");
        out.write("                                        \r\n");
        out.write("\t\t\t\t\t\t\t\t\t\t\t</div>\r\n");
        out.write("\t\t\t\t\t\t\t\t\t\t\t</td>\r\n");
        out.write("\t\t\t\t\t\t\t\t\t</tr>\r\n");
        out.write("\t\t\t\t\t\t\t\t\t");
        int evalDoAfterBody = _jspx_th_s_005fif_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f1);
    return false;
  }

  private boolean _jspx_meth_s_005ftext_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:text
    org.apache.struts2.views.jsp.TextTag _jspx_th_s_005ftext_005f5 = (org.apache.struts2.views.jsp.TextTag) _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.TextTag.class);
    _jspx_th_s_005ftext_005f5.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftext_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f1);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(94,146) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f5.setName("execue.upload.main.app");
    int _jspx_eval_s_005ftext_005f5 = _jspx_th_s_005ftext_005f5.doStartTag();
    if (_jspx_th_s_005ftext_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f5);
    return false;
  }

  private boolean _jspx_meth_s_005fif_005f2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f2 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f2.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f2.setParent(null);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(114,6) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f2.setTest("selectApp");
    int _jspx_eval_s_005fif_005f2 = _jspx_th_s_005fif_005f2.doStartTag();
    if (_jspx_eval_s_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f2.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t<div id=\"pleaseSelectApplication\" style=\"padding-bottom:5px;\">");
        if (_jspx_meth_s_005ftext_005f6(_jspx_th_s_005fif_005f2, _jspx_page_context))
          return true;
        out.write("</div>\r\n");
        out.write("\t\t\t\t\t\t");
        int evalDoAfterBody = _jspx_th_s_005fif_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f2);
    return false;
  }

  private boolean _jspx_meth_s_005ftext_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:text
    org.apache.struts2.views.jsp.TextTag _jspx_th_s_005ftext_005f6 = (org.apache.struts2.views.jsp.TextTag) _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.TextTag.class);
    _jspx_th_s_005ftext_005f6.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftext_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f2);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(115,69) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f6.setName("execue.select.application");
    int _jspx_eval_s_005ftext_005f6 = _jspx_th_s_005ftext_005f6.doStartTag();
    if (_jspx_th_s_005ftext_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f6);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f0 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f0.setParent(null);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(152,25) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(153,20) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f1.setValue("applications.size");
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
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(154,17) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f2.setValue("isPublisher");
    int _jspx_eval_s_005fproperty_005f2 = _jspx_th_s_005fproperty_005f2.doStartTag();
    if (_jspx_th_s_005fproperty_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f2);
    return false;
  }

  private boolean _jspx_meth_s_005ftext_005f7(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:text
    org.apache.struts2.views.jsp.TextTag _jspx_th_s_005ftext_005f7 = (org.apache.struts2.views.jsp.TextTag) _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.TextTag.class);
    _jspx_th_s_005ftext_005f7.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftext_005f7.setParent(null);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(201,38) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f7.setName("execue.app.name");
    int _jspx_eval_s_005ftext_005f7 = _jspx_th_s_005ftext_005f7.doStartTag();
    if (_jspx_th_s_005ftext_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f7);
    return false;
  }

  private boolean _jspx_meth_s_005ftext_005f8(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:text
    org.apache.struts2.views.jsp.TextTag _jspx_th_s_005ftext_005f8 = (org.apache.struts2.views.jsp.TextTag) _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.TextTag.class);
    _jspx_th_s_005ftext_005f8.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftext_005f8.setParent(null);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(201,73) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f8.setName("execue.app.description");
    int _jspx_eval_s_005ftext_005f8 = _jspx_th_s_005ftext_005f8.doStartTag();
    if (_jspx_th_s_005ftext_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f8);
    return false;
  }

  private boolean _jspx_meth_s_005ftext_005f9(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:text
    org.apache.struts2.views.jsp.TextTag _jspx_th_s_005ftext_005f9 = (org.apache.struts2.views.jsp.TextTag) _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.TextTag.class);
    _jspx_th_s_005ftext_005f9.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftext_005f9.setParent(null);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(201,127) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f9.setName("execue.publish.mode");
    int _jspx_eval_s_005ftext_005f9 = _jspx_th_s_005ftext_005f9.doStartTag();
    if (_jspx_th_s_005ftext_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f9);
    return false;
  }

  private boolean _jspx_meth_s_005ftext_005f10(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:text
    org.apache.struts2.views.jsp.TextTag _jspx_th_s_005ftext_005f10 = (org.apache.struts2.views.jsp.TextTag) _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.TextTag.class);
    _jspx_th_s_005ftext_005f10.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftext_005f10.setParent(null);
    // /WEB-INF/jsp/admin/swi/sdx/searchAppsDashboard.jsp(201,166) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f10.setName("execue.global.status");
    int _jspx_eval_s_005ftext_005f10 = _jspx_th_s_005ftext_005f10.doStartTag();
    if (_jspx_th_s_005ftext_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f10);
    return false;
  }
}
