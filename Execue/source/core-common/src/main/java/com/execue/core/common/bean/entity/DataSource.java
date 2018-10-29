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


package com.execue.core.common.bean.entity;

import java.io.Serializable;
import java.util.Set;

import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ConnectionType;
import com.execue.core.common.type.DataSourceType;

public class DataSource implements Serializable {

   private static final long serialVersionUID  = 1L;
   private Long              id;
   private String            name;
   private String            displayName;
   private String            description;
   private ConnectionType    connectionType;
   private AssetProviderType providerType;
   private String            jndiConnectionFactory;
   private String            jndiProviderUrl;
   private String            jndiName;
   private String            location;
   private int               port;
   private String            userName;
   private String            password;
   private String            schemaName;
   private String            owner;
   private Set<Asset>        assets;
   private DataSourceType    type              = DataSourceType.REGULAR;
   private Set<User>         users;
   private CheckType         passwordEncrypted = CheckType.YES;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public ConnectionType getConnectionType () {
      return connectionType;
   }

   public void setConnectionType (ConnectionType connectionType) {
      this.connectionType = connectionType;
   }

   public String getJndiName () {
      return jndiName;
   }

   public void setJndiName (String jndiName) {
      this.jndiName = jndiName;
   }

   public String getLocation () {
      return location;
   }

   public void setLocation (String location) {
      this.location = location;
   }

   public int getPort () {
      return port;
   }

   public void setPort (int port) {
      this.port = port;
   }

   public String getUserName () {
      return userName;
   }

   public void setUserName (String userName) {
      this.userName = userName;
   }

   public String getPassword () {
      return password;
   }

   public void setPassword (String password) {
      this.password = password;
   }

   public String getSchemaName () {
      return schemaName;
   }

   public void setSchemaName (String schemaName) {
      this.schemaName = schemaName;
   }

   public Set<Asset> getAssets () {
      return assets;
   }

   public void setAssets (Set<Asset> assets) {
      this.assets = assets;
   }

   public String getJndiConnectionFactory () {
      return jndiConnectionFactory;
   }

   public void setJndiConnectionFactory (String jndiConnectionFactory) {
      this.jndiConnectionFactory = jndiConnectionFactory;
   }

   public String getJndiProviderUrl () {
      return jndiProviderUrl;
   }

   public void setJndiProviderUrl (String jndiProviderUrl) {
      this.jndiProviderUrl = jndiProviderUrl;
   }

   public AssetProviderType getProviderType () {
      return providerType;
   }

   public void setProviderType (AssetProviderType providerType) {
      this.providerType = providerType;
   }

   public DataSourceType getType () {
      return type;
   }

   public void setType (DataSourceType type) {
      this.type = type;
   }

   @Override
   public String toString () {
      return this.getName();
   }

   @Override
   public int hashCode () {
      return this.toString().hashCode();
   }

   @Override
   public boolean equals (Object obj) {
      if (obj instanceof DataSource || obj instanceof String) {
         return this.toString().equals(((DataSource) obj).toString());
      }
      return false;
   }

   public String getDisplayName () {
      return displayName;
   }

   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

   public Set<User> getUsers () {
      return users;
   }

   public void setUsers (Set<User> users) {
      this.users = users;
   }

   /**
    * @return the owner
    */
   public String getOwner () {
      return owner;
   }

   /**
    * @param owner the owner to set
    */
   public void setOwner (String owner) {
      this.owner = owner;
   }

   public boolean isPasswordEncyptionSet () {
      return passwordEncrypted == CheckType.YES;
   }

   /**
    * @return the passwordEncrypted
    */
   public CheckType getPasswordEncrypted () {
      return passwordEncrypted;
   }

   /**
    * @param passwordEncrypted the passwordEncrypted to set
    */
   public void setPasswordEncrypted (CheckType passwordEncrypted) {
      this.passwordEncrypted = passwordEncrypted;
   }

}
