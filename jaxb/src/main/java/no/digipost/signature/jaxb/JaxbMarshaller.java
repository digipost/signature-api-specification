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

import no.digipost.signature.xsd.SignatureApiSchemas;
import no.digipost.xml.bind.MarshallingCustomization;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static no.digipost.xml.bind.MarshallingCustomization.validateUsingSchemaResources;

/**
 * @see JaxbMarshaller.ForResponsesOfAllApis
 * @see JaxbMarshaller.ForRequestsOfAllApis
 */
public class JaxbMarshaller {

    /**
     * Marshaller for creating requests for both the Direct and Portal API.
     * This marshaller will validate if the XML generated from marshalled Java objects
     * adheres to the API schemas.
     */
     public static class ForRequestsOfAllApis extends JaxbMarshaller {

        public static JaxbMarshaller singleton() {
            return SingletonHolder.instance;
        }

        private static final class SingletonHolder {
            private static final JaxbMarshaller instance =  new JaxbMarshaller.ForRequestsOfAllApis();
        }

        public ForRequestsOfAllApis() {
            super(validateUsingSchemaResources(SignatureApiSchemas.DIRECT_AND_PORTAL_API), SignatureMarshalling.allApiRequestClasses());
        }
    }


    /**
     * Marshaller (unmarshaller) for reading responses from both the Direct and Portal API.
     * This marshaller does <em>not</em> validate the XML before attempting to unmarshal it to Java objects.
     */
    public static class ForResponsesOfAllApis extends JaxbMarshaller {

        public static JaxbMarshaller singleton() {
            return SingletonHolder.instance;
        }

        private static final class SingletonHolder {
            private static final JaxbMarshaller instance = new JaxbMarshaller.ForResponsesOfAllApis();
        }

        public ForResponsesOfAllApis() {
            super(MarshallingCustomization.NO_CUSTOMIZATION, SignatureMarshalling.allApiResponseClasses());
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
     * @see JaxbMarshaller.ForResponsesOfAllApis
     * @see JaxbMarshaller.ForRequestsOfAllApis
     */
    public static class ForAllApis extends JaxbMarshaller {

        public static JaxbMarshaller singleton() {
            return SingletonHolder.instance;
        }

        private static final class SingletonHolder {
            private static final JaxbMarshaller instance = new JaxbMarshaller.ForAllApis();
        }

        public ForAllApis() {
            super(validateUsingSchemaResources(SignatureApiSchemas.DIRECT_AND_PORTAL_API), SignatureMarshalling.allApiClasses());
        }
    }

    private final JAXBContext jaxbContext;
    private final MarshallingCustomization marshallingCustomization;
    private final SaxParserProvider saxParserProvider;

    public JaxbMarshaller(MarshallingCustomization marshallingCustomization, Class<?> ... classesToBeBound) {
        this.jaxbContext = JaxbUtils.initContext(classesToBeBound);
        this.marshallingCustomization = marshallingCustomization;
        this.saxParserProvider = SaxParserProvider.createSecuredParserFactory();
    }

    public JaxbMarshaller(MarshallingCustomization marshallingCustomization, Set<Class<?>> classesToBeBound) {
        this(marshallingCustomization, classesToBeBound.toArray(new Class[classesToBeBound.size()]));
    }

    public JaxbMarshaller(Class<?> ... classesToBeBound) {
        this(MarshallingCustomization.NO_CUSTOMIZATION, classesToBeBound);
    }

    public JaxbMarshaller(Set<Class<?>> classesToBeBound) {
        this(MarshallingCustomization.NO_CUSTOMIZATION, classesToBeBound);
    }

    /**
     * @deprecated Use {@link #JaxbMarshaller(MarshallingCustomization, Set)} with
     *             {@link MarshallingCustomization#validateUsingSchemaResources(Set)}
     */
    @Deprecated
    public JaxbMarshaller(Set<Class<?>> classesToBeBound, Set<String> schemaResources) {
        this(MarshallingCustomization.validateUsingSchemaResources(schemaResources), classesToBeBound);
    }


    public String marshalToString(Object object) {
        return marshalToResult(object, xml -> xml.toString(UTF_8.name()));
    }

    public byte[] marshalToBytes(Object object) {
        return marshalToResult(object, ByteArrayOutputStream::toByteArray);
    }

    public Document marshalToDomDocument(Object object) {
        DOMResult domResult = new DOMResult();
        doWithMarshaller(object, (o, marshaller) -> marshaller.marshal(o, domResult));
        return (Document) domResult.getNode();
    }

    public void marshal(Object object, OutputStream outputStream) {
        doWithMarshaller(object, (o, marshaller) -> marshaller.marshal(o, outputStream));
    }


    private <R> R marshalToResult(Object object, ThrowingFunction<? super ByteArrayOutputStream, ? extends R> outputStreamMapper) {
        try (ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream(128)) {
            marshal(object, xmlOutputStream);
            return outputStreamMapper.apply(xmlOutputStream);
        } catch (SignatureMarshalException marshalException) {
            throw marshalException;
        } catch (Exception e) {
            throw SignatureMarshalException.failedMarshal(object, e);
        }
    }

    @FunctionalInterface
    private interface ThrowingFunction<T, R> {
        R apply(T t) throws Exception;
    }

    @FunctionalInterface
    private interface ThrowingBiConsumer<T, S> {
        void accept(T t, S s) throws Exception;
    }

    private <T> void doWithMarshaller(T object, ThrowingBiConsumer<? super T, ? super Marshaller> operation) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshallingCustomization.customize(marshaller);
            operation.accept(object, marshaller);
        } catch (Exception e) {
            throw SignatureMarshalException.failedMarshal(object, e);
        }
    }

    public <T> T unmarshal(InputStream inputStream, Class<T> type) {
        Source xmlSource = new SAXSource(saxParserProvider.createXMLReader(), new InputSource(inputStream));
        return unmarshal(unmarshaller -> unmarshaller.unmarshal(xmlSource), type);
    }

    public <T> T unmarshal(byte[] bytes, Class<T> type) {
        return unmarshal(new ByteArrayInputStream(bytes), type);
    }

    public <T> T unmarshal(Node node, Class<T> type) {
        return unmarshal(unmarshaller -> unmarshaller.unmarshal(node), type);
    }

    private <T> T unmarshal(ThrowingFunction<? super Unmarshaller, ?> operation, Class<T> type) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            marshallingCustomization.customize(unmarshaller);
            return type.cast(operation.apply(unmarshaller));
        } catch (Exception e) {
            throw SignatureMarshalException.failedUnmarshal(type, e);
        }
    }

}
