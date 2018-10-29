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


package com.execue.das.datatransfer.etl.service.impl;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;

import scriptella.execution.EtlExecutor;
import scriptella.execution.EtlExecutorException;
import scriptella.execution.ExecutionStatistics;

import com.execue.core.configuration.ExecueConfiguration;
import com.execue.core.exception.ConfigurationException;
import com.execue.das.datatransfer.etl.bean.ETLInput;
import com.execue.das.datatransfer.etl.exception.ETLException;
import com.execue.das.datatransfer.etl.helper.ScriptellaETLInputHelper;
import com.execue.das.exception.DataAccessServicesExceptionCodes;

public class ScriptellaETLServiceTest {

   ETLInput            etlInput = new ETLInput();

   ExecueConfiguration execueConfiguration;

   // boolean test = true;

   @Before
   public void setUp () {
      try {
         List<String> filenames = new ArrayList<String>();
         filenames.add("/bean-config/answers-catalog-test/scriptella-etl-input.xml");
         ExecueConfiguration execueConfiguration = new ExecueConfiguration(filenames);
         Map<String, Object> remoteMap = new HashMap<String, Object>();
         Map<String, Object> localMap = new HashMap<String, Object>();

         remoteMap.put("driver", execueConfiguration.getProperty("remote-asset-properties.driver"));
         remoteMap.put("url", execueConfiguration.getProperty("remote-asset-properties.url"));
         remoteMap.put("user", execueConfiguration.getProperty("remote-asset-properties.userid"));
         remoteMap.put("password", execueConfiguration.getProperty("remote-asset-properties.password"));
         etlInput.setSourceConnectionPropsMap(remoteMap);
         /*
          * for localConnectionProperties
          */
         localMap.put("driver", execueConfiguration.getProperty("local-asset-properties.driver"));
         localMap.put("url", execueConfiguration.getProperty("local-asset-properties.url"));
         localMap.put("user", execueConfiguration.getProperty("local-asset-properties.userid"));
         localMap.put("password", execueConfiguration.getProperty("local-asset-properties.password"));
         etlInput.setTargetConnectionPropsMap(localMap);
         etlInput.setSourceQuery(execueConfiguration.getProperty("select-statement"));
         etlInput.setTargetInsert(execueConfiguration.getProperty("insert-statement"));
      } catch (ConfigurationException e) {
         e.printStackTrace();
      }

   }

   @After
   public void tearDown () {

   }

   // @Test
   public void testScriptellaTransformationSuccessCase () throws ETLException {
      int count = 0;
      EtlExecutor etlExecutor = EtlExecutor.newExecutor(ScriptellaETLInputHelper.prepareScriptellaInput(etlInput));

      try {
         ExecutionStatistics statistics = etlExecutor.execute();
         count = statistics.getExecutedStatementsCount();
      } catch (EtlExecutorException etlExecutorException) {
         throw new ETLException(DataAccessServicesExceptionCodes.DEFAULT_ETL_SCRIPTELLA_EXCEPTION_CODE,
                  "EtlExecutorException", etlExecutorException);
      }
      assertTrue("Rows less than zero", count > 0);
   }

   // @Test
   public void copyACL () {
      try {

         String etlInput = null;
         EtlExecutor etlExecutor = null;
         ExecutionStatistics statistics = null;

         etlInput = getAccountACLETLString();
         etlExecutor = EtlExecutor.newExecutor(etlInput);
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Account2 : " + statistics.getExecutedStatementsCount());

         etlInput = getBillingACLETLString();
         etlExecutor = EtlExecutor.newExecutor(etlInput);
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Billing2 : " + statistics.getExecutedStatementsCount());

         etlInput = getProductETLString();
         System.out.println(etlInput);
         etlExecutor = EtlExecutor.newExecutor(etlInput);
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Card Type : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getAccountStatusETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Ext Status : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPerfYMETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Perf YM : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPerfQTRETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Perf QTR : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPerfYRETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Perf YR : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPortSegETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Port Seg : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPortSegGroupETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Port Seg Grp : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getUsageSegETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Usage Seg : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getUsageSegGroupETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Usage Seg Grp : " + statistics.getExecutedStatementsCount());

      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   // @Test
   public void copyLITE () {
      try {

         String etlInput = null;
         EtlExecutor etlExecutor = null;
         ExecutionStatistics statistics = null;

         etlInput = getAccountLITEETLString();
         etlExecutor = EtlExecutor.newExecutor(etlInput);
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Account2 : " + statistics.getExecutedStatementsCount());

         long interval = 5000;
         long currentEnd = 0;
         long currentStart = 0;
         long max = 257598;
         for (int i = 0; currentEnd <= max; i++) {
            currentStart = i * interval;
            currentEnd = currentStart + interval + 1;
            System.out.println("Trying to get Records from [" + currentStart + "] to [" + currentEnd + "]");

            etlInput = getBillingLITEETLString(currentStart, currentEnd);
            etlExecutor = EtlExecutor.newExecutor(etlInput);
            statistics = etlExecutor.execute();
            System.out.println("Records Copied From Billing2 : " + statistics.getExecutedStatementsCount());
            if (interval + 1 != statistics.getExecutedStatementsCount()) {
               System.out.println("record Count did not match");
               break;
            }

         }

         etlInput = getProductETLString();
         etlExecutor = EtlExecutor.newExecutor(etlInput);
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Card Type : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getAccountStatusETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Ext Status : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPerfYMETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Perf YM : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPerfQTRETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Perf QTR : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPerfYRETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Perf YR : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPortSegETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Port Seg : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPortSegGroupETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Port Seg Grp : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getUsageSegETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Usage Seg : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getUsageSegGroupETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Usage Seg Grp : " + statistics.getExecutedStatementsCount());

      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   // @Test
   public void copyNORMAL () {
      try {

         String etlInput = null;
         EtlExecutor etlExecutor = null;
         ExecutionStatistics statistics = null;

         etlInput = getProductETLString();
         etlExecutor = EtlExecutor.newExecutor(etlInput);
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Card Type : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getAccountStatusETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Ext Status : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPerfYMETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Perf YM : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPerfQTRETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Perf QTR : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPerfYRETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Perf YR : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPortSegETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Port Seg : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getPortSegGroupETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Port Seg Grp : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getUsageSegETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Usage Seg : " + statistics.getExecutedStatementsCount());

         etlExecutor = EtlExecutor.newExecutor(getUsageSegGroupETLString());
         statistics = etlExecutor.execute();
         System.out.println("Records Copied From Usage Seg Grp : " + statistics.getExecutedStatementsCount());

         long interval = 5000;
         long currentEnd = 0;
         long currentStart = 0;
         long max = 382280;

         for (int i = 0; currentEnd <= max; i++) {
            currentStart = i * interval;
            currentEnd = currentStart + interval + 1;
            System.out.println("Trying to get Records from [" + currentStart + "] to [" + currentEnd + "]");

            etlInput = getAccountNORMALETLString(currentStart, currentEnd);
            etlExecutor = EtlExecutor.newExecutor(etlInput);
            statistics = etlExecutor.execute();
            System.out.println("Records Copied From Account : " + statistics.getExecutedStatementsCount());
            if (interval + 1 != statistics.getExecutedStatementsCount()) {
               System.out.println("record Count did not match");
               break;
            }

         }

         for (int outerIndex = 4; outerIndex <= 9; outerIndex++) {
            interval = 5000;
            currentEnd = 0;
            currentStart = 0;
            max = 1000001;
            for (int i = 0; currentEnd <= max; i++) {
               currentStart = i * interval;
               currentEnd = currentStart + interval + 1;
               System.out.println("Trying to get Records from [" + currentStart + "] to [" + currentEnd + "]");

               etlInput = getBillingNORMALETLString("BIL" + outerIndex, currentStart, currentEnd);
               etlExecutor = EtlExecutor.newExecutor(etlInput);
               statistics = etlExecutor.execute();
               System.out.println("Records Copied From BIL" + outerIndex + " to Billing : "
                        + statistics.getExecutedStatementsCount());
               if (interval + 1 != statistics.getExecutedStatementsCount()) {
                  System.out.println("record Count did not match");
                  break;
               }

            }
         }

      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   private String getSourceConnectionString () {
      // ACL 
      // return "<connection id=\"source\" driver=\"oracle.jdbc.driver.OracleDriver\" url=\"jdbc:oracle:thin:@10.10.56.60:1521:devdb\" user=\"WH_DEMOCARDS_ACL\" password=\"WH_DEMOCARDS_ACL\"/>";
      // SMALL AND NORMAL
      return "<connection id=\"source\" driver=\"oracle.jdbc.driver.OracleDriver\" url=\"jdbc:oracle:thin:@10.10.56.60:1521:devdb\" user=\"wh_democards\" password=\"wh_democards\"/>";
   }

   private String getTargetConnectionString () {
      // ACL //return "<connection id=\"target\" driver=\"com.ibm.db2.jcc.DB2Driver\" url=\"jdbc:db2j:net://10.10.56.61:50000/DEMOCRAD\" user=\"db2admin\" password=\"execue\"/>";
      // NORM // 
      return "<connection id=\"target\" driver=\"com.ibm.db2.jcc.DB2Driver\" url=\"jdbc:db2j:net://10.10.56.61:50000/NORMALDC\" user=\"db2admin\" password=\"execue\"/>";
      // SMALL
      // return "<connection id=\"target\" driver=\"com.ibm.db2.jcc.DB2Driver\" url=\"jdbc:db2j:net://10.10.56.61:50000/SMALLDC\" user=\"db2admin\" password=\"execue\"/>";
   }

   private String getAccountACLETLString () {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT ACCOUNT_ID AS A FROM ACCOUNT2 ORDER BY ACCOUNT_ID");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ACCOUNT2 (ACCOUNT_ID) VALUES");
      sb.append("(?A)");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getAccountLITEETLString () {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT ACCOUNT_ID AS A, SLICE AS B FROM ACCOUNT2 ORDER BY ACCOUNT_ID");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ACCOUNT2 (ACCOUNT_ID, SLICE) VALUES");
      sb.append("(?A, ?B)");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getAccountNORMALETLString (long start, long end) {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT * FROM (");
      sb.append("SELECT ACCOUNT_ID AS A, SLICE AS B, ROWNUM AS RN FROM ACCOUNT ORDER BY ACCOUNT_ID");
      sb.append(") INN ");
      sb.append("WHERE RN &gt; " + start + " ");
      sb.append("AND RN &lt; " + end + " ");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ACCOUNT (ACCOUNT_ID, SLICE) VALUES");
      sb.append("(?A, ?B)");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getBillingACLETLString () {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT ");
      sb
               .append("ACCOUNT_ID AS A, EXT_STATUS AS B, MRC_BAL AS C, MRC_AMT AS D, PORT_SEG AS E, FICO_SCORE_NUMBER AS F, REVOLVE_BAL AS G,");
      sb.append("REVOLVE_FLAG AS H, DEL_BAL AS I, DEL_BAL_FLAG AS J, PERF_YM AS K, USAGE_SEG AS L,");
      sb.append("UTIL AS M, CLOSE_IND AS N, OPEN_IND AS O, CARD_TYPE_CD AS P, PERF_QTR AS Q, PERF_YR AS R");
      sb.append(" FROM ");
      sb.append("BILLING2");
      sb.append(" ORDER BY ");
      sb.append("ACCOUNT_ID");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ");
      sb.append("BILLING2 (");
      sb.append("ACCOUNT_ID, EXT_STATUS, MRC_BAL, MRC_AMT, PORT_SEG, FICO_SCORE_NUMBER, REVOLVE_BAL,");
      sb.append("REVOLVE_FLAG, DEL_BAL, DEL_BAL_FLAG, PERF_YM, USAGE_SEG,");
      sb.append("UTIL, CLOSE_IND, OPEN_IND, CARD_TYPE_CD, PERF_QTR, PERF_YR");
      sb.append(") VALUES (");
      sb.append("?A, ?B, ?C, ?D, ?E, ?F, ?G, ?H, ?I, ?J, ?K, ?L, ?M, ?N, ?O, ?P, ?Q, ?R");
      sb.append(")");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getBillingLITEETLString (long start, long end) {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");

      sb.append("SELECT * FROM ( ");

      sb.append("SELECT ");
      sb
               .append("ACCOUNT_ID AS AX20, AC_LOGIN AS AX88, AC_REG AS AX87, BEH_SCORE_NUMBER AS AX39, BT_AMT AS AX33, BT_AMT_FLAG AS AX73, BT_BAL AS AX32, BT_BAL_FLAG AS AX58, ");
      sb
               .append("BT_FEE AS AX43, BT_FEE_FLAG AS AX71, CARD_OPTION AS AX24, CARD_TYPE AS AX86, CARD_TYPE_CD AS AX13, CASH_AMT AS AX35, CASH_AMT_FLAG AS AX74, ");
      sb
               .append("CASH_BAL AS AX34, CASH_BAL_FLAG AS AX59, CBB_BANK AS AX65, CBB_BANK1 AS AX25, CBB_BANK2 AS AX26, CBB_BANK_FLAG AS AX66, CBB_EARNED AS AX23, ");
      sb
               .append("CBB_EARNED_FLAG AS AX64, CHARGED_OFF AS AX12, CLOSE_IND AS AX10, CREDIT_LINE AS AX91, CYCL_1_DLQ AS AX92, CYCL_1_DLQ_BAL AS AX101, CYCL_1_PLUS_DLQ AS AX98, ");
      sb
               .append("CYCL_1_PLUS_DLQ_BAL AS AX4, CYCL_2_DLQ AS AX93, CYCL_2_DLQ_BAL AS AX102, CYCL_2_PLUS_DLQ AS AX99, CYCL_2_PLUS_DLQ_BAL AS AX5, CYCL_3_DLQ AS AX94, ");
      sb
               .append("CYCL_3_DLQ_BAL AS AX103, CYCL_3_PLUS_DLQ AS AX100, CYCL_3_PLUS_DLQ_BAL AS AX6, CYCL_4_DLQ AS AX95, CYCL_4_DLQ_BAL AS AX1, CYCL_5_DLQ AS AX96, ");
      sb
               .append("CYCL_5_DLQ_BAL AS AX2, CYCL_6_DLQ AS AX97, CYCL_6_DLQ_BAL AS AX3, CYCL_BAL AS AX27, CYCL_BAL_FLAG AS AX54, DAYS_DELINQUENT AS AX78, DEBIT_ACTIVE_FLAG AS AX75, ");
      sb
               .append("DEL_BAL AS AX79, DEL_BAL_FLAG AS AX80, DLQNT_CNT AS AX40, EXP_BT_BAL AS AX31, EXP_BT_BAL_FLAG AS AX56, EXP_BT_FLAG AS AX57, EXT_STATUS AS AX22, ");
      sb
               .append("EXT_STATUS_DESC AS AX51, FICO_RANGE_CATEGORY AS AX82, FICO_SCORE_NUMBER AS AX38, FIN_CHARGE AS AX28, FIN_CHARGE_FLAG AS AX68, GOOD_BAL AS AX60, ");
      sb
               .append("GOOD_BAL_FLAG AS AX61, GROSS_ACTIVE_FLAG AS AX76, INCENTIVE_CDE AS AX85, INCENTIVE_TYPE AS AX84, LATE_FEE AS AX41, LATE_FEE_FLAG AS AX69, MOB AS AX50, ");
      sb
               .append("MRC_AMT AS AX30, MRC_AMT_FLAG AS AX72, MRC_BAL AS AX29, MRC_BAL_FLAG AS AX55, NEWSLETTER_OPTIN AS AX89, OFFUSBAL AS AX49, OFFUS_CRDTLINE AS AX48, ");
      sb
               .append("OFFUS_UTIL AS AX7, OPEN_IND AS AX11, OPEN_TO_BUY AS AX37, OVERLIMIT_FEE AS AX42, OVERLIMIT_FEE_FLAG AS AX70, PAYMENT_AMOUNT AS AX44, PAYMENT_FLAG AS AX67, ");
      sb
               .append("PERF_MTHLY_DT AS AX21, PERF_QTR AS AX14, PERF_YM AS AX81, PERF_YR AS AX16, PORT_SEG AS AX36, PURCHASE_ACTIVE_FLAG AS AX77, REVOLVE_BAL AS AX62, ");
      sb
               .append("REVOLVE_FLAG AS AX63, SPF1 AS AX45, SPF2 AS AX46, SPF3 AS AX47, TOT_MRC_BAL AS AX52, TOT_MRC_BAL_FLAG AS AX53, USAGE_SEG AS AX83, UTIL AS AX8, ");
      sb
               .append("VINTAGE_DATE AS AX19, VNTG_DT AS AX18, VNTG_QTR AS AX15, VNTG_YM AS AX90, VNTG_YR AS AX17, WO_IND AS AX9");
      sb.append(", ROWNUM AS RN ");
      sb.append(" FROM ");
      sb.append("BILLING2 ");
      sb.append(" ORDER BY ");
      sb.append("ACCOUNT_ID");

      sb.append(") INN ");
      sb.append("WHERE RN &gt; " + start + " ");
      sb.append("AND RN &lt; " + end + " ");

      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ");
      sb.append("BILLING2 (");
      sb
               .append("ACCOUNT_ID, AC_LOGIN, AC_REG, BEH_SCORE_NUMBER, BT_AMT, BT_AMT_FLAG, BT_BAL, BT_BAL_FLAG, BT_FEE, BT_FEE_FLAG, CARD_OPTION, CARD_TYPE, CARD_TYPE_CD, ");
      sb
               .append("CASH_AMT, CASH_AMT_FLAG, CASH_BAL, CASH_BAL_FLAG, CBB_BANK, CBB_BANK1, CBB_BANK2, CBB_BANK_FLAG, CBB_EARNED, CBB_EARNED_FLAG, CHARGED_OFF, CLOSE_IND, ");
      sb
               .append("CREDIT_LINE, CYCL_1_DLQ, CYCL_1_DLQ_BAL, CYCL_1_PLUS_DLQ, CYCL_1_PLUS_DLQ_BAL, CYCL_2_DLQ, CYCL_2_DLQ_BAL, CYCL_2_PLUS_DLQ, CYCL_2_PLUS_DLQ_BAL, ");
      sb
               .append("CYCL_3_DLQ, CYCL_3_DLQ_BAL, CYCL_3_PLUS_DLQ, CYCL_3_PLUS_DLQ_BAL, CYCL_4_DLQ, CYCL_4_DLQ_BAL, CYCL_5_DLQ, CYCL_5_DLQ_BAL, CYCL_6_DLQ, CYCL_6_DLQ_BAL, ");
      sb
               .append("CYCL_BAL, CYCL_BAL_FLAG, DAYS_DELINQUENT, DEBIT_ACTIVE_FLAG, DEL_BAL, DEL_BAL_FLAG, DLQNT_CNT, EXP_BT_BAL, EXP_BT_BAL_FLAG, EXP_BT_FLAG, EXT_STATUS, ");
      sb
               .append("EXT_STATUS_DESC, FICO_RANGE_CATEGORY, FICO_SCORE_NUMBER, FIN_CHARGE, FIN_CHARGE_FLAG, GOOD_BAL, GOOD_BAL_FLAG, GROSS_ACTIVE_FLAG, INCENTIVE_CDE, ");
      sb
               .append("INCENTIVE_TYPE, LATE_FEE, LATE_FEE_FLAG, MOB, MRC_AMT, MRC_AMT_FLAG, MRC_BAL, MRC_BAL_FLAG, NEWSLETTER_OPTIN, OFFUSBAL, OFFUS_CRDTLINE, OFFUS_UTIL, ");
      sb
               .append("OPEN_IND, OPEN_TO_BUY, OVERLIMIT_FEE, OVERLIMIT_FEE_FLAG, PAYMENT_AMOUNT, PAYMENT_FLAG, PERF_MTHLY_DT, PERF_QTR, PERF_YM, PERF_YR, PORT_SEG, ");
      sb
               .append("PURCHASE_ACTIVE_FLAG, REVOLVE_BAL, REVOLVE_FLAG, SPF1, SPF2, SPF3, TOT_MRC_BAL, TOT_MRC_BAL_FLAG, USAGE_SEG, UTIL, VINTAGE_DATE, VNTG_DT, ");
      sb.append("VNTG_QTR, VNTG_YM, VNTG_YR, WO_IND");
      sb.append(") VALUES (");
      sb
               .append("?AX20, ?AX88, ?AX87, ?AX39, ?AX33, ?AX73, ?AX32, ?AX58, ?AX43, ?AX71, ?AX24, ?AX86, ?AX13, ?AX35, ?AX74, ?AX34, ?AX59, ?AX65, ?AX25, ?AX26, ?AX66, ");
      sb
               .append("?AX23, ?AX64, ?AX12, ?AX10, ?AX91, ?AX92, ?AX101, ?AX98, ?AX4, ?AX93, ?AX102, ?AX99, ?AX5, ?AX94, ?AX103, ?AX100, ?AX6, ?AX95, ?AX1, ?AX96, ?AX2, ");
      sb
               .append("?AX97, ?AX3, ?AX27, ?AX54, ?AX78, ?AX75, ?AX79, ?AX80, ?AX40, ?AX31, ?AX56, ?AX57, ?AX22, ?AX51, ?AX82, ?AX38, ?AX28, ?AX68, ?AX60, ?AX61, ?AX76, ");
      sb
               .append("?AX85, ?AX84, ?AX41, ?AX69, ?AX50, ?AX30, ?AX72, ?AX29, ?AX55, ?AX89, ?AX49, ?AX48, ?AX7, ?AX11, ?AX37, ?AX42, ?AX70, ?AX44, ?AX67, ?AX21, ?AX14, ");
      sb
               .append("?AX81, ?AX16, ?AX36, ?AX77, ?AX62, ?AX63, ?AX45, ?AX46, ?AX47, ?AX52, ?AX53, ?AX83, ?AX8, ?AX19, ?AX18, ?AX15, ?AX90, ?AX17, ?AX9");
      sb.append(")");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getBillingNORMALETLString (String tableName, long start, long end) {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT * FROM ( ");
      sb.append("SELECT ");
      sb
               .append("ACCOUNT_ID AS AX20, AC_LOGIN AS AX88, AC_REG AS AX87, BEH_SCORE_NUMBER AS AX39, BT_AMT AS AX33, BT_AMT_FLAG AS AX73, BT_BAL AS AX32, ");
      sb
               .append("BT_BAL_FLAG AS AX58, BT_FEE AS AX43, BT_FEE_FLAG AS AX71, CARD_OPTION AS AX24, CARD_TYPE AS AX86, CARD_TYPE_CD AS AX13, CASH_AMT AS AX35, ");
      sb
               .append("CASH_AMT_FLAG AS AX74, CASH_BAL AS AX34, CASH_BAL_FLAG AS AX59, CBB_BANK AS AX65, CBB_BANK1 AS AX25, CBB_BANK2 AS AX26, CBB_BANK_FLAG AS AX66, ");
      sb
               .append("CBB_EARNED AS AX23, CBB_EARNED_FLAG AS AX64, CHARGED_OFF AS AX12, CLOSE_IND AS AX10, CREDIT_LINE AS AX92, CYCL_1_DLQ AS AX93, ");
      sb
               .append("CYCL_1_DLQ_BAL AS AX102, CYCL_1_PLUS_DLQ AS AX99, CYCL_1_PLUS_DLQ_BAL AS AX4, CYCL_2_DLQ AS AX94, CYCL_2_DLQ_BAL AS AX103, CYCL_2_PLUS_DLQ AS AX100, ");
      sb
               .append("CYCL_2_PLUS_DLQ_BAL AS AX5, CYCL_3_DLQ AS AX95, CYCL_3_DLQ_BAL AS AX104, CYCL_3_PLUS_DLQ AS AX101, CYCL_3_PLUS_DLQ_BAL AS AX6, ");
      sb
               .append("CYCL_4_DLQ AS AX96, CYCL_4_DLQ_BAL AS AX1, CYCL_5_DLQ AS AX97, CYCL_5_DLQ_BAL AS AX2, CYCL_6_DLQ AS AX98, CYCL_6_DLQ_BAL AS AX3, CYCL_BAL AS AX27, ");
      sb
               .append("CYCL_BAL_FLAG AS AX54, DAYS_DELINQUENT AS AX78, DEBIT_ACTIVE_FLAG AS AX75, DEL_BAL AS AX79, DEL_BAL_FLAG AS AX80, DLQNT_CNT AS AX40, ");
      sb
               .append("EFF_APR AS AX91, EXP_BT_BAL AS AX31, EXP_BT_BAL_FLAG AS AX56, EXP_BT_FLAG AS AX57, EXT_STATUS AS AX22, EXT_STATUS_DESC AS AX51, ");
      sb
               .append("FICO_RANGE_CATEGORY AS AX82, FICO_SCORE_NUMBER AS AX38, FIN_CHARGE AS AX28, FIN_CHARGE_FLAG AS AX68, GOOD_BAL AS AX60, GOOD_BAL_FLAG AS AX61, ");
      sb
               .append("GROSS_ACTIVE_FLAG AS AX76, INCENTIVE_CDE AS AX85, INCENTIVE_TYPE AS AX84, LATE_FEE AS AX41, LATE_FEE_FLAG AS AX69, MOB AS AX50, MRC_AMT AS AX30, ");
      sb
               .append("MRC_AMT_FLAG AS AX72, MRC_BAL AS AX29, MRC_BAL_FLAG AS AX55, NEWSLETTER_OPTIN AS AX89, OFFUSBAL AS AX49, OFFUS_CRDTLINE AS AX48, OFFUS_UTIL AS AX7, ");
      sb
               .append("OPEN_IND AS AX11, OPEN_TO_BUY AS AX37, OVERLIMIT_FEE AS AX42, OVERLIMIT_FEE_FLAG AS AX70, PAYMENT_AMOUNT AS AX44, PAYMENT_FLAG AS AX67, ");
      sb
               .append("PERF_MTHLY_DT AS AX21, PERF_QTR AS AX14, PERF_YM AS AX81, PERF_YR AS AX15, PORT_SEG AS AX36, PURCHASE_ACTIVE_FLAG AS AX77, REVOLVE_BAL AS AX62, ");
      sb
               .append("REVOLVE_FLAG AS AX63, SPF1 AS AX45, SPF2 AS AX46, SPF3 AS AX47, TOT_MRC_BAL AS AX52, TOT_MRC_BAL_FLAG AS AX53, USAGE_SEG AS AX83, UTIL AS AX8, ");
      sb
               .append("VINTAGE_DATE AS AX19, VNTG_DT AS AX18, VNTG_QTR AS AX16, VNTG_YM AS AX90, VNTG_YR AS AX17, WO_IND AS AX9");
      sb.append(", ROWNUM AS RN ");
      sb.append(" FROM ");
      sb.append(tableName);
      sb.append(" ORDER BY ");
      sb.append("ACCOUNT_ID");
      sb.append(") INN ");
      sb.append("WHERE RN &gt; " + start + " ");
      sb.append("AND RN &lt; " + end + " ");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ");
      sb.append("BILLING (");
      sb
               .append("ACCOUNT_ID, AC_LOGIN, AC_REG, BEH_SCORE_NUMBER, BT_AMT, BT_AMT_FLAG, BT_BAL, BT_BAL_FLAG, BT_FEE, BT_FEE_FLAG, CARD_OPTION, CARD_TYPE, CARD_TYPE_CD, ");
      sb
               .append("CASH_AMT, CASH_AMT_FLAG, CASH_BAL, CASH_BAL_FLAG, CBB_BANK, CBB_BANK1, CBB_BANK2, CBB_BANK_FLAG, CBB_EARNED, CBB_EARNED_FLAG, CHARGED_OFF, CLOSE_IND,");
      sb
               .append("CREDIT_LINE, CYCL_1_DLQ, CYCL_1_DLQ_BAL, CYCL_1_PLUS_DLQ, CYCL_1_PLUS_DLQ_BAL, CYCL_2_DLQ, CYCL_2_DLQ_BAL, CYCL_2_PLUS_DLQ, CYCL_2_PLUS_DLQ_BAL, ");
      sb
               .append("CYCL_3_DLQ, CYCL_3_DLQ_BAL, CYCL_3_PLUS_DLQ, CYCL_3_PLUS_DLQ_BAL, CYCL_4_DLQ, CYCL_4_DLQ_BAL, CYCL_5_DLQ, CYCL_5_DLQ_BAL, CYCL_6_DLQ, CYCL_6_DLQ_BAL,");
      sb
               .append("CYCL_BAL, CYCL_BAL_FLAG, DAYS_DELINQUENT, DEBIT_ACTIVE_FLAG, DEL_BAL, DEL_BAL_FLAG, DLQNT_CNT, EFF_APR, EXP_BT_BAL, EXP_BT_BAL_FLAG, EXP_BT_FLAG, ");
      sb
               .append("EXT_STATUS, EXT_STATUS_DESC, FICO_RANGE_CATEGORY, FICO_SCORE_NUMBER, FIN_CHARGE, FIN_CHARGE_FLAG, GOOD_BAL, GOOD_BAL_FLAG, GROSS_ACTIVE_FLAG, ");
      sb
               .append("INCENTIVE_CDE, INCENTIVE_TYPE, LATE_FEE, LATE_FEE_FLAG, MOB, MRC_AMT, MRC_AMT_FLAG, MRC_BAL, MRC_BAL_FLAG, NEWSLETTER_OPTIN, OFFUSBAL, OFFUS_CRDTLINE, ");
      sb
               .append("OFFUS_UTIL, OPEN_IND, OPEN_TO_BUY, OVERLIMIT_FEE, OVERLIMIT_FEE_FLAG, PAYMENT_AMOUNT, PAYMENT_FLAG, PERF_MTHLY_DT, PERF_QTR, PERF_YM, PERF_YR, ");
      sb
               .append("PORT_SEG, PURCHASE_ACTIVE_FLAG, REVOLVE_BAL, REVOLVE_FLAG, SPF1, SPF2, SPF3, TOT_MRC_BAL, TOT_MRC_BAL_FLAG, USAGE_SEG, UTIL, VINTAGE_DATE, ");
      sb.append("VNTG_DT, VNTG_QTR, VNTG_YM, VNTG_YR, WO_IND");
      sb.append(") VALUES (");
      sb
               .append("?AX20, ?AX88, ?AX87, ?AX39, ?AX33, ?AX73, ?AX32, ?AX58, ?AX43, ?AX71, ?AX24, ?AX86, ?AX13, ?AX35, ?AX74, ?AX34, ?AX59, ?AX65, ?AX25, ?AX26, ?AX66, ");
      sb
               .append("?AX23, ?AX64, ?AX12, ?AX10, ?AX92, ?AX93, ?AX102, ?AX99, ?AX4, ?AX94, ?AX103, ?AX100, ?AX5, ?AX95, ?AX104, ?AX101, ?AX6, ?AX96, ?AX1, ?AX97, ?AX2, ");
      sb
               .append("?AX98, ?AX3, ?AX27, ?AX54, ?AX78, ?AX75, ?AX79, ?AX80, ?AX40, ?AX91, ?AX31, ?AX56, ?AX57, ?AX22, ?AX51, ?AX82, ?AX38, ?AX28, ?AX68, ?AX60, ?AX61, ");
      sb
               .append("?AX76, ?AX85, ?AX84, ?AX41, ?AX69, ?AX50, ?AX30, ?AX72, ?AX29, ?AX55, ?AX89, ?AX49, ?AX48, ?AX7, ?AX11, ?AX37, ?AX42, ?AX70, ?AX44, ?AX67, ?AX21, ");
      sb
               .append("?AX14, ?AX81, ?AX15, ?AX36, ?AX77, ?AX62, ?AX63, ?AX45, ?AX46, ?AX47, ?AX52, ?AX53, ?AX83, ?AX8, ?AX19, ?AX18, ?AX16, ?AX90, ?AX17, ?AX9");
      sb.append(")");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getPerfYMETLString () {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT ");
      sb.append("PERF_YM AS A, PERF_YM_DESC AS B");
      sb.append(" FROM ");
      sb.append("PERF_YM");
      sb.append(" ORDER BY ");
      sb.append("PERF_YM");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ");
      sb.append("PERF_YM (");
      sb.append("PERF_YM, PERF_YM_DESC");
      sb.append(") VALUES (");
      sb.append("?A, ?B");
      sb.append(")");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getPerfQTRETLString () {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT ");
      sb.append("PERF_QTR AS A, PERF_QTR_DESC AS B");
      sb.append(" FROM ");
      sb.append("PERF_QTR");
      sb.append(" ORDER BY ");
      sb.append("PERF_QTR");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ");
      sb.append("PERF_QTR (");
      sb.append("PERF_QTR, PERF_QTR_DESC");
      sb.append(") VALUES (");
      sb.append("?A, ?B");
      sb.append(")");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getPerfYRETLString () {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT ");
      sb.append("PERF_YR AS A, PERF_YR_DESC AS B");
      sb.append(" FROM ");
      sb.append("PERF_YR");
      sb.append(" ORDER BY ");
      sb.append("PERF_YR");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ");
      sb.append("PERF_YR (");
      sb.append("PERF_YR, PERF_YR_DESC");
      sb.append(") VALUES (");
      sb.append("?A, ?B");
      sb.append(")");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getProductETLString () {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT ");
      sb.append("CARD_TYPE_CD AS A, CARD_TYPE_CD_DESC AS B");
      sb.append(" FROM ");
      sb.append("CARD_TYPE");
      sb.append(" ORDER BY ");
      sb.append("CARD_TYPE_CD");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ");
      sb.append("CARD_TYPE (");
      sb.append("CARD_TYPE_CD, CARD_TYPE_CD_DESC");
      sb.append(") VALUES (");
      sb.append("?A, ?B");
      sb.append(")");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getAccountStatusETLString () {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT ");
      sb.append("EXT_STATUS AS A, EXT_STATUS_DESC AS B");
      sb.append(" FROM ");
      sb.append("EXT_STATUS");
      sb.append(" ORDER BY ");
      sb.append("EXT_STATUS");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ");
      sb.append("EXT_STATUS (");
      sb.append("EXT_STATUS, EXT_STATUS_DESC");
      sb.append(") VALUES (");
      sb.append("?A, ?B");
      sb.append(")");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getPortSegETLString () {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT ");
      sb.append("PORT_SEG AS A, PORT_SEG_DESC AS B, PORT_SEG_GRP AS C");
      sb.append(" FROM ");
      sb.append("PORT_SEG");
      sb.append(" ORDER BY ");
      sb.append("PORT_SEG");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ");
      sb.append("PORT_SEG (");
      sb.append("PORT_SEG, PORT_SEG_DESC, PORT_SEG_GRP");
      sb.append(") VALUES (");
      sb.append("?A, ?B, ?C");
      sb.append(")");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getPortSegGroupETLString () {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT ");
      sb.append("PORT_SEG_GRP AS A, PORT_SEG_GRP_DESC AS B");
      sb.append(" FROM ");
      sb.append("PORT_SEG_GRP");
      sb.append(" ORDER BY ");
      sb.append("PORT_SEG_GRP");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ");
      sb.append("PORT_SEG_GRP (");
      sb.append("PORT_SEG_GRP, PORT_SEG_GRP_DESC");
      sb.append(") VALUES (");
      sb.append("?A, ?B");
      sb.append(")");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getUsageSegETLString () {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT ");
      sb.append("USAGE_SEG AS A, USAGE_SEG_DESC AS B, USAGE_SEG_GRP AS C");
      sb.append(" FROM ");
      sb.append("USAGE_SEG");
      sb.append(" ORDER BY ");
      sb.append("USAGE_SEG");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ");
      sb.append("USAGE_SEG (");
      sb.append("USAGE_SEG, USAGE_SEG_DESC, USAGE_SEG_GRP");
      sb.append(") VALUES (");
      sb.append("?A, ?B, ?C");
      sb.append(")");
      sb.append("</script></query></etl>");
      return sb.toString();
   }

   private String getUsageSegGroupETLString () {
      StringBuilder sb = new StringBuilder("<etl><description>etl.xml file for ETL generation</description>");
      sb.append(getSourceConnectionString());
      sb.append(getTargetConnectionString());
      sb.append("<query connection-id=\"source\">");
      sb.append("SELECT ");
      sb.append("USAGE_SEG_GRP AS A, USAGE_SEG_GRP_DESC AS B");
      sb.append(" FROM ");
      sb.append("USAGE_SEG_GRP");
      sb.append(" ORDER BY ");
      sb.append("USAGE_SEG_GRP");
      sb.append("<script connection-id=\"target\">");
      sb.append("INSERT INTO ");
      sb.append("USAGE_SEG_GRP (");
      sb.append("USAGE_SEG_GRP, USAGE_SEG_GRP_DESC");
      sb.append(") VALUES (");
      sb.append("?A, ?B");
      sb.append(")");
      sb.append("</script></query></etl>");
      return sb.toString();
   }
}
