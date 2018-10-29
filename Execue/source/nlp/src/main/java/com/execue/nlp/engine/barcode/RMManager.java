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
// File Name : RMManager.java
// Date : 7/22/2008
// Author : Kaliki
//
//

package com.execue.nlp.engine.barcode;

import java.util.ArrayList;
import java.util.List;

import com.execue.nlp.bean.NLPToken;
import com.execue.nlp.bean.matrix.RootMatrix;
import com.execue.nlp.engine.barcode.matrix.MatrixUtility;

public class RMManager {

   private List<RootMatrix> RMRegistry;
   private int              rootMatrixCount;
   private static RMManager instance;

   protected void register (com.execue.nlp.bean.matrix.RootMatrix rootMatrix) {
      RMRegistry.add(rootMatrix);
      rootMatrixCount++;
   }

   private RMManager () {
      RMRegistry = new ArrayList<RootMatrix>();
   }

   public static RMManager getInstance () {
      if (instance == null) {
         instance = new RMManager();
      }
      return instance;
   }

   protected void unRegister (RootMatrix rootMatrix) {
      int size = RMRegistry.size();
      int index = -1;
      for (int i = 0; i < size; i++) {
         RootMatrix rMatrix = RMRegistry.get(i);
         if (rootMatrix == rMatrix) {
            index = i;
            break;
         }
      }
      RMRegistry.remove(index);

   }

   // create Root Matrix
   public RootMatrix createRootMatrix (List<NLPToken> nlpTokens) throws CloneNotSupportedException {
      RootMatrix rootMatrix = MatrixUtility.createBaseRootMatrix(nlpTokens);
      register(rootMatrix);
      rootMatrix.setRootMatrixId(rootMatrixCount);
      return rootMatrix;
   }

   public boolean destroyRootMatrix (RootMatrix rootMatrix) {
      boolean retFlag = true;
      rootMatrix = null;
      return retFlag;
   }
}
