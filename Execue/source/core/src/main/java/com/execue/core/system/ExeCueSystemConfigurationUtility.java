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


package com.execue.core.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.execue.core.configuration.IConfigurable;
import com.execue.core.configuration.SystemConstants;
import com.execue.core.exception.ConfigurationException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueDAOUtil;


/**
 * This class is created to reuse the code for invoking all 
 *    the configurable beans from regular flow (ExeCueSystem) 
 *    and as well as Test Case flow (Module specific Base Test Cases).
 * 
 * NOTE: This class should never be used apart from the above mentioned locations
 *  
 * @author gopal
 *
 */
public class ExeCueSystemConfigurationUtility {

   private static final Logger log = Logger.getLogger(ExeCueSystemConfigurationUtility.class);

   /**
    * Get all the bean definitions which are of type IConfigurable.
    * On each of the bean invoke doConfigurable() method
    * 
    * @param applicationContext
    * @param configurablesFilePath 
    * @throws ConfigurationException
    */
   public static void loadConfigurationServices (ApplicationContext applicationContext) throws ConfigurationException {
      String[] beanids = applicationContext.getBeanNamesForType(IConfigurable.class);
      List<String> configurablesToExecute = Arrays.asList(beanids);
      if (ExecueCoreUtil.isCollectionNotEmpty(configurablesToExecute)) {
         List<String> totalOrderedConfigurables = populateTotalOrderedConfigurables(SystemConstants.ORDERED_CONFIGURABLES_FILE_PATH_KEY);
         List<String> orderedConfigurablesToExecute = new ArrayList<String>();
         for (String orderedConfigurable : totalOrderedConfigurables) {
            if (configurablesToExecute.contains(orderedConfigurable)) {
               orderedConfigurablesToExecute.add(orderedConfigurable);
            }
         }
         // if ordered configurables size is not matching ordered configurables size, then we should not intialize the system
         if (configurablesToExecute.size() == orderedConfigurablesToExecute.size()) {
            for (String beanName : orderedConfigurablesToExecute) {
               if (log.isInfoEnabled()) {
                  log.info("Configurable bean, doConfigure() starting -- " + beanName);
               }

               IConfigurable configurable = (IConfigurable) applicationContext.getBean(beanName);
               configurable.doConfigure();

               if (log.isInfoEnabled()) {
                  log.info("Configurable bean, doConfigure() completed -- " + beanName);
               }
            }
         } else {
            throw new ConfigurationException(1234, "Ordered Configurables list is not defined completely");
         }
      }
   }

   private static List<String> populateTotalOrderedConfigurables (String configurablesFilePath) {
      Properties props = new Properties();
      InputStream is = ExecueDAOUtil.class.getResourceAsStream(configurablesFilePath);
      List<String> totalOrderedConfigurables = new ArrayList<String>();
      try {
         props.load(is);
         Set<Object> orderedKeys = props.keySet();
         List<Integer> sortedKeysList = new ArrayList<Integer>();
         for (Object object : orderedKeys) {
            sortedKeysList.add(Integer.valueOf((String)object));
         }
         Collections.sort(sortedKeysList);
         for (Integer order : sortedKeysList) {
            totalOrderedConfigurables.add(((String) props.get(order.toString())).trim());
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return totalOrderedConfigurables;
   }
}
