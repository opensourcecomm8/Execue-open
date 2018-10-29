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


package com.execue.core.common.bean.graph;

import java.util.List;

/**
 * @author Abhijit
 * @since Nov 5, 2008
 */
public interface IGraphPath {

   public List<IGraphComponent> getFullPath ();

   public IGraphComponent getStartVertex ();

   public IGraphComponent getEndVertex ();

   public IGraphComponent getPathComponent (int index);

   public List<IGraphComponent> getConnectors ();

   public int getPathLength ();

   public IGraphPath subPath (int start, int end);

   public List<IGraphComponent> getPartialPath (int start, int end);

   public boolean contains (IGraphComponent gc);

   public void addPathComponent (IGraphComponent gc);

   public int getMaxTokenPosition ();

   public boolean intersectsWith (IGraphPath graphPath);
}
