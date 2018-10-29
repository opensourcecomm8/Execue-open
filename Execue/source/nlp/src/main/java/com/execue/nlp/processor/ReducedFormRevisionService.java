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
package com.execue.nlp.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.nlp.bean.entity.Association;
import com.execue.nlp.bean.matrix.Possibility;

/**
 * @author Nihar
 */
public class ReducedFormRevisionService implements IReducedFormRevesionService {

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.processor.IReducedFormRevesionService#reviseReducedFormByPositions(java.util.List,
    *      com.execue.nlp.bean.matrix.Possibility)
    */
   public Set<Association> reviseReducedFormByPositions (List<Association> associations, Possibility possibility) {
      Set<Association> revisedAssociations = new HashSet<Association>(1);
      Map<Long, List<Association>> associationMap = new HashMap<Long, List<Association>>(1);
      populateAssociationMapByPathId(associationMap, associations, revisedAssociations, possibility);
      for (Entry<Long, List<Association>> entry : associationMap.entrySet()) {
         List<Association> assocList = entry.getValue();
         if (assocList.size() <= 1) {
            revisedAssociations.addAll(assocList);
            continue;
         }
         Association firstAssociation = assocList.get(0);
         if (firstAssociation.getCardinality() > 1) {
            revisedAssociations.addAll(assocList);
            continue;
         }
         sortPathsByProximity(assocList);
         revisedAssociations.add(assocList.get(0));
      }
      return revisedAssociations;
   }

   private void sortPathsByProximity (List<Association> associations) {
      Collections.sort(associations, new Comparator<Association>() {

         public int compare (Association p1, Association p2) {
            int proxmityPath1 = p1.getPositionDiffBetweenSourceAndDestination();
            int proxmityPath2 = p2.getPositionDiffBetweenSourceAndDestination();
            if (proxmityPath1 >= proxmityPath2) {
               return 1;
            } else {
               return -1;
            }

         }
      });

   }

   /**
    * Populate the Map of the Association with path def id as key and list of association as values.
    * 
    * @param associationMap
    * @param associations
    * @param possibility
    */
   private void populateAssociationMapByPathId (Map<Long, List<Association>> associationMap,
            List<Association> associations, Set<Association> revisedAssociations, Possibility possibility) {
      for (Association association : associations) {
         List<IWeightedEntity> weightedEntities = possibility.getRecognitionEntities();
         if (!weightedEntities.contains(association.getPathComponent().get(0))
                  || !weightedEntities.contains(association.getPathComponent().get(
                           association.getPathComponent().size() - 1))) {
            continue;
         }

         //This is needed, as even though the cardinality is one, we still wanted to keep the association if both
         //the subject and object is expicit recognition
         int subjectPostion = association.getSubjectPostion();
         int objectPostion = association.getObjectPostion();
         if (subjectPostion >= 0 && objectPostion >= 0) {
            revisedAssociations.add(association);
         }
         List<Association> assocList = associationMap.get(association.getPathDefId());
         if (assocList == null) {
            assocList = new ArrayList<Association>(1);
            associationMap.put(association.getPathDefId(), assocList);
         }
         assocList.add(association);
      }

   }
}
