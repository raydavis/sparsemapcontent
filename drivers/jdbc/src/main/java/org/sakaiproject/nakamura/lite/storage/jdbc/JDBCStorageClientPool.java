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
package org.sakaiproject.nakamura.lite.storage.jdbc;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.sakaiproject.nakamura.lite.storage.spi.StorageClientPool;

/**
 * A concrete stub for a generic JDBC storage client pool component. The super on its
 * own has all the components necessary to be a generic JDBC driver on its own, therefore we
 * just need to activate the OSGi component properties.
 */
@Component(immediate = true, metatype = true, inherit = true)
@Service(value = StorageClientPool.class)
public class JDBCStorageClientPool extends BaseJDBCStorageClientPool {

}
