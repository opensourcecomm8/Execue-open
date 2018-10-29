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


package com.execue.reporting.aggregation.test.mock;

import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.SQLExeCueCachedResultSet;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.querygen.Query;
import com.execue.dataaccess.clientsource.IClientSourceDAO;
import com.execue.dataaccess.exception.DataAccessException;

public class MockClientSourceDAOImpl implements IClientSourceDAO {

   public ExeCueResultSet executeQuery (DataSource dataSource, Query query, String queryString)
            throws DataAccessException {
      SQLExeCueCachedResultSet rs = new SQLExeCueCachedResultSet(10);

      try {
         // rs.addString("test", "3");
         // rs.addString("test1", "4");

      } catch (Exception e) {
         e.printStackTrace();
      }
      return rs;
   }

   public ExeCueResultSet executeQuery (String dataSourceName, String queryString) throws DataAccessException {
      // TODO Auto-generated method stub
      return null;
   }

}
