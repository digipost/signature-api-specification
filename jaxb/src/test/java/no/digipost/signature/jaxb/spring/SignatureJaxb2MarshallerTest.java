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
import no.digipost.signature.api.xml.XMLNotificationsUsingLookup;
import no.digipost.signature.api.xml.XMLPortalDocument;
import no.digipost.signature.api.xml.XMLPortalSignatureJobManifest;
import no.digipost.signature.api.xml.XMLPortalSignatureJobRequest;
import no.digipost.signature.api.xml.XMLPortalSigner;
import no.digipost.signature.api.xml.XMLSender;
import no.digipost.signature.api.xml.XMLSignerStatus;
import org.junit.jupiter.api.Test;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.time.ZonedDateTime;

import static co.unruly.matchers.Java8Matchers.where;
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

public class SignatureJaxb2MarshallerTest {

    private final Jaxb2Marshaller marshaller = SignatureJaxb2Marshaller.ForAllApis.singleton();

    @Test
    public void valid_objects_can_be_marshalled() {
        XMLSender sender = new XMLSender().withOrganizationNumber("123456789");
        XMLPortalSigner portalSigner = new XMLPortalSigner().withPersonalIdentificationNumber("12345678910").withNotificationsUsingLookup(new XMLNotificationsUsingLookup().withEmail(new XMLEnabled()));
        XMLDirectSigner directSigner = new XMLDirectSigner().withPersonalIdentificationNumber("12345678910");
        XMLPortalDocument portalDocument = new XMLPortalDocument("Title", XMLHref.of("document.pdf"), "application/pdf");
        XMLDirectDocument directDocument = new XMLDirectDocument("Title", null, XMLHref.of("document.pdf"), "application/pdf");
        XMLExitUrls exitUrls = new XMLExitUrls()
                .withCompletionUrl(URI.create("http://localhost/signed"))
                .withRejectionUrl(URI.create("http://localhost/rejected"))
                .withErrorUrl(URI.create("http://localhost/failed"));

        XMLDirectSignatureJobRequest directJob = new XMLDirectSignatureJobRequest("123abc", exitUrls, null, null);
        XMLDirectSignatureJobManifest directManifest = new XMLDirectSignatureJobManifest(asList(directSigner), sender, directDocument, THREE, PERSONAL_IDENTIFICATION_NUMBER_AND_NAME);

        marshaller.marshal(directJob, new StreamResult(new ByteArrayOutputStream()));
        marshaller.marshal(directManifest, new StreamResult(new ByteArrayOutputStream()));

        XMLPortalSignatureJobRequest portalJob = new XMLPortalSignatureJobRequest("123abc", null);
        XMLPortalSignatureJobManifest portalManifest = new XMLPortalSignatureJobManifest(asList(portalSigner), sender, null, "Title", "Non-sensitive title", "Message", asList(portalDocument), FOUR, new XMLAvailability().withActivationTime(ZonedDateTime.now()), PERSONAL_IDENTIFICATION_NUMBER_AND_NAME);
        marshaller.marshal(portalJob, new StreamResult(new ByteArrayOutputStream()));
        marshaller.marshal(portalManifest, new StreamResult(new ByteArrayOutputStream()));
    }

    @Test
    public void invalid_signature_job_request_causes_exceptions() {
        XMLExitUrls exitUrls = new XMLExitUrls()
                .withCompletionUrl(null)
                .withRejectionUrl(URI.create("http://localhost/rejected"))
                .withErrorUrl(URI.create("http://localhost/failed"));

        XMLDirectSignatureJobRequest signatureJobRequest = new XMLDirectSignatureJobRequest("123abc", exitUrls, WAIT_FOR_CALLBACK, null);

        MarshallingFailureException thrown = assertThrows(MarshallingFailureException.class,
                () -> marshaller.marshal(signatureJobRequest, new StreamResult(new ByteArrayOutputStream())));

        assertThat(thrown.getMessage(), allOf(containsString("completion-url"), containsString("is expected")));
    }

    @Test
    public void invalid_manifest_causes_exceptions() {
        XMLSender sender = new XMLSender().withOrganizationNumber("123456789");
        XMLPortalSigner portalSigner = new XMLPortalSigner().withPersonalIdentificationNumber("12345678910");
        XMLDirectSigner directSigner = new XMLDirectSigner().withPersonalIdentificationNumber("12345678910");
        XMLPortalDocument portalDocument = new XMLPortalDocument("Title", null, "application/pdf");
        XMLDirectDocument directDocument = new XMLDirectDocument("Title", null, null, "application/pdf");

        XMLDirectSignatureJobManifest directManifest = new XMLDirectSignatureJobManifest(asList(directSigner), sender, directDocument, FOUR, PERSONAL_IDENTIFICATION_NUMBER_AND_NAME);
        XMLPortalSignatureJobManifest portalManifest = new XMLPortalSignatureJobManifest(asList(portalSigner), sender, null, "Title", "nonsensitive title", "Description", asList(portalDocument) , FOUR, new XMLAvailability(), PERSONAL_IDENTIFICATION_NUMBER_AND_NAME);


        MarshallingFailureException directManifestMarshallingFailure =
                assertThrows(MarshallingFailureException.class, () -> marshaller.marshal(directManifest, new StreamResult(new ByteArrayOutputStream())));

        MarshallingFailureException portalManifestMarshallingFailure =
                assertThrows(MarshallingFailureException.class, () -> marshaller.marshal(portalManifest, new StreamResult(new ByteArrayOutputStream())));

        assertThat(directManifestMarshallingFailure, where(Exception::getMessage, allOf(containsString("href"), containsString("must appear"))));
        assertThat(portalManifestMarshallingFailure, where(Exception::getMessage, allOf(containsString("signature-type"), containsString("notifications-using-lookup"), containsString("notifications"))));

    }

    @Test
    public void response_unmarshaller_ignores_unexpected_elements_in_response() throws Exception {
        XMLDirectSignatureJobStatusResponse unmarshalled;
        try (InputStream responseWithUnknownElement = getClass().getResourceAsStream("/xml/direct_signature_job_response_with_unexpected_element.xml")) {
            unmarshalled = (XMLDirectSignatureJobStatusResponse) SignatureJaxb2Marshaller.ForResponsesOfAllApis.singleton()
                    .unmarshal(new StreamSource(getClass().getResourceAsStream("/xml/direct_signature_job_response_with_unexpected_element.xml")));
        }

        assertThat(unmarshalled, where(XMLDirectSignatureJobStatusResponse::getSignatureJobId, is(1L)));
        assertThat(unmarshalled.getStatuses(), contains(where(XMLSignerStatus::getValue, sameInstance(SIGNED))));
    }

}
