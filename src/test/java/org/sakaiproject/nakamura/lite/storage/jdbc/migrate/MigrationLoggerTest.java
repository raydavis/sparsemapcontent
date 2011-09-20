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

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sakaiproject.nakamura.api.lite.PropertyMigrator;
import org.sakaiproject.nakamura.api.lite.content.Content;
import org.sakaiproject.nakamura.lite.BaseMemoryRepository;
import org.sakaiproject.nakamura.lite.SessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MigrationLoggerTest {

    private SessionImpl session;

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationLoggerTest.class);

    private PropertyMigrator migrator = new PropertyMigrator() {
        public boolean migrate(String rowID, Map<String, Object> properties) {
            return false;
        }

        public boolean verify(String rowID, Map<String, Object> beforeProperties, Map<String, Object> afterProperties) {
            return false;
        }

        public Integer getOrder() {
            return null;
        }
    };

    @Before
    public void setUp() throws Exception {
        BaseMemoryRepository baseMemoryRepository = new BaseMemoryRepository();
        this.session = (SessionImpl) baseMemoryRepository.getRepository().loginAdministrative();

    }

    @SuppressWarnings({"unchecked"})
    @Test
    public void testLogAndWrite() throws Exception {
        MigrationLogger migrationLogger = new MigrationLogger(this.session);
        migrationLogger.log(migrator);

        migrationLogger.write(session);
        Content logContent = session.getContentManager().get(MigrationLogger.LOG_PATH);
        LOGGER.info(logContent.toString());
        Map<String, Object> classLog = (Map<String, Object>) logContent.getProperty(this.migrator.getClass().getName());
        Assert.assertNotNull(classLog);
        Assert.assertNotNull(classLog.get(MigrationLogger.DATE_READABLE));
        Assert.assertNotNull(classLog.get(MigrationLogger.DATE_MS));
        Assert.assertTrue(migrationLogger.hasMigratorRun(this.migrator));

        // try a second log-and-write cycle to make sure old data stays
        migrationLogger.log(migrator);
        migrationLogger.write(session);
        logContent = session.getContentManager().get(MigrationLogger.LOG_PATH);
        LOGGER.info(logContent.toString());
        Assert.assertTrue(migrationLogger.hasMigratorRun(this.migrator));
    }
}
