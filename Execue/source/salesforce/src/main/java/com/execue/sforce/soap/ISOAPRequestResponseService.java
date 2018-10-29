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


package com.execue.sforce.soap;

import com.execue.sforce.exception.SforceException;

/**
 * This interface contains the methods specific to SOAP protocol
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public interface ISOAPRequestResponseService {

   /**
    * This method returns the SOAP ResponseXML for the given soapRequest and the partner url
    * 
    * @param partnerSessionUrl
    *           address of the http server where soapRequest will be written to
    * @param soapRequest
    * @return returns the response from the URL as string
    * @throws SforceException
    */
   public String executeSOAPRequest (String partnerSessionURL, String soapRequestXML) throws SforceException;

}
