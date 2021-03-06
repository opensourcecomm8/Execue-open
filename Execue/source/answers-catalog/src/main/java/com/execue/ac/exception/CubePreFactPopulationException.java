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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.execue.ac.exception;

/**
 * @author Vishay
 */
public class CubePreFactPopulationException extends DataMartCreationException {

   private static final long serialVersionUID = 1L;

   public CubePreFactPopulationException (int exceptionCode, Throwable cause) {
      super(exceptionCode, cause);
   }

   public CubePreFactPopulationException (int exceptionCode, String message) {
      super(exceptionCode, message);
   }

   public CubePreFactPopulationException (int exceptionCode, String message, Throwable cause) {
      super(exceptionCode, message, cause);
   }
}