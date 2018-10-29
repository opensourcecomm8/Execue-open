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


package com.execue.util.cryptography;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.UtilServicesBaseTest;
import com.execue.util.cryptography.exception.CryptographyException;
import com.execue.util.cryptography.type.EncryptionAlgorithm;

/**
 * This test class tests the cryptography services
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/08/09
 */
public class CryptographyServiceTest extends UtilServicesBaseTest {

   @Before
   public void setUp () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTearDown();
   }

   @Test
   public void testTripleDESEncryption () {
      try {
         String encryptBase64 = getCryptograhyService().encryptBase64("root", "execueDatasourceConnection",
                  EncryptionAlgorithm.TRIPLE_DES);
         System.out.println("Encrypted String : " + encryptBase64);
         String decryptBase64 = getCryptograhyService().decryptBase64(encryptBase64, "execueDatasourceConnection",
                  EncryptionAlgorithm.TRIPLE_DES);
         System.out.println("Decrypted String : " + decryptBase64);
      } catch (CryptographyException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testDESEncryption () {
      try {
         String encryptBase64 = getCryptograhyService().encryptBase64("root", "execueDatasourceConnection",
                  EncryptionAlgorithm.DES);
         System.out.println("Encrypted String : " + encryptBase64);
         String decryptBase64 = getCryptograhyService().decryptBase64(encryptBase64, "execueDatasourceConnection",
                  EncryptionAlgorithm.DES);
         System.out.println("Decrypted String : " + decryptBase64);
      } catch (CryptographyException e) {
         e.printStackTrace();
      }
   }
}
