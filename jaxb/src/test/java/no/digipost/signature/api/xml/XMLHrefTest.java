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
package no.digipost.signature.api.xml;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.net.URLDecoder;

import static uk.co.probablyfine.matchers.Java8Matchers.where;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XMLHrefTest {

    @Test
    void correctEqualsAndHashCode() {
        EqualsVerifier.forClass(XMLHref.class).verify();
    }

    @Test
    void trivialStringsProduceSameUrlEncodedString() {
        String href = "abc";
        assertThat(XMLHref.of(href), where(XMLHref::asUrlEncodedString, is(href)));
    }

    @Test
    void alreadyEncodedHrefStringProducesSameUrlEncodedString() {
        String urlEncodedString = "http%3A%2F%2Furl.com";
        assertThat(XMLHref.of(urlEncodedString), where(XMLHref::asUrlEncodedString, is(urlEncodedString)));
    }

    @Test
    void producesUrlEncodedHrefString() {
        assertThat(XMLHref.of("http://url.com"), where(XMLHref::asUrlEncodedString, is("http%3A%2F%2Furl.com")));
    }

    @Test
    void handlesNonEncodedHrefsWithCharactersThatWouldBeIllegallyEncoded() {
        String nonEncodedWithIllegalCharacters = "% B";
        assertDoesNotThrow(() -> XMLHref.of(nonEncodedWithIllegalCharacters));

        assertThrows(IllegalArgumentException.class, () -> URLDecoder.decode(nonEncodedWithIllegalCharacters, "UTF-8"),
                "sanity check that the string is actually a non-encoded String which would not make sense to decode");
    }


}
