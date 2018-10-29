<%-- ************************************************************************************* 
*                             <ExcCueLite - Reporting views>
*                                    www.vbsoftindia.com
* 
* Document author : Ankur M. Bhalodia ,Jaimin Bhavsar
* File Name   : ReportViewer3.jsp
* Perpose : For Rendering Charts
* Module : ExeCueLite
* Dependencies : ex.css, exe-all.css , ExeCueScript.js 
* Created on :  22 January,2009
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.softwarefx.chartfx.server.Pane"%>
<%@page import="com.softwarefx.chartfx.server.ChartServer"%>
<%@page import="com.softwarefx.chartfx.server.SeriesAttributes"%>
<%@page import="com.softwarefx.chartfx.server.Gallery"%>
<%@page import="com.softwarefx.chartfx.server.MarkerShape"%>
<%@page import="com.softwarefx.chartfx.server.BarShape"%>
<%@page	import="com.softwarefx.chartfx.server.dataproviders.XmlDataProvider"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.EnumSet,java.util.ArrayList"%>
<%@page import="com.execue.handler.bean.presentation.FormBeans"%>
<%@page import="com.execue.reporting.presentation.service.impl.ConvertionForChart"%>
<%@page import="com.execue.reporting.presentation.service.impl.ChartAppearance"%>
<%@page import="com.softwarefx.chartfx.server.AxisFormat"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.softwarefx.chartfx.server.ContentLayout"%>
<%@page import="com.softwarefx.chartfx.server.AxisStyles"%>
<%@page import="com.softwarefx.chartfx.server.AxisPosition"%>
<style>
html, body {
	font-family:Arial;
}
table td {
	font-size: 9pt;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
a {
	font-size:10pt;
}
.textArrow {
	font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
	font-size:11px;
	padding-left:3px;
}
.subHeading {
	font-family:Arial, Helvetica, sans-serif;
	font-size:12px;
	font-weight:bold;
	padding:3px;
	color:#333;
}
.request {
	color:#666
}
.source {
	color:#666;
}
.top-bg {
	background-image: url(images/header_top.png);
	background-repeat: repeat-x;
	background-position: left bottom;
	height: 25px;
	background-color: #FBFBFB;
}
.technology-1 {
	background-image: url(images/top-bg-inner-2.png);
	background-repeat: repeat-x;
	background-position: left top;
	height:51px;
}
.breadCrumbLink,.breadCrumbNoLink,.linkDescription{
font-size:8pt;	
}
#disclaimerDetails {
	position: absolute;
	width: 800px;
	height: 530px;
	z-index: 1001;
	border: 1px #CCC solid;
	display: none;
	background-color: #FFF;
}

#disclaimerDetailsCloseDiv {
	width: 750px;
	float: right;
	padding: 10px;
}

#disclaimerDetailsCloseDiv a {
	float: right;
}

#disclaimerDetailsContent {
	overflow: auto;
	padding: 20px;
	width: 750px;
	height: 440px;
}
a.greenLink{
color: green; font-size: 9pt;text-decoration:none;	
}
</style>

<script language="JavaScript" src="js/jquery.js"></script>
<script>
$(document).ready(function () {
	var type = 'SI';	
	if(type == '<%=request.getParameter("type")%>')
    	$("#metrics").val('<%=request.getParameter("requestedString")%>');	
});
function  backToHomePage(){
	 var xmlValue = '<%=request.getParameter("requestedString")%>';		 	 
	 var type = '<%=request.getParameter("type")%>';	 
	var url="";	
	 if(type=="QI"){
	   url="returnToHome.action?type=QI";
	 }else{
	   url="returnToHome.action?type=SI";
	 }
	 $('#requestString').val(xmlValue);
	 $('#typeId').val(type);	
	 document.backToHome.method="post";	
	 document.backToHome.action=url;
	 document.backToHome.submit();
}
</script>

<%@page import="com.softwarefx.chartfx.server.TitleDockable"%>
<%@page import="com.softwarefx.chartfx.server.DockArea"%>
<%@page import="com.softwarefx.StringAlignment"%>
<%@page import="com.softwarefx.chartfx.server.internal.Internal.GalleryAttribute"%>
<%@page import="com.softwarefx.chartfx.server.galleries.Pie"%>
<%@page import="com.softwarefx.chartfx.server.galleries.ExplodingMode"%>
<%@page import="com.softwarefx.chartfx.server.AxesStyle"%>
<%@page import="com.softwarefx.chartfx.server.PointLabelAttributes"%>
<%@page import="com.softwarefx.chartfx.server.PointAttributes"%>
<%@page import="com.softwarefx.chartfx.server.PointAttributesCollection"%>
<%@page import="java.util.Iterator"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>
Semantifi Reports
</title>
<link rel="stylesheet" type="text/css" href="./common/scripts/ext-2.2.1/resources/css/ext-all.css" />
<script type="text/javascript" src="./common/scripts/ext-2.2.1/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="./common/scripts/ext-2.2.1/ext-all.js"></script>
<script type="text/javascript" src="./js/reporting/reporting.js"></script>
<script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js?pub=xa-4a9febfe48560f52"></script>
<script type="text/javascript">
       	function openNewWindow(){
       		obj = window.open('#','ViewXML','width=450,height=250,scrollbars=1');
       		obj.document.write('<html><body><textarea rows=10 cols=40>');
       		obj.document.write('<%=request.getAttribute("xmlReportData").toString().replace("\r","").replace("\n","").replace("\t","").replaceAll("\\s\\s","")%>');
       		obj.document.write('</textarea></body></html>');
       	}
       	 var addthis_config = {
	           services_compact: 'facebook, linkedin, twitter, more',
	           services_exclude: 'print'
	         }
       </script>       
<link href="css/reporting/view.css" rel="stylesheet" type="text/css" />
<!--<script type="text/javascript" src="./common/scripts/ext-2.2.1/examples/grid/xml-grid.js"></script> -->
</head>

<jsp:include page="../../../views/search/qi-head-simpleSearch.jsp"  flush="true" />

<body style="background-image:none; background-color:#ffffff;">
<div id="disclaimerDetails">
<div id="disclaimerDetailsCloseDiv"><a href="#"
	id="disclaimerDetailsClose">Close</a></div>
<div id="disclaimerDetailsContent"></div>
</div>
<div class="mainDiv" style="margin-left: 0px;">
<s:set name="formBeans" value="formBean" scope="request"/>
<s:set name="chartTypeString" value="chartTypeString" scope="request"/>
<%
           try{
               ChartAppearance chartAppearance = new ChartAppearance();
               String chartName= request.getAttribute("reportTitle").toString();
               System.out.println("title :"+chartName+" source:"+request.getParameter("source")+"  chartTypes:: "+request.getAttribute("chartTypeString"));
               String chartTypeString = (String) request.getAttribute("chartTypeString");
               FormBeans formBeans = (FormBeans) request.getAttribute("formBeans");
               String[] chartType = chartTypeString.split(",");
        %>
<table width="100%" cellpadding="0" cellspacing="0" border="0" bgcolor="#FFFFFF" >
  <tr>
    <td class="top-bg">
    
    
    
    
    
    <div style="margin:auto;width:91%">
                
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                    
                      <td width="100%" align="center">
                      
                      
                      <table width="100%" border="0" cellspacing="0" cellpadding="3" align="right">
                          <tr>
                            <td width="41%" height="20" align="right"><!-- sub links starts -->
                              <!--div id="pleaseWaitDiv" style="display: none; margin: auto; width: 133px; margin-top: 1px; margin-bottom: 0px;height:10px;float:right;"> <img id="waiting_img" src="images/wait1e.gif"  /> </div-->
                              <div style="height:16px;padding-top:2px;z-index:20;position:absolute;" id="waitImage"><div id="pleaseWaitDiv" style="display: none; margin: auto; width: 110px;">
								<img id="waiting_img" src="images/Loader-main-page-3.gif" width="22" height="22" />
							</div>
                        </div></td>
                          
                                    
                                    
                                   
                            
                           
                                    <td width="40%" align="right"><div id="showWelcome" style="padding-right:20px;color:#333;padding-top:0px;font-family:'Trebuchet MS', Arial, Helvetica, sans-serif;text-align:right;width:200px;float:right;">
   </div></td>
                          <td width="9%" align="right"><a href="feedback.jsp"><s:text name="execue.feedback.link" /></a></td>
                          
                          <td width="1%" id="publisherTdSeperator" style="display:none">|</td>
                          <td width="5%" align="center" id="publisherTd" style="display:none">
						<div id="showAdminLink"><a href="swi/showConsole.action"
							id="adminId"><s:text name="execue.publisher.link" /></a></div>
						<div id="loadingShowLoginLink1" style="display: none;"><img
							src="images/loaderTrans.gif"></div>
						</td>
                          
                          
                          
                          <td width="1%">|</td> 
                            <td width="6%" align="left" >
                            
                            <span id="showLoginLink" style="padding-left:3px;"><a href="javascript:;" class="links_sem3"
   id="loginId" ><s:text name="execue.login.link" /></a>
  <a href="<c:url value='/j_spring_security_logout'/>" class="links_sem3"
   id="logoutId" style="display: none;"><s:text name="execue.logout.link" /></a></span>
  <span id="loadingShowLoginLink" style="display: none;"><img
   src="images/loaderTrans.gif" ></span>
                            
                            </td>
                            <td width="1%"></td>
                          </tr>
                        </table>
                        
                        
                        </td>
                    </tr>
                </table>
                
                </div>
    
    </td>
  </tr>
 <tr >
    <td class="technology-1" height="51" >
    <div style="margin:auto;width:91%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="8%" valign="top"><div style="padding-top:10px;padding-left:0px;padding-right:0px;padding-bottom:1px;"><a href="index.jsp"><img name="index_r1_c1" src="images/inner-page-logo.png" border="0"
					id="index_r1_c1" alt="" /></a></div></td>
    <td width="92%" align="left"> <jsp:include page="../../../freeFormSearch.jsp"  flush="true" /></td>
  </tr>
</table>
</div>

      </td>
  </tr>
  
</table>

 <div style="width: 90%; margin: auto; background-color: #FFF;"
	id="bCrumb">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  
<tr>
   	<td height="25" colspan="2" align="center" background="images/bc-bg.jpg" valign="top" >
    <form name="backToHome" method="post">
    <table width="100%"><tr><td height="25" align="left" valign="top" >
    <script type="text/javascript">
    if(history.length > 1){
    	window.document.write('<span style="padding-left:0px;color:#333;font-size:14px;"> <a href="#" class="breadCrumbLink" id="backTohome" onclick="backToHomePage();">Home</a> <Span class="textArrow"> >> </Span> <a class="breadCrumbLink" href="javascript:history.back(-1);" style = "margin-left: 7px;">Results</a> <Span class="textArrow"> >> </Span> <span class="breadCrumbNoLink">Reports</span></span></td><td align="right" valign="top" >&nbsp;');
    }
    </script>
    
  
          
          
          </td></tr></table>
          <s:hidden id="requestString" name="requestString"/>
		  <s:hidden id="typeId" name="type"/>
          </form>
          </td>
  	</tr></table></div>
    
    
    
    
    <div id="subHeader" style="width:90%;margin:auto;background-color:#FFF;">
    <table width="auto" border="0" cellspacing="0" cellpadding="5" align="center" >
      <tr>
        
        <td><table align="center">
            <tr>
              <td align="center"><div id="IconView" >
                  <%
            ChartServer chart1 = null;
            String tempId="";
            String id ="";
            String filePath = "";                   
            XmlDataProvider cfxXML1=null ;
            if((chartType[0]).length() > 0) {
            for(int i=0;i<chartType.length;i++){
                 String onClickData ="" ,onMouseOverData="" ,classData="" ,idData="",img="" ,chartList="";     
              try{
                 if (!chartType[i].equals("1") && !chartType[i].equals("4") && !chartType[i].equals("5") && !chartType[i].equals("6")
                          && !chartType[i].equals("20") && !chartType[i].equals("21") && !chartType[i].equals("99")
                          && !chartType[i].equals("98")) {
                     cfxXML1 = new XmlDataProvider();
	                 chart1 = new ChartServer(pageContext, request, response);
	                               
	                 if(chartType[i].equals("7") || chartType[i].equals("8")){
	                	 cfxXML1.loadXML(formBeans.getCrossLinePath()); 
			         }
	                 /*else if(chartType[i].equals("18") || chartType[i].equals("19")){
	                	 cfxXML1.loadXML(formBeans.getCmMultiLinePath());
	                 }*/
	                 else{
	                	 filePath = formBeans.getPath();
	                	 cfxXML1.loadXML(filePath);
	                 }
	                 chart1.getDataSourceSettings().setDataSource(cfxXML1);
	                 classData="activeIcon";
                 }
              }catch(Exception e){
                 e.printStackTrace();
                 out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
              }
                 if(id !=""){
                     if(tempId == "")
                         tempId=id;
                     else
                         tempId = tempId + ","+id;
                 }
                 //------- Grid
                if(chartType[i].equals("1")){ 
                    id = "Grid";
                    System.out.println(" Grid ");
                   %>
                  <img id="gridIcon"  class="activeIcon" style="height:60px;width:80px;border-width:0px; margin-top: 7px;" src="images/reporting/ngrid.png" onclick="chartView('<%=id%>');" onmouseover="chartView('<%=id%>');" />
                  <% 
                  continue;
				 }
                else if(chartType[i].equals("4_NOTUSED"))
                {
                   id = "PivotTable";
             %>
                  <img id="pivotTable1" class="activeIcon"  style="height:60px;width:80px;border-width:0px; margin-top: 7px;" src="images/reporting/pivot.png" onclick="chartView('<%=id%>');" onmouseover="chartView('<%=id%>');" />
                  <%    
                   continue;
                }  

                 //------- GroupBYGrid
                 else if(chartType[i].equals("5")){
                    id = "GroupBYGrid";
             %>
                  <img id="groupGridIcon"  class="activeIcon" style="height:60px;width:80px;border-width:0px; margin-top: 7px;" src="images/reporting/groupgrid.png" onclick="chartView('<%=id%>');" onmouseover="chartView('<%=id%>');" />
                  <%
                    continue;
                 }
                 //------- AcrossGrid
                  else if(chartType[i].equals("6")){
                    id = "AcrossGrid";
             %>
                  <img id="acrossGridIcon"  class="activeIcon" style="height:60px;width:80px;border-width:0px; margin-top: 7px;" src="images/reporting/acrossgrid.png" onclick="chartView('<%=id%>');" onmouseover="chartView('<%=id%>');" />
                  <%
                    continue;
                 }  
                //------- LinesChart                        
                else if(chartType[i].equals("3")){
                    id="LinesChart";
                    chart1.getAllSeries().setGallery(Gallery.LINES);	
               }
               //------- BarChart
               else if(chartType[i].equals("2")){
                    id = "BarChart"; 
                    chart1.getAllSeries().setGallery(Gallery.BAR);	
                    chart1.getAllSeries().setBarShape(BarShape.RECTANGLE);
               }
                //------- multiLinesChart
                else if(chartType[i].equals("14")){
                  try{
                    id="multiLinesChart";
                	chart1.getAllSeries().setGallery(Gallery.LINES);	
                    int noOfCol = chart1.getData().getSeries();
                    int chartCntr = 0;
                    int lastIndex = 0;
                    ArrayList colAList = new ArrayList();
                    ArrayList colNewAList = new ArrayList();
                    if(noOfCol >= 3){
                         for(int cntr =0; cntr < noOfCol ; cntr++){
                            colAList.add(chart1.getSeries().get(cntr));
                         }
                         for(int cntr =0; cntr < colAList.size() ; cntr++){
                             if( colAList.get(cntr).toString().toUpperCase().endsWith(("_COUNT")) || colAList.get(cntr).toString().toUpperCase().endsWith(("_CNT"))){ 
                                 lastIndex = cntr;
                             }
                             else{
                                 colNewAList.add(cntr);
                             }
                             if(cntr == colAList.size()-1){
                                 colNewAList.add(lastIndex);
                             }
                         }						 
                         SeriesAttributes serie5;				 
                         serie5 = chart1.getSeries().get(new Integer(colNewAList.get(colNewAList.size()-1).toString()));
                         serie5.sendToBack();
                         serie5.setVisible(false);
                         for(int cntr =0; cntr < colNewAList.size()-1 ; cntr++){
                        	 SeriesAttributes serie3;
                             serie3 = chart1.getSeries().get(new Integer(colNewAList.get(cntr).toString()));
                             serie3.setAxisY(chart1.getAxisY2());
                         }
                    }
                    else{
                        SeriesAttributes serie3;
                        serie3 = chart1.getSeries().get(0);
                        serie3.setAxisY(chart1.getAxisY2());
                    }
                }catch(Exception e){
                   e.printStackTrace();
                   out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
                }
               }
                 
                 //------- multiBarChart
               else if(chartType[i].equals("11")){
                  try{
                    id = "multiBarChart";
                    chart1.getAllSeries().setGallery(Gallery.BAR);	
                    chart1.getAllSeries().setBarShape(BarShape.RECTANGLE);
                	int noOfCol = chart1.getData().getSeries();
                    int chartCntr = 0;
                    int lastIndex = 0;
                    ArrayList colAList = new ArrayList();
                    ArrayList colNewAList = new ArrayList();

                	System.out.println("inside small image Multibar ELSE");
                    
                    SeriesAttributes serie3;
                    if (noOfCol == 1)
                    {
                		serie3 = chart1.getSeries().get(0);
                    }
                    else
                    {
                    	serie3 = chart1.getSeries().get(1);
                    }
                	serie3.setAxisY(chart1.getAxisY2());
                  }catch(Exception e){
                     e.printStackTrace();
                     out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
                  }
                }
                 //------- CrossBarChart
            else if(chartType[i].equals("7")){
               try{
                    id = "CrossBarChart";
                    chart1.getAllSeries().setGallery(Gallery.BAR);	
                    chart1.getAllSeries().setBarShape(BarShape.RECTANGLE);
                    ConvertionForChart convertionForChart = new ConvertionForChart();
                    int panSize = convertionForChart.PANSIZEFORCROSS;
                    int xData = chart1.getData().getSeries();
                    int panLoop = xData/panSize;
                    for(int j=0;j< xData; j= j+panLoop ){   
                           Pane pane = new Pane();
                           pane.setSeparation(50);
                           if(j==0){
                              pane = chart1.getPanes().get(j);
                              for(int k = 0;k<panLoop;k++){
                                chart1.getSeries().get(j).setPane(pane);
                              }
                           }
                           else{
                              chart1.getPanes().add(pane);
                              for(int k = 0;k<panLoop;k++){
                                chart1.getSeries().get(j+k).setPane(pane);
                                pane.setSeparation(2);
                              }
                           }
                          pane.getAxisY().setVisible(false);
                      }
               }catch(Exception e){
                  e.printStackTrace();
                  out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
               }
            }
                 //------- CrossLineChart
                else if(chartType[i].equals("8")){
                   try{
                    System.out.println("In cross line chart...");
                    id = "CrossLineChart"; 
                    ConvertionForChart convertionForChart = new ConvertionForChart();
                    chart1.getAllSeries().setGallery(Gallery.LINES);	
                    chart1.setGallery(Gallery.LINES);
                    int panSize = convertionForChart.PANSIZEFORCROSS;
                    int xData = chart1.getData().getSeries();
                    int panLoop = xData/panSize;
                    System.out.println("pansize :"+panSize+" xdata:"+xData+" panloop:"+panLoop);
                    for(int j=0;j< xData; j= j+panLoop ){   
                           Pane pane = new Pane();
                           pane.setSeparation(50);
                           if(j==0){
                              pane = chart1.getPanes().get(j);
                              for(int k = 0;k<panLoop;k++){
                                chart1.getSeries().get(j).setPane(pane);
                              }
                           }
                           else{
                              chart1.getPanes().add(pane);
                              for(int k = 0;k<panLoop;k++){
                                chart1.getSeries().get(j+k).setPane(pane);
                                pane.setSeparation(2);
                              }
                           }
                          pane.getAxisY().setVisible(false);
                      }
                    System.out.println("In cross line chart...");
                   }catch(Exception e){
                      e.printStackTrace();
                      out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
                   }
                } 
                //------- CmMultiBarChart                 
                else if(chartType[i].equals("18")){
                   try{
                    id = "CmMultiBarChart"; 
                    chart1.getAllSeries().setGallery(Gallery.BAR);	
                    chart1.getAllSeries().setBarShape(BarShape.RECTANGLE);
                   }catch(Exception e){
                      e.printStackTrace();
                      out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
                   }
                }
                 //------- CmMultiLinesChart
                else if(chartType[i].equals("19")){
                  try{
                    id = "CmMultiLinesChart"; 
                    ConvertionForChart convertionForChart = new ConvertionForChart();
                    chart1.getAllSeries().setGallery(Gallery.LINES);	
                    int panSize =convertionForChart.PANSIZE; 
                    int xData = chart1.getData().getSeries();
                    int panLoop = xData/panSize;
                    for(int j=0;j< xData; j= j+panLoop ){   
                       Pane pane = new Pane();
                       if(j==0){
                          pane = chart1.getPanes().get(j);
                          for(int k = 0;k<panLoop;k++){
                            chart1.getSeries().get(j).setPane(pane);
                          }
                       }
                       else{
                          chart1.getPanes().add(pane);
                          for(int k = 0;k<panLoop;k++){
                            chart1.getSeries().get(j+k).setPane(pane);
                            pane.setSeparation(2);
                          }
                       }
                       pane.getAxisY().setVisible(false);
                   }
                  }catch(Exception e){
                     e.printStackTrace();
                     out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
                  }
                }
                //------- CMultiLineChart                                                                               
                else if(chartType[i].equals("16")){
                   try{
                    id = "CMultiLineChart"; 
                    chart1.setGallery(Gallery.LINES);
                    int xData = chart1.getData().getSeries();
                    for(int j=0;j< xData;j++ ){   
                       Pane pane = new Pane();
                       if(j==0){
                           pane = chart1.getPanes().get(j);
                           chart1.getSeries().get(j).setPane(pane);
                       }
                       else{
                           pane.setSeparation(2);
                           chart1.getPanes().add(pane);
                           chart1.getSeries().get(j).setPane(pane);
                       }
                       pane.getAxisY().setVisible(false);
                    }
                   }catch(Exception e){
                      e.printStackTrace();
                      out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
                   }
                }
                //------- CMultiBarChart                  
                else if(chartType[i].equals("13")){
                   try{
                    id = "CMultiBarChart"; 
                    chart1.setGallery(Gallery.BAR);
                    chart1.getAllSeries().setBarShape(BarShape.RECTANGLE);
                    int xData = chart1.getData().getSeries();
                    for(int j=0;j< xData;j++ ){   
                       Pane pane = new Pane();
                       if(j==0){
                           pane = chart1.getPanes().get(j);
                           chart1.getSeries().get(j).setPane(pane);
                       }
                       else{
                           pane.setSeparation(2);
                           chart1.getPanes().add(pane);
                           chart1.getSeries().get(j).setPane(pane);
                       }
                       pane.getAxisY().setVisible(false);
                    }
                   }catch(Exception e){
                      e.printStackTrace();
                      out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
                   }
                 }
                //------- ClusterBarChart
                  else if(chartType[i].equals("12")){
                     try{
                    id = "ClusterBarChart"; 
                    chart1.setGallery(Gallery.BAR);
                    chart1.getAllSeries().setBarShape(BarShape.RECTANGLE);
                    int xData = chart1.getData().getSeries();
                    for(int j=0;j< xData;j++ ){   
                       Pane pane = new Pane();
                       if(j==0){
                           pane = chart1.getPanes().get(j);
                           chart1.getSeries().get(j).setPane(pane);
                       }
                       else{
                           pane.setSeparation(2);
                           chart1.getPanes().add(pane);
                           chart1.getSeries().get(j).setPane(pane);
                       }
                       pane.getAxisY().setVisible(false);
                    }
                     }catch(Exception e){
                        e.printStackTrace();
                        out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
                     }
                 } 
                 //------- ClusterLinesChart
                 else if(chartType[i].equals("15")){
                    try{
                    id = "ClusterLinesChart"; 
                    int xData = chart1.getData().getSeries();
                    for(int j=0;j< xData;j++ ){   
                       Pane pane = new Pane();
                       if(j==0){
                           pane = chart1.getPanes().get(j);
                           chart1.getSeries().get(j).setPane(pane);
                       }
                       else{
                           pane.setSeparation(2);
                           chart1.getPanes().add(pane);
                           chart1.getSeries().get(j).setPane(pane);
                       }
                       pane.getAxisY().setVisible(false);
                    }
                    }catch(Exception e){
                       e.printStackTrace();
                       out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
                    }
                }
                //------- BarLinesChart
                 else if(chartType[i].equals("10")){
                    try{
                     id = "BarLinesChart";
                     int noOfCol = chart1.getData().getSeries();
                     int chartCntr = 0;
                     int lastIndex = 0;
                     ArrayList colAList = new ArrayList();
                     ArrayList colNewAList = new ArrayList();
                     for(int alCntr =0 ; alCntr< noOfCol ; alCntr++)
                     {
                     	if(alCntr == 0)
                     	{
                     		//System.out.println("Jai:::: "+alCntr);
                     		//System.out.println("ALCNTR :: "+colAList.get(alCntr));
                     		SeriesAttributes serie3 = chart1.getSeries().get(alCntr);
                     		serie3.setGallery(Gallery.BAR);	
                             serie3.setBarShape(BarShape.RECTANGLE);
                             serie3.getAxisY().getLabelsFormat().setFormat(AxisFormat.NUMBER);                        
                     	}
                     	else
                     	{
                     		//System.out.println("Jai:::: "+alCntr);
                     		SeriesAttributes serie4 = chart1.getSeries().get(alCntr);
                     		serie4.setGallery(Gallery.LINES);
                     		serie4.setAxisY(chart1.getAxisY2());
                     		serie4.bringToFront();
                     	}
                     }
                     chart1.getAxisY2().setVisible(false);
                     chart1.getAxisY().setMax(chart1.getAxisY().getMax()+( 10 * chart1.getAxisY().getMax())/100);
                     chart1.getPlotAreaMargin().setBottom(1); 
                     chart1.getPlotAreaMargin().setTop(1); 
                     chart1.getPlotAreaMargin().setLeft(1); 
                     chart1.getPlotAreaMargin().setRight(1); 
                    }catch(Exception e){
                       e.printStackTrace();
                       out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
                    }
                 }  
                 else{
                     continue;
                 }
                 onClickData = "chartView('"+id+"')";
                 onMouseOverData = "chartView('"+id+"')";                                                               
        %>
                  <a  onclick="<%=onClickData%>" onmouseover="<%=onMouseOverData%>;" >
                  <%   
                    // code for small image
                    chartAppearance.setPropertiesForSmallCharts(chart1);                                     
                    chart1.renderControl();	
        %>
                  </a>
                  <% }
               if(tempId == ""){
                   if(id=="")
                      tempId = "";
                   else
                      tempId = id;
               }
               else{
                   tempId = tempId +","+id;
               }
               //--------Detail reports
               if(formBeans.getDetailReport() != null && formBeans.getDetailReport().length() > 1){ 
                 id = "DetailReport";
                %> <img id="detailGridIcon" class="activeIcon"
   					style="height: 60px; width: 80px; border-width: 0px; margin-top: 7px;"
   					src="images/reporting/detailedgrid.png" onclick="chartView('<%=id%>');"
   					onmouseover="chartView('<%=id%>');" /> <% 
   					tempId = tempId +",DetailReport";
   			 }else if(formBeans.getDetailGroupReport() != null && formBeans.getDetailGroupReport().length() > 1){ 
                 id = "DetailReport";
                %> <img id="detailGridIcon" class="activeIcon"
   					style="height: 60px; width: 80px; border-width: 0px; margin-top: 7px;"
   					src="images/reporting/detailedgtable.jpg" onclick="chartView('<%=id%>');"
   					onmouseover="chartView('<%=id%>');" /> <% 
   					tempId = tempId +",DetailReport";
   			 }
                                          
     %>	
                  <input type="hidden" name="id" id="id" value="<%=id%>">
                  <input type="hidden" name="tempId" id="tempId" value="<%=tempId%>">
                  <!--   <img id="icon5" class="activeIcon" style="height:23px;width:23px;border-width:0px; margin-bottom: 14px;" src="images/right1.png"/>  -->
                </div></td>
            </tr>
            <tr><td align="center"> 
            
            
           
            <table width="auto" border="0" cellspacing="0" cellpadding="0">
  <% if(chartType.length > 1) {%>
  <tr>    
    <td align="center" width="60" valign="top" height="30" style="padding-top:6px;" id="showAllTd"> <font face="arial"><a onclick="showAll(this);" style="margin-bottom:22px;cursor: pointer;text-decoration: underline;">Show all</a></font></td>
    <td align="left" width="54"><div style="padding-bottom:4px;">            
            <!-- AddThis Button BEGIN -->
            <div id="shareButton"><a class="addthis_button"
	href="http://www.addthis.com/bookmark.php?v=250&amp;pub=xa-4a9febfe48560f52"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a></div>
            <!-- AddThis Button END -->
          </div></td>
  </tr>
 	<%} %>
</table>
<%}else{
		// the catch block should be here as to not to show the share this feature because of no reports to share
     	out.println("<b><h3>Some problem occured while generating reports...</h3></b>");
     }%>
            
            
            
            </td></tr>
          </table></td>
       
      </tr>
    </table>
  </div>
  
  
  
  
  
  <!--tr>
    <td height="23"><span style="padding-left:10px;padding-top:15px;"><a href="index.jsp" ><a href="index.jsp">Home</a></a> <Span class="textArrow"> >> </Span> <a class="backlink" href="javascript:history.back(-1);" style = "margin-left: 7px;">Results</a> <Span class="textArrow"> >> </Span> Reports</span></td>
  </tr-->
  <div style="width:90%;margin:auto;background-color:#FFF;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">

  <tr>
    <td height="600" align="center" valign="top">
  <!--/div-->
  
  
  <!-- Code For Grid -->
  <%
         XmlDataProvider cfxXMLCurve =null;
         ChartServer chartCurve =null;
         String panValue = "";
         String divId = "", setGalleryType="", classType="";
         
         for(int i=0;i<chartType.length;i++){
            try{
        	 if(!chartType[i].equals("1") && !chartType[i].equals("4") && !chartType[i].equals("5") && !chartType[i].equals("6") && !chartType[i].equals("7")
        	          && !chartType[i].equals("8") && !chartType[i].equals("13") && !chartType[i].equals("12")
        	          && !chartType[i].equals("15") && !chartType[i].equals("16") && !chartType[i].equals("20") && !chartType[i].equals("21") && !chartType[i].equals("99") && !chartType[i].equals("98")){
        	    System.out.println("inside if BIG IMAGE.....");
        		cfxXMLCurve = new XmlDataProvider();
                chartCurve = new ChartServer(pageContext, request, response);
                               
                /* if(chartType[i].equals("7")){
                	 cfxXMLCurve.loadXML(formBeans.getCrossLinePath()); 
                 }
                 else if(chartType[i].equals("18") || chartType[i].equals("19")){
                	 cfxXMLCurve.loadXML(formBeans.getCmMultiLinePath());
                 }*/
                 if(chartType[i].equals("11") || chartType[i].equals("10") || chartType[i].equals("14") || chartType[i].equals("2") || chartType[i].equals("3")  ){
                	 filePath = formBeans.getPath();
                     cfxXMLCurve.loadXML(filePath);
                 }
	             chartCurve.getDataSourceSettings().setDataSource(cfxXMLCurve);
	             chartCurve.getLegendBox().getTitles().add(new TitleDockable("  Source: "+(String)request.getParameter("source")));
	           	 chartCurve.getLegendBox().getTitles().get(0).setDock(DockArea.BOTTOM);
	           	 chartCurve.getLegendBox().getTitles().get(0).setFont(new java.awt.Font("arial",java.awt.Font.ITALIC, 6));
	           	 chartCurve.getLegendBox().getTitles().get(0).setAlignment(StringAlignment.NEAR);

               	chartCurve.getLegendBox().getTitles().add(new TitleDockable("  www.semantifi.com"));
               	chartCurve.getLegendBox().getTitles().get(1).setDock(DockArea.BOTTOM);
               	chartCurve.getLegendBox().getTitles().get(1).setFont(new java.awt.Font("arial",java.awt.Font.ITALIC, 6));
               	chartCurve.getLegendBox().getTitles().get(1).setAlignment(StringAlignment.NEAR);
             }
        	//if(i==0 && formBeans.getDetailReport() == null)  
        	 if(i == 0) 
                classType="active";
             else
                classType="inactive";
        	 
            }catch(Exception e){
               e.printStackTrace();
               out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
            }  
             if(chartType[i].equals("1")){
                 %>
  <div id="Grid" class="<%=classType%>" align="center" style="margin-top: 15px">
    <%    			
                out.write(formBeans.getGridString());
         %>
         	<a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a>
  </div>
  <%   
            continue;
            }
             else if(chartType[i].equals("4_NOTUSED"))
             {  divId = "PivotTable";
               %>
  <div id="PivotTable" class="<%= classType %>" align="center">
    <% 
                String pivotXML = formBeans.getPivotTable();
                 
         %>
    <APPLET  code="webPivot" codebase="./jide"  archive="webPivot.jar,jide-grids.jar,jide-pivot.jar,jide-common.jar,jide-components.jar,jide-data.jar" width=1000 height=800 >
      <PARAM name="Message" value="<%= pivotXML %>">
    </APPLET>
  </div>
  <br>
  <%    
              continue;
             }
            else if(chartType[i].equals("11")){
               try{
                  System.out.println("classType :"+classType);
           %>
  <div id="multiBarChart" class="<%=classType%>" align="center" style="margin-top: 15px">
    <%
                  chartCurve.getAllSeries().setBarShape(BarShape.RECTANGLE);
                  //chartCurve.setHeight(365);  
                  divId = "multiBarChart";                
                  chartCurve.setGallery(Gallery.BAR);

                  int noOfCol = chartCurve.getData().getSeries();
              	  chartCurve.getAxisY().getTitle().setFont(new java.awt.Font("arial",java.awt.Font.PLAIN, 9));      
                  /*String axisArry[]= chartAppearance.getMeasureOnY1Y2Axis();
                  double maxValue = 0.0;
                  boolean y2DummyFirstCount = false;
                  int cntY1 = 0,cntY2 = 0; 
	              for(int cnt = 0 ; cnt<axisArry.length;cnt++){
                  	  if(axisArry[cnt].equalsIgnoreCase("Y1"))
                  	  {
                  		cntY1++;                  		
                  	  }
                  	  else
                  	  {
                  		cntY2++;
                  	  }
	             }
                 for(int alCntr =0 ; alCntr< noOfCol ; alCntr++){
                      	if(axisArry[alCntr].equalsIgnoreCase("Y1"))
                      	{
                      		SeriesAttributes serie3 = chartCurve.getSeries().get(alCntr);
   	                		serie3.getAxisY().getLabelsFormat().setFormat(AxisFormat.NUMBER);
   	                        serie3.bringToFront();
   	                        if(cntY1 > 1){
   	                  			chartCurve.getSeries().get(alCntr).setText(serie3.getText().replaceAll("_"," ") + " :Left");
   	                  		}
   	                        if(cntY1 == 1){
   	                          chartCurve.getAxisY().getTitle().setText(serie3.getText().replaceAll("_"," "));
   	                      	  chartCurve.getAxisY().getTitle().setFont(new java.awt.Font("arial",java.awt.Font.PLAIN, 9));
   	                        }
                      	}
                      	else
                      	{		
     	                		int greaterFlag = Double.compare(chartCurve.getSeries().get(alCntr).getAxisY().getMax(),maxValue);
     	                		SeriesAttributes serie4 = chartCurve.getSeries().get(alCntr);
     	                		if(cntY2 > 1){
		                  		   chartCurve.getSeries().get(alCntr).setText(serie4.getText().replaceAll("_"," ") + (" :Right"));
		                  		}
     	                		if( greaterFlag > 0){
     	                		   maxValue = serie4.getAxisY().getMax();
     	                		}
    	                		if(axisArry[alCntr].equalsIgnoreCase("Y2_dummy")){
	    	                		// setting the negative value to have the graph on the other side go all the way down from 0
	   		   	               		//chartCurve.getAxisY2().setMin(serie4.getAxisY().getMin());
	   		   	               		//chartCurve.getAxisY2().setMax(maxValue);
	   		   	               		//chartCurve.getAxisY().setMax(maxValue);
	   		   	               		serie4.setAxisY(chartCurve.getAxisY());
	   		   	               		serie4.setText(serie4.getText().replaceAll("_"," ").replaceAll(" :Right",""));
	   		   	               		chartCurve.getAxisY().getTitle().setText(serie4.getText());	   		   	               		
	   	 		                    chartCurve.getAxisY().getTitle().setFont(new java.awt.Font("arial",java.awt.Font.PLAIN, 9));
	   		   	               		if(alCntr == 0){
	   		   	               		  y2DummyFirstCount = true;
	   		   	               		} 		   	               		   
	   	                 		}else{
	   	                 		  	serie4.setAxisY(chartCurve.getAxisY2());
	   	                 		}
	   		              		if(y2DummyFirstCount){
	   		              		 	//chartCurve.getAxisY().setMax(maxValue);
	   		              		}
	   		              		serie4.bringToFront();
     	                		
     	                		if(cntY2 == 1){
        	   	                  chartCurve.getAxisY2().getTitle().setText(serie4.getText().replaceAll("_"," "));
        	                   }
                     	}
                 }*/              	   
	               int xAxisWidth = chartCurve.getData().getPoints();
	               for(int alCntr = 0 ; alCntr < noOfCol ; alCntr++){
	            		SeriesAttributes serie4 = chartCurve.getSeries().get(alCntr);
		                serie4.getAxisY().getLabelsFormat().setFormat(AxisFormat.NUMBER);
	             		
	                   if(alCntr == 0){
							chartCurve.getAxisY().getTitle().setText(serie4.getText().replaceAll("_"," "));                
							chartCurve.getAxisY().getTitle().setFont(new java.awt.Font("arial",java.awt.Font.PLAIN, 9));
							serie4.setAxisY(chartCurve.getAxisY());
	             		}else{
	             		   if(noOfCol == 2){
	             		  		chartCurve.getAxisY2().getTitle().setText(serie4.getText().replaceAll("_"," "));                
								chartCurve.getAxisY2().getTitle().setFont(new java.awt.Font("arial",java.awt.Font.PLAIN, 9));
	             		   }
	             		   serie4.setAxisY(chartCurve.getAxisY2());
	             		}
	                   if(chartCurve.getSeries().get(alCntr).getText().toLowerCase().startsWith("count")){
	                     	serie4.sendToBack();                      	   
	                   }else{
	                   		serie4.bringToFront();
	                   }
	              	   if(xAxisWidth <= 8){               		   
	                       serie4.setVolume((short)(chartCurve.getWidth() * .0425 * noOfCol));
	                   }
	              	   
	              	 	EnumSet<AxisStyles> styles = chartCurve.getAxisY2().getStyle();
	                	styles.add(AxisStyles.GRID_FRONT);
	                	//chartCurve.getAxisY2().setStyle(styles);
	               }
                 chartAppearance.getLegendBoxLabels(chartCurve);
	             chartAppearance.setPropertiesForBigCharts(chartCurve,chartName);
	             if(noOfCol > 1){
	                	chartCurve.getAxisY2().setVisible(true);
	           			chartCurve.getAxisY2().setPosition(AxisPosition.FAR);
	             }
	             chartCurve.renderControl();
                }catch(Exception e){
     		        e.printStackTrace();
     		        out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
                }
            %><div>
            <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a></div>
  </div>
  <%      
             } 
             else if(chartType[i].equals("7")){
               try{
            	%>
  <div id="CrossBarChart" class="<%=classType%>" align="center" style="margin-top: 15px">
    <table  style="BORDER: gray thin solid;-moz-border-radius:7px;">
      <tr>
        <td><table cellspacing="10" width="350px">
            <tr>
              <%
                    String cMultiArray[] = formBeans.getCrossLineChart();
                    XmlDataProvider cfxXMLCurveTemp[] = new XmlDataProvider[cMultiArray.length];
                    ChartServer chartCurveTemp[] = new ChartServer[cMultiArray.length];
                    int tempCntr = 0;
                    %>
              <td align="center" colspan=<%= cMultiArray.length %>><font face="arial"><strong><%= chartName%></strong></font></td>
            </tr>
            <%
                        for(int i1 =0; i1< cMultiArray.length ; i1++){                         
                        %>
            <%                 
                    //for(int cntr1=0;cntr1 < 2 ; cntr1++){
                        %>
            <tr>
              <td><%
                       cfxXMLCurveTemp[tempCntr] = new XmlDataProvider();
                       chartCurveTemp[tempCntr] = new ChartServer(pageContext, request, response);
                       cfxXMLCurveTemp[tempCntr].loadXML(cMultiArray[tempCntr]);
                       chartCurveTemp[tempCntr].getDataSourceSettings().setDataSource(cfxXMLCurveTemp[tempCntr]); 
                       chartCurveTemp[tempCntr].setGallery(Gallery.BAR);
                       chartCurveTemp[tempCntr].getAllSeries().setBarShape(BarShape.RECTANGLE);
                       //chartCurveTemp[tempCntr].getAxisY().setSeparation(40);
                       //chartCurveTemp[tempCntr].setHeight(450);
                       chartCurveTemp[tempCntr].getLegendBox().setAutoSize(true);
                       int xAxisWidth = chartCurveTemp[tempCntr].getData().getPoints();
                       if(xAxisWidth <= 8){
                          for(int k = 0; k < chartCurveTemp[tempCntr].getSeries().size(); k++)
                          	chartCurveTemp[tempCntr].getSeries().get(k).setVolume((short)(chartCurveTemp[tempCntr].getWidth()*.070));
                       }
                       //chartCurveTemp[tempCntr].getLegendBox().setHeight(75);
                       //chartCurveTemp[tempCntr].getLegendBox().setWidth(600);
                       chartAppearance.getLegendBoxLabels(chartCurveTemp[tempCntr]);
                       chartCurveTemp[tempCntr].getLegendBox().getTitles().add(new TitleDockable("  Source: "+(String)request.getParameter("source")));
                       chartCurveTemp[tempCntr].getLegendBox().getTitles().get(0).setDock(DockArea.BOTTOM);
                       chartCurveTemp[tempCntr].getLegendBox().getTitles().get(0).setFont(new java.awt.Font("arial",java.awt.Font.ITALIC, 6));
                       chartCurveTemp[tempCntr].getLegendBox().getTitles().get(0).setAlignment(StringAlignment.NEAR);

                       chartCurveTemp[tempCntr].getLegendBox().getTitles().add(new TitleDockable("  www.semantifi.com"));
                       chartCurveTemp[tempCntr].getLegendBox().getTitles().get(1).setDock(DockArea.BOTTOM);
                       chartCurveTemp[tempCntr].getLegendBox().getTitles().get(1).setFont(new java.awt.Font("arial",java.awt.Font.ITALIC, 6));
                       chartCurveTemp[tempCntr].getLegendBox().getTitles().get(1).setAlignment(StringAlignment.NEAR);
                       chartAppearance.setPropertiesForClusterCharts(chartCurveTemp,tempCntr,chartType[i].toString());
                       tempCntr++;
                       %></td>
            </tr>
            <%
                   //}
                   %>
            <% 
                   }
                  
               }catch(Exception e){
                  e.printStackTrace();
                  out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
               }
            %>
          </table></td>
      </tr>
    </table>
    <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a>
  </div>
  <%
               
             }
             else if(chartType[i].equals("8")){
                %>
  <div id="CrossLineChart" class="<%=classType%>" align="center" style="margin-top: 15px">
    <table  style="BORDER: gray thin solid;-moz-border-radius:7px;">
      <tr>
        <td><table cellspacing="10" width="350px">
            <tr>
              <%
               try{
            	 divId = "CrossLineChart";
                 String cMultiArray[] = formBeans.getCrossLineChart();
                 XmlDataProvider cfxXMLCurveTemp[] = new XmlDataProvider[cMultiArray.length];
                 ChartServer chartCurveTemp[] = new ChartServer[cMultiArray.length];
                 int tempCntr = 0;
                 %>
              <td align="center" colspan=<%= cMultiArray.length %>><font face="arial"><strong><%= chartName%></strong></font></td>
            </tr>
            <%
                     for(int i1 =0; i1< cMultiArray.length ; i1++){                         
                     %>
            <%                 
                 //for(int cntr1=0;cntr1 < 2 ; cntr1++){
                     %>
            <tr>
              <td><%
                    cfxXMLCurveTemp[tempCntr] = new XmlDataProvider();
                    chartCurveTemp[tempCntr] = new ChartServer(pageContext, request, response);
                    cfxXMLCurveTemp[tempCntr].loadXML(cMultiArray[tempCntr]);
                    chartCurveTemp[tempCntr].getDataSourceSettings().setDataSource(cfxXMLCurveTemp[tempCntr]); 
                    chartCurveTemp[tempCntr].getAllSeries().setBarShape(BarShape.RECTANGLE);
                    //System.out.println("\n\nInside CrossLinesChart BIG image");
                    chartCurveTemp[tempCntr].setGallery(Gallery.LINES);
                    chartCurveTemp[tempCntr].getAllSeries().setMarkerShape(MarkerShape.NONE);
                   // chartCurveTemp[tempCntr].setWidth(650);
                    //chartCurveTemp[tempCntr].setHeight(400);
                    chartCurveTemp[tempCntr].getLegendBox().setPlotAreaOnly(false);
                    chartCurveTemp[tempCntr].getLegendBox().setAutoSize(true);
                    //chartCurveTemp[tempCntr].getLegendBox().setHeight(70);
                    //chartCurveTemp[tempCntr].getLegendBox().setWidth(600);
                    chartCurveTemp[tempCntr].getLegendBox().setContentLayout(ContentLayout.CENTER);
                    chartAppearance.getLegendBoxLabels(chartCurveTemp[tempCntr]);
                    chartCurveTemp[tempCntr].getLegendBox().getTitles().add(new TitleDockable("  Source: "+(String)request.getParameter("source")));
                    chartCurveTemp[tempCntr].getLegendBox().getTitles().get(0).setDock(DockArea.BOTTOM);
                    chartCurveTemp[tempCntr].getLegendBox().getTitles().get(0).setFont(new java.awt.Font("arial",java.awt.Font.ITALIC, 6));
                    chartCurveTemp[tempCntr].getLegendBox().getTitles().get(0).setAlignment(StringAlignment.NEAR);

                    chartCurveTemp[tempCntr].getLegendBox().getTitles().add(new TitleDockable("  www.semantifi.com"));
                    chartCurveTemp[tempCntr].getLegendBox().getTitles().get(1).setDock(DockArea.BOTTOM);
                    chartCurveTemp[tempCntr].getLegendBox().getTitles().get(1).setFont(new java.awt.Font("arial",java.awt.Font.ITALIC, 6));
                    chartCurveTemp[tempCntr].getLegendBox().getTitles().get(1).setAlignment(StringAlignment.NEAR);
                    chartAppearance.setPropertiesForClusterCharts(chartCurveTemp,tempCntr,chartType[i].toString());
                    tempCntr++;
                    %></td>
            </tr>
            <%
                //}
                %>
            <% 
                }
               }catch(Exception e){
                  e.printStackTrace();
                  out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
               }
                %>
          </table></td>
      </tr>
    </table>
    <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a>
  </div>
  <%               
             }
             else if(chartType[i].equals("13")){
               try{
            	 divId = "CMultiBarChart";
                 %>
  <div id="CMultiBarChart" class="<%=classType%>" align="center" style="margin-top: 15px">
    <%
                 String[] cMultiArray = formBeans.getCMultiArry();
                 XmlDataProvider[] cfxXMLCurveTemp = new XmlDataProvider[cMultiArray.length];
                 ChartServer[] chartCurveTemp = new ChartServer[cMultiArray.length];
                 int tempCntr = 0;
                 %>
    <table  style="BORDER: gray thin solid;-moz-border-radius:7px;">
      <tr>
        <td><table cellspacing="10" width="350px">
            <tr >
              <td align="center" colspan=<%=cMultiArray.length%>><font face="arial"><strong><%= chartName%></strong></font></td>
            </tr>
            <%
                     for(int cntr=0;cntr < (cMultiArray.length)/2 ; cntr++){
                     %>
            <tr>
              <%
                 for(int cntr1=0;cntr1 < 2 ; cntr1++){
                     %>
              <td><%
                    cfxXMLCurveTemp[tempCntr] = new XmlDataProvider();
                    chartCurveTemp[tempCntr] = new ChartServer(pageContext, request, response);
                    cfxXMLCurveTemp[tempCntr].loadXML(cMultiArray[tempCntr]);
                    chartCurveTemp[tempCntr].getDataSourceSettings().setDataSource(cfxXMLCurveTemp[tempCntr]); 
                    chartCurveTemp[tempCntr].getAllSeries().setBarShape(BarShape.RECTANGLE);
                    chartCurveTemp[tempCntr].setGallery(Gallery.BAR);
                   // chartCurveTemp[tempCntr].setWidth(270);
                    chartCurveTemp[tempCntr].setHeight(220);
                    chartAppearance.getLegendBoxLabels(chartCurveTemp[tempCntr]);
                    chartAppearance.setPropertiesForClusterCharts(chartCurveTemp,tempCntr,chartType[i].toString());
                    tempCntr++;
                    %></td>
              <%
                }
                %>
            </tr>
            <%
                }
             %>
          </table></td>
      </tr>
    </table>
    <%	}catch(Exception e){
		        	 e.printStackTrace();
		         	out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
		      	}
             %>
             <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a>
  </div>
  <%
               
             }
             else if(chartType[i].equals("12")){
               try{
            	divId = "ClusterBarChart";
                %>
  <div id="ClusterBarChart" class="<%=classType%>" align="center" style="margin-top: 15px">
    <%
                 String cMultiArray[] = formBeans.getCMultiArry();
                 XmlDataProvider cfxXMLCurveTemp[] = new XmlDataProvider[cMultiArray.length];
                 ChartServer chartCurveTemp[] = new ChartServer[cMultiArray.length];                 
                 int tempCntr = 0;
                 %>
    <!--Table starts  -->
    <table border="0" cellpadding="0" cellspacing="0">
      <!-- fwtable fwsrc="reportTable.png" fwpage="Page 1" fwbase="reportTable.jpg" fwstyle="Dreamweaver" fwdocid = "254765007" fwnested="1" -->
      <tr>
        <td width="28"><img name="reportTable_r1_c1" src="images/reportTable_r1_c1.jpg" width="28" height="23" border="0" id="reportTable_r1_c1" alt="" /></td>
        <td background="images/reportTable_r1_c2.jpg"><img name="reportTable_r1_c2" src="images/reportTable_r1_c2.jpg" width="100" height="23" border="0" id="reportTable_r1_c2" alt="" /></td>
        <td width="29"><img name="reportTable_r1_c3" src="images/reportTable_r1_c3.jpg" width="29" height="23" border="0" id="reportTable_r1_c3" alt="" /></td>
      </tr>
      <tr>
        <td background="images/reportTable_r2_c1.jpg"><img name="reportTable_r2_c1" src="images/reportTable_r2_c1.jpg" width="28" height="50" border="0" id="reportTable_r2_c1" alt="" /></td>
        <td width="100" height="50"><!--Content starts -->
          <table cellspacing="10" width="350px">
            <tr >
              <td align="center" colspan=<%= cMultiArray.length %>><font face="arial"><strong><%= chartName%></strong></font></td>
            </tr>
            <tr>
              <%
                 
                 for(int cntr1=0;cntr1 < cMultiArray.length ; cntr1++){
                     %>
              <td><%
                    cfxXMLCurveTemp[tempCntr] = new XmlDataProvider();
                    chartCurveTemp[tempCntr] = new ChartServer(pageContext, request, response);
                    cfxXMLCurveTemp[tempCntr].loadXML(cMultiArray[tempCntr]);
                    chartCurveTemp[tempCntr].getDataSourceSettings().setDataSource(cfxXMLCurveTemp[tempCntr]); 
                    chartCurveTemp[tempCntr].setGallery(Gallery.BAR);
                    int xAxisWidth = chartCurveTemp[tempCntr].getData().getPoints();
                    if(xAxisWidth <= 8){
                       chartCurveTemp[tempCntr].getSeries().get(0).setVolume((short)(chartCurveTemp[tempCntr].getWidth()*.070));
                    }
                    chartCurveTemp[tempCntr].getAllSeries().setBarShape(BarShape.RECTANGLE);
                   // chartCurveTemp[tempCntr].setWidth(300);
                    chartCurveTemp[tempCntr].setHeight(300);  
                    chartAppearance.getLegendBoxLabels(chartCurveTemp[tempCntr]);
                    chartAppearance.setPropertiesForClusterCharts(chartCurveTemp,tempCntr,chartType[i].toString());
                    tempCntr++;
                    %></td>
              <%
                }
                %>
            </tr>
            <tr >
              <td align="left" colspan=<%= cMultiArray.length %>><span style="font-family: arial;font-style: italic;font-size: 6.4pt;">Source: <%=request.getParameter("source")%></span></td>
            </tr>
            <tr>
            	<td align="left" valign="top" colspan=<%= cMultiArray.length %>><span style="font-family: arial; font-style: italic; font-size: 6.4pt;">www.semantifi.com</span></td>
			</tr>
          </table>
          <!--Content ends  --></td>
        <td background="images/reportTable_r2_c3.jpg"><img name="reportTable_r2_c3" src="images/reportTable_r2_c3.jpg" width="29" height="50" border="0" id="reportTable_r2_c3" alt="" /></td>
      </tr>
      <tr>
        <td><img name="reportTable_r3_c1" src="images/reportTable_r3_c1.jpg" width="28" height="26" border="0" id="reportTable_r3_c1" alt="" /></td>
        <td background="images/reportTable_r3_c2.jpg"><img name="reportTable_r3_c2" src="images/reportTable_r3_c2.jpg" width="100" height="26" border="0" id="reportTable_r3_c2" alt="" /></td>
        <td><img name="reportTable_r3_c3" src="images/reportTable_r3_c3.jpg" width="29" height="26" border="0" id="reportTable_r3_c3" alt="" /></td>
      </tr>
    </table>
    <!--Table ends  -->
    <%	}catch(Exception e){
			        	 e.printStackTrace();
			         	out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
			      	}
		             %>
		<a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a>
  </div>
  <%
             }
             else if(chartType[i].equals("15")){
                %>
  <div id="ClusterLinesChart" class="<%=classType%>" align="center" style="margin-top: 15px">
    <%
               try{
            	 divId = "ClusterLinesChart";
                 String cMultiArray[] = formBeans.getCMultiArry();
                 XmlDataProvider cfxXMLCurveTemp[] = new XmlDataProvider[cMultiArray.length];
                 ChartServer chartCurveTemp[] = new ChartServer[cMultiArray.length];
                 int tempCntr = 0;
                 %>
    <table  style="BORDER: gray thin solid;-moz-border-radius:7px;">
      <tr>
        <td><table cellspacing="10" width="350px">
            <tr>
              <td align="center" colspan=<%= cMultiArray.length %>><font face="arial"><strong><%= chartName%></strong></font></td>
            </tr>
            <tr>
              <%
                 for(int cntr1=0;cntr1 < cMultiArray.length ; cntr1++){
                     %>
              <td><%
                    cfxXMLCurveTemp[tempCntr] = new XmlDataProvider();
                    chartCurveTemp[tempCntr] = new ChartServer(pageContext, request, response);
                    cfxXMLCurveTemp[tempCntr].loadXML(cMultiArray[tempCntr]);
                    chartCurveTemp[tempCntr].getDataSourceSettings().setDataSource(cfxXMLCurveTemp[tempCntr]); 
                    chartCurveTemp[tempCntr].getAllSeries().setBarShape(BarShape.RECTANGLE);
                    System.out.println("\n\nInside ClusterLinesChart BIG image");
                    chartCurveTemp[tempCntr].setGallery(Gallery.LINES);
                    chartCurveTemp[tempCntr].getAllSeries().setMarkerShape(MarkerShape.NONE);
                   // chartCurveTemp[tempCntr].setWidth(270);
                    chartCurveTemp[tempCntr].setHeight(220);  
                    chartAppearance.getLegendBoxLabels(chartCurveTemp[tempCntr]);
                    chartAppearance.setPropertiesForClusterCharts(chartCurveTemp,tempCntr,chartType[i].toString());
                    tempCntr++;
                    %></td>
              <%
                }
                %>
            </tr>
            <tr >
              <td align="left" colspan=<%= cMultiArray.length %>><span style="font-family: arial;font-style: italic;font-size: 6.4pt;">Source: <%=request.getParameter("source")%></span></td>
            </tr>
            <tr>
            	<td align="left" valign="top" colspan=<%= cMultiArray.length %>><span style="font-family: arial; font-style: italic; font-size: 6.4pt;">www.semantifi.com</span></td>
			</tr>
          </table></td>
      </tr>
    </table>
    <%	}catch(Exception e){
			        	 e.printStackTrace();
			         	out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
			      	}
		             %>
		             <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a>
  </div>
  <%
             }
             else if(chartType[i].equals("10")){
               try{
            	 System.out.println("******"+classType);
                divId = "BarLinesChart";     
        %>
  <div id="BarLinesChart" class="<%=classType%>" align="center" style="margin-top: 15px">
    <%
                //chartCurve.setHeight(365);  
                chartCurve.getAllSeries().getLine().setWidth((short)1);
                chartCurve.getAllSeries().setMarkerSize((short)1);
                int noOfCol = chartCurve.getData().getSeries();
                int xAxisWidth = chartCurve.getData().getPoints();
                for(int alCntr = 0 ; alCntr < noOfCol ; alCntr++){
             		SeriesAttributes serie4 = chartCurve.getSeries().get(alCntr);
	                serie4.getAxisY().getLabelsFormat().setFormat(AxisFormat.NUMBER);
              		
                    if(alCntr == 0){
						serie4.setGallery(Gallery.BAR);	
						serie4.setBarShape(BarShape.RECTANGLE);
						chartCurve.getAxisY().getTitle().setText(serie4.getText().replaceAll("_"," "));                
						chartCurve.getAxisY().getTitle().setFont(new java.awt.Font("arial",java.awt.Font.PLAIN, 9));
						serie4.setAxisY(chartCurve.getAxisY());
              		}else{
              		   	serie4.setGallery(Gallery.LINES);
              		   	if(noOfCol == 2){
	             			chartCurve.getAxisY2().getTitle().setText(serie4.getText().replaceAll("_"," "));                
							chartCurve.getAxisY2().getTitle().setFont(new java.awt.Font("arial",java.awt.Font.PLAIN, 9));
	             		}
                		serie4.setAxisY(chartCurve.getAxisY2());                		
              		}
               		if(chartCurve.getSeries().get(alCntr).getText().toLowerCase().startsWith("count")){
                      	serie4.sendToBack();                      	   
                    }else{
                       serie4.bringToFront();
                    }
               		
               		if(xAxisWidth <= 8){               		   
                        serie4.setVolume((short)(chartCurve.getWidth()*.064));
                    }
                }
                chartAppearance.getLegendBoxLabels(chartCurve);
                chartAppearance.setPropertiesForBigCharts(chartCurve,chartName);
             	if(noOfCol > 1){
                	chartCurve.getAxisY2().setVisible(true);
           			chartCurve.getAxisY2().setPosition(AxisPosition.FAR);
             	}
                chartCurve.renderControl();
 			    }catch(Exception e){
		        	 e.printStackTrace();
		         	out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
		      	}
 		     %>
 		     <div>
            <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a></div>
  </div>
  <%
             }
             else if(chartType[i].equals("5")){
              try{
            	 System.out.println("******"+classType);
                 divId = "GroupBYGrid"; 
                if(i==0)                 
         %>
  <div id="GroupBYGrid" class="<%=classType%>" align="center" style="margin-top: 15px">
    <% 
                out.write(formBeans.getGroupByGrid());
    			//out.write("<br><span style='font-family: arial;font-style: italic;font-size: 6.5pt;'>Source: "+request.getParameter("source")+"</span>");
	      		}catch(Exception e){
		        	 e.printStackTrace();
		         	out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
	      		}
            %>
            <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a>
  </div>
  <%
	          continue;
             }
             else if(chartType[i].equals("6")){
              try{
                divId = "AcrossGrid";
                if(i==0)
         %>
  <div id="AcrossGrid" class="<%=classType%>" align="center" style="margin-top: 15px">
    <% 
                out.write(formBeans.getAcrossGrid());
    			//out.write("<br><span style='font-family: arial;font-style: italic;font-size: 6.5pt;'>Source: "+request.getParameter("source")+"</span>");
	     		}catch(Exception e){
	        		 e.printStackTrace();
	         		out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
	      		}
		     %>
		     <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a>
  </div>
  <%
               continue;
             }
             else if(chartType[i].equals("14")){
               try{
                 %>
  <div id="multiLinesChart" class="<%=classType%>" align="center" style="margin-top: 15px">
    <%
                //chartCurve.setHeight(365);  
                chartCurve.getAllSeries().getLine().setWidth((short)1);
                chartCurve.getAllSeries().setMarkerSize((short)1);
                divId = "multiLinesChart";
                chartCurve.setGallery(Gallery.LINES);
                
				int noOfCol = chartCurve.getData().getSeries();
				chartAppearance.getLegendBoxLabels(chartCurve);
				/*chartCurve.getAxisY().getTitle().setFont(new java.awt.Font("arial",java.awt.Font.PLAIN, 9));      
				chartCurve.getSeries().get(0).setText(chartCurve.getSeries().get(0).toString().replace("_"," "));
           
                String axisArry[]= chartAppearance.getMeasureOnY1Y2Axis();
                double maxValue = 0.0;
             	boolean y2DummyFirstCount = false;
                int cntY1 = 0,cntY2 = 0; 
                for(int cnt = 0 ; cnt<axisArry.length;cnt++){
                	  if(axisArry[cnt].equalsIgnoreCase("Y1"))
                	  {
                		cntY1++;  
                	  }
                	  else
                	  {
                		cntY2++;
                	  }
                }        
                for(int alCntr =0 ; alCntr< noOfCol ; alCntr++){
                  	if(axisArry[alCntr].equalsIgnoreCase("Y1"))
                  	{
                  			SeriesAttributes serie3 = chartCurve.getSeries().get(alCntr);
	                		serie3.getAxisY().getLabelsFormat().setFormat(AxisFormat.NUMBER);
	                        serie3.bringToFront();
	                        chartCurve.getSeries().get(alCntr).setText(serie3.getText().replaceAll("_"," ") + " :Left");
	                        if(cntY1 == 1){
	                      	  chartCurve.getAxisY().getTitle().setText(serie3.getText().replaceAll("_"," ").replaceAll(" :Left",""));
	                      	 chartCurve.getSeries().get(alCntr).setText(serie3.getText().replaceAll("_"," ").replaceAll(" :Left",""));
	                          chartCurve.getAxisY().getTitle().setFont(new java.awt.Font("arial",java.awt.Font.PLAIN, 9));
	                        }
                  	}
                  	else
                  	{
  	                		
  	                		int greaterFlag = Double.compare(chartCurve.getSeries().get(alCntr).getAxisY().getMax(),maxValue);
  	                		SeriesAttributes serie4 = chartCurve.getSeries().get(alCntr);
  	                		chartCurve.getSeries().get(alCntr).setText(serie4.getText().replaceAll("_"," ") + " :Right");
  	                		if( greaterFlag > 0){
  	                		   maxValue = serie4.getAxisY().getMax();
  	                		}
 	                		if(axisArry[alCntr].equalsIgnoreCase("Y2_dummy")){
		   	               		// setting the negative value to have the graph on the other side go all the way down from 0
		   	               		//chartCurve.getAxisY2().setMin(serie4.getAxisY().getMin());
		   	               		//chartCurve.getAxisY2().setMax(maxValue);
		   	               		//chartCurve.getAxisY().setMax(maxValue);
		   	               		serie4.setAxisY(chartCurve.getAxisY());
		   	               		chartCurve.getAxisY().getTitle().setText(serie4.getText().replaceAll("_"," ").replaceAll(" :Right",""));
		   	               		chartCurve.getSeries().get(alCntr).setText(serie4.getText().replaceAll("_"," ").replaceAll(" :Right",""));
	 		                    chartCurve.getAxisY().getTitle().setFont(new java.awt.Font("arial",java.awt.Font.PLAIN, 9));
		   	               		if(alCntr == 0){
		   	               		  y2DummyFirstCount = true;
		   	               		} 		   	               		   
	                 		}else{
	                 		  	serie4.setAxisY(chartCurve.getAxisY2());
	                 		}
		              		if(y2DummyFirstCount){
		              		 	//chartCurve.getAxisY().setMax(maxValue);
		              		}
		              		serie4.bringToFront();
  	                		
  	                		if(cntY2 == 1){
     	   	                  chartCurve.getAxisY2().getTitle().setText(serie4.getText().replaceAll("_"," ").replaceAll(" :Right",""));
     	                    }
                  	}
                  }*/
                  
					int xAxisWidth = chartCurve.getData().getPoints();
		            for(int alCntr = 0 ; alCntr < noOfCol ; alCntr++){
		         		SeriesAttributes serie4 = chartCurve.getSeries().get(alCntr);
		                serie4.getAxisY().getLabelsFormat().setFormat(AxisFormat.NUMBER);
		          		
		                if(alCntr == 0){
							chartCurve.getAxisY().getTitle().setText(serie4.getText().replaceAll("_"," "));                
							chartCurve.getAxisY().getTitle().setFont(new java.awt.Font("arial",java.awt.Font.PLAIN, 9));
							serie4.setAxisY(chartCurve.getAxisY());
		          		}else{
		          		   if(noOfCol == 2){
		          		  		chartCurve.getAxisY2().getTitle().setText(serie4.getText().replaceAll("_"," "));                
								chartCurve.getAxisY2().getTitle().setFont(new java.awt.Font("arial",java.awt.Font.PLAIN, 9));
		          		   }
		          		   serie4.setAxisY(chartCurve.getAxisY2());
		          		}
		                if(chartCurve.getSeries().get(alCntr).getText().toLowerCase().startsWith("count")){
		                  	serie4.sendToBack();                      	   
		                }else{
		                		serie4.bringToFront();
		                }
		                
		           	 	EnumSet<AxisStyles> styles = chartCurve.getAxisY2().getStyle();
		             	styles.add(AxisStyles.GRID_FRONT);	                	
		             	chartCurve.getAxisY2().setStyle(styles);
		            }
		            chartAppearance.getLegendBoxLabels(chartCurve);
		            chartAppearance.setPropertiesForBigCharts(chartCurve,chartName);
		            if(noOfCol > 1){
		             	chartCurve.getAxisY2().setVisible(true);
		        		chartCurve.getAxisY2().setPosition(AxisPosition.FAR);
		            }
		           chartCurve.renderControl();
			    }catch(Exception e){
			        	 e.printStackTrace();
			         	out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
			      	}
		    %>
		    <div>
            <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a></div>
  </div>
  <%
             }
             else if(chartType[i].equals("2")){
              try{
            	// System.out.println("******"+classType);
                %>
  <div id="BarChart" class="<%=classType%>" align="center" style="margin-top: 15px">
    <%
                chartCurve.getAllSeries().setBarShape(BarShape.RECTANGLE);
             //	chartCurve.setWidth(600);
                //chartCurve.setHeight(300);  
                //chartCurve.getAxisX().setLabelAngle((short) 90);
                divId = "BarChart";
                chartCurve.setGallery(Gallery.BAR);
                chartAppearance.getLegendBoxLabels(chartCurve);
                //System.out.println("\n\nbefore exception "+chartCurve+" :: "+chartName);
                chartAppearance.setPropertiesForBigCharts(chartCurve,chartName);
                int xAxisWidth = chartCurve.getData().getPoints();
                if(xAxisWidth <= 10){               		   
                   chartCurve.getSeries().get(0).setVolume((short)(chartCurve.getWidth()*.064));
               	}
                chartCurve.renderControl();
              	}catch(Exception e){
			        	 e.printStackTrace();
			         	out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
		      	}
             %>
             <div>
            <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a></div>
  </div>
  <%
      		} 
             else if(chartType[i].equals("3")){
				try{                
            	 //System.out.println("******"+classType);
               %>
  <div id="LinesChart" class="<%=classType%>" align="center" style="margin-top: 15px">
    <%
               // chartCurve.setWidth(600);
                //chartCurve.setHeight(300);  
                divId = "LinesChart";
                chartCurve.setGallery(Gallery.LINES);
                chartCurve.getAllSeries().getLine().setWidth((short)1);
                chartCurve.getAllSeries().setMarkerSize((short)1);
                chartAppearance.getLegendBoxLabels(chartCurve);
                chartAppearance.setPropertiesForBigCharts(chartCurve,chartName);
                chartCurve.renderControl();
		      	}catch(Exception e){
		        	 e.printStackTrace();
		         	out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
		      	}
             %>
             <div>
            <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a></div>
  </div>
  <%
      			}            
             else if(chartType[i].equals("16")){
               try{
            	 //System.out.println("******"+classType);
             %>
  <div id="CMultiLineChart" class="<%=classType%>" align="center" style="margin-top: 15px">
    <%
                 divId = "CMultiLineChart";
                 String[] cMultiArray = formBeans.getCMultiArry();
                 XmlDataProvider[] cfxXMLCurveTemp = new XmlDataProvider[cMultiArray.length];
                 ChartServer[] chartCurveTemp = new ChartServer[cMultiArray.length];
                 int tempCntr = 0;
                 %>
    <table  style="BORDER: gray thin solid;-moz-border-radius:7px;">
      <tr>
        <td><table cellspacing="10" width="350px">
            <tr >
              <td align="center" colspan=<%= cMultiArray.length %>><font face="arial"><strong><%= chartName%></strong></font></td>
            </tr>
            <%
                     for(int cntr=0;cntr < (cMultiArray.length)/2 ; cntr++){
                     %>
            <tr>
              <%
                 for(int cntr1=0;cntr1 < 2 ; cntr1++){
           %>
              <td><%
                    cfxXMLCurveTemp[tempCntr] = new XmlDataProvider();
                    chartCurveTemp[tempCntr] = new ChartServer(pageContext, request, response);
                    cfxXMLCurveTemp[tempCntr].loadXML(cMultiArray[tempCntr]);
                    chartCurveTemp[tempCntr].getDataSourceSettings().setDataSource(cfxXMLCurveTemp[tempCntr]); 
                    chartCurveTemp[tempCntr].getAllSeries().setBarShape(BarShape.RECTANGLE);
                   // System.out.println("\n\nInside CMultiLineChart BIG image");
                    chartCurveTemp[tempCntr].setGallery(Gallery.LINES);
                //    chartCurveTemp[tempCntr].setWidth(270);
                    chartCurveTemp[tempCntr].setHeight(220);  
                    chartCurveTemp[tempCntr].getAllSeries().setMarkerShape(MarkerShape.NONE);
                    chartAppearance.getLegendBoxLabels(chartCurveTemp[tempCntr]);
                    chartAppearance.setPropertiesForClusterCharts(chartCurveTemp,tempCntr,chartType[i].toString());
                    tempCntr++;
                    %></td>
              <%  }
           %>
            </tr>
            <%  }
           %>
          </table></td>
      </tr>
    </table>
    <%	}catch(Exception e){
			        	 e.printStackTrace();
			         	out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
			      	}
		             %>
		             <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a>
  </div>
  <%
               }
            else if(chartType[i].equals("18")){
               try{
           %>
  <div id="CmMultiBarChart" class="<%=classType%>" align="center" style="margin-top: 15px">
    <%
                chartCurve.getAllSeries().setBarShape(BarShape.RECTANGLE);
               // chartCurve.setWidth(1500);
                //chartCurve.setHeight(1500);
                divId = "CmMultiBarChart";
                chartCurve.getAxisY().getTitle().setText(chartCurve.getSeries().get(0).getText().replaceAll("_"," "));
                chartCurve.setGallery(Gallery.BAR);
                chartAppearance.getLegendBoxLabels(chartCurve);
                chartAppearance.setPropertiesForBigCharts(chartCurve,chartName);
                chartCurve.renderControl();
           		}catch(Exception e){
		        	 e.printStackTrace();
		         	out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
		      	}
             %>
            <div>
            <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a></div>
  </div>
  <%
      		}
             else if(chartType[i].equals("19")){
               try{
            	System.out.println("******"+classType);
	  %>
  <div id="CmMultiLinesChart" class="<%=classType%>" align="center" style="margin-top: 15px">
    <%                
                ConvertionForChart convertionForChart = new ConvertionForChart();
                 int panSize =convertionForChart.PANSIZE;
                 panValue = convertionForChart.PANVALUE;
                 int xData = chartCurve.getData().getSeries();
                 int panLoop = xData/panSize;
                 divId = "CmMultiLinesChart";
                 chartCurve.setGallery(Gallery.LINES);
	             String[] panValueArray = panValue.split(",");
                 for(int j=0,l=0;j< xData; j= j+panLoop,l++ ){   
                   Pane pane = new Pane();
                   if(j==0){
                         pane = chartCurve.getPanes().get(j);
                         for(int k = 0;k<panLoop;k++){
                            chartCurve.getSeries().get(j).setPane(pane);
                         }
                         pane.getAxisY().getTitle().setText(panValueArray[l]);
                       }
                       else{
                            chartCurve.getPanes().add(pane);
                            for(int k = 0;k<panLoop;k++){
                                chartCurve.getSeries().get(j+k).setPane(pane);
                            }
                            pane.getAxisY().getTitle().setText(panValueArray[l]);
                        }
                   pane.getAxisY().getTitle().setFont(new java.awt.Font("arial",java.awt.Font.PLAIN, 9));
               }
                chartAppearance.getLegendBoxLabels(chartCurve);
                chartAppearance.setPropertiesForBigCharts(chartCurve,chartName);
                chartCurve.renderControl();
              	}catch(Exception e){
			       	 e.printStackTrace();
			       	out.println("<b><h3>Some problem occured while generating chart...</h3></b>");
			   	}
		      %>
		      <div>
            <a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
				addthis:url="&reportType=<%=chartType[i]%>"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a></div>
  </div>
  <%
      		} 
             else{
                // for specific case for detail report when no other reports exist.
                // to make this logic work, put all the other report types in the if logic
                // at the begning if u dont want to show it in this page.
                String detailReport = "";
                if(formBeans.getDetailReport() != null){
                   detailReport = formBeans.getDetailReport();
                }else if (formBeans.getDetailGroupReport() != null){
                   detailReport = formBeans.getDetailGroupReport();
                }
                if(detailReport.length() > 1){
                   
          		    %>
          		<div id="DetailReport" class="<%= classType %>" align="center"
          			style="margin-top: 0px;">
          			<% boolean messageFlag = Boolean.parseBoolean(request.getAttribute("printDetailReportMessage").toString());
          			if(messageFlag && formBeans.getDetailReport() != null) {%>
          				<b>Detail Report Snapshot ... <a
          					href='showDetailReport.action?queryId=<%=request.getParameter("queryId")%>&assetId=<%=request.getParameter("assetId")%>&businessQueryId=<%=request.getParameter("businessQueryId")%>&title=<%=chartName%>&source=<%=request.getParameter("source")%>&type=<%=request.getParameter("type")%>&requestedString=<%=request.getParameter("requestedString")%>'>More</a></b>
          			<%}else{ %>
          				<b>Detail Report Summary </b>
      				<%}
          		           out.write(detailReport);
          		           if(!messageFlag){
      		    			%>
      		    			<a class="sharing-button" addthis:title="<%=request.getParameter("title")%>"
      						addthis:url="&reportType=0"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a>
      						<%} %>
          		</div>
          		<%   
                 }
             }
           }
         
         }
           catch(Exception e){
            out.println("<b><h3>Some problem occured while generating Reports...</h3></b>");
            e.printStackTrace();
           }
      %>
      
  <!--div align="center" style="margin-top: 15px;"><font style="font-family:arial;"><a href="javascript:openNewWindow();">view xml</a></font></div-->
  </td>
  
  </tr>

</table>
</div>
 <div id="reportComments"/>
 <div style="width: 90%; margin: auto; background-color: #FFF;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">

	<tr>
		<td height="25" colspan="2" align="center"
			background="images/bc-bg.jpg" valign="top">&nbsp;</td>
	</tr>

	<tr>
		<td height="45" colspan="2" align="center" valign="top">
		<div id="disclaimer"><span id="disclaimerSummary"></span><a
			href="#" id="disclaimerMore" style="display: none;" class="greenLink"> ...More</a></div>

		</td>
	</tr>
</table>
</div>  
<jsp:include page="../../../views/footer-search.jsp" flush="true" />
</body>

</html>
<script>
if($("#IconView img").length<2){$("#showAllTd").hide();}
var assetDetailId=0;
$(document).ready(function(){
var windowUrl = window.location;
$.each($('.sharing-button'), function(){
	var shareUrl = $(this).attr("addthis:url");
	$(this).attr("addthis:url",windowUrl + shareUrl);
});
addthis.button('.sharing-button');
var assetId='<%=request.getAttribute("assetId")%>';
$("a#disclaimerDetailsClose").hide();
      getDisclaimerSummary(assetId);
		$("a#disclaimerMore").click(function(){	
		    hideAll();	
		if(assetDetailId !='0'){
	 		$.getJSON("qi/getExtendedDisclaimer.action",{"uiAssetDetail.assetDetailId" : assetDetailId},function(data){																							
				$("#disclaimerDetailsContent").html(data.extendedDisclaimer)
				$("#disclaimerDetails").show().css("left",$("#bCrumb").position().left+152+"px");
				$("#disclaimerDetails").css("top",$("#bCrumb").position().top+160+"px");																	
			});
		 }
											 
	 });
						
	$("a#disclaimerDetailsClose").click(function(){
		$("#disclaimerDetails").fadeOut("fast"); 
	});
 
  //getReportComments();
});
function getDisclaimerSummary(assetId){
   $.getJSON("qi/getShortDisclaimer.action",{assetId:assetId},function(data){ 
   assetDetailId=data.assetDetailId;
	   if(data.shortDisclaimer){
	     $("#disclaimerSummary").html(data.shortDisclaimer);
	     $("#disclaimerMore").show();
	    
	   }  
   });
}
function getReportComments(){
	var queryId='<%=request.getAttribute("queryId")%>';
	var requestedString='<%=request.getParameter("requestedString")%>';
	var assetId='<%=request.getAttribute("assetId")%>';

   // alert('userQuery::'+userQuery+':assetId-'+assetId+'queryId:'+queryId+'requestedString-'+requestedString);    
   $("#reportComments").empty();
   $.get("reporting/showReportComments.action",{"reportComment.queryHash":requestedString,
                                                "reportComment.assetId":assetId,
                                                "reportComment.queryId":queryId},function(data){          
	     $("#reportComments").append(data);	    
   });
}

function saveReportComment(){
   var reportUserComment=$('#reportComment').serialize(); 
   alert(reportUserComment);
    $("#reportComments").empty();	   
   $.get("reporting/saveReportComment.action",reportUserComment,function(data){          
	    // $("#reportComments").append(data);
	    getReportComments();	    
   });
}

</script>