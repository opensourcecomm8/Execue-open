package org.apache.jsp.views.main.search;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.execue.core.common.bean.Pagination;
import com.execue.web.core.util.ExecueWebConstants;

public final class qi_002dhead_002dsimpleSearch_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.release();
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
      response.setContentType("text/html; charset=ISO-8859-1");
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
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      //  c:set
      org.apache.taglibs.standard.tag.rt.core.SetTag _jspx_th_c_005fset_005f0 = (org.apache.taglibs.standard.tag.rt.core.SetTag) _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.SetTag.class);
      _jspx_th_c_005fset_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fset_005f0.setParent(null);
      // /views/main/search/qi-head-simpleSearch.jsp(8,0) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fset_005f0.setVar("basePath");
      // /views/main/search/qi-head-simpleSearch.jsp(8,0) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fset_005f0.setValue(request.getContextPath());
      int _jspx_eval_c_005fset_005f0 = _jspx_th_c_005fset_005f0.doStartTag();
      if (_jspx_th_c_005fset_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f0);
      out.write("\r\n");
      out.write("\r\n");
      //  c:set
      org.apache.taglibs.standard.tag.rt.core.SetTag _jspx_th_c_005fset_005f1 = (org.apache.taglibs.standard.tag.rt.core.SetTag) _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.SetTag.class);
      _jspx_th_c_005fset_005f1.setPageContext(_jspx_page_context);
      _jspx_th_c_005fset_005f1.setParent(null);
      // /views/main/search/qi-head-simpleSearch.jsp(10,0) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fset_005f1.setVar("adminPath");
      // /views/main/search/qi-head-simpleSearch.jsp(10,0) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fset_005f1.setValue(application.getAttribute(ExecueWebConstants.ADMIN_CONTEXT));
      int _jspx_eval_c_005fset_005f1 = _jspx_th_c_005fset_005f1.doStartTag();
      if (_jspx_th_c_005fset_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f1);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f1);
      out.write('\r');
      out.write('\n');

   String baseURL = "";
   response.setHeader("Pragma", "No-cache");
   response.setDateHeader("Expires", 0);
   response.setHeader("Cache-Control", "no-cache");
   response.setHeader("Cache-Control", "no-store");
   response.addHeader("Cache-Control", "post-check=0, pre-check=0");
   response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
   
   Pagination pagination = (Pagination) request.getAttribute("PAGINATION");
   String requestedPage = "";
   String totalRecrods = "";
   if (pagination != null) {
      baseURL = pagination.getBaseURL();
      requestedPage = pagination.getRequestedPage();
      totalRecrods = pagination.getPageCount();
   }
      

      out.write("\r\n");
      out.write("\r\n");
      //  c:set
      org.apache.taglibs.standard.tag.rt.core.SetTag _jspx_th_c_005fset_005f2 = (org.apache.taglibs.standard.tag.rt.core.SetTag) _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.SetTag.class);
      _jspx_th_c_005fset_005f2.setPageContext(_jspx_page_context);
      _jspx_th_c_005fset_005f2.setParent(null);
      // /views/main/search/qi-head-simpleSearch.jsp(31,0) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fset_005f2.setVar("basePath");
      // /views/main/search/qi-head-simpleSearch.jsp(31,0) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fset_005f2.setValue(request.getContextPath());
      int _jspx_eval_c_005fset_005f2 = _jspx_th_c_005fset_005f2.doStartTag();
      if (_jspx_th_c_005fset_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f2);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f2);
      out.write("\r\n");
      out.write("<link href=\"");
      if (_jspx_meth_c_005fout_005f0(_jspx_page_context))
        return;
      out.write("/css/common/qiStyle_new.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n");
      out.write("<link rel=\"stylesheet\" href=\"");
      if (_jspx_meth_c_005fout_005f1(_jspx_page_context))
        return;
      out.write("/css/common/roundedSearch.css\" type=\"text/css\" />\r\n");
      out.write("<link rel=\"stylesheet\" href=\"");
      if (_jspx_meth_c_005fout_005f2(_jspx_page_context))
        return;
      out.write("/css/main/jquery.autocomplete.css\" type=\"text/css\" />\r\n");
      out.write("<script language=\"JavaScript\" src=\"");
      if (_jspx_meth_c_005fout_005f3(_jspx_page_context))
        return;
      out.write("/js/common/goog_analytics.js\"></script>\r\n");
      out.write("<script language=\"JavaScript\" src=\"");
      if (_jspx_meth_c_005fout_005f4(_jspx_page_context))
        return;
      out.write("/js/common/jquery.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      if (_jspx_meth_c_005fout_005f5(_jspx_page_context))
        return;
      out.write("/js/common/jquery.ui.all.js\"></script>\r\n");
      out.write("<script type='text/javascript'\r\n");
      out.write("\tsrc='");
      if (_jspx_meth_c_005fout_005f6(_jspx_page_context))
        return;
      out.write("/js/main/qi/jquery.autocomplete.freeform.js'></script>\r\n");
      out.write("    \r\n");
      out.write("<script><!--\r\n");
      out.write("var autoSuggestClicked=false;\r\n");
      out.write("var tabIn=0;\r\n");
      out.write("var xmlQueryData;\r\n");
      out.write("var toggle=true;\r\n");
      out.write("var selectTextProcessed;\r\n");
      out.write("var processing=false;\r\n");
      out.write("var appCount='");
      if (_jspx_meth_s_005ftext_005f0(_jspx_page_context))
        return;
      out.write("'\r\n");
      out.write("\r\n");
      out.write("var str='");
      if (_jspx_meth_s_005ftext_005f1(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("var disableCache=false;\r\n");
      out.write("var reSemantificationProcess=false;\r\n");
      out.write("var oldQuery=\"\";\r\n");
      out.write("$(document).ready(function(){\r\n");
      out.write("     var appId='");
      if (_jspx_meth_s_005fproperty_005f0(_jspx_page_context))
        return;
      out.write("';  \r\n");
      out.write("     if(appId){\r\n");
      out.write("      str='");
      if (_jspx_meth_s_005ftext_005f2(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("     }            \r\n");
      out.write("\tif($(\"#metrics\").val()==\"\"){\r\n");
      out.write("\t\t$(\"#metrics\").val(str);\t\r\n");
      out.write("\t\t$(\"#metrics\").blur();\r\n");
      out.write("\t\t//setTimeout(function(){  $(\"#metrics\").blur();},100);\r\n");
      out.write("\t}\r\n");
      out.write("$(\"#metrics\").focus(function(){ \r\n");
      out.write("\t\t\t\t\t\t\t if($(\"#metrics\").val()==str){\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#metrics\").val(\"\"); \r\n");
      out.write("\t\t\t\t\t\t\t }\r\n");
      out.write("\t\t\t\t\t });\r\n");
      out.write("$(\"#metrics\").blur(function(){\r\n");
      out.write("\t\t\t\t\t\t\tif($(\"#metrics\").val()==\"\"){\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#metrics\").val(str); \t\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#noLink\").focus();\r\n");
      out.write("\t\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\t\t });\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\r\n");
      out.write(" });\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("function refreshClicked(){\r\n");
      out.write("disableCache=true; \r\n");
      out.write("$(\"#searchBtnFreeForm\").click();\r\n");
      out.write("}\r\n");
      out.write("function submitDataFreeForm(st) {\r\n");
      out.write("\t$(\"div.ac_results\").hide();\r\n");
      out.write("\t var $appId=$(\"#applicationId\").val(); \t\r\n");
      out.write("\ttempImg = $(\"#waiting_img\").attr(\"src\");\r\n");
      out.write("    var data=$.trim($(\"#metrics\").val());    \r\n");
      out.write("    var type=$(\"#type\").val(); \r\n");
      out.write("\tif((data==\"\")||(data.indexOf(str)>-1)){\r\n");
      out.write("\t\talert(\"Please Enter Data\"); processing=false; $(\"#metrics\").focus(); $(\"#searchBtnFreeForm\").removeClass(\"imgBorder1\");\r\n");
      out.write("\t\t\t\t\t\t$(\"#searchBtnFreeForm\").addClass(\"imgNoBorder1\"); return false}\r\n");
      out.write("\tif(disableCache){$(\"#disableCache\").val('true'); }\r\n");
      out.write("\t//TODO-JT- need to read appId from the configration\r\n");
      out.write("\tif($appId == 1508){\r\n");
      out.write("\t   $('#requestString').val(data);\r\n");
      out.write("\t}else{\t \r\n");
      out.write("\t   $('#request').val(data);\r\n");
      out.write("\t}\r\n");
      out.write("\t$('#type').val(type);\t\r\n");
      out.write("\t$(\"#pleaseWaitDiv\").show();\r\n");
      out.write("\t$(\"#showLoaderPopup\").show();\r\n");
      out.write("\tsetTimeout('document.images[\"waiting_img\"].src = tempImg', 1); \r\n");
      out.write("\tsetTimeout(function(){$(\"div.ac_results\").hide();},1000);\r\n");
      out.write("\t$(\"#waitImage\").css(\"left\",$(\"#metrics\").position().left+190+\"px\");\r\n");
      out.write("\t$(\"#waitImage\").css(\"top\",$(\"#metrics\").position().top+\"px\");\r\n");
      out.write("\tif (navigator.appVersion.indexOf(\"MSIE 7.\") != -1 && st){\r\n");
      out.write("\t$(\"#waitImage\").css(\"left\",$(\"#metrics\").position().left+230+\"px\")\r\n");
      out.write("\t}\r\n");
      out.write("\tif(navigator.appVersion.indexOf(\"MSIE 7.\") != -1 && !st){\r\n");
      out.write("\t$(\"#waitImage\").css(\"left\",$(\"#metrics\").position().left-690+\"px\")\r\n");
      out.write("\t}\r\n");
      out.write("\tif(navigator.appVersion.indexOf(\"MSIE 7.\") != -1 && st==\"app\"){\r\n");
      out.write("\t$(\"#waitImage\").css(\"left\",$(\"#metrics\").position().left-490+\"px\")\r\n");
      out.write("\t}\r\n");
      out.write("\tif(!processing){\r\n");
      out.write("\tprocessing=true; \r\n");
      out.write("\treturn true;\r\n");
      out.write("\t}\r\n");
      out.write("\telse{\r\n");
      out.write("\t\t$(\"#underProcess\").show();\r\n");
      out.write("\t\t$(\"#underProcess\").css(\"left\",$(\"#metrics\").position().left+100+\"px\");\r\n");
      out.write("\t$(\"#underProcess\").css(\"top\",$(\"#metrics\").position().top-20+\"px\");\r\n");
      out.write("\t\r\n");
      out.write("\treturn false;\r\n");
      out.write("\t}\r\n");
      out.write("}\r\n");
      out.write("function hasImageCheck(){\r\n");
      out.write("var hasImage=$(\"#imagePresent\").is(\":checked\");\r\n");
      out.write("\tif(hasImage){\r\n");
      out.write("\treturn \"true\";\r\n");
      out.write("\t}else{\r\n");
      out.write("\treturn \"false\";\t\r\n");
      out.write("\t}\t\r\n");
      out.write("}\r\n");
      out.write("function submitDataUnstructuredFreeForm() { \r\n");
      out.write("\r\n");
      out.write("\t//$(\"#userReqZip\").val($zip);\t\r\n");
      out.write("\t//$(\"#selectedDVDLimit\").val($(\"#selectedDefaultVicinityDistanceLimit\").val());\r\n");
      out.write("\t//var hasImg = hasImageCheck();\r\n");
      out.write("\t//$(\"#imgPresent\").val(hasImg);\t\r\n");
      out.write("\t//var selectedSortType=$(\"#udxCarsInfoSortTypeId option:selected\").val();\t\r\n");
      out.write("\t//$(\"#udxCarSortTypeId\").val(selectedSortType);\r\n");
      out.write("\t//alert(reSemantificationProcess);\r\n");
      out.write("\tif(reSemantificationProcess){\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tjConfirm('Are you sure you want to stop resemantification ?', 'Confirmation Dialog', \r\n");
      out.write("\t\t\t\t\r\n");
      out.write("\t\t\t\t    function(r) {\r\n");
      out.write("\t\t\t\t\tif(r==true)\r\n");
      out.write("\t\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t    $(\"#box\").fadeOut(300);\r\n");
      out.write("\t\t\t\t\t\treSemantificationProcess=false;\r\n");
      out.write("\t\t\t\t\t\t$(\"#searchBtnFreeForm\").click();\r\n");
      out.write("\t\t\t\t\t}else{\r\n");
      out.write("\t\t\t\t\t\t$(\"#metrics\").val(oldQuery);\r\n");
      out.write("\t\t\t\t\t\treturn false;\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t});\r\n");
      out.write("\t\t\r\n");
      out.write("\t\treturn false;\r\n");
      out.write("\t}else{\r\n");
      out.write("\t$(\"div.ac_results\").hide();\r\n");
      out.write("\ttempImg = $(\"#waiting_img\").attr(\"src\");\r\n");
      out.write("    var data=$(\"#metrics\").val();    \r\n");
      out.write("    var type=$(\"#type\").val();\r\n");
      out.write("\tif((data==\"\")||(data.indexOf(str)>-1)){\r\n");
      out.write("\t\talert(\"Please Enter Data\"); processing=false; $(\"#metrics\").focus(); $(\"#searchBtnFreeForm\").removeClass(\"imgBorder1\");\r\n");
      out.write("\t\t\t\t\t\t$(\"#searchBtnFreeForm\").addClass(\"imgNoBorder1\"); \r\n");
      out.write("\t\treturn false\r\n");
      out.write("\t}\r\n");
      out.write("\tif(disableCache){\r\n");
      out.write("\t   $(\"#disableCache\").val('true'); \r\n");
      out.write("\t}\r\n");
      out.write("\t$('#request').val(data);\r\n");
      out.write("\t$('#type').val(type);\r\n");
      out.write("\t$(\"#selectedDVDLimit\").val($(\"#distance\").val()); //$(\"#selectedDefaultVicinityDistanceLimit\").val()); // selectedDefaultVicinityDistanceLimit changed to distance\r\n");
      out.write("\tvar hasImg = hasImageCheck();\r\n");
      out.write("\t$(\"#imgPresent\").val(hasImg);\r\n");
      out.write("\t\r\n");
      out.write("\t$(\"#locationSuggestTerm_locationBedId\").val($(\"#locationBedId\").val());\r\n");
      out.write("\t$(\"#locationSuggestTerm_latitude\").val($(\"#latitude\").val());\r\n");
      out.write("\t$(\"#locationSuggestTerm_longitude\").val($(\"#longitude\").val());\r\n");
      out.write("\t$(\"#locationSuggestTerm_id\").val($(\"#locationId\").val());\r\n");
      out.write("\t$(\"#locationSuggestTerm_displayName\").val($(\"#location\").val());\r\n");
      out.write("\t//$('#query').hide();\r\n");
      out.write("\t//hideAll();\r\n");
      out.write("\t//$(\"#showAllContainer\").hide();\r\n");
      out.write("\t$(\"#pleaseWaitDiv\").show();\r\n");
      out.write("\tsetTimeout('document.images[\"waiting_img\"].src = tempImg', 1); \r\n");
      out.write("\tsetTimeout(function(){$(\"div.ac_results\").hide();},1000);\r\n");
      out.write("\t$(\"#waitImage\").css(\"left\",$(\"#metrics\").position().left+205+\"px\");\r\n");
      out.write("\t$(\"#waitImage\").css(\"top\",$(\"#metrics\").position().top+\"px\");\r\n");
      out.write("\tif(!processing){\r\n");
      out.write("\tprocessing=true;\r\n");
      out.write("\treturn true;\r\n");
      out.write("\t}\r\n");
      out.write("\telse{\r\n");
      out.write("\t\t$(\"#underProcess\").show();\r\n");
      out.write("\t\t$(\"#underProcess\").css(\"left\",$(\"#metrics\").position().left+100+\"px\");\r\n");
      out.write("\t$(\"#underProcess\").css(\"top\",$(\"#metrics\").position().top-20+\"px\");\r\n");
      out.write("\t\t//window.history.go(0); \r\n");
      out.write("\treturn false;\r\n");
      out.write("\t}\r\n");
      out.write("\t\r\n");
      out.write("\t}\r\n");
      out.write("\t/*if(apId==\"1508\"){ //todo this is hard coded id . we need to think other solution\r\n");
      out.write("\t\tvar $formData=$(\"#searchFormId\").serialize(); \r\n");
      out.write("\t\t$.get(\"");
      if (_jspx_meth_c_005fout_005f7(_jspx_page_context))
        return;
      out.write("/semanticSearch.action?\"+$formData,function(data){\r\n");
      out.write("\t\t\t\t\tdisplayDataInDiv(data);\t  \r\n");
      out.write("\t\t\t\t\t\t  });\r\n");
      out.write("\t\treturn false;\r\n");
      out.write("\t}*/\r\n");
      out.write("}\r\n");
      out.write("--></script>\r\n");
      out.write("<script>\r\n");
      out.write("\r\n");
      out.write("function setLeftTop(d,m){\r\n");
      out.write("var margin=10;\r\n");
      out.write("\tmargin=m;\r\n");
      out.write("\t\tvar\t$left=$(d).offset().left;\r\n");
      out.write("\t\tvar\t$top=$(d).offset().top;\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tvar $divWidth=$(d).width();\r\n");
      out.write("\t\tvar $divHeight=$(d).height();\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t$ht=screen.height;\r\n");
      out.write("\t\t$wt=screen.width;\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tvar right=$wt-$divWidth;\r\n");
      out.write("\t\tvar bottom=$ht-$divHeight-200;\r\n");
      out.write("\r\n");
      out.write("\t\tif($top <=margin   ){\r\n");
      out.write("\t\t$(d).css(\"top\",margin+5+\"px\");\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tif( $top >=(bottom) ){\r\n");
      out.write("\t\t$(d).css(\"top\",bottom-10+\"px\");\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tif($left <=margin ){\r\n");
      out.write("\t\t$(d).css(\"left\",margin+5+\"px\");\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tif( $left >= right){\r\n");
      out.write("\t\t$(d).css(\"left\",right-20+\"px\");\r\n");
      out.write("\t\t}\r\n");
      out.write("}\r\n");
      out.write("function stopStartDragging(l,t,d,m){\r\n");
      out.write("\t\r\n");
      out.write("\tvar $divWidth=$(d).width();\r\n");
      out.write("\t\tvar $divHeight=$(d).height();\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t$ht=screen.height;\r\n");
      out.write("\t\t$wt=screen.width;\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tvar right=$wt-$divWidth;\r\n");
      out.write("\t\tvar bottom=$ht-$divHeight-200;\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t \r\n");
      out.write("\t\tif(t <=m  ){\r\n");
      out.write("\t\t $(d).css(\"top\",m+5+\"px\");\r\n");
      out.write("\t\t //$(d).draggable('destroy');\r\n");
      out.write("         startDragging();\r\n");
      out.write("\t\t }\r\n");
      out.write("\t\t \r\n");
      out.write("\t\t \r\n");
      out.write("\t\t if(t>=(bottom)){\r\n");
      out.write("\t\t $(d).css(\"top\",bottom-10+\"px\");\r\n");
      out.write("\t\t// $(d).draggable('destroy');\r\n");
      out.write("         startDragging();\r\n");
      out.write("\t\t }\r\n");
      out.write("\t\t \r\n");
      out.write("\t\t \r\n");
      out.write("\t\tif(l <=m ){\r\n");
      out.write("\t\t $(d).css(\"left\",m+5+\"px\");\r\n");
      out.write("\t\t //$(d).draggable('destroy'); \r\n");
      out.write("         startDragging();\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tif( l>=right){\r\n");
      out.write("\t\t $(d).css(\"left\",right-20+\"px\");\r\n");
      out.write("\t\t// $(d).draggable('destroy'); \r\n");
      out.write("         startDragging();\r\n");
      out.write("\t\t}\r\n");
      out.write("}\r\n");
      out.write("function startDragging(){\r\n");
      out.write("\tdivName='div#hiddenPane';\r\n");
      out.write("\tmargin=10;\r\n");
      out.write("\t\t//setLeftTop(divName,margin);\r\n");
      out.write("\t\tvar\t$left=$(divName).offset().left;\r\n");
      out.write("\t\tvar\t$top=$(divName).offset().top;\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\r\n");
      out.write("\r\n");
      out.write("\t  $(divName).draggable( {      drag:function(){\r\n");
      out.write("\t\t\t\t\t\t$left=$(divName).offset().left;\r\n");
      out.write("\t\t\t\t\t\t$top=$(divName).offset().top;\r\n");
      out.write("\t\t\t\t\t\t$('#left').html($left);\r\n");
      out.write("\t\t\t\t\t\t$('#top').html($top);\r\n");
      out.write("\t\t\t\t\t\tstopStartDragging($left,$top,divName,margin);\r\n");
      out.write("\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t     , stop:function(){\r\n");
      out.write("\t\t\t\t\t\tstopStartDragging($left,$top,divName,margin);\r\n");
      out.write("\t\t\t\t\t\t }\r\n");
      out.write("\t\t\t\t\t\t/*  , start:function(){\r\n");
      out.write("\t\t\t\t\t\t  stopStartDragging($left,$top,divName,margin);\r\n");
      out.write("\t\t\t\t\t\t }*/\r\n");
      out.write("\t\t\t }\t\t\t\t\t\t\t \r\n");
      out.write("\t\t);\r\n");
      out.write(" }\r\n");
      out.write("function doDrag(){\r\n");
      out.write("\r\n");
      out.write(" $(\"#boxTemplate_r1_c2,#boxTemplate_r1_c1,#boxTemplate_r1_c3\").mousedown(startDragging);\r\n");
      out.write(" \r\n");
      out.write("    $(\"#boxTemplate_r1_c2,#boxTemplate_r1_c1\").mouseup(function(){\r\n");
      out.write("\t\t\tsetLeftTop('div#hiddenPane');\r\n");
      out.write("   });\r\n");
      out.write("}\r\n");
      out.write(" \r\n");
      out.write(" \r\n");
      out.write(" autoSelect=false;\r\n");
      out.write(" enterPressed=false;\r\n");
      out.write("\r\n");
      out.write(" \r\n");
      out.write("\r\n");
      out.write("var example;\r\n");
      out.write("var jsonExamples;\r\n");
      out.write("var jsonVerticals;\r\n");
      out.write("\r\n");
      out.write("function showHide(){\r\n");
      out.write("\t$('#queries').hide();\r\n");
      out.write("\t$(\"#metrics\").focus();\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("function getAppExamples(){\r\n");
      out.write("\tvar openTable = '<table cellpadding=\"3\" id=\"queriesDiv\">'\r\n");
      out.write("\t\t\t\t\t+ '<tr><td align=\"left\" style=\"border-bottom:#CCC thin dashed;\">'\r\n");
      out.write("\t\t\t\t\t+ '<span style=\"padding-left:2px;color:#666;float:left;text-align:left;\" >'\r\n");
      out.write("\t\t\t\t\t+ '<b>General Questions</b></span></td></tr>';\r\n");
      out.write("\tvar closeTable = '</table>';\r\n");
      out.write("\t$(\"#queries\").empty();\r\n");
      out.write("\tif(jsonExamples){\r\n");
      out.write("\t\tjsonExamples.abort();\r\n");
      out.write("\t}\r\n");
      out.write("\tvar applicationId='");
      if (_jspx_meth_s_005fproperty_005f1(_jspx_page_context))
        return;
      out.write("';\t\r\n");
      out.write("\tif(applicationId != -1){\r\n");
      out.write("\t\tjsonExamples = $.get(\"");
      if (_jspx_meth_c_005fout_005f8(_jspx_page_context))
        return;
      out.write("/querySuggest/showAppExamples.action?applicationId=\"+applicationId,\r\n");
      out.write("\t\tfunction(data) {\r\n");
      out.write("\t\t    var data=eval(data);\r\n");
      out.write("\t\t    $(\"#queries\").html(openTable);\r\n");
      out.write("\t\t     $.each(data, function(i, query) {\r\n");
      out.write("\t\t    \tprepareExamplesTable(query);\r\n");
      out.write("\t\t    });\r\n");
      out.write("\t\t    $(\"#queries\").append(closeTable);\r\n");
      out.write("\t\t    $(\"#queriesDiv td a\").bind(\"click\",function(){\r\n");
      out.write("\t\t\t\t$(\"#metrics\").val($(this).text());\r\n");
      out.write("\t\t\t\t$(\"#searchBtnFreeForm\").click();\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t  \t});\r\n");
      out.write("\t}else{\r\n");
      out.write("\t\tjsonExamples = $.get(\"");
      if (_jspx_meth_c_005fout_005f9(_jspx_page_context))
        return;
      out.write("/querySuggest/showAppExamples.action\",function(data) {\r\n");
      out.write("\t\t    var data=eval(data);\r\n");
      out.write("\t\t    $(\"#queries\").html(openTable);\t    \r\n");
      out.write("\t\t     $.each(data, function(i, query) {\r\n");
      out.write("\t\t    \tprepareExamplesTable(query); \r\n");
      out.write("\t\t    });\r\n");
      out.write("\t\t    $(\"#queries\").append(closeTable);\r\n");
      out.write("\t\t    $(\"#queriesDiv td a\").bind(\"click\",function(){\r\n");
      out.write("\t\t\t\t$(\"#metrics\").val($(this).text()); \r\n");
      out.write("\t\t\t\t//example=setTimeout(showHide,200);\r\n");
      out.write("\t\t\t\t$(\"#searchBtnFreeForm\").click();\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t  \t});\r\n");
      out.write("  \t}\r\n");
      out.write("}\r\n");
      out.write("function prepareExamplesTable(query){\r\n");
      out.write("\tvar openTag = '<tr><td align=\"left\"><a style=\"cursor: pointer;style: underline;\" class=\"box2-link\">';\r\n");
      out.write("\tvar closeTag = '</a></td></tr>';\r\n");
      out.write("\tvar rowTag = openTag + query + closeTag;\r\n");
      out.write("\t$(\"#queriesDiv\").append(rowTag);\r\n");
      out.write("}\r\n");
      out.write("function cancelRequest()\r\n");
      out.write("{\r\n");
      out.write("\tif ( navigator.userAgent.indexOf(\"MSIE\") > 0 )\r\n");
      out.write("\t\tdocument.execCommand('Stop');\r\n");
      out.write("\telse\r\n");
      out.write("\t\twindow.stop();\r\n");
      out.write("\t\r\n");
      out.write("\t\r\n");
      out.write("\t$(\"#pleaseWaitDiv\").hide();\r\n");
      out.write("\t$(\"#underProcess\").hide();\r\n");
      out.write("\t$(\"#metrics\").focus();\r\n");
      out.write("\tprocessing=false;\r\n");
      out.write("\tsubmitRequest = 0;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(" $(document).ready(function () { \r\n");
      out.write("\t processing=false;\r\n");
      out.write("\tautoSuggestClicked=false;\r\n");
      out.write("\t\r\n");
      out.write("\t$('#processYes').click(function(){\r\n");
      out.write("\t\t\t\t\t\t\t\tcancelRequest();\r\n");
      out.write("\t\t\t\t\t\t\t\t\t});\r\n");
      out.write("\t$('#processNo').click(function(){\r\n");
      out.write("\t\t\t\t\t\t\t\t\t$(\"#underProcess\").hide();\r\n");
      out.write("\t\t\t\t\t\t\t\t\t});\r\n");
      out.write("\t\r\n");
      out.write("\t$('#queries').hide();\r\n");
      out.write("\t\t$('#examplesId').mouseover(function(){\r\n");
      out.write("\t\t\t$('#queries').css(\"left\",$(this).position().left+\"px\");\r\n");
      out.write("\t\t\t$('#queries').css(\"top\",$(this).position().top+79+\"px\");\r\n");
      out.write("\t\t\tgetAppExamples();\r\n");
      out.write("\t\t\tclearTimeout(example);\r\n");
      out.write("\t\t\t$('#queries').show();\r\n");
      out.write("\t\t});   \r\n");
      out.write("\t\t$('#examplesId-freeform').mouseover(function(){\r\n");
      out.write("\t\t\t$('#queries').css(\"left\",$(this).position().left+\"px\");\r\n");
      out.write("\t\t\t$('#queries').css(\"top\",$(this).position().top+15+\"px\");\r\n");
      out.write("\t\t\t$('#queries a').css({color:\"#333\",fontSize:\"12px\"});\r\n");
      out.write("\t\t\tgetAppExamples();\r\n");
      out.write("\t\t\tclearTimeout(example);\r\n");
      out.write("\t\t\t$('#queries').show();\r\n");
      out.write("\t\t});  \r\n");
      out.write("\t\t$('#examplesId-sem2').mouseover(function(){\r\n");
      out.write("\t\t\t$('#queries').css(\"left\",$(this).position().left+0+\"px\");\r\n");
      out.write("\t\t\t$('#queries').css(\"top\",$(this).position().top+16+\"px\");\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\tclearTimeout(example);\r\n");
      out.write("\t\t\t$('#queries').show();\r\n");
      out.write("\t\t\t$('#queries').find(\"a\").css(\"color\",\"#333\");\r\n");
      out.write("\t\t});  \r\n");
      out.write("\t\t$('#examplesId').mouseout(function(){\r\n");
      out.write("\t\t\texample=setTimeout(showHide,200);\r\n");
      out.write("\t\t});\t\r\n");
      out.write("\t\t$('#examplesId-freeform').mouseout(function(){\r\n");
      out.write("\t\t\texample=setTimeout(showHide,200);\r\n");
      out.write("\t\t});\t\r\n");
      out.write("\t\t$('#examplesId-sem2').mouseout(function(){\r\n");
      out.write("\t\t\texample=setTimeout(showHide,200);\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t$('#queries').mouseover(function(){\r\n");
      out.write("\t\t\tclearTimeout(example);\r\n");
      out.write("\t\t});\t\t\r\n");
      out.write("\t\t$('#queries').mouseout(function(){\r\n");
      out.write("\t\t\texample=setTimeout(showHide,200);\r\n");
      out.write("\t\t});\t\r\n");
      out.write("\t\t\r\n");
      out.write("\t$(\"#queries td a\").bind(\"click\",function(){\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t$(\"#metrics\").val($(this).text()); \r\n");
      out.write("\t\t\texample=setTimeout(showHide,200);\r\n");
      out.write("\t\t\t//alert(\"g\");\r\n");
      out.write("\t\t\t$(\"#searchBtnFreeForm\").click();\r\n");
      out.write("\t\t\t });\t\t\r\n");
      out.write("\tvar applicationId='");
      if (_jspx_meth_s_005fproperty_005f2(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("\tvar url=\"\"\r\n");
      out.write("\tif(applicationId){\r\n");
      out.write("\t  url='");
      if (_jspx_meth_c_005fout_005f10(_jspx_page_context))
        return;
      out.write("/querySuggest/semanticSuggest.action?applicationId='+applicationId;\r\n");
      out.write("\t}else{\r\n");
      out.write("\t   url='");
      if (_jspx_meth_c_005fout_005f11(_jspx_page_context))
        return;
      out.write("/querySuggest/semanticSuggest.action';\r\n");
      out.write("\t}\r\n");
      out.write("\t\t$(\"#metrics\").autocomplete(url, {\r\n");
      out.write("\t\tmultiple: true,\r\n");
      out.write("\t\tmustMatch: false,\r\n");
      out.write("\t\tformatMatch: false,\r\n");
      out.write("\t\tselectFirst : false,\r\n");
      out.write("\t\tcacheLength: 0,\r\n");
      out.write("\t\tautoFill: false,\r\n");
      out.write("\t\tmultipleSeparator: \" \"//,\r\n");
      out.write("\t\t//width:604\r\n");
      out.write("\t});\r\n");
      out.write("\r\n");
      out.write("$(\"#metrics\").bind('keydown', function(e) {\r\n");
      out.write("\t\t\t\t\tif (e.keyCode == 13) {\r\n");
      out.write("\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t//alert(autoSelect);\r\n");
      out.write("\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t    if(autoSuggestClicked){$(\"#searchBtnFreeForm\").trigger('click'); return false;}\r\n");
      out.write("\t\t\t\t\t\tif(autoSelect){ autoSelect=false; return false;}\r\n");
      out.write("\t\t\t\t\t\telse{\r\n");
      out.write("\t\t\t\t\t\t$(\"#searchBtnFreeForm\").focus();\r\n");
      out.write("\t\t\t\t\t\t$(\"#searchBtnFreeForm\").removeClass(\"imgNoBorder1\");\r\n");
      out.write("\t\t\t\t\t\t$(\"#searchBtnFreeForm\").addClass(\"imgBorder1\");\r\n");
      out.write("\t\t\t\t\t\t$(\"#searchBtnFreeForm\").trigger('click');\r\n");
      out.write("\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\t//$(\"#searchBtn\").trigger('click');\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\telse if(e.keyCode==9){\r\n");
      out.write("\t\t\t\t\t\tsetFocusOnSearch();\r\n");
      out.write("\t\t\t\t\t\t//$(\"#searchBtn\").trigger('click');\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\telse if(e.keyCode==8){\r\n");
      out.write("\t\t\t\t\t   \r\n");
      out.write("\t\t\t\t\t\tvar displayString=$(\"#metrics\").val();\r\n");
      out.write("\t\t\t\t\t\tif(displayString.indexOf(\"Ask simple questions.\")>-1){\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t  $(\"#metrics\").val(\"\");\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t  return;\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t  }\r\n");
      out.write("\t\t\t\t\t\t//$(\"#searchBtn\").trigger('click');\r\n");
      out.write("\t\t\t\t\t}\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t}).result(function(event, data, formatted) {  autoSelect=true; event.stopPropagation(); return false;});\r\n");
      out.write("\r\n");
      out.write("$ht=screen.height;\r\n");
      out.write("//$htMargin=($ht-($ht-160))/4;\r\n");
      out.write("$htSearchDiv=($ht-160)-($ht/6);\r\n");
      out.write("\r\n");
      out.write("$(\"#tdHeight\").css(\"height\",$htSearchDiv+\"px\");\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t \r\n");
      out.write(" $(\"a#bookmarksId\").click(function(){\r\n");
      out.write(" data=$(\"#metrics\").val();\r\n");
      out.write(" $(\"#requestId\").val(data);\r\n");
      out.write("   // alert($(\"#requestId\").val());\r\n");
      out.write("\tPane_Left=$(this).position().left;\r\n");
      out.write("\tPane_Top=$(this).position().top;\r\n");
      out.write("\tmaxRecords=2;\r\n");
      out.write("\t$(\"#showBookmarksLink\").hide(); \t\r\n");
      out.write("\t$(\"#loadingShowBookmarksLink\").show();\r\n");
      out.write("\tif(data==\"\"){\r\n");
      out.write("\t\talert(\"please enter query\");\r\n");
      out.write("\t\t$(\"#showBookmarksLink\").show();\r\n");
      out.write("\t\t$(\"#loadingShowBookmarksLink\").hide();\r\n");
      out.write("\t\treturn false; \r\n");
      out.write("\t}else{\r\n");
      out.write("\t\r\n");
      out.write("\t$.get(\"");
      if (_jspx_meth_c_005fout_005f12(_jspx_page_context))
        return;
      out.write("/bookmark/showUserFolders.action\", {ajax: 'true'}, function(data) { \t\t\t\t\t\t\t\t\t\t\t  \r\n");
      out.write("\t\t$(\"#loadingShowBookmarksLink\").hide(); \r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").empty(); \r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").append(data);\r\n");
      out.write("\t\t$(\"#hiddenPane\").css(\"left\",Pane_Left); \r\n");
      out.write("\t\t$(\"#hiddenPane\").css(\"top\",Pane_Top+20); \r\n");
      out.write("\t\t$(\"#hiddenPane\").fadeIn(\"slow\"); \r\n");
      out.write("\t\t$(\"#userName\").select().focus();\r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").css(\"height\",130+\"px\");\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").css(\"width\",290+\"px\");\r\n");
      out.write("\t\t$(\"#showBookmarksLink\").show();\r\n");
      out.write("\t\t$(\"#loadingShowBookmarksLink\").hide();\r\n");
      out.write("\t\t//$(\"#bookmarkName\").focus();\r\n");
      out.write("\t\t$(\"#txtId\").select().focus();\r\n");
      out.write("\t\t$(\"#bookmarkVal\").val(xmlQueryData);\r\n");
      out.write("\t\t//doDrag();\r\n");
      out.write("\t\t//alert($(\"#bookmarkVal\").val(xmlQueryData));\r\n");
      out.write("\t\t//alert(data.indexOf(\"SIGN IN\"));\r\n");
      out.write("\t\tif(data.indexOf(\"SIGN IN\")>-1){\r\n");
      out.write("\t\t\t$(\"form#s_form input#userName\").select().focus();\r\n");
      out.write("\t\t}else{\r\n");
      out.write("\t\t\t$(\"ul#browser li ul\").hide();\r\n");
      out.write("\t\t\t$(\"form#form1 input#bookmarkName\").focus();\r\n");
      out.write("\t\t\t$(\"#hiddenPaneContent\").css(\"height\",100+\"px\");\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\r\n");
      out.write("\r\n");
      out.write("\t});\r\n");
      out.write("\t\r\n");
      out.write("\t}\r\n");
      out.write("});\r\n");
      out.write(" \r\n");
      out.write("  $(\"a#bookmarksSearchId\").click(function(){\r\n");
      out.write("\tPane_Left=$(this).position().left-200;\r\n");
      out.write("\tPane_Top=$(this).position().top;\r\n");
      out.write("\tmaxRecords=2;\r\n");
      out.write("\t//$(\"#clearPage\").trigger(\"click\");\r\n");
      out.write("\t$(\"#hiddenPane\").hide();\r\n");
      out.write("\t$(\"#showBookmarksSearchLink\").hide(); \t\r\n");
      out.write("\t$(\"#loadingShowBookmarksSearchLink\").show();\r\n");
      out.write("\t$.get(\"");
      if (_jspx_meth_c_005fout_005f13(_jspx_page_context))
        return;
      out.write("/bookmark/showBookmarkSearch.action\", {ajax: 'true'}, function(data) { \r\n");
      out.write("\t\t$(\"#loadingShowBookmarksSearchLink\").hide(); \r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").empty(); \r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").append(data);\r\n");
      out.write("\t\t$(\"#hiddenPane\").css(\"left\",Pane_Left); \r\n");
      out.write("\t\t$(\"#hiddenPane\").css(\"top\",Pane_Top+20); \r\n");
      out.write("\t\t$(\"#hiddenPane\").fadeIn(\"slow\"); \r\n");
      out.write("\t\t$(\"#hiddenPane\").css(\"height\",\"250px\");\r\n");
      out.write("\t\t$(\"#hiddenPane\").css(\"width\",\"320px\");\r\n");
      out.write("\t\t$(\"#userName\").select().focus();\r\n");
      out.write("\t    $(\"#hiddenPaneContent\").css(\"height\",\"150px\");\r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").css(\"width\",290+\"px\");\r\n");
      out.write("\t\t$(\"#showBookmarksSearchLink\").show();\r\n");
      out.write("\t\t$(\"#loadingShowBookmarksSearchLink\").hide();\r\n");
      out.write("\t\t$(\"#searchText\").focus();\r\n");
      out.write("\t\t//doDrag();\r\n");
      out.write("\r\n");
      out.write("\t\tif(data.indexOf(\"SIGN IN\")==-1){\r\n");
      out.write("\t\t\tif(data.indexOf(\"###searchBookMarksBox####\")>-1){\r\n");
      out.write("\t\t\t$(\"ul#browser li ul\").hide();\r\n");
      out.write("\t\t\t$(\"#hiddenPaneContent\").css(\"width\",320+\"px\");\r\n");
      out.write("\t\t\t$(\"#hiddenPaneContent\").css(\"height\",\"auto\");\r\n");
      out.write("\t\t\t$(\"#hiddenPane\").css(\"height\",\"250px\");\r\n");
      out.write("\t\t$(\"#hiddenPane\").css(\"width\",\"320px\");\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t$.each($(\"li span.file\"),function(){\t\t   \r\n");
      out.write("\t\t\tif($(this).find('.dType').text()!='S'){\r\n");
      out.write("\t\t\t\t//$(this).parent('li').hide();\r\n");
      out.write("\t\t\t$(this).find('.dVal').attr(\"title\",\"Not applicable for this page\");\r\n");
      out.write("\t\t\t$(this).find('.dVal').css(\"color\",\"#DFDFDF\");\r\n");
      out.write("\t\t\t$(this).find('.dVal').unbind(\"click\");\r\n");
      out.write("\t\t\t$(\"#hiddenPane\").css(\"height\",\"250px\");\r\n");
      out.write("\t\t    $(\"#hiddenPane\").css(\"width\",\"320px\");\r\n");
      out.write("\t\t }else{\r\n");
      out.write("\t\t   var applicationId='");
      if (_jspx_meth_s_005fproperty_005f3(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("\t\t   if(applicationId){\t\t\t\r\n");
      out.write("\t\t\t  var bookmarkAppId=$(this).attr(\"appId\");\t\t\r\n");
      out.write("\t\t\t  if(bookmarkAppId != -1 && applicationId != bookmarkAppId){\r\n");
      out.write("\t\t\t\t   $(this).find('.dVal').attr(\"title\",\"Not applicable for this page\");\r\n");
      out.write("\t\t\t\t   $(this).find('.dVal').css(\"color\",\"#DFDFDF\");\r\n");
      out.write("\t\t\t\t   $(this).find('.dVal').unbind(\"click\");\r\n");
      out.write("\t\t\t  }\t\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t }\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\r\n");
      out.write("\t});\r\n");
      out.write("});\r\n");
      out.write(" \r\n");
      out.write("/* $(\".qiHeaderDiv_new a#loginId\").click(function(){\r\n");
      out.write("Pane_Left=$(this).position().left;\r\n");
      out.write("Pane_Top=$(this).position().top;\r\n");
      out.write("$(\"#showLoginLink\").hide(); \t\r\n");
      out.write("$(\"#loadingShowLoginLink\").show(); \t\t\t\t\t\t\t   \r\n");
      out.write("$.get(\"ajaxlogin.jsp\", {ajax: 'true', noRedirect : 'true'}, function(data) { \r\n");
      out.write("\t\t$(\"#loadingShowLoginLink\").hide(); \r\n");
      out.write("\r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").empty(); \r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").append(data);\r\n");
      out.write("\t\t$(\"#hiddenPane\").css(\"left\",Pane_Left+300); \r\n");
      out.write("\t\t$(\"#hiddenPane\").css(\"top\",Pane_Top+23); \r\n");
      out.write("\t\t$(\"#hiddenPane\").fadeIn(\"slow\");\r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").css(\"width\",290+\"px\");\r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").css(\"height\",130+\"px\");\r\n");
      out.write("\t\t$(\"#showLoginLink\").show();\r\n");
      out.write("\t\t$(\"#userName\").select().focus();\r\n");
      out.write("\t});\r\n");
      out.write("});*/\r\n");
      out.write("function setOpacity(value) {\r\n");
      out.write("\ttestObj.style.opacity = value/10;\r\n");
      out.write("\ttestObj.style.filter = 'alpha(opacity=' + value*10 + ')';\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write(" function sim(l,t){\r\n");
      out.write("\t//alert(\"hi\");\r\n");
      out.write("var shimmer = document.createElement('iframe');\r\n");
      out.write("        shimmer.id='shimmer';\r\n");
      out.write("        shimmer.style.position='absolute';\r\n");
      out.write("        // normally you would get the dimensions and \r\n");
      out.write("        // positions of the sub div dynamically. For demo \r\n");
      out.write("        // purposes this is hardcoded\r\n");
      out.write("        shimmer.style.width='220px';\r\n");
      out.write("        shimmer.style.height='100px';\r\n");
      out.write("\t\t//alert(l+\"::\"+t)\r\n");
      out.write("        shimmer.style.top=t+105+'px';\r\n");
      out.write("        shimmer.style.left=l-240+'px';\r\n");
      out.write("        shimmer.style.zIndex='999';\r\n");
      out.write("\t\tshimmer.setAttribute('frameborder','0');\r\n");
      out.write("\t\tvalue=1;\r\n");
      out.write("\t\tshimmer.style.opacity = value/10;\r\n");
      out.write("\t    shimmer.style.filter = 'alpha(opacity=' + value*10 + ')';\r\n");
      out.write("\r\n");
      out.write("        //shimmer.setAttribute('src','javascript:false;');\r\n");
      out.write("        document.body.appendChild(shimmer);\r\n");
      out.write("}\r\n");
      out.write("  $(\"a#loginId\").click(function(){\r\n");
      out.write("Pane_Left=$(this).position().left;\r\n");
      out.write("Pane_Top=$(this).position().top;\r\n");
      out.write("if($(\"#PivotTable\").html()!=null){\r\n");
      out.write("\tsim(Pane_Left,Pane_Top);\t\r\n");
      out.write("\t}\r\n");
      out.write("$(\"#showLoginLink\").hide(); \t\r\n");
      out.write("$(\"#loadingShowLoginLink\").show(); \t\t\t\t\t\t\t   \r\n");
      out.write("$.get(\"");
      if (_jspx_meth_c_005fout_005f14(_jspx_page_context))
        return;
      out.write("/ajaxlogin.jsp\", {ajax: 'true', noRedirect : 'true'}, function(data) { \r\n");
      out.write("\t\t$(\"#loadingShowLoginLink\").hide(); \r\n");
      out.write("\r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").empty(); \r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").append(data);\r\n");
      out.write("\t\t$(\"#hiddenPane\").css(\"left\",Pane_Left-280); \r\n");
      out.write("\t\t$(\"#hiddenPane\").css(\"top\",Pane_Top+23); \r\n");
      out.write("\t\t$(\"#hiddenPane\").fadeIn(\"slow\");\r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").css(\"width\",290+\"px\");\r\n");
      out.write("\t\t$(\"#hiddenPaneContent\").css(\"height\",160+\"px\");\r\n");
      out.write("\t\t$(\"#showLoginLink\").show();\r\n");
      out.write("\t\t$(\"#hiddenPane\").css(\"height\",\"160px\");\r\n");
      out.write("\t\t$(\"#userName\").select().focus();\r\n");
      out.write("\t});\r\n");
      out.write("});\r\n");
      out.write("\t\t\t\t\t\t\t \r\n");
      out.write("$(\"#hiddenPane a#closeButtonId\").click(function(){\r\n");
      out.write("\r\n");
      out.write("\t\t$(\"#hiddenPane\").fadeOut(\"slow\");\r\n");
      out.write("\t\t$('#shimmer').remove();\r\n");
      out.write("        \r\n");
      out.write("\t});\r\n");
      out.write("\t\r\n");
      out.write("\r\n");
      out.write("//Query population on back button click\r\n");
      out.write("  function convert(str){\r\n");
      out.write("  str = str.replace(/&amp;/g, \"&\");\r\n");
      out.write("  str = str.replace(/&gt;/g, \">\");\r\n");
      out.write("  str = str.replace(/&lt;/g, \"<\");\r\n");
      out.write("  str = str.replace(/&quot;/g, '\"');\r\n");
      out.write("  str = str.replace(/&#039;/g, \"'\");\r\n");
      out.write("  return str;\r\n");
      out.write("}\r\n");
      out.write("var data='");
      if (_jspx_meth_s_005fproperty_005f4(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("var reqString=convert(data);\r\n");
      out.write("if(reqString){\r\n");
      out.write("   $(\"#metrics\").empty();\r\n");
      out.write("   var type = '");
      if (_jspx_meth_s_005fproperty_005f5(_jspx_page_context))
        return;
      out.write("'; \r\n");
      out.write("   if(convert(type) == 'SI'){   \r\n");
      out.write("   \t\t$(\"#metrics\").val(reqString);  \r\n");
      out.write("   \t} \r\n");
      out.write("}\r\n");
      out.write("  showLoginInfo('");
      if (_jspx_meth_security_005fauthentication_005f0(_jspx_page_context))
        return;
      out.write("');\r\n");
      out.write("  showPublisherInfo('");
      if (_jspx_meth_security_005fauthentication_005f1(_jspx_page_context))
        return;
      out.write('\'');
      out.write(',');
      out.write('\'');
      if (_jspx_meth_security_005fauthentication_005f2(_jspx_page_context))
        return;
      out.write("');\r\n");
      out.write(" \r\n");
      out.write(" \r\n");
      out.write(" var loginUserId='");
      if (_jspx_meth_security_005fauthentication_005f3(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("if(loginUserId==\"guest\"){\r\n");
      out.write("\t//$(\"#publisherConsoleId\").hide();\r\n");
      out.write("\t//$(\"#publishAppId\").show();\r\n");
      out.write("\t\r\n");
      out.write("}else{\r\n");
      out.write("    var isPublisher='");
      if (_jspx_meth_security_005fauthentication_005f4(_jspx_page_context))
        return;
      out.write("'; \r\n");
      out.write("\t     if(\"YES\"==isPublisher){\r\n");
      out.write("\t          $(\"#publishAppId\").hide();\r\n");
      out.write("\t\t\t  $(\"#publisherConsoleId\").show();\t\t\t \t\t  \t\t\t \r\n");
      out.write("\t       }else{\r\n");
      out.write("\t        //  $(\"#publishAppId\").show();\r\n");
      out.write("\t\t\t // $(\"#publisherConsoleId\").hide();\r\n");
      out.write("\t       }\t\r\n");
      out.write("}\r\n");
      out.write("});// close of ready\r\n");
      out.write("</script>\r\n");
      out.write("<script>\r\n");
      out.write("\r\n");
      out.write("\tfunction showLoginInfo(name) {\r\n");
      out.write("\t\tname = name.replace(/^\\s+|\\s+$/g,\"\");\r\n");
      out.write("\t\tif(name && name.length > 0) {\r\n");
      out.write("\t\t\t$(\"#showWelcome\").empty().append('");
      if (_jspx_meth_s_005ftext_005f3(_jspx_page_context))
        return;
      out.write(" ' + name);\r\n");
      out.write("\t\t\t$(\"#userNameForComments\").val(name);\r\n");
      out.write("\t\t\t$(\"#loginId\").hide();\r\n");
      out.write("\t\t\t$(\"#logoutId\").show();\r\n");
      out.write("\t\t\t$(\"#showBookmarksSearchLink\").show();\r\n");
      out.write("\t\t\t$(\"#changePassword\").show();\r\n");
      out.write("\t\t\t$(\"#moredata\").hide();\r\n");
      out.write("\t\t\t$(\"#publishAppId\").hide();\r\n");
      out.write("\t\t\t  $(\"#publisherConsoleId\").show();\t\t\t \r\n");
      out.write("\t\t}\t\r\n");
      out.write("\t}\r\n");
      out.write("   function showPublisherInfo(admin,publisher) {\r\n");
      out.write("\t\t\tif(admin==\"true\" ||publisher==\"true\"){\r\n");
      out.write("\t\t\t$(\"#publisherTdSeperator\").show();\r\n");
      out.write("\t\t\t$(\"#publisherTd\").show();\r\n");
      out.write("\t\t\t$(\"#seperatorId\").show();\t\r\n");
      out.write("\t\t\t$(\"#changePassword\").show();\r\n");
      out.write("\t\t\t$(\"#adminId\").attr(\"href\",\"");
      if (_jspx_meth_c_005fout_005f15(_jspx_page_context))
        return;
      out.write("swi/showSearchAppsDashboard.action\");\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t}\r\n");
      out.write("function setDetailedResultsPerPage(resultsPerPage){\r\n");
      out.write(" var srcType = $(\"#typeId\").val(); \r\n");
      out.write(" var xmlValue = $(\"#requestedStringId\").val();\r\n");
      out.write(" //alert(xmlValue);\r\n");
      out.write("   //alert(srcType); \r\n");
      out.write("   if(srcType==\"QI\"){\r\n");
      out.write("  \t window.location=\"");
      out.print("./"+baseURL);
      out.write("&resultsPerPage=\"+resultsPerPage+\"&srcType=QI\";\r\n");
      out.write("   }else{\r\n");
      out.write("\twindow.location=\"");
      out.print("./"+baseURL);
      out.write("&resultsPerPage=\"+resultsPerPage+\"&srcType=SI\";\r\n");
      out.write("\t}\r\n");
      out.write("}\r\n");
      out.write("/*$(document).mousemove(function(){\r\n");
      out.write("\t\t\t\t\t\t\t   if($(\"#at15s\")){\r\n");
      out.write("\t\t\t\t$(\"#at15s\").remove();\r\n");
      out.write("}\r\n");
      out.write("});*/\r\n");
      out.write("window.onload=function(){\r\n");
      out.write("processing=false;\t\r\n");
      out.write("}\r\n");
      out.write("function js_RemoveChar(str){\r\n");
      out.write("\tcharToRemove = '\"';\r\n");
      out.write("\t\r\n");
      out.write("\tregExp = new RegExp(\"[\"+charToRemove+\"]\",\"g\");\r\n");
      out.write("\t\r\n");
      out.write("\treturn str.replace(regExp,\"'\");\r\n");
      out.write("\r\n");
      out.write("}\r\n");
      out.write("function showRecordsDiv(){\r\n");
      out.write("\r\n");
      out.write("\tvar resultsPerPage = 50;\r\n");
      out.write("   \tvar requestedPage = 1;\r\n");
      out.write("   \tvar startRecord = 1;\r\n");
      out.write("   \tvar endRecord = 1;\r\n");
      out.write("   \tvar totalRecrods = 1;\r\n");
      out.write("   \tvar userPageSize = '");
      out.print(request.getSession().getAttribute("USER_DETAIL_PAGESIZE"));
      out.write("';\r\n");
      out.write("   \r\n");
      out.write("   if('");
      out.print(baseURL);
      out.write("'.length > 0 ) {\r\n");
      out.write("\t   requestedPage = '");
      out.print(requestedPage);
      out.write("';\r\n");
      out.write("\t   totalRecrods = '");
      out.print(totalRecrods);
      out.write("';\r\n");
      out.write("\t   \r\n");
      out.write("\t   if (userPageSize.length > 0){\r\n");
      out.write("\t      resultsPerPage = userPageSize;\r\n");
      out.write("\t   }\r\n");
      out.write("\t   if(requestedPage == 1){\r\n");
      out.write("\t      startRecord = 1;\r\n");
      out.write("\t      endRecord = resultsPerPage * requestedPage;\r\n");
      out.write("\t   } else {\r\n");
      out.write("\t      startRecord = (resultsPerPage * requestedPage - resultsPerPage)+ 1;\r\n");
      out.write("\t      endRecord = resultsPerPage * requestedPage;\r\n");
      out.write("\t   }\r\n");
      out.write("\t   if(endRecord > totalRecrods)\r\n");
      out.write("\t      endRecord = totalRecrods;\r\n");
      out.write("\t   \r\n");
      out.write("\t   $(\"#recordsDiv\").html('<b>Displaying ' + startRecord + ' - ' + endRecord + ' of ' + totalRecrods + ' records</b>');\r\n");
      out.write("   }\r\n");
      out.write("}\r\n");
      out.write("function  removeSpecialChar(text){\r\n");
      out.write("\t$text=text;\r\n");
      out.write("$text=$text.replace(/#b9;/gi,\"\");\t\t\r\n");
      out.write("\t\t\t$text=$text.replace(/#b9;/gi,\"\");\t\t\t\r\n");
      out.write("\t\t\t$text=$text.replace(/#e0;/gi,\"\");\t\t\r\n");
      out.write("\t\t\t$text=$text.replace(/#e0;/gi,\"\");\t\t\t\r\n");
      out.write("\t\t\t$text=$text.replace(/#ae;/gi,\"\");\t\t\r\n");
      out.write("\t\t\t$text=$text.replace(/ae;/gi,\"\");\r\n");
      out.write("\t\t\t$text=$text.replace(/#ff;/gi,\"\");\t\t\t\t\r\n");
      out.write("\t\t\t$text=$text.replace(/#c3;/gi,\"\");\t\t\r\n");
      out.write("\t\t\t$text=$text.replace(/##/gi,\"\");\r\n");
      out.write("\t\t\t$text=$text.replace(/@@/gi,\"\");\r\n");
      out.write("\t\t\t$text=$text.replace(/\\^/gi,\"\");\r\n");
      out.write("\t\t\t$text=$text.replace(/__/gi,\"\");\r\n");
      out.write("\t\t\t$text=$text.replace(/\\*/gi,\"\");\r\n");
      out.write("\t\t\t$text=$text.replace(/~/gi,\"\");\r\n");
      out.write("\t\t\t$text=$text.replace(/Â¿/gi,\"\");\r\n");
      out.write("\t\t\t$text=$text.replace(/#be;/gi,\"\");\t\t\r\n");
      out.write("\t\t\t$text=$text.replace(/&#16;/gi,\"\");\t\t\r\n");
      out.write("\t\t\t$text=$text.replace(/#b6;/gi,\"\");\t\t\r\n");
      out.write("\t\t\t$text=$text.replace(/#d3/gi,\"\");\t\t\t\r\n");
      out.write("\t\t\t$text=$text.replace(/|/gi,\"\");\t\t\t\r\n");
      out.write("\t\t\t$text=$text.replace(/&amp;/gi,\"\");\t\t\t\r\n");
      out.write("\t\t\t$text=$text.replace(/;/gi,\"\");\r\n");
      out.write("\t\t\treturn $text;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("$(document).ajaxError(function(e,xhr,settings) {\r\n");
      out.write("\t\t$(\"#dynamicPane\").text(\"Unable to process\");\r\n");
      out.write("\t\t$(\".dynamicPaneBgLoader\").css(\"backgroundImage\",\"none\");\r\n");
      out.write("\t\t//alert(settings.url);\r\n");
      out.write("\t\t\r\n");
      out.write("\r\n");
      out.write("});\r\n");
      out.write("\r\n");
      out.write("function showApplications(){\t \r\n");
      out.write("\t$.get(\"qi/showCommunityApplicationsIncludingUserSpecificApps.action?type=SI\",function(data){\t\r\n");
      out.write("\t\t$(\"#applicationListDiv\").empty().append(data);\t\t\r\n");
      out.write("\t\t\r\n");
      out.write("\t});\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("}\r\n");
      out.write("function getInternetExplorerVersion() {\r\n");
      out.write("\r\n");
      out.write("    var rv = -1; // Return value assumes failure.\r\n");
      out.write("\r\n");
      out.write("    if (navigator.appName == 'Microsoft Internet Explorer') {\r\n");
      out.write("\r\n");
      out.write("        var ua = navigator.userAgent;\r\n");
      out.write("\r\n");
      out.write("        var re = new RegExp(\"MSIE ([0-9]{1,}[\\.0-9]{0,})\");\r\n");
      out.write("\r\n");
      out.write("        if (re.exec(ua) != null)\r\n");
      out.write("\r\n");
      out.write("            rv = parseFloat(RegExp.$1);\r\n");
      out.write("\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    return rv;\r\n");
      out.write("\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("function checkVersion() {\r\n");
      out.write("\r\n");
      out.write("    var msg = \"You're not using Windows Internet Explorer.\";\r\n");
      out.write("\r\n");
      out.write("    var ver = getInternetExplorerVersion();\r\n");
      out.write("\r\n");
      out.write("    if (ver > -1) {\r\n");
      out.write("\r\n");
      out.write("        if (ver >= 8.0)\r\n");
      out.write("\r\n");
      out.write("            msg = \"You're using a recent copy of Windows Internet Explorer.\"\r\n");
      out.write("\r\n");
      out.write("        else\r\n");
      out.write("\r\n");
      out.write("            msg = \"You should upgrade your copy of Windows Internet Explorer.\";\r\n");
      out.write("\r\n");
      out.write("    }\r\n");
      out.write("\r\n");
      out.write("    alert(msg);\r\n");
      out.write("\r\n");
      out.write("}\r\n");
      out.write("</script>\r\n");
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

  private boolean _jspx_meth_c_005fout_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f0 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f0.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(32,12) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f0.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f0 = _jspx_th_c_005fout_005f0.doStartTag();
    if (_jspx_th_c_005fout_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f1 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f1.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(33,29) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f1.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f1 = _jspx_th_c_005fout_005f1.doStartTag();
    if (_jspx_th_c_005fout_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f1);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f2 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f2.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f2.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(34,29) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f2.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f2 = _jspx_th_c_005fout_005f2.doStartTag();
    if (_jspx_th_c_005fout_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f2);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f3 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f3.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f3.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(35,35) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f3.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f3 = _jspx_th_c_005fout_005f3.doStartTag();
    if (_jspx_th_c_005fout_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f3);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f4(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f4 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f4.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f4.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(36,35) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f4.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f4 = _jspx_th_c_005fout_005f4.doStartTag();
    if (_jspx_th_c_005fout_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f4);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f5(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f5 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f5.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f5.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(37,36) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f5.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f5 = _jspx_th_c_005fout_005f5.doStartTag();
    if (_jspx_th_c_005fout_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f5);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f6(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f6 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f6.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f6.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(39,6) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f6.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f6 = _jspx_th_c_005fout_005f6.doStartTag();
    if (_jspx_th_c_005fout_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f6);
    return false;
  }

  private boolean _jspx_meth_s_005ftext_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:text
    org.apache.struts2.views.jsp.TextTag _jspx_th_s_005ftext_005f0 = (org.apache.struts2.views.jsp.TextTag) _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.TextTag.class);
    _jspx_th_s_005ftext_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftext_005f0.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(48,14) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f0.setName("execue.appCount");
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
    // /views/main/search/qi-head-simpleSearch.jsp(50,9) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f1.setName("execue.qi.search.all.published.apps");
    int _jspx_eval_s_005ftext_005f1 = _jspx_th_s_005ftext_005f1.doStartTag();
    if (_jspx_th_s_005ftext_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f1);
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
    // /views/main/search/qi-head-simpleSearch.jsp(55,16) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f0.setValue("applicationId");
    int _jspx_eval_s_005fproperty_005f0 = _jspx_th_s_005fproperty_005f0.doStartTag();
    if (_jspx_th_s_005fproperty_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f0);
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
    // /views/main/search/qi-head-simpleSearch.jsp(57,11) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f2.setName("execue.qi.search.selected.published.apps");
    int _jspx_eval_s_005ftext_005f2 = _jspx_th_s_005ftext_005f2.doStartTag();
    if (_jspx_th_s_005ftext_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f2);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f7(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f7 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f7.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f7.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(213,9) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f7.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f7 = _jspx_th_c_005fout_005f7.doStartTag();
    if (_jspx_th_c_005fout_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f7);
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
    // /views/main/search/qi-head-simpleSearch.jsp(351,20) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f1.setValue("applicationId");
    int _jspx_eval_s_005fproperty_005f1 = _jspx_th_s_005fproperty_005f1.doStartTag();
    if (_jspx_th_s_005fproperty_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f1);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f8(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f8 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f8.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f8.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(353,24) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f8.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f8 = _jspx_th_c_005fout_005f8.doStartTag();
    if (_jspx_th_c_005fout_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f8);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f9(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f9 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f9.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f9.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(367,24) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f9.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f9 = _jspx_th_c_005fout_005f9.doStartTag();
    if (_jspx_th_c_005fout_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f9);
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
    // /views/main/search/qi-head-simpleSearch.jsp(461,20) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f2.setValue("applicationId");
    int _jspx_eval_s_005fproperty_005f2 = _jspx_th_s_005fproperty_005f2.doStartTag();
    if (_jspx_th_s_005fproperty_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f2);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f10(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f10 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f10.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f10.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(464,8) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f10.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f10 = _jspx_th_c_005fout_005f10.doStartTag();
    if (_jspx_th_c_005fout_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f10);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f11(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f11 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f11.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f11.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(466,9) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f11.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f11 = _jspx_th_c_005fout_005f11.doStartTag();
    if (_jspx_th_c_005fout_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f11);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f12(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f12 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f12.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f12.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(535,8) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f12.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f12 = _jspx_th_c_005fout_005f12.doStartTag();
    if (_jspx_th_c_005fout_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f12);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f13(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f13 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f13.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f13.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(577,8) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f13.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f13 = _jspx_th_c_005fout_005f13.doStartTag();
    if (_jspx_th_c_005fout_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f13);
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
    // /views/main/search/qi-head-simpleSearch.jsp(613,24) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f3.setValue("applicationId");
    int _jspx_eval_s_005fproperty_005f3 = _jspx_th_s_005fproperty_005f3.doStartTag();
    if (_jspx_th_s_005fproperty_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f3);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f14(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f14 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f14.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f14.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(684,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f14.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f14 = _jspx_th_c_005fout_005f14.doStartTag();
    if (_jspx_th_c_005fout_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f14);
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
    // /views/main/search/qi-head-simpleSearch.jsp(717,10) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f4.setValue("requestString");
    int _jspx_eval_s_005fproperty_005f4 = _jspx_th_s_005fproperty_005f4.doStartTag();
    if (_jspx_th_s_005fproperty_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f4);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f5(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f5 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f5.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f5.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(721,15) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f5.setValue("type");
    int _jspx_eval_s_005fproperty_005f5 = _jspx_th_s_005fproperty_005f5.doStartTag();
    if (_jspx_th_s_005fproperty_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f5);
    return false;
  }

  private boolean _jspx_meth_security_005fauthentication_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  security:authentication
    org.springframework.security.taglibs.authz.AuthenticationTag _jspx_th_security_005fauthentication_005f0 = (org.springframework.security.taglibs.authz.AuthenticationTag) _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.get(org.springframework.security.taglibs.authz.AuthenticationTag.class);
    _jspx_th_security_005fauthentication_005f0.setPageContext(_jspx_page_context);
    _jspx_th_security_005fauthentication_005f0.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(726,17) name = property type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_security_005fauthentication_005f0.setProperty("principal.fullName");
    int _jspx_eval_security_005fauthentication_005f0 = _jspx_th_security_005fauthentication_005f0.doStartTag();
    if (_jspx_th_security_005fauthentication_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.reuse(_jspx_th_security_005fauthentication_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.reuse(_jspx_th_security_005fauthentication_005f0);
    return false;
  }

  private boolean _jspx_meth_security_005fauthentication_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  security:authentication
    org.springframework.security.taglibs.authz.AuthenticationTag _jspx_th_security_005fauthentication_005f1 = (org.springframework.security.taglibs.authz.AuthenticationTag) _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.get(org.springframework.security.taglibs.authz.AuthenticationTag.class);
    _jspx_th_security_005fauthentication_005f1.setPageContext(_jspx_page_context);
    _jspx_th_security_005fauthentication_005f1.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(727,21) name = property type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_security_005fauthentication_005f1.setProperty("principal.admin");
    int _jspx_eval_security_005fauthentication_005f1 = _jspx_th_security_005fauthentication_005f1.doStartTag();
    if (_jspx_th_security_005fauthentication_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.reuse(_jspx_th_security_005fauthentication_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.reuse(_jspx_th_security_005fauthentication_005f1);
    return false;
  }

  private boolean _jspx_meth_security_005fauthentication_005f2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  security:authentication
    org.springframework.security.taglibs.authz.AuthenticationTag _jspx_th_security_005fauthentication_005f2 = (org.springframework.security.taglibs.authz.AuthenticationTag) _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.get(org.springframework.security.taglibs.authz.AuthenticationTag.class);
    _jspx_th_security_005fauthentication_005f2.setPageContext(_jspx_page_context);
    _jspx_th_security_005fauthentication_005f2.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(727,77) name = property type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_security_005fauthentication_005f2.setProperty("principal.publisher");
    int _jspx_eval_security_005fauthentication_005f2 = _jspx_th_security_005fauthentication_005f2.doStartTag();
    if (_jspx_th_security_005fauthentication_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.reuse(_jspx_th_security_005fauthentication_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.reuse(_jspx_th_security_005fauthentication_005f2);
    return false;
  }

  private boolean _jspx_meth_security_005fauthentication_005f3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  security:authentication
    org.springframework.security.taglibs.authz.AuthenticationTag _jspx_th_security_005fauthentication_005f3 = (org.springframework.security.taglibs.authz.AuthenticationTag) _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.get(org.springframework.security.taglibs.authz.AuthenticationTag.class);
    _jspx_th_security_005fauthentication_005f3.setPageContext(_jspx_page_context);
    _jspx_th_security_005fauthentication_005f3.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(730,18) name = property type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_security_005fauthentication_005f3.setProperty("principal.username");
    int _jspx_eval_security_005fauthentication_005f3 = _jspx_th_security_005fauthentication_005f3.doStartTag();
    if (_jspx_th_security_005fauthentication_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.reuse(_jspx_th_security_005fauthentication_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.reuse(_jspx_th_security_005fauthentication_005f3);
    return false;
  }

  private boolean _jspx_meth_security_005fauthentication_005f4(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  security:authentication
    org.springframework.security.taglibs.authz.AuthenticationTag _jspx_th_security_005fauthentication_005f4 = (org.springframework.security.taglibs.authz.AuthenticationTag) _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.get(org.springframework.security.taglibs.authz.AuthenticationTag.class);
    _jspx_th_security_005fauthentication_005f4.setPageContext(_jspx_page_context);
    _jspx_th_security_005fauthentication_005f4.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(736,21) name = property type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_security_005fauthentication_005f4.setProperty("principal.user.isPublisher");
    int _jspx_eval_security_005fauthentication_005f4 = _jspx_th_security_005fauthentication_005f4.doStartTag();
    if (_jspx_th_security_005fauthentication_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.reuse(_jspx_th_security_005fauthentication_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.reuse(_jspx_th_security_005fauthentication_005f4);
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
    // /views/main/search/qi-head-simpleSearch.jsp(752,37) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f3.setName("execue.login.welcome.message");
    int _jspx_eval_s_005ftext_005f3 = _jspx_th_s_005ftext_005f3.doStartTag();
    if (_jspx_th_s_005ftext_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f3);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f15(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f15 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f15.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f15.setParent(null);
    // /views/main/search/qi-head-simpleSearch.jsp(769,30) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f15.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${adminPath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f15 = _jspx_th_c_005fout_005f15.doStartTag();
    if (_jspx_th_c_005fout_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f15);
    return false;
  }
}
