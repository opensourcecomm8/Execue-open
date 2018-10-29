<%@page import="com.softwarefx.chartfx.server.ChartServer"%>
<%@page import="com.softwarefx.chartfx.server.maps.Map"%>
<%@page import="com.softwarefx.chartfx.server.dataproviders.XmlDataProvider"%>
<%@page import="com.softwarefx.chartfx.server.DockBorder"%>
<%@page import="com.softwarefx.chartfx.server.ConditionalAttributes"%>
<%@page import="java.awt.Color"%>
<%@page import="com.softwarefx.chartfx.server.ConditionalAttributesCollection"%>
<%@page import="com.softwarefx.chartfx.server.TitleDockable"%>
<%@page import="java.awt.Font"%>
<%@page import="com.softwarefx.chartfx.server.DockArea"%>
<%@page import="com.softwarefx.StringAlignment"%>


<table width="840" border="0" align="center">
    <tr>
        <td colspan=2><p>Sample to show the US states map for the election results
for some states held between Obama and McClain</p></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td class="descContent">
            <div style="width: 800px;">
        <%!
			//===============================================================================
			// ConfigureUSMap Method
			//===============================================================================
			public void configureUSMap (ChartServer aChart1, Map aMap1, String aAppRealPath) { 
		    
		    //Clear Labels, Set MapSource, configure data and labellinks
		    aChart1.getData().clear();
		    aMap1.setMapSource(aAppRealPath + "/chartfx70/Library/US/USA-StatesAbrev.svg");

		    XmlDataProvider xmlData = new XmlDataProvider();
		    xmlData.loadXML("<?xml version='1.0'?><CHARTFX><COLUMNS><COLUMN NAME='c0' TYPE='String' DESCRIPTION='States' /><COLUMN NAME='c1' TYPE='Double' DESCRIPTION='Obama' /><COLUMN NAME='c2' TYPE='Double' DESCRIPTION='McCain' /></COLUMNS><ROW c0='ALABAMA' c1='234234.345' c2='234523.23'></ROW><ROW c0='ALASKA' c1='3453535.456' c2='234345.45'></ROW><ROW c0='ARIZONA' c1='3453.56' c2='234523.23'></ROW><ROW c0='GEORGIA' c1='250433.47' c2='234345.45'></ROW><ROW c0='IDAHO' c1='34535.5345' c2='234523.23'></ROW><ROW c0='ILLINOIS' c1='345.53' c2='234345.45'></ROW><ROW c0='LOUISIANA' c1='345335.5' c2='234345.45'></ROW><ROW c0='MARYLAND' c1='23424.4' c2='234523.23'></ROW><ROW c0='MISSISSIPPI' c1='567575.54' c2='234345.45'></ROW><ROW c0='NEVADA' c1='67868645.5' c2='234523.23'></ROW><ROW c0='NEW MEXICO' c1='45634563.5' c2='234345.45'></ROW><ROW c0='NEW YORK' c1='6784.5' c2='234523.23'></ROW><ROW c0='OHIO' c1='48746.535' c2='234345.45'></ROW><ROW c0='OKLAHOMA' c1='3567357.46' c2='234523.23'></ROW><ROW c0='PENNSYLVANIA' c1='45624526.52' c2='234345.45'></ROW><ROW c0='SOUTH CAROLINA' c1='4564.545' c2='234523.23'></ROW><ROW c0='TENNESSEE' c1='7975.55' c2='234345.45'></ROW><ROW c0='UTAH' c1='757975.5' c2='234523.23'></ROW></CHARTFX>");
		    aChart1.getDataSourceSettings().setDataSource(xmlData);
			
		    aMap1.setLabelLinkFile(aAppRealPath + "/chartfx70/Library/US_LabelLinks.xml");
		
		    //display DataEditor and UserLegendBox
		    aChart1.getDataGrid().setVisible(true);
		    aChart1.getLegendBox().setBorder(DockBorder.NONE);
		    aChart1.getLegendBox().setVisible(true);
		    aChart1.getLegendBox().getItemAttributes().get(aChart1.getSeries()).setVisible(false);
		    aChart1.getAllSeries().getPointLabels().setFormat("%s from %l - Total Vote: %v");
		    
	        //Create conditional attributes
	        ConditionalAttributesCollection m_condList = aChart1.getConditionalAttributes();
	        m_condList.clear();

	        ConditionalAttributes cond1 = new ConditionalAttributes();
	        cond1.setColor(aChart1.getSeries().get(0).getColor());
	        cond1.getCondition().setActive(false);
	        cond1.setText("Republican");

	        ConditionalAttributes cond2 = new ConditionalAttributes();
	        cond2.setColor(aChart1.getSeries().get(1).getColor());
	        cond2.getCondition().setActive(false);
	        cond2.setText("Democrat");

	        m_condList.addRange(new ConditionalAttributes[] { cond1, cond2});
		}
		%>
		<%
                        
            ChartServer chart1 = new ChartServer(pageContext, request, response);
       		chart1.setID("chart1");
       		chart1.getImageSettings().setInteractive(false);
  			Map map1 = new Map();
            map1.setMapSource(application.getRealPath("/chartfx70") + "\\Library\\US\\USA-StatesAbrev.svg");
            map1.setChart(chart1);
            
          	//Titles and other visuals
            TitleDockable title = new TitleDockable();
            title.setText("US 2000 Presidential Election");
            title.setFont(new Font("Arial", 12, Font.BOLD));
            chart1.getTitles().add(title);
            
            map1.setTitleFont(new Font("Arial", Font.PLAIN, 10));
		
            TitleDockable subtitle = new TitleDockable();
            subtitle.setText(" Obama (Republican) vs. McClain (Democrat)");
            subtitle.setFont(new Font("Arial", Font.ITALIC, 10));
            chart1.getTitles().add(subtitle);
		
            TitleDockable footer = new TitleDockable();
            footer.setFont(new Font("Arial", Font.PLAIN, 10));
            footer.setAlignment(StringAlignment.CENTER);
            footer.setDock(DockArea.BOTTOM);
            chart1.getTitles().add(footer);
            
            map1.setTitleFont(new Font("Arial", Font.PLAIN, 10)); 
            
          	//Method ConfigureUSMap
            configureUSMap(chart1, map1, application.getRealPath("/"));  
          	           
          	chart1.getDataGrid().setVisible(false);
          	chart1.setZoom(false);
          	chart1.setWidth(800);
            chart1.setHeight(600);
            chart1.renderControl();
   		%>
	        </div>
	    </td>
	</tr>
</table>