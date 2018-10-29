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


//
// Project : Execue NLP
// File Name : Matrix.java
// Date : 7/22/2008
// Author : Kaliki
//
//

package com.execue.nlp.bean.matrix;

import java.util.ArrayList;
import java.util.List;

import com.execue.nlp.bean.NLPToken;

/**
 * This represents cell of the Root Matrix.
 * 
 * @author kaliki
 */

public class Matrix {

   private List<NLPToken> NLPTokens;

   public List<NLPToken> getNLPTokens () {
      return NLPTokens;
   }

   public void setNLPTokens (List<NLPToken> tokens) {
      NLPTokens = tokens;
   }

   public void addNLPToken (NLPToken token) {
      if (NLPTokens == null) {
         NLPTokens = new ArrayList<NLPToken>();
      }
      NLPTokens.add(token);
   }

   public NLPToken getNLPToken (int position) {
      if (position >= 0)
         return this.NLPTokens.get(position);
      else {
         for (NLPToken tok : this.NLPTokens) {
            if (tok.getRecognitionEntities().size() > 0 && tok.getDefaultRecognitionEntity().getPosition() == position) {
               return tok;
            }
         }
      }
      return null;
   }

   @Override
   protected Object clone () throws CloneNotSupportedException {
      Matrix matrixToBeCloned = this;
      Matrix clonedMatrix = new Matrix();
      List<NLPToken> clonedNLPTokens = new ArrayList<NLPToken>();
      for (NLPToken nlpToken : matrixToBeCloned.getNLPTokens()) {
         clonedNLPTokens.add((NLPToken) nlpToken.clone());
      }
      clonedMatrix.setNLPTokens(clonedNLPTokens);
      return clonedMatrix;
   }
}
