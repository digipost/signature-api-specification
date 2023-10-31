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
package no.digipost.signature.jaxb;

import no.digipost.signature.api.xml.XMLPortalSignatureJobRequest;
import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static no.digipost.signature.jaxb.SignatureMarshalling.allApiClasses;
import static no.digipost.xml.bind.MarshallingCustomization.onMarshalling;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.co.probablyfine.matchers.Java8Matchers.where;

class BillionLaughsTest {

    private static final String A_BILLION_LAUGHS_DOCTYPE_DECLARATION =
            "<!DOCTYPE lolz [<!ENTITY lol \"lol\"><!ELEMENT lolz (#PCDATA)>\n" +
            "  <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
            "  <!ENTITY lol2 \"&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;\">\n" +
            "  <!ENTITY lol3 \"&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;\">\n" +
            "  <!ENTITY lol4 \"&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;\">\n" +
            "  <!ENTITY lol5 \"&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;\">\n" +
            "  <!ENTITY lol6 \"&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;\">\n" +
            "  <!ENTITY lol7 \"&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;\">\n" +
            "  <!ENTITY lol8 \"&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;\">\n" +
            "  <!ENTITY lol9 \"&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;\">\n" +
            "]>";

    @Test
    void doctypeDeclarationIsDeniedPreventingXxeAttacks() {

        JaxbMarshaller jaxb = new JaxbMarshaller(
                onMarshalling(marshaller -> marshaller.setProperty("com.sun.xml.bind.xmlHeaders", A_BILLION_LAUGHS_DOCTYPE_DECLARATION)),
                allApiClasses());

        String evilPayload = jaxb.marshalToString(new XMLPortalSignatureJobRequest().withReference("heisann")).replace("heisann", "&lol1;");
        assertThat(evilPayload, containsString(A_BILLION_LAUGHS_DOCTYPE_DECLARATION));

        SignatureMarshalException thrown = assertThrows(SignatureMarshalException.class,
                () -> jaxb.unmarshal(evilPayload.getBytes(UTF_8), XMLPortalSignatureJobRequest.class));

        assertThat(thrown, where(Throwable::getMessage, containsString("DOCTYPE is disallowed")));
    }

}
