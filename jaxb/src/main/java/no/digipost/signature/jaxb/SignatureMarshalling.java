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
import no.digipost.signature.api.xml.XMLDirectSignerResponse;
import no.digipost.signature.api.xml.XMLDirectSignerUpdateRequest;
import no.digipost.signature.api.xml.XMLError;
import no.digipost.signature.api.xml.XMLPortalSignatureJobManifest;
import no.digipost.signature.api.xml.XMLPortalSignatureJobRequest;
import no.digipost.signature.api.xml.XMLPortalSignatureJobResponse;
import no.digipost.signature.api.xml.XMLPortalSignatureJobStatusChangeResponse;
import no.digipost.signature.api.xml.thirdparty.asice.XAdESSignatures;
import no.digipost.signature.api.xml.thirdparty.xades.QualifyingProperties;
import no.digipost.signature.jaxb.spring.SignatureJaxb2Marshaller;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

/**
 * Different sets of classes to be bound by a {@link javax.xml.bind.JAXBContext JAXB context} in order to
 * create marshallers and unmarshallers. All the sets are immutable.
 * <p>
 * If you use Spring, there are ready preconfigured {@link org.springframework.oxm.jaxb.Jaxb2Marshaller}s
 * available in {@link SignatureJaxb2Marshaller}.
 */
public final class SignatureMarshalling {

    /**
     * All classes necessary for a {@code JAXBContext} to handle marshalling and unmarshalling
     * <em>requests and reponses</em> for both the Direct- and Portal API.
     */
    public static Set<Class<?>> allApiClasses() {
        return unionOf(allApiRequestClasses(), allApiResponseClasses());
    }

    /**
     * All classes necessary for a {@code JAXBContext} to handle marshalling <em>requests</em> to both the Direct and Portal API.
     */
    public static Set<Class<?>> allApiRequestClasses() {
        return unionOf(commonJaxbClassesForRequests(), directApiJaxbClassesForRequests(), portalApiJaxbClassesForRequests());
    }

    /**
     * All classes necessary for a {@code JAXBContext} to handle unmarshalling <em>responses</em> from both the Direct and Portal API.
     */
    public static Set<Class<?>> allApiResponseClasses() {
        return unionOf(commonJaxbClassesForResponses(), directApiJaxbClassesForResponses(), portalApiJaxbClassesForResponses());
    }


    /**
     * All classes necessary for a {@code JAXBContext} to handle marshalling <em>requests</em> to the Direct API.
     */
    public static Set<Class<?>> directApiJaxbClassesForRequests() {
        return unionOf(commonJaxbClassesForRequests(), XMLDirectSignatureJobRequest.class, XMLDirectSignerUpdateRequest.class, XMLDirectSignatureJobManifest.class);
    }

    /**
     * All classes necessary for a {@code JAXBContext} to handle unmarshalling <em>responses</em> from the Direct API.
     */
    public static Set<Class<?>> directApiJaxbClassesForResponses() {
        return unionOf(commonJaxbClassesForResponses(), XMLDirectSignatureJobResponse.class, XMLDirectSignerResponse.class, XMLDirectSignatureJobStatusResponse.class);
    }

    /**
     * All classes necessary for a {@code JAXBContext} to handle marshalling <em>requests</em> to the Portal API.
     */
    public static Set<Class<?>> portalApiJaxbClassesForRequests() {
        return unionOf(commonJaxbClassesForRequests(), XMLPortalSignatureJobManifest.class, XMLPortalSignatureJobRequest.class);
    }

    /**
     * All classes necessary for a {@code JAXBContext} to handle unmarshalling <em>responses</em> from the Portal API.
     */
    public static Set<Class<?>> portalApiJaxbClassesForResponses() {
        return unionOf(commonJaxbClassesForResponses(), XMLPortalSignatureJobResponse.class, XMLPortalSignatureJobStatusChangeResponse.class);
    }


    private static Set<Class<?>> commonJaxbClassesForResponses() {
        return Collections.<Class<?>>singleton(XMLError.class);
    }

    private static Set<Class<?>> commonJaxbClassesForRequests() {
        return unionOf(Collections.<Class<?>>emptySet(), QualifyingProperties.class, XAdESSignatures.class);
    }

    @SafeVarargs
    private static Set<Class<?>> unionOf(Collection<Class<?>> set, Class<?> ... more) {
        return unionOf(set, asList(more));
    }

    @SafeVarargs
    private static Set<Class<?>> unionOf(Collection<Class<?>> set1, Collection<Class<?>> ... moreSets) {
        LinkedHashSet<Class<?>> union = new LinkedHashSet<>(set1);
        for (Collection<Class<?>> set : moreSets) {
            union.addAll(set);
        }
        return unmodifiableSet(union);
    }


    private SignatureMarshalling() {}
}
