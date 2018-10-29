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


package com.execue.handler.audittrail;

import java.util.List;

import com.execue.core.common.bean.Page;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIUser;
import com.execue.handler.bean.UIUserAccessAuditInput;
import com.execue.handler.bean.grid.UIUserAccessAudit;
import com.execue.handler.bean.grid.UIUserSearchAudit;

public interface IAuditTrailServiceHandler {

   public List<UIUser> getUsers (String search) throws HandlerException;

   public List<UIUserAccessAudit> getUserAccessAuditLog (UIUserAccessAuditInput userAccessAuditLog, Page page)
            throws HandlerException;

   public List<UIUserSearchAudit> getUserSearchAuditLog (UIUserAccessAuditInput userAccessAuditLog, Page page)
            throws HandlerException;

}
