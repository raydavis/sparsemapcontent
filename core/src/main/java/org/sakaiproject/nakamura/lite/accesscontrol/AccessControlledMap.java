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
package org.sakaiproject.nakamura.lite.accesscontrol;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class AccessControlledMap<K, V> extends HashMap<K, V> {


    private PropertyAcl propertyAcl;

    public AccessControlledMap(PropertyAcl propertyAcl) {
        this.propertyAcl = propertyAcl;
    }
    /**
     * 
     */
    private static final long serialVersionUID = -6550830558631198709L;

    @Override
    public V put(K key, V value) {
        if ( propertyAcl.canWrite(key)) {
            return super.put(key, value);
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for ( Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }


}
