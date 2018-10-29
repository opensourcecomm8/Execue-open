package org.apache.jsp.views.main.search;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class home_002dlinks_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
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
      out.write("\r\n");
      out.write("<div id=\"hiddenPane\"\r\n");
      out.write("   style=\"position: absolute; width: 350px; height: 200px; z-index: 2; display: none;\">\r\n");
      out.write("      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"310\" align=\"left\" >\r\n");
      out.write("        <!-- fwtable fwsrc=\"boxTemplate.png\" fwpage=\"Page 1\" fwbase=\"boxTemplate.gif\" fwstyle=\"Dreamweaver\" fwdocid = \"901881679\" fwnested=\"0\" -->\r\n");
      out.write("        \r\n");
      out.write("<tr>\r\n");
      out.write("          <td width=\"15\"><img name=\"boxTemplate_r1_c1\"\r\n");
      out.write("     src=\"images/quinnox/boxTemplate_r1_c1.gif\" width=\"15\" height=\"40\"\r\n");
      out.write("     border=\"0\" id=\"boxTemplate_r1_c1\" alt=\"\" /></td>\r\n");
      out.write("          <td background=\"images/quinnox/boxTemplate_r1_c2.gif\"><img\r\n");
      out.write("     name=\"boxTemplate_r1_c2\" src=\"images/main/spacer.gif\"\r\n");
      out.write("     width=\"279\" height=\"40\" border=\"0\" id=\"boxTemplate_r1_c2\" alt=\"\"/></td>\r\n");
      out.write("          <td width=\"15\"><img\r\n");
      out.write("     name=\"boxTemplate_r1_c3\" src=\"images/quinnox/boxTemplate_r1_c3.gif\"\r\n");
      out.write("     width=\"15\" height=\"40\" border=\"0\" id=\"boxTemplate_r1_c3\"\r\n");
      out.write("     alt=\"Close\" title=\"Close\"/></td>\r\n");
      out.write("          <td width=\"13\"><img src=\"images/main/spacer.gif\" width=\"1\" height=\"15\"\r\n");
      out.write("     border=\"0\" alt=\"\" /></td>\r\n");
      out.write("          </tr>\r\n");
      out.write("        <tr>\r\n");
      out.write("          <td background=\"images/main/boxTemplate_r2_c1.gif\"><img\r\n");
      out.write("     name=\"boxTemplate_r2_c1\" src=\"images/main/boxTemplate_r2_c1.gif\"\r\n");
      out.write("     width=\"15\" height=\"10\" border=\"0\" id=\"boxTemplate_r2_c1\" alt=\"\" /></td>\r\n");
      out.write("          <td width=\"1045\" valign=\"top\" bgcolor=\"#FFFFFF\" >\r\n");
      out.write("            <div id=\"hiddenPaneContent\"></div>\r\n");
      out.write("            </td>\r\n");
      out.write("          <td background=\"images/main/boxTemplate_r2_c3.gif\" ><img\r\n");
      out.write("     name=\"boxTemplate_r2_c3\" src=\"images/main/boxTemplate_r2_c3.gif\"\r\n");
      out.write("     width=\"15\" height=\"1\" border=\"0\" id=\"boxTemplate_r2_c3\" alt=\"\" /></td>\r\n");
      out.write("          <td><img src=\"images/main/spacer.gif\" width=\"1\" height=\"10\"\r\n");
      out.write("     border=\"0\" alt=\"\" /></td>\r\n");
      out.write("          </tr>\r\n");
      out.write("        <tr>\r\n");
      out.write("          <td><img name=\"boxTemplate_r3_c1\"\r\n");
      out.write("     src=\"images/main/boxTemplate_r3_c1.gif\" width=\"15\" height=\"13\"\r\n");
      out.write("     border=\"0\" id=\"boxTemplate_r3_c1\" alt=\"\" /></td>\r\n");
      out.write("          <td background=\"images/main/boxTemplate_r3_c2.gif\" valign=\"top\"><img\r\n");
      out.write("     name=\"boxTemplate_r3_c2\" src=\"images/main/boxTemplate_r3_c2.gif\"\r\n");
      out.write("     width=\"25\" height=\"13\" border=\"0\" id=\"boxTemplate_r3_c2\" alt=\"\" /></td>\r\n");
      out.write("          <td><img name=\"boxTemplate_r3_c3\"\r\n");
      out.write("     src=\"images/main/boxTemplate_r3_c3.gif\" width=\"15\" height=\"13\"\r\n");
      out.write("     border=\"0\" id=\"boxTemplate_r3_c3\" alt=\"\" /></td>\r\n");
      out.write("          <td><img src=\"images/main/spacer.gif\" width=\"1\" height=\"13\"\r\n");
      out.write("     border=\"0\" alt=\"\" /></td>\r\n");
      out.write("          </tr>\r\n");
      out.write("        </table>\r\n");
      out.write("      </div>\r\n");
      out.write("      <script>\r\n");
      out.write("\t  $(function(){\r\n");
      out.write("\t\r\n");
      out.write("\t$(\"#closeButtonLink\").click(function(){\r\n");
      out.write("\t\t$(this).blur();\r\n");
      out.write("\t\t$(\"#hiddenPane\").fadeOut();\t\t\t\t\t\t\t\t \r\n");
      out.write("\t});\r\n");
      out.write("});\r\n");
      out.write("\t  </script>");
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
}
