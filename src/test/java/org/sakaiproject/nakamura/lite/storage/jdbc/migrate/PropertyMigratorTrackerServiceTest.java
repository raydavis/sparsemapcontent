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

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.sakaiproject.nakamura.api.lite.PropertyMigrator;

public class PropertyMigratorTrackerServiceTest extends Assert {

    private PropertyMigratorTrackerService tracker;

    @Mock
    PropertyMigrator firstMockMigrator;

    @Mock
    PropertyMigrator secondMockMigrator;

    @Mock
    PropertyMigrator nullMockMigrator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.tracker = new PropertyMigratorTrackerService();
        Mockito.when(firstMockMigrator.getOrder()).thenReturn(0);
        Mockito.when(secondMockMigrator.getOrder()).thenReturn(10);
        Mockito.when(nullMockMigrator.getOrder()).thenReturn(null);

    }

    @Test
    public void getPropertyMigrators() {
        assertTrue(this.tracker.getPropertyMigrators().length == 0);
    }

    @Test
    public void bind() {
        this.tracker.bind(firstMockMigrator);
        assertFalse(this.tracker.getPropertyMigrators().length == 0);
    }

    @Test
    public void bindAndSort() {
        // add to tracker in unsorted order
        this.tracker.bind(nullMockMigrator);
        this.tracker.bind(secondMockMigrator);
        this.tracker.bind(firstMockMigrator);

        // see if tracker keeps them in sorted order
        PropertyMigrator[] migrators = this.tracker.getPropertyMigrators();
        assertEquals(firstMockMigrator, migrators[0]);
        assertEquals(secondMockMigrator, migrators[1]);
        assertEquals(nullMockMigrator, migrators[2]);
    }

    @Test
    public void unbind() {
        this.tracker.bind(nullMockMigrator);
        this.tracker.bind(secondMockMigrator);

        assertEquals(2, this.tracker.getPropertyMigrators().length);

        this.tracker.unbind(nullMockMigrator);
        assertEquals(1, this.tracker.getPropertyMigrators().length);
        assertEquals(this.secondMockMigrator, this.tracker.getPropertyMigrators()[0]);
    }
}
