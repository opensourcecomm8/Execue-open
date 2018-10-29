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

import java.util.List;

import com.execue.core.common.bean.nlp.UserQuery;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.nlp.bean.matrix.RootMatrix;

/**
 * @author Nitesh
 */
public interface ISummarizationService {

   // /**
   // * This method returns the list of unambiguous possibilities for the given possibility
   // *
   // * @param possibility
   // * @return the List<Possibility>
   // */
   // public List<Possibility> getUnambiguousPossibilities (Possibility possibility, RootMatrix rootMatrix,
   // UserQuery userQuery) throws CloneNotSupportedException;

   /**
    * This method sets the correct associations in each of the unambiguous possibility
    * 
    * @param associations
    * @param unAmbiguousPossibilities
    * @throws CloneNotSupportedException
    */
   // public void setAssociations (RootMatrix rootMatrix, List<Association> associations,
   // List<Possibility> unAmbiguousPossibilities) throws CloneNotSupportedException;
   public List<Possibility> filterByHangingRelations (List<Possibility> allUnAmbiguousPossibilities);

   public void populateUnambiguousPossibilities (RootMatrix rootMatrix, UserQuery userQuery);

   public void updatPossibilityWithSemanticScopingOutput (Possibility possibility);
}
