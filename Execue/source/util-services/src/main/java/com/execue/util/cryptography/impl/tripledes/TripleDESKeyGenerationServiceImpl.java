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


package com.execue.util.cryptography.impl.tripledes;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.util.cryptography.ICryptographyKeyGenerationService;
import com.execue.util.cryptography.exception.CryptographyException;
import com.execue.util.cryptography.type.EncryptionAlgorithm;

/**
 * This class is for generating secret key for encryption for DES algorithm
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/08/09
 */
public class TripleDESKeyGenerationServiceImpl implements ICryptographyKeyGenerationService {

   @Override
   public SecretKey generateSecretKey (String key) throws CryptographyException {
      try {
         if (key.getBytes().length < 24) {
            throw new CryptographyException(
                     ExeCueExceptionCodes.SECRET_KEY_GENERATION_TRIPLE_DES_LENGTH_CANNOT_BE_LESSTHAN_24_BYTES_FAILED,
                     "Inappropriate key length");
         }
         SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(EncryptionAlgorithm.TRIPLE_DES.getValue());
         DESedeKeySpec keySpec = new DESedeKeySpec(key.getBytes());
         return keyFactory.generateSecret(keySpec);
      } catch (NoSuchAlgorithmException e) {
         throw new CryptographyException(ExeCueExceptionCodes.SECRET_KEY_GENERATION_FAILED, e);
      } catch (InvalidKeyException e) {
         throw new CryptographyException(ExeCueExceptionCodes.SECRET_KEY_GENERATION_FAILED, e);
      } catch (InvalidKeySpecException e) {
         throw new CryptographyException(ExeCueExceptionCodes.SECRET_KEY_GENERATION_FAILED, e);
      }

   }
}
