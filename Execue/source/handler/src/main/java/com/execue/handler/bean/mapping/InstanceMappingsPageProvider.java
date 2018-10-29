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

import org.apache.log4j.Logger;

import com.execue.core.common.util.MappingEntityType;

public class InstanceMappingsPageProvider {

   private static final Logger log = Logger.getLogger(InstanceMappingsPageProvider.class);

   private String                     mappingType;
   private int                        currentPageNo = 1;
   private int                        pageSize;
   private int                        totalPages;
   private Long                       counter;
   private String                     filterByMapping;  // Suggested, Existing
   private List<InstanceAssetMapping> instanceMappings;

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

   /*
    * Delete the unselected mapping on the UI from the provider
    */
   public void deleteInstanceMapping (InstanceAssetMapping instanceAssetMapping) {
      List<InstanceAssetMapping> delInsList = new ArrayList<InstanceAssetMapping>();
      if (instanceMappings != null && instanceMappings.size() > 0) {
         for (InstanceAssetMapping iaMapping : instanceMappings) {
            if (iaMapping.getAedId() == instanceAssetMapping.getAedId()
                     && iaMapping.getMappingType().equals(instanceAssetMapping.getMappingType())) {
               delInsList.add(iaMapping);
            }
         }
         if (delInsList.size() > 0) {
            instanceMappings.removeAll(delInsList);
         }
      }
   }

   public InstanceAssetMapping getInstanceMappingByAEDId (Long aedId) {
      InstanceAssetMapping match = null;
      if (instanceMappings != null && instanceMappings.size() > 0) {
         for (InstanceAssetMapping iaMapping : instanceMappings) {
            if (iaMapping.getAedId() == aedId) {
               match = iaMapping;
               break;
            }
         }
      }
      return match;
   }

   public List<InstanceAssetMapping> getInstanceMappingsByBEDid (Long bedId) {
      List<InstanceAssetMapping> mappings = null;
      if (instanceMappings != null && instanceMappings.size() > 0) {
         for (InstanceAssetMapping iaMapping : instanceMappings) {
            if (MappingEntityType.EXISTING.equals(iaMapping.getMappingType()) && iaMapping.getBedId() == bedId) {
               if (mappings == null) {
                  mappings = new ArrayList<InstanceAssetMapping>();
               }
               mappings.add(iaMapping);
            }
         }
      }
      return mappings;
   }

   public List<InstanceAssetMapping> getPage (int currentPageNo) {
      this.currentPageNo = currentPageNo;
      List<InstanceAssetMapping> pageMappings = null;
      List<InstanceAssetMapping> filterList = paginate();
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
   private List<InstanceAssetMapping> paginate () {
      List<InstanceAssetMapping> filterList = new ArrayList<InstanceAssetMapping>(instanceMappings);

      filterListByMapping(filterList);

      calculateTotalPages(filterList.size());

      return filterList;
   }

   private void filterListByMapping (List<InstanceAssetMapping> filterList) {
      if (filterByMapping == null) {
         return;
      }
      List<InstanceAssetMapping> removeList = new ArrayList<InstanceAssetMapping>();
      for (InstanceAssetMapping InstanceAssetMapping : filterList) {
         if (!InstanceAssetMapping.getMappingType().equals(filterByMapping)) {
            removeList.add(InstanceAssetMapping);
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
      if (log.isDebugEnabled()) {
         log.debug("InstanceMappingsPageProvider - Page Size : " + pageSize);
      }
      int totalPages = totalMappings / pageSize;
      int remainder = totalMappings % pageSize;
      if (remainder > 0) {
         totalPages++;
      }
      setTotalPages(totalPages);
   }

   public void setInstanceMappings (List<InstanceAssetMapping> mappings) {
      if (instanceMappings == null) {
         this.instanceMappings = mappings;
      } else {
         if (mappings == null) {
            this.instanceMappings = null;
         } else {
            instanceMappings.clear();
            for (InstanceAssetMapping iaMapping : mappings) {
               addInstanceMapping(iaMapping);
            }
         }
      }
   }

   public void addInstanceMapping (InstanceAssetMapping iaMapping) {
      boolean absent = true;
      if (instanceMappings == null) {
         instanceMappings = new ArrayList<InstanceAssetMapping>();
      }
      for (InstanceAssetMapping iam : instanceMappings) {
         if (iaMapping.getAedId() == iam.getAedId() && iaMapping.getMappingType().equals(iam.getMappingType())) {
            absent = false;
            break;
         }
      }
      if (absent) {
         instanceMappings.add(0, iaMapping);
      }
   }

   /**
    * This method should not be used anymore, instead use
    * <code>getInstanceMappings (String colDispName, String tblDispName)</code>
    * 
    * @deprecated
    */
   public List<InstanceAssetMapping> getInstanceMappings () {
      return instanceMappings;
   }

   /**
    * This method returns the member-instance mappings for a column
    * 
    * @param colDispName
    * @param tblDispName
    * @return List<InstanceAssetMapping>
    */
   public List<InstanceAssetMapping> getInstanceMappings (String colDispName, String tblDispName) {
      List<InstanceAssetMapping> selInstanceMappings = new ArrayList<InstanceAssetMapping>();
      if (instanceMappings != null && colDispName != null && tblDispName != null) {
         for (InstanceAssetMapping iam : instanceMappings) {
            if (colDispName.equalsIgnoreCase(iam.getColDispName()) && tblDispName.equals(iam.getTblDispName())) {
               selInstanceMappings.add(iam);
            }
         }
      }
      return selInstanceMappings;
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
}
