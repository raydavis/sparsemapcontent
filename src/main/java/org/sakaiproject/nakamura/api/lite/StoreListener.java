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

import java.util.Map;

/**
 * The StorageListener is notified when actions are performed objects in storage.
 */
public interface StoreListener {
    public static final String TOPIC_BASE = "org/sakaiproject/nakamura/lite/";
    public static final String DELETE_TOPIC = "DELETE";
    public static final String ADDED_TOPIC = "ADDED";
    public static final String UPDATED_TOPIC = "UPDATED";
    public static final String DEFAULT_DELETE_TOPIC = TOPIC_BASE + DELETE_TOPIC;
    public static final String DEFAULT_CREATE_TOPIC = TOPIC_BASE + ADDED_TOPIC;
    public static final String DEFAULT_UPDATE_TOPIC = TOPIC_BASE + UPDATED_TOPIC;
    public static final String[] DEFAULT_TOPICS = new String[] { DEFAULT_CREATE_TOPIC,
            DEFAULT_UPDATE_TOPIC, DEFAULT_DELETE_TOPIC, 
            TOPIC_BASE + "authorizables/" + DELETE_TOPIC,
            TOPIC_BASE + "groups/" + DELETE_TOPIC, 
            TOPIC_BASE + "users/" + DELETE_TOPIC, 
            TOPIC_BASE + "admin/" + DELETE_TOPIC,
            TOPIC_BASE + "authorizables/" + DELETE_TOPIC, 
            TOPIC_BASE + "content/" + DELETE_TOPIC,
            TOPIC_BASE + "authorizables/"+ADDED_TOPIC,
            TOPIC_BASE + "groups/"+ADDED_TOPIC,
            TOPIC_BASE + "users/"+ADDED_TOPIC,
            TOPIC_BASE + "admin/"+ADDED_TOPIC,
            TOPIC_BASE + "authorizables/"+ADDED_TOPIC, 
            TOPIC_BASE + "content/"+ADDED_TOPIC,
            TOPIC_BASE + "authorizables/"+UPDATED_TOPIC, 
            TOPIC_BASE + "groups/"+UPDATED_TOPIC,
            TOPIC_BASE + "users/"+UPDATED_TOPIC,
            TOPIC_BASE + "admin/"+UPDATED_TOPIC,
            TOPIC_BASE + "authorizables/"+UPDATED_TOPIC, 
            TOPIC_BASE + "content/"+UPDATED_TOPIC };
    public static final String USERID_PROPERTY = "userid";
    public static final String PATH_PROPERTY = "path";
    public static final String RESOURCE_TYPE_PROPERTY = "resourceType";
    public static final String BEFORE_EVENT_PROPERTY = "_beforeEvent";

    /**
     * onDelete is called after an object has been deleted.
     * @param zone an identifier for the type of object being acted upon
     * @param path the path to the object
     * @param user the user logged in causing this action
     * @param resourceType the resource type of the item, if known.
     * @param beforeEvent the properties of the object before it was deleted
     * @param attributes properties of the event itself
     */
    void onDelete(String zone, String path, String user, String resourceType, Map<String, Object> beforeEvent, String... attributes);

    /**
     * onUpdate is called after an object has been updated.
     * @param zone an identifier for the type of object being acted upon
     * @param path the path to the object
     * @param user the user logged in causing this action
     * @param resourceType the resource type of the item, if known.
     * @param beforeEvent the properties of the object before it was updated
     * @param attributes properties of the event itself
     */
    void onUpdate(String zone, String path, String user, String resourceType, boolean isNew,  Map<String, Object> beforeEvent, String... attributes);

    /**
     * onLogin is called when a user logs in and creates a new {@link Session}
     * @param userid
     * @param sessionID
     */
    void onLogin(String userid, String sessionID);

    /**
     * onLogout is called when a user logs out of their {@link Session}
     * @param userid
     * @param sessionID
     */
    void onLogout(String userid, String sessionID);

}
