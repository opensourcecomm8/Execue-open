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


package com.execue.nlp.engine.barcode;

import org.springframework.beans.factory.ObjectFactory;

import com.execue.nlp.bean.matrix.Possibility;
import com.execue.nlp.bean.matrix.RootMatrix;
import com.execue.nlp.engine.barcode.matrix.IMatrixExecutor;
import com.execue.nlp.engine.barcode.possibility.AbstractExecPossibility;
import com.execue.nlp.engine.barcode.possibility.IExecutablePossibility;
import com.execue.nlp.engine.barcode.possibility.PossibilityController;

public class BarcodeScannerFactory {

   private ObjectFactory                execPossibilityFactory;
   private ObjectFactory                matrixExecutorFactory;
   private ObjectFactory                possibilityControllerFactory;

   private static BarcodeScannerFactory _factory;

   public static BarcodeScannerFactory getInstance () {
      if (_factory == null) {
         _factory = new BarcodeScannerFactory();
      }
      return _factory;
   }

   public ObjectFactory getExecPossibilityFactory () {
      return execPossibilityFactory;
   }

   public void setExecPossibilityFactory (ObjectFactory execPossibilityFactory) {
      this.execPossibilityFactory = execPossibilityFactory;
   }

   public ObjectFactory getMatrixExecutorFactory () {
      return matrixExecutorFactory;
   }

   public ObjectFactory getPossibilityControllerFactory () {
      return possibilityControllerFactory;
   }

   public void setPossibilityControllerFactory (ObjectFactory possibilityControllerFactory) {
      this.possibilityControllerFactory = possibilityControllerFactory;
   }

   public void setMatrixExecutorFactory (ObjectFactory matrixExecutorFactory) {
      this.matrixExecutorFactory = matrixExecutorFactory;
   }

   // start of factory methods

   public IMatrixExecutor getMatrixExecutor () {
      return (IMatrixExecutor) matrixExecutorFactory.getObject();
   }

   public IExecutablePossibility getExecPossibility (Possibility possibility, RootMatrix rootMatrix) {
      AbstractExecPossibility abstractExecPossibility = (AbstractExecPossibility) execPossibilityFactory.getObject();
      abstractExecPossibility.setRootMatrix(rootMatrix);
      abstractExecPossibility.setPossibility(possibility);
      return abstractExecPossibility;
   }

   public PossibilityController createPossibilityController () {
      return (PossibilityController) possibilityControllerFactory.getObject();
   }
}
