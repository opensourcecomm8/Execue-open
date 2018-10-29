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


package com.execue.core.common.bean.entity;

public class Path implements java.io.Serializable, Comparable<Path> {

   private Long                   id;

   private PathDefinition         pathDefinition;
   private EntityTripleDefinition entityTripleDefinition;
   private int                    entityTripleOrder;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public PathDefinition getPathDefinition () {
      return pathDefinition;
   }

   public void setPathDefinition (PathDefinition pathDefinition) {
      this.pathDefinition = pathDefinition;
   }

   public EntityTripleDefinition getEntityTripleDefinition () {
      return entityTripleDefinition;
   }

   public void setEntityTripleDefinition (EntityTripleDefinition entityTripleDefinition) {
      this.entityTripleDefinition = entityTripleDefinition;
   }

   public int getEntityTripleOrder () {
      return entityTripleOrder;
   }

   public void setEntityTripleOrder (int entityTripleOrder) {
      this.entityTripleOrder = entityTripleOrder;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   public int compareTo (Path path) {
      return entityTripleOrder - path.getEntityTripleOrder();
   }
}
