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
package no.digipost.signature.jaxb;

import no.digipost.signature.api.xml.XMLDirectSignatureJobManifest;
import no.digipost.signature.api.xml.XMLDirectSignatureJobRequest;
import no.digipost.signature.api.xml.XMLDirectSignatureJobResponse;
import no.digipost.signature.api.xml.XMLDirectSignatureJobStatusResponse;
import no.digipost.signature.api.xml.XMLError;
import no.digipost.signature.api.xml.XMLPortalSignatureJobManifest;
import no.digipost.signature.api.xml.XMLPortalSignatureJobRequest;
import no.digipost.signature.api.xml.XMLPortalSignatureJobResponse;
import no.digipost.signature.api.xml.XMLPortalSignatureJobStatusChangeResponse;
import no.digipost.signature.api.xml.thirdparty.asice.XAdESSignatures;
import no.digipost.signature.api.xml.thirdparty.xades.QualifyingProperties;
import no.digipost.signature.jaxb.spring.SignatureJaxb2Marshaller;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

/**
 * Sets of classes to be bound by a {@link javax.xml.bind.JAXBContext JAXB context} in order to
 * create marshallers and unmarshallers.
 * <p>
 * If you use Spring, there is a ready preconfigured {@link org.springframework.oxm.jaxb.Jaxb2Marshaller}
 * available in {@link SignatureJaxb2Marshaller}.
 */
public final class SignatureMarshalling {

    /**
     * All classes necessary for a {@code JAXBContext} for both the Direct- and Portal API
     */
    public static Set<Class<?>> directAndPortalApiClasses() {
        LinkedHashSet<Class<?>> directAndPortal = new LinkedHashSet<>(directApiJaxbClasses());
        directAndPortal.addAll(portalApiJaxbClasses());
        return unmodifiableSet(directAndPortal);
    }

    /**
     * All classes necessary for a {@code JAXBContext} for the Direct API
     */
    public static Set<Class<?>> directApiJaxbClasses() {
        LinkedHashSet<Class<?>> directApiJaxbClasses = new LinkedHashSet<Class<?>>(asList(
                XMLDirectSignatureJobManifest.class, XMLDirectSignatureJobRequest.class,
                XMLDirectSignatureJobResponse.class, XMLDirectSignatureJobStatusResponse.class));
        directApiJaxbClasses.addAll(commonJaxbClasses());
        return unmodifiableSet(directApiJaxbClasses);
    }

    /**
     * All classes necessary for a {@code JAXBContext} for the Portal API
     */
    public static Set<Class<?>> portalApiJaxbClasses() {
        LinkedHashSet<Class<?>> portalApiJaxbClasses = new LinkedHashSet<Class<?>>(asList(
                XMLPortalSignatureJobManifest.class, XMLPortalSignatureJobRequest.class,
                XMLPortalSignatureJobResponse.class, XMLPortalSignatureJobStatusChangeResponse.class));
        portalApiJaxbClasses.addAll(commonJaxbClasses());
        return unmodifiableSet(portalApiJaxbClasses);
    }


    private static Set<Class<?>> commonJaxbClasses() {
        return unmodifiableSet(new LinkedHashSet<Class<?>>(asList(XMLError.class, QualifyingProperties.class, XAdESSignatures.class)));
    }

    private SignatureMarshalling() {}

}
