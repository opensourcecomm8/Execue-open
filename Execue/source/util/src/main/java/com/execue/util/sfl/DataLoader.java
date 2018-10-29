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


package com.execue.util.sfl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author laura
 */

public class DataLoader {

   public static String db       = "jdbc:mysql://localhost:3306/swi-freebase-films"; // the swi db
   public static String userName = "root";
   public static String pss      = "execue";

   /**
    * loads all the instances' displayname from the DB where the display name does not exist as business term at sfl
    * term
    */
   public static Set<String> loadDisplayInstancesSetFromDBIgnoreExisting () throws ClassNotFoundException, SQLException {
      Set<String> result = new HashSet<String>();
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection(db, userName, pss);
      Statement stmt = con.createStatement();
      String query = "SELECT display_name FROM instance where display_name not in (select business_term from sfl_term)";
      ResultSet queryResults = stmt.executeQuery(query);
      while (queryResults.next()) {
         result.add(queryResults.getString(1));
      }
      return result;
   }

   public static Set<String> loadInstanceDescriptions (long modelId, int startIndex) throws ClassNotFoundException,
            SQLException {
      Set<String> result = new HashSet<String>();
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection(db, userName, pss);
      Statement stmt = con.createStatement();
      // String query = "SELECT description FROM instance where description is not null and description != name and id >
      // 1000";
      String query = "SELECT i.description FROM business_entity be, instance i, model_group_mapping mgm WHERE be.instance_id = i.id"
               + " AND be.entity_type = 'CLI'"
               + " AND mgm.model_id = "
               + modelId
               + " AND mgm.primry = 'Y'"
               + " AND be.model_group_id = mgm.model_group_id"
               + " AND i.description IS NOT NULL"
               + " AND i.description != i.name" + " AND i.id > " + startIndex;
      ResultSet queryResults = stmt.executeQuery(query);
      while (queryResults.next()) {
         result.add(queryResults.getString(1));
      }
      return result;
   }

   public static Set<String> loadInstanceDisplayName (long modelId, int startIndex) throws ClassNotFoundException,
            SQLException {
      Set<String> result = new HashSet<String>();
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection(db, userName, pss);
      Statement stmt = con.createStatement();
      // String query = "SELECT display_name FROM instance where display_name is not null and display_name != name and
      // (" +
      // "display_name != description or description is null and id > 1000)";
      String query = "SELECT DISTINCT i.display_name FROM business_entity be, instance i, model_group_mapping mgm WHERE be.instance_id = i.id"
               + " AND be.entity_type = 'CLI'"
               + " AND mgm.model_id = "
               + modelId
               + " AND mgm.primry = 'Y'"
               + " AND be.model_group_id = mgm.model_group_id"
               + " AND i.display_name IS NOT NULL"
               + " AND i.display_name != i.NAME AND (i.display_name != i.description OR i.description IS NULL)"
               + " AND i.id  > " + startIndex;
      ResultSet queryResults = stmt.executeQuery(query);
      while (queryResults.next()) {
         result.add(queryResults.getString(1));
      }
      return result;
   }

   /**
    * loads the concepts' displayname from the DB
    */
   public static Set<String> loadDisplayConceptsSetFromDB (long modelId, int startIndex) throws ClassNotFoundException,
            SQLException {
      Set<String> result = new HashSet<String>();
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection(db, userName, pss);
      Statement stmt = con.createStatement();
      // String query = "SELECT display_name FROM concept where id >= " + id;
      String query = "SELECT c.display_name FROM business_entity be, concept c, model_group_mapping mgm WHERE be.concept_id=c.id  AND c.id >= "
               + startIndex
               + " AND be.entity_type = 'C'"
               + " AND mgm.model_id = "
               + modelId
               + " AND mgm.primry = 'Y'"
               + " AND be.model_group_id = mgm.model_group_id";
      ResultSet queryResults = stmt.executeQuery(query);
      while (queryResults.next()) {
         result.add(queryResults.getString(1));
      }
      return result;
   }

   public static Set<String> loadDisplayRelationsSetFromDB (long modelId, int startIndex)
            throws ClassNotFoundException, SQLException {
      Set<String> result = new HashSet<String>();
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection(db, userName, pss);
      Statement stmt = con.createStatement();
      // String query = "SELECT display_name FROM concept where id >= " + id;
      String query = "SELECT r.display_name FROM business_entity be, relation r, model_group_mapping mgm WHERE be.relation_id=r.id  AND r.id >= "
               + startIndex
               + " AND be.entity_type = 'R'"
               + " AND mgm.model_id = "
               + modelId
               + " AND mgm.primry = 'Y'"
               + " AND be.model_group_id = mgm.model_group_id";
      ResultSet queryResults = stmt.executeQuery(query);
      while (queryResults.next()) {
         result.add(queryResults.getString(1));
      }
      return result;
   }

   /**
    * loads the concepts' displayname from the DB where the display name does not exist as business term at sfl term
    */
   public static Set<String> loadDisplayConceptsSetFromDBIgnoreExisting (int id) throws ClassNotFoundException,
            SQLException {
      Set<String> result = new HashSet<String>();
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection(db, userName, pss);
      Statement stmt = con.createStatement();
      String query = "SELECT display_name FROM concept where display_name not in (select business_term from sfl_term) and id >= "
               + id;
      ResultSet queryResults = stmt.executeQuery(query);
      while (queryResults.next()) {
         result.add(queryResults.getString(1));
      }
      return result;
   }

   /**
    * loads the ID and BEID of each relation from DB
    */
   public static Map<String, String> loadRelationsFromDB () throws ClassNotFoundException, SQLException {
      Map<String, String> result = new HashMap<String, String>();
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection(db, userName, pss);
      Statement stmt = con.createStatement();
      String query = "SELECT relation.ID,NAME,business_entity.id FROM relation, business_entity WHERE business_entity.relation_id=relation.ID";
      ResultSet queryResults = stmt.executeQuery(query);
      while (queryResults.next()) {
         result.put(queryResults.getString(2), queryResults.getString(3) + "," + queryResults.getString(1));
      }
      return result;
   }
}