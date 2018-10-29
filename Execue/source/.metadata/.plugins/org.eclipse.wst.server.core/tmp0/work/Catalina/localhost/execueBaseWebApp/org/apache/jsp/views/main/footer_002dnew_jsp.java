package org.apache.jsp.views.main;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class footer_002dnew_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.release();
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
      //  c:set
      org.apache.taglibs.standard.tag.rt.core.SetTag _jspx_th_c_005fset_005f0 = (org.apache.taglibs.standard.tag.rt.core.SetTag) _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.SetTag.class);
      _jspx_th_c_005fset_005f0.setPageContext(_jspx_page_context);
      _jspx_th_c_005fset_005f0.setParent(null);
      // /views/main/footer-new.jsp(3,0) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fset_005f0.setVar("basePath");
      // /views/main/footer-new.jsp(3,0) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_c_005fset_005f0.setValue(request.getContextPath());
      int _jspx_eval_c_005fset_005f0 = _jspx_th_c_005fset_005f0.doStartTag();
      if (_jspx_th_c_005fset_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f0);
      out.write("\r\n");
      out.write("<style>\r\n");
      out.write("a.footer-sublink-bottom {\r\n");
      out.write("\tfont-family: \"trebuchet ms\";\r\n");
      out.write("\tfont-size: 11px;\r\n");
      out.write("\tfont-weight: normal;\r\n");
      out.write("\tcolor: #3E5356;\r\n");
      out.write("\ttext-align: left;\r\n");
      out.write("\ttext-decoration:  underline;\r\n");
      out.write("}\r\n");
      out.write("a.footer-sublink-bottom:hover {\r\n");
      out.write("\tfont-family: \"trebuchet ms\";\r\n");
      out.write("\tfont-size: 11px;\r\n");
      out.write("\tfont-weight: normal;\r\n");
      out.write("\tcolor: #FFF;\r\n");
      out.write("\ttext-decoration: none;\r\n");
      out.write("\ttext-indent: 5;\r\n");
      out.write("}\r\n");
      out.write(".copyright {\r\n");
      out.write("\tfont-family: \"trebuchet ms\";\r\n");
      out.write("\tfont-size: 10px;\r\n");
      out.write("\tfont-weight: normal;\r\n");
      out.write("\tcolor: #FFFFFF;\r\n");
      out.write("\ttext-align: right;\r\n");
      out.write("\tmargin-right: 8px;\r\n");
      out.write("\tpadding-right: 8px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("#bFooter a{\r\n");
      out.write("color:#ffffff;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("</style>\r\n");
      out.write("<table width=\"100%\" border=\"0\" cellspacing=\"0\" id=\"bFooter\" cellpadding=\"0\">\r\n");
      out.write("      <tr>\r\n");
      out.write("        <td height=\"30\" valign=\"middle\" background=\"");
      if (_jspx_meth_c_005fout_005f0(_jspx_page_context))
        return;
      out.write("/images/quinnox/footer-bg-3.png\">\r\n");
      out.write("        \r\n");
      out.write("        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n");
      out.write("          <tr>\r\n");
      out.write("          <td width=\"1%\" height=\"30\" align=\"left\">&nbsp;</td>\r\n");
      out.write("            <td width=\"50%\" height=\"30\" align=\"left\">\r\n");
      out.write("             <!-- <a href=\"#\" class=\"footer-sublink-bottom\">Semantifi Company</a>&nbsp;&nbsp;\r\n");
      out.write("            <a href=\"javascript:openWIndow('http://www.semantifi.com/privacy.html');\"  class=\"footer-sublink-bottom\">Privacy policy</a>&nbsp;&nbsp;\r\n");
      out.write("           <a href=\"javascript:openWIndow('http://www.semantifi.com/disclaimer.html');\" class=\"footer-sublink-bottom\">Disclaimer</a>&nbsp;&nbsp;\r\n");
      out.write("              <a href=\"javascript:openWIndow('http://www.semantifi.com/terms.html');\"  class=\"footer-sublink-bottom\">Terms & conditions</a>&nbsp;--></td>\r\n");
      out.write("\r\n");
      out.write("            <td width=\"32%\" align=\"left\">&nbsp;</td>\r\n");
      out.write("            <td width=\"17%\" class=\"copyright\">Copyright &copy;   2009-2011  SEMANTIFI\r\n");
      out.write("          </td></tr>\r\n");
      out.write("        </table></td>\r\n");
      out.write("      </tr>\r\n");
      out.write("    </table>\r\n");
      out.write("    <script>\r\n");
      out.write("\tfunction openWIndow(url){\r\n");
      out.write("myRef = window.open(url,'mywin');\t\r\n");
      out.write("}\r\n");
      out.write("\t</script>");
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
    // /views/main/footer-new.jsp(38,52) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f0.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${basePath}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f0 = _jspx_th_c_005fout_005f0.doStartTag();
    if (_jspx_th_c_005fout_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f0);
    return false;
  }
}
