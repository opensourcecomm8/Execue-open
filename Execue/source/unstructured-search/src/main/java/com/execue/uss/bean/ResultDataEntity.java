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


package com.execue.uss.bean;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.kb.DataEntity;
import com.execue.core.common.bean.qdata.UnStructuredIndex;

/**
 * @author Abhijit
 * @since Jul 7, 2009 : 4:19:15 PM
 */
public class ResultDataEntity extends DataEntity {
   private List<UnStructuredIndex> unstructuredInfoList;

   // Getter Setter

   public List<UnStructuredIndex> getUnstructuredInfoList() {
      return unstructuredInfoList;
   }

   public void setUnstructuredInfoList(List<UnStructuredIndex> unstructuredInfoList) {
      this.unstructuredInfoList = unstructuredInfoList;
   }

   // Utility Methods

   public void addUnstructuredInfo(UnStructuredIndex usi) {
      if(this.unstructuredInfoList == null) this.unstructuredInfoList = new ArrayList<UnStructuredIndex>();
      boolean add = true;
      for(UnStructuredIndex indx : this.unstructuredInfoList) {
         if(indx.getUrl().equalsIgnoreCase(usi.getUrl())) {
            add = false; break;
         }
      }
      if(add) this.unstructuredInfoList.add(usi);
   }

   public void addUnstructuredInfos(List<UnStructuredIndex> usiList) {
      for(UnStructuredIndex indx : usiList) {
         addUnstructuredInfo(indx);
      }
   }
}
