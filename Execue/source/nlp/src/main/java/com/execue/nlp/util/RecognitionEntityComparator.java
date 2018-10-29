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
package com.execue.nlp.util;

import java.util.Comparator;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;

/**
 * @author Nitesh
 */
public class RecognitionEntityComparator implements Comparator<IWeightedEntity> {

   /*
    * (non-Javadoc)
    * 
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
   public int compare (IWeightedEntity o1, IWeightedEntity o2) {
      if (o1 instanceof RecognitionEntity && o2 instanceof RecognitionEntity) {
         ReferedTokenPosition rtp1 = new ReferedTokenPosition(((RecognitionEntity) o1).getReferedTokenPositions());
         ReferedTokenPosition rtp2 = new ReferedTokenPosition(((RecognitionEntity) o2).getReferedTokenPositions());
         return rtp1.compareTo(rtp2);
      }
      return 0;
   }
}
