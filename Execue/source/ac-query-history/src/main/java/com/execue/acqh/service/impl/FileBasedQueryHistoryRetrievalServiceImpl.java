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


package com.execue.acqh.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.execue.acqh.bean.QueryEntityInformation;
import com.execue.acqh.bean.QueryInformation;
import com.execue.acqh.bean.ThirdPartyQueryHistoryInfo;
import com.execue.acqh.configuration.IAnswersCatalogQueryHistoryConfigurationService;
import com.execue.acqh.exception.AnswersCatalogQueryHistoryException;
import com.execue.acqh.exception.AnswersCatalogQueryHistoryExceptionCodes;
import com.execue.acqh.service.ThirdPartyQueryHistoryRetrievalService;

public class FileBasedQueryHistoryRetrievalServiceImpl extends ThirdPartyQueryHistoryRetrievalService {

   private IAnswersCatalogQueryHistoryConfigurationService answersCatalogQueryHistoryConfigurationService;

   @Override
   public ThirdPartyQueryHistoryInfo populateThirdPartyQueryHistoryInfo () throws AnswersCatalogQueryHistoryException {
      ThirdPartyQueryHistoryInfo thirdPartyQueryHistoryInfo = new ThirdPartyQueryHistoryInfo();
      try {
         thirdPartyQueryHistoryInfo
                  .setQueryEntityInformations(populateQueryEntityInformation(getAnswersCatalogQueryHistoryConfigurationService()
                           .getQueryHistoryFileStorageBedDetailsPath()));
         thirdPartyQueryHistoryInfo
                  .setQueryInformations(populateQueryInformation(getAnswersCatalogQueryHistoryConfigurationService()
                           .getQueryHistoryFileStoragePastUsagePath()));
      } catch (IOException e) {
         throw new AnswersCatalogQueryHistoryException(AnswersCatalogQueryHistoryExceptionCodes.DEFAULT_EXCEPTION_CODE,
                  e);
      }
      return thirdPartyQueryHistoryInfo;
   }

   private List<QueryEntityInformation> populateQueryEntityInformation (String filePath) throws IOException {
      List<QueryEntityInformation> queryInformations = new ArrayList<QueryEntityInformation>();
      BufferedReader inStream = null;
      try {
         inStream = new BufferedReader(new FileReader(new File(filePath)));
         String line = null;
         List<String> tempStrings = null;
         while ((line = inStream.readLine()) != null) {
            QueryEntityInformation queryEntityInformation = new QueryEntityInformation();
            line = line.trim();
            tempStrings = null;
            if (!StringUtils.isBlank(line)) {
               tempStrings = java.util.Arrays.asList(line.split("#\\*#"));
               queryEntityInformation.setName(tempStrings.get(0));
               queryEntityInformation.setBedId(tempStrings.get(1));
               queryEntityInformation.setKdxType(tempStrings.get(2));
               String variation = tempStrings.get(3);
               String[] variations = variation.split(",");
               Set<String> variationSet = new TreeSet<String>();
               for (int i = 0; i < variations.length; i++) {
                  String name = variations[i].trim();
                  variationSet.add(name);
               }
               if (tempStrings.size() > 4) {
                  variation = tempStrings.get(4);
                  variations = variation.split(",");
                  for (int i = 0; i < variations.length; i++) {
                     String name = variations[i].trim();
                     variationSet.add(name);
                  }
               }
               queryEntityInformation.setVariations(variationSet);
               queryInformations.add(queryEntityInformation);
            }
         }
      } finally {
         if (inStream != null) {
            inStream.close();
         }
      }
      return queryInformations;
   }

   private List<QueryInformation> populateQueryInformation (String filePath) throws AnswersCatalogQueryHistoryException {
      BufferedReader inStream = null;
      List<QueryInformation> queryRecords = new ArrayList<QueryInformation>();
      try {
         inStream = new BufferedReader(new FileReader(new File(filePath)));

         String line = null;
         List<String> tempStrings = null;
         while ((line = inStream.readLine()) != null) {
            QueryInformation queryRecord = new QueryInformation();
            line = line.trim();
            tempStrings = null;
            if (!StringUtils.isBlank(line)) {
               tempStrings = java.util.Arrays.asList(line.split("#\\*#"));
               queryRecord.setSelect(getSelectEntities(tempStrings.get(1)));
               queryRecord.setGroupBy(getGroupByEntities(tempStrings.get(2)));
               queryRecord.setWhere(getWhereEntities(tempStrings.get(3)));
               queryRecord.setUsageCount(Long.parseLong(tempStrings.get(4)));
               queryRecords.add(queryRecord);
               // System.out.println("tempString:::::" + tempStrings.toString());
            }
         }
      } catch (NumberFormatException e) {
         throw new AnswersCatalogQueryHistoryException(AnswersCatalogQueryHistoryExceptionCodes.DEFAULT_EXCEPTION_CODE,
                  e);
      } catch (IOException e) {
         throw new AnswersCatalogQueryHistoryException(AnswersCatalogQueryHistoryExceptionCodes.DEFAULT_EXCEPTION_CODE,
                  e);
      } finally {
         if (inStream != null) {
            try {
               inStream.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      return queryRecords;
   }

   private Set<String> getSelectEntities (String record) {
      Set<String> businessEntities = new HashSet<String>();
      String[] selectStrings = record.split(",");
      for (int i = 0; i < selectStrings.length; i++) {
         businessEntities.add(selectStrings[i].trim());
      }
      return businessEntities;
   }

   private Set<String> getWhereEntities (String record) {
      Set<String> businessEntities = new HashSet<String>();
      String[] whereStrs = record.split(",");
      for (int i = 0; i < whereStrs.length; i++) {
         String[] dimensions = whereStrs[i].split("=");
         businessEntities.add(dimensions[0].trim());
      }
      return businessEntities;
   }

   private Set<String> getGroupByEntities (String record) {
      return getSelectEntities(record);
   }

   public IAnswersCatalogQueryHistoryConfigurationService getAnswersCatalogQueryHistoryConfigurationService () {
      return answersCatalogQueryHistoryConfigurationService;
   }

   public void setAnswersCatalogQueryHistoryConfigurationService (
            IAnswersCatalogQueryHistoryConfigurationService answersCatalogQueryHistoryConfigurationService) {
      this.answersCatalogQueryHistoryConfigurationService = answersCatalogQueryHistoryConfigurationService;
   }
}