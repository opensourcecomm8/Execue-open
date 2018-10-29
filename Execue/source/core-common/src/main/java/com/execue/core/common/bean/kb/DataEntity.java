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

import com.execue.core.common.type.ConnectionEndPointType;

/**
 * @author Abhijit
 * @since Jul 6, 2009 : 10:24:56 PM
 */
public class DataEntity {
   private Long id;
   private String name;
   private ConnectionEndPointType type;
   private Map<Long, DataConnectionEntity> connections;

   // Getter Setter

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Map<Long, DataConnectionEntity> getConnections() {
      return connections;
   }

   public void setConnections(Map<Long, DataConnectionEntity> connections) {
      this.connections = connections;
   }

   public ConnectionEndPointType getType() {
      return type;
   }

   public void setType(ConnectionEndPointType type) {
      this.type = type;
   }

   // Utility Methods

   public void addConnection(DataConnectionEntity connection) {
      if(this.connections == null) this.connections = new HashMap<Long, DataConnectionEntity>();
      this.connections.put(connection.getId(), connection);
   }

   public DataConnectionEntity getConnection(long connectionID) {
      if(this.connections != null) return this.connections.get(connectionID);
      return null;
   }

   public boolean hasConnection(long connectionID) {
      return this.connections != null && this.connections.containsKey(connectionID);
   }
}
