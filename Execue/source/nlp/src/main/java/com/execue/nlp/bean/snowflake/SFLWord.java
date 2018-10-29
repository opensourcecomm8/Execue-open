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
 * Created on Aug 20, 2008
 */
package com.execue.nlp.bean.snowflake;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.SFLTermToken;

/**
 * @author kaliki
 */
public class SFLWord {

   private SFLTermToken  sflTermToken;
   private List<Integer> positions;

   public SFLWord (SFLTermToken sflTermToken) {
      this.sflTermToken = sflTermToken;
   }

   public SFLTermToken getSflTermToken () {
      return sflTermToken;
   }

   public void setSfLTermToken (SFLTermToken sflTermToken) {
      this.sflTermToken = sflTermToken;
   }

   public List<Integer> getPositions () {
      return positions;
   }

   public void setPositions (List<Integer> positions) {
      this.positions = positions;
   }

   public void addPosition (int position) {
      if (positions == null) {
         positions = new ArrayList<Integer>();
      }

      if (!positions.contains(position)) {
         positions.add(position);
      }

   }

   public String toString () {
      StringBuffer sb = new StringBuffer();
      sb.append("Postions = " + positions);
      sb.append(" ; ");
      sb.append(sflTermToken.toString());
      return sb.toString();
   }
}
