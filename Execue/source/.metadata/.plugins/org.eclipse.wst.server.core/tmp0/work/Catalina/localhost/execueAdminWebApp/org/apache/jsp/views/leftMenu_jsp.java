package org.apache.jsp.views;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class leftMenu_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(1);
    _jspx_dependants.add("/WEB-INF/tlds/adminConsole.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fadmin_005fshowMenu_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fadmin_005fshowMenu_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fadmin_005fshowMenu_005fnobody.release();
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
      out.write("<style>\r\n");
      out.write(".bcLinks a {\r\n");
      out.write("\tpadding-left: 5px;\r\n");
      out.write("\tpadding-right: 5px;\r\n");
      out.write("\tfont-size: 11px;\r\n");
      out.write("\tcolor: #039;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".bcArrows {\r\n");
      out.write("\tpadding-left: 3px;\r\n");
      out.write("\tpadding-right: 3px;\r\n");
      out.write("\tfont-size: 11px;\r\n");
      out.write("\tfont-family: \"Lucida Sans Unicode\", \"Lucida Grande\", sans-serif;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".bcNoLinks {\r\n");
      out.write("\tpadding-left: 4px;\r\n");
      out.write("\tfont-size: 11px;\r\n");
      out.write("}\r\n");
      out.write("</style>\r\n");
      out.write("<div id=\"pageLoader\" style=\"padding:4px;padding-top:5px;display:none;z-index:1000000;position:absolute;border:none;width:50px;height:50px;\"><div style=\"width:32px;;margin:auto;\"><img id=\"waiting_img2\"   src=\"../images/admin/Loader-main-page-3.gif\" width=\"32\" height=\"32\" /></div><div style=\"width:100%;text-align:center;\">Loading</div></div>\r\n");
      out.write("<form id=\"leftMenuForm\" name=\"leftMenuForm\">\r\n");
      out.write("<DIV id=\"container-1\"\r\n");
      out.write("\tstyle=\"width: auto;  margin-top: 1px; margin-left: 0px;margin-right: 3px; padding: 2px; padding-top: 0px; padding-bottom: 6px; padding-left: 0px; background-color: #FFF; min-height: 420px; height: auto;\">\r\n");
      out.write("\r\n");
      out.write("<!-- %=request.getSession().getAttribute(\"adminHtmlMenu\")%-->\r\n");
      if (_jspx_meth_admin_005fshowMenu_005f0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("</DIV>\r\n");
      out.write("<input type=\"hidden\" id=\"clickedMenuItem\" name=\"clickedMenuItem\"/>\r\n");
      out.write("</form>\r\n");
      out.write("\r\n");
      out.write("<script>\r\n");
      out.write("var left=(screen.width)-(screen.width/2)-20;\r\n");
      out.write("var top=(screen.height)-(screen.height/2)-180;\r\n");
      out.write("$(document).ready(function() {\r\n");
      out.write("\r\n");
      out.write("\t$(\"#container-1 a\").click(function() {\r\n");
      out.write("\t\r\n");
      out.write("\tif($(this).attr(\"target\")!=\"_blank\"){\r\n");
      out.write("\t\tshowLoaderImageOnLoad();\r\n");
      out.write("\t\tvar openBranchs = \"\";\r\n");
      out.write("\t\tvar clickedPath = \"\";\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tclickedPath = $(this).attr(\"id\");\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t$.each($(\"div .branch:visible\"),function(index, data){\r\n");
      out.write("\t\t\tvar openPath = $(this).attr(\"id\");\r\n");
      out.write("\t\t\topenBranchs +=  \", \" + openPath;\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\tvar clickedMenuPath = clickedPath +\"#\" +openBranchs;\r\n");
      out.write("\t\t$(\"#clickedMenuItem\").val(clickedMenuPath);\r\n");
      out.write("\t\tdocument.leftMenuForm.action = $(this).attr(\"href\");\r\n");
      out.write("\t\tdocument.leftMenuForm.method = \"post\";\r\n");
      out.write("\t\t//$(this).attr(\"href\", \"#\");\r\n");
      out.write("\t\tdocument.leftMenuForm.submit();\r\n");
      out.write("\t\treturn false;\t\r\n");
      out.write("\t\t}\t\r\n");
      out.write("\t});\r\n");
      out.write("});\r\n");
      out.write("function hideLoaderImageOnLoad(){\r\n");
      out.write("\r\n");
      out.write("$(\"#waiting_img2\").hide();\r\n");
      out.write("}\r\n");
      out.write("function showLoaderImageOnLoad(){\r\n");
      out.write("tempImg = $(\"#waiting_img2\").attr(\"src\");\r\n");
      out.write("\t\t$(\"#pageLoader\").show().css(\"left\",left+\"px\");\r\n");
      out.write("\t\t$(\"#pageLoader\").css(\"top\",top+\"px\");\r\n");
      out.write("\t\tsetTimeout(function(){document.images[\"waiting_img2\"].src = tempImg; $(\"#waiting_img2\").show(); }, 1); \t\r\n");
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

  private boolean _jspx_meth_admin_005fshowMenu_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  admin:showMenu
    com.execue.web.core.util.AdminMenuPageImpl _jspx_th_admin_005fshowMenu_005f0 = (com.execue.web.core.util.AdminMenuPageImpl) _005fjspx_005ftagPool_005fadmin_005fshowMenu_005fnobody.get(com.execue.web.core.util.AdminMenuPageImpl.class);
    _jspx_th_admin_005fshowMenu_005f0.setPageContext(_jspx_page_context);
    _jspx_th_admin_005fshowMenu_005f0.setParent(null);
    int _jspx_eval_admin_005fshowMenu_005f0 = _jspx_th_admin_005fshowMenu_005f0.doStartTag();
    if (_jspx_th_admin_005fshowMenu_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fadmin_005fshowMenu_005fnobody.reuse(_jspx_th_admin_005fshowMenu_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fadmin_005fshowMenu_005fnobody.reuse(_jspx_th_admin_005fshowMenu_005f0);
    return false;
  }
}
