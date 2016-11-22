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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A Spring {@link Jaxb2Marshaller} which is configured for marshalling and unmarshalling
 * request and response messages for Posten signering. To use this marshaller, make sure you have both
 * <a href="http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.springframework%20a%3Aspring-oxm">Spring OXM</a> and
 * <a href="http://search.maven.org/#search%7Cga%7C1%7Cg%3Ano.digipost.signature%20a%3Asignature-api-specification">Posten signering - API Schema</a>
 * on your classpath.
 */
public class SignatureJaxb2Marshaller extends Jaxb2Marshaller {

    /**
     * Get a singleton instance of the {@link Jaxb2Marshaller} for the Posten signering API. This method will ensure that the marshaller
     * is instantiated only on the first invocation of this method, and repeated invokations will always return the same instance.
     * If you have other requirements for controlling the creation of the marshaller, you can consider instantiating
     * the {@link SignatureJaxb2Marshaller} yourself.
     */
    public static Jaxb2Marshaller marshallerSingleton() {
        return SingletonHolder.instance;
    }

    private static final class SingletonHolder {
        private static final SignatureJaxb2Marshaller instance = new SignatureJaxb2Marshaller();
        static {
            instance.afterPropertiesSet();
        }
    }



    private static final Resource[] ALL_SCHEMAS;
    static {
        Set<Resource> schemas = new LinkedHashSet<>();
        for (String schemaResourceName : SignatureApiSchemas.DIRECT_AND_PORTAL_API) {
            schemas.add(new ClassPathResource(schemaResourceName));
        }
        ALL_SCHEMAS = schemas.toArray(new Resource[schemas.size()]);
    }

    public SignatureJaxb2Marshaller() {
        Set<Class<?>> classesToBeBound = SignatureMarshalling.directAndPortalApiClasses();
        setClassesToBeBound(classesToBeBound.toArray(new Class[classesToBeBound.size()]));
        setSchemas(ALL_SCHEMAS);
    }

    @Override
    public void afterPropertiesSet() {
        try {
            super.afterPropertiesSet();
        } catch (Exception e) {
            throw new IllegalStateException("Initializing " + Jaxb2Marshaller.class.getSimpleName() + " failed because " +
                                            e.getClass().getSimpleName() + ": '" + e.getMessage() + "'. " +
                                            "ClassesToBeBound=" + getClassesToBeBound() + ", schemas=" + ALL_SCHEMAS + ". " +
                                            "Do you have both Spring OXM and the signature-api-specification on your classpath?");
        }
    }
}