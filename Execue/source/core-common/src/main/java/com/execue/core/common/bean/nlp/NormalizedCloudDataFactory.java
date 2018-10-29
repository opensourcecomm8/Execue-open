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


package com.execue.core.common.bean.nlp;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.entity.NormalizedDataType;



/**
 * @author Nitesh
 *
 */
public class NormalizedCloudDataFactory {
   
   private static final Logger log = Logger.getLogger(NormalizedCloudDataFactory.class);
   
   public static INormalizedData getNormalizedCloudData (NormalizedDataType normalizedCloudDataType) {
      INormalizedData normalizedCloudData = null;

      try {
         normalizedCloudData = (INormalizedData) Class.forName(normalizedCloudDataType.getValue()).newInstance();
      } catch (ClassNotFoundException classNotFoundException) {
         log.error(classNotFoundException);
         classNotFoundException.printStackTrace();
      } catch (InstantiationException instantiationException) {
         log.error(instantiationException);
         instantiationException.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }

      return normalizedCloudData;
   }   

}
