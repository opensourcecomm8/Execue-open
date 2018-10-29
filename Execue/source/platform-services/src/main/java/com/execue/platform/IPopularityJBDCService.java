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


package com.execue.platform;

import java.sql.Connection;
import java.util.List;

import com.execue.core.common.bean.swi.PopularityInfo;
import com.execue.core.common.type.PopularityTermType;
import com.execue.swi.exception.SWIException;

public interface IPopularityJBDCService {

   public void insertIntoPopularityInfo (Connection connection, List<List<Object>> popularityInfo,
            List<Integer> sqlTypes) throws SWIException;

   public List<PopularityInfo> getPopularityInfo (Connection connection, PopularityTermType popularityTermType)
            throws SWIException;

   public void createPopularityInfo (Connection connection, String createStatement) throws SWIException;

   public void dropPopularityInfo (Connection connection, String dropStatement) throws SWIException;
}
