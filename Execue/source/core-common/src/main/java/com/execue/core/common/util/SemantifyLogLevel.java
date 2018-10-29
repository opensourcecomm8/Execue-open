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

import org.apache.log4j.Level;

/**
 * This class defines the semantify custom log level.
 * 
 * @author Vishay Gupta
 * @version 1.0
 * @since 13/01/10
 */
public class SemantifyLogLevel extends Level {

   private static final long serialVersionUID = 1L;

   public static final int   SEMANTIFY_INT    = DEBUG_INT + 10;

   public static final Level SEMANTIFY        = new SemantifyLogLevel(SEMANTIFY_INT, "SEMANTIFY", 7);

   protected SemantifyLogLevel (int level, String levelStr, int syslogEquivalent) {
      super(level, levelStr, syslogEquivalent);
   }

   public static Level toLevel (String levelStr) {
      if (levelStr != null && levelStr.toUpperCase().equals("SEMANTIFY")) {
         return SEMANTIFY;
      }
      return toLevel(levelStr, Level.DEBUG);
   }

   public static Level toLevel (int level) {
      if (level == SEMANTIFY_INT) {
         return SEMANTIFY;
      }
      return toLevel(level, Level.DEBUG);
   }

   public static Level toLevel (int level, Level defaultLevel) {
      if (level == SEMANTIFY_INT) {
         return SEMANTIFY;
      }
      return Level.toLevel(level, defaultLevel);
   }

   public static Level toLevel (String levelStr, Level defaultLevel) {
      if (levelStr != null && levelStr.toUpperCase().equals("SEMANTIFY")) {
         return SEMANTIFY;
      }
      return Level.toLevel(levelStr, defaultLevel);
   }
}
