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


package com.execue.platform.swi.operation.synchronization;

import com.execue.core.common.bean.swi.AssetSyncAbsorptionContext;
import com.execue.platform.exception.AssetSynchronizationException;

/**
 * This service absorbs the AssetSync information into swi for particular asset
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */
public interface IParentAssetSyncAbsorptionService {

   // rules for absorption
   // 1. the list of tables which are deleted needs to be deleted with standard call to delete a table
   // 2. updation of table is pretty straightforward, we need to fire update on table
   // 3. columns deleted, if column is lookup column, delete the table else delete the column
   // 4. columns updated, fire update on columns
   // 5. columns added, addition is easy but how to map ????
   // 6. members deletion pretty straight forward
   // 7. members updation pretty straight forward
   // 8. members addition need to find how to map to instance ??
   public boolean absorbAssetSyncInfo (AssetSyncAbsorptionContext assetSyncAbsorptionContext) throws AssetSynchronizationException;

}
