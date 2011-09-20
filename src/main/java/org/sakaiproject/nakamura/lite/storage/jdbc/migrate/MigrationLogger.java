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
import com.google.common.collect.Sets;
import org.sakaiproject.nakamura.api.lite.PropertyMigrator;
import org.sakaiproject.nakamura.api.lite.Session;
import org.sakaiproject.nakamura.api.lite.StorageClientException;
import org.sakaiproject.nakamura.api.lite.accesscontrol.AccessDeniedException;
import org.sakaiproject.nakamura.api.lite.content.Content;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MigrationLogger {

    public static final String LOG_PATH = "/system/migrationlog";

    public static final String DATE_READABLE = "migrationDate";

    public static final String DATE_MS = "migrationEpoch";

    private Map<String, Map<String, Object>> logMap = Maps.newHashMap();

    private Set<String> seenClasses = Sets.newHashSet();

    private Content logContent;

    public MigrationLogger(Session session) throws StorageClientException, AccessDeniedException {
        this.logContent = session.getContentManager().get(LOG_PATH);
        if (this.logContent == null) {
            this.logContent = new Content(LOG_PATH, new HashMap<String, Object>());
        } else {
            Map<String, Object> classLogs = this.logContent.getProperties();
            for (String className : classLogs.keySet()) {
                Object val = classLogs.get(className);
                if (val instanceof Map) {
                    seenClasses.add(className);
                }
            }
        }
    }

    public void log(PropertyMigrator migrator) {
        String className = migrator.getClass().getName();
        Map<String, Object> classMap = this.logMap.get(className);
        if (classMap == null) {
            classMap = Maps.newHashMap();
        }
        long currentMS = System.currentTimeMillis();
        classMap.put(DATE_READABLE, new Date(currentMS));
        classMap.put(DATE_MS, currentMS);
        logMap.put(className, classMap);
        seenClasses.add(className);
    }

    public void write(Session session) throws StorageClientException, AccessDeniedException {
        for (String className : this.logMap.keySet()) {
            Map<String, Object> rowMap = this.logMap.get(className);
            this.logContent.setProperty(className, rowMap);
        }
        session.getContentManager().update(this.logContent);
    }

    public boolean hasMigratorRun(PropertyMigrator migrator) {
        return seenClasses.contains(migrator.getClass().getName());
    }

}
