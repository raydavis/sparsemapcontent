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
