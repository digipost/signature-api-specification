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
package no.digipost.signature.xsd.spring;

import no.digipost.signature.api.xml.XMLAvailability;
import no.digipost.signature.api.xml.XMLDirectDocument;
import no.digipost.signature.api.xml.XMLDirectSignatureJobManifest;
import no.digipost.signature.api.xml.XMLDirectSignatureJobRequest;
import no.digipost.signature.api.xml.XMLDirectSigner;
import no.digipost.signature.api.xml.XMLExitUrls;
import no.digipost.signature.api.xml.XMLPortalDocument;
import no.digipost.signature.api.xml.XMLPortalSignatureJobManifest;
import no.digipost.signature.api.xml.XMLPortalSigner;
import no.digipost.signature.api.xml.XMLPortalSigners;
import no.digipost.signature.api.xml.XMLSender;
import no.digipost.signature.api.xml.XMLStatusRetrievalMethod;
import no.digipost.signature.jaxb.spring.SignatureJaxb2Marshaller;
import org.junit.Test;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.transform.stream.StreamResult;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import static java.util.Collections.singletonList;
import static no.digipost.signature.api.xml.XMLAuthenticationLevel.FOUR;
import static no.digipost.signature.api.xml.XMLAuthenticationLevel.THREE;
import static no.digipost.signature.api.xml.XMLStatusRetrievalMethod.WAIT_FOR_CALLBACK;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SignatureJaxb2MarshallerTest {

    private final Jaxb2Marshaller marshaller = SignatureJaxb2Marshaller.marshallerSingleton();

    @Test
    public void valid_objects_can_be_marshalled() {
        XMLDirectSignatureJobRequest signatureJobRequest = new XMLDirectSignatureJobRequest("ref", new XMLExitUrls("http://completion", "http://rejection", "http://error"), XMLStatusRetrievalMethod.WAIT_FOR_CALLBACK);
        XMLDirectSignatureJobManifest manifest = new XMLDirectSignatureJobManifest(singletonList(new XMLDirectSigner().withSignerIdentifier("a-signer-reference")), new XMLSender("123456789"), new XMLDirectDocument("Title", "Description", THREE, "doc.pdf", "application/pdf"));

        marshaller.marshal(signatureJobRequest, new StreamResult(new ByteArrayOutputStream()));
        marshaller.marshal(manifest, new StreamResult(new ByteArrayOutputStream()));
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
        XMLPortalDocument portalDocument = new XMLPortalDocument("Title", "nonsensitive title", "Description", FOUR, null, "application/pdf");
        XMLDirectDocument directDocument = new XMLDirectDocument("Title", "Description", THREE, null, "application/pdf");
        XMLDirectSignatureJobManifest directManifest = new XMLDirectSignatureJobManifest(Arrays.asList(directSigner), sender, directDocument);
        XMLPortalSignatureJobManifest portalManifest = new XMLPortalSignatureJobManifest(new XMLPortalSigners().withSigners(portalSigner), sender, portalDocument, new XMLAvailability());

        try {
            marshaller.marshal(directManifest, new StreamResult(new ByteArrayOutputStream()));
            marshaller.marshal(portalManifest, new StreamResult(new ByteArrayOutputStream()));
            fail("Should have failed with XSD-validation error due to href-attribute on document element being empty.");
        } catch (MarshallingFailureException e) {
            assertThat(e.getMessage(), allOf(containsString("href"), containsString("must appear")));
        }
    }

}
