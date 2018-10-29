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


package com.execue.das.datatransfer.etl.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.NotePadMeta;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.trans.StepLoader;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.selectvalues.SelectMetadataChange;
import org.pentaho.di.trans.steps.selectvalues.SelectValuesMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;
import org.pentaho.di.trans.steps.tableoutput.TableOutputMeta;

import com.execue.das.datatransfer.etl.bean.ETLInput;
import com.execue.das.datatransfer.etl.exception.ETLException;

public class KettleETLInputHelper {

   public static final String xml      = "<connection><name>";
   public static final String xmlPart  = "</name><server>";
   public static final String xmlPart1 = "</server><type>";
   public static final String xmlPart2 = "</type><access>Native</access><database>";
   public static final String xmlPart3 = "</database><port>";
   public static final String xmlPart4 = "</port><username>";
   public static final String xmlPart5 = "</username><password>";
   public static final String xmlPart6 = "</password></connection>";
   private static String      location;
   private static String      port;
   private static String      database;

   /**
    * Returns TransMeta Object that prepares and executes the kettle ETL Code
    * 
    * @param etlInput
    * @return TransMeta
    * @throws KettleException
    */
   public static TransMeta prepareKettleInput (ETLInput etlInput) throws ETLException {
      TransMeta transMeta = null;

      Map<String, Object> localURL = extractURL(etlInput.getTargetConnectionPropsMap());
      Map<String, Object> remoteURL = extractURL(etlInput.getSourceConnectionPropsMap());
      try {

         String targetXML = xml + "source" + xmlPart + remoteURL.get("location") + xmlPart1
                  + etlInput.getSourceConnectionPropsMap().get("driver") + xmlPart2 + remoteURL.get("database")
                  + xmlPart3 + remoteURL.get("port") + xmlPart4 + etlInput.getSourceConnectionPropsMap().get("user")
                  + xmlPart5 + etlInput.getSourceConnectionPropsMap().get("password") + xmlPart6;

         String localXML = xml + "target" + xmlPart + localURL.get("location") + xmlPart1
                  + etlInput.getTargetConnectionPropsMap().get("driver") + xmlPart2 + localURL.get("database")
                  + xmlPart3 + localURL.get("port") + xmlPart4 + etlInput.getTargetConnectionPropsMap().get("user")
                  + xmlPart5 + etlInput.getTargetConnectionPropsMap().get("password") + xmlPart6;

         String[] databaseXML = { localXML, targetXML };
         String transformationName = "Kettle - Transformation";
         String sourceDatabaseName = "SOURCE";
         String targetDatabaseName = "TARGET";
         String remoteTableName = new String();

         if (etlInput.getSourceQuery().toUpperCase().indexOf("WHERE") < 0)
            remoteTableName = getTableName(etlInput.getSourceQuery().toUpperCase().substring(
                     (etlInput.getSourceQuery().indexOf("FROM") + 5), (etlInput.getSourceQuery().length())));
         else
            remoteTableName = getTableName(etlInput.getSourceQuery().toUpperCase().substring(
                     (etlInput.getSourceQuery().indexOf("FROM") + 5), etlInput.getSourceQuery().indexOf("WHERE") - 1));
         String localTableName = getTableName(etlInput.getTargetInsert());
         List<String> fieldNames = getRemoteFields(etlInput.getSourceQuery());
         String searchCondition = "";

         if (etlInput.getSourceQuery().toUpperCase().indexOf("WHERE") >= 0) {
            searchCondition = etlInput.getSourceQuery().toUpperCase().substring(
                     etlInput.getSourceQuery().indexOf("WHERE"));
         }
         transMeta = createTransformation(transformationName, sourceDatabaseName, remoteTableName, fieldNames,
                  targetDatabaseName, localTableName, fieldNames, searchCondition, databaseXML);

      } catch (Exception e) {
         e.printStackTrace();
      }
      return transMeta;

   }

   /**
    * Returns a map containing parts of the url(location,port,schemaname)
    * 
    * @param propertiesMap
    * @return
    */
   public static Map<String, Object> extractURL (Map<String, Object> propertiesMap) {
      String url = (String)propertiesMap.get("url");
      Map<String, Object> urlMap = new HashMap<String, Object>();
      StringTokenizer token = new StringTokenizer(url, ":");
      location = null;
      port = null;
      database = null;
      int locationToken;
      int portToken;
      int databaseToken;
      int totalTokens = token.countTokens();
      if (((String)propertiesMap.get("driver")).contains("oracle")) {

         databaseToken = token.countTokens() - 1;
         portToken = token.countTokens() - 2;
         locationToken = token.countTokens() - 3;
         for (int cnt = 0; cnt < totalTokens; cnt++) {
            if (cnt == locationToken) {
               location = token.nextToken().substring(1);
               continue;
            } else if (cnt == portToken) {
               port = token.nextToken();
               continue;
            } else if (cnt == databaseToken) {
               database = token.nextToken();
               continue;
            }
            token.nextToken();

         }
      } else if (((String)propertiesMap.get("driver")).contains("mysql")) {
         String tokens[] = null;
         databaseToken = token.countTokens() - 1;
         portToken = token.countTokens() - 1;
         locationToken = token.countTokens() - 2;
         String urlTokens[] = null;

         for (int i = 0; i < totalTokens; i++) {
            if (i == locationToken) {
               location = token.nextToken().substring(2);
               continue;
            } else if ((i == portToken) || i == (databaseToken)) {
               urlTokens = token.nextToken().split("/");
               continue;
            }
            token.nextToken();

         }
         port = urlTokens[0];
         database = urlTokens[1];

      }
      urlMap.put("location", location);
      urlMap.put("port", port);
      urlMap.put("database", database);
      return urlMap;

   }

   /**
    * Prepares TransMeta by taking Source and Target Parameters
    * 
    * @param transformationName
    * @param sourceDatabaseName
    * @param sourceTableName
    * @param fieldNames
    * @param targetDatabaseName
    * @param targetTableName
    * @param fieldNames2
    * @param sourceCondition
    * @return
    * @throws KettleException
    */
   public static final TransMeta createTransformation (String transformationName, String sourceDatabaseName,
            String sourceTableName, List<String> fieldNames, String targetDatabaseName, String targetTableName,
            List<String> fieldNames2, String sourceCondition, String[] databasesXML) throws ETLException {
      EnvUtil.environmentInit();
      try {
         TransMeta transMeta = new TransMeta();
         transMeta.setName(transformationName);

         // Add the database connections

         for (int i = 0; i < databasesXML.length; i++) {

            DatabaseMeta databaseMeta = new DatabaseMeta(databasesXML[i]);
            transMeta.addDatabase(databaseMeta);
         }

         DatabaseMeta sourceDBInfo = transMeta.findDatabase(sourceDatabaseName);
         DatabaseMeta targetDBInfo = transMeta.findDatabase(targetDatabaseName);

         //
         // Add a note
         //
         String note = "Reads from table [" + sourceTableName + "] on database [" + sourceDBInfo + "]" + Const.CR;
         note += "After that, it writes to table [" + targetTableName + "] on database [" + targetDBInfo + "]";

         NotePadMeta ni = new NotePadMeta(note, 150, 10, -1, -1);
         transMeta.addNote(ni);

         // 
         // create the source step...
         //
         String fromstepname = "read from [" + sourceTableName + "]";

         TableInputMeta tii = new TableInputMeta();
         tii.setDatabaseMeta(sourceDBInfo);
         String selectSQL = "SELECT " + Const.CR;
         for (int i = 0; i < fieldNames.size(); i++) {
            if (i > 0)
               selectSQL += ", ";
            else
               selectSQL += "  ";
            selectSQL += fieldNames.get(i) + Const.CR;
         }
         selectSQL += "FROM " + sourceTableName;
         selectSQL += " " + sourceCondition;
         tii.setSQL(selectSQL);

         StepLoader steploader = StepLoader.getInstance();

         String fromstepid = steploader.getStepPluginID(tii);
         StepMeta fromstep = new StepMeta(fromstepid, fromstepname, (StepMetaInterface) tii);
         fromstep.setLocation(150, 100);
         fromstep.setDraw(true);
         fromstep.setDescription("Reads from table [" + sourceTableName + "] on database [" + sourceDBInfo + "]");

         transMeta.addStep(fromstep);

         //
         // add logic to rename fields
         // Use metadata logic in SelectValues, use SelectValueInfo...
         //

         SelectValuesMeta svi = new SelectValuesMeta();

         svi.allocate(0, 0, fieldNames.size());
         for (int i = 0; i < fieldNames.size(); i++) {
            SelectMetadataChange meta[] = svi.getMeta();
            meta[i] = new SelectMetadataChange();
            meta[i].setName(fieldNames.get(i));
            meta[i].setRename(fieldNames2.get(i));
         }

         String selstepname = "Rename field names";

         String selstepid = steploader.getStepPluginID(svi);
         StepMeta selstep = new StepMeta(selstepid, selstepname, (StepMetaInterface) svi);
         selstep.setLocation(350, 100);
         selstep.setDraw(true);
         selstep.setDescription("Rename field names");
         transMeta.addStep(selstep);

         TransHopMeta shi = new TransHopMeta(fromstep, selstep);
         transMeta.addTransHop(shi);
         fromstep = selstep;

         // 
         // Create the target step...
         //
         //
         // Add the TableOutputMeta step...
         //
         String tostepname = "write to [" + targetTableName + "]";
         TableOutputMeta toi = new TableOutputMeta();
         toi.setDatabaseMeta(targetDBInfo);
         toi.setTablename(targetTableName);
         toi.setCommitSize(200);
         toi.setTruncateTable(false);

         String tostepid = steploader.getStepPluginID(toi);
         StepMeta tostep = new StepMeta(tostepid, tostepname, (StepMetaInterface) toi);
         tostep.setLocation(550, 100);
         tostep.setDraw(true);
         tostep.setDescription("Write to table [" + targetTableName + "] on database [" + targetDBInfo + "]");
         transMeta.addStep(tostep);

         //
         // Add a hop between the two steps...
         //
         TransHopMeta hi = new TransHopMeta(fromstep, tostep);
         transMeta.addTransHop(hi);

         // OK, if we're still here: overwrite the current transformation...
         return transMeta;
      } catch (Exception e) {
         throw new ETLException(004, "An unexpected error occurred creating the new transformation", e);
      }
   }

   /**
    * returns the table name from the query
    * 
    * @param query
    * @return
    */
   private static String getTableName (String query) {

      int tokenCount = 0;
      String tableName = new String();
      int tableNameToken = 0;
      StringTokenizer token = new StringTokenizer(query);
      if (query.contains("select") || query.contains("SELECT"))
         tableNameToken = token.countTokens() - 1;
      else if (query.contains("insert") || query.contains("INSERT"))
         tableNameToken = 2;
      while (token.hasMoreTokens()) {

         if (tokenCount == (tableNameToken))

            tableName = (String) token.nextElement();
         else
            token.nextElement();
         tokenCount++;

      }

      return tableName;

   }

   /**
    * returns fields of the remotequery as list
    * 
    * @param query
    * @return
    */
   private static List<String> getRemoteFields (String query) {
      StringTokenizer token = new StringTokenizer(query);
      int fieldToken = 1;
      int totalTokens = token.countTokens();
      String fieldList = null;
      for (int cnt = 0; cnt < totalTokens; cnt++) {
         if (cnt == fieldToken) {
            fieldList = token.nextToken();

         } else
            token.nextToken();

      }
      StringTokenizer fieldToken1 = new StringTokenizer(fieldList, ",");
      List<String> fields = new ArrayList<String>();
      int t = fieldToken1.countTokens();

      for (int cnt = 0; cnt < t; cnt++) {
         fields.add(fieldToken1.nextToken());

      }
      return fields;
   }

   /**
    * returns fields of the localquery as list
    * 
    * @param query
    * @return
    */
   private static List<String> getLocalFields (String query) {
      StringTokenizer token = new StringTokenizer(query, "(");
      int tokenCount;
      int fieldToken = 1;
      tokenCount = token.countTokens();
      String field = null;
      for (int cnt = 0; cnt < tokenCount; cnt++) {
         if (cnt == fieldToken) {
            field = token.nextToken();
         } else
            token.nextToken();
      }
      StringTokenizer Tokenizer = new StringTokenizer(field, ")");
      String fields = null;
      fieldToken = 0;
      tokenCount = Tokenizer.countTokens();
      for (int cnt = 0; cnt < tokenCount; cnt++) {
         if (cnt == fieldToken) {
            fields = Tokenizer.nextToken();

         } else
            Tokenizer.nextToken();

      }
      StringTokenizer fieldTokens = new StringTokenizer(fields, ",");
      List<String> fieldList = new ArrayList<String>();
      tokenCount = fieldTokens.countTokens();
      for (int cnt = 0; cnt < tokenCount; cnt++)
         fieldList.add(fieldTokens.nextToken());
      return fieldList;
   }

}
