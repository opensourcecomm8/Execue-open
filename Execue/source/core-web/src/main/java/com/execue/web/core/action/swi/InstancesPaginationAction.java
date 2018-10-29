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


package com.execue.web.core.action.swi;

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;

/**
 * @author John Mallavalli
 */
public class InstancesPaginationAction extends SWIPaginationAction {

   private static Logger    logger          = Logger.getLogger(InstancesPaginationAction.class);
   private List<Instance>   instances;
   private Concept          concept;
   private static final int PAGE_SIZE       = 11;
   private static final int NUMBER_OF_LINKS = 15;

   @SuppressWarnings ("unchecked")
   @Override
   public String processPage () throws ExeCueException {
      try {
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
         setConcept(getKdxServiceHandler().getConcept(concept.getId()));
         setInstances(getKdxServiceHandler().getInstancesByPage(getApplicationContext().getModelId(), concept.getId(),
                  getPageDetail()));
         if (logger.isDebugEnabled()) {
            logger.debug(getPageDetail().toString());
         }
         getHttpRequest().put(PAGINATION, getPageDetail());
         if (getInstances().size() > 0) {
            return SUCCESS;
         }
         return CREATE;
      } catch (Exception exception) {
         logger.error(exception, exception);
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
   }

   public List<Instance> getInstances () {
      return instances;
   }

   public void setInstances (List<Instance> instances) {
      this.instances = instances;
   }

   public Concept getConcept () {
      return concept;
   }

   public void setConcept (Concept concept) {
      this.concept = concept;
   }
}
