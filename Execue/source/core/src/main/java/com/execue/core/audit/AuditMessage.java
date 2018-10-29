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


package com.execue.core.audit;


public class AuditMessage {

    private int type;

    private String message;

    private long duration;

    private String username;

    private long transactionId;

    public AuditMessage () {
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String val) {
        this.message = val;
    }

    public int getType () {
        return type;
    }

    public void setType (int val) {
        this.type = val;
    }

    public long getDuration () {
        return duration;
    }

    public void setDuration (long val) {
        this.duration = val;
    }

    public long getTransactionId () {
        return transactionId;
    }

    public void setTransactionId (long val) {
        this.transactionId = val;
    }

    public String getUsername () {
        return username;
    }

    public void setUsername (String val) {
        this.username = val;
    }

}
