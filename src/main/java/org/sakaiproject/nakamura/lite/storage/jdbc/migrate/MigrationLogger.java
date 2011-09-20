/*
 * Licensed to the Sakai Foundation (SF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The SF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.sakaiproject.nakamura.lite.storage.jdbc.migrate;

import com.google.common.collect.Maps;
import org.sakaiproject.nakamura.api.lite.PropertyMigrator;
import org.sakaiproject.nakamura.api.lite.Session;
import org.sakaiproject.nakamura.api.lite.StorageClientException;
import org.sakaiproject.nakamura.api.lite.StorageClientUtils;
import org.sakaiproject.nakamura.api.lite.accesscontrol.AccessDeniedException;
import org.sakaiproject.nakamura.api.lite.content.Content;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class MigrationLogger {

    public static final String LOG_ROOT_PATH = "/system/migrationlog";

    public static final String DATE_READABLE = "migrationDate";

    public static final String DATE_MS = "migrationEpoch";

    private Map<String, Content> logMap = Maps.newHashMap();

    public MigrationLogger(Session session) throws StorageClientException, AccessDeniedException {
        Iterator<Content> savedLogs = session.getContentManager().listChildren(LOG_ROOT_PATH);
        while ( savedLogs.hasNext() ) {
            Content log = savedLogs.next();
            logMap.put(StorageClientUtils.getObjectName(log.getPath()), log);
        }
    }

    public void log(PropertyMigrator migrator) {
        String className = migrator.getClass().getName();
        long currentMS = System.currentTimeMillis();

        Map<String, Object> logData = Maps.newHashMap();
        logData.put(DATE_READABLE, new Date(currentMS).toString());
        logData.put(DATE_MS, currentMS);

        Content logContent = this.logMap.get(className);
        if (logContent == null) {
            logContent = new Content(StorageClientUtils.newPath(LOG_ROOT_PATH, className), logData);
            this.logMap.put(className, logContent);
        }
    }

    public void write(Session session) throws StorageClientException, AccessDeniedException {
        for (Content content : this.logMap.values() ) {
          session.getContentManager().update(content);
        }
    }

    public boolean hasMigratorRun(PropertyMigrator migrator) {
        return logMap.get(migrator.getClass().getName()) != null;
    }

}
