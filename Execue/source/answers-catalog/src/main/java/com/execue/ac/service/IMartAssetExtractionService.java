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


package com.execue.ac.service;

import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.exception.MartAssetExtractionException;

/**
 * This service represents the step7 of mart creation process. Extraction of asset and carry the source asset
 * information to parent asset happens here.
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/01/2011
 */
public interface IMartAssetExtractionService {

   /**
    * This method extracts the asset from the data mart created. The mart creation output info contains all the
    * information required to extract the asset as data mart in swi
    * 
    * @param martCreationOutputInfo
    * @throws MartAssetExtractionException
    */
   public boolean extractAsset (MartCreationOutputInfo martCreationOutputInfo) throws MartAssetExtractionException;
}
