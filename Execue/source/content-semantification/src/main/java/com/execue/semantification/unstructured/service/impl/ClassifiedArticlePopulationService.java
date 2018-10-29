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


package com.execue.semantification.unstructured.service.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.execue.core.common.bean.entity.unstructured.RIFeatureContent;
import com.execue.core.common.bean.entity.unstructured.SemantifiedContent;
import com.execue.core.common.bean.entity.unstructured.SemantifiedContentFeatureInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.exception.PlatformException;
import com.execue.semantification.bean.SemantificationInput;
import com.execue.semantification.exception.SemantificationException;
import com.execue.semantification.exception.SemantificationExceptionCodes;
import com.execue.semantification.service.GenericArticlePopulationService;
import com.execue.semantification.util.SemantificationUtil;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.helper.UnstructuredWarehouseHelper;

/**
 * @author abhijit
 * @since Feb 17, 2011 : 11:04:11 AM
 * @version 1.0
 */
public class ClassifiedArticlePopulationService extends GenericArticlePopulationService {

   private static Logger    logger               = Logger.getLogger(ClassifiedArticlePopulationService.class);

   private static final int FORBIDDEN_ERROR_CODE = 403;
   private static final int PAGE_NOT_FOUND_CODE  = 404;

   /**
    * @param contextId
    * @param sourceContent
    * @return the SemantifiedContent
    * @throws UnstructuredWarehouseException
    */
   @Override
   public SemantifiedContent populateSemantifiedContent (Long contextId, SemantificationInput semantificationInput,
            LocationPointInfo locationPointInfo) throws SemantificationException {
      SemantifiedContent semantifiedContent = SemantificationUtil
               .getSemantifiedContentForSourceContent(semantificationInput);

      boolean isAppWithLocation = getPlatformServicesConfigurationService().isApplicationHasLocationRealization(
               contextId);
      if (isAppWithLocation && locationPointInfo != null) {
         updateSemantifiedContentWithLocationDetails(semantifiedContent, locationPointInfo);
      }

      try {
         semantifiedContent = getUnstructuredWarehouseManagementService().createSemantifiedContent(contextId,
                  semantifiedContent);
      } catch (UnstructuredWarehouseException e) {
         throw new SemantificationException(e.getCode(), e);
      }

      return semantifiedContent;
   }

   /**
    * Store Reduced Form in the Semantified Content Feature Information table
    * 
    * @param contextId
    * @param semanticPossibilities
    *           Possibility containing Reduced form
    * @param semantifiedContent
    *           SemantifiedContent
    * @throws UnstructuredWarehouseException
    */
   @Override
   public List<SemantifiedContentFeatureInformation> populateSemantifiedContentFeatureInformation (Long contextId,
            SemanticPossibility semanticPossibility, SemantifiedContent semantifiedContent)
            throws SemantificationException {

      List<SemantifiedContentFeatureInformation> featureInformations = new ArrayList<SemantifiedContentFeatureInformation>();
      try {
         featureInformations = getSemantificationHelper().getSemantifiedContentFeaturesInformation(semanticPossibility,
                  true);
      } catch (UnstructuredWarehouseException e) {
         throw new SemantificationException(e.getCode(), e);
      }

      if (CollectionUtils.isEmpty(featureInformations)) {
         logger.error("SHOULD NOT HAPPEN :: No Feature Info Found...");
         throw new SemantificationException(SemantificationExceptionCodes.DEFAULT_SYSTEM_EXCEPTION_CODE,
                  "No Feature Information Found");
      }

      Map<Long, List<SemantifiedContentFeatureInformation>> contentFeatureInfoById = UnstructuredWarehouseHelper
               .getFeaturesInformationByFeatureId(featureInformations);

      // TODO: NK: Update feature information for missing feature info based on the feature knowledge info we have

      // Populate the unknown features
      Map<Long, Map<Long, FeatureValueType>> existingFeatureByFeatureTypeMapByAppId = getSemantificationConfiguration()
               .getExistingFeatureByFeatureTypeMapByAppId();
      Long applicationId = semanticPossibility.getApplication().getId();
      Map<Long, FeatureValueType> existingFeatureByFeatureTypeMap = existingFeatureByFeatureTypeMapByAppId
               .get(applicationId);
      if (!MapUtils.isEmpty(existingFeatureByFeatureTypeMap)) {
         for (Entry<Long, FeatureValueType> entry : existingFeatureByFeatureTypeMap.entrySet()) {
            Long featureId = entry.getKey();
            if (!contentFeatureInfoById.containsKey(featureId)) {
               try {
                  addUnknownFeatureInformation(featureInformations, entry);
               } catch (CloneNotSupportedException e) {
                  throw new SemantificationException(SemantificationExceptionCodes.DEFAULT_SYSTEM_EXCEPTION_CODE, e);
               } catch (UnstructuredWarehouseException e) {
                  throw new SemantificationException(e.getCode(), e);
               }
            }
         }
      }

      try {
         boolean isAppWithLocation = getPlatformServicesConfigurationService().isApplicationHasLocationRealization(
                  contextId);
         updateFeatureInfoWithSemantifiedContentDetails(semantifiedContent, featureInformations, isAppWithLocation);

         List<RIFeatureContent> displayableRIFeatureContentList = getUnstructuredWHFeatureContentLookupService()
                  .getDisplayableRIFeatureContentByContextId(contextId);
         // populate displayable column feature values based on FCM (Feature Column Mapping) information
         getUnstructuredWarehouseHelper().populateDisplayableFeatureValues(displayableRIFeatureContentList,
                  featureInformations);
         getUnstructuredWarehouseManagementService().saveSemantifiedContentFeatureInformation(contextId,
                  featureInformations);
         return featureInformations;
      } catch (UnstructuredWarehouseException e) {
         throw new SemantificationException(e.getCode(), e);
      }
   }

   @Override
   public int populateFacetInformation (Long contextId, List<SemantifiedContentFeatureInformation> featureInfoList)
            throws SemantificationException {
      try {
         return getUnstructuredFacetManagementService().storeFacetInfo(contextId, featureInfoList);
      } catch (PlatformException e) {
         throw new SemantificationException(e.getCode(), e);
      }
   }

   private void updateSemantifiedContentWithLocationDetails (SemantifiedContent semantifiedContent,
            LocationPointInfo locationPointInfo) {
      semantifiedContent.setLocationId(locationPointInfo.getId());
      semantifiedContent.setLocationDisplayName(locationPointInfo.getLocationDisplayName());
      semantifiedContent.setLongitude(locationPointInfo.getLongitude());
      semantifiedContent.setLatitude(locationPointInfo.getLatitude());
   }

   private void updateFeatureInfoWithSemantifiedContentDetails (SemantifiedContent semantifiedContent,
            List<SemantifiedContentFeatureInformation> featureInformations, boolean isAppWithLocation) {
      for (SemantifiedContentFeatureInformation featureInformation : featureInformations) {
         featureInformation.setSemantifiedContentId(semantifiedContent.getId());
         featureInformation.setSemantifiedContentDate(semantifiedContent.getContentDate());
         featureInformation.setProcessingState(semantifiedContent.getProcessingState());
         if (isAppWithLocation) {
            featureInformation.setLocationId(semantifiedContent.getLocationId());
            featureInformation.setLocationDisplayName(semantifiedContent.getLocationDisplayName());
            featureInformation.setLongitude(semantifiedContent.getLongitude());
            featureInformation.setLatitude(semantifiedContent.getLatitude());
         }
      }
   }

   private void addUnknownFeatureInformation (List<SemantifiedContentFeatureInformation> structuredContentList,
            Entry<Long, FeatureValueType> entry) throws CloneNotSupportedException, UnstructuredWarehouseException {
      Long featureId = entry.getKey();
      FeatureValueType featureValueType = entry.getValue();
      // Getting the first car information and cloning it
      SemantifiedContentFeatureInformation unknownCarInformation = (SemantifiedContentFeatureInformation) structuredContentList
               .get(0).clone();
      unknownCarInformation.setFeatureId(featureId);
      unknownCarInformation.setFeatureValueType(featureValueType);
      unknownCarInformation.setFeatureWeight(10d);
      if (FeatureValueType.VALUE_STRING == featureValueType) {
         unknownCarInformation.setFeatureValue(getUnstructuredWHConfigurationService().getFeatureStringUnknownValue());
         unknownCarInformation.setFeatureNumberValue(null);
      } else if (FeatureValueType.VALUE_NUMBER == featureValueType) {
         unknownCarInformation.setFeatureValue(null);
         unknownCarInformation.setFeatureNumberValue(getUnstructuredWHFeatureContentLookupService()
                  .getFeatureRangeDefaultValue(unknownCarInformation.getContextId(), featureId));
      }
      structuredContentList.add(unknownCarInformation);
   }

   @Override
   public List<String> getValidImageUrls (SemantifiedContent semantifiedContent) throws SemantificationException {
      String description = semantifiedContent.getLongDescription();
      List<String> imageUrls = new ArrayList<String>(1);
      Long minImageSize = getSemantificationConfiguration().getMinImageSize();
      Long maxImageCount = getSemantificationConfiguration().getMaxImageCount();
      imageUrls = populateImageUrls(description, maxImageCount, minImageSize);
      if (ExecueCoreUtil.isCollectionEmpty(imageUrls)) {
         try {
            String imageUrlString = getImageURLString(semantifiedContent.getUrl());
            List<String> imgUrls = populateImageUrls(imageUrlString, maxImageCount, minImageSize);
            if (!CollectionUtils.isEmpty(imgUrls)) {
               imageUrls.addAll(imgUrls);
            }
         } catch (IOException e) {
            throw new SemantificationException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
         }
      }
      return imageUrls;
   }

   private List<String> populateImageUrls (String htmlBody, Long maxImageCount, Long minImageSize)
            throws SemantificationException {
      List<String> imageUrls = new ArrayList<String>(1);
      if (!htmlBody.trim().startsWith("<html>")) {
         htmlBody = "<html>" + htmlBody;
      }
      Parser bodyParser;
      try {
         bodyParser = new Parser(htmlBody);
         NodeList row = bodyParser.extractAllNodesThatMatch(new TagNameFilter("IMG"));
         for (int rowIndex = 0; rowIndex < row.size(); rowIndex++) {
            if (imageUrls.size() >= maxImageCount) {
               break;
            }
            ImageTag linkNode = (ImageTag) row.elementAt(rowIndex);
            String imageUrl = linkNode.getAttribute("src");
            if (isImageUrlValid(imageUrl, minImageSize)) {
               imageUrls.add(imageUrl);
            }
         }
      } catch (ParserException e) {
         throw new SemantificationException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
      } catch (IOException e) {
         throw new SemantificationException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
      }
      return imageUrls;
   }

   private String getImageURLString (String url) throws IOException {
      BufferedReader bufferedReader = null;
      HttpURLConnection connection = null;
      try {
         StringBuilder urlString = new StringBuilder();
         connection = getHttpURLConnection(url);
         bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         String line = null;
         while ((line = bufferedReader.readLine()) != null) {
            urlString.append(line);
         }
         return urlString.toString();
      } finally {
         if (bufferedReader != null) {
            bufferedReader.close();
         }
         if (connection != null) {
            if (connection.getInputStream() != null) {
               connection.getInputStream().close();
            }
            connection.disconnect();
            connection = null;
         }
      }
   }

   private HttpURLConnection getHttpURLConnection (String httpURL) throws IOException {
      URL u = new URL(httpURL);
      return (HttpURLConnection) u.openConnection();
   }

   private String getEscapedHttpURLByDecoding (String httpURL) {
      String escapedImageUrl = httpURL;
      try {
         escapedImageUrl = URLDecoder.decode(httpURL, "UTF-8");
      } catch (Exception e) {
      }
      return escapedImageUrl;
   }

   private boolean isImageUrlValid (String imageUrl, Long minImageSize) throws IOException {
      if (StringUtils.isEmpty(imageUrl)) {
         return false;
      }
      HttpURLConnection httpURLConnection = getHttpURLConnection(imageUrl);
      try {
         int responseCode = httpURLConnection.getResponseCode();
         if (responseCode == PAGE_NOT_FOUND_CODE) {
            httpURLConnection.disconnect();
            // Page Not Found
            // Try with decoded URL
            httpURLConnection = getHttpURLConnection(getEscapedHttpURLByDecoding(imageUrl));
            responseCode = httpURLConnection.getResponseCode();
            if (responseCode == PAGE_NOT_FOUND_CODE) {
               logger.error("PAGE NOT FOUND ERROR for URL: " + imageUrl);
               return false;
            }
            if (responseCode == FORBIDDEN_ERROR_CODE) {
               // Forbidden
               logger.error("FORBIDDEN ERROR for URL: " + imageUrl);
               return false;
            }
         }
         if (responseCode == FORBIDDEN_ERROR_CODE) {
            // Forbidden
            logger.error("FORBIDDEN ERROR for URL: " + imageUrl);
            return false;
         }
         long imageSize = httpURLConnection.getContentLength();
         if (imageSize >= minImageSize) {
            return true;
         } else if (imageSize < 0) {
            // If couldn't determine the image size based on connection header, then read the input stream and
            // calculate the size
            imageSize = getImageSize(httpURLConnection);
            if (imageSize >= minImageSize) {
               return true;
            }
         }
      } finally {
         if (httpURLConnection != null) {
            try {
               if (httpURLConnection.getInputStream() != null) {
                  httpURLConnection.getInputStream().close();
               }
            } catch (FileNotFoundException fileNotFoundException) {

            }
            httpURLConnection.disconnect();
            httpURLConnection = null;
         }
      }
      return false;
   }

   private long getImageSize (URLConnection connection) throws IOException {
      InputStreamReader input = new InputStreamReader(connection.getInputStream());
      char[] buffer = new char[1024 * 4];
      long count = 0;
      int n = 0;
      while (-1 != (n = input.read(buffer))) {
         count += n;
      }
      input.close();
      return count;
   }

}