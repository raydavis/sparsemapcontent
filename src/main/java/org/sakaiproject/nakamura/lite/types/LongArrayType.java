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

public class LongArrayType implements Type<Long[]> {

    public int getTypeId() {
        return 1001;
    }

    public void save(DataOutputStream dos, Object object ) throws IOException {
        Long[] values;
        if ( object instanceof long[] ) {
            // sadly, autoboxing does not work for primitive types.
            long[] primitiveArray = (long[]) object;
            values = new Long[primitiveArray.length];
            for ( int i = 0; i < primitiveArray.length; i++ ) {
                values[i] = primitiveArray[i];
            }
        } else {
            values = (Long[]) object;
        }
        dos.writeInt(values.length);
        for ( Long s : values) {
            dos.writeLong(s);
        }
    }

    public Long[] load(DataInputStream in) throws IOException {
        int l = in.readInt();
        Long[] values = new Long[l];
        for ( int i = 0; i < l; i++ ) {
            values[i] = in.readLong();
        }
        return values;
    }

    public Class<Long[]> getTypeClass() {
        return Long[].class;
    }

    public boolean accepts(Object object) {
        return (object instanceof Long[] || object instanceof long[]);
    }
}
