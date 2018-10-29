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


package com.execue.nlp.service;

import java.util.List;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.swi.exception.KDXException;

/**
 * @author John Mallavalli
 */
public interface ICloudService {

   public void processRecognitionRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput);

   public boolean processValidationRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput);

   public void processWeightAssignmentRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput);

   public List<RecognizedCloudEntity> getCloudEntities (List<RICloud> riClouds,
            List<IWeightedEntity> recognitionEntities, ProcessorInput processorInput) throws KDXException;

   public boolean ifCloudShouldBeProcessed (RecognizedCloudEntity cloudEntity);

}
