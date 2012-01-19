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
package org.sakaiproject.nakamura.lite;

import java.io.IOException;
import java.util.Map;


import org.junit.Assert;
import org.junit.Test;
import org.sakaiproject.nakamura.lite.content.InternalContentAccess;

import com.google.common.collect.ImmutableMap;

public class ConfigurationImplTest {
    @Test
    public void testProperties() throws IOException {
        InternalContentAccess.resetInternalContent();
        ConfigurationImpl configurationImpl = new ConfigurationImpl();
        Map<String,Object> properties = ImmutableMap.of(ConfigurationImpl.INDEX_COLUMN_NAMES, (Object)ConfigurationImpl.DEFAULT_INDEX_COLUMN_NAMES);
        configurationImpl.activate(properties);
        Assert.assertEquals("n",configurationImpl.getKeySpace());
    }
    @Test
    public void testPropertiesOSGiOverride() throws IOException {
        InternalContentAccess.resetInternalContent();
        ConfigurationImpl configurationImpl = new ConfigurationImpl();
        Map<String,Object> properties = ImmutableMap.of(ConfigurationImpl.INDEX_COLUMN_NAMES,(Object)"somethingElse");
        configurationImpl.activate(properties);
        Assert.assertArrayEquals(new String[]{"somethingElse"}, configurationImpl.getIndexColumnNames());
    }
    @Test
    public void testPropertiesSharedOverride() throws IOException {
        InternalContentAccess.resetInternalContent();
        ConfigurationImpl configurationImpl = new ConfigurationImpl();
        System.setProperty(ConfigurationImpl.SHAREDCONFIGPROPERTY, "src/test/resources/testsharedoverride.properties");
        Map<String,Object> properties = ImmutableMap.of(ConfigurationImpl.INDEX_COLUMN_NAMES, (Object)ConfigurationImpl.DEFAULT_INDEX_COLUMN_NAMES);
        configurationImpl.activate(properties);
        System.clearProperty(ConfigurationImpl.SHAREDCONFIGPROPERTY);
        Assert.assertArrayEquals(new String[]{"somethingElseFromProperties"}, configurationImpl.getIndexColumnNames());
    }
    @Test
    public void testPropertiesSharedOverrideOSGi() throws IOException {
        InternalContentAccess.resetInternalContent();
        ConfigurationImpl configurationImpl = new ConfigurationImpl();
        System.setProperty(ConfigurationImpl.SHAREDCONFIGPROPERTY, "src/test/resources/testsharedoverride.properties");
        Map<String,Object> properties = ImmutableMap.of(ConfigurationImpl.INDEX_COLUMN_NAMES,(Object)"somethingElse");
        configurationImpl.activate(properties);
        System.clearProperty(ConfigurationImpl.SHAREDCONFIGPROPERTY);
        Assert.assertArrayEquals(new String[]{"somethingElse"}, configurationImpl.getIndexColumnNames());
    }
}
