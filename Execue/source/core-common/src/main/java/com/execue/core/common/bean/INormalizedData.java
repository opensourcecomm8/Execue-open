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
package com.execue.core.common.bean;

import java.util.Set;

import com.execue.core.common.bean.entity.NormalizedDataType;

/**
 * @author Nitesh
 */
public interface INormalizedData extends Cloneable {

   /**
    * This method returns the string representation of the implement normalized data
    * 
    * @return String
    */
   public String getValue ();

   /**
    * This method returns the display string representation of the implement normalized data
    * 
    * @return String
    */
   public String getDisplayValue ();

   /**
    * This method returns the type name of the normalized data
    * 
    * @return Long
    */
   public String getType ();

   /**
    * This method returns the type be id of the normalized data
    * 
    * @return Long
    */
   public Long getTypeBedId ();

   /**
    * This method return the normalized data type of the implementation
    * 
    * @return
    */
   public NormalizedDataType getNormalizedDataType ();

   /**
    * This method returns the referred token positions for all the recognitions in the current normalized data
    * 
    * @return the Set<Integer> containing the referred token positions
    */
   public Set<Integer> getReferredTokenPositions ();

   /**
    * This method returns the Original referred token positions for all the recognitions in the current normalized data
    * 
    * @return the Set<Integer> containing the referred token positions
    */
   public Set<Integer> getOriginalReferredTokenPositions ();

   /**
    * @return
    * @throws CloneNotSupportedException
    */
   public Object clone () throws CloneNotSupportedException;
}
