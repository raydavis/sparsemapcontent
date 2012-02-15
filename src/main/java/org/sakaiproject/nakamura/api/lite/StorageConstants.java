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
package org.sakaiproject.nakamura.api.lite;

public class StorageConstants {

    /**
     * Property used to select a set of query statements in the finder. These must exist in 
     * the driver configuration and are intended to allow institutions to optimize certain 
     * queries. If not present, a the default set will be used. 
     */
    public static final String CUSTOM_STATEMENT_SET = "_statementset";
    
    /**
     * Property used to set the maximum number of items a query should return per page.
     * The starting row of the query is determined by the page number.
     * Defaults to 25.
     */
    public static final String ITEMS = "_items";
    
    
    /**
     * Page number to start at, defaults to 0.
     */
    public static final String PAGE = "_page";
    
    /**
     * The column on which to perform a sort.
     */
    public static final String SORT = "_sort";

    /**
     * If present Raw Results will be returned as string values for each record.
     */
    public static final String RAWRESULTS = "_rawresults";

    /**
     * If true, then the query cache may be used. key-value pairs of the query must uniquely identify the query, and 
     * cache must be cleared by other means. This is a big ask and requires effort.
     */
    public static final String CACHEABLE = "_cacheable";


}
