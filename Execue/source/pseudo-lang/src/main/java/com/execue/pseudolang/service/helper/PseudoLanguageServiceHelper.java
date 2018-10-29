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


package com.execue.pseudolang.service.helper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.IBusinessEntity;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.pseudolang.NormalizedPseudoQuery;
import com.execue.core.common.bean.pseudolang.PseudoEntity;
import com.execue.core.common.bean.pseudolang.PseudoStat;
import com.execue.core.common.bean.reports.prsntion.Scale;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.util.ExeCueXMLUtils;
import com.execue.core.util.ExecueCoreUtil;

/**
 * @author John Mallavalli
 * @since 4.0
 * @version 1.0
 */
public class PseudoLanguageServiceHelper {

   private static final String SPACE             = " ";
   private static final String COMMA             = ",";
   // private static final String OF = "of";
   private static final String OF_WITH_SPACES    = " of ";
   private static final String COHORT_IDENTIFIER = "(*)";

   public static PseudoEntity createPseudoEntity (BusinessTerm businessTerm) {
      PseudoEntity pseudoEntity = new PseudoEntity();
      pseudoEntity.setEntityDescription(getDomainEntityName(businessTerm));
      pseudoEntity.setFromUser(businessTerm.isRequestedByUser());
      pseudoEntity.setId(getDomainEntityId(businessTerm));
      pseudoEntity.setFromCohort(businessTerm.isFromCohort());
      // add profile information
      Long profileBedId = businessTerm.getProfileDomainEntityDefinitionId();
      if (profileBedId != null) {
         pseudoEntity.setPartOfProfile(true);
         pseudoEntity.setProfileBedId(profileBedId);
         pseudoEntity.setProfileName(businessTerm.getProfileName());
      }
      PseudoStat pseudoStat = getPseudoStat(businessTerm);
      if (pseudoStat != null) {
         List<PseudoStat> pseudoStats = new ArrayList<PseudoStat>();
         pseudoStats.add(pseudoStat);
         pseudoEntity.setStats(pseudoStats);
      }
      return pseudoEntity;
   }

   public static String getDomainEntityName (BusinessTerm businessTerm) {
      String entityName = "";
      BusinessEntityType businessEntityType = businessTerm.getBusinessEntityTerm().getBusinessEntityType();
      IBusinessEntity entity = businessTerm.getBusinessEntityTerm().getBusinessEntity();
      if (businessEntityType.equals(BusinessEntityType.CONCEPT)) {
         entityName = ((Concept) entity).getDisplayName();
      } else if (businessEntityType.equals(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE)) {
         entityName = ((Instance) entity).getDisplayName();
      }
      return entityName;
   }

   public static long getDomainEntityId (BusinessTerm businessTerm) {
      long entityId = -1;
      BusinessEntityType businessEntityType = businessTerm.getBusinessEntityTerm().getBusinessEntityType();
      IBusinessEntity entity = businessTerm.getBusinessEntityTerm().getBusinessEntity();
      if (businessEntityType.equals(BusinessEntityType.CONCEPT)) {
         entityId = ((Concept) entity).getId();
      } else if (businessEntityType.equals(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE)) {
         entityId = ((Instance) entity).getId();
      }
      return entityId;
   }

   public static PseudoStat getPseudoStat (BusinessTerm businessTerm) {
      PseudoStat pseudoStat = null;
      BusinessStat businessStat = businessTerm.getBusinessStat();
      if (businessStat != null && businessStat.getStat() != null) {
         pseudoStat = new PseudoStat();
         pseudoStat.setStatDescription(businessStat.getStat().getDisplayName());
         pseudoStat.setStatFromUser(businessStat.isRequestedByUser());
      }
      return pseudoStat;
   }

   public static PseudoEntity processPseudoEntity (BusinessTerm businessTerm,
            NormalizedPseudoQuery normalizedPseudoQuery) {
      Map<Long, PseudoEntity> pseudoEntities = normalizedPseudoQuery.getPseudoEntities();
      Long entityId = PseudoLanguageServiceHelper.getDomainEntityId(businessTerm);
      PseudoEntity pseudoEntity = null;
      // Create a pseudo entity only if the entities map does not contain it and after creation add the entity into the
      // map
      boolean isDuplicateTerm = false;
      // First check if the map contains the id
      if (pseudoEntities.containsKey(entityId)) {
         // get the stored entity and check for the cohort flag
         PseudoEntity storedEntity = pseudoEntities.get(entityId);
         if (storedEntity.isFromCohort() == businessTerm.isFromCohort()) {
            isDuplicateTerm = true;
         }
      }
      // if (!pseudoEntities.containsKey(entityId)) {
      if (!isDuplicateTerm) {
         pseudoEntity = PseudoLanguageServiceHelper.createPseudoEntity(businessTerm);
         pseudoEntities.put(entityId, pseudoEntity);
      } else {
         pseudoEntity = pseudoEntities.get(entityId);
         // Since there is already an entity, add only the statistic of the current BT
         PseudoStat pStat = PseudoLanguageServiceHelper.getPseudoStat(businessTerm);
         if (ExecueCoreUtil.isCollectionEmpty(pseudoEntity.getStats()) && pStat != null) {
            List<PseudoStat> stats = new ArrayList<PseudoStat>();
            stats.add(pStat);
            pseudoEntity.setStats(stats);
         }
         // TODO: -JVK- Do we need to add the stat if there is already a stat assigned to the BT?
         // if (pStat != null) {
         // TODO: -JVK- add this stat only if it is not present in the list
         // pseudoEntity.getStats().add(pStat);
         // }
      }
      return pseudoEntity;
   }

   public static String statementForMetrics (NormalizedPseudoQuery pseudoQuery) {
      StringBuffer sbMetricsStatement = new StringBuffer();
      if (pseudoQuery != null) {
         List<PseudoEntity> metrics = pseudoQuery.getMetrics();
         int index = 0;
         if (metrics != null) {
            boolean isProfilePresent = false;
            // check for the presence of profiles
            for (PseudoEntity entity : metrics) {
               if (entity.isPartOfProfile()) {
                  isProfilePresent = true;
                  break;
               }
            }
            if (isProfilePresent) {
               sbMetricsStatement.append(prepareStatementForProfiles(metrics));
            } else {
               for (PseudoEntity entity : metrics) {
                  if (index++ > 0) {
                     sbMetricsStatement.append(COMMA);
                     sbMetricsStatement.append(SPACE);
                  }
                  sbMetricsStatement.append(statementForEntity(entity));
               }
            }
         }
      }
      return sbMetricsStatement.toString();
   }

   public static String prepareTitleForProfiles (List<PseudoEntity> metrics) {
      StringBuilder sbMetricsStatement = new StringBuilder();
      int index = 0;
      // build a map of the profile ids and the list of the components
      Map<String, List<PseudoEntity>> profileMap = new HashMap<String, List<PseudoEntity>>();
      for (PseudoEntity entity : metrics) {
         if (entity.isPartOfProfile() && !entity.isFromCohort()) {
            List<PseudoEntity> profileEntities = null;
            if (profileMap.containsKey(entity.getProfileName())) {
               profileEntities = profileMap.get(entity.getProfileName());
               profileEntities.add(entity);
            } else {
               profileEntities = new ArrayList<PseudoEntity>();
               profileEntities.add(entity);
               profileMap.put(entity.getProfileName(), profileEntities);
            }
         }
      }
      // use the profile names to build the pseudo statement
      for (String profile : profileMap.keySet()) {
         if (index++ > 0) {
            sbMetricsStatement.append(COMMA);
            sbMetricsStatement.append(SPACE);
         }
         sbMetricsStatement.append(profile);
      }
      // check if there are any entities outside the profiles and add them to the statement
      index = 0;
      for (PseudoEntity entity : metrics) {
         if (!entity.isPartOfProfile() && !entity.isFromCohort()) {
            sbMetricsStatement.append(COMMA);
            sbMetricsStatement.append(SPACE);
            sbMetricsStatement.append(entity.getEntityDescription());
         }
      }
      return sbMetricsStatement.toString();
   }

   public static String prepareStatementForProfiles (List<PseudoEntity> metrics) {
      StringBuilder sbMetricsStatement = new StringBuilder();
      int index = 0;
      // build a map of the profile ids and the list of the components
      Map<String, List<PseudoEntity>> profileMap = new HashMap<String, List<PseudoEntity>>();
      for (PseudoEntity entity : metrics) {
         if (entity.isPartOfProfile()) {
            List<PseudoEntity> profileEntities = null;
            if (profileMap.containsKey(entity.getProfileName())) {
               profileEntities = profileMap.get(entity.getProfileName());
               profileEntities.add(entity);
            } else {
               profileEntities = new ArrayList<PseudoEntity>();
               profileEntities.add(entity);
               profileMap.put(entity.getProfileName(), profileEntities);
            }
         }
      }
      // use the profile names to build the pseudo statement
      for (String profile : profileMap.keySet()) {
         if (index++ > 0) {
            sbMetricsStatement.append(COMMA);
            sbMetricsStatement.append(SPACE);
         }
         sbMetricsStatement.append(profile);
      }
      // check if there are any entities outside the profiles and add them to the statement
      index = 0;
      for (PseudoEntity entity : metrics) {
         if (!entity.isPartOfProfile()) {
            sbMetricsStatement.append(COMMA);
            sbMetricsStatement.append(SPACE);
            sbMetricsStatement.append(statementForEntity(entity));
         }
      }
      return sbMetricsStatement.toString();
   }

   public static String statementForEntity (PseudoEntity entity) {
      StringBuffer sbMetric = new StringBuffer();
      List<PseudoStat> stats = entity.getStats();
      int index = 0;
      if (ExecueCoreUtil.isCollectionNotEmpty(stats)) {
         for (PseudoStat stat : stats) {
            if (index++ > 0) {
               sbMetric.append(COMMA).append(SPACE);
            }
            sbMetric.append(stat.getStatDescription());
         }
      }
      if (index > 0) {
         sbMetric.append(OF_WITH_SPACES);
      }
      sbMetric.append(entity.getEntityDescription());
      if (entity.isFromCohort()) {
         sbMetric.append(SPACE).append(COHORT_IDENTIFIER);
      }
      return sbMetric.toString();
   }

   public static String statementForSummarizations (NormalizedPseudoQuery pseudoQuery) {
      List<PseudoEntity> summarizations = pseudoQuery.getSummarizations();
      return statementForEntityList(summarizations);
   }

   public static String statementForOrderClauses (NormalizedPseudoQuery pseudoQuery) {
      List<PseudoEntity> oderClauses = pseudoQuery.getOrderClauses();
      return statementForEntityList(oderClauses);
   }

   private static String statementForEntityList (List<PseudoEntity> entityList) {
      StringBuffer sbEntityList = new StringBuffer();
      int index = 0;
      if (entityList != null) {
         for (PseudoEntity entity : entityList) {
            if (index++ > 0) {
               sbEntityList.append(COMMA).append(SPACE);
            }
            sbEntityList.append(entity.getEntityDescription());
         }
      }
      return sbEntityList.toString();
   }

   public static String statementForPopulation (NormalizedPseudoQuery pseudoQuery) {
      StringBuilder sbPopulationStatement = new StringBuilder();
      List<PseudoEntity> population = pseudoQuery.getPopulation();
      int index = 0;
      if (population != null) {
         for (PseudoEntity entity : population) {
            if (index++ > 0) {
               sbPopulationStatement.append(COMMA);
               sbPopulationStatement.append(SPACE);
            }
            sbPopulationStatement.append(entity.getEntityDescription());
         }
      }
      return sbPopulationStatement.toString();
   }

   /**
    * This method returns the formatted value for decimal numbers up to 2 decimal digits
    */
   public static String getFormattedValueForDecimals (String value) {
      String formattedString = value;
      // check if this is a number
      if (NumberUtils.isNumber(value)) {
         // check if the value has a decimal
         if (value.indexOf(".") != -1) {
            try {
               List<String> values = new ArrayList<String>();
               values.add(value);
               Scale scale = ExeCueXMLUtils.getScale(values);
               System.out.println(scale.getSuffix());
               // check for E
               if (!scale.getSuffix().contains("E")) {
                  NumberFormat numberFormat = new DecimalFormat("#,###.##");
                  numberFormat.setMaximumFractionDigits(2);
                  formattedString = numberFormat.format(Double.parseDouble(value));
               }
            } catch (Exception e) {
               // TODO: handle exception
            }
         }
      }
      return formattedString;
   }
}