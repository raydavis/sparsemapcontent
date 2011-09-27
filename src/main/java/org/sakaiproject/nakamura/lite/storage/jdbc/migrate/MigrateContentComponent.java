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

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.sakaiproject.nakamura.api.lite.MigrationService;
import org.sakaiproject.nakamura.api.lite.StorageClientUtils;
import org.sakaiproject.nakamura.lite.ManualOperationService;

import java.util.Map;

@SuppressWarnings({"UnusedParameters"})
@Component(immediate = true, enabled = false, metatype = true)
@Service(value = ManualOperationService.class)
public class MigrateContentComponent implements ManualOperationService {

    @Property(boolValue = true)
    private static final String DRY_RUN_PROPERTY = "dryRun";

    @Property(boolValue = true)
    private static final String VERIFY_PROPERTY = "verify";

    boolean dryRun = true;

    boolean verify = false;

    @Reference
    private MigrationService migrationService;

    @Activate
    public void activate(Map<String, Object> properties) throws Exception {
        this.dryRun = StorageClientUtils.getSetting(properties.get(DRY_RUN_PROPERTY), true);
        this.verify = StorageClientUtils.getSetting(properties.get(VERIFY_PROPERTY), false);
        this.migrationService.doMigration(this.dryRun, this.verify);
    }

}
