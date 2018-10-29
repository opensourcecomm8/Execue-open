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


package com.execue.web.core.action.swi.mappings;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.util.MappingEntityType;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.bean.mapping.AssetColumn;
import com.execue.handler.bean.mapping.AssetTable;
import com.execue.handler.bean.mapping.ConceptAssetMapping;
import com.execue.handler.bean.mapping.ConceptMapping;
import com.execue.handler.bean.mapping.InstanceMappingsPageProvider;
import com.execue.handler.bean.mapping.MappingHeader;
import com.execue.handler.bean.mapping.MappingsPageProvider;
import com.execue.handler.bean.mapping.SaveMapping;
import com.execue.web.core.action.swi.SWIAction;

@SuppressWarnings ("unchecked")
public class ConceptMappingsAction extends SWIAction {

   private Logger                       logger                = Logger.getLogger(ConceptMappingsAction.class);
   // this has a list of all mappings
   private ConceptMapping               conceptMapping;
   // mapping data to be persisted from the UI
   private List<SaveMapping>            saveMappings;
   private int                          pageNo                = 1;
   private Long                         assetId;
   // the columns for which mappings to be suggested
   private List<Long>                   selColAedIds;
   // filter on the suggested list to pick up an existing concept
   private List<Long>                   selConBedIds;
   // to render the asset table info on the UI
   private List<AssetTable>             assetTables;
   // common cache with all concept mappings; Will be updated on a save mappings
   private MappingsPageProvider         provider;
   // common cache with all instance mappings; Will be updated on a save mappings
   private InstanceMappingsPageProvider instanceMappingsProvider;
   private static final String          PROVIDER              = "PROVIDER";
   private static final String          INSTANCES_PROVIDER    = "INSTANCES_PROVIDER";
   // holds the type of mappings which should be displayed
   private String                       mappingFilter;
   private String                       sourceName            = MAPPING;
   private List<Asset>                  assets;
   private Asset                        asset;
   // id to be used for initializing the BED of concept suggestion & instance suggestion
   private static final Long            CONCEPT_BED_START_ID  = -11111L;
   private static final Long            INSTANCE_BED_START_ID = -999999L;

   public String initializeMappings () {
      // TODO:- JT- Tried to get assetId from app context, will revisit and test it.
      try {
         assetId = getApplicationContext().getAssetId();
         assets = getSdxServiceHandler().getAllAssets(getApplicationContext().getAppId());
         if (getHttpSession().containsKey(PROVIDER)) {
            getHttpSession().remove(PROVIDER);
         }
         if (assetId != null) {
            asset = getSdxServiceHandler().getAsset(assetId);
         }
      } catch (ExeCueException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return SUCCESS;
   }

   private void init () {
      // Get the page size from the configuration
      int pageSize = getMappingServiceHandler().getMappingPageSize();
      // Set the concept and instance provider objects
      if (!getHttpSession().containsKey(PROVIDER)) {
         provider = new MappingsPageProvider();
         provider.setCounter(CONCEPT_BED_START_ID);
         provider.setPageSize(pageSize);
         getHttpSession().put(PROVIDER, provider);
      } else {
         provider = (MappingsPageProvider) getHttpSession().get(PROVIDER);
      }
      if (!getHttpSession().containsKey(INSTANCES_PROVIDER)) {
         instanceMappingsProvider = new InstanceMappingsPageProvider();
         instanceMappingsProvider.setCounter(INSTANCE_BED_START_ID);
         instanceMappingsProvider.setPageSize(pageSize);
         getHttpSession().put(INSTANCES_PROVIDER, instanceMappingsProvider);
      } else {
         instanceMappingsProvider = (InstanceMappingsPageProvider) getHttpSession().get(INSTANCES_PROVIDER);
      }
   }

   /**
    * This method displays the concept mappings based on the type of mappings
    */
   public String displayConceptMappings () {
      try {
         init();
         if (mappingFilter != null) {
            if (!(mappingFilter.equals(MappingEntityType.SUGGESTED) || mappingFilter.equals(MappingEntityType.EXISTING))) {
               // the 'Show All' option is handled here
               mappingFilter = null;
            }
         }
         // set the filter by mapping type
         provider.setFilterByMapping(mappingFilter);
         paginate();
      } catch (Exception e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String suggestConceptMappings () {
      try {
         init();
         // check if the request is for selected columns or for all the columns
         if (selColAedIds == null) {
            selColAedIds = new ArrayList<Long>();
            // Equivalent to setting the selColAedIds list with the list of aed ids of all the columns
            if (assetTables == null) {
               assetTables = getMappingServiceHandler().getAssetTables(getAssetId());
            }
            for (AssetTable table : assetTables) {
               for (AssetColumn col : table.getCols()) {
                  selColAedIds.add(col.getAedId());
               }
            }
         }
         logger.debug(getSelColAedIds());
         List<Long> suggestAedIds = new ArrayList<Long>();
         for (Long colAedId : selColAedIds) {
            ConceptAssetMapping mapping = provider.getConceptMappingByAEDId(colAedId);
            if (mapping == null) {
               suggestAedIds.add(colAedId);
            }
         }
         // if the suggest list has elements then trigger the suggestion process
         if (suggestAedIds.size() > 0) {
            conceptMapping = getMappingServiceHandler().suggestConceptMappingsForSelectedColumns(
                     getApplicationContext().getModelId(), suggestAedIds, selConBedIds, provider);
         } else {
            logger.debug("Found all the mappings in the provider object...");
            conceptMapping = new ConceptMapping();
            conceptMapping.setMsgType("W");
            conceptMapping.setMsg("Unable to suggest since the selected columns have been mapped");
         }
         paginate();
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   /*
    * This will show a list of existing mappings in SWI for the asset
    */
   public String showExistingConceptMappings () {
      List<ConceptAssetMapping> mappings = new ArrayList<ConceptAssetMapping>();
      conceptMapping = new ConceptMapping();
      try {
         init();
         // provider has a mix of confirmed and suggested mappings
         // get the confirmed/existing ones
         mappings = provider.getConfirmedConceptMappings();
         if (mappings.size() == 0) {
            // provider is not loaded yet
            mappings = getMappingServiceHandler().showExistingConceptMappings(assetId);
            if (mappings != null && mappings.size() <= 0) {
               // no mappings yet in SWI for the asset in question
               conceptMapping.setMsgType("W");
               conceptMapping.setMsg(getText("execue.mappings.noExistingMappings"));
            }
            provider.setConceptMappings(mappings);
         }
         // Reset the messages for each mapping while displaying the existing mappings
         resetMessages(mappings);

         // 
         paginate();
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String showPage () {
      init();
      paginate();
      return SUCCESS;
   }

   // private void paginate (List<Long> aedIdFilter, List<Long> dedIdFilter, String mappingTypeFilter) {
   // provider.setFilterByAedId(aedIdFilter);
   // provider.setFilterByDedId(dedIdFilter);
   // provider.setFilterByMapping(mappingTypeFilter);
   // paginate();
   // }

   private void paginate () {
      if (conceptMapping == null) {
         conceptMapping = new ConceptMapping();
      }
      List<ConceptAssetMapping> mappings = provider.getPage(pageNo);
      if (mappings == null || mappings.size() <= 0) {
         conceptMapping.setMappings(new ArrayList<ConceptAssetMapping>());
      } else {
         conceptMapping.setMappings(mappings);
      }
      MappingHeader header = new MappingHeader();
      header.setCurrentPage(provider.getCurrentPageNo());
      header.setTotalPages(provider.getTotalPages());
      conceptMapping.setHeader(header);
   }

   private void resetMessages (List<ConceptAssetMapping> caMappings) {
      for (ConceptAssetMapping cam : caMappings) {
         cam.setMsgType(null);
         cam.setMsg(null);
      }
   }

   public String storeConceptMappings () {
      String msg = getText("execue.mapping.confirm.success");
      String msgType = "S";
      boolean errorFlag = false;
      // filter the mappings that are already
      // get saveMapping
      // populate nextPage ConceptMapping
      try {
         init();
         // save the mappings
         conceptMapping = getMappingServiceHandler().saveConceptMappings(getApplicationContext().getModelId(),
                  saveMappings, provider, instanceMappingsProvider);
         // update the conceptMapping object with the apt message
         for (ConceptAssetMapping caMapping : conceptMapping.getMappings()) {
            if ("E".equals(caMapping.getMsgType())) {
               errorFlag = true;
               break;
            }
         }
         if (errorFlag) {
            msg = "Error occurred while saving the mappings";
            msgType = "E";
         }
         conceptMapping.setMsg(msg);
         conceptMapping.setMsgType(msgType);
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String retrieveAssetTables () {
      try {
         assetTables = getMappingServiceHandler().getAssetTables(getAssetId());
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String deleteMapping () {
      return SUCCESS;
   }

   public ConceptMapping getConceptMapping () {
      return conceptMapping;
   }

   public void setConceptMapping (ConceptMapping conceptMapping) {
      this.conceptMapping = conceptMapping;
   }

   public List<SaveMapping> getSaveMappings () {
      return saveMappings;
   }

   public void setSaveMappings (List<SaveMapping> saveMappings) {
      this.saveMappings = saveMappings;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public int getPageNo () {
      return pageNo;
   }

   public void setPageNo (int pageNo) {
      this.pageNo = pageNo;
   }

   public void setAssetTables (List<AssetTable> assetTables) {
      this.assetTables = assetTables;
   }

   public List<AssetTable> getAssetTables () {
      return assetTables;
   }

   public List<Long> getSelColAedIds () {
      return selColAedIds;
   }

   public void setSelColAedIds (List<Long> selColAedIds) {
      this.selColAedIds = selColAedIds;
   }

   public List<Long> getSelConBedIds () {
      return selConBedIds;
   }

   public void setSelConBedIds (List<Long> selConBedIds) {
      this.selConBedIds = selConBedIds;
   }

   public InstanceMappingsPageProvider getInstanceMappingsProvider () {
      return instanceMappingsProvider;
   }

   public void setInstanceMappingsProvider (InstanceMappingsPageProvider instanceMappingsProvider) {
      this.instanceMappingsProvider = instanceMappingsProvider;
   }

   public String getMappingFilter () {
      return mappingFilter;
   }

   public void setMappingFilter (String mappingFilter) {
      this.mappingFilter = mappingFilter;
   }

   /**
    * @return the assets
    */
   public List<Asset> getAssets () {
      return assets;
   }

   /**
    * @param assets
    *           the assets to set
    */
   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   /**
    * @return the sourceName
    */
   public String getSourceName () {
      return sourceName;
   }

   /**
    * @param sourceName
    *           the sourceName to set
    */
   public void setSourceName (String sourceName) {
      this.sourceName = sourceName;
   }

   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

}