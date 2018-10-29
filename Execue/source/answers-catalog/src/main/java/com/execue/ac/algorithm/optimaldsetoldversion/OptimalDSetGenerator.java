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


package com.execue.ac.algorithm.optimaldsetoldversion;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.ObjectFactory;

import com.execue.ac.algorithm.optimaldsetoldversion.impl.OptimalDSetAlgorithm;
import com.execue.core.common.bean.optimaldset.OptimalDSet;
import com.execue.core.common.bean.optimaldset.OptimalDSetAlgorithmInput;

public class OptimalDSetGenerator {

   private static final Logger log = Logger.getLogger(OptimalDSetGenerator.class);

   private ObjectFactory       optimalDSetAlgorithmFactory;

   public Collection<OptimalDSet> generateOptimalDSets (OptimalDSetAlgorithmInput optimalDSetAlgorithmInput) {
      OptimalDSetAlgorithm optimalDSetAlgorithm = (OptimalDSetAlgorithm) getOptimalDSetAlgorithmFactory().getObject();
      if (log.isDebugEnabled()) {
         log.debug(optimalDSetAlgorithm);
      }
      return optimalDSetAlgorithm.computeOptimalDSets(optimalDSetAlgorithmInput);
   }

   public ObjectFactory getOptimalDSetAlgorithmFactory () {
      return optimalDSetAlgorithmFactory;
   }

   public void setOptimalDSetAlgorithmFactory (ObjectFactory optimalDSetAlgorithmFactory) {
      this.optimalDSetAlgorithmFactory = optimalDSetAlgorithmFactory;
   }

}
