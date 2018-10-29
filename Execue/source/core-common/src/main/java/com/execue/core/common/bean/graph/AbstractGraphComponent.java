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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Abhijit
 * @since Sep 17, 2008 - 2:50:23 PM
 */
public abstract class AbstractGraphComponent implements IGraphComponent {

   private GraphComponentType type;
   List<IGraphComponent>      nextGraphComponents;

   public double getWeight () {
      return 0;
   }

   public GraphComponentType getType () {
      return type;
   }

   public void setType (GraphComponentType type) {
      this.type = type;
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      return super.clone();
   }

   /**
    * @return the nextGraphComponents
    */
   public List<IGraphComponent> getNextGraphComponents () {
      if (nextGraphComponents == null) {
         return new ArrayList<IGraphComponent>(1);
      }
      return nextGraphComponents;
   }

   /**
    * @return the nextGraphComponents
    */
   public boolean hasNextGraphComponents () {
      if(this.nextGraphComponents == null) return false;
      else if(this.nextGraphComponents.size() == 0) return false;
      return true;
   }

   /**
    * @param nextGraphComponent
    *           the nextGraphComponents to set
    */
   public void addNextGraphComponent (IGraphComponent nextGraphComponent) {
      if (this.nextGraphComponents == null) {
         nextGraphComponents = new ArrayList<IGraphComponent>(1);
      }
      if (!this.nextGraphComponents.contains(nextGraphComponent)) {
         nextGraphComponents.add(nextGraphComponent);
      }
   }

   public String print (String space) {
      StringBuffer sb = new StringBuffer();
      sb.append(space).append(this.getRecognitionDisplayName()).append("\n");
      for (IGraphComponent component : getNextGraphComponents()) {
         sb.append(component.print(space + "\t"));
      }
      return sb.toString();
   }
}
