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

import no.digipost.DiggExceptions;
import no.digipost.signature.jaxb.JaxbMarshaller;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.javers.core.Javers;
import org.javers.core.diff.Diff;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;
import static no.digipost.DiggBase.friendlyName;
import static no.digipost.DiggExceptions.causalChainOf;
import static org.javers.core.JaversBuilder.javers;

final class MarshallingMatchers {

    private final JaxbMarshaller marshaller;
    private final Javers javers;

    public MarshallingMatchers(JaxbMarshaller marshaller) {
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
                try (ByteArrayOutputStream xmlWriter = new ByteArrayOutputStream())  {
                    String xml;
                    try {
                        xml = marshaller.marshalToString(item);
                    } catch (Exception e) {
                        mismatchDescription
                            .appendText("Unable to marshall ").appendValue(item).appendText(" to XML, because ")
                            .appendText(causalChainOf(e)
                                .map(DiggExceptions::exceptionNameAndMessage)
                                .collect(joining(", caused by ")));
                        return false;
                    }
                    Object unmarshalled;
                    try {
                        unmarshalled = marshaller.unmarshal(xml.getBytes(UTF_8), item.getClass());
                    } catch (Exception e) {
                        mismatchDescription
                            .appendValue(item).appendText(" marshalled successfully to XML:\n").appendText(xml)
                            .appendText("\nbut was unable to unmarshall back to ").appendText(friendlyName(item.getClass()))
                            .appendText(", because ")
                            .appendText(causalChainOf(e)
                                .map(DiggExceptions::exceptionNameAndMessage)
                                .collect(joining(", caused by ")));
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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        };
    }


}
