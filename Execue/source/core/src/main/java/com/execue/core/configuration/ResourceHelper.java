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


package com.execue.core.configuration;

import java.util.HashMap;
import java.util.Map;

import com.execue.core.bean.Resource;

public class ResourceHelper {

   private static Map<String, Resource> resourceLookupMap = new HashMap<String, Resource>();

   private ResourceHelper () {

   }

   public static Map<String, Resource> getResourceLookupMap () {
      return ResourceHelper.resourceLookupMap;
   }

   public static void setResourceLookupMap (Map<String, Resource> resourceLookupMap) {
      ResourceHelper.resourceLookupMap = resourceLookupMap;
   }

   public static Resource getResourceByActionName (String actionName) {
      return getResourceLookupMap().get(actionName);
   }
}
