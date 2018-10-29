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


package com.execue.util.cryptography.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.util.cryptography.CryptographyKeyGenerationFactory;
import com.execue.util.cryptography.ICryptographyKeyGenerationService;
import com.execue.util.cryptography.ICryptographyService;
import com.execue.util.cryptography.exception.CryptographyException;
import com.execue.util.cryptography.type.EncryptionAlgorithm;

/**
 * This class is for doing encryption and decryption process
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/08/09
 */
public class CryptographyServiceImpl implements ICryptographyService {

   CryptographyKeyGenerationFactory cryptographyKeyGenerationFactory;

   public String decryptBase64 (String encryptedString, String encryptionKey, EncryptionAlgorithm encryptionAlgorithm)
            throws CryptographyException {
      try {
         if (StringUtils.isBlank(encryptedString)) {
            return encryptedString;
         }
         Cipher decryptCipher = Cipher.getInstance(encryptionAlgorithm.getValue());

         decryptCipher.init(Cipher.DECRYPT_MODE, getCryptographyKeyGenerationService(encryptionAlgorithm)
                  .generateSecretKey(encryptionKey));
         // Encode bytes to base64 to get a string
         BASE64Decoder decoder = new BASE64Decoder();
         byte[] decodedBytes = decoder.decodeBuffer(encryptedString);
         // Decrypt
         byte[] unencryptedByteArray = decryptCipher.doFinal(decodedBytes);
         // Decode using utf-8
         return new String(unencryptedByteArray, "UTF8");
      } catch (NoSuchAlgorithmException e) {
         throw new CryptographyException(ExeCueExceptionCodes.DECYPTION_PROCESS_FAILED, e);
      } catch (NoSuchPaddingException e) {
         throw new CryptographyException(ExeCueExceptionCodes.DECYPTION_PROCESS_FAILED, e);
      } catch (InvalidKeyException e) {
         throw new CryptographyException(ExeCueExceptionCodes.DECYPTION_PROCESS_FAILED, e);
      } catch (UnsupportedEncodingException e) {
         throw new CryptographyException(ExeCueExceptionCodes.DECYPTION_PROCESS_FAILED, e);
      } catch (IllegalBlockSizeException e) {
         throw new CryptographyException(ExeCueExceptionCodes.DECYPTION_PROCESS_FAILED, e);
      } catch (BadPaddingException e) {
         throw new CryptographyException(ExeCueExceptionCodes.DECYPTION_PROCESS_FAILED, e);
      } catch (IOException e) {
         throw new CryptographyException(ExeCueExceptionCodes.DECYPTION_PROCESS_FAILED, e);
      }
   }

   public String encryptBase64 (String unencryptedString, String decryptionKey, EncryptionAlgorithm encryptionAlgorithm)
            throws CryptographyException {
      try {
         if (StringUtils.isBlank(unencryptedString)) {
            return unencryptedString;
         }
         Cipher encryptCipher = Cipher.getInstance(encryptionAlgorithm.getValue());
         encryptCipher.init(Cipher.ENCRYPT_MODE, getCryptographyKeyGenerationService(encryptionAlgorithm)
                  .generateSecretKey(decryptionKey));
         // Encode the string into bytes using utf-8
         byte[] unencryptedByteArray = unencryptedString.getBytes("UTF8");
         // Encrypt
         byte[] encryptedBytes = encryptCipher.doFinal(unencryptedByteArray);
         // Encode bytes to base64 to get a string
         BASE64Encoder encoder = new BASE64Encoder();
         return encoder.encode(encryptedBytes);
      } catch (NoSuchAlgorithmException e) {
         throw new CryptographyException(ExeCueExceptionCodes.ENCRPTION_PROCESS_FAILED, e);
      } catch (NoSuchPaddingException e) {
         throw new CryptographyException(ExeCueExceptionCodes.ENCRPTION_PROCESS_FAILED, e);
      } catch (InvalidKeyException e) {
         throw new CryptographyException(ExeCueExceptionCodes.ENCRPTION_PROCESS_FAILED, e);
      } catch (UnsupportedEncodingException e) {
         throw new CryptographyException(ExeCueExceptionCodes.ENCRPTION_PROCESS_FAILED, e);
      } catch (IllegalBlockSizeException e) {
         throw new CryptographyException(ExeCueExceptionCodes.ENCRPTION_PROCESS_FAILED, e);
      } catch (BadPaddingException e) {
         throw new CryptographyException(ExeCueExceptionCodes.ENCRPTION_PROCESS_FAILED, e);
      }
   }

   public CryptographyKeyGenerationFactory getCryptographyKeyGenerationFactory () {
      return cryptographyKeyGenerationFactory;
   }

   public void setCryptographyKeyGenerationFactory (CryptographyKeyGenerationFactory cryptographyKeyGenerationFactory) {
      this.cryptographyKeyGenerationFactory = cryptographyKeyGenerationFactory;
   }

   private ICryptographyKeyGenerationService getCryptographyKeyGenerationService (
            EncryptionAlgorithm encryptionAlgorithm) {
      return cryptographyKeyGenerationFactory.getCryptographyKeyGenerationService(encryptionAlgorithm);
   }

}
