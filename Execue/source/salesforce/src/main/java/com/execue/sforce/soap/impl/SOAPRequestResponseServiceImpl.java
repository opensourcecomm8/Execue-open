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


package com.execue.sforce.soap.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.execue.sforce.exception.SforceException;
import com.execue.sforce.exception.SforceExceptionCodes;
import com.execue.sforce.helper.SforceUtilityHelper;
import com.execue.sforce.soap.ISOAPRequestResponseService;

/**
 * This class contains the methods specific to SOAP protocol
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public class SOAPRequestResponseServiceImpl implements ISOAPRequestResponseService {

   /**
    * This method returns the SOAP ResponseXML for the given soapRequest and the partner url
    * 
    * @param partnerSessionUrl
    *           address of the http server where soapRequest will be written to
    * @param soapRequest
    * @return returns the response from the URL as string
    * @throws SforceException
    */
   public String executeSOAPRequest (String partnerSessionURL, String soapRequestXML) throws SforceException {
      SOAPConnection soapConnection = null;
      ByteArrayOutputStream byteArrayOutputStream = null;
      try {
         byteArrayOutputStream = new ByteArrayOutputStream();
         SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
         soapConnection = soapConnectionFactory.createConnection();
         URL soapSessionURL = new URL(partnerSessionURL);
         SOAPMessage soapMessage = SforceUtilityHelper.prepareSOAPMessage(soapRequestXML);
         soapMessage.writeTo(byteArrayOutputStream);
         SOAPMessage soapResponseMessage = soapConnection.call(soapMessage, soapSessionURL);
         byteArrayOutputStream = new ByteArrayOutputStream();
         soapResponseMessage.writeTo(byteArrayOutputStream);
         return byteArrayOutputStream.toString();
      } catch (SOAPException e) {
         throw new SforceException(
                  SforceExceptionCodes.SFORCE_SOAP_REQUEST_FAILED_EXCEPTION_CODE, e);
      } catch (MalformedURLException e) {
         throw new SforceException(
                  SforceExceptionCodes.SFORCE_SOAP_REQUEST_FAILED_EXCEPTION_CODE, e);
      } catch (IOException e) {
         throw new SforceException(
                  SforceExceptionCodes.SFORCE_SOAP_REQUEST_FAILED_EXCEPTION_CODE, e);
      } finally {
         try {
            if (byteArrayOutputStream != null) {
               byteArrayOutputStream.flush();
               byteArrayOutputStream.close();
            }
            if (soapConnection != null) {
               soapConnection.close();
            }
         } catch (IOException e) {
            throw new SforceException(
                     SforceExceptionCodes.SFORCE_SOAP_REQUEST_FAILED_EXCEPTION_CODE, e);
         } catch (SOAPException e) {
            throw new SforceException(
                     SforceExceptionCodes.SFORCE_SOAP_REQUEST_FAILED_EXCEPTION_CODE, e);
         }
      }
   }
}
