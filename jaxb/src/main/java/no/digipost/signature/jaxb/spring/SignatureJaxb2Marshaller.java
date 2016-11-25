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

import no.digipost.signature.jaxb.SignatureMarshalling;
import no.digipost.signature.xsd.SignatureApiSchemas;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.util.Collections;
import java.util.Set;

import static no.digipost.signature.jaxb.spring.SpringUtils.asClassPathResources;

/**
 * Various Spring {@link Jaxb2Marshaller}s which are configured for marshalling and/or unmarshalling
 * request and response messages for the Posten signering API. To use this marshaller, make sure you have both
 * <a href="http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.springframework%20a%3Aspring-oxm">Spring OXM</a> and
 * <a href="http://search.maven.org/#search%7Cga%7C1%7Cg%3Ano.digipost.signature%20a%3Asignature-api-specification">Posten signering - API Schema</a>
 * on your classpath.
 * <p>
 * Instances of SignatureJaxb2Marshaller behaves as expected when managed by a Spring DI container, following the pattern of an
 * {@link org.springframework.beans.factory.InitializingBean InitializingBean}, meaning that {@link #afterPropertiesSet()} will properly
 * initialize after it is constructed and configured.
 * <p>
 * Using one of the various static factory methods for retrieving singleton instances does <em>not</em> require any call to
 * {@link #afterPropertiesSet()}. Each factory method will ensure that the marshaller is instantiated only on the first invocation of this method,
 * and repeated invokations will always return the same instance, which will be ready to use immediately.
 *
 * @see SignatureJaxb2Marshaller.ForResponsesOfAllApis
 * @see SignatureJaxb2Marshaller.ForRequestsOfAllApis
 */
public abstract class SignatureJaxb2Marshaller extends Jaxb2Marshaller {



    /**
     * Marshaller for creating requests for both the Direct and Portal API.
     * This marshaller will validate if the XML generated from marshalled Java objects
     * adheres to the API schemas.
     */
    public static class ForRequestsOfAllApis extends SignatureJaxb2Marshaller {

        public static Jaxb2Marshaller singleton() {
            return SingletonHolder.instance;
        }

        private static final class SingletonHolder {
            private static final SignatureJaxb2Marshaller instance = new SignatureJaxb2Marshaller.ForRequestsOfAllApis().triggerBeanInitialized();
        }

        public ForRequestsOfAllApis() {
            super(SignatureMarshalling.allApiRequestClasses(), asClassPathResources(SignatureApiSchemas.DIRECT_AND_PORTAL_API));
        }
    }


    /**
     * Marshaller (unmarshaller) for reading responses from both the Direct and Portal API.
     * This marshaller does <em>not</em> validate the XML before attempting to unmarshal it to Java objects.
     */
    public static class ForResponsesOfAllApis extends SignatureJaxb2Marshaller {

        public static Jaxb2Marshaller singleton() {
            return SingletonHolder.instance;
        }

        private static final class SingletonHolder {
            private static final SignatureJaxb2Marshaller instance = new SignatureJaxb2Marshaller.ForResponsesOfAllApis().triggerBeanInitialized();
        }

        public ForResponsesOfAllApis() {
            super(SignatureMarshalling.allApiResponseClasses(), Collections.<Resource>emptySet());
        }
    }



    /**
     * Marshaller for both the Direct and Portal API.
     * <p><strong>
     *  This marshaller is intended only to be used internally by Posten signering, as both
     *  requests and responses are validated against schemas. A 3rd party client should <em>not</em>
     *  validate responses from the API.
     * </strong>
     *
     *
     * @see SignatureJaxb2Marshaller.ForResponsesOfAllApis
     * @see SignatureJaxb2Marshaller.ForRequestsOfAllApis
     */
    public static class ForAllApis extends SignatureJaxb2Marshaller {

        public static Jaxb2Marshaller singleton() {
            return SingletonHolder.instance;
        }

        private static final class SingletonHolder {
            private static final SignatureJaxb2Marshaller instance = new SignatureJaxb2Marshaller.ForAllApis().triggerBeanInitialized();
        }

        public ForAllApis() {
            super(SignatureMarshalling.allApiClasses(), asClassPathResources(SignatureApiSchemas.DIRECT_AND_PORTAL_API));
        }
    }




    private final Set<? extends Resource> schemas;

    protected SignatureJaxb2Marshaller(Set<Class<?>> classesToBeBound, Set<? extends Resource> schemasForValidation) {
        this.schemas = schemasForValidation;
        setClassesToBeBound(classesToBeBound.toArray(new Class[classesToBeBound.size()]));
        if (schemas != null && !schemas.isEmpty()) {
            setSchemas(schemas.toArray(new Resource[schemas.size()]));
        }
    }

    @Override
    public void afterPropertiesSet() {
        try {
            super.afterPropertiesSet();
        } catch (Exception e) {
            throw new IllegalStateException("Initializing " + Jaxb2Marshaller.class.getSimpleName() + " failed because " +
                                            e.getClass().getSimpleName() + ": '" + e.getMessage() + "'. " +
                                            "ClassesToBeBound=" + getClassesToBeBound() + ", schemas=" + schemas + ". " +
                                            "Do you have both Spring OXM and the signature-api-specification on your classpath?");
        }
    }

    SignatureJaxb2Marshaller triggerBeanInitialized() {
        afterPropertiesSet();
        return this;
    }
}