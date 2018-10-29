/**
 * Licensed to the Execue Software Foundation (ESF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ESF licenses this file
 * to you under the Execue License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * webPivot.java Date : 05-Apr-09
 * 
 * @author Dhananjay Chauhan
 */

package com.execue.web.applet.presentation.pivot;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.TableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.jidesoft.grid.CalculatedTableModel;
import com.jidesoft.grid.DefaultContextSensitiveTableModel;
import com.jidesoft.pivot.DefaultSummaryCalculator;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotConstants;
import com.jidesoft.pivot.PivotDataModel;
import com.jidesoft.pivot.PivotField;
import com.jidesoft.pivot.PivotTablePane;

public class WebPivot extends Applet {

   protected PivotTablePane _pivotTablePane;
   String                   Filepath    = "";
   String                   value       = "";
   String                   data        = "";
   String                   dType       = "";
   String                   strDtype    = "DIMENSION";
   String                   numberDtype = "MEASURE";
   String                   idDtype     = "ID";
   String                   dateDtype   = "DATE";

   ArrayList                valueNames  = new ArrayList();
   ArrayList                idAList     = new ArrayList();
   ArrayList                dataAlist   = new ArrayList();
   ArrayList                dTypeAlist  = new ArrayList();

   Vector<Vector<Object>>   dataVector  = new Vector();
   Vector<String>           columnNames = new Vector();

   public Component getDemoPanel (String xmlStringdata) {
      com.jidesoft.utils.Lm.verifyLicense("ExeCue", "BI Factor", "NHKBRGB6Wf06x7r0P1uUkfFetrAypWi");
      String xmlString = xmlStringdata;
      final TableModel tableModel = dataModel(xmlString);

      if (tableModel == null) {
         return new JLabel("Failed to read data file");
      }

      final CalculatedTableModel calculatedTableModel = setupCalculatedTableModel(tableModel);
      final PivotDataModel pivotDataModel = PivotDataModel(calculatedTableModel);

      _pivotTablePane = new PivotTablePane(pivotDataModel);
      _pivotTablePane.setAutoscrolls(true);
      _pivotTablePane.dataTableUpdated();
      _pivotTablePane.autoResizeAllColumns();
      return _pivotTablePane;
   }

   /********************************************************************************************************************************************************************************
    * ******** Preparing Table Model
    *******************************************************************************************************************************************************************************/
   private TableModel dataModel (String dataString) {

      String xmlStringData = dataString;
      try {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         DocumentBuilder db = dbf.newDocumentBuilder();

         InputSource inStream = new InputSource();
         inStream.setCharacterStream(new StringReader(xmlStringData));

         Document doc = db.parse(inStream);
         doc.getDocumentElement().normalize();

         NodeList colNodeList = doc.getElementsByTagName("COLUMN");
         for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {

            Node fstColNode = colNodeList.item(cntr);
            Element colElement = (Element) fstColNode;
            if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
               NodeList idNodeList = colElement.getElementsByTagName("ID");
               Element idElement = (Element) idNodeList.item(0);
               NodeList idChNodeList = idElement.getChildNodes();
               idAList.add(new String(((Node) idChNodeList.item(0)).getNodeValue()));

               NodeList valueNodeList = colElement.getElementsByTagName("DESC");
               Element valueElement = (Element) valueNodeList.item(0);
               NodeList valueChNodeList = valueElement.getChildNodes();
               value = ((Node) valueChNodeList.item(0)).getNodeValue();
               columnNames.add(new String(value));

               NodeList dTypeNodeList = colElement.getElementsByTagName("CTYPE");
               Element dTypeElement = (Element) dTypeNodeList.item(0);
               NodeList dTypeChNodeList = dTypeElement.getChildNodes();
               dType = ((Node) dTypeChNodeList.item(0)).getNodeValue().toString().replace(' ', '_');
               dTypeAlist.add(new String(dType));

            }
         }
         NodeList rowNodeList = doc.getElementsByTagName("VALUES");
         for (int cntr = 0; cntr < rowNodeList.getLength(); cntr++) {
            Vector<Object> linedata = new Vector();
            Node fstRowNode = rowNodeList.item(cntr);
            Element rowElement = (Element) fstRowNode;
            if (fstRowNode.getNodeType() == Node.ELEMENT_NODE) {
               for (int cntrAList = 0; cntrAList < idAList.size(); cntrAList++) {
                  NodeList idRowNodeList = rowElement.getElementsByTagName(idAList.get(cntrAList).toString());
                  Element idRowElement = (Element) idRowNodeList.item(0);
                  NodeList idRowChNodeList = idRowElement.getChildNodes();
                  data = ((Node) idRowChNodeList.item(0)).getNodeValue();
                  String idDataType = (String) dTypeAlist.get(cntrAList);
                  if (idDataType.equals(strDtype)) {
                     linedata.add(data);
                  } else if (idDataType.equals(numberDtype) || idDataType.equals(idDtype)) {
                     if (!data.equalsIgnoreCase("N/A")) {
                        float l = Float.parseFloat(data);
                        linedata.add(l);
                     } else {
                        linedata.add(data);
                     }
                  } else if (idDataType.equals(dateDtype)) {
                     SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                     if (!data.equalsIgnoreCase("N/A")) {
                        Date date = format.parse(data);
                        linedata.add(date); // order date
                     } else {
                        linedata.add(data);
                     }
                  }
               }
            }
            dataVector.add(linedata);

         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return new DefaultContextSensitiveTableModel(dataVector, columnNames) {

         @Override
         public Class<?> getColumnClass (int columnIndex) {

            String dtType = (String) dTypeAlist.get(columnIndex);
            if (dtType.equals(strDtype)) {
               return String.class;
            } else if (dtType.equals(numberDtype)) {
               return Float.class;
            } else if (dtType.equals(dateDtype)) {
               return Date.class;
            }
            return super.getColumnClass(columnIndex);
         }
      };
   }

   /********************************************************************************************************************************************************************************
    * ******** Preparing Calculated Table Model
    *******************************************************************************************************************************************************************************/
   private CalculatedTableModel setupCalculatedTableModel (TableModel tableModel) {
      CalculatedTableModel calculatedTableModel = new CalculatedTableModel(tableModel);
      calculatedTableModel.addAllColumns();
      return calculatedTableModel;
   }

   /********************************************************************************************************************************************************************************
    * ******** Preparing Pivot Data Model
    *******************************************************************************************************************************************************************************/
   private PivotDataModel PivotDataModel (TableModel derivedTableModel) {
      final PivotDataModel pivotDataModel = new PivotDataModel(derivedTableModel);

      pivotDataModel.setSummaryCalculator(new DefaultSummaryCalculator() {

         final int      SUMMARY_LAST_VALUE = PivotConstants.SUMMARY_RESERVED_MAX + 1;
         private Object _lastValue;

         @Override
         public void addValue (IPivotDataModel dataModel, PivotField field, int rowIndex, int columnIndex, Object v) {
            super.addValue(dataModel, field, rowIndex, columnIndex, v);
            if (v instanceof Number) {
               _lastValue = ((Number) v).doubleValue();
            }
         }

         @Override
         public void clear () {
            super.clear();
            _lastValue = 0;
         }

         @Override
         public int getNumberOfSummaries () {
            return super.getNumberOfSummaries() + 1;
         }

         @Override
         public String getSummaryName (Locale locale, int type) {
            if (type == SUMMARY_LAST_VALUE) {
               return "Last Value";
            }
            return super.getSummaryName(locale, type);
         }

         @Override
         public Object getSummaryResult (int type) {
            if (type == SUMMARY_LAST_VALUE) {
               return _lastValue;
            }
            return super.getSummaryResult(type);
         }

         @Override
         public int[] getAllowedSummaries (Class<?> type) {
            int[] summaries = super.getAllowedSummaries(type);
            int[] newSummaries = new int[summaries.length + 1];
            System.arraycopy(summaries, 0, newSummaries, 0, summaries.length);
            newSummaries[summaries.length] = SUMMARY_LAST_VALUE;
            return newSummaries;
         }
      });

      pivotDataModel.setExpandByDefault(true);
      pivotDataModel.getDataFields();
      pivotDataModel.setSingleValueMode(Boolean.TRUE);
      pivotDataModel.setShowGrandTotalForColumn(true);
      pivotDataModel.setShowGrandTotalForRow(true);
      pivotDataModel.calculate();
      return pivotDataModel;
   }

   @Override
   public void init () {
      String xmlString = this.getParameter("Message");
      JPanel pivotPanel = new JPanel();
      pivotPanel.setLayout(new BorderLayout());
      pivotPanel.add(getDemoPanel(xmlString));
      setLayout(new BorderLayout());
      add(pivotPanel);
   }
}
