/**
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

import no.digipost.signature.api.xml.XMLAvailability;
import no.digipost.signature.api.xml.XMLDirectDocument;
import no.digipost.signature.api.xml.XMLDirectSignatureJobManifest;
import no.digipost.signature.api.xml.XMLDirectSignatureJobRequest;
import no.digipost.signature.api.xml.XMLDirectSignatureJobStatusResponse;
import no.digipost.signature.api.xml.XMLDirectSigner;
import no.digipost.signature.api.xml.XMLEnabled;
import no.digipost.signature.api.xml.XMLExitUrls;
import no.digipost.signature.api.xml.XMLHref;
import no.digipost.signature.api.xml.XMLLegacyDirectDocument;
import no.digipost.signature.api.xml.XMLLegacyPortalDocument;
import no.digipost.signature.api.xml.XMLNotificationsUsingLookup;
import no.digipost.signature.api.xml.XMLPortalDocument;
import no.digipost.signature.api.xml.XMLPortalSignatureJobManifest;
import no.digipost.signature.api.xml.XMLPortalSignatureJobRequest;
import no.digipost.signature.api.xml.XMLPortalSigner;
import no.digipost.signature.api.xml.XMLSender;
import no.digipost.signature.api.xml.XMLSignerStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static co.unruly.matchers.Java8Matchers.where;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Arrays.asList;
import static no.digipost.signature.api.xml.XMLAuthenticationLevel.FOUR;
import static no.digipost.signature.api.xml.XMLAuthenticationLevel.THREE;
import static no.digipost.signature.api.xml.XMLDirectSignerStatusValue.SIGNED;
import static no.digipost.signature.api.xml.XMLIdentifierInSignedDocuments.PERSONAL_IDENTIFICATION_NUMBER_AND_NAME;
import static no.digipost.signature.api.xml.XMLStatusRetrievalMethod.WAIT_FOR_CALLBACK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SignatureJaxb2MarshallerTest {

    private static final Jaxb2Marshaller marshaller = SignatureJaxb2Marshaller.ForAllApis.singleton();
    private static final MarshallingMatchers marshalling = new MarshallingMatchers(marshaller);


    private final XMLSender sender = new XMLSender().withOrganizationNumber("123456789");


    @Nested
    class DirectApi {

        final XMLDirectSigner directSigner = new XMLDirectSigner().withPersonalIdentificationNumber("12345678910");

        @Test
        void directjob_is_marshalled() {
            XMLExitUrls exitUrls = new XMLExitUrls()
                    .withCompletionUrl(URI.create("http://localhost/signed"))
                    .withRejectionUrl(URI.create("http://localhost/rejected"))
                    .withErrorUrl(URI.create("http://localhost/failed"));
            XMLDirectSignatureJobRequest directJob = new XMLDirectSignatureJobRequest("123abc", exitUrls, null, null);

            assertThat(directJob, marshalling.marshallsToXmlAndUnmarshallsBackToJava());
        }

        @Test
        void direct_manifest_is_marshalled() {
            XMLDirectDocument directDocument = new XMLDirectDocument("Document title", XMLHref.of("document.pdf"), "application/pdf");
            XMLDirectSignatureJobManifest directManifest = new XMLDirectSignatureJobManifest(
                    asList(directSigner), sender, null, "Title", "Description", asList(directDocument), THREE, PERSONAL_IDENTIFICATION_NUMBER_AND_NAME);

            assertThat(directManifest, marshalling.marshallsToXmlAndUnmarshallsBackToJava());
        }

        @Test
        void legacy_direct_manifest_is_marshalled() {
            XMLLegacyDirectDocument legacyDocument = new XMLLegacyDirectDocument("Title", "description", XMLHref.of("document.pdf"), "application/pdf");
            XMLDirectSignatureJobManifest legacyManifest = new XMLDirectSignatureJobManifest(
                    asList(directSigner), sender, legacyDocument, null, null, null, THREE, PERSONAL_IDENTIFICATION_NUMBER_AND_NAME);

            assertThat(legacyManifest, marshalling.marshallsToXmlAndUnmarshallsBackToJava());
        }

        @Test
        void invalid_manifest_causes_exceptions() {
            XMLDirectDocument directDocument = new XMLDirectDocument("Document title", null, "application/pdf");
            XMLDirectSignatureJobManifest directManifest = new XMLDirectSignatureJobManifest(
                    asList(directSigner), sender, null, "Title", null, asList(directDocument), FOUR, PERSONAL_IDENTIFICATION_NUMBER_AND_NAME);


            MarshallingFailureException marshallingFailure =
                    assertThrows(MarshallingFailureException.class, () -> marshaller.marshal(directManifest, new StreamResult(new ByteArrayOutputStream())));

            assertThat(marshallingFailure, where(Exception::getMessage, allOf(containsString("href"), containsString("must appear"))));
        }

        @Test
        void invalid_signature_job_request_causes_exceptions() {
            XMLExitUrls exitUrls = new XMLExitUrls()
                    .withCompletionUrl(null)
                    .withRejectionUrl(URI.create("http://localhost/rejected"))
                    .withErrorUrl(URI.create("http://localhost/failed"));

            XMLDirectSignatureJobRequest signatureJobRequest = new XMLDirectSignatureJobRequest("123abc", exitUrls, WAIT_FOR_CALLBACK, null);

            MarshallingFailureException thrown = assertThrows(MarshallingFailureException.class,
                    () -> marshaller.marshal(signatureJobRequest, new StreamResult(new ByteArrayOutputStream())));

            assertThat(thrown, where(Exception::getMessage, allOf(containsString("completion-url"), containsString("is expected"))));
        }

    }


    @Nested
    class PortalApi {

        final XMLPortalSigner portalSigner = new XMLPortalSigner()
                .withPersonalIdentificationNumber("12345678910")
                .withNotificationsUsingLookup(new XMLNotificationsUsingLookup().withEmail(new XMLEnabled()));
        final XMLAvailability availability = new XMLAvailability().withActivationTime(ZonedDateTime.now(ZoneId.of("GMT")).truncatedTo(MILLIS));


        @Test
        void portaljob_is_marshalled() {
            assertThat(new XMLPortalSignatureJobRequest("123abc", null), marshalling.marshallsToXmlAndUnmarshallsBackToJava());
        }

        @Test
        void portal_manifest_is_marshalled() {
            XMLPortalDocument portalDocument = new XMLPortalDocument("Title", XMLHref.of("document.pdf"), "application/pdf");

            XMLPortalSignatureJobManifest portalManifest = new XMLPortalSignatureJobManifest(
                    asList(portalSigner), sender, null, "Title", "Non-sensitive title", "Description", asList(portalDocument), FOUR, availability, PERSONAL_IDENTIFICATION_NUMBER_AND_NAME);
            assertThat(portalManifest, marshalling.marshallsToXmlAndUnmarshallsBackToJava());
        }


        @Test
        void legacy_portal_manifest_is_marshalled() {
            XMLLegacyPortalDocument legacyDocument = new XMLLegacyPortalDocument("Title", "Non-sensitive title", "Description", XMLHref.of("document.pdf"), "application/pdf");

            XMLPortalSignatureJobManifest legacyManifest = new XMLPortalSignatureJobManifest(
                    asList(portalSigner), sender, legacyDocument, null, null, null, null, FOUR, availability, PERSONAL_IDENTIFICATION_NUMBER_AND_NAME);

            assertThat(legacyManifest, marshalling.marshallsToXmlAndUnmarshallsBackToJava());
        }

        @Test
        public void invalid_manifest_causes_exceptions() {
            XMLPortalSigner portalSigner = new XMLPortalSigner().withPersonalIdentificationNumber("12345678910");
            XMLPortalDocument portalDocument = new XMLPortalDocument("Title", null, "application/pdf");
            XMLPortalSignatureJobManifest portalManifest = new XMLPortalSignatureJobManifest(
                    asList(portalSigner), sender, null, "Title", "nonsensitive title", "Description", asList(portalDocument) , FOUR, new XMLAvailability(), PERSONAL_IDENTIFICATION_NUMBER_AND_NAME);

            MarshallingFailureException marshallingFailure =
                    assertThrows(MarshallingFailureException.class, () -> marshaller.marshal(portalManifest, new StreamResult(new ByteArrayOutputStream())));

            assertThat(marshallingFailure, where(Exception::getMessage, allOf(containsString("signature-type"), containsString("notifications-using-lookup"), containsString("notifications"))));
        }

    }



    @Test
    void response_unmarshaller_ignores_unexpected_elements_in_response() throws Exception {
        XMLDirectSignatureJobStatusResponse unmarshalled;
        try (InputStream responseWithUnknownElement = getClass().getResourceAsStream("/xml/direct_signature_job_response_with_unexpected_element.xml")) {
            unmarshalled = (XMLDirectSignatureJobStatusResponse) SignatureJaxb2Marshaller.ForResponsesOfAllApis.singleton()
                    .unmarshal(new StreamSource(responseWithUnknownElement));
        }

        assertThat(unmarshalled, where(XMLDirectSignatureJobStatusResponse::getSignatureJobId, is(1L)));
        assertThat(unmarshalled.getStatuses(), contains(where(XMLSignerStatus::getValue, sameInstance(SIGNED))));
    }

}
