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


package com.execue.handler.swi.constraints;

import java.util.List;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.bean.UIColumn;
import com.execue.handler.bean.UIConstraint;
import com.execue.handler.bean.UITable;
import com.execue.handler.bean.UITableConstraintsInfo;

public interface IConstraintServiceHandler {

   public Asset getAsset (Long assetId) throws ExeCueException;

   public List<UITable> getUITablesForAsset (Long assetId) throws ExeCueException;

   public UITableConstraintsInfo getTableInfoById (Long assetId, Long tableId) throws ExeCueException;

   public void updateConstraintInfo (TableInfo tableInfo) throws ExeCueException;

   public List<UIColumn> getUICoumnsForTable (Long tableId) throws ExeCueException;

   public List<UITable> getUIOrphanTablesForAsset (Long assetId) throws ExeCueException;

   public List<UITable> getUIPrimaryKeyTablesForAsset (Long assetId) throws ExeCueException;

   public void saveConstraint (UITableConstraintsInfo tableConstraintsInfo) throws ExeCueException;

   public Colum getColumnById (Long columnId) throws ExeCueException;

   public Tabl getTableById (Long tableId) throws ExeCueException;

   public List<Colum> getColumnsOfTable (Long tableId) throws ExeCueException;
   
   public void deleteForeignKeyConstraint(UIConstraint fkConstraint) throws ExeCueException;
}
