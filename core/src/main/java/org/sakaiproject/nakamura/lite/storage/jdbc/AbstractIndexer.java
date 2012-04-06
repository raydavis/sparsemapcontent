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

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractIndexer implements Indexer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIndexer.class);
    private Set<String> indexColumns;
    
    public AbstractIndexer(Set<String> indexColumns2) {
        this.indexColumns = indexColumns2;
    }
    
    boolean shouldFind(String keySpace, String columnFamily, String k) {
        String key = columnFamily+":"+k;
        if ( JDBCStorageClient.AUTO_INDEX_COLUMNS.contains(key) || indexColumns.contains(key)) {
            return true;
        } else {
            LOGGER.debug("Ignoring Find operation on {}:{}", columnFamily, k);     
        }
        return false;
    }
    boolean shouldIndex(String keySpace, String columnFamily, String k) {
        String key = columnFamily+":"+k;
        if ( JDBCStorageClient.AUTO_INDEX_COLUMNS.contains(key) || indexColumns.contains(key)) {
            LOGGER.debug("Will Index {}:{}", columnFamily, k);
            return true;
        } else {
            LOGGER.debug("Will Not Index {}:{}", columnFamily, k);
            return false;
        }
    }

}
