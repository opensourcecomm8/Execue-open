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
import java.util.Map;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.platform.exception.SourceMetaDataException;

/**
 * @author Raju Gottumukkala
 */
public interface ISourceMetaDataService {

   public List<Tabl> getTablesFromSource (DataSource dataSource, Long userId) throws SourceMetaDataException;

   public List<Colum> getColumnsFromSource (DataSource dataSource, Tabl table) throws SourceMetaDataException;

   public TableInfo getTableFromSource (Asset asset, Tabl table) throws SourceMetaDataException;

   public List<Membr> getMembersFromSource (Asset asset, Tabl table, LimitEntity limitEntity)
            throws SourceMetaDataException;

   public List<String> getPrimaryKeysFromSource (Asset asset, Tabl table) throws SourceMetaDataException;

   public Map<String, String> getForeignKeysFromSource (Asset asset, Tabl table) throws SourceMetaDataException;

   public Integer getMembersCount (Asset asset, Tabl table, Colum lookupColumn) throws SourceMetaDataException;

   public List<String> getMinMaxMemberValueFromSource (Asset asset, Tabl tabl, Colum colum)
            throws SourceMetaDataException;

}
