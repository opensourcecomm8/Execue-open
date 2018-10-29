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


package com.execue.web.core.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.exception.ExeCueException;
import com.execue.swi.exception.SDXException;
import com.execue.web.core.action.swi.SWIAction;

public class AppInfoAction extends SWIAction {

   private Long                applicationId;
   private List<Asset>         assets;
   private static final Logger log = Logger.getLogger(AppInfoAction.class);

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   public String showAppInfo () {
      try {
         assets = getSdxServiceHandler().getAllAssets(applicationId);
         System.out.println("asset size ::" + assets.size());
      } catch (SDXException sdxException) {
         log.error(sdxException, sdxException);
         sdxException.printStackTrace();
         return ERROR;
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         exeCueException.printStackTrace();
         return ERROR;
      }
      return SUCCESS;
   }

}
