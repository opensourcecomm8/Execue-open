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

import com.execue.util.cryptography.exception.CryptographyException;
import com.execue.util.cryptography.type.EncryptionAlgorithm;

/**
 * This interface is for doing encryption and decryption process
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/08/09
 */
public interface ICryptographyService {

   /**
    * Encrypt a string using DES encryption, and return the encrypted string as a base64 encoded string.
    * 
    * @param unencryptedString
    *           The string to encrypt.
    * @return String The DES encrypted and base 64 encoded string.
    * @throws CryptographyException
    *            If an error occurs.
    */
   public String encryptBase64 (String unencryptedString, String encryptionKey, EncryptionAlgorithm encryptionAlgorithm)
            throws CryptographyException;

   /**
    * Decrypt a base64 encoded, DES encrypted string and return the unencrypted string.
    * 
    * @param encryptedString
    *           The base64 encoded string to decrypt.
    * @return String The decrypted string.
    * @throws CryptographyException
    *            If an error occurs.
    */
   public String decryptBase64 (String encryptedString, String decryptionKey, EncryptionAlgorithm encryptionAlgorithm)
            throws CryptographyException;
}
