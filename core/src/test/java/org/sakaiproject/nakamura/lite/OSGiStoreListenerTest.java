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
package org.sakaiproject.nakamura.lite;

import com.google.common.collect.ImmutableMap;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.service.event.EventAdmin;
import org.sakaiproject.nakamura.api.lite.accesscontrol.Security;

import java.util.Map;

public class OSGiStoreListenerTest {

    @Mock
    private EventAdmin eventAdmin;

    public OSGiStoreListenerTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() {
        OSGiStoreListener l = new OSGiStoreListener();
        l.eventAdmin = eventAdmin;
        Map<String, Object> testMap = ImmutableMap.of("test", (Object) new String[]{"an", "array"});
        for (String zone : new String[] { Security.ADMIN_AUTHORIZABLES, Security.ADMIN_GROUPS,
                Security.ADMIN_USERS, Security.ZONE_ADMIN, Security.ZONE_AUTHORIZABLES,
                Security.ZONE_CONTENT }) {
            l.onDelete(zone, "path", "user", "x", null);
            l.onDelete(zone, "path", "user", null, testMap, (String[]) null);
            l.onDelete(zone, "path", "user", "x", null, "xx");
            l.onDelete(zone, "path", "user", null, testMap, "x:x");
            l.onDelete(zone, null, "user", "x", null, "x:x", "x:x");
            l.onUpdate(zone, "path", "user", null, true, null);
            l.onUpdate(zone, "path", "user", "x", false, testMap, (String[]) null);
            l.onUpdate(zone, "path", "user", null, true, null, "xx");
            l.onUpdate(zone, "path", "user", "x", false, testMap, "x:x");
            l.onUpdate(zone, null, "user", null, true, null, "x:x", "x:x");
        }
        l.onLogin("userId", "sessionId");
        l.onLogout("userId", "sessoionID");
    }
}
