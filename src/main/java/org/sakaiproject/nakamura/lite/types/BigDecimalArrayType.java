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
package org.sakaiproject.nakamura.lite.types;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalArrayType implements Type<BigDecimal[]> {

    public int getTypeId() {
        return 1006;
    }

    public void save(DataOutputStream dos, Object object) throws IOException {
        BigDecimal[] values = (BigDecimal[]) object;
        dos.writeInt(values.length);
        for ( BigDecimal s : values) {
            dos.writeUTF(s.toString());
        }
    }

    public BigDecimal[] load(DataInputStream in) throws IOException {
        int l = in.readInt();
        BigDecimal[] values = new BigDecimal[l];
        for ( int i = 0; i < l; i++ ) {
            values[i] = new BigDecimal(in.readUTF());
        }
        return values;
    }

    public Class<BigDecimal[]> getTypeClass() {
        return BigDecimal[].class;
    }

    public boolean accepts(Object object) {
        return (object instanceof BigDecimal[]);
    }

}
