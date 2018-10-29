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


package com.execue.platform.swi;

import java.util.List;

import com.execue.core.common.bean.swi.IndexFormManagementContext;
import com.execue.core.common.type.OperationType;
import com.execue.swi.exception.SWIException;

/**
 * @author John Mallavalli
 */
public interface IIndexFormManagementService {

   // methods for BusinessEntityDefinition
   // /**
   // * This method manages the index forms based for a business entity. <BR>
   // * ADD operation takes care of adding the index forms for a freshly created business entity. <BR>
   // * UPDATE operation needs to be invoked carefully, consumer of this method should call this only when the name or
   // * display name or description of the business term changes. <BR>
   // * For example, this method should not be invoked when there is a change in the associated Stats of a concept. <BR>
   // * DELETE operation cleans up the respective index forms of the business entity.
   // *
   // * @param BusinessEntityDefinition
   // * the Business Entity Definition of the business entity
   // * @param OperationType
   // * the operation type, ADD or DELETE or UPDATE
   // */
   // public void manageIndexForms (BusinessEntityDefinition businessEntity, OperationType operationType)
   // throws SWIException;

   /**
    * This method manages the index forms based for a list of business entities. <BR>
    * ADD operation takes care of adding the index forms for freshly created business entities. <BR>
    * UPDATE operation needs to be invoked carefully, consumer of this method should call this only when the name or
    * display name or description of the business term changes. <BR>
    * For example, this method should not be invoked when there is a change in the associated Stats of a concept. <BR>
    * DELETE operation cleans up the respective index forms of the supplied business entities.
    * 
    * @param List
    *           the list of Business Entity Definition Ids
    * @param OperationType
    *           the operation type, ADD or DELETE or UPDATE
    */
   public void manageIndexForms (Long modelId, List<Long> businessEntityDefinitionIds, Long parentBedId,
            OperationType operationType) throws SWIException;

   public void manageIndexForms (Long modelId, Long businessEntityDefinitionId, Long parentBedId,
            OperationType operationType) throws SWIException;

   /**
    * This method creates the index forms for the business entities that are freshly created in the given model and do
    * not have the index forms generated. <BR>
    * Note: <i>Currently the UPDATE and DELETE operations are not supported.</i>
    * 
    * @param modelId
    */
   public void manageIndexForms (IndexFormManagementContext indexFormManagementContext) throws SWIException;

}
