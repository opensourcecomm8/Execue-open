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


/**
 *
 */
package com.execue.nlp.bean.entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Nihar
 *
 */
public class SFLEntityComaparator implements Comparator<RecognitionEntity> {

   /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
   public int compare (RecognitionEntity sflEntity1, RecognitionEntity sflEntity2) {
      List<Integer> tokenPositions1 = sflEntity1.getReferedTokenPositions();
      List<Integer> tokenPositions2 = sflEntity2.getReferedTokenPositions();
      Collections.sort(tokenPositions1);
      Collections.sort(tokenPositions2);
      int maxTokenPos1 = tokenPositions1.get(tokenPositions1.size() - 1);
      int maxTokenPos2 = tokenPositions2.get(tokenPositions2.size() - 1);
      if (maxTokenPos1 == maxTokenPos2) {
         return 0;
      }
      return maxTokenPos1 < maxTokenPos2 ? -1 : 1;

   }

}
