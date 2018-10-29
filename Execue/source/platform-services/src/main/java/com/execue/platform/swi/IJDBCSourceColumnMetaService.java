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

import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.swi.ColumnDataTypeMetaInfo;
import com.execue.swi.exception.SWIException;

public interface IJDBCSourceColumnMetaService {

   public ColumnDataTypeMetaInfo getColumnDataTypeMetaInfo (DataSource dataSource, String tableName, QueryColumn column)
            throws SWIException;

   public boolean isColumnDateType (DataSource dataSource, String tableName, QueryColumn column, DateFormat dateFormat,
            Long countEntireRecords, Long countDateRecords) throws SWIException;

}
