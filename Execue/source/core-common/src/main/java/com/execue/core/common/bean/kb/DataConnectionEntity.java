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


package com.execue.core.common.bean.kb;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Abhijit
 * @since Jul 6, 2009 : 10:29:24 PM
 */
public class DataConnectionEntity {
   private long id;
   private String name;
   private Long entityPathID;
   private Map<Long, DataEntity> dataItemsByInstancePathID; //  Map of Instance Path Definition ID Against the Requested Data Entity ID

   // Getter Setters

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Map<Long, DataEntity> getDataItemsByInstancePathID() {
      return dataItemsByInstancePathID;
   }

   public void setDataItemsByInstancePathID(Map<Long, DataEntity> dataItemsByInstancePathID) {
      this.dataItemsByInstancePathID = dataItemsByInstancePathID;
   }

   public Long getEntityPathID() {
      return entityPathID;
   }

   public void setEntityPathID(Long entityPathID) {
      this.entityPathID = entityPathID;
   }

   // Utility Methods

   public void addDataItemForInstancePathID(long id, DataEntity dataItem) {
      if(this.dataItemsByInstancePathID == null) this.dataItemsByInstancePathID = new HashMap<Long, DataEntity>();
      this.dataItemsByInstancePathID.put(id, dataItem);
   }
}
