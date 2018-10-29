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
package com.execue.platform.swi;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.wrapper.AssetExtractionInput;
import com.execue.core.common.type.SuccessFailureType;
import com.execue.swi.exception.SDXException;

/**
 * @author Jayadev
 */
public interface IAssetExtractionService {

   /**
    * This operation is transactional. The asset schema is created in SWI as the asset is being recorded in the system.
    * The following are the usecases - 1) Source Asset Extraction: The user is trying to create a parent asset from the
    * client source 2) Derived Asset Extraction: A cube/mart data has been created(using ETL) and the Asset schema is to
    * be absorbed into the system; The physical target of recording the asset is same, i.e., in SWI schema. This is
    * achieved via the ORM layer and appropriate module-based DAO manager
    * 
    * @param assetExtractionInput
    * @return
    * @throws SDXExtractionException
    */
   public SuccessFailureType registerAsset (AssetExtractionInput assetExtractionInput) throws SDXException;

   public void absorbConstraints (Asset asset) throws SDXException;

   public void absorbJoins (Asset asset) throws SDXException;

}
