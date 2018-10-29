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


package com.execue.web.core.converter;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.util.TypeConversionException;

/**
 * Converts a blank string to null when the field doesn't get a value from request
 * 
 * @author Raju Gottumukkala
 */
public class StringTypeConverter extends StrutsTypeConverter {

   private static final Logger log = Logger.getLogger(StringTypeConverter.class);
   
   @Override
   public Object convertFromString (Map context, String[] values, Class toClass) {
      try {
         if (values.length != 1) {
            return super.performFallbackConversion(context, values, toClass);
         }
         String toObject = values[0];
         if (StringUtils.isBlank(toObject)) {
            return null;
         } else {
            return toObject;
         }
      } catch (Exception exception) {
         log.error(exception, exception);
         throw new TypeConversionException(exception.getMessage());
      }
   }

   @Override
   public String convertToString (Map context, Object object) {
      /*
       * Return null if the object is null
       * Return null if the zeroth element is blank
       * Return object if the zeroth element is not blank
       * 
       * NOTE: If not null object is assumed as an array of Strings
       */
      try {
         if (object == null) {
            return null;
         } else if (StringUtils.isBlank(((String[]) object)[0])) {
            return null;
         } else {
            return ((String[]) object)[0];
         }
      } catch (Exception exception) {
         log.error(exception, exception);
         throw new TypeConversionException(exception.getMessage());
      }
   }

}
