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


package com.execue.util;

import java.io.IOException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptionUtil {

   private static final Logger log = Logger.getLogger(EncryptionUtil.class);
  private static final String key = "ehFaZgg/awdrifajsql5xQ==";

  private static Cipher cipher;

  static {
    try {
      cipher = Cipher.getInstance("AES");
    } catch (Exception exp) {
      log.error("Error While Creating Cipher ; " + exp);
      exp.printStackTrace();
    }
  }

  public static String encrypt(String msg) {
    String encodedMsg = null;
    try {
      if (msg == null) return null;
      SecretKeySpec skeySpec = new SecretKeySpec(decodeKey(key), cipher.getAlgorithm());
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
      byte[] kbytes = cipher.doFinal(msg.getBytes());
      BASE64Encoder encoder = new BASE64Encoder();
      encodedMsg = encoder.encode(kbytes);
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return encodedMsg;
  }

  public static String decrypt(String msg) {
    String theMsg = null;
    try {
      if (msg == null) return null;
      SecretKeySpec skeySpec = new SecretKeySpec(decodeKey(key), cipher.getAlgorithm());
      cipher.init(Cipher.DECRYPT_MODE, skeySpec);
      byte[] kbytes = cipher.doFinal(decodeKey(msg));
      theMsg = new String(kbytes);
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    return theMsg;
  }

  private static byte[] decodeKey(String msg) throws IOException {
    BASE64Decoder decoder = new BASE64Decoder();
    byte[] kbytes = decoder.decodeBuffer(msg);
    return kbytes;
  }

}
