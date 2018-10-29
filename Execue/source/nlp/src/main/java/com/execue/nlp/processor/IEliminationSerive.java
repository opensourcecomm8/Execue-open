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

import com.execue.nlp.bean.NLPToken;
import com.execue.nlp.bean.matrix.IterationSummary;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.nlp.bean.matrix.Summary;

/**
 * @author Nihar
 */
public interface IEliminationSerive {

   /**
    * Method to remove the SFL entities from the recognition if a PWentity exists with the same word as there is no need
    * of keeping the duplicates.
    * 
    * @param summary
    * @param summary
    * @return
    */
   public Summary filterSFLEntities (Summary summary);

   /**
    * Method to check for the group recognitions in rthe NLP Tokens. if one of the recognition entity of consecutive NLP
    * tokens refer to same SFL term, This recognition entity should be used.
    * 
    * @param nlpTokens
    * @return
    */
   public List<NLPToken> addGroupRecognition (List<NLPToken> nlpTokens);

   /**
    * Method to filter out the OntoEntities by TopCluster for a given possibility
    * 
    * @param possibilities
    */
   public void filterOntoEntitiesByTopCluster (List<Possibility> possibilities);

   /**
    * Method to filter the Individual Recognition Summary by checking the quality for each groupRecognition to the
    * corresponding individualRecognition. if for all groupRecognition the quality for individualRec is more
    * individualPossibility will not get filtered.
    * 
    * @param iterationSummary
    */
   public void filterIndividualRecognitionsBasedOnQuality (IterationSummary iterationSummary);
}
