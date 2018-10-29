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

import java.util.List;

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Stat;

public interface IAnswersCatalogDefaultsService {

   public List<Stat> getDefaultStatsForCubeCreation () throws AnswersCatalogException;

   public List<String> getDefaultStatNamesForCubeCreation () throws AnswersCatalogException;

   public DataSource getDefaultTargetDataSource () throws AnswersCatalogException;

   public String getStatisticsConceptName () throws AnswersCatalogException;

   public String getCubeAllValue () throws AnswersCatalogException;

   public boolean isMartTargetDataSourceSameAsSourceDataSource () throws AnswersCatalogException;

   public boolean isCubeTargetDataSourceSameAsSourceDataSource () throws AnswersCatalogException;
}
