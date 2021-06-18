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
package no.digipost.signature.jaxb.spring;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.stream.StreamResult;

import java.io.CharArrayWriter;

import static no.digipost.DiggBase.friendlyName;
import static no.digipost.DiggExceptions.exceptionNameAndMessage;
import static org.javers.core.JaversBuilder.javers;

final class MarshallingMatchers {

    private final Jaxb2Marshaller marshaller;
    private final Javers javers;

    public MarshallingMatchers(Jaxb2Marshaller marshaller) {
        this.marshaller = marshaller;
        this.javers = javers().build();
    }


    public <T> Matcher<T> marshallsToXmlAndUnmarshallsBackToJava() {
        return new TypeSafeDiagnosingMatcher<T>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("translates to XML and back to Java");
            }

            @Override
            protected boolean matchesSafely(T item, Description mismatchDescription) {
                try (CharArrayWriter xmlWriter = new CharArrayWriter())  {
                    try {
                        marshaller.marshal(item, new StreamResult(xmlWriter));
                    } catch (Exception e) {
                        mismatchDescription
                            .appendText("Unable to marshall ").appendValue(item).appendText(" to XML, because ")
                            .appendText(exceptionNameAndMessage(e));
                        return false;
                    }
                    String xml = xmlWriter.toString();
                    Object unmarshalled;
                    try {
                        unmarshalled = marshaller.unmarshal(new StringSource(xml));
                    } catch (Exception e) {
                        mismatchDescription
                            .appendValue(item).appendText(" marshalled successfully to XML:\n").appendText(xml)
                            .appendText("\nbut was unable to unmarshall back to ").appendText(friendlyName(item.getClass()))
                            .appendText(", because ").appendText(exceptionNameAndMessage(e));
                            return false;
                    }

                    Diff diff = javers.compare(unmarshalled, item);
                    if (!diff.hasChanges()) {
                        return true;
                    } else {
                        mismatchDescription
                            .appendValue("When marshalling ").appendValue(item).appendText("\nto XML:\n").appendText(xml)
                            .appendText("\nand back, the resulting ").appendText(friendlyName(unmarshalled.getClass()))
                            .appendText(" differed from the expected object:\n").appendValue(diff);
                        return false;
                    }
                }
            }

        };
    }


}
