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

import java.util.Set;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.ReferenceStrategy;
import org.apache.felix.scr.annotations.Service;
import org.sakaiproject.nakamura.api.lite.PropertyMigrator;

import com.google.common.collect.Sets;

@Component(immediate = true, metatype = true)
@Service(value = PropertyMigratorTracker.class)
@Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, name = "propertyMigrator", referenceInterface = PropertyMigrator.class, policy = ReferencePolicy.DYNAMIC, strategy = ReferenceStrategy.EVENT, bind = "bind", unbind = "unbind")
public class PropertyMigratorTrackerService implements PropertyMigratorTracker {

    private Set<PropertyMigrator> propertyMigrators = Sets.newHashSet();

    public PropertyMigrator[] getPropertyMigrators() {
        synchronized (propertyMigrators) {
            return propertyMigrators.toArray(new PropertyMigrator[propertyMigrators.size()]);
        }
    }

    public void bind(PropertyMigrator pm) {
        synchronized (propertyMigrators) {
            propertyMigrators.add(pm);
        }
    }

    public void unbind(PropertyMigrator pm) {
        synchronized (propertyMigrators) {
            propertyMigrators.remove(pm);
        }
    }

}
