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

import com.google.common.collect.ImmutableMap;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sakaiproject.nakamura.api.lite.PropertyMigrator;
import org.sakaiproject.nakamura.api.lite.content.Content;
import org.sakaiproject.nakamura.lite.BaseMemoryRepository;
import org.sakaiproject.nakamura.lite.ConfigurationImpl;
import org.sakaiproject.nakamura.lite.SessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MigrationServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationServiceImplTest.class);

    private MigrationServiceImpl migrationService;

    // Example of writing a sparse storage property migrator.
    // This one renames "prop1" to "newprop1".
    // Note that the migrate method must agree with the verify method.
    private PropertyMigrator migrator = new PropertyMigrator() {
        public boolean migrate(String rowID, Map<String, Object> properties) {
            Object val = properties.get("prop1");
            if (val != null) {
                properties.put("newprop1", val);
                properties.remove("prop1");
                return true;
            }
            return false;
        }

        public boolean verify(String rowID, Map<String, Object> beforeProperties, Map<String, Object> afterProperties) {
            if (beforeProperties.get("prop1") == null) {
                return true;
            }
            Object oldVal = afterProperties.get("prop1");
            Object newVal = afterProperties.get("newprop1");
            return oldVal == null && newVal != null;
        }

        public Integer getOrder() {
            return null; // we don't care what order we run in
        }
    };

    @Before
    public void setUp() throws Exception {
        this.migrationService = new MigrationServiceImpl();
        this.migrationService.migratorTracker = new PropertyMigratorTrackerService();
        BaseMemoryRepository baseMemoryRepository = new BaseMemoryRepository();
        this.migrationService.repository = baseMemoryRepository.getRepository();
        this.migrationService.configuration = new ConfigurationImpl();
        ((ConfigurationImpl) this.migrationService.configuration).activate(new HashMap<String, Object>());
    }

    @Test
    public void doMigration() throws Exception {
        this.migrationService.migratorTracker.bind(this.migrator);

        // add some content
        SessionImpl session = (SessionImpl) this.migrationService.repository.loginAdministrative();
        Content content = new Content("/foo", ImmutableMap.of("prop1", (Object) "value1"));
        session.getContentManager().update(content);
        Content content2 = new Content("/bar", ImmutableMap.of("prop1", (Object) "value2"));
        session.getContentManager().update(content2);
        Content content3 = new Content("/baz", ImmutableMap.of("somethingelse", (Object) "value1"));
        session.getContentManager().update(content3);

        // test the migrate method
        this.migrationService.doMigration(false, true);

        // make sure the content rows have the new values
        Content updated = session.getContentManager().get("/foo");
        Assert.assertEquals("value1", updated.getProperty("newprop1"));
        Assert.assertNull(updated.getProperty("prop1"));
        Content updated2 = session.getContentManager().get("/bar");
        Assert.assertEquals("value2", updated2.getProperty("newprop1"));
        Assert.assertNull(updated2.getProperty("prop1"));
        Content updated3 = session.getContentManager().get("/baz");
        Assert.assertEquals("value1", updated3.getProperty("somethingelse"));
        Assert.assertNull(updated3.getProperty("prop1"));
        Assert.assertNull(updated3.getProperty("newprop1"));

        // and check that it all got logged
        Content log = session.getContentManager().get(MigrationLogger.LOG_ROOT_PATH);
        Assert.assertNotNull(log);
        LOGGER.info(log.toString());
    }

}
