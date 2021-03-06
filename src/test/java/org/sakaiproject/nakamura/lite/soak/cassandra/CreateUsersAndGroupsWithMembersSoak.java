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
package org.sakaiproject.nakamura.lite.soak.cassandra;

import com.google.common.collect.ImmutableMap;

import org.sakaiproject.nakamura.api.lite.ClientPoolException;
import org.sakaiproject.nakamura.api.lite.StorageClientException;
import org.sakaiproject.nakamura.api.lite.StorageClientUtils;
import org.sakaiproject.nakamura.api.lite.accesscontrol.AccessDeniedException;
import org.sakaiproject.nakamura.lite.soak.AbstractSoakController;
import org.sakaiproject.nakamura.lite.soak.authorizable.CreateUsersAndGroupsWithMembersClient;
import org.sakaiproject.nakamura.lite.storage.StorageClientPool;
import org.sakaiproject.nakamura.lite.storage.cassandra.CassandraClientPool;

public class CreateUsersAndGroupsWithMembersSoak extends AbstractSoakController {

    private int totalUsers;
    private StorageClientPool connectionPool;
    private int totalGroups;

    public CreateUsersAndGroupsWithMembersSoak(int totalUsers, int totalGroups,
            StorageClientPool connectionPool) {
        super(totalUsers + (totalGroups * 5));
        this.connectionPool = connectionPool;
        this.totalUsers = totalUsers;
        this.totalGroups = totalGroups;
    }

    protected Runnable getRunnable(int nthreads) throws ClientPoolException,
            StorageClientException, AccessDeniedException {
        int usersPerThread = totalUsers / nthreads;
        int groupsPerThread = totalGroups / nthreads;
        return new CreateUsersAndGroupsWithMembersClient(usersPerThread, groupsPerThread,
                connectionPool);
    }

    public static void main(String[] argv) throws ClientPoolException, StorageClientException,
            AccessDeniedException, ClassNotFoundException {

        int totalUsers = 1000;
        int totalGroups = 100;
        int nthreads = 10;

        if (argv.length > 0) {
            nthreads = StorageClientUtils.getSetting(Integer.valueOf(argv[0]), nthreads);
        }
        if (argv.length > 1) {
            totalUsers = StorageClientUtils.getSetting(Integer.valueOf(argv[1]), totalUsers);
        }
        if (argv.length > 2) {
            totalGroups = StorageClientUtils.getSetting(Integer.valueOf(argv[2]), totalUsers);
        }

        CreateUsersAndGroupsWithMembersSoak createUsersAndGroupsSoak = new CreateUsersAndGroupsWithMembersSoak(
                totalUsers, totalGroups, getConnectionPool());
        createUsersAndGroupsSoak.launchSoak(nthreads);
    }

    protected static StorageClientPool getConnectionPool() throws ClassNotFoundException {
        CassandraClientPool cp = new CassandraClientPool();
        cp.activate(ImmutableMap.of("test", (Object) "test"));
        return cp;
    }

}
