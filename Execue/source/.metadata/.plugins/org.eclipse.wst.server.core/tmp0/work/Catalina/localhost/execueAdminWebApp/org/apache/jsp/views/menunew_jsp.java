package org.apache.jsp.views;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.execue.web.core.util.ExecueWebConstants;

public final class menunew_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(1);
    _jspx_dependants.add("/WEB-INF/tlds/adminConsole.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fadmin_005fshowBreadcrumb_0026_005fhomeName_005fhomeLink_005fassetDisplayName_005fappDisplayName_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fadmin_005fshowBreadcrumb_0026_005fhomeName_005fhomeLink_005fassetDisplayName_005fappDisplayName_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fadmin_005fshowBreadcrumb_0026_005fhomeName_005fhomeLink_005fassetDisplayName_005fappDisplayName_005fnobody.release();
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
      out.write("\r\n");

String mainPath= (String) application.getAttribute(ExecueWebConstants.MAIN_CONTEXT)+"/index.jsp";

      out.write("\r\n");
      out.write("\r\n");
      out.write("<script language=\"JavaScript1.2\" type=\"text/javascript\">\r\n");
      out.write("<!--\r\n");
      out.write("function MM_findObj(n, d) { //v4.01\r\n");
      out.write("  var p,i,x;  if(!d) d=document; if((p=n.indexOf(\"?\"))>0&&parent.frames.length) {\r\n");
      out.write("    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}\r\n");
      out.write("  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];\r\n");
      out.write("  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);\r\n");
      out.write("  if(!x && d.getElementById) x=d.getElementById(n); return x;\r\n");
      out.write("}\r\n");
      out.write("function MM_swapImage() { //v3.0\r\n");
      out.write("  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)\r\n");
      out.write("   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}\r\n");
      out.write("}\r\n");
      out.write("function MM_swapImgRestore() { //v3.0\r\n");
      out.write("  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("function MM_preloadImages() { //v3.0\r\n");
      out.write("  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();\r\n");
      out.write("    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)\r\n");
      out.write("    if (a[i].indexOf(\"#\")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("//-->\r\n");
      out.write("$(document).ready(function(){\r\n");
      out.write("\t\t\t\t\t\t   \r\n");
      out.write("\t$pathName=window.location.pathname;\r\n");
      out.write("\t\r\n");
      out.write("\thomePage=\"showConsole.action\";\r\n");
      out.write("\t///Data Sources ////\r\n");
      out.write("\taction1=\"showDataSources.action\";\r\n");
      out.write("\taction1a=\"showDataSource.action\";\r\n");
      out.write("\taction2=\"showAssets.action\";\r\n");
      out.write("\taction2a=\"showAsset.action\";\r\n");
      out.write("  actionDA3=\"showConstraints.action\";\r\n");
      out.write("  actionDA4=\"showJoins.action\";\r\n");
      out.write("\t///Data Sources ////\r\n");
      out.write("\t\r\n");
      out.write("\t///Business Terminology ////\r\n");
      out.write("\taction3=\"showBusinessTerms.action\";\r\n");
      out.write("\taction4=\"showProfiles.action\";\r\n");
      out.write("\taction5=\"showRanges.action\";\r\n");
      out.write("\t///Business Terminology ////\r\n");
      out.write("\t\r\n");
      out.write("\t///mapping////\r\n");
      out.write("\taction6=\"showAssetSelectionForMappings.action\";\r\n");
      out.write("\taction6a=\"showMappings.action\";\r\n");
      out.write("  actionMP6=\"showAssetsGrain\";\r\n");
      out.write("\t///mapping////\r\n");
      out.write("\t\r\n");
      out.write("\t///security////\r\n");
      out.write("\taction7=\"showRoles.action\";\r\n");
      out.write("\taction8=\"showGroups.action\";\r\n");
      out.write("\taction9=\"showUsers.action\";\r\n");
      out.write("\t///security////\r\n");
      out.write("\t\r\n");
      out.write("\tif($pathName.indexOf(homePage)>-1)$(\"#bi_home\").attr(\"src\",\"../images/admin/publisherHome_hover.jpg\");\r\n");
      out.write("\tif(($pathName.indexOf(action1)>-1)||($pathName.indexOf(action1a)>-1)||($pathName.indexOf(action2)>-1)||($pathName.indexOf(action2a)>-1)||($pathName.indexOf(actionDA3)>-1)||($pathName.indexOf(actionDA4)>-1))$(\"#bi_ds\").attr(\"src\",\"../images/bi_ds_f2.jpg\");\r\n");
      out.write("\tif(($pathName.indexOf(action3)>-1))$(\"#bi_bt\").attr(\"src\",\"../images/admin/pub/searchView_hover.jpg\");\r\n");
      out.write("\tif(($pathName.indexOf(action6)>-1)||($pathName.indexOf(action6a)>-1)||($pathName.indexOf(actionMP6)>-1))$(\"#bi_mapping\").attr(\"src\",\"../images/bi_mapping_f2.jpg\");\r\n");
      out.write("\tif(($pathName.indexOf(action7)>-1)||($pathName.indexOf(action8)>-1)||($pathName.indexOf(action9)>-1))$(\"#bi_system\").attr(\"src\",\"../images/admin/pub/adminSecurity_hover.jpg\");\r\n");
      out.write("\tif(($pathName.indexOf(action4)>-1)||($pathName.indexOf(action5)>-1))$(\"#bi_da\").attr(\"src\",\"../images/admin/pub/preferences_hover.jpg\");\r\n");
      out.write("  \r\n");
      out.write("\t});\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("<table width=\"1164\" border=\"0\" align=\"center\" cellpadding=\"0\"  cellspacing=\"0\">\r\n");
      out.write("  <tr>\r\n");
      out.write("    <td height=\"18\" align=\"left\" style=\"padding-left:1px;\" >");
      //  admin:showBreadcrumb
      com.execue.web.core.util.AdminBreadcrumbImpl _jspx_th_admin_005fshowBreadcrumb_005f0 = (com.execue.web.core.util.AdminBreadcrumbImpl) _005fjspx_005ftagPool_005fadmin_005fshowBreadcrumb_0026_005fhomeName_005fhomeLink_005fassetDisplayName_005fappDisplayName_005fnobody.get(com.execue.web.core.util.AdminBreadcrumbImpl.class);
      _jspx_th_admin_005fshowBreadcrumb_005f0.setPageContext(_jspx_page_context);
      _jspx_th_admin_005fshowBreadcrumb_005f0.setParent(null);
      // /views/menunew.jsp(79,60) name = homeName type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_admin_005fshowBreadcrumb_005f0.setHomeName("Semantifi");
      // /views/menunew.jsp(79,60) name = homeLink type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_admin_005fshowBreadcrumb_005f0.setHomeLink(mainPath );
      // /views/menunew.jsp(79,60) name = appDisplayName type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_admin_005fshowBreadcrumb_005f0.setAppDisplayName("App");
      // /views/menunew.jsp(79,60) name = assetDisplayName type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_admin_005fshowBreadcrumb_005f0.setAssetDisplayName("Dataset Collection");
      int _jspx_eval_admin_005fshowBreadcrumb_005f0 = _jspx_th_admin_005fshowBreadcrumb_005f0.doStartTag();
      if (_jspx_th_admin_005fshowBreadcrumb_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fadmin_005fshowBreadcrumb_0026_005fhomeName_005fhomeLink_005fassetDisplayName_005fappDisplayName_005fnobody.reuse(_jspx_th_admin_005fshowBreadcrumb_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fadmin_005fshowBreadcrumb_0026_005fhomeName_005fhomeLink_005fassetDisplayName_005fappDisplayName_005fnobody.reuse(_jspx_th_admin_005fshowBreadcrumb_005f0);
      out.write("</td>\r\n");
      out.write("  </tr>\r\n");
      out.write("</table>\r\n");
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
