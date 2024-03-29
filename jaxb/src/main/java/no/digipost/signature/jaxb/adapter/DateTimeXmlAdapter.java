/*
 * Copyright (C) Posten Norge AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.digipost.signature.jaxb.adapter;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

public final class DateTimeXmlAdapter extends XmlAdapter<String, ZonedDateTime> {

    @Override
    public ZonedDateTime unmarshal(final String value) {
        if (value == null) {
            return null;
        }
        Calendar parsed = DatatypeConverter.parseDate(value);
        return ZonedDateTime.ofInstant(parsed.toInstant(), parsed.getTimeZone().toZoneId());
    }

    @Override
    public String marshal(final ZonedDateTime value) {
        if (value == null) {
            return null;
        }

        return DatatypeConverter.printDateTime(GregorianCalendar.from(value));
    }

}
