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


package com.execue.core.exception;

/** */
public class UidException extends java.lang.Exception {
  
  public UidException(int exceptionCode, String message) {
    super(message);
    this.code = exceptionCode;
  }
  
  public UidException(int exceptionCode, String message, Throwable cause) {
    super(message, cause);
    this.code = exceptionCode;
  }
  
  public UidException(int exceptionCode, Throwable cause) {
    super(cause);
    this.code = exceptionCode;
  }

  /** */
  public int code = ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE;

  /** */
  public int getCode() {
    return this.code;
  }
}
