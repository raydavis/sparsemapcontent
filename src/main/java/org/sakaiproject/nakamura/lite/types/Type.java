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
package org.sakaiproject.nakamura.lite.types;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Type<T> {

    /**
     * @return the type ID of this type. Once a type ID has been assigned to an
     *         object type it can never be reused.
     */
    int getTypeId();

    /**
     * Safe the type to a data output stream
     * @param dos
     * @param o
     * @throws IOException
     */
    void save(DataOutputStream dos, Object o) throws IOException;

    /**
     * Load the type from a data output stream
     * @param in
     * @return
     * @throws IOException
     */
    T load(DataInputStream in) throws IOException;

    /**
     * @return get the class of the type
     */
    Class<T> getTypeClass();

    /**
     * return true if the Type can save the object.
     * @param object
     * @return
     */
    boolean accepts(Object object);

}
