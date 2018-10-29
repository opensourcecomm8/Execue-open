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


package com.execue.core.common.util;

import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.execue.core.common.bean.ReportQueryData;
import com.execue.core.common.bean.aggregation.QueryDataColumnData;
import com.execue.core.common.bean.aggregation.QueryDataColumnMember;
import com.execue.core.common.bean.aggregation.QueryDataHeaderColumnMeta;
import com.execue.core.common.bean.aggregation.QueryDataHeaderHierarchyColumnMeta;
import com.execue.core.common.bean.aggregation.QueryDataHeaderHierarchyMeta;
import com.execue.core.common.bean.aggregation.QueryDataRowData;
import com.execue.core.common.bean.aggregation.QueryDataRows;
import com.execue.core.common.type.ReportType;
import com.execue.core.util.ExecueCoreUtil;

/**
 * @author John Mallavalli
 */

public class QueryDataXMLWriter {

   private static final String docTag                      = "XMLDocument";
   private static final String headerTag                   = "HEADER";
   private static final String numcolsTag                  = "NUMCOLUMNS";
   private static final String reporttypesTag              = "REPORTTYPES";
   private static final String titleTag                    = "TITLE";
   private static final String columnTag                   = "COLUMN";
   private static final String idTag                       = "ID";
   private static final String descTag                     = "DESC";
   private static final String ctypeTag                    = "CTYPE";
   private static final String plotasTag                   = "PLOTAS";
   private static final String dtypeTag                    = "DTYPE";
   private static final String dataformatTag               = "DATAFORMAT";
   private static final String unittypeTag                 = "UNITTYPE";
   private static final String unitTag                     = "UNIT";
   private static final String precisionTag                = "PRECISION";
   private static final String scaleTag                    = "SCALE";
   // private static final String parentTag = "PARENT";
   private static final String fromCohortTag               = "FROMCOHORT";
   private static final String nummembersTag               = "NUMMEMBERS";
   private static final String membersTag                  = "MEMBERS";
   private static final String memberTag                   = "MEMBER";
   private static final String valueTag                    = "VALUE";
   private static final String displayTag                  = "DISPLAY";
   private static final String dataTag                     = "DATA";
   private static final String valuesTag                   = "VALUES";

   // Tags Section for hierarchy header element
   private static final String hierarchyTag                = "HIERARCHY";
   private static final String hierarchyNameTag            = "NAME";
   private static final String hierarchyEntityCountTag     = "HIERARCHYENTITYCOUNT";
   private static final String hierarchyDetailsTag         = "HIERARCHYDETAILS";
   private static final String hierarchyEntityTag          = "HIERARCHYENTITY";
   private static final String hierarchyEntityRefColumnTag = "REFCOLUMN";
   private static final String hierarchyEntityLevelTag     = "LEVEL";

   public String toXML (ReportQueryData reportQueryData) {
      String xmlString = null;
      try {
         DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
         DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
         Document doc = docBuilder.newDocument();

         // create the root element and add it to the document
         Element root = doc.createElement(docTag);
         doc.appendChild(root);

         // generate header
         Element header = doc.createElement(headerTag);
         root.appendChild(header);

         Element numcols = doc.createElement(numcolsTag);
         Text numcolsVal = doc.createTextNode(Integer.toString(reportQueryData.getQueryData().getQueryDataHeader()
                  .getColumnCount()));
         numcols.appendChild(numcolsVal);
         header.appendChild(numcols);

         // report title tag
         Element title = doc.createElement(titleTag);
         Text titleVal = doc.createTextNode(reportQueryData.getQueryData().getQueryDataHeader().getTitle());
         title.appendChild(titleVal);
         header.appendChild(title);

         StringBuffer reportTypesStr = new StringBuffer();

         for (ReportType repType : reportQueryData.getReportTypes()) {
            reportTypesStr.append(String.valueOf(repType.getValue().intValue()) + ",");
         }

         Element reporttypes = doc.createElement(reporttypesTag);
         Text reporttypesVal = doc.createTextNode(reportTypesStr.toString());
         reporttypes.appendChild(reporttypesVal);
         header.appendChild(reporttypes);

         // add the columns meta
         for (QueryDataHeaderColumnMeta queryDataHeaderColumnMeta : reportQueryData.getQueryData().getQueryDataHeader()
                  .getQueryDataHeaderColumns()) {
            Element column = doc.createElement(columnTag);
            Element id = doc.createElement(idTag);
            Text idVal = doc.createTextNode(queryDataHeaderColumnMeta.getId());
            id.appendChild(idVal);
            column.appendChild(id);

            Element desc = doc.createElement(descTag);
            Text descVal = doc.createTextNode(queryDataHeaderColumnMeta.getDescription());
            desc.appendChild(descVal);
            column.appendChild(desc);

            Element ctype = doc.createElement(ctypeTag);
            Text ctypeVal = doc.createTextNode(queryDataHeaderColumnMeta.getCtype());
            ctype.appendChild(ctypeVal);
            column.appendChild(ctype);

            Element plotas = doc.createElement(plotasTag);
            Text plotasVal = doc.createTextNode(queryDataHeaderColumnMeta.getPlotAs());
            plotas.appendChild(plotasVal);
            column.appendChild(plotas);

            Element dtype = doc.createElement(dtypeTag);
            Text dtypeVal = doc.createTextNode(queryDataHeaderColumnMeta.getDtype());
            dtype.appendChild(dtypeVal);
            column.appendChild(dtype);

            Element dataformat = doc.createElement(dataformatTag);
            Text dataformatVal = doc.createTextNode(queryDataHeaderColumnMeta.getDataFormat());
            dataformat.appendChild(dataformatVal);
            column.appendChild(dataformat);

            Element unittype = doc.createElement(unittypeTag);
            Text unittypeVal = doc.createTextNode(queryDataHeaderColumnMeta.getUnitType());
            unittype.appendChild(unittypeVal);
            column.appendChild(unittype);

            Element unit = doc.createElement(unitTag);
            Text unitVal = doc.createTextNode(queryDataHeaderColumnMeta.getUnit());
            unit.appendChild(unitVal);
            column.appendChild(unit);

            Element precision = doc.createElement(precisionTag);
            Text precisionVal = doc.createTextNode(queryDataHeaderColumnMeta.getPrecision());
            precision.appendChild(precisionVal);
            column.appendChild(precision);

            Element scale = doc.createElement(scaleTag);
            Text scaleVal = doc.createTextNode(queryDataHeaderColumnMeta.getScale());
            scale.appendChild(scaleVal);
            column.appendChild(scale);

            // Element parent = doc.createElement(parentTag);
            // Text parentVal = doc.createTextNode(queryDataHeaderColumnMeta.getParent());
            // parent.appendChild(parentVal);
            // column.appendChild(parent);
            // add "fromCohort" tag if the value is set
            // if (queryDataHeaderColumnMeta.isFromCohort()) {
            Element fromCohort = doc.createElement(fromCohortTag);
            String val = "false";
            if (queryDataHeaderColumnMeta.isFromCohort()) {
               val = "true";
            }
            Text fromCohortVal = doc.createTextNode(val);
            fromCohort.appendChild(fromCohortVal);
            column.appendChild(fromCohort);
            // }

            if (queryDataHeaderColumnMeta.getMemberCount() > 0) {
               Element nummembers = doc.createElement(nummembersTag);
               Text nummembersVal = doc.createTextNode(Integer.toString(queryDataHeaderColumnMeta.getMemberCount()));
               nummembers.appendChild(nummembersVal);
               column.appendChild(nummembers);
               if (queryDataHeaderColumnMeta.getMembers() != null && queryDataHeaderColumnMeta.getMembers().size() > 0) {
                  Element members = doc.createElement(membersTag);
                  for (int i = 0; i < queryDataHeaderColumnMeta.getMembers().size(); i++) {
                     List<QueryDataColumnMember> colMembers = queryDataHeaderColumnMeta.getMembers();

                     Element member = doc.createElement(memberTag);
                     Element value = doc.createElement(valueTag);
                     Text valueVal = doc.createTextNode(colMembers.get(i).getValue());
                     value.appendChild(valueVal);
                     member.appendChild(value);

                     Element desc1 = doc.createElement(descTag);
                     Text desc1Val = doc.createTextNode(colMembers.get(i).getDescription());
                     desc1.appendChild(desc1Val);
                     member.appendChild(desc1);

                     Element display = doc.createElement(displayTag);
                     Text displayVal = doc.createTextNode(colMembers.get(i).getDisplayString());
                     display.appendChild(displayVal);
                     member.appendChild(display);
                     members.appendChild(member);
                  }
                  column.appendChild(members);
               }
            }
            header.appendChild(column);
         }

         // Process for hierarchy header tag
         QueryDataHeaderHierarchyMeta queryDataHeaderHierarchyMeta = reportQueryData.getQueryData()
                  .getQueryDataHeader().getQueryDataHeaderHierarchyMeta();
         if (queryDataHeaderHierarchyMeta != null
                  && ExecueCoreUtil.isCollectionNotEmpty(queryDataHeaderHierarchyMeta.getHierarhcyDetails())) {
            // generate hierarchy
            Element hierarchyElement = doc.createElement(hierarchyTag);
            header.appendChild(hierarchyElement);

            Element hierarchyNameElement = doc.createElement(hierarchyNameTag);
            Text hierarchyNameVal = doc.createTextNode(queryDataHeaderHierarchyMeta.getHierarchyName());
            hierarchyNameElement.appendChild(hierarchyNameVal);
            hierarchyElement.appendChild(hierarchyNameElement);

            Element hierarchyEntityCountElement = doc.createElement(hierarchyEntityCountTag);
            Text hierarchyEntityCountVal = doc.createTextNode(String.valueOf(queryDataHeaderHierarchyMeta
                     .getHierarchyEntityCount()));
            hierarchyEntityCountElement.appendChild(hierarchyEntityCountVal);
            hierarchyElement.appendChild(hierarchyEntityCountElement);

            Element hierarchyDetailsElement = doc.createElement(hierarchyDetailsTag);
            hierarchyElement.appendChild(hierarchyDetailsElement);

            // add the hierarchy columns meta
            for (QueryDataHeaderHierarchyColumnMeta queryDataHeaderHierarchyColumnMeta : queryDataHeaderHierarchyMeta
                     .getHierarhcyDetails()) {
               Element hierarchyEntity = doc.createElement(hierarchyEntityTag);

               Element refColumn = doc.createElement(hierarchyEntityRefColumnTag);
               Text refColumnVal = doc.createTextNode(queryDataHeaderHierarchyColumnMeta.getReferenceColumnId());
               refColumn.appendChild(refColumnVal);
               hierarchyEntity.appendChild(refColumn);

               Element level = doc.createElement(hierarchyEntityLevelTag);
               Text levelVal = doc.createTextNode(String.valueOf(queryDataHeaderHierarchyColumnMeta.getLevel()));
               level.appendChild(levelVal);
               hierarchyEntity.appendChild(level);

               hierarchyDetailsElement.appendChild(hierarchyEntity);
            }
         }

         // generate data
         Element data = doc.createElement(dataTag);
         root.appendChild(data);
         QueryDataRows queryDataRows = reportQueryData.getQueryData().getQueryDataRows();
         for (QueryDataRowData queryDataRowData : queryDataRows.getQueryDataRowsList()) {
            Element values = doc.createElement(valuesTag);
            for (QueryDataColumnData colval : queryDataRowData.getQueryDataColumns()) {
               Element colname = doc.createElement(colval.getColumnName());
               Text colvalue = doc.createTextNode(colval.getColumnValue());

               colname.appendChild(colvalue);
               values.appendChild(colname);
            }
            data.appendChild(values);
         }

         // set up a transformer
         TransformerFactory transfactory = TransformerFactory.newInstance();
         Transformer transformer = transfactory.newTransformer();
         transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
         // transformer.setOutputProperty(OutputKeys.INDENT, "yes");

         // create string from xml tree
         StringWriter stringWriter = new StringWriter();
         // StreamResult streamResult = new StreamResult(System.out);
         StreamResult streamResult = new StreamResult(stringWriter);
         DOMSource domSource = new DOMSource(doc);

         transformer.transform(domSource, streamResult);
         xmlString = stringWriter.toString();
      } catch (Exception e) {
         e.printStackTrace();
      }
      return xmlString;
   }
}