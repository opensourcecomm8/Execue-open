<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
#bFooter a{
color:#ffffff;
}
.copyright {
    font-family: "trebuchet ms";
    font-size: 10px;
    font-weight: normal;
    color: #FFFFFF;
    text-align: right;
    margin-right: 8px;
    padding-right: 8px;
}
</style>
<c:set var="basePath" value="<%=request.getContextPath()%>" />

<table width="100%" border="0" cellspacing="0" id="bFooter" cellpadding="0">
      <tr>
        <td height="30" valign="middle" background="<c:out value='${basePath}' />/images/quinnox/footer-bg-3.png">
        
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
          <td width="1%" height="30" align="left">&nbsp;</td>
            <td width="50%" height="30" align="left"><!-- <a href="privacy.html" class="footer-sublink-bottom">Privacy policy</a>&nbsp;&nbsp;
           <a href="disclaimer.html" class="footer-sublink-bottom">Disclaimer</a>&nbsp;&nbsp;
              <a href="terms.html" class="footer-sublink-bottom">Terms & conditions</a>&nbsp;--></td>

            <td width="32%" align="left">&nbsp;</td>
            <td width="17%" class="copyright">Copyright &copy;   2009-2011  SEMANTIFI
          </td></tr>
        </table></td>
      </tr>
    </table>
