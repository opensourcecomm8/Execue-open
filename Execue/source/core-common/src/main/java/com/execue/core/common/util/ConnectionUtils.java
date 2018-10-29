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


package com.execue.core.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author abhijit
 * @since Sep 18, 2010
 * Time: 3:42:46 PM
 */
public class ConnectionUtils {
   public final static String MYSQL_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

   public static Connection getConnection(String DB_CONN_STRING, String USER_NAME, String PASSWORD) {
      Connection result = null;
      try {
         Class.forName(MYSQL_DRIVER_CLASS_NAME).newInstance();
         result = DriverManager.getConnection(DB_CONN_STRING, USER_NAME, PASSWORD);
      } catch (SQLException e) {
         e.printStackTrace();
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      } catch (InstantiationException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }
      return result;
   }   
}
