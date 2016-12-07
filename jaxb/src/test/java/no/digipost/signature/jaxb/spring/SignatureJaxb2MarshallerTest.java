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

import no.digipost.signature.api.xml.*;
import org.junit.Test;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static no.digipost.signature.api.xml.XMLAuthenticationLevel.FOUR;
import static no.digipost.signature.api.xml.XMLAuthenticationLevel.THREE;
import static no.digipost.signature.api.xml.XMLStatusRetrievalMethod.WAIT_FOR_CALLBACK;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SignatureJaxb2MarshallerTest {

    private final Jaxb2Marshaller marshaller = SignatureJaxb2Marshaller.ForAllApis.singleton();

    @Test
    public void valid_objects_can_be_marshalled() {
        XMLSender sender = new XMLSender().withOrganizationNumber("123456789");
        XMLPortalSigner portalSigner = new XMLPortalSigner().withPersonalIdentificationNumber("12345678910").withNotificationsUsingLookup(new XMLNotificationsUsingLookup().withEmail(new XMLEnabled()));
        XMLDirectSigner directSigner = new XMLDirectSigner().withPersonalIdentificationNumber("12345678910");
        XMLPortalDocument portalDocument = new XMLPortalDocument("Title", "Non-sensitive title", "Message", "document.pdf", "application/pdf");
        XMLDirectDocument directDocument = new XMLDirectDocument("Title", "Message", "document.pdf", "application/pdf");
        XMLExitUrls exitUrls = new XMLExitUrls()
                .withCompletionUrl("http://localhost/signed")
                .withRejectionUrl("http://localhost/rejected")
                .withErrorUrl("http://localhost/failed");

        XMLDirectSignatureJobRequest directJob = new XMLDirectSignatureJobRequest("123abc", exitUrls, null);
        XMLDirectSignatureJobManifest directManifest = new XMLDirectSignatureJobManifest(Arrays.asList(directSigner), sender, directDocument, THREE);

        marshaller.marshal(directJob, new StreamResult(new ByteArrayOutputStream()));
        marshaller.marshal(directManifest, new StreamResult(new ByteArrayOutputStream()));

        XMLPortalSignatureJobRequest portalJob = new XMLPortalSignatureJobRequest("123abc");
        XMLPortalSignatureJobManifest portalManifest = new XMLPortalSignatureJobManifest(Arrays.asList(portalSigner), sender, portalDocument, FOUR, new XMLAvailability().withActivationTime(new Date()));
        marshaller.marshal(portalJob, new StreamResult(new ByteArrayOutputStream()));
        marshaller.marshal(portalManifest, new StreamResult(new ByteArrayOutputStream()));
    }

    @Test
    public void invalid_signature_job_request_causes_exceptions() {
        XMLExitUrls exitUrls = new XMLExitUrls()
                .withCompletionUrl(null)
                .withRejectionUrl("http://localhost/rejected")
                .withErrorUrl("http://localhost/failed");

        XMLDirectSignatureJobRequest signatureJobRequest = new XMLDirectSignatureJobRequest("123abc", exitUrls, WAIT_FOR_CALLBACK);

        try {
            marshaller.marshal(signatureJobRequest, new StreamResult(new ByteArrayOutputStream()));
            fail("Should have failed with XSD-validation error due to completion-url being empty.");
        } catch (MarshallingFailureException e) {
            assertThat(e.getMessage(), allOf(containsString("completion-url"), containsString("is expected")));
        }
    }

    @Test
    public void invalid_manifest_causes_exceptions() {
        XMLSender sender = new XMLSender().withOrganizationNumber("123456789");
        XMLPortalSigner portalSigner = new XMLPortalSigner().withPersonalIdentificationNumber("12345678910");
        XMLDirectSigner directSigner = new XMLDirectSigner().withPersonalIdentificationNumber("12345678910");
        XMLPortalDocument portalDocument = new XMLPortalDocument("Title", "nonsensitive title", "Description", null, "application/pdf");
        XMLDirectDocument directDocument = new XMLDirectDocument("Title", "Description", null, "application/pdf");

        XMLDirectSignatureJobManifest directManifest = new XMLDirectSignatureJobManifest(Arrays.asList(directSigner), sender, directDocument, FOUR);
        XMLPortalSignatureJobManifest portalManifest = new XMLPortalSignatureJobManifest(Arrays.asList(portalSigner), sender, portalDocument, FOUR, new XMLAvailability());

        try {
            marshaller.marshal(directManifest, new StreamResult(new ByteArrayOutputStream()));
            marshaller.marshal(portalManifest, new StreamResult(new ByteArrayOutputStream()));
            fail("Should have failed with XSD-validation error due to href-attribute on document element being empty.");
        } catch (MarshallingFailureException e) {
            assertThat(e.getMessage(), allOf(containsString("href"), containsString("must appear")));
        }
    }

    @Test
    public void response_unmarshaller_ignores_unexpected_elements_in_response() throws Exception {
        XMLDirectSignatureJobStatusResponse unmarshalled;
        try (InputStream responseWithUnknownElement = getClass().getResourceAsStream("/xml/direct_signature_job_response_with_unexpected_element.xml")) {
            unmarshalled = (XMLDirectSignatureJobStatusResponse) SignatureJaxb2Marshaller.ForResponsesOfAllApis.singleton()
                    .unmarshal(new StreamSource(getClass().getResourceAsStream("/xml/direct_signature_job_response_with_unexpected_element.xml")));
        }

        assertEquals(1, unmarshalled.getSignatureJobId());
        assertEquals("SIGNED", unmarshalled.getStatuses().get(0).getValue());
    }

}
