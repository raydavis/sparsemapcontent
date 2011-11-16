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
package org.sakaiproject.nakamura.lite.content;

import org.sakaiproject.nakamura.api.lite.StorageClientException;
import org.sakaiproject.nakamura.api.lite.StorageClientUtils;
import org.sakaiproject.nakamura.api.lite.accesscontrol.AccessDeniedException;
import org.sakaiproject.nakamura.api.lite.accesscontrol.PrincipalTokenResolver;
import org.sakaiproject.nakamura.api.lite.content.Content;
import org.sakaiproject.nakamura.api.lite.content.ContentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import java.util.List;

public class PathPrincipalTokenResolver implements PrincipalTokenResolver,
        ChainingPrincipalTokenResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(PathPrincipalTokenResolver.class);
    private ContentManager contentManager;
    private PrincipalTokenResolver nextTokenResolver;
    private String tokenPath;

    public PathPrincipalTokenResolver(String tokenPath, ContentManager contentManager) {
        this.contentManager = contentManager;
        this.tokenPath = tokenPath;
    }

    public List<Content> resolveTokens(String principal) {
        List<Content> tokens = Lists.newArrayList();
        try {
            Content token = contentManager.get(StorageClientUtils.newPath(tokenPath, principal));
            if (token != null) {
                tokens.add(token);
            }
        } catch (AccessDeniedException e) {
            LOGGER.warn("Unable to get token for user " + e.getMessage());
        } catch (StorageClientException e) {
            LOGGER.warn("Unable to get token for user " + e.getMessage(), e);
        }
        if (nextTokenResolver != null) {
            tokens.addAll(nextTokenResolver.resolveTokens(principal));
        }
        return tokens;
    }

    public void setNextTokenResovler(PrincipalTokenResolver nextTokenResolver) {
        this.nextTokenResolver = nextTokenResolver;
    }

    public void clearNextTokenResolver() {
        this.nextTokenResolver = null;
    }

}
