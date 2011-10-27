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
package org.sakaiproject.nakamura.lite.accesscontrol;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.sakaiproject.nakamura.api.lite.accesscontrol.Permissions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class PropertyAcl  implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3998584870894631478L;
    private Set<String> readDenied;
    private Set<String> writeDenied;

    public PropertyAcl(Map<String, Integer> denies) {
        Set<String> r = Sets.newHashSet();
        Set<String> w = Sets.newHashSet();
        for (Entry<String, Integer> ace : denies.entrySet()) {
            if ((Permissions.CAN_READ_PROPERTY.getPermission() & ace.getValue()) == Permissions.CAN_READ_PROPERTY
                    .getPermission()) {
                r.add(ace.getKey());
            }
            if ((Permissions.CAN_WRITE_PROPERTY.getPermission() & ace.getValue()) == Permissions.CAN_WRITE_PROPERTY
                    .getPermission()) {
                w.add(ace.getKey());
            }
        }
        readDenied = ImmutableSet.copyOf(r.toArray(new String[r.size()]));
        writeDenied = ImmutableSet.copyOf(w.toArray(new String[w.size()]));
    }

    public PropertyAcl() {
        readDenied = ImmutableSet.of();
        writeDenied = ImmutableSet.of();
    }

    public Set<String> readDeniedSet() {
        return readDenied;
    }

    public boolean canWrite(Object key) {
        return !writeDenied.contains(key);
    }

}
