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


/*
 * Created on Oct 3, 2008
 */
package com.execue.nlp.bean.snowflake;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kaliki
 */
public class SFLCluster {

   private int           id;
   private List<Integer> positions = new ArrayList<Integer>(); ;

   public int getId () {
      return id;
   }

   public void setId (int id) {
      this.id = id;
   }

   public List<Integer> getPositions () {
      return positions;
   }

   public void setPositions (List<Integer> positions) {
      this.positions = positions;
   }

   public void addPosition (int position) {
      positions.add(position);
   }

   public String toString () {
      StringBuffer sb = new StringBuffer();
      sb.append("Position List " + positions);
      return sb.toString();
   }
}
