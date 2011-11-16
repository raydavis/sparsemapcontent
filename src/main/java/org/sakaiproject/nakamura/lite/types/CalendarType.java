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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CalendarType implements Type<Calendar> {

    public int getTypeId() {
        return 4;
    }

    public void save(DataOutputStream dos, Object object) throws IOException {
        Calendar calendar = (Calendar) object;
        dos.writeLong(calendar.getTimeInMillis());
        dos.writeUTF(calendar.getTimeZone().getID());
    }

    public Calendar load(DataInputStream in) throws IOException {
        long millis = in.readLong();
        TimeZone zone = TimeZone.getTimeZone(in.readUTF());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(millis);
        gregorianCalendar.setTimeZone(zone);
        return gregorianCalendar;
    }

    public Class<Calendar> getTypeClass() {
        return Calendar.class;
    }

    public boolean accepts(Object object) {
        return (object instanceof Calendar);
    }

}
