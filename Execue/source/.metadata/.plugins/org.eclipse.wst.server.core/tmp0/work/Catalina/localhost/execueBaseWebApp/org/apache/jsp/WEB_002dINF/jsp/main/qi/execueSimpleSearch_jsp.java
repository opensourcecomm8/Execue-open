package org.apache.jsp.WEB_002dINF.jsp.main.qi;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.execue.core.common.bean.Pagination;
import com.execue.web.core.util.ExecueWebConstants;

public final class execueSimpleSearch_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody;
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
    _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.release();
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
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
      out.write("<head>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      //  c:set
      org.apache.taglibs.standard.tag.rt.core.SetTag _jspx_th_c_005fset_005f0 = (org.apache.taglibs.standard.tag.rt.core.SetTag) _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.SetTag.class);
      _jspx_th_c_005fset_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fset_005f0.setParent(null);
      // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(11,0) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fset_005f0.setVar("basePath");
      // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(11,0) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
      // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(13,0) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fset_005f1.setVar("adminPath");
      // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(13,0) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fset_005f1.setValue(application.getAttribute(ExecueWebConstants.ADMIN_CONTEXT));
      int _jspx_eval_c_005fset_005f1 = _jspx_th_c_005fset_005f1.doStartTag();
      if (_jspx_th_c_005fset_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f1);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f1);
      out.write('\r');
      out.write('\n');

   response.setHeader("Pragma", "No-cache");
   response.setDateHeader("Expires", 0);
   response.setHeader("Cache-Control", "no-cache");
   response.setHeader("Cache-Control", "no-store");
   response.addHeader("Cache-Control", "post-check=0, pre-check=0");
   response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

      out.write("\r\n");
      out.write("<title>Semantifi|semantic search engine|semantic search|A portal\r\n");
      out.write("to publish and search datasets|search apps|enterprise search and\r\n");
      out.write("analytics</title>\r\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "../selectCss.jsp", out, true);
      out.write("\r\n");
      out.write("<meta name=\"robots\" content=\"index,follow\" />\r\n");
      out.write("<meta name=\"keywords\"\r\n");
      out.write("\tcontent=\"Semantic search, data sets, demographic search, Census 2000, business search , finance, census 2000 search, government, economics, shopping, travel,  sec  filling, FDIC backs, US Case Shiller Housing Indices, Federal Budget Actual And Forecast,  US Economic Aid, Gross Job Flows By Sector, Earmarks, Federal IT Spending\" />\r\n");
      out.write("<meta name=\"description\"\r\n");
      out.write("\tcontent=\"Semantifi is semantic search engine. Quickly find what you're searching on demography, finance, government, shopping, sports, travel. Shows automatic data summaries and visualizations to interpret database results and Index multi-terabyte data. Crawl any type of database to compose complex queries\" />\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n");
      out.write("\r\n");
      out.write("<link href=\"css/common/qiStyle_new.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n");
      out.write("<link rel=\"stylesheet\" href=\"css/common/roundedSearch.css\" type=\"text/css\" />\r\n");
      out.write("<link rel=\"stylesheet\" href=\"css/main/jquery.autocomplete.css\"\r\n");
      out.write("\ttype=\"text/css\" />\r\n");
      out.write("\r\n");
      out.write("<script language=\"JavaScript\" src=\"js/common/jquery.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"js/common/jquery.ui.all.js\"></script>\r\n");
      out.write("<script type='text/javascript'\r\n");
      out.write("\tsrc='");
      if (_jspx_meth_c_005fout_005f0(_jspx_page_context))
        return;
      out.write("/js/main/qi/jquery.autocomplete.freeform.js'></script>\r\n");
      out.write("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=6;\" />\r\n");
      out.write("\r\n");
      out.write("<!-- link href=\"css/main/semantifi.css\" rel=\"stylesheet\" type=\"text/css\" /-->\r\n");
      out.write("<link href=\"css/main/quinnox.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n");
      out.write("\r\n");
      out.write("<link href=\"css/main/home.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n");
      out.write("<link id=\"page_favicon\" href=\"favicon.ico\" rel=\"icon\"\r\n");
      out.write("\ttype=\"image/x-icon\" />\r\n");
      out.write("<link rel=\"shortcut icon\" href=\"favicon.ico\" type=\"image/x-icon\" />\r\n");
      out.write("</head>\r\n");

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
      out.write("<body>\r\n");
      out.write("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td class=\"top-bg\" align=\"right\" style=\"padding-right: 20px;\">\r\n");
      out.write("\t\t<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("                                    <tr>\r\n");
      out.write("                                        <td align=\"left\">\r\n");
      out.write("                                        \r\n");
      out.write("                                        \r\n");
      out.write("                                            <span\r\n");
      out.write("            id=\"showWelcome\"\r\n");
      out.write("            style=\"padding-right: 20px; color: #333;font-size:14px; padding-right: 18px; font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif; text-align: right;  margin: auto;\">\r\n");
      out.write("        </span>\r\n");
      out.write("            </td>\r\n");
      out.write("                                        <td> <a style=\"color:#3C71A1;\" href=\"");
      if (_jspx_meth_c_005fout_005f1(_jspx_page_context))
        return;
      out.write("swi/showSearchAppsDashboard.action\"\r\n");
      out.write("                                            class=\"menus\" >Publish Apps</a></td>\r\n");
      out.write("                              <td>&nbsp;| &nbsp;</td> \r\n");
      out.write("                                        <td width=\"70\" align=\"left\"><span class=\"menu-signin\">\r\n");
      out.write("                                        <span id=\"showLoginLink\" style=\"padding-left: 3px;color:#FF9505;\"><a\r\n");
      out.write("                                            href=\"javascript:;\" class=\"menus\" id=\"loginId\">Sign In</a> <a\r\n");
      out.write("                                            href=\"");
      if (_jspx_meth_c_005furl_005f0(_jspx_page_context))
        return;
      out.write("\"\r\n");
      out.write("                                            class=\"menus\" style=\"color:#FF9505;\" id=\"logoutId\"\r\n");
      out.write("                                            style=\"display: none; white-space: nowrap\">");
      if (_jspx_meth_s_005ftext_005f0(_jspx_page_context))
        return;
      out.write("</a> </span> <span\r\n");
      out.write("                                            id=\"loadingShowLoginLink\" style=\"display: none;\"><img\r\n");
      out.write("                                            src=\"images/main/loaderTrans50.gif\"></span></span></td>\r\n");
      out.write("                                    </tr>\r\n");
      out.write("                                </table>\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td align=\"center\" class=\"center-bg\">\r\n");
      out.write("\t\t<div id=\"innovaterAward\">\r\n");
      out.write("\r\n");
      out.write("\t\t<div style=\"margin-top: 5px;\"></div>\r\n");
      out.write("\r\n");
      out.write("\t\t<div style=\"margin-top: 5px;\"></div>\r\n");
      out.write("\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t<div id=\"govExpo\"><!--div style=\"margin-top:5px;\"><a   href=\"http://www.gov2expo.com/gov2expo2010\"  target=\"_blank\" title=\"Gov Expo\"><img src=\"images/gov20.jpg\" border=\"0\" /></a></div-->\r\n");
      out.write("\r\n");
      out.write("\t\t<div style=\"margin-top: 5px;\"></div>\r\n");
      out.write("\r\n");
      out.write("\t\t<div style=\"margin-top: 5px;\"></div>\r\n");
      out.write("\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<table width=\"850\" border=\"0\" align=\"center\" cellpadding=\"0\"\r\n");
      out.write("\t\t\tcellspacing=\"0\">\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td height=\"210\" class=\"centerImage\">\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t<table width=\"60%\" border=\"0\" align=\"center\" cellpadding=\"0\"\r\n");
      out.write("\t\t\t\t\tcellspacing=\"0\">\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t<td align=\"left\" style=\"padding-left: 50px;\">\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<form action='semanticSearch.action'\r\n");
      out.write("\t\t\t\t\t\t\tonSubmit=\"return submitDataFreeForm(true)\" accept-charset=\"utf-8\"\r\n");
      out.write("\t\t\t\t\t\t\tmethod=\"post\"><input type=\"hidden\" name=\"request\"\r\n");
      out.write("\t\t\t\t\t\t\tid=\"request\" /><input type=\"hidden\" name=\"type\" id=\"type\"\r\n");
      out.write("\t\t\t\t\t\t\tvalue=\"SI\" />\r\n");
      out.write("\t\t\t\t\t\t<table border=\"0\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\"\r\n");
      out.write("\t\t\t\t\t\t\twidth=\"auto\">\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t\t\t<td><input id=\"metrics\" tabindex=\"10\"  type=\"text\"\r\n");
      out.write("\t\t\t\t\t\t\t\t\tmaxlength=\"500\" class=\"inputBox\" title=\"Enter your search term\"\r\n");
      out.write("\t\t\t\t\t\t\t\t\tvalue=\"\" /></td>\r\n");
      out.write("\t\t\t\t\t\t\t\t<td align=\"left\">\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<div id=\"search_image\"\r\n");
      out.write("\t\t\t\t\t\t\t\t\tstyle=\"padding-top: 6px; margin-top: 1px;margin-left:6px\"><input\r\n");
      out.write("\t\t\t\t\t\t\t\t\tid=\"searchBtnFreeForm\"  type=\"image\"\r\n");
      out.write("\t\t\t\t\t\t\t\t\tsrc=\"images/quinnox/semantifi-search2.png\" /></div>\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t </td>\r\n");
      out.write("\t\t\t\t\t\t\t\t<td valign=\"bottom\"><img src=\"images/quinnox/beta.png\" /></td>\r\n");
      out.write("\t\t\t\t\t\t\t</tr>\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t\t\t<td align=\"right\" valign=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t\t<table width=\"100%\" ><tr><td style=\"color:#fff;text-align:left;\">\r\n");
      out.write("\t\t\t\t\t\t\t\tSemantic integration, reporting & search \r\n");
      out.write("\t\t\t\t\t\t\t\t</td><td align=\"right\" valign=\"middle\">\r\n");
      out.write("\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t\t<span\r\n");
      out.write("                                    id=\"showBookmarksSearchLink\" style=\"display: none;\"><a\r\n");
      out.write("                                    href=\"javascript:;\" class=\"advancedSearch\" title=\"Saved Queries\" alt=\"Saved Queries\" \r\n");
      out.write("                                    id=\"bookmarksSearchId\" style=\"\">Saved\r\n");
      out.write("                                Queries</a></span> <span id=\"loadingShowBookmarksSearchLink\"\r\n");
      out.write("                                    style=\"display: none; padding-left: 13px; padding-top: 5px;\"><img\r\n");
      out.write("                                    src=\"images/main/loaderTrans50.gif\" width=\"35px\" /></span> <span\r\n");
      out.write("                                    style=\"padding-left: 10px;\"><a\r\n");
      out.write("                                    href=\"execueHome.action?type=QI\" class=\"advancedSearch\"\r\n");
      out.write("                                    title=\"Advanced Search\" alt=\"Advanced Search\" style=\"margin-right: 0px;\">Advanced\r\n");
      out.write("                                Search</a></span> \r\n");
      out.write("\t\t\t\t\t\t\t\t</td></tr></table>\r\n");
      out.write("\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t\t\r\n");
      out.write("\t\t\t\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t\t\t\t\t<td align=\"right\" style=\"padding-right: 5px;\">&nbsp;</td>\r\n");
      out.write("\t\t\t\t\t\t\t\t<td></td>\r\n");
      out.write("\t\t\t\t\t\t\t</tr>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t</table>\r\n");
      out.write("\t\t\t\t\t\t<!-- <input type=\"hidden\" name=\"requestBookMark\" id=\"requestId\" />--><input\r\n");
      out.write("\t\t\t\t\t\t\ttype=\"hidden\"\r\n");
      out.write("\t\t\t\t\t\t\tvalue=\"");
      out.print(com.execue.core.common.type.BookmarkType.SEARCH_INTERFACE);
      out.write("\"\r\n");
      out.write("\t\t\t\t\t\t\tid=\"bookmarkType\" /></form>\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t</table>\r\n");
      out.write("\t\t\t\t<div id=\"hiddenPane\"\r\n");
      out.write("\t\t\t\t\tstyle=\"position: absolute; border-bottom: #666 solid 1px; border-right: #666 solid 1px; border-top: #fff solid 1px; border-left: #fff solid 1px; width: 350px; height: 200px; z-index: 2; display: none; padding: 5px 10px 10px 10px; background-color: #ffffff; width: 300px; height: 130px;\">\r\n");
      out.write("\t\t\t\t<div id=\"closeLinkDiv\"\r\n");
      out.write("\t\t\t\t\tstyle=\"width: 10px; float: right; cursor: pointer; color: #666; font-size: 11px; position: absolute; left: 300px;\">X</div>\r\n");
      out.write("\t\t\t\t<div id=\"hiddenPaneContent\"></div>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t<td height=\"33\" align=\"center\">\r\n");
      out.write("\t\t\t\t\t\t<div\r\n");
      out.write("\t\t\t\t\t\t\tstyle=\"height: 16px;width:32px; padding-top: 7px; z-index: 20; position: absolute;\"\r\n");
      out.write("\t\t\t\t\t\t\tid=\"waitImage\">\r\n");
      out.write("\t\t\t\t\t\t<div id=\"pleaseWaitDiv\"\r\n");
      out.write("\t\t\t\t\t\t\tstyle=\"display: none; margin: auto; width: 32px;\"><img\r\n");
      out.write("\t\t\t\t\t\t\tid=\"waiting_img\" src=\"images/main/Loader-main-page-3.gif\" width=\"32\"\r\n");
      out.write("\t\t\t\t\t\t\theight=\"32\" /></div>\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<div id=\"underProcess\">\r\n");
      out.write("\t\t\t\t\t\t<div style=\"padding: 10px;\">");
      if (_jspx_meth_s_005ftext_005f1(_jspx_page_context))
        return;
      out.write("</div>\r\n");
      out.write("\t\t\t\t\t\t<div style=\"padding-left: 30px;\"><span style=\"padding: 5px;\"><input\r\n");
      out.write("\t\t\t\t\t\t\tid=\"processYes\" type=\"button\" width=\"70\" style=\"width: 70px;\"\r\n");
      out.write("\t\t\t\t\t\t\tvalue=\"Yes\" /></span><span style=\"padding: 5px;\"><input\r\n");
      out.write("\t\t\t\t\t\t\ttype=\"button\" id=\"processNo\" style=\"width: 70px;\" value=\"No\" /></span></div>\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t<td align=\"left\" valign=\"bottom\">&nbsp;</td>\r\n");
      out.write("\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t<tr>\r\n");
      out.write("\t\t\t\t\t\t<td align=\"center\">\r\n");
      out.write("\t\t\t\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("\t\t\t\t\t\t\t<tr>\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t<td align=\"center\" style=\"padding-left: 60px;\"><!--span class=\"advancedSearch\" style=\"font-size:11px;\"><img src=\"images/findOut.png\"  /></span-->\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t\t\t\t\t<td width=\"50\" align=\"right\"><!--span id=\"moredata\" style=\"color:#FFF;font-size:12px;white-space:nowrap;\">( <a href=\"biSignUp.action\" class=\"linkWhite\">Signup</a> to view 12 years of data )</span--></td>\r\n");
      out.write("\t\t\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t\t</table>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t</td>\r\n");
      out.write("\t\t\t\t\t</tr>\r\n");
      out.write("\t\t\t\t\t\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t</table>\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</table>\r\n");
      out.write("  <div id=\"applicationListDiv\">\r\n");
      out.write(" \r\n");
      out.write("   </div>\t\r\n");
      out.write("\r\n");
      out.write("<div id=\"showBigImage\"\r\n");
      out.write("\tstyle=\"display: none; overflow: auto; width: auto;; min-height: 1px; height: auto; position: absolute; z-index: 10000; left: 0px; top: 0px; border: 1px solid #fff;\">\r\n");
      out.write("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td bgcolor=\"#000000\"><object width=\"640\" height=\"390\">\r\n");
      out.write("\t\t\t<param name=\"movie\"\r\n");
      out.write("\t\t\t\tvalue=\"http://www.youtube.com/v/8rB5a7zIah4&hl=en_US&feature=player_embedded&version=3\"></param>\r\n");
      out.write("\t\t\t<param name=\"allowFullScreen\" value=\"true\"></param>\r\n");
      out.write("\t\t\t<param name=\"allowScriptAccess\" value=\"always\"></param>\r\n");
      out.write("\t\t\t<embed\r\n");
      out.write("\t\t\t\tsrc=\"http://www.youtube.com/v/8rB5a7zIah4&hl=en_US&feature=player_embedded&version=3\"\r\n");
      out.write("\t\t\t\ttype=\"application/x-shockwave-flash\" allowfullscreen=\"true\"\r\n");
      out.write("\t\t\t\tallowScriptAccess=\"always\" width=\"640\" height=\"390\"></embed></object></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td bgcolor=\"#000000\" align=\"right\"\r\n");
      out.write("\t\t\tstyle=\"padding-right: 3px; padding-top: 3px; background: url(images/video_bg.png) no-repeat; height: 31px;\"><a\r\n");
      out.write("\t\t\thref=\"javascript:closeImage();\"><img\r\n");
      out.write("\t\t\tsrc=\"images/main/closeUserInfo.png\" border=\"0\" /></a></td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
      out.write("\r\n");
      out.write("<script>\r\n");
      out.write("var appCount='");
      if (_jspx_meth_s_005ftext_005f2(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("var applicationId='");
      if (_jspx_meth_s_005fproperty_005f0(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("var url=\"\";\r\n");
      out.write("var welcomeMessage='");
      if (_jspx_meth_s_005ftext_005f3(_jspx_page_context))
        return;
      out.write(" ';\r\n");
      out.write("var currentPosition=\"t1\";\r\n");
      out.write("var data='");
      if (_jspx_meth_s_005fproperty_005f1(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("var type = '");
      if (_jspx_meth_s_005fproperty_005f2(_jspx_page_context))
        return;
      out.write("'; \r\n");
      out.write("var isPublisher='");
      if (_jspx_meth_security_005fauthentication_005f0(_jspx_page_context))
        return;
      out.write("'; \r\n");
      out.write("var loginUserId='");
      if (_jspx_meth_security_005fauthentication_005f1(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("var fullName='");
      if (_jspx_meth_security_005fauthentication_005f2(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("var pricipalAdmin='");
      if (_jspx_meth_security_005fauthentication_005f3(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("var principalPublisher='");
      if (_jspx_meth_security_005fauthentication_005f4(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("var basePath='");
      if (_jspx_meth_c_005fout_005f2(_jspx_page_context))
        return;
      out.write("/';\r\n");
      out.write("var adminPath='");
      if (_jspx_meth_c_005fout_005f3(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("showApplications();\r\n");
      out.write("$(function(){\r\n");
      out.write("$(\"#metrics\").blur();\r\n");
      out.write("});\r\n");
      out.write("\r\n");
      out.write("</script>\r\n");
      out.write("<!--<script type=\"text/javascript\" src=\"js/main/home.js\"></script>-->");
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(41,6) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(85,77) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f1.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${adminPath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f1 = _jspx_th_c_005fout_005f1.doStartTag();
    if (_jspx_th_c_005fout_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f1);
    return false;
  }

  private boolean _jspx_meth_c_005furl_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:url
    org.apache.taglibs.standard.tag.rt.core.UrlTag _jspx_th_c_005furl_005f0 = (org.apache.taglibs.standard.tag.rt.core.UrlTag) _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.UrlTag.class);
    _jspx_th_c_005furl_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005furl_005f0.setParent(null);
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(91,50) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005furl_005f0.setValue("/j_spring_security_logout");
    int _jspx_eval_c_005furl_005f0 = _jspx_th_c_005furl_005f0.doStartTag();
    if (_jspx_th_c_005furl_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005furl_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005furl_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005furl_005f0);
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(93,87) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f0.setName("execue.logout.link");
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(211,34) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f1.setName("execue.cancel.request");
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(290,14) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f2.setName("execue.appCount");
    int _jspx_eval_s_005ftext_005f2 = _jspx_th_s_005ftext_005f2.doStartTag();
    if (_jspx_th_s_005ftext_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f2);
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(291,19) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f0.setValue("applicationId");
    int _jspx_eval_s_005fproperty_005f0 = _jspx_th_s_005fproperty_005f0.doStartTag();
    if (_jspx_th_s_005fproperty_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f0);
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(293,20) name = name type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftext_005f3.setName("execue.login.welcome.message");
    int _jspx_eval_s_005ftext_005f3 = _jspx_th_s_005ftext_005f3.doStartTag();
    if (_jspx_th_s_005ftext_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftext_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftext_005f3);
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(295,10) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f1.setValue("requestString");
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(296,12) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f2.setValue("type");
    int _jspx_eval_s_005fproperty_005f2 = _jspx_th_s_005fproperty_005f2.doStartTag();
    if (_jspx_th_s_005fproperty_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f2);
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(297,17) name = property type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_security_005fauthentication_005f0.setProperty("principal.user.isPublisher");
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(298,17) name = property type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_security_005fauthentication_005f1.setProperty("principal.username");
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(299,14) name = property type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_security_005fauthentication_005f2.setProperty("principal.fullName");
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(300,19) name = property type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_security_005fauthentication_005f3.setProperty("principal.admin");
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(301,24) name = property type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_security_005fauthentication_005f4.setProperty("principal.publisher");
    int _jspx_eval_security_005fauthentication_005f4 = _jspx_th_security_005fauthentication_005f4.doStartTag();
    if (_jspx_th_security_005fauthentication_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.reuse(_jspx_th_security_005fauthentication_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fsecurity_005fauthentication_0026_005fproperty_005fnobody.reuse(_jspx_th_security_005fauthentication_005f4);
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(302,14) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
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
    // /WEB-INF/jsp/main/qi/execueSimpleSearch.jsp(303,15) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f3.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${adminPath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f3 = _jspx_th_c_005fout_005f3.doStartTag();
    if (_jspx_th_c_005fout_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f3);
    return false;
  }
}
