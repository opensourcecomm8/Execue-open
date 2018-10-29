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
 * Created on Jul 26, 2008
 */
package com.execue.nlp.engine.barcode.matrix;

import com.execue.nlp.bean.ProcessorInput;

/**
 * @author kaliki
 */
public interface IMatrixExecutor {

	/**
	 * Method to execute the Matrix for Query Processing.
	 * @param processorInput input for the iteration.
	 * @param contextName name of the processor Context.
	 * @return
	 */
	public boolean executeMatrix (ProcessorInput processorInput, String contextName);

}
