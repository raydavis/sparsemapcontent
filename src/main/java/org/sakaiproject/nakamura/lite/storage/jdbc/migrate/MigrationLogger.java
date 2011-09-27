/**
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
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.sakaiproject.nakamura.api.lite.PropertyMigrator;
import org.sakaiproject.nakamura.api.lite.Repository;
import org.sakaiproject.nakamura.api.lite.StorageClientException;
import org.sakaiproject.nakamura.api.lite.StorageClientUtils;
import org.sakaiproject.nakamura.api.lite.accesscontrol.AccessDeniedException;
import org.sakaiproject.nakamura.api.lite.content.Content;
import org.sakaiproject.nakamura.lite.SessionImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component(immediate = true, metatype = true)
@Service(value = MigrationLogger.class)
public class MigrationLogger {

    public static final String LOG_ROOT_PATH = "/system/migrationlog";

    public static final String DATE_READABLE = "migrationDate";

    public static final String DATE_MS = "migrationEpoch";

    private Map<String, Content> logMap = Maps.newHashMap();

    @Reference
    Repository repository;

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

    public void write() throws StorageClientException, AccessDeniedException {
        SessionImpl session = null;
        try {
            session = (SessionImpl) repository.loginAdministrative();
            for (Content content : this.logMap.values() ) {
              session.getContentManager().update(content);
            }
        } finally {
            if ( session != null ) {
                session.logout();
            }
        }
    }

    public PropertyMigrator[] filterMigrators(PropertyMigrator[] migrators)
            throws StorageClientException, AccessDeniedException{

        readLogMapFromStore();

        // filter out migrators that have already run
        List<PropertyMigrator> filtered = new ArrayList<PropertyMigrator>(migrators.length);
        for (PropertyMigrator migrator : migrators) {
            if (!this.hasMigratorRun(migrator)) {
                filtered.add(migrator);
            }
        }
        return filtered.toArray(new PropertyMigrator[filtered.size()]);
    }

    boolean hasMigratorRun(PropertyMigrator migrator) {
        return logMap.get(migrator.getClass().getName()) != null;
    }

    private void readLogMapFromStore() throws StorageClientException, AccessDeniedException {
        SessionImpl session = null;
        try {
            session = (SessionImpl) repository.loginAdministrative();
            Iterator<Content> savedLogs = session.getContentManager().listChildren(LOG_ROOT_PATH);
            while ( savedLogs.hasNext() ) {
                Content log = savedLogs.next();
                logMap.put(StorageClientUtils.getObjectName(log.getPath()), log);
            }
        } finally {
            if ( session != null ) {
                session.logout();
            }
        }
    }

}
