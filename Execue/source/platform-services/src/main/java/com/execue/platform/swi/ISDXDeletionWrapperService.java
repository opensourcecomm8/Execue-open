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

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.swi.exception.SDXException;

public interface ISDXDeletionWrapperService {

   public void deleteAssetTables (Asset asset, List<Tabl> tables) throws SDXException;

   public void deleteAssetTableColumns (Asset asset, Tabl table, List<Colum> columns) throws SDXException;

}
