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


package com.execue.handler.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.security.UserRemoteLocationInfo;
import com.execue.core.util.ExecueCoreUtil;

/**
 * Utility to extract user's remote location based on HTTPRequest
 * 
 * @author Jiten Tiwari
 */
public class RemoteLocationRetrievalUtil {

   private static final String USER_REMOTE_LOCATION_INFO = "userRemoteLocationInfo";
   private static Logger       logger                    = Logger.getLogger(RemoteLocationRetrievalUtil.class);

   /**
    * Look for the remote location object in session, if exists, return it., else Get the remote address from the
    * request Hit the IP Address service to extract location information Store the object in session
    * 
    * @param request
    * @param userRemoteLocationUrl 
    * @return
    */
   @SuppressWarnings ("unchecked")
   public static UserRemoteLocationInfo populateUserRemoteLocationInfo (HttpServletRequest request, Map session,
            String userRemoteLocationUrl, String connectTimeout, String readTimeout) {
      UserRemoteLocationInfo userRemoteLocationInfo = (UserRemoteLocationInfo) session.get(USER_REMOTE_LOCATION_INFO);

      if (userRemoteLocationInfo == null || ExecueCoreUtil.isEmpty(userRemoteLocationInfo.getIpAddress())) {
         userRemoteLocationInfo = new UserRemoteLocationInfo();
         session.put(USER_REMOTE_LOCATION_INFO, userRemoteLocationInfo);
         String remoteAddr = request.getRemoteAddr();
         // remoteAddr = "122.169.195.206";
         String remoteAddrUrl = userRemoteLocationUrl + remoteAddr;

         populateRemoteLocation(userRemoteLocationInfo, remoteAddrUrl, connectTimeout, readTimeout);
      }
      return userRemoteLocationInfo;
   }

   public static void populateRemoteLocation (UserRemoteLocationInfo userRemoteLocationInfo, String remoteAddrUrl,
            String connectTimeout, String readTimeout) {
      String userRemoteInfo = getUserRemoteLocationInfo(remoteAddrUrl, connectTimeout, readTimeout);
      if (!StringUtils.isEmpty(userRemoteInfo)) {
         prepareUserRemoteLocationInfo(userRemoteInfo, userRemoteLocationInfo);
      }
   }

   private static UserRemoteLocationInfo prepareUserRemoteLocationInfo (String userRemoteInfo,
            UserRemoteLocationInfo userRemoteLocationInfo) {
      String[] data = userRemoteInfo.split(",");
      if (data.length > 0) {
         userRemoteLocationInfo.setIpAddress(takeOffDoubleQuote(data[0]));
         userRemoteLocationInfo.setStateCode(takeOffDoubleQuote(data[4]));
         userRemoteLocationInfo.setStateName(takeOffDoubleQuote(data[5]));
         userRemoteLocationInfo.setCityName(takeOffDoubleQuote(data[6]));
         userRemoteLocationInfo.setZipCode(takeOffDoubleQuote(data[7]));
         userRemoteLocationInfo.setLatitude(takeOffDoubleQuote(data[8]));
         userRemoteLocationInfo.setLongitude(takeOffDoubleQuote(data[9]));
         // NOTE: NK:: Can remove it later if this info is not required in logs
         logger.info("For Client IP Address: " + userRemoteLocationInfo.getIpAddress() + " City is: "
                  + userRemoteLocationInfo.getCityName() + ", State is: " + userRemoteLocationInfo.getStateName()
                  + ", State Code: " + userRemoteLocationInfo.getStateCode() + ", and Zip Code is: "
                  + userRemoteLocationInfo.getZipCode() + ", Latitude: " + userRemoteLocationInfo.getLatitude()
                  + ", Longitude: " + userRemoteLocationInfo.getLongitude());
      }
      return userRemoteLocationInfo;
   }

   private static String takeOffDoubleQuote (String inputStr) {
      return inputStr.replaceAll("\"", "");
   }

   private static String getUserRemoteLocationInfo (String ipServiceUrl, String connectTimeout, String readTimeout) {
      // sample info getUserRemoteLocationInfo
      // IP: 122.169.195.206
      // Hostname: ABTS-AP-static-206.195.169.122.airtelbroadband.in
      // City: Hyderabad
      // Country: IN (India)
      // State: 02 (Andhra Pradesh)

      // ISP: Bharti Broadband
      // Organization: Bharti Broadband
      // Latitude: 17.3753
      // Longitude: 78.4744

      StringBuilder sb = new StringBuilder();
      try {
         URL url = new URL(ipServiceUrl);
         URLConnection openConnection = url.openConnection();
         openConnection.setConnectTimeout(Integer.parseInt(connectTimeout));
         openConnection.setReadTimeout(Integer.parseInt(readTimeout));
         InputStream inputStream = openConnection.getInputStream();
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
         String line = null;
         while ((line = bufferedReader.readLine()) != null) {
            sb.append(line + "\n");
         }
         bufferedReader.close();
      } catch (SocketTimeoutException e) {
         logger.error("Remote User Location Timeout: " + e.getMessage());
      } catch (Exception e) {
         logger.error(e.getMessage());
      }
      return sb.toString();
   }
}
