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


package com.execue.handler.bean.mapping;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.util.MappingEntityType;

public class MappingsPageProvider {

   private String                     mappingType;
   private int                        currentPageNo = 1;
   private int                        pageSize;
   private int                        totalPages;
   private Long                       counter;
   private String                     filterByMapping;         // Suggested, Existing
   private List<Long>                 filterByAedId;           // mappings filtered by list of selected AEDs
   private List<Long>                 filterByBedId;           // mappings filtered by list of selected BEDs
   private List<ConceptAssetMapping>  suggestedConceptMappings;
   private List<ConceptAssetMapping>  confirmedConceptMappings;
   private List<ConceptAssetMapping>  conceptMappings;         // Suggested, Confirmed, New
   private List<InstanceAssetMapping> instanceMappings;

   public List<InstanceAssetMapping> getInstanceMappings () {
      return instanceMappings;
   }

   public void setInstanceMappings (List<InstanceAssetMapping> instanceMappings) {
      this.instanceMappings = instanceMappings;
   }

   public String getFilterByMapping () {
      return filterByMapping;
   }

   public void setFilterByMapping (String filterByMapping) {
      this.filterByMapping = filterByMapping;
   }

   public Long getCounter () {
      return counter;
   }

   public void setCounter (Long counter) {
      this.counter = counter;
   }

   public List<Long> getFilterByAedId () {
      return filterByAedId;
   }

   public void setFilterByAedId (List<Long> filterByAedId) {
      this.filterByAedId = filterByAedId;
   }

   public List<Long> getFilterByBedId () {
      return filterByBedId;
   }

   public void setFilterByBedId (List<Long> filterByBedId) {
      this.filterByBedId = filterByBedId;
   }

   public String getMappingType () {
      return mappingType;
   }

   public void setMappingType (String mappingType) {
      this.mappingType = mappingType;
   }

   public int getCurrentPageNo () {
      return currentPageNo;
   }

   public void setCurrentPageNo (int currentPageNo) {
      this.currentPageNo = currentPageNo;
   }

   public int getPageSize () {
      return pageSize;
   }

   public void setPageSize (int pageSize) {
      this.pageSize = pageSize;
   }

   public int getTotalPages () {
      return totalPages;
   }

   public void setTotalPages (int totalPages) {
      this.totalPages = totalPages;
   }

   @SuppressWarnings ("unchecked")
   public void updateMappings (Mapping mapping) {
      BusinessEntityDefinition bed = mapping.getBusinessEntityDefinition();
      Instance instance = bed.getInstance();
      if (instance == null) {
         for (ConceptAssetMapping caMapping : conceptMappings) {
            String colName = mapping.getAssetEntityDefinition().getColum().getName();
            String tabName = mapping.getAssetEntityDefinition().getTabl().getName();
            if (caMapping.getTblDispName().equals(tabName) && caMapping.getColDispName().equals(colName)) {
               // caMapping.getBedId());
               caMapping.setConDispName(mapping.getBusinessEntityDefinition().getConcept().getDisplayName());
               caMapping.setBedId(mapping.getBusinessEntityDefinition().getId());
               // caMapping.getBedId());
               caMapping.setMappingType(MappingEntityType.EXISTING);
               // caMapping.setHasInstance(false);
            }
         }
      } else {
         for (InstanceAssetMapping iaMapping : instanceMappings) {
            String colName = mapping.getAssetEntityDefinition().getColum().getName();
            String memName = mapping.getAssetEntityDefinition().getMembr().getLookupDescription();
            String tabName = mapping.getAssetEntityDefinition().getTabl().getName();
            if (iaMapping.getTblDispName().equals(tabName) && iaMapping.getColDispName().equals(colName)
                     && iaMapping.getMemDispName().equals(memName)) {
               // iaMapping.getBedId());
               iaMapping.setInstanceDispName(mapping.getBusinessEntityDefinition().getInstance().getDisplayName());
               iaMapping.setBedId(mapping.getBusinessEntityDefinition().getId());
               // iaMapping.getBedId());
               iaMapping.setMappingType(MappingEntityType.EXISTING);
               // caMapping.setHasInstance(false);
            }
         }
      }
   }

   // TODO: JVK revisit - not required, delete later
   public void deleteMappingByAED (Mapping mapping) {
      AssetEntityDefinition aed = mapping.getAssetEntityDefinition();
      List<ConceptAssetMapping> delConList = new ArrayList<ConceptAssetMapping>();
      List<InstanceAssetMapping> delInsList = new ArrayList<InstanceAssetMapping>();
      for (ConceptAssetMapping caMapping : conceptMappings) {
         if (aed.getId() == caMapping.getAedId()) {
            delConList.add(caMapping);
         }
      }
      if (delConList.size() > 0) {
         conceptMappings.removeAll(delConList);
      } else {
         for (InstanceAssetMapping iaMapping : instanceMappings) {
            if (aed.getId() == iaMapping.getAedId()) {
               delInsList.add(iaMapping);
            }
         }
         conceptMappings.removeAll(delInsList);
      }
   }

   public void deleteConceptMapping (ConceptAssetMapping conceptAssetMapping) {
      List<ConceptAssetMapping> delConList = new ArrayList<ConceptAssetMapping>();
      for (ConceptAssetMapping caMapping : conceptMappings) {
         if (caMapping.getAedId() == conceptAssetMapping.getAedId()
                  && caMapping.getMappingType().equals(conceptAssetMapping.getMappingType())) {
            delConList.add(caMapping);
         }
      }
      if (delConList.size() > 0) {
         conceptMappings.removeAll(delConList);
      }
   }

   public ConceptAssetMapping getConceptMappingByAEDId (Long aedId) {
      ConceptAssetMapping match = null;
      for (ConceptAssetMapping caMapping : conceptMappings) {
         if (caMapping.getAedId() == aedId) {
            match = caMapping;
            break;
         }
      }
      return match;
   }

   public List<ConceptAssetMapping> getConceptMappingsByBEDid (Long bedId) {
      List<ConceptAssetMapping> mappings = null;
      for (ConceptAssetMapping caMapping : conceptMappings) {
         if (MappingEntityType.EXISTING.equals(caMapping.getMappingType()) && caMapping.getBedId() == bedId) {
            if (mappings == null) {
               mappings = new ArrayList<ConceptAssetMapping>();
            }
            mappings.add(caMapping);
         }
      }
      return mappings;
   }

   public List<ConceptAssetMapping> getPage (int currentPageNo) {
      this.currentPageNo = currentPageNo;
      List<ConceptAssetMapping> pageMappings = null;
      List<ConceptAssetMapping> filterList = paginate();
      // validate the page number
      int filterListSize = filterList.size();
      if (currentPageNo <= getTotalPages()) {
         int fromIndex = ((currentPageNo - 1) * getPageSize());
         int toIndex = currentPageNo * getPageSize();
         // validate the toIndex to be less than the total number of mappings
         if (toIndex > filterListSize) {
            toIndex = filterListSize;
         }
         pageMappings = filterList.subList(fromIndex, toIndex);
      }
      return pageMappings;
   }

   @SuppressWarnings ("unchecked")
   private List<ConceptAssetMapping> paginate () {
      List<ConceptAssetMapping> filterList = new ArrayList<ConceptAssetMapping>(conceptMappings);

      filterListByAedIds(filterList);
      filterListByBedIds(filterList);
      filterListByMapping(filterList);

      calculateTotalPages(filterList.size());

      return filterList;
   }

   private void filterListByAedIds (List<ConceptAssetMapping> filterList) {
      if (filterByAedId == null || filterByAedId.size() == 0) {
         return;
      }
      List<ConceptAssetMapping> removeList = new ArrayList<ConceptAssetMapping>();
      for (ConceptAssetMapping conceptAssetMapping : filterList) {
         if (!filterByAedId.contains(conceptAssetMapping.getAedId())) {
            removeList.add(conceptAssetMapping);
         }
      }
      // remove
      filterList.removeAll(removeList);
   }

   private void filterListByBedIds (List<ConceptAssetMapping> filterList) {
      if (filterByBedId == null || filterByBedId.size() == 0) {
         return;
      }
      List<ConceptAssetMapping> removeList = new ArrayList<ConceptAssetMapping>();
      for (ConceptAssetMapping conceptAssetMapping : filterList) {
         if (!filterByBedId.contains(conceptAssetMapping.getBedId())) {
            removeList.add(conceptAssetMapping);
         }
      }
      // remove
      filterList.removeAll(removeList);
   }

   private void filterListByMapping (List<ConceptAssetMapping> filterList) {
      if (filterByMapping == null) {
         return;
      }
      List<ConceptAssetMapping> removeList = new ArrayList<ConceptAssetMapping>();
      for (ConceptAssetMapping conceptAssetMapping : filterList) {
         if (!conceptAssetMapping.getMappingType().equals(filterByMapping)) {
            removeList.add(conceptAssetMapping);
         }
      }
      // remove
      filterList.removeAll(removeList);
   }

   private void calculateTotalPages (int totalMappings) {
      if (pageSize == 0) {
         // default it to 20
         pageSize = 20;
      }
      int totalPages = totalMappings / pageSize;
      int remainder = totalMappings % pageSize;
      if (remainder > 0) {
         totalPages++;
      }
      setTotalPages(totalPages);
   }

   public List<ConceptAssetMapping> getAllConceptMappings () {
      return conceptMappings;
   }

   public List<ConceptAssetMapping> getConfirmedConceptMappings () {
      processConceptMappings();
      return confirmedConceptMappings;
   }

   public List<ConceptAssetMapping> getSuggestedConceptMappings () {
      processConceptMappings();
      return suggestedConceptMappings;
   }

   private void processConceptMappings () {
      suggestedConceptMappings = new ArrayList<ConceptAssetMapping>();
      confirmedConceptMappings = new ArrayList<ConceptAssetMapping>();
      if (conceptMappings != null) {
         for (ConceptAssetMapping cam : conceptMappings) {
            if (cam.getMappingType().equals(MappingEntityType.SUGGESTED)) {
               suggestedConceptMappings.add(cam);
            } else if (cam.getMappingType().equals(MappingEntityType.EXISTING)) {
               confirmedConceptMappings.add(cam);
            }
         }
      }
   }

   public List<ConceptAssetMapping> getConceptMappings () {
      return conceptMappings;
   }

   public void setConceptMappings (List<ConceptAssetMapping> mappings) {
      if (conceptMappings == null) {
         this.conceptMappings = mappings;
      } else {
         for (ConceptAssetMapping caMapping : mappings) {
            addConceptMapping(caMapping);
         }
      }
   }

   public void addConceptMapping (ConceptAssetMapping caMapping) {
      boolean absent = true;
      if (conceptMappings == null) {
         conceptMappings = new ArrayList<ConceptAssetMapping>();
      }
      for (ConceptAssetMapping cam : conceptMappings) {
         if (caMapping.getAedId() == cam.getAedId() && caMapping.getMappingType().equals(cam.getMappingType())) {
            absent = false;
            break;
         }
      }
      if (absent) {
         conceptMappings.add(0, caMapping);
      }
   }
}