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
 * Created on Sep 24, 2008
 */
package com.execue.nlp.bean.snowflake;

import java.util.ArrayList;
import java.util.List;

import com.execue.nlp.bean.NLPToken;
import com.execue.nlp.engine.barcode.token.TokenUtility;

/**
 * @author kaliki
 */
public class SFLToken {

   private NLPToken      nLPToken;
   private List<Integer> positions;

   public NLPToken getNLPToken () {
      return nLPToken;
   }

   public void setNLPToken (NLPToken token) {
      nLPToken = token;
   }

   public String getWord () {
      return TokenUtility.getCurrentWordValue(this.nLPToken);
   }

   public List<String> getPossibleWords () {
      return TokenUtility.getAllPossibleWordValues(this.nLPToken);
   }

   public String getNLPTag () {
      return nLPToken.getDefaultRecognitionEntity().getNLPtag();
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
}
