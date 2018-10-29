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


package com.execue.util.conversion.timeframe;

import java.util.Map;

import com.execue.util.conversion.timeframe.bean.TimeFrameConversionInputStructure;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionInputStructureList;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionOutputInfo;

/**
 * This service performs the basic time frame conversion
 * 
 * @author Vishay
 * @version 1.0
 */
public interface ITimeFrameHandlerService {

   public TimeFrameConversionOutputInfo timeFrameConversion (
            TimeFrameConversionInputStructure timeFrameConversionInputStructure) throws Exception;

   public TimeFrameConversionOutputInfo timeFrameConversion (
            TimeFrameConversionInputStructureList timeFrameConversionInputStructureList) throws Exception;

   public Map<String, String> getMonthLastDayMap ();
}
